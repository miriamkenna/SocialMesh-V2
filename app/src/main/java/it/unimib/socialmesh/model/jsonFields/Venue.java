package it.unimib.socialmesh.model.jsonFields;
import androidx.room.Embedded;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Venue {
    public Venue() {}

    @SerializedName("name")
    @Expose
    private String name;

    @Embedded(prefix = "city_")
    @SerializedName("city")
    @Expose
    private City city;

    @Embedded(prefix = "state_")
    @SerializedName("state")
    @Expose
    private State state;

    @Embedded(prefix = "address_")
    @SerializedName("address")
    @Expose
    private Address address;


    @Embedded(prefix = "location_")
    @SerializedName("location")
    @Expose
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

