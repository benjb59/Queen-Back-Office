package fr.insee.queen.api.surveyunit.service;

import com.fasterxml.jackson.databind.JsonNode;
import fr.insee.queen.api.surveyunit.service.gateway.SurveyUnitRepository;
import fr.insee.queen.api.web.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonalizationApiService implements PersonalizationService {
    private final SurveyUnitRepository surveyUnitRepository;

    @Override
    public String getPersonalization(String surveyUnitId) {
        return surveyUnitRepository
                .findPersonalization(surveyUnitId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Personalization not found for survey unit %s", surveyUnitId)));
    }

    @Override
    public void updatePersonalization(String surveyUnitId, JsonNode personalizationValue) {
        surveyUnitRepository.updatePersonalization(surveyUnitId, personalizationValue.toString());
    }
}
