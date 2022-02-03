package qa.engineeric.product.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static qa.engineeric.product.web.rest.TestUtil.sameInstant;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import qa.engineeric.product.IntegrationTest;
import qa.engineeric.product.domain.Reviews;
import qa.engineeric.product.repository.ReviewsRepository;

/**
 * Integration tests for the {@link ReviewsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReviewsResourceIT {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEW = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PRODCUTS_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODCUTS_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private MockMvc restReviewsMockMvc;

    private Reviews reviews;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reviews createEntity() {
        Reviews reviews = new Reviews()
            .customerId(DEFAULT_CUSTOMER_ID)
            .orderId(DEFAULT_ORDER_ID)
            .review(DEFAULT_REVIEW)
            .rating(DEFAULT_RATING)
            .createdAt(DEFAULT_CREATED_AT)
            .prodcutsId(DEFAULT_PRODCUTS_ID);
        return reviews;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reviews createUpdatedEntity() {
        Reviews reviews = new Reviews()
            .customerId(UPDATED_CUSTOMER_ID)
            .orderId(UPDATED_ORDER_ID)
            .review(UPDATED_REVIEW)
            .rating(UPDATED_RATING)
            .createdAt(UPDATED_CREATED_AT)
            .prodcutsId(UPDATED_PRODCUTS_ID);
        return reviews;
    }

    @BeforeEach
    public void initTest() {
        reviewsRepository.deleteAll();
        reviews = createEntity();
    }

    @Test
    void createReviews() throws Exception {
        int databaseSizeBeforeCreate = reviewsRepository.findAll().size();
        // Create the Reviews
        restReviewsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviews)))
            .andExpect(status().isCreated());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeCreate + 1);
        Reviews testReviews = reviewsList.get(reviewsList.size() - 1);
        assertThat(testReviews.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testReviews.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testReviews.getReview()).isEqualTo(DEFAULT_REVIEW);
        assertThat(testReviews.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testReviews.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReviews.getProdcutsId()).isEqualTo(DEFAULT_PRODCUTS_ID);
    }

    @Test
    void createReviewsWithExistingId() throws Exception {
        // Create the Reviews with an existing ID
        reviews.setId("existing_id");

        int databaseSizeBeforeCreate = reviewsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviews)))
            .andExpect(status().isBadRequest());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReviews() throws Exception {
        // Initialize the database
        reviewsRepository.save(reviews);

        // Get all the reviewsList
        restReviewsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviews.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].review").value(hasItem(DEFAULT_REVIEW)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].prodcutsId").value(hasItem(DEFAULT_PRODCUTS_ID)));
    }

    @Test
    void getReviews() throws Exception {
        // Initialize the database
        reviewsRepository.save(reviews);

        // Get the reviews
        restReviewsMockMvc
            .perform(get(ENTITY_API_URL_ID, reviews.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviews.getId()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.review").value(DEFAULT_REVIEW))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.prodcutsId").value(DEFAULT_PRODCUTS_ID));
    }

    @Test
    void getNonExistingReviews() throws Exception {
        // Get the reviews
        restReviewsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewReviews() throws Exception {
        // Initialize the database
        reviewsRepository.save(reviews);

        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();

        // Update the reviews
        Reviews updatedReviews = reviewsRepository.findById(reviews.getId()).get();
        updatedReviews
            .customerId(UPDATED_CUSTOMER_ID)
            .orderId(UPDATED_ORDER_ID)
            .review(UPDATED_REVIEW)
            .rating(UPDATED_RATING)
            .createdAt(UPDATED_CREATED_AT)
            .prodcutsId(UPDATED_PRODCUTS_ID);

        restReviewsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReviews.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReviews))
            )
            .andExpect(status().isOk());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
        Reviews testReviews = reviewsList.get(reviewsList.size() - 1);
        assertThat(testReviews.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testReviews.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testReviews.getReview()).isEqualTo(UPDATED_REVIEW);
        assertThat(testReviews.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testReviews.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReviews.getProdcutsId()).isEqualTo(UPDATED_PRODCUTS_ID);
    }

    @Test
    void putNonExistingReviews() throws Exception {
        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();
        reviews.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviews.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviews))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReviews() throws Exception {
        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();
        reviews.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviews))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReviews() throws Exception {
        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();
        reviews.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviews)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReviewsWithPatch() throws Exception {
        // Initialize the database
        reviewsRepository.save(reviews);

        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();

        // Update the reviews using partial update
        Reviews partialUpdatedReviews = new Reviews();
        partialUpdatedReviews.setId(reviews.getId());

        partialUpdatedReviews
            .customerId(UPDATED_CUSTOMER_ID)
            .orderId(UPDATED_ORDER_ID)
            .rating(UPDATED_RATING)
            .prodcutsId(UPDATED_PRODCUTS_ID);

        restReviewsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviews.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReviews))
            )
            .andExpect(status().isOk());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
        Reviews testReviews = reviewsList.get(reviewsList.size() - 1);
        assertThat(testReviews.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testReviews.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testReviews.getReview()).isEqualTo(DEFAULT_REVIEW);
        assertThat(testReviews.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testReviews.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReviews.getProdcutsId()).isEqualTo(UPDATED_PRODCUTS_ID);
    }

    @Test
    void fullUpdateReviewsWithPatch() throws Exception {
        // Initialize the database
        reviewsRepository.save(reviews);

        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();

        // Update the reviews using partial update
        Reviews partialUpdatedReviews = new Reviews();
        partialUpdatedReviews.setId(reviews.getId());

        partialUpdatedReviews
            .customerId(UPDATED_CUSTOMER_ID)
            .orderId(UPDATED_ORDER_ID)
            .review(UPDATED_REVIEW)
            .rating(UPDATED_RATING)
            .createdAt(UPDATED_CREATED_AT)
            .prodcutsId(UPDATED_PRODCUTS_ID);

        restReviewsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviews.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReviews))
            )
            .andExpect(status().isOk());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
        Reviews testReviews = reviewsList.get(reviewsList.size() - 1);
        assertThat(testReviews.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testReviews.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testReviews.getReview()).isEqualTo(UPDATED_REVIEW);
        assertThat(testReviews.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testReviews.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReviews.getProdcutsId()).isEqualTo(UPDATED_PRODCUTS_ID);
    }

    @Test
    void patchNonExistingReviews() throws Exception {
        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();
        reviews.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviews.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reviews))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReviews() throws Exception {
        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();
        reviews.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reviews))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReviews() throws Exception {
        int databaseSizeBeforeUpdate = reviewsRepository.findAll().size();
        reviews.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reviews)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reviews in the database
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReviews() throws Exception {
        // Initialize the database
        reviewsRepository.save(reviews);

        int databaseSizeBeforeDelete = reviewsRepository.findAll().size();

        // Delete the reviews
        restReviewsMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviews.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reviews> reviewsList = reviewsRepository.findAll();
        assertThat(reviewsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
