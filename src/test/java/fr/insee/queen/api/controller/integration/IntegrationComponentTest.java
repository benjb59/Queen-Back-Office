package fr.insee.queen.api.controller.integration;

import fr.insee.queen.api.controller.integration.builder.dummy.CampaignFakeBuilder;
import fr.insee.queen.api.controller.integration.builder.dummy.NomenclatureFakeBuilder;
import fr.insee.queen.api.controller.integration.builder.dummy.QuestionnaireFakeBuilder;
import fr.insee.queen.api.controller.integration.component.IntegrationComponent;
import fr.insee.queen.api.controller.integration.component.exception.IntegrationComponentException;
import fr.insee.queen.api.dto.integration.IntegrationResultDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class IntegrationComponentTest {

    private IntegrationComponent integrationComponent;
    private QuestionnaireFakeBuilder questionnaireBuilder;
    private NomenclatureFakeBuilder nomenclatureBuilder;
    private CampaignFakeBuilder campaignBuilder;

    @BeforeEach
    void init() {
        questionnaireBuilder = new QuestionnaireFakeBuilder();
        nomenclatureBuilder = new NomenclatureFakeBuilder();
        campaignBuilder = new CampaignFakeBuilder();
        integrationComponent = new IntegrationComponent(nomenclatureBuilder, campaignBuilder, questionnaireBuilder);
    }

    @Test
    @DisplayName("on integration, when file is not a zip, throw exception")
    void integrate01() {
        MultipartFile uploadedFile = new MockMultipartFile("file", "hello.txt", MediaType.APPLICATION_JSON_VALUE, "Hello, World!".getBytes()
        );
        assertThatThrownBy(() -> integrationComponent.integrateContext(uploadedFile)).isInstanceOf(IntegrationComponentException.class);
    }

    @Test
    @DisplayName("on integration, return an integration result list")
    void integrate02() throws IOException {
        InputStream zipInputStream = getClass().getClassLoader().getResourceAsStream("integration/integration-component.zip");
        MultipartFile uploadedFile = new MockMultipartFile("file", "hello.txt", MediaType.APPLICATION_JSON_VALUE, zipInputStream);
        IntegrationResultDto result = integrationComponent.integrateContext(uploadedFile);
        log.error(result.toString());
        assertThat(result.campaign()).isEqualTo(campaignBuilder.resultSuccess());
        assertThat(result.nomenclatures()).isEqualTo(nomenclatureBuilder.results());
        assertThat(result.questionnaireModels()).isEqualTo(questionnaireBuilder.results());
    }

    @Test
    @DisplayName("when integrating campaign result in errors, then do not process questionnaires")
    void integrate03() throws IOException {
        campaignBuilder.resultIsInErrorState(true);
        InputStream zipInputStream = getClass().getClassLoader().getResourceAsStream("integration/integration-component.zip");
        MultipartFile uploadedFile = new MockMultipartFile("file", "hello.txt", MediaType.APPLICATION_JSON_VALUE, zipInputStream);
        IntegrationResultDto result = integrationComponent.integrateContext(uploadedFile);
        assertThat(result.campaign()).isEqualTo(campaignBuilder.resultError());
        assertThat(result.nomenclatures()).isEqualTo(nomenclatureBuilder.results());
        assertThat(result.questionnaireModels()).isNull();
    }
}
