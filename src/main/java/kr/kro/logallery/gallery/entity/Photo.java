package kr.kro.logallery.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int width;

    @Column
    private int height;

    @Column
    private String url;

    @Column
    private String thbUrl;

    @ManyToMany(mappedBy = "photos", fetch = FetchType.LAZY)
    private List<HashTag> hashTags = new ArrayList<>();
  
    @Column
    private int likes;

    @Column(nullable = true)
    private double gpsLatitude;

    @Column(nullable = true)
    private double gpsLongitude;

    @Column(nullable = true)
    private Date uploadDate;

}
