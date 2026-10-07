package it.unimib.socialmesh.data.repository.event;

import static it.unimib.socialmesh.util.Constants.FRESH_TIMEOUT;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.socialmesh.model.Embedded;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.data.source.event.BaseEventsLocalDataSource;
import it.unimib.socialmesh.data.source.event.BaseEventsRemoteDataSource;
import it.unimib.socialmesh.util.EventCallback;

public class EventsRepositoryWithLiveData implements IEventsRepositoryWithLiveData, EventCallback{

    private static final String TAG = EventsRepositoryWithLiveData.class.getSimpleName();
    private final MutableLiveData<Result> allEventsMutableLiveData;
    private final BaseEventsRemoteDataSource eventsRemoteDataSource;
    private final BaseEventsLocalDataSource eventsLocalDataSource;

    public EventsRepositoryWithLiveData(BaseEventsRemoteDataSource eventsRemoteDataSource, BaseEventsLocalDataSource eventsLocalDataSource){
        allEventsMutableLiveData = new MutableLiveData<>();
        this.eventsRemoteDataSource = eventsRemoteDataSource;
        this.eventsLocalDataSource = eventsLocalDataSource;
        this.eventsRemoteDataSource.setEventCallback(this);
        this.eventsLocalDataSource.setEventCallback(this);
        Log.d(TAG, "Construct: Started");
    }
    @Override
    public MutableLiveData<Result> fetchEvents(String type, String city,int size, String startDateTime, String endDateTime){
        Log.d(TAG, "fetchEvents: Started");

        eventsRemoteDataSource.getEvents(type, city, size,startDateTime, endDateTime);

        return allEventsMutableLiveData;
    }

    @Override
    public void onSuccessFromRemote(@NonNull EventApiResponse eventApiResponse, long lastUpdate) {
        eventsLocalDataSource.insertEvents(eventApiResponse.getEvents());
    }
    @Override
    public void onFailureFromRemote(Exception exception){
        Result.Error result = new Result.Error(exception.getMessage());
        allEventsMutableLiveData.postValue(result);
    }
    @Override

    public void onSuccessFromLocal(List<Event> eventsList) {
        Embedded embedded = new Embedded();
        embedded.setEvents(eventsList);

        EventApiResponse eventApiResponse = new EventApiResponse();
        eventApiResponse.setEmbedded(embedded);

        Result.EventResponseSuccess result = new Result.EventResponseSuccess(eventApiResponse);
        allEventsMutableLiveData.postValue(result);
    }
    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allEventsMutableLiveData.postValue(resultError);
    }
}