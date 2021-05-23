package repository;
import entity.Food;
import java.util.ArrayList;


public class FoodsRepository implements IFoodRepository {
GenericRepository<Food> repository = new GenericRepository<Food>(Food.class);

    @Override
    public boolean save(Food food) {
        return repository.save(food);
    }

    @Override
    public ArrayList<Food> findAll() {
        return repository.findAll();
    }

    @Override
    public Food findById(Object id) {
        return null;
    }

    @Override
    public ArrayList<Food> findByStatus() {
        return repository.findByStatus();
    }

    @Override
    public Food update(Food food) {
        return repository.update(food);
    }

    @Override
    public boolean delete(Object id) {
        return false;
    }

}
