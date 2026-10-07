package it.unimib.socialmesh.model.jsonFields;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class State {
    public State() {}

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("stateCode")
    @Expose
    private String stateCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
