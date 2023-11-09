package fr.insee.queen.api.dto.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationResultDto {
	@JsonProperty
	private IntegrationResultUnitDto campaign;
	@JsonProperty
	private List<IntegrationResultUnitDto> nomenclatures;
	@JsonProperty
	private List<IntegrationResultUnitDto> questionnaireModels;
}
