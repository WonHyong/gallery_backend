package kr.kro.logallery.gallery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import kr.kro.logallery.gallery.entity.Photo;
import kr.kro.logallery.gallery.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "사진 컨트롤러", description = "사진 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;

    @Operation(summary = "get all photos")
    @GetMapping
    public ResponseEntity getPhotos(@RequestParam int page,@RequestParam int size){

        Page<Photo> photoPage = photoService.findPhotos(page, size);

        List<Photo> photos = photoPage.getContent();

        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @Operation(summary = "Upload photos")
    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("photos") List<MultipartFile> photos){
        photoService.save(photos);

        return ResponseEntity.ok("저장성공");
    }

    @Operation(summary = "delete photos")
    @DeleteMapping("/{photoId}")
    public ResponseEntity delete(@PathVariable Long photoId){

        photoService.delete(photoId);

        return ResponseEntity.ok("삭제 성공");
    }

}
