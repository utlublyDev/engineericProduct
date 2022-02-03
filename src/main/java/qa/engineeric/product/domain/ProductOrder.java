package qa.engineeric.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import qa.engineeric.product.domain.enumeration.OrderStatus;

/**
 * A ProductOrder.
 */
@Document(collection = "product_order")
public class ProductOrder implements Serializable {

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
    @Field("placed_date")
    private Instant placedDate;

    @NotNull
    @Field("status")
    private OrderStatus status;

    @NotNull
    @Field("code")
    private String code;

    @Field("invoice_id")
    private Long invoiceId;

    @NotNull
    @Field("customer")
    private String customer;

    @DBRef
    @Field("orderItem")
    @JsonIgnoreProperties(value = { "product", "order" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ProductOrder id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserStoreOwnerId() {
        return this.userStoreOwnerId;
    }

    public ProductOrder userStoreOwnerId(String userStoreOwnerId) {
        this.setUserStoreOwnerId(userStoreOwnerId);
        return this;
    }

    public void setUserStoreOwnerId(String userStoreOwnerId) {
        this.userStoreOwnerId = userStoreOwnerId;
    }

    public String getUserId() {
        return this.userId;
    }

    public ProductOrder userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getPlacedDate() {
        return this.placedDate;
    }

    public ProductOrder placedDate(Instant placedDate) {
        this.setPlacedDate(placedDate);
        return this;
    }

    public void setPlacedDate(Instant placedDate) {
        this.placedDate = placedDate;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public ProductOrder status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public ProductOrder code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getInvoiceId() {
        return this.invoiceId;
    }

    public ProductOrder invoiceId(Long invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomer() {
        return this.customer;
    }

    public ProductOrder customer(String customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setOrder(this));
        }
        this.orderItems = orderItems;
    }

    public ProductOrder orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public ProductOrder addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public ProductOrder removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrder)) {
            return false;
        }
        return id != null && id.equals(((ProductOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOrder{" +
            "id=" + getId() +
            ", userStoreOwnerId='" + getUserStoreOwnerId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", placedDate='" + getPlacedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", code='" + getCode() + "'" +
            ", invoiceId=" + getInvoiceId() +
            ", customer='" + getCustomer() + "'" +
            "}";
    }
}
