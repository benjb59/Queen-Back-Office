package fr.insee.queen.infrastructure.mongo.paradata.dao;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.insee.queen.domain.paradata.gateway.ParadataEventRepository;
import fr.insee.queen.infrastructure.mongo.paradata.document.ParadataEventDocument;
import fr.insee.queen.infrastructure.mongo.paradata.repository.ParadataEventMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Primary
@AllArgsConstructor
@Slf4j
public class ParadataEventMongoDao implements ParadataEventRepository {
    private final ParadataEventMongoRepository repository;

    @Override
    public void createParadataEvent(UUID id, ObjectNode paradataValue, String surveyUnitId) {
        ParadataEventDocument paradata = new ParadataEventDocument(id, paradataValue, surveyUnitId);
        repository.save(paradata);
    }
}
