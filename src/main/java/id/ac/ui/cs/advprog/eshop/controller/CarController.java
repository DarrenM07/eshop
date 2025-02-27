package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarServiceImpl carService;

    // Displays the page to create a new Car
    @GetMapping("/createCar")
    public String showCreateCarPage(Model model) {
        model.addAttribute("car", new Car());
        return "CreateCar"; // Name of the HTML template
    }

    // Processes the form submission to create a new Car
    @PostMapping("/createCar")
    public String processCreateCar(@ModelAttribute Car car) {
        carService.create(car);
        return "redirect:/car/listCar";
    }

    // Lists all cars
    @GetMapping("/listCar")
    public String showCarList(Model model) {
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "CarList"; // Name of the HTML template
    }

    // Displays the edit page for a specific Car
    @GetMapping("/editCar/{carId}")
    public String showEditCarPage(@PathVariable String carId, Model model) {
        Car car = carService.findById(carId);
        model.addAttribute("car", car);
        return "EditCar"; // Name of the HTML template
    }

    // Processes the form submission to update a Car
    @PostMapping("/editCar")
    public String processEditCar(@ModelAttribute Car car) {
        carService.update(car.getCarId(), car);
        return "redirect:/car/listCar";
    }

    // Processes the request to delete a Car
    @PostMapping("/deleteCar")
    public String processDeleteCar(@RequestParam("carId") String carId) {
        carService.deleteCarById(carId);
        return "redirect:/car/listCar";
    }
}