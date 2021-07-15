package fr.insee.queen.api.repository.base;

import com.fasterxml.jackson.databind.JsonNode;
import fr.insee.queen.api.dto.surveyunit.SurveyUnitResponseDto;
import fr.insee.queen.api.repository.SimpleApiRepository;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@ConditionalOnProperty(prefix = "fr.insee.queen.application", name = "persistenceType", havingValue = "MONGODB", matchIfMissing = true)
public class SimpleMongoRepository implements SimpleApiRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void updateSurveyUnitData(String id, JsonNode data) {
        updateJsonValueOfSurveyUnit("data",id,data);
    }

    @Override
    public void updateSurveyUnitComment(String id, JsonNode comment)  {
        updateJsonValueOfSurveyUnit("comment",id,comment);
    }

    @Override
    public void updateSurveyUnitPersonalization(String id, JsonNode personalization) {
        updateJsonValueOfSurveyUnit("personalization",id, personalization);
    }

    @Override
    public void updateSurveyUnitStateDate(String id, JsonNode stateData){
        // TODO
    }

    @Override
    public void createSurveyUnit(String campaignId, SurveyUnitResponseDto surveyUnitResponseDto) {
        // TODO
    }

    private void updateJsonValueOfSurveyUnit(String table, String id, JsonNode jsonValue) {
        //search a document that doesn't exist
        Query query = new Query();
        // query.addCriteria(Criteria.where("name").is("appleZ"));
        // TODO
    }
}
