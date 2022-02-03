package qa.engineeric.product.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qa.engineeric.product.domain.ProductCategory;
import qa.engineeric.product.repository.ProductCategoryRepository;

/**
 * Service Implementation for managing {@link ProductCategory}.
 */
@Service
public class ProductCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * Save a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    public ProductCategory save(ProductCategory productCategory) {
        log.debug("Request to save ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    /**
     * Partially update a productCategory.
     *
     * @param productCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductCategory> partialUpdate(ProductCategory productCategory) {
        log.debug("Request to partially update ProductCategory : {}", productCategory);

        return productCategoryRepository
            .findById(productCategory.getId())
            .map(existingProductCategory -> {
                if (productCategory.getUserStoreOwnerId() != null) {
                    existingProductCategory.setUserStoreOwnerId(productCategory.getUserStoreOwnerId());
                }
                if (productCategory.getProductCategoryName() != null) {
                    existingProductCategory.setProductCategoryName(productCategory.getProductCategoryName());
                }
                if (productCategory.getProductCategoryNameInArabic() != null) {
                    existingProductCategory.setProductCategoryNameInArabic(productCategory.getProductCategoryNameInArabic());
                }
                if (productCategory.getProductCategoryDescription() != null) {
                    existingProductCategory.setProductCategoryDescription(productCategory.getProductCategoryDescription());
                }
                if (productCategory.getProductCategoryDescriptionInArabic() != null) {
                    existingProductCategory.setProductCategoryDescriptionInArabic(productCategory.getProductCategoryDescriptionInArabic());
                }
                if (productCategory.getSortOrder() != null) {
                    existingProductCategory.setSortOrder(productCategory.getSortOrder());
                }
                if (productCategory.getDateAdded() != null) {
                    existingProductCategory.setDateAdded(productCategory.getDateAdded());
                }
                if (productCategory.getDateModified() != null) {
                    existingProductCategory.setDateModified(productCategory.getDateModified());
                }
                if (productCategory.getStatus() != null) {
                    existingProductCategory.setStatus(productCategory.getStatus());
                }

                return existingProductCategory;
            })
            .map(productCategoryRepository::save);
    }

    /**
     * Get all the productCategories.
     *
     * @return the list of entities.
     */
    public List<ProductCategory> findAll() {
        log.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the productCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductCategory> findAllWithEagerRelationships(Pageable pageable) {
        return productCategoryRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ProductCategory> findOne(String id) {
        log.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ProductCategory : {}", id);
        productCategoryRepository.deleteById(id);
    }
}
