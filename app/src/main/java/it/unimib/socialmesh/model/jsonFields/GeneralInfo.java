package it.unimib.socialmesh.model.jsonFields;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GeneralInfo {
    public GeneralInfo() {}

    @SerializedName("generalRule")
    @Expose
    private String generalRule;
    @SerializedName("childRule")
    @Expose
    private String childRule;

    public String getGeneralRule() {
        return generalRule;
    }

    public void setGeneralRule(String generalRule) {
        this.generalRule = generalRule;
    }

    public String getChildRule() {
        return childRule;
    }

    public void setChildRule(String childRule) {
        this.childRule = childRule;
    }

}
