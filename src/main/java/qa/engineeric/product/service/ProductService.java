package qa.engineeric.product.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qa.engineeric.product.domain.Product;
import qa.engineeric.product.repository.ProductRepository;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Save a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    public Product save(Product product) {
        log.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Partially update a product.
     *
     * @param product the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getUserStoreOwnerId() != null) {
                    existingProduct.setUserStoreOwnerId(product.getUserStoreOwnerId());
                }
                if (product.getProductName() != null) {
                    existingProduct.setProductName(product.getProductName());
                }
                if (product.getProductNameInArabic() != null) {
                    existingProduct.setProductNameInArabic(product.getProductNameInArabic());
                }
                if (product.getProductDescription() != null) {
                    existingProduct.setProductDescription(product.getProductDescription());
                }
                if (product.getProductDescriptionInArabic() != null) {
                    existingProduct.setProductDescriptionInArabic(product.getProductDescriptionInArabic());
                }
                if (product.getPrice() != null) {
                    existingProduct.setPrice(product.getPrice());
                }
                if (product.getImage() != null) {
                    existingProduct.setImage(product.getImage());
                }
                if (product.getImageContentType() != null) {
                    existingProduct.setImageContentType(product.getImageContentType());
                }
                if (product.getImageUrl() != null) {
                    existingProduct.setImageUrl(product.getImageUrl());
                }
                if (product.getKeywords() != null) {
                    existingProduct.setKeywords(product.getKeywords());
                }
                if (product.getDateAdded() != null) {
                    existingProduct.setDateAdded(product.getDateAdded());
                }
                if (product.getDateModified() != null) {
                    existingProduct.setDateModified(product.getDateModified());
                }

                return existingProduct;
            })
            .map(productRepository::save);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Product> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable);
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Product> findOne(String id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
