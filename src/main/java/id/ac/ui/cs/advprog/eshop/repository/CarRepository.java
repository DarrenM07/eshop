package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


@Repository
public class CarRepository implements InterfaceCarRepository {

    static int id = 0;

    private final List<Car> carData = new ArrayList<>();

    @Override
    public Car create(Car car) {
        if (car.getCarId() == null) {
            UUID uuid = UUID.randomUUID();
            car.setCarId(uuid.toString());
        }
        carData.add(car);
        return car;
    }

    public Iterator<Car> findAll() {
        return carData.iterator();
    }

    public Car findById(String carId) {
        for (Car car : carData) {
            if (car.getCarId().equals(carId)) {
                return car;
            }
        }
        return null; // Car not found
    }

    public Car update(String carId, Car updatedCar) {
        for (int i = 0; i < carData.size(); i++) {
            Car car = carData.get(i);
            if (car.getCarId().equals(carId)) {
                carData.set(i, updatedCar);
                return updatedCar; // Return the updated car
            }
        }
        return null; // or throw an exception if the car is not found
    }

    public boolean delete(String carId) {
        // removeIf returns true if any element was removed
        return carData.removeIf(car -> car.getCarId().equals(carId));
    }
}