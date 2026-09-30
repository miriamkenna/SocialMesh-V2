package it.unimib.socialmesh.model.jsonFields;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dates{
    public Dates() {}
    @Embedded(prefix = "start_")
    @SerializedName("start")
    @Expose
    private Start start;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @Embedded(prefix = "status_")
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("spanMultipleDays")
    @Expose
    private Boolean spanMultipleDays;

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getSpanMultipleDays() {
        return spanMultipleDays;
    }

    public void setSpanMultipleDays(Boolean spanMultipleDays) {
        this.spanMultipleDays = spanMultipleDays;
    }
}
