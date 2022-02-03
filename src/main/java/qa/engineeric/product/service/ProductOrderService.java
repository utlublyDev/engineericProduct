package qa.engineeric.product.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qa.engineeric.product.domain.ProductOrder;
import qa.engineeric.product.repository.ProductOrderRepository;

/**
 * Service Implementation for managing {@link ProductOrder}.
 */
@Service
public class ProductOrderService {

    private final Logger log = LoggerFactory.getLogger(ProductOrderService.class);

    private final ProductOrderRepository productOrderRepository;

    public ProductOrderService(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    /**
     * Save a productOrder.
     *
     * @param productOrder the entity to save.
     * @return the persisted entity.
     */
    public ProductOrder save(ProductOrder productOrder) {
        log.debug("Request to save ProductOrder : {}", productOrder);
        return productOrderRepository.save(productOrder);
    }

    /**
     * Partially update a productOrder.
     *
     * @param productOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductOrder> partialUpdate(ProductOrder productOrder) {
        log.debug("Request to partially update ProductOrder : {}", productOrder);

        return productOrderRepository
            .findById(productOrder.getId())
            .map(existingProductOrder -> {
                if (productOrder.getUserStoreOwnerId() != null) {
                    existingProductOrder.setUserStoreOwnerId(productOrder.getUserStoreOwnerId());
                }
                if (productOrder.getUserId() != null) {
                    existingProductOrder.setUserId(productOrder.getUserId());
                }
                if (productOrder.getPlacedDate() != null) {
                    existingProductOrder.setPlacedDate(productOrder.getPlacedDate());
                }
                if (productOrder.getStatus() != null) {
                    existingProductOrder.setStatus(productOrder.getStatus());
                }
                if (productOrder.getCode() != null) {
                    existingProductOrder.setCode(productOrder.getCode());
                }
                if (productOrder.getInvoiceId() != null) {
                    existingProductOrder.setInvoiceId(productOrder.getInvoiceId());
                }
                if (productOrder.getCustomer() != null) {
                    existingProductOrder.setCustomer(productOrder.getCustomer());
                }

                return existingProductOrder;
            })
            .map(productOrderRepository::save);
    }

    /**
     * Get all the productOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<ProductOrder> findAll(Pageable pageable) {
        log.debug("Request to get all ProductOrders");
        return productOrderRepository.findAll(pageable);
    }

    /**
     * Get one productOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ProductOrder> findOne(String id) {
        log.debug("Request to get ProductOrder : {}", id);
        return productOrderRepository.findById(id);
    }

    /**
     * Delete the productOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ProductOrder : {}", id);
        productOrderRepository.deleteById(id);
    }
}
