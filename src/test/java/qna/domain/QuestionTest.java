package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.answer.Answer;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.exception.CannotDeleteException;
import qna.exception.NotDeletedException;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("삭제 명령시 상태 변경")
    void deleteBy() throws CannotDeleteException {
        // when
        boolean beforeDelete = question.isDeleted();
        question.deleteBy(UserTest.JAVAJIGI);
        boolean afterDelete = question.isDeleted();

        // then
        assertThat(beforeDelete).isFalse();
        assertThat(afterDelete).isTrue();
    }

    @Test
    @DisplayName("작성자가 아닐 때 삭제 명령시 예외처리")
    void deleteByException() {
        // then
        assertThatThrownBy(() -> question.deleteBy(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제시 다른 사람이 등록한 답변이 없을 경우 정상 삭제")
    void deleteAnswersByQuestion() throws CannotDeleteException {
        // given
        question.addAnswer(new Answer(UserTest.JAVAJIGI, question, "자문자답이다!"));

        // when
        question.deleteBy(UserTest.JAVAJIGI);

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 삭제시 다른 사람이 등록한 답변이 있을 경우 예외처리")
    void deleteAnswersByQuestionException() {
        // given
        question.addAnswer(new Answer(UserTest.SANJIGI, question, "와우!"));

        // then
        assertThatThrownBy(() -> question.deleteBy(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제된 질문 기록 요청")
    void deleteHistories() throws CannotDeleteException {
        // given
        question.deleteBy(UserTest.JAVAJIGI);

        // when
        List<DeleteHistory> histories = question.deleteHistories();

        // then
        assertThat(histories).isNotEmpty();
    }

    @Test
    @DisplayName("삭제되지 않은 질문에 대해 기록 요청시 예외처리")
    void deleteHistoriesException() {
        // then
        assertThatThrownBy(() -> question.deleteHistories())
            .isInstanceOf(NotDeletedException.class);
    }

}
