package model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by paner on 17/2/28.
 */
@ApiModel(value = "Car",discriminator = "汽车实例")
public class Car {
    @ApiModelProperty(value = "汽车制造商",dataType = "string")
    private String make;

    @ApiModelProperty(value = "汽车制造商",dataType = "string")
    private int numberOfSeats;

    //constructor, getters, setters etc.


    public Car(String make, int numberOfSeats) {
        this.make = make;
        this.numberOfSeats = numberOfSeats;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }


}

