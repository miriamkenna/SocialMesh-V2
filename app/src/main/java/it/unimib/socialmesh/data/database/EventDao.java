package it.unimib.socialmesh.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.unimib.socialmesh.model.Event;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event ORDER BY CAST((ABS(CAST((localId || 'salt') AS INTEGER)) % 100000) AS INTEGER)")
    List<Event> getAll();
    @Query("SELECT * FROM event WHERE remoteId = :id")
    Event getEvent(long id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertNewsList(List<Event> eventList);

    @Insert
    void insertAll(Event... event);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertEventsList(List<Event> eventsList);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    void deleteAll();


    @Delete
    void deleteAllWithoutQuery(Event... event);

}
