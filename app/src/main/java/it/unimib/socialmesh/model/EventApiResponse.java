package it.unimib.socialmesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventApiResponse {
    private int totalResults;
    @SerializedName("_embedded")

    @Expose
    private Embedded embedded;


    public List<Event> getEvents() {
        return embedded.getEvents();
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }


    public EventApiResponse() {}

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int describeContents() {
        return 0;
    }
}