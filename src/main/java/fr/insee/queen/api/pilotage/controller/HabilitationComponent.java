package fr.insee.queen.api.pilotage.controller;

import fr.insee.queen.api.pilotage.service.PilotageRole;
import org.springframework.security.core.Authentication;

public interface HabilitationComponent {
    /**
     * Check if the current user has defined roles for a survey unit
     *
     * @param auth         current user
     * @param surveyUnitId the survey unit the user want to access
     * @param roles        the roles the current user should have to access the survey unit
     */
    void checkHabilitations(Authentication auth, String surveyUnitId, PilotageRole... roles);
}
