package jiohh.springlogin.user.exception;

public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException(String message) {
        super(message);
    }

    public DuplicateUserIdException() {
        super("이미 사용 중인 아이디입니다");
    }
}
