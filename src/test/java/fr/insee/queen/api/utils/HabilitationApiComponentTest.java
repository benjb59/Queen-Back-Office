package fr.insee.queen.api.utils;

import fr.insee.queen.api.configuration.auth.AuthorityRoleEnum;
import fr.insee.queen.api.pilotage.controller.habilitation.HabilitationApiComponent;
import fr.insee.queen.api.pilotage.service.PilotageRole;
import fr.insee.queen.api.pilotage.service.dummy.PilotageFakeService;
import fr.insee.queen.api.pilotage.service.exception.HabilitationException;
import fr.insee.queen.api.surveyunit.service.dummy.SurveyUnitFakeService;
import fr.insee.queen.api.utils.dummy.AuthenticationFakeHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HabilitationApiComponentTest {
    private PilotageFakeService pilotageService;
    private AuthenticationFakeHelper authHelper;
    private AuthenticatedUserTestHelper authenticatedUserTestHelper;
    private SurveyUnitFakeService surveyUnitService;
    private HabilitationApiComponent habilitationComponent;

    @BeforeEach
    void init() {
        authenticatedUserTestHelper = new AuthenticatedUserTestHelper();
        pilotageService = new PilotageFakeService();
        surveyUnitService = new SurveyUnitFakeService();
    }

    @Test
    @DisplayName("On check habilitations when non authenticated user throw exception")
    void testCheckHabilitations01() {
        authHelper = new AuthenticationFakeHelper(authenticatedUserTestHelper.getNotAuthenticatedUser());
        habilitationComponent = new HabilitationApiComponent(pilotageService, authHelper, surveyUnitService);
        assertThatThrownBy(() -> habilitationComponent.checkHabilitations("11", PilotageRole.INTERVIEWER))
                .isInstanceOf(HabilitationException.class);
    }

    @Test
    @DisplayName("On check habilitations when ADMIN role do not check pilotage api")
    void testCheckHabilitations02() {
        authHelper = new AuthenticationFakeHelper(authenticatedUserTestHelper.getAuthenticatedUser(AuthorityRoleEnum.INTERVIEWER, AuthorityRoleEnum.ADMIN));
        habilitationComponent = new HabilitationApiComponent(pilotageService, authHelper, surveyUnitService);
        habilitationComponent.checkHabilitations("11", PilotageRole.INTERVIEWER);
        assertThat(pilotageService.wentThroughHasHabilitation()).isZero();
    }

    @Test
    @DisplayName("On check habilitations when WEBCLIENT role do not check pilotage api")
    void testCheckHabilitations03() {
        authHelper = new AuthenticationFakeHelper(authenticatedUserTestHelper.getAuthenticatedUser(AuthorityRoleEnum.INTERVIEWER, AuthorityRoleEnum.WEBCLIENT));
        habilitationComponent = new HabilitationApiComponent(pilotageService, authHelper, surveyUnitService);
        habilitationComponent.checkHabilitations("11", PilotageRole.INTERVIEWER);
        assertThat(pilotageService.wentThroughHasHabilitation()).isZero();
    }

    @Test
    @DisplayName("On check habilitations then check pilotage api")
    void testCheckHabilitations04() {
        authHelper = new AuthenticationFakeHelper(authenticatedUserTestHelper.getAuthenticatedUser(AuthorityRoleEnum.INTERVIEWER, AuthorityRoleEnum.REVIEWER));
        habilitationComponent = new HabilitationApiComponent(pilotageService, authHelper, surveyUnitService);
        habilitationComponent.checkHabilitations("11", PilotageRole.INTERVIEWER);
        assertThat(pilotageService.wentThroughHasHabilitation()).isEqualTo(1);
    }

    @Test
    @DisplayName("On check habilitations when pilotage api always return false then throws exception")
    void testCheckHabilitations05() {
        Authentication authenticatedUser = authenticatedUserTestHelper.getAuthenticatedUser(
                AuthorityRoleEnum.INTERVIEWER, AuthorityRoleEnum.REVIEWER_ALTERNATIVE, AuthorityRoleEnum.REVIEWER);
        authHelper = new AuthenticationFakeHelper(authenticatedUser);
        pilotageService.hasHabilitation(false);
        habilitationComponent = new HabilitationApiComponent(pilotageService, authHelper, surveyUnitService);
        assertThatThrownBy(() -> habilitationComponent.checkHabilitations("11", PilotageRole.INTERVIEWER, PilotageRole.REVIEWER))
                .isInstanceOf(HabilitationException.class);
        assertThat(pilotageService.wentThroughHasHabilitation()).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("On check if campaign closed return result from pilotage service")
    void testIsClosed(boolean pilotageServiceResult) {
        Authentication authenticatedUser = authenticatedUserTestHelper.getAuthenticatedUser();
        authHelper = new AuthenticationFakeHelper(authenticatedUser);
        pilotageService.isCampaignClosed(pilotageServiceResult);
        habilitationComponent = new HabilitationApiComponent(pilotageService, authHelper, surveyUnitService);
        boolean isCampaignClosed = habilitationComponent.isClosed("écampaign-id");
        assertThat(isCampaignClosed).isEqualTo(pilotageServiceResult);
    }
}
