package kr.kro.logallery.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column
    private int width;

    @Column
    private int height;

    @Column
    private String url;

    @Column
    private String thbUrl;

    @Column
    private int likes;

    @Column(nullable = true)
    private double gpsLatitude;

    @Column(nullable = true)
    private double gpsLongitude;

    @Column(nullable = true)
    private Date uploadDate;


}
