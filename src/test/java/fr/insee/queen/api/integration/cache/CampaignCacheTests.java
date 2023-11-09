package fr.insee.queen.api.integration.cache;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import fr.insee.queen.api.configuration.cache.CacheName;
import fr.insee.queen.api.domain.CampaignData;
import fr.insee.queen.api.service.campaign.CampaignExistenceService;
import fr.insee.queen.api.service.campaign.CampaignService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("cache-testing")
@ContextConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class CampaignCacheTests {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private CampaignExistenceService campaignExistenceService;

    @Autowired
    private CacheManager cacheManager;

    @AfterEach
    public void clearCaches() {
        for (String cacheName : cacheManager.getCacheNames()) {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        }
    }

    @Test
    @DisplayName("When handling campaigns, handle correctly cache for campaign existence")
    void check_campaign_existence_cache() throws Exception {
        String campaignId = "campaign-cache-id";
        assertThat(Objects.requireNonNull(cacheManager.getCache(CacheName.CAMPAIGN_EXIST)).get(campaignId)).isNull();
        campaignExistenceService.existsById(campaignId);
        boolean campaignExist = (boolean) Objects.requireNonNull(cacheManager.getCache(CacheName.CAMPAIGN_EXIST).get(campaignId).get());
        assertThat(campaignExist).isFalse();

        campaignService.createCampaign(new CampaignData(campaignId, "label", new HashSet<>(), JsonNodeFactory.instance.objectNode().toString()));
        assertThat(Objects.requireNonNull(cacheManager.getCache(CacheName.CAMPAIGN_EXIST)).get(campaignId)).isNull();

        campaignExistenceService.existsById(campaignId);
        campaignExist = (boolean) Objects.requireNonNull(cacheManager.getCache(CacheName.CAMPAIGN_EXIST).get(campaignId).get());
        assertThat(campaignExist).isTrue();

        campaignService.delete(campaignId);
        assertThat(Objects.requireNonNull(cacheManager.getCache(CacheName.CAMPAIGN_EXIST)).get(campaignId)).isNull();
    }
}
