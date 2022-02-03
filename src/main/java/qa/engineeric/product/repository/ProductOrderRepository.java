package qa.engineeric.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import qa.engineeric.product.domain.ProductOrder;

/**
 * Spring Data MongoDB repository for the ProductOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOrderRepository extends MongoRepository<ProductOrder, String> {}
