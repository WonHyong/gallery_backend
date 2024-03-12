package kr.kro.logallery.gallery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import kr.kro.logallery.gallery.dto.PhotoDTO;
import kr.kro.logallery.gallery.entity.HashTag;
import kr.kro.logallery.gallery.entity.Photo;
import kr.kro.logallery.gallery.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "사진 컨트롤러", description = "사진 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;

    @Operation(summary = "get all photos")
    @GetMapping
    public ResponseEntity getPhotos(Pageable pageable){

        Slice<Photo> result = photoService.getPage(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Upload photos")
    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("photo") MultipartFile photo){
        photoService.save(photo);

        return ResponseEntity.ok("저장성공");
    }

    @Operation(summary = "delete photos")
    @DeleteMapping("/{photoId}")
    public ResponseEntity delete(@PathVariable Long photoId){

        photoService.delete(photoId);

        return ResponseEntity.ok("삭제 성공");
    }

    @Operation(summary = "increase likes")
    @PutMapping("/{photoId}/likes")
    public ResponseEntity increaseLike(@PathVariable Long photoId){

        int updatedLike = photoService.increaseLikes(photoId);

        return new ResponseEntity<>(updatedLike, HttpStatus.OK);
    }

}
