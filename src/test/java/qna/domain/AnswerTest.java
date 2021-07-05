package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotDeletedException;

public class AnswerTest {

    private Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    private Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @BeforeEach
    void setUp() {
        A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    }

    @Test
    @DisplayName("삭제 명령시 상태 변경")
    void deleteBy() {
        // when
        boolean beforeDelete = A1.isDeleted();
        A1.delete();
        boolean afterDelete = A1.isDeleted();

        // then
        assertThat(beforeDelete).isFalse();
        assertThat(afterDelete).isTrue();
    }

    @Test
    @DisplayName("작성자가 다른지 판별")
    void isOwner() {
        assertThat(A1.isNotOwner(UserTest.SANJIGI)).isTrue();
        assertThat(A1.isNotOwner(UserTest.JAVAJIGI)).isFalse();
    }

    @Test
    @DisplayName("아직 삭제되지 않은 답변의 경우 삭제기록 요청시 예외처리")
    void deleteHistoryException() {
        // given
        A1.delete();

        // when

        // then
        assertThat(A1.deleteHistory()).isNotNull();
        assertThatThrownBy(A2::deleteHistory)
            .isInstanceOf(NotDeletedException.class);
    }
}
