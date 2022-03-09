package qa.engineeric.product.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import qa.engineeric.product.domain.Reviews;

/**
 * Spring Data MongoDB repository for the Reviews entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewsRepository extends MongoRepository<Reviews, String> {
    List<Reviews> findAllByProdcutsId(String productId);
}
