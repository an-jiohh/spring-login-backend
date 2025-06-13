package jiohh.springlogin.user.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SaltUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateSalt() {
        byte[] salt = new byte[16]; //16바이트 크기의 salt = 바이트로 변환하여 랜던값 높힘
        random.nextBytes(salt); //전달된 바이트 배열을 무작위 값으로 채움
        return Base64.getEncoder().encodeToString(salt);
    }
}
