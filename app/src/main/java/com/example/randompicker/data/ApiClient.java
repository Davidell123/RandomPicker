package com.example.randompicker.data;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {
    private final String SERVER_URL = "http://10.0.2.2:5000";
    private final OkHttpClient client;

    public ApiClient() {
        client = new OkHttpClient();
    }

    // Interfata prin care comunicam rezultatul inapoi catre interfata grafica
    public interface ApiCallback {
        void onSuccess(String result);
        void onError(String error);
    }

    public void extrageRandom(String categorie, ApiCallback callback) {
        Request request = new Request.Builder()
                .url(SERVER_URL + "/extrage?categorie=" + categorie)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Eroare de conectare!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onError("Eroare la raspunsul serverului");
                }
            }
        });
    }

    public void adaugaIdee(String categorie, String idee, ApiCallback callback) {
        String jsonBody = "{\"categorie\":\"" + categorie + "\", \"idee\":\"" + idee + "\"}";
        RequestBody requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(SERVER_URL + "/adauga")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Eroare retea");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Idee adaugata cu succes in cloud!");
                } else {
                    callback.onError("Eroare la adaugare");
                }
            }
        });
    }
}