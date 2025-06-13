package jiohh.springlogin.memo.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("작성한 유저를 찾을 수 없습니다.");
    }
}
