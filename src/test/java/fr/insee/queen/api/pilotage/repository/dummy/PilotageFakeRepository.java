package fr.insee.queen.api.pilotage.repository.dummy;

import fr.insee.queen.api.pilotage.service.PilotageRole;
import fr.insee.queen.api.pilotage.service.gateway.PilotageRepository;
import fr.insee.queen.api.pilotage.service.model.PilotageCampaign;
import fr.insee.queen.api.pilotage.service.model.PilotageSurveyUnit;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitSummary;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PilotageFakeRepository implements PilotageRepository {

    public static final String INTERVIEWER_CAMPAIGN1_ID = "interviewer-campaign1";
    public static final String CURRENT_SU_CAMPAIGN1_ID = "su-campaign1";

    public static final String SURVEY_UNIT1_ID = "pilotage-su-1";
    public static final String SURVEY_UNIT2_ID = "pilotage-su-2";
    public static final String SURVEY_UNIT3_ID = "pilotage-su-2";

    @Getter
    private boolean wentThroughIsClosedCampaign = false;
    @Getter
    private boolean wentThroughHasHabilitation = true;
    @Setter
    private boolean nullInterviewerCampaigns = false;
    @Setter
    private boolean nullCurrentSurveyUnit = false;

    @Override
    public boolean isClosed(String campaignId, String authToken) {
        wentThroughIsClosedCampaign = true;
        return false;
    }

    @Override
    public List<PilotageSurveyUnit> getSurveyUnits(String authToken) {
        if (nullCurrentSurveyUnit) {
            return null;
        }
        return List.of(
                new PilotageSurveyUnit(SURVEY_UNIT1_ID, CURRENT_SU_CAMPAIGN1_ID),
                new PilotageSurveyUnit(SURVEY_UNIT2_ID, "su-campaign2"),
                new PilotageSurveyUnit(SURVEY_UNIT3_ID, CURRENT_SU_CAMPAIGN1_ID)
        );
    }

    @Override
    public List<PilotageCampaign> getInterviewerCampaigns(String authToken) {
        if (nullInterviewerCampaigns) {
            return null;
        }
        return List.of(
                new PilotageCampaign(INTERVIEWER_CAMPAIGN1_ID, List.of("questionnaire-id")),
                new PilotageCampaign("interviewer-campaign2", List.of("questionnaire-id"))
        );
    }

    @Override
    public boolean hasHabilitation(SurveyUnitSummary surveyUnit, PilotageRole role, String idep, String authToken) {
        this.wentThroughHasHabilitation = true;
        return true;
    }

}