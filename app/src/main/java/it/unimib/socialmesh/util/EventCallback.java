package it.unimib.socialmesh.util;

import java.util.List;

import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;

public interface EventCallback {
    void onSuccessFromRemote(EventApiResponse eventApiResponse, long lastUpdate);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<Event> eventsList);
    void onFailureFromLocal(Exception exception);

}
