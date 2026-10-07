package it.unimib.socialmesh.data.source.event;

import static it.unimib.socialmesh.util.Constants.API_KEY_ERROR;
import static it.unimib.socialmesh.util.Constants.FILE_JSON_TEST_API;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.util.JSONParserUtil;

/**
 * Class to get the news from a local JSON file to simulate the Web Service response.
 */
public class EventsMockRemoteDataSource extends BaseEventsRemoteDataSource {

    private JSONParserUtil jsonParserUtil;

    public EventsMockRemoteDataSource(JSONParserUtil jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getEvents(String type, String city, int size, String startDateTime, String time) {
        EventApiResponse eventApiResponse = null;

        try{
            eventApiResponse = jsonParserUtil.parseJson();
        } catch (Exception e) {
            Log.e("ERRORE JSON", "ERRORE JSON");
        }


        if (eventApiResponse != null) {
            eventCallback.onSuccessFromRemote(eventApiResponse, System.currentTimeMillis());
        } else {
            eventCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
        }
    }
}

