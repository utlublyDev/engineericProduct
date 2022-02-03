package qa.engineeric.product.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Reviews.
 */
@Document(collection = "reviews")
public class Reviews implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("customer_id")
    private String customerId;

    @Field("order_id")
    private String orderId;

    @Field("review")
    private String review;

    @Field("rating")
    private Integer rating;

    @Field("created_at")
    private ZonedDateTime createdAt;

    @Field("prodcuts_id")
    private String prodcutsId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Reviews id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public Reviews customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public Reviews orderId(String orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReview() {
        return this.review;
    }

    public Reviews review(String review) {
        this.setReview(review);
        return this;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Reviews rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Reviews createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getProdcutsId() {
        return this.prodcutsId;
    }

    public Reviews prodcutsId(String prodcutsId) {
        this.setProdcutsId(prodcutsId);
        return this;
    }

    public void setProdcutsId(String prodcutsId) {
        this.prodcutsId = prodcutsId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reviews)) {
            return false;
        }
        return id != null && id.equals(((Reviews) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reviews{" +
            "id=" + getId() +
            ", customerId='" + getCustomerId() + "'" +
            ", orderId='" + getOrderId() + "'" +
            ", review='" + getReview() + "'" +
            ", rating=" + getRating() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", prodcutsId='" + getProdcutsId() + "'" +
            "}";
    }
}
