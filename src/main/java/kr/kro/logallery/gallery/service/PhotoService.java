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
        for (MultipartFile photo : photos) {
            try {
                String fileName = photo.getOriginalFilename();

                // S3에 업로드
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(photo.getContentType());
                byte[] bytes = IOUtils.toByteArray(photo.getInputStream());
                InputStream inputStream = new ByteArrayInputStream(bytes);
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

                // Photo 엔티티 생성 및 저장
                String url = amazonS3.getUrl(bucketName, fileName).toString();
                Photo newPhoto = new Photo();
                newPhoto.setUrl(url);
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
}
