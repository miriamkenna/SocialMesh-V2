package it.unimib.socialmesh.model;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import it.unimib.socialmesh.model.jsonFields.Classification;
import it.unimib.socialmesh.model.jsonFields.Dates;
import it.unimib.socialmesh.model.jsonFields.Embedded_1;
import it.unimib.socialmesh.model.jsonFields.Genre;
import it.unimib.socialmesh.model.jsonFields.Image;
import it.unimib.socialmesh.model.jsonFields.Venue;
import it.unimib.socialmesh.util.Converters;


@Entity
public class Event implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long localId;
    @Ignore
    private double venueLatitude;
    @Ignore
    private double venueLongitude;
    @Ignore
    private String placeName;
    @Ignore
    private String mainUrlImage;
    @Ignore
    private String dateAndTime;

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    private static final String TAG = Event.class.getSimpleName();
    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    private String name;

    public String getMainUrlImage() {
        return mainUrlImage;
    }

    public void setMainUrlImage(String mainUrlImage) {
        this.mainUrlImage = mainUrlImage;
    }

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private String remoteId;

    @TypeConverters(Converters.class)
    @SerializedName("images")
    @Expose
    private List<Image> images;

    @TypeConverters(Converters.class)
    @SerializedName("classifications")
    @Expose
    private List<Classification> classifications;
    @Embedded(prefix = "dates_")
    @SerializedName("dates")
    @Expose
    private Dates dates;

    @Embedded(prefix = "genre_")
    @SerializedName("genre")
    @Expose
    private Genre genre;

    @Embedded(prefix = "_embedded_")
    @SerializedName("_embedded")
    @Expose
    private Embedded_1 embedded;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected Event(Parcel in) {
        localId = in.readLong();
        name = in.readString();
        type = in.readString();
        remoteId = in.readString();
        description = in.readString();
        venueLatitude = in.readDouble();
        venueLongitude = in.readDouble();
        placeName = in.readString();
        mainUrlImage = in.readString();
        dateAndTime = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public Event(){}

    public String getName1() {
        if (name != null && name.length() > 20) {
            int count = 0;
            StringBuilder word = new StringBuilder();

            while (count < name.length()) {
                char character = name.charAt(count);
                if (character == '-' || character == ':' || character == ' ') {
                    return word.toString();
                }
                word.append(character);
                count++;
            }
            return word.toString();
        } else {
            return name;
        }
    }


    public String getName(){
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
    public String getRemoteId() {
        return remoteId;
    }
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Dates getDates() {
        return dates;
    }

    public String getDates1() {

        int count = 0;
        StringBuilder date = new StringBuilder();
        if (dates != null && dates.getStart() != null && dates.getStart().getLocalDate() != null){
            while (count < dates.getStart().getLocalDate().length()) {
                char character = dates.getStart().getLocalDate().charAt(count);
                if (character == 'T') {
                    return date.toString();
                }
                date.append(character);
                count++;
            }
        }
        return date.toString();
    }

    public String getLocalDateAndTime() {
        String startDate = null;
        String startTime = null;
        if(this.dates != null && this.dates.getStart() != null && this.dates.getStart().getLocalDate() != null) {
            startDate = this.dates.getStart().getLocalDate();
            if(this.dates.getStart().getLocalTime() != null)
                startTime = this.dates.getStart().getLocalTime();
        }
        if(startDate != null && startTime != null)
            return (startDate + "\n" + startTime);
        else {
            if(startDate != null)
                return startDate;
        }

        return null;
    }

    public String getPlaceName() {
        if(this.getEmbedded().getVenues().get(0).getName() != null)
            return this.getEmbedded().getVenues().get(0).getName();
        else
            return null;
    }
public void setPlaceName(String name){
        this.placeName = name;
}
public String getPlaceNameMap(){
        return placeName;

}

   public List<Image> getImages() {
        return images;
    }
   public void setImages(List<Image> images) {
        this.images = images;
    }

   public void setDates(Dates dates) {
       this.dates = dates;
   }

   public Genre getGenre(){
       return genre;
   }

   public void setGenre(Genre genre){
       this.genre=genre;
   }

   public List<Classification> getClassifications() {
       return classifications;
   }

   public void setClassifications(List<Classification> classifications){
       this.classifications=classifications;
   }

   public String getGenreName(){
       Log.d(TAG, classifications.get(0).getGenre().getName());
       return classifications.get(0).getGenre().getName();
   }

   public Embedded_1 getEmbedded() {
       return embedded;
   }

   public void setEmbedded(Embedded_1 embedded) {
       this.embedded = embedded;
   }

   public String getUrlImages() {
       if (images != null && !images.isEmpty()) {
           return images.get(0).getUrlImages();
       } else {
           return "URL vuoto";
       }
   }

   public String getUrlImagesHD() {
       if (images != null && !images.isEmpty()) {
           return images.get(7).getUrlImages();
       } else {
           return "URL vuoto";
       }
   } public void setVenueLatitude(double venueLatitude) {
        this.venueLatitude = venueLatitude;
    }

    public void setVenueLongitude(double venueLongitude) {
        this.venueLongitude = venueLongitude;
    }
   public double getLatitude() {
       return this.getEmbedded().getVenues().get(0).getLocation().getLatitude();
   }
   public double getVenueLatitude(){
        return venueLatitude;
   }
    public double getVenueLongitude(){
        return venueLongitude;
    }

   public double getLongitude() {
       return this.getEmbedded().getVenues().get(0).getLocation().getLongitude();
   }

   @Override
   public int describeContents() {
       return 0;
   }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(localId);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(remoteId);
        dest.writeString(description);
        dest.writeDouble(venueLatitude);
        dest.writeDouble(venueLongitude);
        dest.writeString(placeName);
        dest.writeString(mainUrlImage);
        dest.writeString(dateAndTime);
    }
}


