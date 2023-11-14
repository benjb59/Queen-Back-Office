package fr.insee.queen.api.surveyunit.repository.jpa;

import fr.insee.queen.api.surveyunit.repository.entity.CommentDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository to handle a survey unit's comment
 */
@Repository
public interface CommentJpaRepository extends JpaRepository<CommentDB, UUID> {
    /**
     * Delete all survey units comment for a campaign
     *
     * @param campaignId campaign id
     */
    @Transactional
    @Modifying
    @Query(value = """
            delete from comment c where id in (
                select c.id from survey_unit s
                    where s.id = c.survey_unit_id
                    and s.campaign_id = :campaignId
            )""", nativeQuery = true)
    void deleteComments(String campaignId);

    /**
     * Update comment for a survey unit
     *
     * @param surveyUnitId survey unit id
     * @param comment json comment to set
     * @return number of updated rows
     */
    @Transactional
    @Modifying
    @Query("update CommentDB c set c.value = :comment where c.surveyUnit.id = :surveyUnitId")
    int updateComment(String surveyUnitId, String comment);

    /**
     * Find the comment of a survey unit
     *
     * @param surveyUnitId survey unit id
     * @return an optional of the comment (json format)
     */
    @Query("select s.comment.value from SurveyUnitDB s where s.id=:surveyUnitId")
    Optional<String> findComment(String surveyUnitId);

    /**
     * Delete comment of a survey unit
     * @param surveyUnitId survey unit id
     */
    void deleteBySurveyUnitId(String surveyUnitId);
}
