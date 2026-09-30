package it.unimib.socialmesh.model.jsonFields;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Start {
    public Start() {}

    @SerializedName("localDate")
    @Expose
    private String localDate;
    @SerializedName("localTime")
    @Expose
    private String localTime;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
