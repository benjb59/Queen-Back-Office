package fr.insee.queen.api.utils;

import fr.insee.queen.api.campaign.service.dummy.CampaignFakeService;
import fr.insee.queen.api.campaign.service.model.CampaignSummary;
import fr.insee.queen.api.pilotage.controller.interviewer.NoPilotageInterviewerComponent;
import fr.insee.queen.api.pilotage.service.model.PilotageCampaign;
import fr.insee.queen.api.surveyunit.service.dummy.SurveyUnitFakeService;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnit;
import fr.insee.queen.api.surveyunit.service.model.SurveyUnitSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NoPilotageInterviewerComponentTest {
    private SurveyUnitFakeService surveyUnitService;
    private CampaignFakeService campaignService;
    private NoPilotageInterviewerComponent pilotageComponent;

    @BeforeEach
    void init() {
        surveyUnitService = new SurveyUnitFakeService();
        campaignService = new CampaignFakeService();
    }

    @Test
    @DisplayName("When retrieving survey units by campaign, return all survey units from campaign")
    void testGetSUByCampaign() {
        pilotageComponent = new NoPilotageInterviewerComponent(surveyUnitService, campaignService);
        List<SurveyUnitSummary> surveyUnits = pilotageComponent.getSurveyUnitsByCampaign("campaign-id");
        assertThat(surveyUnits).isEqualTo(surveyUnitService.surveyUnitSummaries());
    }

    @Test
    @DisplayName("When retrieving campaigns for interviewer, return all campaigns")
    void testGetInterviewerCampaigns() {
        pilotageComponent = new NoPilotageInterviewerComponent(surveyUnitService, campaignService);
        List<PilotageCampaign> campaignSummaries = pilotageComponent.getInterviewerCampaigns();
        for(CampaignSummary campaign : CampaignFakeService.CAMPAIGN_SUMMARY_LIST) {
            assertThat(campaignSummaries).contains(new PilotageCampaign(campaign.id(), campaign.questionnaireIds().stream().toList()));
        }
    }

    @Test
    @DisplayName("When retrieving survey units for interviewer, return all survey units")
    void testGetInterviewerSurveyUnits() {
        pilotageComponent = new NoPilotageInterviewerComponent(surveyUnitService, campaignService);
        List<SurveyUnit> surveyUnits = pilotageComponent.getInterviewerSurveyUnits();
        assertThat(surveyUnitService.allSurveyUnits()).isEqualTo(surveyUnits);

    }
}
