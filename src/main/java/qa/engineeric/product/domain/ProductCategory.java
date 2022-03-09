package qa.engineeric.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import qa.engineeric.product.domain.enumeration.CategoryStatus;

/**
 * A ProductCategory.
 */
@Document(collection = "product_category")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user_store_owner_id")
    private String userStoreOwnerId;

    @NotNull
    @Field("product_category_name")
    private String productCategoryName;

    @NotNull
    @Field("product_category_name_in_arabic")
    private String productCategoryNameInArabic;

    @Field("product_category_description")
    private String productCategoryDescription;

    @Field("product_category_description_in_arabic")
    private String productCategoryDescriptionInArabic;

    @Field("sort_order")
    private Integer sortOrder;

    @Field("date_added")
    private LocalDate dateAdded;

    @Field("date_modified")
    private LocalDate dateModified;

    @Field("status")
    private CategoryStatus status;

    @Field("web_key")
    private String webKey;

    @Field("image_url")
    private String imageUrl;

    @DBRef
    @Field("products")
    @JsonIgnoreProperties(value = { "categories" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ProductCategory id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserStoreOwnerId() {
        return this.userStoreOwnerId;
    }

    public ProductCategory userStoreOwnerId(String userStoreOwnerId) {
        this.setUserStoreOwnerId(userStoreOwnerId);
        return this;
    }

    public void setUserStoreOwnerId(String userStoreOwnerId) {
        this.userStoreOwnerId = userStoreOwnerId;
    }

    public String getProductCategoryName() {
        return this.productCategoryName;
    }

    public ProductCategory productCategoryName(String productCategoryName) {
        this.setProductCategoryName(productCategoryName);
        return this;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getProductCategoryNameInArabic() {
        return this.productCategoryNameInArabic;
    }

    public ProductCategory productCategoryNameInArabic(String productCategoryNameInArabic) {
        this.setProductCategoryNameInArabic(productCategoryNameInArabic);
        return this;
    }

    public void setProductCategoryNameInArabic(String productCategoryNameInArabic) {
        this.productCategoryNameInArabic = productCategoryNameInArabic;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductCategoryDescription() {
        return this.productCategoryDescription;
    }

    public ProductCategory productCategoryDescription(String productCategoryDescription) {
        this.setProductCategoryDescription(productCategoryDescription);
        return this;
    }

    public void setProductCategoryDescription(String productCategoryDescription) {
        this.productCategoryDescription = productCategoryDescription;
    }

    public String getProductCategoryDescriptionInArabic() {
        return this.productCategoryDescriptionInArabic;
    }

    public ProductCategory productCategoryDescriptionInArabic(String productCategoryDescriptionInArabic) {
        this.setProductCategoryDescriptionInArabic(productCategoryDescriptionInArabic);
        return this;
    }

    public void setProductCategoryDescriptionInArabic(String productCategoryDescriptionInArabic) {
        this.productCategoryDescriptionInArabic = productCategoryDescriptionInArabic;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public ProductCategory sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public ProductCategory dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public ProductCategory dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public CategoryStatus getStatus() {
        return this.status;
    }

    public ProductCategory status(CategoryStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public String getWebKey() {
        return this.webKey;
    }

    public ProductCategory webKey(String webKey) {
        this.setWebKey(webKey);
        return this;
    }

    public void setWebKey(String webKey) {
        this.webKey = webKey;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public ProductCategory products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public ProductCategory addProduct(Product product) {
        this.products.add(product);
        product.getCategories().add(this);
        return this;
    }

    public ProductCategory removeProduct(Product product) {
        this.products.remove(product);
        product.getCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCategory)) {
            return false;
        }
        return id != null && id.equals(((ProductCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategory{" +
            "id=" + getId() +
            ", userStoreOwnerId='" + getUserStoreOwnerId() + "'" +
            ", productCategoryName='" + getProductCategoryName() + "'" +
            ", productCategoryNameInArabic='" + getProductCategoryNameInArabic() + "'" +
            ", productCategoryDescription='" + getProductCategoryDescription() + "'" +
            ", productCategoryDescriptionInArabic='" + getProductCategoryDescriptionInArabic() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", status='" + getStatus() + "'" +
            ", webKey='" + getWebKey() + "'" +
            "}";
    }
}
