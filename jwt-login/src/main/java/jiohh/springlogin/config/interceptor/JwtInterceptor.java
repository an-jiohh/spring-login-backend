package jiohh.springlogin.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jiohh.springlogin.error.model.ErrorResponseDTO;
import jiohh.springlogin.user.dto.JwtPayloadDto;
import jiohh.springlogin.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper;
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null || accessToken.startsWith("Bearer ")) {
            ErrorResponseDTO invalidAuthHeader = ErrorResponseDTO.builder()
                    .code("INVALID_AUTH_HEADER")
                    .message("Authorization 헤더가 누락되었거나 Bearer 토큰 형식이 아닙니다.").build();
            String errorJson = objectMapper.writeValueAsString(invalidAuthHeader);
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print(errorJson);
            return false;
        }
        accessToken = accessToken.substring("Bearer ".length());
        if (!jwtUtil.validationToken(accessToken)){
            ErrorResponseDTO invalidAuthHeader = ErrorResponseDTO.builder()
                    .code("INVALID_TOKEN")
                    .message("유효하지 않은 액세스 토큰입니다.").build();
            String errorJson = objectMapper.writeValueAsString(invalidAuthHeader);
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print(errorJson);
            return false;
        }
        JwtPayloadDto user = jwtUtil.getUser(accessToken);
        request.setAttribute("user", user);
        return true;
    }
}
