package kr.kro.logallery.gallery.respository;

import kr.kro.logallery.gallery.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Page<Photo> findAll(Pageable pageable);


    Page<Photo> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT p FROM Photo p JOIN p.hashTags t WHERE t.tag IN :tags GROUP BY p HAVING COUNT(DISTINCT t.tag) = :#{#tags.length}")
    Page<Photo> findAllByTagsContainsAll(Pageable pageable, String[] tags);

    @Modifying
    @Query("update Photo p set p.likes = p.likes+1 where p.id = :id")
    void increaseLike(@Param("id")Long id);

    @Query("select p.likes from Photo p where p.id = :id")
    int findLikeById(@Param("id") Long id);
}
