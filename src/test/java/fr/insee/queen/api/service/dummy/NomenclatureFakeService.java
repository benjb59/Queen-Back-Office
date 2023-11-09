package fr.insee.queen.api.service.dummy;

import fr.insee.queen.api.dto.input.NomenclatureInputDto;
import fr.insee.queen.api.dto.nomenclature.NomenclatureDto;
import fr.insee.queen.api.service.questionnaire.NomenclatureService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class NomenclatureFakeService implements NomenclatureService {
    @Setter
    private List<String> nonExistingNomenclatures = new ArrayList<>();

    @Getter
    private boolean saved = false;

    @Override
    public NomenclatureDto getNomenclature(String id) {
        return null;
    }

    @Override
    public boolean existsById(String id) {
        return !nonExistingNomenclatures.contains(id);
    }

    @Override
    public boolean areNomenclaturesValid(Set<String> nomenclatureIds) {
        return false;
    }

    @Override
    public void saveNomenclature(NomenclatureInputDto nomenclature) {
        saved = true;
    }

    @Override
    public List<String> getAllNomenclatureIds() {
        return null;
    }

    @Override
    public List<String> findRequiredNomenclatureByCampaign(String campaignId) {
        return null;
    }

    @Override
    public List<String> findRequiredNomenclatureByQuestionnaire(String questionnaireId) {
        return null;
    }
}
