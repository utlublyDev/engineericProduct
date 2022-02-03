package qa.engineeric.product;

import java.util.List;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.testcontainers.containers.MongoDBContainer;
import qa.engineeric.product.MongoDbTestContainerExtension;

public class TestContainersSpringContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        return (context, mergedConfig) -> {
            MongoDBContainer container = (MongoDBContainer) MongoDbTestContainerExtension.getThreadContainer().get();
            if (container != null) {
                TestPropertyValues.of("spring.data.mongodb.uri=" + container.getReplicaSetUrl()).applyTo(context.getEnvironment());
            }
        };
    }
}