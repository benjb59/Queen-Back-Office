package fr.insee.queen.api.pilotage.controller.dummy;

import fr.insee.queen.api.pilotage.controller.habilitation.HabilitationComponent;
import fr.insee.queen.api.pilotage.service.PilotageRole;
import lombok.Getter;
import lombok.Setter;

public class HabilitationFakeComponent implements HabilitationComponent {
    @Getter
    private boolean checked = false;
    @Setter
    private boolean isCampaignClosed = true;

    @Override
    public void checkHabilitations(String surveyUnitId, PilotageRole... roles) {
        checked = true;
    }

    @Override
    public boolean isClosed(String campaignId) {
        return this.isCampaignClosed;
    }
}
