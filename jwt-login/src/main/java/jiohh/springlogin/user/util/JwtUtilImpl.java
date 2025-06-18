package jiohh.springlogin.user.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jiohh.springlogin.user.dto.JwtPayloadDto;
import jiohh.springlogin.user.dto.UserDto;
import jiohh.springlogin.user.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtilImpl implements JwtUtil {

//    예시
//    private final long accessTokenValiditySeconds = 1000 * 60 * 30;
//    private final long refreshTokenValiditySeconds = 1000 * 60 * 60 * 24 * 7;
    private final String secretKey;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;

    public JwtUtilImpl(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.accessTokenSeconds}") long accessTokenValiditySeconds,
                       @Value("${jwt.refreshTokenSeconds}") long refreshTokenValiditySeconds ) {
        this.secretKey = secretKey;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding(); //패딩이 없는
    private final Base64.Decoder decoder = Base64.getUrlDecoder();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String createAccessToken(JwtPayloadDto user) {
        return generateToken(user, accessTokenValiditySeconds);
    }

    @Override
    public String createRefreshToken() {
        return generateToken(null, refreshTokenValiditySeconds);
    }

    private String generateToken(JwtPayloadDto dto, long expiresIn) {
        long now = System.currentTimeMillis();
        long exp = now + expiresIn;
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        Map<String, Object> payload = new HashMap<>();
        if (dto != null) {
            payload.put("sub", dto.getSub());
            payload.put("userId", dto.getUserId());
            payload.put("name", dto.getName());
            payload.put("role", dto.getRole());
        }
        payload.put("iat", now / 1000);
        payload.put("exp", exp / 1000);

        try {
            String encodedHeader = encodeToken(header);
            String encodedPayload = encodeToken(payload);

            String signature = createSignature(encodedHeader, encodedPayload);

            return encodedHeader + "." + encodedPayload + "." +signature;
        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException e){
//            TODO : 커스텀 exception 작성
            throw new IllegalArgumentException("JWT 서명 생성 실패",e);
        }
    }
    
    private String encodeToken(Map<String, Object> claims) throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(claims);
        return encoder.encodeToString(jsonString.getBytes(StandardCharsets.UTF_8));
    }

    private String createSignature(String header, String payload) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(key);
        String data = header + "." + payload;
        byte[] sig= hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return encoder.encodeToString(sig);
    }

    @Override
    public boolean validationToken(String token) {
        String[] split = token.split("\\.");
        if (split.length != 3) {
            return false;
        }
        String header = split[0];
        String payload = split[1];
        String signature = split[2];

        String recreatedSignature;
        try{
            recreatedSignature = createSignature(header, payload);
            if (!recreatedSignature.equals(signature)){
                return false;
            }
            String decodedPayload = decodeToken(payload);
            Map<String, Object> parsedPayload = objectMapper.readValue(decodedPayload, new TypeReference<>() {});
            long expiresIn = ((Number) parsedPayload.get("exp")).longValue();
            long now = System.currentTimeMillis() / 1000;
            return expiresIn >= now;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
//            TODO : 커스텀예외 작성 및 프로세스 간 예외처리 분리
            log.error(e.getMessage());
            return false;
        }  catch (JsonProcessingException e) { //JsonMappingException e
            throw new RuntimeException(e);
        }
    }

    private String decodeToken(String data) {
        byte[] decode = decoder.decode(data);
        return new String(decode, StandardCharsets.UTF_8);
    }

    @Override
    public Long getSubject(String token) {
        try{
            String[] split = token.split("\\.");
            String payload = split[1];
            String decodedToken = decodeToken(payload);
            Map<String, Object> payloadObject = objectMapper.readValue(decodedToken, new TypeReference<Map<String, Object>>() {
            });
            Object sub = payloadObject.get("sub");
            if (sub == null) { return null;}
            if (sub instanceof Number){
                return ((Number) sub).longValue();
            } else if (sub instanceof String){
                return Long.parseLong((String) sub);
            } else {
                throw new IllegalArgumentException("sub 값이 잘못되었습니다.");
            }
        } catch (JsonProcessingException e) {
//            TODO : 커스텀 exception 처리
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> getClaims(String token)  {
        try{
            String[] split = token.split("\\.");
            String payload = split[1];
            String decodedToken = decodeToken(payload);
            return objectMapper.readValue(decodedToken, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            //            TODO : 커스텀 exception 처리
            throw new RuntimeException(e);
        }
    }

    @Override
    public JwtPayloadDto getUser(String token) {
        try{
            String[] split = token.split("\\.");
            String payload = split[1];
            String decodedToken = decodeToken(payload);
            return objectMapper.readValue(decodedToken, JwtPayloadDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
