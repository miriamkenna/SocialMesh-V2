package it.unimib.socialmesh.data.source.event;

import java.util.List;

import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.util.EventCallback;

public abstract class BaseEventsLocalDataSource {

    protected EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    public abstract void getEvents();
    public abstract void insertEvents(List<Event> eventsList);
    
}