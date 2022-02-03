package qa.engineeric.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import qa.engineeric.product.domain.enumeration.OrderItemStatus;

/**
 * A OrderItem.
 */
@Document(collection = "order_item")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user_store_owner_id")
    private String userStoreOwnerId;

    @NotNull
    @Field("user_id")
    private String userId;

    @NotNull
    @Min(value = 0)
    @Field("quantity")
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Field("total_price")
    private BigDecimal totalPrice;

    @NotNull
    @Field("status")
    private OrderItemStatus status;

    @NotNull
    @Field("payment_id")
    private String paymentId;

    @NotNull
    @Field("order_number")
    private Integer orderNumber;

    @DBRef
    @Field("product")
    @JsonIgnoreProperties(value = { "categories" }, allowSetters = true)
    private Product product;

    @DBRef
    @Field("order")
    @JsonIgnoreProperties(value = { "orderItems" }, allowSetters = true)
    private ProductOrder order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public OrderItem id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserStoreOwnerId() {
        return this.userStoreOwnerId;
    }

    public OrderItem userStoreOwnerId(String userStoreOwnerId) {
        this.setUserStoreOwnerId(userStoreOwnerId);
        return this;
    }

    public void setUserStoreOwnerId(String userStoreOwnerId) {
        this.userStoreOwnerId = userStoreOwnerId;
    }

    public String getUserId() {
        return this.userId;
    }

    public OrderItem userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderItem totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderItemStatus getStatus() {
        return this.status;
    }

    public OrderItem status(OrderItemStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public String getPaymentId() {
        return this.paymentId;
    }

    public OrderItem paymentId(String paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getOrderNumber() {
        return this.orderNumber;
    }

    public OrderItem orderNumber(Integer orderNumber) {
        this.setOrderNumber(orderNumber);
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public ProductOrder getOrder() {
        return this.order;
    }

    public void setOrder(ProductOrder productOrder) {
        this.order = productOrder;
    }

    public OrderItem order(ProductOrder productOrder) {
        this.setOrder(productOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", userStoreOwnerId='" + getUserStoreOwnerId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", quantity=" + getQuantity() +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            ", paymentId='" + getPaymentId() + "'" +
            ", orderNumber=" + getOrderNumber() +
            "}";
    }
}
