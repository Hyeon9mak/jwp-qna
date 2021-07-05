package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    private User writer;

    @BeforeEach
    void setUp() {
        writer = users.save(new User("hyeon9mak", "1234", "최현구", "hyeon9mak@email.com"));
    }

    @AfterEach
    void tearDown() {
        questions.deleteAll();
    }

    @Test
    @DisplayName("질문글 생성")
    void create() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now();
        Question question = new Question("여기가 어디죠?", "여기가 어딘가요?").writeBy(writer);

        // when
        Question savedQuestion = questions.save(question);

        // then
        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion).isSameAs(question);
        assertThat(savedQuestion.getCreatedAt()).isAfter(beforeTime);
        assertThat(savedQuestion.isDeleted()).isFalse();
        assertThat(savedQuestion.getWriter()).isSameAs(writer);
    }

    @Test
    @DisplayName("질문글 삭제시 delete 상태 반영")
    void update() throws CannotDeleteException {
        // given
        Question question = new Question("여기가 어디죠?", "여기가 어딘가요?").writeBy(writer);
        Question savedQuestion = questions.save(question);

        // when
        question.deleteBy(writer);
        Question foundQuestion = questions.findById(savedQuestion.getId())
            .orElseThrow(NotFoundException::new);

        // then
        assertThat(foundQuestion.isDeleted()).isTrue();
    }
}