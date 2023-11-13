package fr.insee.queen.api.integration.controller.builder.dummy;

import fr.insee.queen.api.integration.controller.component.builder.NomenclatureBuilder;
import fr.insee.queen.api.integration.controller.dto.output.IntegrationResultUnitDto;
import lombok.Getter;

import java.util.List;
import java.util.zip.ZipFile;

public class NomenclatureFakeBuilder implements NomenclatureBuilder {

    @Getter
    private final List<IntegrationResultUnitDto> results = List.of(
            IntegrationResultUnitDto.integrationResultUnitUpdated("id-nomenclature1"),
            IntegrationResultUnitDto.integrationResultUnitCreated("id-nomenclature2")
    );

    @Override
    public List<IntegrationResultUnitDto> build(ZipFile integrationZipFile) {
        return results;
    }
}
