package kr.kro.logallery.gallery.respository;

import kr.kro.logallery.gallery.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Page<Photo> findAllByOrderByIdDesc(Pageable pageable);
}
