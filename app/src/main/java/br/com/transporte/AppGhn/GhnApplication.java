package br.com.transporte.AppGhn;

import static br.com.transporte.AppGhn.repository.LoginRepository.CHAVE_LOGADO;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.transporte.AppGhn.model.Frete;

public class GhnApplication extends Application {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
  /*  private final RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(
            this,
            CHAVE_LOGADO
    ).build();*/


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Handler getMainThreadHandler() {
        return mainThreadHandler;
    }

   /* public RxDataStore<Preferences> getDataStore() {return dataStore;}*/
}
