package jiohh.springlogin.config;

import jiohh.springlogin.config.interceptor.LogInterceptor;
import jiohh.springlogin.config.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;
    private final SessionInterceptor sessionInterceptor;

    public WebConfig(LogInterceptor logInterceptor, SessionInterceptor sessionInterceptor) {
        this.logInterceptor = logInterceptor;
        this.sessionInterceptor = sessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/api/**", "/me", "/logout")
                .excludePathPatterns("/login", "/signup");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
