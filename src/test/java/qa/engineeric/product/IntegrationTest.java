package qa.engineeric.product;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import qa.engineeric.product.MongoDbTestContainerExtension;
import qa.engineeric.product.ProductApp;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = ProductApp.class)
@ExtendWith(MongoDbTestContainerExtension.class)
public @interface IntegrationTest {
}
