package fr.insee.queen.application.utils.dummy;

import fr.insee.queen.application.configuration.auth.AuthConstants;
import fr.insee.queen.application.web.authentication.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class AuthenticationFakeHelper implements AuthenticationHelper {
    private final Authentication authenticationUser;

    @Override
    public String getUserToken() {
        return null;
    }

    @Override
    public String getUserId() {
        return AuthConstants.GUEST;
    }

    @Override
    public Authentication getAuthenticationPrincipal() {
        return authenticationUser;
    }
}