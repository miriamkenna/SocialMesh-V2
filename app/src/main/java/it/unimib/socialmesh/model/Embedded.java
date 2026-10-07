package it.unimib.socialmesh.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Embedded {
    public Embedded() {}

    @SerializedName("events")
    @Expose
    private List<Event> events;


    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
