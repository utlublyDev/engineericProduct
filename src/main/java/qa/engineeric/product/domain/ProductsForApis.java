package qa.engineeric.product.domain;

import java.math.BigDecimal;

public class ProductsForApis {

    private String id;
    private String title;
    private BigDecimal price;
    private String description;
    private String category;
    private String image;
    private Rating rating;
    private String userStoreOwnerId;
    private String webKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /**
     * @return String return the userStoreOwnerId
     */
    public String getUserStoreOwnerId() {
        return userStoreOwnerId;
    }

    /**
     * @param userStoreOwnerId the userStoreOwnerId to set
     */
    public void setUserStoreOwnerId(String userStoreOwnerId) {
        this.userStoreOwnerId = userStoreOwnerId;
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
