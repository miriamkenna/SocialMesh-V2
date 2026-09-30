package it.unimib.socialmesh.model.jsonFields;
import androidx.room.Embedded;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {
    public Links() {}
    @Embedded(prefix = "self_")
    @SerializedName("self")
    @Expose
    private Self self;

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }
}
