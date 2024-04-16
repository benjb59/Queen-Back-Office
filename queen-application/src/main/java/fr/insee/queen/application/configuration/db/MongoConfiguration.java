package fr.insee.queen.application.configuration.db;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ConditionalOnProperty(name = "feature.mongo.enabled", havingValue = "true")
@ComponentScan(basePackages = {"fr.insee.queen.infrastructure.mongo"})
@EnableMongoRepositories(basePackages = {"fr.insee.queen.infrastructure.mongo"})
@Configuration
public class MongoConfiguration {
}
