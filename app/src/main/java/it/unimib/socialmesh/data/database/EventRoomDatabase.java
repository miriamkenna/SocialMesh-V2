package it.unimib.socialmesh.data.database;

import static it.unimib.socialmesh.util.Constants.DATABASE_VERSION;
import static it.unimib.socialmesh.util.Constants.EVENTS_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.util.Converters;

@Database(entities = {Event.class}, version = DATABASE_VERSION,exportSchema = false)
@TypeConverters({Converters.class})

public abstract class EventRoomDatabase extends RoomDatabase {

    public abstract EventDao eventDao();

    private static volatile EventRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static EventRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EventRoomDatabase.class, EVENTS_DATABASE_NAME)
                            .fallbackToDestructiveMigration() // Abilita la migrazione distruttiva
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
