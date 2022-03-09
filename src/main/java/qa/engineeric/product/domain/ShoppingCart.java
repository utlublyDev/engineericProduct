package qa.engineeric.product.domain;

import java.io.Serializable;

public class ShoppingCart implements Serializable {

    private int id;
    private String title;
    private double price;
    private String description;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return (
            "ShoppingCart [description=" + description + ", id=" + id + ", image=" + image + ", price=" + price + ", title=" + title + "]"
        );
    }
}
