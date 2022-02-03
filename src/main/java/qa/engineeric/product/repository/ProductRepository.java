package qa.engineeric.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import qa.engineeric.product.domain.Product;

/**
 * Spring Data MongoDB repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {}
