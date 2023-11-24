package fr.insee.queen.api.pilotage.controller.habilitation;

import fr.insee.queen.api.pilotage.service.PilotageRole;
import fr.insee.queen.api.pilotage.service.PilotageService;
import fr.insee.queen.api.pilotage.service.exception.HabilitationException;
import fr.insee.queen.api.surveyunit.service.SurveyUnitService;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitSummary;
import fr.insee.queen.api.web.authentication.AuthenticationHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@ConditionalOnExpression(value = "'${application.auth}' == 'OIDC' and ${feature.enable.pilotage} == true")
@Component
@AllArgsConstructor
@Slf4j
public class HabilitationApiComponent implements HabilitationComponent {
    private final PilotageService pilotageService;
    private final AuthenticationHelper authHelper;
    private final SurveyUnitService surveyUnitService;

    @Override
    public boolean isClosed(String campaignId) {
        String userToken = authHelper.getUserToken();
        return pilotageService.isClosed(campaignId, userToken);
    }

    @Override
    public void checkHabilitations(String surveyUnitId, PilotageRole... rolesToCheck) {
        SurveyUnitSummary surveyUnit = surveyUnitService.getSurveyUnitWithCampaignById(surveyUnitId);
        Authentication auth = authHelper.getAuthenticationPrincipal();

        if (!auth.isAuthenticated()) {
            // anonymous user cannot have habilitation
            throw new HabilitationException();
        }

        List<String> userRoles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (userRoles.contains("ROLE_ADMIN") || userRoles.contains("ROLE_WEBCLIENT")) {
            return;
        }

        String userId = authHelper.getUserId();
        log.info("Check habilitation of user {} with role {} to access survey-unit {} ", userId, rolesToCheck, surveyUnit.id());
        String userToken = authHelper.getUserToken();
        for (PilotageRole roleToCheck : rolesToCheck) {
            if (pilotageService.hasHabilitation(surveyUnit, roleToCheck, userId, userToken)) {
                return;
            }
        }
        throw new HabilitationException();
    }
}
