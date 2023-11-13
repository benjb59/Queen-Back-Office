package fr.insee.queen.api.campaign.service;

import fr.insee.queen.api.campaign.service.model.QuestionnaireModel;

import java.util.List;

public interface QuestionnaireModelService {
    List<String> getQuestionnaireIds(String campaignId);

    String getQuestionnaireData(String id);

    void createQuestionnaire(QuestionnaireModel qm);

    void updateQuestionnaire(QuestionnaireModel qm);

    List<String> getQuestionnaireDatas(String campaignId);
}
