package fr.insee.queen.api.pilotage.controller.interviewer;

import fr.insee.queen.api.pilotage.service.PilotageService;
import fr.insee.queen.api.pilotage.service.model.PilotageCampaign;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnit;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitSummary;
import fr.insee.queen.api.web.authentication.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;

@ConditionalOnExpression(value = "'${application.auth}' == 'OIDC' and ${feature.enable.pilotage} == true and ${feature.enable.interviewer-collect} == true")
@Component
@RequiredArgsConstructor
@Slf4j
public class PilotageInterviewerApiComponent implements PilotageInterviewerComponent {
    private final PilotageService pilotageService;
    private final AuthenticationHelper authHelper;
    @Override
    public List<SurveyUnitSummary> getSurveyUnitsByCampaign(String campaignId) {
        String authToken = authHelper.getUserToken();
        return pilotageService.getSurveyUnitsByCampaign(campaignId, authToken);
    }

    @Override
    public List<PilotageCampaign> getInterviewerCampaigns() {
        String userId = authHelper.getUserId();
        log.info("User {} need his campaigns", userId);

        String authToken = authHelper.getUserToken();
        List<PilotageCampaign> campaigns = pilotageService.getInterviewerCampaigns(authToken);
        log.info("{} campaign(s) found for {}", campaigns.size(), userId);

        return campaigns;
    }

    @Override
    public List<SurveyUnit> getInterviewerSurveyUnits() {
        String authToken = authHelper.getUserToken();
        return pilotageService.getInterviewerSurveyUnits(authToken);
    }
}
