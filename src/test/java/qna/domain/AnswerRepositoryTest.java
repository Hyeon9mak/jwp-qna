package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @AfterEach
    void tearDown() {
        answers.deleteAll();
    }

    @Test
    @DisplayName("답변글 생성")
    void create() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "나는 댕댕박사라네.");

        // when
        Answer savedAnswer = answers.save(answer);

        // then
        assertThat(savedAnswer.getId()).isNotNull();
        assertThat(savedAnswer).isSameAs(answer);
        assertThat(savedAnswer.getCreatedAt()).isAfter(beforeTime);
        assertThat(savedAnswer.isOwner(UserTest.JAVAJIGI)).isTrue();
    }
}