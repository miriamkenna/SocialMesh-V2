package it.unimib.socialmesh.data.source.event;


import it.unimib.socialmesh.util.EventCallback;

public abstract class BaseEventsRemoteDataSource{
    protected EventCallback eventCallback;
    public void setEventCallback(EventCallback eventCallback){
        this.eventCallback = eventCallback;
    }
    public abstract void getEvents(String type, String city, int size,String startDateTime, String time);
}