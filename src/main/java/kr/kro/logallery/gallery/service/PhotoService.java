package kr.kro.logallery.gallery.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import jakarta.transaction.Transactional;
import kr.kro.logallery.gallery.entity.Photo;
import kr.kro.logallery.gallery.respository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public void save(List<MultipartFile> photos) {
        for (MultipartFile photo : photos) {
            try {
                String fileName = photo.getOriginalFilename();

                Photo newPhoto = new Photo();

                // 메타데이터 읽기
                try{
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(photo.getBytes());
                    Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

                    // ExifSubIFDDirectory에서 생성 날짜 정보 추출
                    ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                    Date creationDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    newPhoto.setUploadDate(creationDate);

                    // GpsDirectory에서 GPS 정보 추출
                    GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                    if(gpsDirectory != null){
                        double latitude = gpsDirectory.getGeoLocation().getLatitude();
                        double longitude = gpsDirectory.getGeoLocation().getLongitude();

                        newPhoto.setGpsLatitude(latitude);
                        newPhoto.setGpsLongitude(longitude);
                    }else{
                        System.out.println("GPS not found");
                    }

                }catch (IOException e){
                    e.printStackTrace();
                } catch (ImageProcessingException e) {
                    throw new RuntimeException(e);
                }

                // S3에 업로드
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(photo.getContentType());
                byte[] bytes = IOUtils.toByteArray(photo.getInputStream());

                // BufferedImage로 이미지 읽기
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                BufferedImage bufferedImage = ImageIO.read(inputStream);

                // 이미지의 너비와 높이 얻기
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();

                // Amazon S3에 이미지 업로드
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
                byteArrayInputStream.close();

                // Photo 엔티티 생성 및 저장
                String url = amazonS3.getUrl(bucketName, fileName).toString();
                newPhoto.setUrl(url);
                newPhoto.setWidth(width);
                newPhoto.setHeight(height);
                newPhoto.setLikes(0);
                photoRepository.save(newPhoto);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Page<Photo> findPhotos(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return photoRepository.findAllByOrderByIdDesc(pageRequest);
    }

    public void delete(Long id) {
        photoRepository.deleteById(id);
    }

    public int increaseLikes(Long id) {
        photoRepository.increaseLike(id);
        return photoRepository.findLikeById(id);
    }
}
