package service;

import entity.Food;
import repository.FoodsRepository;
import repository.IFoodRepository;

import java.sql.Date;
import java.util.ArrayList;

public class FoodsService {
    IFoodRepository iFoodRepository = new FoodsRepository();
    java.util.Date date=new java.util.Date();
    java.sql.Timestamp sqlTime=new java.sql.Timestamp(date.getTime());
    public boolean create(Food food){
        if(food.isValid()){
            food.setStatus(1);
            food.setCreatedAt(sqlTime);
            food.setUpdatedAt(sqlTime);
            return iFoodRepository.save(food);
        }
        return false;
    };
    public Food edit(Food food){
        if(food.isValid()&&iFoodRepository.findById(food.getId())!=null){
            food.setUpdatedAt(sqlTime);
            return iFoodRepository.update(food);
        }
        return null;
    }
    public boolean delete(Object id){
        Food food =iFoodRepository.findById(id);
        if(food!=null&&food.getStatus()!=0){
            food.setStatus(0);
            iFoodRepository.update(food);
            return true;
        }
        return false;
    }
    public ArrayList<Food> findByStatus(){
        return iFoodRepository.findByStatus();
    }
    public ArrayList<Food> findAll(){
        return iFoodRepository.findAll();
    }


}
