package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    private Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @BeforeEach
    void setUp() {
        A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    }

    @Test
    @DisplayName("삭제 명령시 상태가 변경")
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
    @DisplayName("")
    void () {
        // given
        A1.isNotOwner();
        A1.deleteHistory();
        A1.delete();

        // when

        // then

    }
}
