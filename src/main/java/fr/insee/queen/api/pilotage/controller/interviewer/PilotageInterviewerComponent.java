package fr.insee.queen.api.pilotage.controller.interviewer;

import fr.insee.queen.api.pilotage.service.model.PilotageCampaign;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnit;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitSummary;

import java.util.List;

public interface PilotageInterviewerComponent {
    /**
     * Retrieve survey unit list of a campaign
     * @param campaignId campaign id
     * @return List of {@link SurveyUnitSummary} survey units of the campaign
     */
    List<SurveyUnitSummary> getSurveyUnitsByCampaign(String campaignId);

    /**
     * Retrieve campaigns the user has access to as an interviewer
     * @return List of {@link PilotageCampaign} authorized campaigns
     */
    List<PilotageCampaign> getInterviewerCampaigns();

    /**
     * Retrieve survey unit list for an interviewer
     * @return List of {@link SurveyUnit} survey units of the campaign
     */
    List<SurveyUnit> getInterviewerSurveyUnits();
}

