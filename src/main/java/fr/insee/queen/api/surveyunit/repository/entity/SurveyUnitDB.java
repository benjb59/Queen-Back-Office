package fr.insee.queen.api.surveyunit.repository.entity;

import fr.insee.queen.api.campaign.repository.entity.CampaignDB;
import fr.insee.queen.api.campaign.repository.entity.QuestionnaireModelDB;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Survey unit entity
 */
@Entity
@Table(name = "survey_unit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyUnitDB {
    /**
     * survey unit id
     */
    @Id
    @org.springframework.data.annotation.Id
    private String id;

    /**
     * campaign of the survey unit
     */
    @ManyToOne
    private CampaignDB campaign;

    /**
     * questionnaire model of the survey unit
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire_model_id", referencedColumnName = "id")
    private QuestionnaireModelDB questionnaireModel;

    @OneToOne(mappedBy = "surveyUnit", cascade = CascadeType.ALL)
    private StateDataDB stateData;

    @OneToOne(mappedBy = "surveyUnit", cascade = CascadeType.ALL)
    private PersonalizationDB personalization;

    @OneToOne(mappedBy = "surveyUnit", cascade = CascadeType.ALL)
    private DataDB data;

    @OneToOne(mappedBy = "surveyUnit", cascade = CascadeType.ALL)
    private CommentDB comment;

    public SurveyUnitDB(String id, CampaignDB campaign, QuestionnaireModelDB questionnaireModel) {
        this.id = id;
        this.campaign = campaign;
        this.questionnaireModel = questionnaireModel;
    }
}
