package it.unimib.socialmesh.model.jsonFields;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Status {

    public Status() {}

    @SerializedName("code")
    @Expose
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
