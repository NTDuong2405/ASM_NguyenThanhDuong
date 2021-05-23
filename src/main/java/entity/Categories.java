package entity;

import dannotation.Column;
import dannotation.Entity;
import dannotation.Id;

import java.sql.Date;
@Entity(tableName = "categories")
public class Categories {
    @Id(autoIncrement = true)
    @Column(columnName = "id", columnType = "INT")
    private int id;
    @Column(columnName = "name",columnType = "VARCHAR(250)")
    private String name;
    @Column(columnName = "createdAt", columnType = "DATE")
    private Date createdAt;
    @Column(columnName = "updatedAt", columnType = "DATE")
    private Date updatedAt;
    public boolean validate(){
        if(this.getName().trim().length()>0){
            return true;
        }
        return false;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
