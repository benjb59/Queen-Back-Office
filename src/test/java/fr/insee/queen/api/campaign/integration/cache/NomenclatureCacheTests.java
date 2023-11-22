package fr.insee.queen.api.campaign.integration.cache;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import fr.insee.queen.api.campaign.service.NomenclatureService;
import fr.insee.queen.api.campaign.service.model.Nomenclature;
import fr.insee.queen.api.configuration.cache.CacheName;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
class NomenclatureCacheTests {

    @Autowired
    private NomenclatureService nomenclatureService;

    @Autowired
    private CacheManager cacheManager;

    @AfterEach
    public void clearCaches() {
        for (String cacheName : cacheManager.getCacheNames()) {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        }
    }

    @Test
    @DisplayName("When saving nomenclature, evict the associated nomenclature in nomenclature cache")
    void check_nomenclature_cache() {
        String nomenclatureId = "nomenclature-cache-id";

        // create nomenclature
        nomenclatureService.saveNomenclature(new Nomenclature(nomenclatureId, "label", JsonNodeFactory.instance.arrayNode().toString()));
        assertThat(Objects.requireNonNull(cacheManager.getCache(CacheName.NOMENCLATURE)).get(nomenclatureId)).isNull();

        // when retrieving nomenclature, cache is created
        Nomenclature nomenclature = nomenclatureService.getNomenclature(nomenclatureId);
        Nomenclature nomenclatureCache = Objects.requireNonNull(cacheManager.getCache(CacheName.NOMENCLATURE)).get(nomenclatureId, Nomenclature.class);
        assertThat(nomenclature).isEqualTo(nomenclatureCache);

        // when updating nomenclature, cache is evicted
        nomenclatureService.saveNomenclature(new Nomenclature(nomenclatureId, "label2", JsonNodeFactory.instance.arrayNode().toString()));
        assertThat(Objects.requireNonNull(cacheManager.getCache(CacheName.NOMENCLATURE)).get(nomenclatureId)).isNull();

        // when retrieving nomenclature, cache is created
        nomenclature = nomenclatureService.getNomenclature(nomenclatureId);
        nomenclatureCache = Objects.requireNonNull(cacheManager.getCache(CacheName.NOMENCLATURE)).get(nomenclatureId, Nomenclature.class);
        assertThat(nomenclature).isEqualTo(nomenclatureCache);
    }
}