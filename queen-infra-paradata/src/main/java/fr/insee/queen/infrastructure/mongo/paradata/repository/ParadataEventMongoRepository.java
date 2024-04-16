package fr.insee.queen.infrastructure.mongo.paradata.repository;

import fr.insee.queen.infrastructure.mongo.paradata.document.ParadataEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * JPA repository to handle paradata in DB
 */
@Repository
public interface ParadataEventMongoRepository extends MongoRepository<ParadataEventDocument, UUID> {
}
