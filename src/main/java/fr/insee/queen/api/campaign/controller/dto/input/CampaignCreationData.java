package fr.insee.queen.api.campaign.controller.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.insee.queen.api.campaign.service.model.Campaign;
import fr.insee.queen.api.web.validation.IdValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * Data used for campaign creation
 *
 * @param id campaign id
 * @param label campaign labe
 * @param questionnaireIds list of questionnaire ids linked to the campaign
 * @param metadata campaign metadata
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CampaignCreationData(
        @IdValid
        String id,
        @NotBlank
        String label,
        @NotEmpty
        Set<String> questionnaireIds,
        @Valid
        MetadataCreationData metadata) {

    public static Campaign toModel(CampaignCreationData campaign) {
        ObjectNode metadataValue = JsonNodeFactory.instance.objectNode();
        if (campaign.metadata() != null) {
            metadataValue = campaign.metadata.value();
        }
        return new Campaign(campaign.id, campaign.label, campaign.questionnaireIds, metadataValue.toString());
    }
}
