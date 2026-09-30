
package it.unimib.socialmesh.data.service;

import static it.unimib.socialmesh.util.Constants.EVENTS_CITY_PARAMETER;
import static it.unimib.socialmesh.util.Constants.EVENTS_ENDPOINT;
import static it.unimib.socialmesh.util.Constants.EVENTS_END_DATE_PARAMETER;
import static it.unimib.socialmesh.util.Constants.EVENTS_START_DATE_PARAMETER;
import static it.unimib.socialmesh.util.Constants.EVENTS_TYPE_PARAMETER;
import static it.unimib.socialmesh.util.Constants.API_KEY_PARAMETER;

import it.unimib.socialmesh.model.EventApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface
EventApiService {
    @GET(EVENTS_ENDPOINT)
    Call<EventApiResponse> getEvents(
            @Query(EVENTS_TYPE_PARAMETER) String type,
            @Query(EVENTS_CITY_PARAMETER) String city,
            @Query("size") int size,
            @Query(EVENTS_START_DATE_PARAMETER) String startDateTime,
            @Query(EVENTS_END_DATE_PARAMETER) String endDateTime,
            @Query(API_KEY_PARAMETER) String apiKey);

}
