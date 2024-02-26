package kr.kro.logallery.gallery.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import jakarta.transaction.Transactional;
import kr.kro.logallery.gallery.entity.Photo;
import kr.kro.logallery.gallery.respository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public void save(List<MultipartFile> photos) {
        photos.stream().map((photo) -> {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(photo.getContentType());
            objectMetadata.setContentLength(photo.getSize());

            InputStream photoInputStream = null;
            try {
                photoInputStream = photo.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            PutObjectRequest putObjectRequest  = new PutObjectRequest(
                    bucketName,
                    photo.getOriginalFilename(),
                    photoInputStream,
                    objectMetadata
            );

            // Photo 엔티티 생성 및 저장
            byte[] bytes = new byte[0];
            try {
                bytes = IOUtils.toByteArray(photoInputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            InputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String url = amazonS3.getUrl(bucketName, photo.getOriginalFilename()).toString();
            Photo newPhoto = new Photo();
            newPhoto.setUrl(url);
            newPhoto.setWidth(bufferedImage.getWidth());
            newPhoto.setHeight(bufferedImage.getHeight());
            photoRepository.save(newPhoto);

            return amazonS3.putObject(putObjectRequest);
        });
    }

    public Page<Photo> findPhotos(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return photoRepository.findAllByOrderByIdDesc(pageRequest);
    }

    public void delete(Long id) {
        photoRepository.deleteById(id);
    }
}
