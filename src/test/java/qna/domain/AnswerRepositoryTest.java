package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = users.save(new User("hyeon9mak", "1234", "최현구", "hyeon9mak@email.com"));
        question = questions.save(new Question("여기가 어디죠?", "여기가 어딘가요?").writeBy(writer));
    }

    @AfterEach
    void tearDown() {
        answers.deleteAll();
    }

    @Test
    @DisplayName("답변글 생성")
    void create() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now();
        Answer answer = new Answer(writer, question, "자문자답합니다.");

        // when
        Answer savedAnswer = answers.save(answer);

        // then
        assertThat(savedAnswer.getId()).isNotNull();
        assertThat(savedAnswer).isSameAs(answer);
        assertThat(savedAnswer.getCreatedAt()).isAfter(beforeTime);
        assertThat(savedAnswer.isNotOwner(writer)).isFalse();
    }

    @Test
    @DisplayName("답변글 삭제시 delete 상태 반영")
    void update() {
        // given
        Answer answer = new Answer(writer, question, "자문자답합니다.");
        Answer savedAnswer = answers.save(answer);

        // when
        answer.delete();
        Answer foundAnswer = answers.findById(savedAnswer.getId())
            .orElseThrow(NotFoundException::new);

        // then
        assertThat(foundAnswer.isDeleted()).isTrue();
    }
}