package jiohh.springlogin.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jiohh.springlogin.error.model.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null){
            ErrorResponseDTO error = ErrorResponseDTO.builder().status("error").code("UNAUTHORIZED").message("로그인이 필요합니다.").build();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return false;
        }
        return true;
    }
}
