package com.rangel.innovation.ui.home.res;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rangel.innovation.constantes.Constantes;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rangel.innovation.constantes.Constantes;
import com.rangel.innovation.ui.model.UsuariosList;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
public class UsuarioRestAdapter {

    private String TAG = "UsuarioRestAdapter";

    private AsyncHttpClient iClient = new AsyncHttpClient();

    private UsuarioRestListener iListener;

    public UsuarioRestAdapter(UsuarioRestListener restListener) {
        this.iListener = restListener;
    }

    public void ideas() {
        String finalURL = Constantes.URL_BASE;

        // RequestParams params = new RequestParams("key", Constantes.API_KEY);
        RequestParams params = new RequestParams();

        iClient.get(finalURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson dGson = new Gson();
                UsuariosList iResult = dGson.fromJson(response.toString(), UsuariosList.class);
                iListener.onSucces(iResult);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, statusCode + "");
                iListener.onError(throwable);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retry");
            }

        });

    }
}
