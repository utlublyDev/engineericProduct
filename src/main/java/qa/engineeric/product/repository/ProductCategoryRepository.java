package qa.engineeric.product.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import qa.engineeric.product.domain.ProductCategory;

/**
 * Spring Data MongoDB repository for the ProductCategory entity.
 */
@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {
    @Query("{}")
    Page<ProductCategory> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<ProductCategory> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<ProductCategory> findOneWithEagerRelationships(String id);

    List<ProductCategory> findAllByUserStoreOwnerIdAndWebKey(String userStoreOwnerId, String webKey);
}
