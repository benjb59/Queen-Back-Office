package fr.insee.queen.infrastructure.mongo.paradata.document;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value="paradata")
public class ParadataEventDocument {

    /**
     * The id of the ParadataEvent
     */
    @Id
    private UUID id;

    /**
     * The value of data (jsonb format)
     */
    private ObjectNode value;

    private String surveyUnitId;
}
