package qa.engineeric.product.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import qa.engineeric.product.domain.Reviews;
import qa.engineeric.product.repository.ReviewsRepository;
import qa.engineeric.product.service.ReviewsService;
import qa.engineeric.product.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link qa.engineeric.product.domain.Reviews}.
 */
@RestController
@RequestMapping("/api")
public class ReviewsResource {

    private final Logger log = LoggerFactory.getLogger(ReviewsResource.class);

    private static final String ENTITY_NAME = "productReviews";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewsService reviewsService;

    private final ReviewsRepository reviewsRepository;

    public ReviewsResource(ReviewsService reviewsService, ReviewsRepository reviewsRepository) {
        this.reviewsService = reviewsService;
        this.reviewsRepository = reviewsRepository;
    }

    /**
     * {@code POST  /reviews} : Create a new reviews.
     *
     * @param reviews the reviews to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviews, or with status {@code 400 (Bad Request)} if the reviews has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reviews")
    public ResponseEntity<Reviews> createReviews(@RequestBody Reviews reviews) throws URISyntaxException {
        log.debug("REST request to save Reviews : {}", reviews);
        if (reviews.getId() != null) {
            throw new BadRequestAlertException("A new reviews cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reviews result = reviewsService.save(reviews);
        return ResponseEntity
            .created(new URI("/api/reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /reviews/:id} : Updates an existing reviews.
     *
     * @param id the id of the reviews to save.
     * @param reviews the reviews to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviews,
     * or with status {@code 400 (Bad Request)} if the reviews is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviews couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reviews/{id}")
    public ResponseEntity<Reviews> updateReviews(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Reviews reviews
    ) throws URISyntaxException {
        log.debug("REST request to update Reviews : {}, {}", id, reviews);
        if (reviews.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviews.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Reviews result = reviewsService.save(reviews);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviews.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /reviews/:id} : Partial updates given fields of an existing reviews, field will ignore if it is null
     *
     * @param id the id of the reviews to save.
     * @param reviews the reviews to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviews,
     * or with status {@code 400 (Bad Request)} if the reviews is not valid,
     * or with status {@code 404 (Not Found)} if the reviews is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviews couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Reviews> partialUpdateReviews(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Reviews reviews
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reviews partially : {}, {}", id, reviews);
        if (reviews.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviews.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reviews> result = reviewsService.partialUpdate(reviews);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviews.getId()));
    }

    /**
     * {@code GET  /reviews} : get all the reviews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviews in body.
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<Reviews>> getAllReviews(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Reviews");
        Page<Reviews> page = reviewsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reviews/:id} : get the "id" reviews.
     *
     * @param id the id of the reviews to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviews, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reviews/{id}")
    public ResponseEntity<Reviews> getReviews(@PathVariable String id) {
        log.debug("REST request to get Reviews : {}", id);
        Optional<Reviews> reviews = reviewsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviews);
    }

    /**
     * {@code DELETE  /reviews/:id} : delete the "id" reviews.
     *
     * @param id the id of the reviews to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReviews(@PathVariable String id) {
        log.debug("REST request to delete Reviews : {}", id);
        reviewsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
