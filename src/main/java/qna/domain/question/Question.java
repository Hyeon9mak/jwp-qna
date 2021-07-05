package qna.domain.question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import qna.domain.BaseEntity;
import qna.domain.ContentType;
import qna.domain.answer.Answer;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;
import qna.exception.CannotDeleteException;
import qna.exception.NotDeletedException;

@Entity
public class Question extends BaseEntity {

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        super(id);
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public void deleteBy(User loginUser) throws CannotDeleteException {
        validateQuestionOwner(loginUser);
        validateAnswersOwner(loginUser);
        this.deleted = true;
        answers.forEach(Answer::delete);
    }

    public List<DeleteHistory> deleteHistories() {
        if (isDeleted()) {
            List<DeleteHistory> deleteHistories = deleteAnswerHistories();
            deleteHistories.add(new DeleteHistory(ContentType.QUESTION, super.getId(), writer));
            return deleteHistories;
        }

        throw new NotDeletedException("삭제 되지 않은 질문 입니다.");
    }

    public boolean isDeleted() {
        return deleted;
    }

    private List<DeleteHistory> deleteAnswerHistories() {
        return answers.stream()
            .map(Answer::deleteHistory)
            .collect(Collectors.toList());
    }

    private void validateQuestionOwner(User loginUser) throws CannotDeleteException {
        if (isNotOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private void validateAnswersOwner(User loginUser) throws CannotDeleteException {
        if (isAnswersWrittenByOthers(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private boolean isAnswersWrittenByOthers(User loginUser) {
        return answers.stream()
            .anyMatch(answer -> answer.isNotOwner(loginUser));
    }

    public boolean isNotOwner(User writer) {
        return !this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public Long getId() {
        return super.getId();
    }

    public User getWriter() {
        return writer;
    }
}
