package entity;

import dannotation.Column;
import dannotation.Entity;
import dannotation.Id;
import helper.ValidationConstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Entity(tableName = "foods")
public class Food {
    @Id(autoIncrement = true)
    @Column(columnName = "id", columnType = "int")
    private int id;
    @Column(columnName = "foodName", columnType = "VARCHAR(250)")
    private String foodName;
    @Column(columnName = "description", columnType = "VARCHAR(250)")
    private String description;
    @Column(columnName = "categoryId", columnType = "INT")
    private int categoryId;
    @Column(columnName = "thumbnail", columnType = "VARCHAR(250)")
    private String thumbnail;
    @Column(columnName = "price", columnType = "double")
    private double price;
    @Column(columnName = "createdAt", columnType = "DATE")
    private Date createdAt;
    @Column(columnName = "updatedAt", columnType = "DATE")
    private Date updatedAt;
    @Column(columnName = "status", columnType = "INT")
    private int status;
    public String toStatus(int status) {
        switch (status) {
            case 0:
                return "Delete";
            case 1:
                return "Sold";
            case 2:
                return "Sold Out";
            default:
                return "";
        }
    }

    public Food(int id, String foodName, String description,int categoryId, String thumbnail, Double price, Date createdAt, Date updatedAt, int status) {
        this.foodName = foodName;
        this.description = description;
        this.categoryId = categoryId;
        this.thumbnail = thumbnail;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public Food() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + foodName + '\'' +", description='" + description + '\'' +", thumbnail='" + thumbnail + '\'' +
                ", price='" + price + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, ArrayList<String>> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, ArrayList<String>> errors) {
        this.errors = errors;
    }

    private HashMap<String, ArrayList<String>> errors = new HashMap<>();

    private boolean checkFoodName(){
        ArrayList<String> foodNameError = new ArrayList<>();
        if (this.foodName == null || this.foodName.length() == 0){
            foodNameError.add(ValidationConstant.FOOD_FOODNAME_MS_REQUIRED);
            this.errors.put(ValidationConstant.FOOD_FIELDNAME_FOODNAME,foodNameError);
        }
        if (this.foodName != null && this.foodName.length()<7 ){
            foodNameError.add(ValidationConstant.FOOD_FOODNAME_MS_LENGTH);
            this.errors.put(ValidationConstant.FOOD_FIELDNAME_FOODNAME,foodNameError);
        }
        return foodNameError.size() == 0;
    }
    private boolean checkPrice(){
        ArrayList<String> priceError = new ArrayList<>();
        if ( this.price == 0){
            priceError.add(ValidationConstant.FOOD_PRICE_MS_REQUIRED);
            this.errors.put(ValidationConstant.FOOD_FIELDNAME_PRICE,priceError);
        }
        return priceError.size() == 0;
    }

    public boolean isValid(){
        checkFoodName();
        checkPrice();
        return this.errors.size() == 0;
    }
}

