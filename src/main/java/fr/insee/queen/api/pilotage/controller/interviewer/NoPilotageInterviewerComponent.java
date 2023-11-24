package fr.insee.queen.api.pilotage.controller.interviewer;

import fr.insee.queen.api.campaign.service.CampaignService;
import fr.insee.queen.api.pilotage.service.model.PilotageCampaign;
import fr.insee.queen.api.surveyunit.service.SurveyUnitService;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnit;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;

@ConditionalOnExpression(value = "'${application.auth}' == 'NOAUTH' or ${feature.enable.pilotage} == false or ${feature.enable.interviewer-collect} == false")
@RequiredArgsConstructor
@Component
public class NoPilotageInterviewerComponent implements PilotageInterviewerComponent {
    private final SurveyUnitService surveyUnitService;
    private final CampaignService campaignService;

    @Override
    public List<SurveyUnitSummary> getSurveyUnitsByCampaign(String campaignId) {
        return surveyUnitService.findSummariesByCampaignId(campaignId);
    }

    @Override
    public List<PilotageCampaign> getInterviewerCampaigns() {
        return campaignService.getAllCampaigns().stream()
                .map(campaign -> new PilotageCampaign(campaign.id(), campaign.questionnaireIds().stream().toList()))
                .toList();
    }

    @Override
    public List<SurveyUnit> getInterviewerSurveyUnits() {
        return surveyUnitService.findAllSurveyUnits();
    }
}
