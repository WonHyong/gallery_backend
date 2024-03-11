package kr.kro.logallery.gallery.respository;

import kr.kro.logallery.gallery.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, String> {

    HashTag findByTag(String tag);
}
