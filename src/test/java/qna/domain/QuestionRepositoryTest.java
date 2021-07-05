package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @AfterEach
    void tearDown() {
        questions.deleteAll();
    }

    @Test
    @DisplayName("질문글 생성")
    void create() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now();
        Question question = new Question("여기가 어디죠?", "여기가 어딘가요?");

        // when
        Question savedQuestion = questions.save(question);

        // then
        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion).isSameAs(question);
        assertThat(savedQuestion.getCreatedAt()).isAfter(beforeTime);
        assertThat(savedQuestion.isDeleted()).isFalse();
    }
}