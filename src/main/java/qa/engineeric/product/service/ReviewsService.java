package qa.engineeric.product.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qa.engineeric.product.domain.Reviews;
import qa.engineeric.product.repository.ReviewsRepository;

/**
 * Service Implementation for managing {@link Reviews}.
 */
@Service
public class ReviewsService {

    private final Logger log = LoggerFactory.getLogger(ReviewsService.class);

    private final ReviewsRepository reviewsRepository;

    public ReviewsService(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    /**
     * Save a reviews.
     *
     * @param reviews the entity to save.
     * @return the persisted entity.
     */
    public Reviews save(Reviews reviews) {
        log.debug("Request to save Reviews : {}", reviews);
        return reviewsRepository.save(reviews);
    }

    /**
     * Partially update a reviews.
     *
     * @param reviews the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Reviews> partialUpdate(Reviews reviews) {
        log.debug("Request to partially update Reviews : {}", reviews);

        return reviewsRepository
            .findById(reviews.getId())
            .map(existingReviews -> {
                if (reviews.getCustomerId() != null) {
                    existingReviews.setCustomerId(reviews.getCustomerId());
                }
                if (reviews.getOrderId() != null) {
                    existingReviews.setOrderId(reviews.getOrderId());
                }
                if (reviews.getReview() != null) {
                    existingReviews.setReview(reviews.getReview());
                }
                if (reviews.getRating() != null) {
                    existingReviews.setRating(reviews.getRating());
                }
                if (reviews.getCreatedAt() != null) {
                    existingReviews.setCreatedAt(reviews.getCreatedAt());
                }
                if (reviews.getProdcutsId() != null) {
                    existingReviews.setProdcutsId(reviews.getProdcutsId());
                }

                return existingReviews;
            })
            .map(reviewsRepository::save);
    }

    /**
     * Get all the reviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Reviews> findAll(Pageable pageable) {
        log.debug("Request to get all Reviews");
        return reviewsRepository.findAll(pageable);
    }

    /**
     * Get one reviews by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Reviews> findOne(String id) {
        log.debug("Request to get Reviews : {}", id);
        return reviewsRepository.findById(id);
    }

    /**
     * Delete the reviews by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Reviews : {}", id);
        reviewsRepository.deleteById(id);
    }
}
