package fr.insee.queen.api.utils;

import fr.insee.queen.api.configuration.auth.AuthorityRoleEnum;
import fr.insee.queen.api.pilotage.controller.interviewer.PilotageInterviewerApiComponent;
import fr.insee.queen.api.pilotage.controller.interviewer.PilotageInterviewerComponent;
import fr.insee.queen.api.pilotage.service.dummy.PilotageFakeService;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnit;
import fr.insee.queen.api.utils.dummy.AuthenticationFakeHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PilotageInterviewerComponentTest {
    private PilotageFakeService pilotageService;
    private AuthenticationFakeHelper authHelper;
    private AuthenticatedUserTestHelper authenticatedUserTestHelper;
    private PilotageInterviewerComponent pilotageInterviewerComponent;

    @BeforeEach
    void init() {
        authenticatedUserTestHelper = new AuthenticatedUserTestHelper();
        pilotageService = new PilotageFakeService();
    }

    @Test
    @DisplayName("On retrieving survey units by campaign for current user, return survey units")
    void testSuByCampaign() {
        Authentication authenticatedUser = authenticatedUserTestHelper.getAuthenticatedUser(
                AuthorityRoleEnum.INTERVIEWER, AuthorityRoleEnum.REVIEWER_ALTERNATIVE, AuthorityRoleEnum.REVIEWER);
        authHelper = new AuthenticationFakeHelper(authenticatedUser);
        pilotageInterviewerComponent = new PilotageInterviewerApiComponent(pilotageService, authHelper);
        assertThat(pilotageInterviewerComponent.getSurveyUnitsByCampaign("campaign-id")).isEqualTo(pilotageService.surveyUnitSummaries());
    }

    @Test
    @DisplayName("On retrieving campaigns for current interviewer, return campaigns")
    void testInterviewerCampaigns() {
        Authentication authenticatedUser = authenticatedUserTestHelper.getAuthenticatedUser();
        authHelper = new AuthenticationFakeHelper(authenticatedUser);
        pilotageInterviewerComponent = new PilotageInterviewerApiComponent(pilotageService, authHelper);
        assertThat(pilotageInterviewerComponent.getInterviewerCampaigns()).isEqualTo(pilotageService.interviewerCampaigns());
    }

    @Test
    @DisplayName("When retrieving survey units for interviewer, return interviewer  survey units")
    void testGetInterviewerSurveyUnits() {
        Authentication authenticatedUser = authenticatedUserTestHelper.getAuthenticatedUser();
        authHelper = new AuthenticationFakeHelper(authenticatedUser);
        pilotageInterviewerComponent = new PilotageInterviewerApiComponent(pilotageService, authHelper);
        List<SurveyUnit> surveyUnits = pilotageInterviewerComponent.getInterviewerSurveyUnits();
        assertThat(pilotageService.interviewerSurveyUnits()).isEqualTo(surveyUnits);
    }
}
