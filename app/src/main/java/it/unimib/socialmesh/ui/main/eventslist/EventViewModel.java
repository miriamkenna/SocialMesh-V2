package it.unimib.socialmesh.ui.main.eventslist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.data.repository.event.IEventsRepositoryWithLiveData;

public class EventViewModel extends ViewModel {
    private static final String TAG = EventViewModel.class.getSimpleName();
    private final IEventsRepositoryWithLiveData eventsRepositoryWithLiveData;
    private MutableLiveData<Result> eventsListLiveData;
    private int page;
    private int size;
    private int currentResults;
    private int totalResults;
    private boolean isLoading;
    private boolean firstLoading;

    public EventViewModel(IEventsRepositoryWithLiveData iEventsRepositoryWithLiveData) {
        this.eventsRepositoryWithLiveData = iEventsRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getEvents(String type, String city,int size ,String startDateTime, String time){
        if( eventsListLiveData == null){
            fetchEvents(type,city,size,startDateTime,time);
        }
        return eventsListLiveData;
    }

    private void fetchEvents(String type, String city, int size,String startDateTime, String time){
        eventsListLiveData = eventsRepositoryWithLiveData.fetchEvents(type, city, size, startDateTime, time);
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isFirstLoading() {
        return firstLoading;
    }

    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }

    public MutableLiveData<Result> getNewsResponseLiveData() {
        return eventsListLiveData;
    }
}
