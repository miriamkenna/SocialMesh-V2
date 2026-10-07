package it.unimib.socialmesh.util;

public class Constants {

    public static final int LIMIT_AGE = 16;

    //Repositoy constants
    public static final String ALL_USA_DMAID = "200";

    public static final int WEEKS_OF_EVENT_SEARCH = 2;

    public static final int SIZE_OF_EVENT_SEARCH = 50;

    //Empty for all events type
    public static final String TYPE_OF_EVENT_SEARCH = "";

    public static final String FILE_JSON_TEST_API = "EventsApiTest.json";

    public static final String EVENTS_API_BASE_URL = "https://app.ticketmaster.com/discovery/v2/";

    public static final String EVENTS_ENDPOINT = "events.json";

    public static final String EVENTS_TYPE_PARAMETER = "classificationName";

    public static final String EVENTS_CITY_PARAMETER = "dmaId";
    public static final String EVENTS_START_DATE_PARAMETER = "startDateTime";
    public static final String EVENTS_END_DATE_PARAMETER = "endDateTime";
    public static final String API_KEY_PARAMETER = "apikey";
    public static final int FRESH_TIMEOUT = 1000 * 60 * 60; // 1 hour in milliseconds


    // Constants for Room database
    public static final String EVENTS_DATABASE_NAME = "events_db";
    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String API_KEY_ERROR = "api_key_error";
    public static final int DATABASE_VERSION = 3;


    // Constants for Firebase
    public static final String FIREBASE_REALTIME_DATABASE = "https://socialmeshunimib-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_EVENTS_COLLECTION = "events";
    public static final String FIREBASE_PICTURES_COLLECTION = "pictures";
    public static final String FIREBASE_PROFILE_PIC_NAME = "profilePic.jpg";

    //Errors
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

}
