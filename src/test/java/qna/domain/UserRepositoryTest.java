package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @AfterEach
    public void clear() {
        users.deleteAll();
    }

    @Test
    @DisplayName("유저 생성")
    void create() {
        // given
        User user = new User("hyeon9mak", "1234", "hyeon-gu", "hyeon9mak@email.com");

        // when
        User savedUser = users.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser).isSameAs(user);
    }

    @Test
    @DisplayName("유저 정보 수정")
    void update() {
        // given
        User user = users.save(new User("hyeon9mak", "1234", "hyeon-gu", "hyeon9mak@email.com"));
        User target = new User("hyeon9mak", "1234", "hyeon-gu", "newEmail@email.com");

        // when
        user.update(user, target);
        User foundUser = users.findByUserId(user.getUserId()).orElseThrow(NotFoundException::new);

        // then
        assertThat(foundUser).isSameAs(user);
        assertThat(user.getEmail()).isEqualTo(target.getEmail());
        assertThat(foundUser.getEmail()).isEqualTo(target.getEmail());
    }

    @Test
    @DisplayName("유저 삭제")
    void delete() {
        // given
        User user = users.save(new User("hyeon9mak", "1234", "hyeon-gu", "hyeon9mak@email.com"));

        // when
        users.delete(user);

        // then
        assertThat(users.findByUserId(user.getUserId())).isNotPresent();

    }
}