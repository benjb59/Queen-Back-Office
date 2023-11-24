package fr.insee.queen.api.utils;

import fr.insee.queen.api.pilotage.controller.habilitation.NoHabilitationComponent;
import fr.insee.queen.api.pilotage.service.PilotageRole;
import fr.insee.queen.api.surveyunit.service.dummy.SurveyUnitFakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoHabilitationComponentTest {
    private SurveyUnitFakeService surveyUnitService;
    private NoHabilitationComponent habilitationComponent;

    @BeforeEach
    void init() {
        surveyUnitService = new SurveyUnitFakeService();
    }

    @Test
    @DisplayName("On check habilitations check survey unit existence")
    void testCheckHabilitations01() {
        habilitationComponent = new NoHabilitationComponent(surveyUnitService);
        habilitationComponent.checkHabilitations("11", PilotageRole.INTERVIEWER);
        assertThat(surveyUnitService.checkSurveyUnitExist()).isTrue();
    }

    @Test
    @DisplayName("When checking if campaign is closed, always return true")
    void testIsClosed() {
        habilitationComponent = new NoHabilitationComponent(surveyUnitService);
        boolean isClosed = habilitationComponent.isClosed("campaign-id");
        assertThat(isClosed).isTrue();
    }
}
