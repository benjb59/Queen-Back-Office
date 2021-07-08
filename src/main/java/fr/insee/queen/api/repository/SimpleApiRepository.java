package fr.insee.queen.api.repository;

import com.fasterxml.jackson.databind.JsonNode;

public interface SimpleApiRepository {

    void updateSurveyUnitData(String id, JsonNode data);
    void updateSurveyUnitComment(String id, JsonNode comment);
    void updateSurveyUnitPersonalization(String id, JsonNode personalization);
    void updateSurveyUnitStateDate(String id, JsonNode stateData);
}
