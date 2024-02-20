package kr.kro.logallery.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private String url;

    @Column(nullable = true)
    private String thbUrl;
}
