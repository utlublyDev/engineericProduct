package qa.engineeric.product.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qa.engineeric.product.domain.OrderItem;
import qa.engineeric.product.repository.OrderItemRepository;

/**
 * Service Implementation for managing {@link OrderItem}.
 */
@Service
public class OrderItemService {

    private final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Save a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public OrderItem save(OrderItem orderItem) {
        log.debug("Request to save OrderItem : {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    /**
     * Partially update a orderItem.
     *
     * @param orderItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItem> partialUpdate(OrderItem orderItem) {
        log.debug("Request to partially update OrderItem : {}", orderItem);

        return orderItemRepository
            .findById(orderItem.getId())
            .map(existingOrderItem -> {
                if (orderItem.getUserStoreOwnerId() != null) {
                    existingOrderItem.setUserStoreOwnerId(orderItem.getUserStoreOwnerId());
                }
                if (orderItem.getUserId() != null) {
                    existingOrderItem.setUserId(orderItem.getUserId());
                }
                if (orderItem.getQuantity() != null) {
                    existingOrderItem.setQuantity(orderItem.getQuantity());
                }
                if (orderItem.getTotalPrice() != null) {
                    existingOrderItem.setTotalPrice(orderItem.getTotalPrice());
                }
                if (orderItem.getStatus() != null) {
                    existingOrderItem.setStatus(orderItem.getStatus());
                }
                if (orderItem.getPaymentId() != null) {
                    existingOrderItem.setPaymentId(orderItem.getPaymentId());
                }
                if (orderItem.getOrderNumber() != null) {
                    existingOrderItem.setOrderNumber(orderItem.getOrderNumber());
                }

                return existingOrderItem;
            })
            .map(orderItemRepository::save);
    }

    /**
     * Get all the orderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<OrderItem> findAll(Pageable pageable) {
        log.debug("Request to get all OrderItems");
        return orderItemRepository.findAll(pageable);
    }

    /**
     * Get one orderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<OrderItem> findOne(String id) {
        log.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findById(id);
    }

    /**
     * Delete the orderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete OrderItem : {}", id);
        orderItemRepository.deleteById(id);
    }
}
