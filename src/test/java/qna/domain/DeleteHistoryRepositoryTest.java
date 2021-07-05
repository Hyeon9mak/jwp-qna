package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @AfterEach
    void tearDown() {
        deleteHistories.deleteAll();
    }

    @Test
    @DisplayName("삭제 기록 생성")
    void create() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

        // when
        DeleteHistory savedDeleteHistory = deleteHistories.save(deleteHistory);

        // then
        assertThat(savedDeleteHistory.getId()).isNotNull();
        assertThat(savedDeleteHistory).isSameAs(deleteHistory);
        assertThat(savedDeleteHistory.getCreateDate()).isAfter(beforeTime);
    }
}