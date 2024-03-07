package kr.kro.logallery.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
