package kr.kro.logallery.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private String url;

    @Column(nullable = true)
    private String thbUrl;

    @ManyToMany(mappedBy = "photos", fetch = FetchType.LAZY)
    private List<HashTag> hashTags = new ArrayList<>();
}
