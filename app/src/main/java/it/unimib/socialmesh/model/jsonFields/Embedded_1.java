package it.unimib.socialmesh.model.jsonFields;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import it.unimib.socialmesh.util.Converters;

public class Embedded_1 {

    public Embedded_1() {}

    @TypeConverters(Converters.class)
    @SerializedName("venues")
    @Expose
    private List<Venue> venues;

    public List<Venue> getVenues() {
        return venues;
    }
    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

}
