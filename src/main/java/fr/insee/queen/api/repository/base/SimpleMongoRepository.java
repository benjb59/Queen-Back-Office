package fr.insee.queen.api.repository.base;

import com.fasterxml.jackson.databind.JsonNode;
import fr.insee.queen.api.domain.*;
import fr.insee.queen.api.dto.surveyunit.SurveyUnitResponseDto;
import fr.insee.queen.api.repository.SimpleApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "fr.insee.queen.application", name = "persistenceType", havingValue = "MONGODB", matchIfMissing = true)
public class SimpleMongoRepository implements SimpleApiRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void updateSurveyUnitData(String id, JsonNode data) {
        // setData
        Data dataModel = new Data();
        dataModel.setValue(data);
        // make the update
        Update update = new Update();
        update.set("data", dataModel);
        // Do the update
        updateJsonValueOfSurveyUnit(update,id);
    }

    @Override
    public void updateSurveyUnitComment(String id, JsonNode comment)  {
        // setComment
        Comment commentModel = new Comment();
        commentModel.setValue(comment);
        // make the update
        Update update = new Update();
        update.set("comment", commentModel);
        // Do the update
        updateJsonValueOfSurveyUnit(update,id);
    }

    @Override
    public void updateSurveyUnitPersonalization(String id, JsonNode personalization) {
        // setPersonalization
        Personalization personalizationModel = new Personalization();
        personalizationModel.setValue(personalization);
        // make the update
        Update update = new Update();
        update.set("personalization", personalizationModel);
        // Do the update
        updateJsonValueOfSurveyUnit(update,id);
    }

    @Override
    public void updateSurveyUnitStateDate(String id, JsonNode stateData){
        // make the update
        Update update = new Update();
        update.set("stateData", stateData);
        // Do the update
        updateJsonValueOfSurveyUnit(update,id);
    }

    @Override
    public void createSurveyUnit(String campaignId, SurveyUnitResponseDto surveyUnitResponseDto) {
        // TODO
    }

    private void updateJsonValueOfSurveyUnit(Update update, String id) {
        // create Query for mongo
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        // update first element which match with the criteria with the update
        mongoTemplate.updateFirst(query, update, SurveyUnit.class);
    }
}
