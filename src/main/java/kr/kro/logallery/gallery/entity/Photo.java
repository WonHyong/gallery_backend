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

    @Column
    private double gpsLatitude;

    @Column
    private double gpsLongitude;

    @Column
    private Date uploadDate;


}
