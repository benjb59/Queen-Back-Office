package fr.insee.queen.api.integration.controller.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.insee.queen.api.campaign.service.model.Campaign;
import fr.insee.queen.api.web.validation.IdValid;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CampaignIntegrationData(
        @IdValid
        String id,
        @NotBlank
        String label,
        ObjectNode metadata) {

    public static Campaign toModel(CampaignIntegrationData campaign) {
        ObjectNode metadata = campaign.metadata();
        if(campaign.metadata() == null) {
            metadata = JsonNodeFactory.instance.objectNode();
        }
        return new Campaign(campaign.id.toUpperCase(), campaign.label, metadata.toString());
    }
}
