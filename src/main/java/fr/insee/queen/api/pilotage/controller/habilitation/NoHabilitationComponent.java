package fr.insee.queen.api.pilotage.controller.habilitation;

import fr.insee.queen.api.pilotage.service.PilotageRole;
import fr.insee.queen.api.surveyunit.service.SurveyUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@ConditionalOnExpression(value = "'${application.auth}' == 'NOAUTH' or ${feature.enable.pilotage} == false")
@RequiredArgsConstructor
@Component
public class NoHabilitationComponent implements HabilitationComponent {
    private final SurveyUnitService surveyUnitService;

    @Override
    public boolean isClosed(String campaignId) {
        return true;
    }

    @Override
    public void checkHabilitations(String surveyUnitId, PilotageRole... roles) {
        surveyUnitService.throwExceptionIfSurveyUnitNotExist(surveyUnitId);
    }
}
