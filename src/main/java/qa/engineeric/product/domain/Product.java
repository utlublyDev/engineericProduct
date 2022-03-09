package qa.engineeric.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import qa.engineeric.product.service.ReviewsService;

/**
 * Product sold by the Online store
 */
@Schema(description = "Product sold by the Online store")
@Document(collection = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user_store_owner_id")
    private String userStoreOwnerId;

    @Field("web_key")
    private String webKey;

    @NotNull
    @Field("product_name")
    private String productName;

    @NotNull
    @Field("product_name_in_arabic")
    private String productNameInArabic;

    @Field("product_description")
    private String productDescription;

    @Field("product_description_in_arabic")
    private String productDescriptionInArabic;

    @NotNull
    @DecimalMin(value = "0")
    @Field("price")
    private BigDecimal price;

    @Field("image")
    private byte[] image;

    @Field("image_content_type")
    private String imageContentType;

    @Field("image_url")
    private String imageUrl;

    @Field("keywords")
    private String keywords;

    @Field("date_added")
    private LocalDate dateAdded;

    @Field("date_modified")
    private LocalDate dateModified;

    private Rating rating;

    @DBRef
    @Field("categories")
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<ProductCategory> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Product id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getUserStoreOwnerId() {
        return this.userStoreOwnerId;
    }

    public Product userStoreOwnerId(String userStoreOwnerId) {
        this.setUserStoreOwnerId(userStoreOwnerId);
        return this;
    }

    public void setUserStoreOwnerId(String userStoreOwnerId) {
        this.userStoreOwnerId = userStoreOwnerId;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameInArabic() {
        return this.productNameInArabic;
    }

    public Product productNameInArabic(String productNameInArabic) {
        this.setProductNameInArabic(productNameInArabic);
        return this;
    }

    public void setProductNameInArabic(String productNameInArabic) {
        this.productNameInArabic = productNameInArabic;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public Product productDescription(String productDescription) {
        this.setProductDescription(productDescription);
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductDescriptionInArabic() {
        return this.productDescriptionInArabic;
    }

    public Product productDescriptionInArabic(String productDescriptionInArabic) {
        this.setProductDescriptionInArabic(productDescriptionInArabic);
        return this;
    }

    public void setProductDescriptionInArabic(String productDescriptionInArabic) {
        this.productDescriptionInArabic = productDescriptionInArabic;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Product image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Product imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Product imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public Product keywords(String keywords) {
        this.setKeywords(keywords);
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Product dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public Product dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Set<ProductCategory> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<ProductCategory> productCategories) {
        if (this.categories != null) {
            this.categories.forEach(i -> i.removeProduct(this));
        }
        if (productCategories != null) {
            productCategories.forEach(i -> i.addProduct(this));
        }
        this.categories = productCategories;
    }

    public Product categories(Set<ProductCategory> productCategories) {
        this.setCategories(productCategories);
        return this;
    }

    public Product addCategory(ProductCategory productCategory) {
        this.categories.add(productCategory);
        productCategory.getProducts().add(this);
        return this;
    }

    public Product removeCategory(ProductCategory productCategory) {
        this.categories.remove(productCategory);
        productCategory.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", userStoreOwnerId='" + getUserStoreOwnerId() + "'" +
            ", productName='" + getProductName() + "'" +
            ", productNameInArabic='" + getProductNameInArabic() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", productDescriptionInArabic='" + getProductDescriptionInArabic() + "'" +
            ", price=" + getPrice() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }

    /**
     * @return String return the webKey
     */
    public String getWebKey() {
        return webKey;
    }

    /**
     * @param webKey the webKey to set
     */
    public void setWebKey(String webKey) {
        this.webKey = webKey;
    }
}
