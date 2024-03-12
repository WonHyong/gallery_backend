package kr.kro.logallery.gallery.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {

    private Long photoId;
    private int width;
    private int height;
    private String url;
    private String thbUrl;
    private List<String> tags;
}
