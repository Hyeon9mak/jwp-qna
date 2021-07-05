package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.deletehistory.DeleteHistoryRepository;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @Autowired
    AnswerRepository answers;

    private User writer;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        writer = users.save(new User("hyeon9mak", "1234", "최현구", "hyeon9mak@email.com"));
        question = questions.save(new Question("여기가 어디죠?", "여기가 어딘가요?").writeBy(writer));
        answer = answers.save(new Answer(writer, question, "자문자답합니다."));
    }

    @AfterEach
    void tearDown() {
        deleteHistories.deleteAll();
    }

    @Test
    @DisplayName("질문 삭제 기록 생성")
    void createQuestionDeleteHistory() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now());

        // when
        DeleteHistory savedDeleteHistory = deleteHistories.save(deleteHistory);

        // then
        assertThat(savedDeleteHistory.getId()).isNotNull();
        assertThat(savedDeleteHistory).isSameAs(deleteHistory);
        assertThat(savedDeleteHistory.getCreateDate()).isAfter(beforeTime);
    }

    @Test
    @DisplayName("답변 삭제 기록 생성")
    void createAnswerDeleteHistory() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, answer.getId(), writer, LocalDateTime.now());

        // when
        DeleteHistory savedDeleteHistory = deleteHistories.save(deleteHistory);

        // then
        assertThat(savedDeleteHistory.getId()).isNotNull();
        assertThat(savedDeleteHistory).isSameAs(deleteHistory);
        assertThat(savedDeleteHistory.getCreateDate()).isAfter(beforeTime);
    }
}