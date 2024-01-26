package fr.insee.queen.infrastructure.db.surveyunit.repository;

import fr.insee.queen.domain.surveyunit.gateway.SurveyUnitRepository;
import fr.insee.queen.domain.surveyunit.model.*;
import fr.insee.queen.infrastructure.db.campaign.entity.CampaignDB;
import fr.insee.queen.infrastructure.db.campaign.entity.QuestionnaireModelDB;
import fr.insee.queen.infrastructure.db.campaign.repository.jpa.CampaignJpaRepository;
import fr.insee.queen.infrastructure.db.campaign.repository.jpa.QuestionnaireModelJpaRepository;
import fr.insee.queen.infrastructure.db.surveyunit.projection.SurveyUnitProjection;
import fr.insee.queen.infrastructure.db.surveyunit.repository.jpa.DataJpaRepository;
import fr.insee.queen.infrastructure.db.surveyunit.repository.jpa.PersonalizationJpaRepository;
import fr.insee.queen.infrastructure.db.surveyunit.repository.jpa.SurveyUnitJpaRepository;
import fr.insee.queen.infrastructure.db.surveyunittempzone.repository.jpa.SurveyUnitTempZoneJpaRepository;
import fr.insee.queen.infrastructure.db.paradata.repository.jpa.ParadataEventJpaRepository;
import fr.insee.queen.infrastructure.db.surveyunit.entity.CommentDB;
import fr.insee.queen.infrastructure.db.surveyunit.entity.DataDB;
import fr.insee.queen.infrastructure.db.surveyunit.entity.PersonalizationDB;
import fr.insee.queen.infrastructure.db.surveyunit.entity.SurveyUnitDB;
import fr.insee.queen.infrastructure.db.surveyunit.repository.jpa.CommentJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO to handle survey units in DB
 */
@Repository
@AllArgsConstructor
public class SurveyUnitDao implements SurveyUnitRepository {

    private final SurveyUnitJpaRepository crudRepository;
    private final CommentJpaRepository commentRepository;
    private final PersonalizationJpaRepository personalizationRepository;
    private final DataJpaRepository dataRepository;
    private final StateDataDao stateDataDao;
    private final CampaignJpaRepository campaignRepository;
    private final QuestionnaireModelJpaRepository questionnaireModelRepository;
    private final SurveyUnitTempZoneJpaRepository surveyUnitTempZoneRepository;
    private final ParadataEventJpaRepository paradataEventRepository;

    @Override
    public Optional<SurveyUnitSummary> findSummaryById(String surveyUnitId) {
        return crudRepository.findSummaryById(surveyUnitId);
    }

    @Override
    public List<SurveyUnitSummary> findAllSummaryByCampaignId(String campaignId) {
        return crudRepository.findAllSummaryByCampaignId(campaignId);
    }

    @Override
    public List<SurveyUnitSummary> findAllSummaryByIdIn(List<String> surveyUnitIds) {
        return crudRepository.findAllSummaryByIdIn(surveyUnitIds);
    }

    @Override
    public Optional<SurveyUnit> find(String surveyUnitId) {
        return crudRepository.findOneById(surveyUnitId)
                .map(SurveyUnitProjection::toModel);
    }

    @Override
    public Optional<SurveyUnitDepositProof> findWithCampaignAndStateById(String surveyUnitId) {
        return crudRepository.findWithCampaignAndStateById(surveyUnitId);
    }

    @Override
    public Optional<List<String>> findAllIds() {
        return crudRepository.findAllIds();
    }

    @Override
    public List<SurveyUnitState> findAllWithStateByIdIn(List<String> surveyUnitIds) {
        return crudRepository.findAllWithState(surveyUnitIds);
    }

    @Override
    public List<String> findIdsWithExistingState(String campaignId) {
        return crudRepository.findIdsWithExistingState(campaignId);
    }

    @Override
    public List<SurveyUnitState> findWithExistingStateByCampaignId(String campaignId) {
        return crudRepository.findWithExistingStateByCampaignId(campaignId);
    }

    @Override
    public List<String> findIdsWithExistingState(String campaignId, StateDataType... stateDataTypes) {
        return crudRepository.findIdsWithExistingState(campaignId, stateDataTypes);
    }

    @Override
    public void deleteSurveyUnits(String campaignId) {
        dataRepository.deleteDatas(campaignId);
        stateDataDao.deleteStateDatas(campaignId);
        commentRepository.deleteComments(campaignId);
        personalizationRepository.deletePersonalizations(campaignId);
        surveyUnitTempZoneRepository.deleteSurveyUnits(campaignId);
        paradataEventRepository.deleteBySurveyUnitCampaignId(campaignId);
        crudRepository.deleteSurveyUnits(campaignId);
    }

    @Override
    public void delete(String surveyUnitId) {
        dataRepository.deleteBySurveyUnitId(surveyUnitId);
        stateDataDao.deleteBySurveyUnitId(surveyUnitId);
        commentRepository.deleteBySurveyUnitId(surveyUnitId);
        personalizationRepository.deleteBySurveyUnitId(surveyUnitId);
        surveyUnitTempZoneRepository.deleteBySurveyUnitId(surveyUnitId);
        paradataEventRepository.deleteBySurveyUnitId(surveyUnitId);
        crudRepository.deleteById(surveyUnitId);
    }

    @Override
    public void create(SurveyUnit surveyUnit) {
        CampaignDB campaign = campaignRepository.getReferenceById(surveyUnit.campaignId());
        QuestionnaireModelDB questionnaire = questionnaireModelRepository.getReferenceById(surveyUnit.questionnaireId());
        SurveyUnitDB surveyUnitDB = new SurveyUnitDB(surveyUnit.id(), campaign, questionnaire);
        DataDB dataDB = new DataDB(surveyUnit.data(), surveyUnitDB);
        CommentDB commentDB = new CommentDB(surveyUnit.comment(), surveyUnitDB);
        PersonalizationDB personalizationDB = new PersonalizationDB(surveyUnit.personalization(), surveyUnitDB);
        surveyUnitDB.setPersonalization(personalizationDB);
        surveyUnitDB.setComment(commentDB);
        surveyUnitDB.setData(dataDB);
        crudRepository.save(surveyUnitDB);
    }

    @Override
    public void savePersonalization(String surveyUnitId, String personalization) {
        if (personalization == null) {
            return;
        }

        int countUpdated = personalizationRepository.updatePersonalization(surveyUnitId, personalization);
        if (countUpdated == 0) {
            SurveyUnitDB surveyUnit = crudRepository.getReferenceById(surveyUnitId);
            PersonalizationDB personalizationDB = new PersonalizationDB(personalization, surveyUnit);
            personalizationRepository.save(personalizationDB);
        }
    }

    @Override
    public void saveComment(String surveyUnitId, String comment) {
        if (comment == null) {
            return;
        }

        int countUpdated = commentRepository.updateComment(surveyUnitId, comment);
        if (countUpdated == 0) {
            SurveyUnitDB surveyUnit = crudRepository.getReferenceById(surveyUnitId);
            CommentDB commentDB = new CommentDB(comment, surveyUnit);
            commentRepository.save(commentDB);
        }
    }

    @Override
    public void saveData(String surveyUnitId, String data) {
        if (data == null) {
            return;
        }

        int countUpdated = dataRepository.updateData(surveyUnitId, data);
        if (countUpdated == 0) {
            SurveyUnitDB surveyUnit = crudRepository.getReferenceById(surveyUnitId);
            DataDB dataDB = new DataDB(data, surveyUnit);
            dataRepository.save(dataDB);
        }
    }

    @Override
    public Optional<String> findComment(String surveyUnitId) {
        return commentRepository.findComment(surveyUnitId);
    }

    @Override
    public Optional<String> findData(String surveyUnitId) {
        return dataRepository.findData(surveyUnitId);
    }

    @Override
    public Optional<String> findPersonalization(String surveyUnitId) {
        return personalizationRepository.findPersonalization(surveyUnitId);
    }

    @Override
    public boolean exists(String surveyUnitId) {
        return crudRepository.existsById(surveyUnitId);
    }

    @Override
    public void updateInfos(SurveyUnit surveyUnit) {
        String surveyUnitId = surveyUnit.id();
        savePersonalization(surveyUnitId, surveyUnit.personalization());
        saveComment(surveyUnitId, surveyUnit.comment());
        saveData(surveyUnitId, surveyUnit.data());
    }

    @Override
    public void updateSummary(SurveyUnitSummary surveyUnit) {
        crudRepository.updateSummary(surveyUnit.id(), surveyUnit.questionnaireId(), surveyUnit.campaignId());
    }

    @Override
    public List<SurveyUnit> find(List<String> surveyUnitIds) {
        return crudRepository.findSurveyUnitsByIdIn(surveyUnitIds).stream()
                .map(SurveyUnitProjection::toModel)
                .toList();
    }

    @Override
    public List<SurveyUnit> findAll() {
        return crudRepository.findAllSurveyUnits().stream()
                .map(SurveyUnitProjection::toModel)
                .toList();
    }

    @Override
    public void deleteDataBySurveyUnitIds(List<String> surveyUnitIds) {
        dataRepository.deleteBySurveyUnitIdIn(surveyUnitIds);
    }


}
