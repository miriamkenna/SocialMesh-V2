package it.unimib.socialmesh.model.jsonFields;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Classification {
    public Classification() {}

    @Embedded(prefix = "segment_")
    @SerializedName("segment")
    @Expose
    private Segment segment;
    @Embedded(prefix = "genre_")
    @SerializedName("genre")
    @Expose
    private Genre genre;
    @Embedded(prefix = "subGenre_")
    @SerializedName("subGenre")
    @Expose
    private SubGenre subGenre;

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public SubGenre getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(SubGenre subGenre) {
        this.subGenre = subGenre;
    }
}
