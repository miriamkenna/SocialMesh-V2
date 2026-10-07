package it.unimib.socialmesh.data.source.event;

import androidx.annotation.NonNull;

import it.unimib.socialmesh.data.service.EventApiService;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsRemoteDataSource extends BaseEventsRemoteDataSource {
    private final EventApiService eventApiService;
    private final String apiKey;

    public EventsRemoteDataSource(String apiKey){
        this.apiKey = apiKey;
        this.eventApiService = ServiceLocator.getInstance().getEventsApiService();
    }
    @Override
    public void getEvents(String type, String city,int size, String startDateTime, String time){
        Call<EventApiResponse> eventResponseCall = eventApiService.getEvents(type,city,size,startDateTime,time,apiKey);
        eventResponseCall.enqueue(new Callback<EventApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<EventApiResponse> call, @NonNull Response<EventApiResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    eventCallback.onSuccessFromRemote(response.body(),System.currentTimeMillis());
                }
                else {
                    eventCallback.onFailureFromRemote(new Exception("Errore api key"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventApiResponse> call,@NonNull Throwable t) {
                eventCallback.onFailureFromRemote(new Exception("Errore di retrofit"));
            }
        });
    }
}