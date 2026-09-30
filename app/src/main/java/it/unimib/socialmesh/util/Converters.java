package it.unimib.socialmesh.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import it.unimib.socialmesh.model.jsonFields.Classification;
import it.unimib.socialmesh.model.jsonFields.Image;
import it.unimib.socialmesh.model.jsonFields.Venue;

public class Converters {

    @TypeConverter
    public static String fromImageList(List<Image> imageList) {
        if (imageList == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Image>>() {}.getType();
        return gson.toJson(imageList, type);
    }

    @TypeConverter
    public static List<Image> toImageList(String imageListString) {
        if (imageListString == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Image>>() {}.getType();
        return gson.fromJson(imageListString, type);
    }
    @TypeConverter
    public static List<Classification> fromStringClassification(String value) {
        Type listType = new TypeToken<List<Classification>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromListClassification(List<Classification> classifications) {
        Gson gson = new Gson();
        return gson.toJson(classifications);
    }

    @TypeConverter
    public static List<Venue> fromStringVenue(String value) {
        Type listType = new TypeToken<List<Venue>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromListVenue(List<Venue> venues) {
        Gson gson = new Gson();
        return gson.toJson(venues);
    }
}