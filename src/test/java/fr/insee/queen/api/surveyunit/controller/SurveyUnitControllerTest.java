package fr.insee.queen.api.surveyunit.controller;

import fr.insee.queen.api.pilotage.controller.dummy.HabilitationFakeComponent;
import fr.insee.queen.api.pilotage.controller.dummy.PilotageInterviewerFakeComponent;
import fr.insee.queen.api.surveyunit.controller.dto.output.SurveyUnitByCampaignDto;
import fr.insee.queen.api.surveyunit.controller.dto.output.SurveyUnitDto;
import fr.insee.queen.api.surveyunit.service.dummy.SurveyUnitFakeService;
import fr.insee.queen.api.utils.AuthenticatedUserTestHelper;
import fr.insee.queen.api.utils.dummy.AuthenticationFakeHelper;
import fr.insee.queen.api.web.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SurveyUnitControllerTest {

    private SurveyUnitController surveyUnitController;

    private PilotageInterviewerFakeComponent pilotageInterviewerComponent;

    @BeforeEach
    public void init() {
        AuthenticatedUserTestHelper authenticatedUserTestHelper = new AuthenticatedUserTestHelper();
        Authentication authenticatedUser = authenticatedUserTestHelper.getAuthenticatedUser();
        AuthenticationFakeHelper authenticationHelper = new AuthenticationFakeHelper(authenticatedUser);
        SurveyUnitFakeService surveyUnitService = new SurveyUnitFakeService();
        pilotageInterviewerComponent = new PilotageInterviewerFakeComponent();
        HabilitationFakeComponent habilitationComponent = new HabilitationFakeComponent();
        surveyUnitController = new SurveyUnitController(surveyUnitService, habilitationComponent, pilotageInterviewerComponent, authenticationHelper);
    }

    @Test
    @DisplayName("On retrieving survey units for a campaign, then return survey units from pilotage service")
    void testGetSurveyUnitsCampaign01() {
        List<SurveyUnitByCampaignDto> surveyUnits = surveyUnitController.getListSurveyUnitByCampaign("campaign-id");
        assertThat(surveyUnits).hasSize(2);
        assertThat(surveyUnits.get(0).id()).isEqualTo(PilotageInterviewerFakeComponent.SURVEY_UNIT1_ID);
    }

    @Test
    @DisplayName("On retrieving survey units for a campaign, when survey units are empty then throws exception")
    void testGetSurveyUnitsCampaign02() {
        pilotageInterviewerComponent.hasEmptySurveyUnits(true);
        assertThatThrownBy(() -> surveyUnitController.getListSurveyUnitByCampaign("campaign-id"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("On retrieving survey units for an interviewer, return survey units found")
    void testGetSurveyUnitsForInterviewers03() {
        List<SurveyUnitDto> surveyUnits =  surveyUnitController.getInterviewerSurveyUnits();
        assertThat(surveyUnits).size().isEqualTo(2);
        assertThat(surveyUnits.get(0).id()).isEqualTo(PilotageInterviewerFakeComponent.SURVEY_UNIT1_ID);
        assertThat(surveyUnits.get(1).id()).isEqualTo(PilotageInterviewerFakeComponent.SURVEY_UNIT2_ID);
    }

    @Test
    @DisplayName("On retrieving survey units for an interviewer, return survey units found")
    void testGetSurveyUnitsForInterviewers04() {
        List<SurveyUnitDto> surveyUnits =  surveyUnitController.getInterviewerSurveyUnits();
        assertThat(surveyUnits).size().isEqualTo(2);
        assertThat(surveyUnits.get(0).id()).isEqualTo(PilotageInterviewerFakeComponent.SURVEY_UNIT1_ID);
        assertThat(surveyUnits.get(1).id()).isEqualTo(PilotageInterviewerFakeComponent.SURVEY_UNIT2_ID);
    }
}
