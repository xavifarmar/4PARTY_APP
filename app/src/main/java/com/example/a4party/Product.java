public class Product {
    private int id;
    private String product_name;
    private String product_price;
    private String description;
    private int stock;
    private int category_id;
    private int color_id;
    private int gender_id;
    private int clothing_type_id;
    private int view_count;
    private int like_count;
    private String created_at;
    private int product_img;  // Para la imagen del producto (si se requiere)

    // Constructor
    public Product(int id, String product_name, String product_price, String description, int stock, int category_id,
                   int color_id, int gender_id, int clothing_type_id, int view_count, int like_count, String created_at, int product_img) {
        this.id = id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.description = description;
        this.stock = stock;
        this.category_id = category_id;
        this.color_id = color_id;
        this.gender_id = gender_id;
        this.clothing_type_id = clothing_type_id;
        this.view_count = view_count;
        this.like_count = like_count;
        this.created_at = created_at;
        this.product_img = product_img;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getColor_id() {
        return color_id;
    }

    public int getGender_id() {
        return gender_id;
    }

    public int getClothing_type_id() {
        return clothing_type_id;
    }

    public int getView_count() {
        return view_count;
    }

    public int getLike_count() {
        return like_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getProduct_img() {
        return product_img;
    }
}
