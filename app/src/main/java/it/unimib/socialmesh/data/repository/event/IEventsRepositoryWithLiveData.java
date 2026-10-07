package it.unimib.socialmesh.data.repository.event;
import androidx.lifecycle.MutableLiveData;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.Result;
import java.util.List;

public interface IEventsRepositoryWithLiveData {
    MutableLiveData<Result>  fetchEvents(String type, String city, int size,
                                         String startDateTime, String time);

}
