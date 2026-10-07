package it.unimib.socialmesh.util;
import java.util.List;

import it.unimib.socialmesh.model.Event;

public interface ResponseCallback {
    void onSuccess(List<Event> eventsList, long lastUpdate);
    void onFailure(String errorMessage);
}

