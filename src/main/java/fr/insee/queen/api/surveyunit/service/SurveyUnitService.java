package fr.insee.queen.api.surveyunit.service;

import fr.insee.queen.api.depositproof.service.model.SurveyUnitDepositProof;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnit;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitState;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitSummary;

import java.util.List;
import java.util.Optional;

public interface SurveyUnitService {
    boolean existsById(String surveyUnitId);

    void throwExceptionIfSurveyUnitNotExist(String surveyUnitId);

    SurveyUnit getSurveyUnit(String id);

    List<SurveyUnitSummary> findByCampaignId(String campaignId);

    List<String> findAllSurveyUnitIds();

    void updateSurveyUnit(SurveyUnit surveyUnit);

    void createSurveyUnit(SurveyUnit surveyUnit);

    List<SurveyUnitSummary> findSummaryByIds(List<String> surveyUnits);

    Optional<SurveyUnitSummary> findSummaryById(String surveyUnitId);

    List<SurveyUnitState> findWithStateByIds(List<String> surveyUnits);

    void delete(String surveyUnitId);

    SurveyUnitDepositProof getSurveyUnitDepositProof(String surveyUnitId);

    SurveyUnitSummary getSurveyUnitWithCampaignById(String surveyUnitId);
}
