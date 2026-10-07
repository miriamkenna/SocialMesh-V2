package it.unimib.socialmesh.model.jsonFields;
import androidx.room.Embedded;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attraction {
    public Attraction() {
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @Embedded(prefix = "aliases_")
    @SerializedName("aliases")
    @Expose
    private List<String> aliases;
    @Embedded(prefix = "images_")
    @SerializedName("images")
    @Expose
    private List<Image> images;
    @Embedded(prefix = "classifications_")
    @SerializedName("classifications")
    @Expose
    private List<Classification> classifications;
    @Embedded(prefix = "links_")
    @SerializedName("_links")
    @Expose
    private Links links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }




    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}