package qa.engineeric.product.domain;

public class CategoriesNames {

    private String id;
    private String categoriesName;
    private String imageUrl;

    public String getCategoriesName() {
        return categoriesName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
