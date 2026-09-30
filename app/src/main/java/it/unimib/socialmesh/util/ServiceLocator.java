package it.unimib.socialmesh.util;

import android.app.Application;
import android.util.Log;

import it.unimib.socialmesh.data.repository.event.EventsRepositoryWithLiveData;
import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.database.EventRoomDatabase;
import it.unimib.socialmesh.data.repository.event.IEventsRepositoryWithLiveData;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.data.repository.user.UserRepository;
import it.unimib.socialmesh.data.service.EventApiService;
import it.unimib.socialmesh.data.source.event.BaseEventsLocalDataSource;
import it.unimib.socialmesh.data.source.event.BaseEventsRemoteDataSource;
import it.unimib.socialmesh.data.source.event.EventsLocalDataSource;
import it.unimib.socialmesh.data.source.event.EventsMockRemoteDataSource;
import it.unimib.socialmesh.data.source.event.EventsRemoteDataSource;
import it.unimib.socialmesh.data.source.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.socialmesh.data.source.user.BaseUserDataRemoteDataSource;
import it.unimib.socialmesh.data.source.user.UserAuthenticationRemoteDataSource;
import it.unimib.socialmesh.data.source.user.UserDataRemoteDataSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;
    private static final String TAG = ServiceLocator.class.getSimpleName();

    private ServiceLocator() {
    }

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public EventApiService getEventsApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.EVENTS_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(EventApiService.class);
    }


    public EventRoomDatabase getEventDao(Application application) {
        return EventRoomDatabase.getDatabase(application);
    }

    public IEventsRepositoryWithLiveData getEventRepository(Application application, boolean testMode) {
        BaseEventsLocalDataSource eventsLocalDataSource;
        BaseEventsRemoteDataSource eventsRemoteDataSource;

        if(testMode) {
            Log.d(TAG, "Test mode enabled");
            eventsRemoteDataSource = new EventsMockRemoteDataSource(new JSONParserUtil(application));
        }
        else {
            eventsRemoteDataSource = new EventsRemoteDataSource(application.getResources().getString(R.string.events_api_key));
        }

        eventsLocalDataSource = new EventsLocalDataSource(getEventDao(application));

        return new EventsRepositoryWithLiveData(eventsRemoteDataSource,eventsLocalDataSource);

    }

    public IUserRepository getUserRepository(Application application) {
        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationRemoteDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserDataRemoteDataSource();

        BaseEventsLocalDataSource eventsLocalDataSource =
                new EventsLocalDataSource(getEventDao(application));

        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource, eventsLocalDataSource);
    }
}
