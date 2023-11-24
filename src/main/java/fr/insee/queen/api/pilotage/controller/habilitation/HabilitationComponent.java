package fr.insee.queen.api.pilotage.controller.habilitation;

import fr.insee.queen.api.pilotage.service.PilotageRole;

public interface HabilitationComponent {
    /**
     * Check if the current user has defined roles for a survey unit
     *
     * @param surveyUnitId the survey unit the user want to access
     * @param roles        the roles the current user should have to access the survey unit
     */
    void checkHabilitations(String surveyUnitId, PilotageRole... roles);

    /**
     * Check if a campaign is closed
     * @param campaignId campaign id
     * @return true if campaign is closed, false otherwise
     */
    boolean isClosed(String campaignId);
}
