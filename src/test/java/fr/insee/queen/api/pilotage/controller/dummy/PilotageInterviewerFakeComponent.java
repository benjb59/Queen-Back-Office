package fr.insee.queen.api.pilotage.controller.dummy;

import fr.insee.queen.api.depositproof.service.model.StateDataType;
import fr.insee.queen.api.pilotage.controller.interviewer.PilotageInterviewerComponent;
import fr.insee.queen.api.pilotage.service.model.PilotageCampaign;
import fr.insee.queen.api.surveyunit.service.model.StateData;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnit;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitSummary;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PilotageInterviewerFakeComponent implements PilotageInterviewerComponent {
    @Getter
    private boolean wentThroughInterviewerCampaigns = false;
    @Setter
    private boolean hasEmptySurveyUnits = false;

    public static final String CAMPAIGN1_ID = "interviewerCampaign1";
    public static final String SURVEY_UNIT1_ID = "pilotage-component-s1";
    public static final String SURVEY_UNIT2_ID = "pilotage-component-s2";

    @Override
    public List<SurveyUnitSummary> getSurveyUnitsByCampaign(String campaignId) {
        if (this.hasEmptySurveyUnits) {
            return new ArrayList<>();
        }
        return List.of(
                new SurveyUnitSummary(SURVEY_UNIT1_ID, "questionnaire-id", "campaign-id"),
                new SurveyUnitSummary("s2", "questionnaire-id", "campaign-id")
        );
    }

    @Override
    public List<PilotageCampaign> getInterviewerCampaigns() {
        wentThroughInterviewerCampaigns = true;
        return List.of(
                new PilotageCampaign(CAMPAIGN1_ID, new ArrayList<>()),
                new PilotageCampaign("interviewerCampaign2", new ArrayList<>())
        );
    }

    @Override
    public List<SurveyUnit> getInterviewerSurveyUnits() {
        if (this.hasEmptySurveyUnits) {
            return new ArrayList<>();
        }
        return List.of(
                new SurveyUnit(SURVEY_UNIT1_ID, "campaign-id", "questionnaire-id",
                        "[]", "{}", "{}",
                        new StateData(StateDataType.INIT, 0L, "2#3")),
                new SurveyUnit(SURVEY_UNIT2_ID, "campaign-id", "questionnaire-id",
                        "[]", "{}", "{}",
                        new StateData(StateDataType.INIT, 0L, "2#3"))
        );
    }
}