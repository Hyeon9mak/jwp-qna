package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.user.User;

public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(3L, "jason", "password", "name", "jason@woowa.course");
    }

    @Test
    @DisplayName("유저 정보 변경")
    void update() {
        // given
        User newUser = new User("jason", "password", "newName", "newMail@woowa.course");

        // when
        user.update(user, newUser);

        // then
        assertThat(user.getEmail()).isEqualTo(newUser.getEmail());
    }
}
