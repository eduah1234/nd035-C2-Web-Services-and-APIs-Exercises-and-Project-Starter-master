package com.udacity.vehicles.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = getCar();
        car.setId(1L);
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = getCar();
        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.condition", is("USED")))
                .andExpect(jsonPath("$.details.model", is("Impala")))
                .andExpect(jsonPath("$.details.manufacturer.name", is("Chevrolet")))
                .andExpect(jsonPath("$.details.mileage", is(32280)))
                .andExpect(jsonPath("$.details.externalColor", is("white")))
                .andExpect(jsonPath("$.details.body", is("sedan")))
                .andExpect(jsonPath("$.details.engine", is("3.6L V6")))
                .andExpect(jsonPath("$.details.fuelType", is("Gasoline")))
                .andExpect(jsonPath("$.details.modelYear", is(2018)))
                .andExpect(jsonPath("$.details.productionYear", is(2018)))
                .andExpect(jsonPath("$.details.numberOfDoors", is(4)))
                .andExpect(jsonPath("$.location.lat", is(40.730610)))
                .andExpect(jsonPath("$.location.lon", is(-73.935242)));
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        mvc.perform(get("/cars")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.carList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.carList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.carList[0].condition", is("USED")))
                .andExpect(jsonPath("$._embedded.carList[0].details.model", is("Impala")))
                .andExpect(jsonPath("$._embedded.carList[0].details.manufacturer.name", is("Chevrolet")))
                .andExpect(jsonPath("$._embedded.carList[0].details.mileage", is(32280)))
                .andExpect(jsonPath("$._embedded.carList[0].details.externalColor", is("white")))
                .andExpect(jsonPath("$._embedded.carList[0].details.body", is("sedan")))
                .andExpect(jsonPath("$._embedded.carList[0].details.engine", is("3.6L V6")))
                .andExpect(jsonPath("$._embedded.carList[0].details.fuelType", is("Gasoline")))
                .andExpect(jsonPath("$._embedded.carList[0].details.modelYear", is(2018)))
                .andExpect(jsonPath("$._embedded.carList[0].details.productionYear", is(2018)))
                .andExpect(jsonPath("$._embedded.carList[0].details.numberOfDoors", is(4)))
                .andExpect(jsonPath("$._embedded.carList[0].location.lat", is(40.730610)))
                .andExpect(jsonPath("$._embedded.carList[0].location.lon", is(-73.935242)));

    }

    /**
     * Tests the read operation for a single car by ID.
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        mvc.perform(get("/cars/1")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.condition", is("USED")))
                .andExpect(jsonPath("$.details.model", is("Impala")))
                .andExpect(jsonPath("$.details.manufacturer.name", is("Chevrolet")))
                .andExpect(jsonPath("$.details.mileage", is(32280)))
                .andExpect(jsonPath("$.details.externalColor", is("white")))
                .andExpect(jsonPath("$.details.body", is("sedan")))
                .andExpect(jsonPath("$.details.engine", is("3.6L V6")))
                .andExpect(jsonPath("$.details.fuelType", is("Gasoline")))
                .andExpect(jsonPath("$.details.modelYear", is(2018)))
                .andExpect(jsonPath("$.details.productionYear", is(2018)))
                .andExpect(jsonPath("$.details.numberOfDoors", is(4)))
                .andExpect(jsonPath("$.location.lat", is(40.730610)))
                .andExpect(jsonPath("$.location.lon", is(-73.935242)));
    }

    /**
     * Tests the deletion of a single car by ID.
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        mvc.perform(delete("/cars/1")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }

    /**
     * Tests the update operation for a single car by ID.
     * @throws Exception if the update operation of a vehicle fails
     */
    @Test
    public void updateCar() throws Exception {
        Car car = getCar();
        car.setId(1L);
        car.getDetails().setModel("Malibu");  // Change the model
        car.setCondition(Condition.NEW);  // Change the condition

        given(carService.save(any())).willReturn(car);

        mvc.perform(
                put(new URI("/cars/1"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.condition", is("NEW")))
                .andExpect(jsonPath("$.details.model", is("Malibu")))
                .andExpect(jsonPath("$.details.manufacturer.name", is("Chevrolet")))
                .andExpect(jsonPath("$.details.mileage", is(32280)))
                .andExpect(jsonPath("$.details.externalColor", is("white")))
                .andExpect(jsonPath("$.details.body", is("sedan")))
                .andExpect(jsonPath("$.details.engine", is("3.6L V6")))
                .andExpect(jsonPath("$.details.fuelType", is("Gasoline")))
                .andExpect(jsonPath("$.details.modelYear", is(2018)))
                .andExpect(jsonPath("$.details.productionYear", is(2018)))
                .andExpect(jsonPath("$.details.numberOfDoors", is(4)))
                .andExpect(jsonPath("$.location.lat", is(40.730610)))
                .andExpect(jsonPath("$.location.lon", is(-73.935242)));
    }
}