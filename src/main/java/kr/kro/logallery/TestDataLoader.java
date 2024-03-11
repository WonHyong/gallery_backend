package kr.kro.logallery;


import jakarta.annotation.PostConstruct;
import kr.kro.logallery.gallery.entity.HashTag;
import kr.kro.logallery.gallery.respository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataLoader {

    private final HashTagRepository hashTagRepository;

    @PostConstruct
    private void loadTestData() {
        clearAllData();

        HashTag tag1 = makeHashTag("고양이");
        HashTag tag2 = makeHashTag("사람");
        HashTag tag3 = makeHashTag("집");

        hashTagRepository.saveAll(List.of(tag1, tag2, tag3));


        //loadChatRoomData(10);
    }

    private void clearAllData() {
        hashTagRepository.deleteAll();
    }

    private HashTag makeHashTag(String tag) {
        return HashTag.builder()
                .tag(tag)
                .build();
    }


}
