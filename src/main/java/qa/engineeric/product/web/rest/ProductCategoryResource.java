package qa.engineeric.product.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qa.engineeric.product.domain.CategoriesNames;
import qa.engineeric.product.domain.Product;
import qa.engineeric.product.domain.ProductCategory;
import qa.engineeric.product.domain.ProductsForApis;
import qa.engineeric.product.repository.ProductCategoryRepository;
import qa.engineeric.product.service.ProductCategoryService;
import qa.engineeric.product.service.ReviewsService;
import qa.engineeric.product.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link qa.engineeric.product.domain.ProductCategory}.
 */
@RestController
@RequestMapping("/api")
public class ProductCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryResource.class);

    private static final String ENTITY_NAME = "productProductCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductCategoryService productCategoryService;

    private final ProductCategoryRepository productCategoryRepository;
    private final ReviewsService reviewsService;

    public ProductCategoryResource(
        ProductCategoryService productCategoryService,
        ProductCategoryRepository productCategoryRepository,
        ReviewsService reviewsService
    ) {
        this.productCategoryService = productCategoryService;
        this.productCategoryRepository = productCategoryRepository;
        this.reviewsService = reviewsService;
    }

    /**
     * {@code POST  /product-categories} : Create a new productCategory.
     *
     * @param productCategory the productCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productCategory, or with status {@code 400 (Bad Request)} if the productCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-categories")
    public ResponseEntity<ProductCategory> createProductCategory(@Valid @RequestBody ProductCategory productCategory)
        throws URISyntaxException {
        log.debug("REST request to save ProductCategory : {}", productCategory);
        if (productCategory.getId() != null) {
            throw new BadRequestAlertException("A new productCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductCategory result = productCategoryService.save(productCategory);
        return ResponseEntity
            .created(new URI("/api/product-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /product-categories/:id} : Updates an existing productCategory.
     *
     * @param id the id of the productCategory to save.
     * @param productCategory the productCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategory,
     * or with status {@code 400 (Bad Request)} if the productCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-categories/{id}")
    public ResponseEntity<ProductCategory> updateProductCategory(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ProductCategory productCategory
    ) throws URISyntaxException {
        log.debug("REST request to update ProductCategory : {}, {}", id, productCategory);
        if (productCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductCategory result = productCategoryService.save(productCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCategory.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-categories/:id} : Partial updates given fields of an existing productCategory, field will ignore if it is null
     *
     * @param id the id of the productCategory to save.
     * @param productCategory the productCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategory,
     * or with status {@code 400 (Bad Request)} if the productCategory is not valid,
     * or with status {@code 404 (Not Found)} if the productCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the productCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductCategory> partialUpdateProductCategory(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ProductCategory productCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductCategory partially : {}, {}", id, productCategory);
        if (productCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductCategory> result = productCategoryService.partialUpdate(productCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCategory.getId())
        );
    }

    /**
     * {@code GET  /product-categories} : get all the productCategories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productCategories in body.
     */
    @GetMapping("/product-categories")
    public List<ProductCategory> getAllProductCategories(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ProductCategories");
        return productCategoryService.findAll();
    }

    @GetMapping("/product-categories/store/web/categoriesNames/{userStoreOwnerId}/{webKey}")
    public List<CategoriesNames> getAllProductCategoriesByNames(
        @PathVariable String userStoreOwnerId,
        @PathVariable String webKey,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all ProductCategories");
        List<CategoriesNames> categoryName = new ArrayList<>();

        List<ProductCategory> productCategory = productCategoryService.findAllByUserStoreOwnerIdAndWebKey(userStoreOwnerId, webKey);
        for (int i = 0; i < productCategory.size(); i++) {
            CategoriesNames objects = new CategoriesNames();
            objects.setId(productCategory.get(i).getId());
            objects.setCategoriesName(productCategory.get(i).getProductCategoryName());
            objects.setImageUrl(productCategory.get(i).getImageUrl());
            categoryName.add(objects);
        }
        return categoryName;
    }

    @GetMapping("/product-categories/store/web/{userStoreOwnerId}/{webKey}")
    public List<ProductsForApis> getAllProductCategoriesByUserStoreOwnerIdAndWebKey(
        @PathVariable String userStoreOwnerId,
        @PathVariable String webKey,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all ProductCategories");
        List<ProductCategory> productCategory = productCategoryService.findAllByUserStoreOwnerIdAndWebKey(userStoreOwnerId, webKey);
        Product productCategoryNew = new Product();

        List<ProductsForApis> products = new ArrayList<>();

        for (int i = 0; i < productCategory.size(); i++) {
            Iterator<Product> IteratorProduct = productCategory.get(i).getProducts().iterator();

            while (IteratorProduct.hasNext()) {
                productCategoryNew = IteratorProduct.next();
                ProductsForApis productsForApis = new ProductsForApis();
                productsForApis.setId(productCategoryNew.getId());
                productsForApis.setCategory(productCategory.get(i).getProductCategoryName());
                productsForApis.setDescription(productCategoryNew.getProductDescription());
                productsForApis.setTitle(productCategoryNew.getProductName());
                productsForApis.setPrice(productCategoryNew.getPrice());
                productsForApis.setImage(productCategoryNew.getImageUrl());
                productsForApis.setUserStoreOwnerId(productCategory.get(i).getUserStoreOwnerId());
                productsForApis.setWebKey(productCategory.get(i).getWebKey());
                productsForApis.setRating(reviewsService.findAllByProdcutsId(productCategoryNew.getId()));

                products.add(productsForApis);
            }
        }

        return products;
    }

    /**
     * {@code GET  /product-categories/:id} : get the "id" productCategory.
     *
     * @param id the id of the productCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-categories/{id}")
    public ResponseEntity<ProductCategory> getProductCategory(@PathVariable String id) {
        log.debug("REST request to get ProductCategory : {}", id);
        Optional<ProductCategory> productCategory = productCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productCategory);
    }

    /**
     * {@code DELETE  /product-categories/:id} : delete the "id" productCategory.
     *
     * @param id the id of the productCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-categories/{id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable String id) {
        log.debug("REST request to delete ProductCategory : {}", id);
        productCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
