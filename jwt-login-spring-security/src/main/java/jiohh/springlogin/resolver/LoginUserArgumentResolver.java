package jiohh.springlogin.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jiohh.springlogin.user.dto.JwtPayloadDto;
import jiohh.springlogin.user.dto.UserDto;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(LoginUser.class)
                && parameter.getParameterType().equals(UserDto.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        JwtPayloadDto payloadDto = (JwtPayloadDto) request.getAttribute("user");
        if (payloadDto == null) {
//            TODO : custom exception 작성
            throw new IllegalArgumentException("User id is required");
        }
        return UserDto.builder()
                .id(payloadDto.getSub())
                .userId(payloadDto.getUserId())
                .role(payloadDto.getRole())
                .name(payloadDto.getName()).build();
    }
}
