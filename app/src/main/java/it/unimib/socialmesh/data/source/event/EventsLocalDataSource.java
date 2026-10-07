package it.unimib.socialmesh.data.source.event;

import android.util.Log;

import java.util.List;

import it.unimib.socialmesh.data.database.EventDao;
import it.unimib.socialmesh.data.database.EventRoomDatabase;
import it.unimib.socialmesh.data.source.event.BaseEventsLocalDataSource;
import it.unimib.socialmesh.model.Event;

public class EventsLocalDataSource extends BaseEventsLocalDataSource {
    private final EventDao eventDao;

    public EventsLocalDataSource(EventRoomDatabase eventRoomDatabase){
        this.eventDao = eventRoomDatabase.eventDao();
    }

    @Override
    public void getEvents(){
        Log.d("localdatasource", "getevents: Started");
        EventRoomDatabase.databaseWriteExecutor.execute(()-> {
            eventCallback.onSuccessFromLocal(eventDao.getAll());
        });
    }
    @Override
    public void insertEvents(List<Event> eventsList){
        EventRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Event> allEvents = eventDao.getAll();

            if(eventsList != null){
                for(Event event : allEvents){
                    if(eventsList.contains(event)){
                        eventsList.set(eventsList.indexOf(event), event);
                    }
                }

                List <Long> insertedEventsIds = eventDao.insertEventsList(eventsList);
                for (int i = 0; i < eventsList.size(); i++){
                    eventsList.get(i).setLocalId(insertedEventsIds.get(i));
                }
                eventCallback.onSuccessFromLocal(eventsList);
            }
        });
    }
}