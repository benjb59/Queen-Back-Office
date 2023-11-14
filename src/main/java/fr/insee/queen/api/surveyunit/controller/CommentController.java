package fr.insee.queen.api.surveyunit.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.insee.queen.api.configuration.auth.AuthorityRole;
import fr.insee.queen.api.pilotage.controller.HabilitationComponent;
import fr.insee.queen.api.pilotage.service.PilotageRole;
import fr.insee.queen.api.surveyunit.service.CommentService;
import fr.insee.queen.api.web.validation.IdValid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * These endpoints handle the comment filled by a survey unit at the nd of the questionnaire*
 */
@RestController
@Tag(name = "06. Survey units")
@RequestMapping(path = "/api")
@Slf4j
@AllArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;
    private final HabilitationComponent habilitationComponent;

    /**
     * Retrieve the comment linked to the survey unit
     *
     * @param surveyUnitId the id of survey unit
     * @param auth         authenticated user
     * @return {@link String} the comment linked to the survey unit
     */
    @Operation(summary = "Get comment for a survey unit")
    @GetMapping(path = "/survey-unit/{id}/comment")
    @PreAuthorize(AuthorityRole.HAS_ANY_ROLE)
    public String getCommentBySurveyUnit(@IdValid @PathVariable(value = "id") String surveyUnitId,
                                         Authentication auth) {
        log.info("GET comment for reporting unit with id {}", surveyUnitId);
        habilitationComponent.checkHabilitations(auth, surveyUnitId, PilotageRole.INTERVIEWER);
        return commentService.getComment(surveyUnitId);
    }

    /**
     * Update the comment linked to the survey unit
     *
     * @param commentValue the value to update
     * @param surveyUnitId the id of the survey unit
     * @param auth         authenticated user
     */
    @Operation(summary = "Update comment for a survey unit")
    @PutMapping(path = "/survey-unit/{id}/comment")
    @PreAuthorize(AuthorityRole.HAS_ANY_ROLE)
    public void setComment(@NotNull @RequestBody ObjectNode commentValue,
                           @IdValid @PathVariable(value = "id") String surveyUnitId,
                           Authentication auth) {
        log.info("PUT comment for reporting unit with id {}", surveyUnitId);
        habilitationComponent.checkHabilitations(auth, surveyUnitId, PilotageRole.INTERVIEWER);
        commentService.updateComment(surveyUnitId, commentValue);
    }
}
