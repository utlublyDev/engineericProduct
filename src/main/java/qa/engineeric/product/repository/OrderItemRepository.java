package qa.engineeric.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import qa.engineeric.product.domain.OrderItem;

/**
 * Spring Data MongoDB repository for the OrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem, String> {}
