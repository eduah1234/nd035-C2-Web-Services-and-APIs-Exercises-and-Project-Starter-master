package com.udacity.vehicles.api;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.service.CarService;

import io.swagger.annotations.ApiOperation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implements a REST-based controller for the Vehicles API.
 */
@RestController
@RequestMapping("/cars")
class CarController {

    private final CarService carService;
    private final CarResourceAssembler assembler;

    CarController(CarService carService, CarResourceAssembler assembler) {
        this.carService = carService;
        this.assembler = assembler;
    }

    /**
     * Creates a list to store any vehicles.
     * @return list of vehicles
     */
    @ApiOperation(value = "Get all cars")
    @GetMapping
    Resources<Resource<Car>> list() {
        List<Resource<Car>> resources = carService.list().stream().map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,
                linkTo(methodOn(CarController.class).list()).withSelfRel());
    }

    /**
     * Gets information of a specific car by ID.
     * @param id the id number of the given vehicle
     * @return all information for the requested vehicle
     */
    @ApiOperation(value = "Get a specific car by ID")
    @GetMapping("/{id}")
    Resource<Car> get(@PathVariable Long id) {
        Car car = carService.findById(id);
        return assembler.toResource(car);
    }

    /**
     * Posts information to create a new vehicle in the system.
     * @param car A new vehicle to add to the system.
     * @return response that the new vehicle was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     */
    @PostMapping
    ResponseEntity<?> post(@Valid @RequestBody Car car) throws URISyntaxException {
        Car savedCar = carService.save(car);
        Resource<Car> resource = assembler.toResource(savedCar);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Updates the information of a vehicle in the system.
     * @param id The ID number for which to update vehicle information.
     * @param car The updated information about the related vehicle.
     * @return response that the vehicle was updated in the system
     */
    @PutMapping("/{id}")
    ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody Car car) {
        Car existingCar = carService.findById(id);
        if (existingCar == null) {
            return ResponseEntity.notFound().build();
        }

        // Update fields if they are provided in the input
        if (car.getCondition() != null) {
            existingCar.setCondition(car.getCondition());
        }
        if (car.getDetails() != null) {
            // Update individual fields in Details
            Details existingDetails = existingCar.getDetails();
            Details newDetails = car.getDetails();
            if (newDetails.getBody() != null) {
                existingDetails.setBody(newDetails.getBody());
            }
            if (newDetails.getModel() != null) {
                existingDetails.setModel(newDetails.getModel());
            }
            if (newDetails.getManufacturer() != null) {
                existingDetails.setManufacturer(newDetails.getManufacturer());
            }
            if (newDetails.getNumberOfDoors() != null) {
                existingDetails.setNumberOfDoors(newDetails.getNumberOfDoors());
            }
            if (newDetails.getFuelType() != null) {
                existingDetails.setFuelType(newDetails.getFuelType());
            }
            if (newDetails.getEngine() != null) {
                existingDetails.setEngine(newDetails.getEngine());
            }
            if (newDetails.getMileage() != null) {
                existingDetails.setMileage(newDetails.getMileage());
            }
            if (newDetails.getModelYear() != null) {
                existingDetails.setModelYear(newDetails.getModelYear());
            }
            if (newDetails.getProductionYear() != null) {
                existingDetails.setProductionYear(newDetails.getProductionYear());
            }
            if (newDetails.getExternalColor() != null) {
                existingDetails.setExternalColor(newDetails.getExternalColor());
            }
        }
        if (car.getLocation() != null) {
            existingCar.setLocation(car.getLocation());
        }

        Car savedCar = carService.save(existingCar);
        Resource<Car> resource = assembler.toResource(savedCar);
        return ResponseEntity.ok(resource);
    }

    /**
     * Removes a vehicle from the system.
     * @param id The ID number of the vehicle to remove.
     * @return response that the related vehicle is no longer in the system
     */
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
