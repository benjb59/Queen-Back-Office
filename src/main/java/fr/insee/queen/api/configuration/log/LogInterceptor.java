package fr.insee.queen.api.configuration.log;

import fr.insee.queen.api.configuration.properties.ApplicationProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private final ApplicationProperties applicationProperties;

    public LogInterceptor(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String fishTag = UUID.randomUUID().toString();
        String method = request.getMethod();
        String operationPath = request.getRequestURI();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = switch (applicationProperties.auth()) {
            case BASIC -> {
                Object basic = authentication.getPrincipal();
                if (basic instanceof UserDetails userDetails) {
                    yield userDetails.getUsername();
                }
                yield basic.toString();
            }
            case KEYCLOAK -> {
                if(authentication.getCredentials() instanceof Jwt jwt) {
                    yield jwt.getClaims().get("preferred_username").toString();
                }
                yield "GUEST";
            }
            default -> "GUEST";
        };


        MDC.put("id", fishTag);
        MDC.put("path", operationPath);
        MDC.put("method", method);
        MDC.put("user", userId);

        log.info("["+userId+"] - ["+method+"] - ["+operationPath+"]");
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) {
        // no need to posthandle things for this interceptor
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) {
        MDC.clear();
    }
}