package it.unimib.socialmesh.util;

import static it.unimib.socialmesh.util.Constants.FILE_JSON_TEST_API;

import android.app.Application;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.unimib.socialmesh.model.EventApiResponse;

public class JSONParserUtil {

    private final Application application;

    public JSONParserUtil (Application application) {
        this.application = application;
    }

    // Metodo per effettuare il parsing del JSON
    public EventApiResponse parseJson() throws IOException {
        InputStream inputStream = application.getAssets().open(FILE_JSON_TEST_API);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        return gson.fromJson(bufferedReader, EventApiResponse.class);
    }
}
