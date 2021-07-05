package qna;

public class NotDeletedException extends RuntimeException {

    public NotDeletedException(String message) {
        super(message);
    }
}
