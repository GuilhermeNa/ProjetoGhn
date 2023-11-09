package br.com.transporte.appGhn;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GhnApplication extends Application {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Handler getMainThreadHandler() {
        return mainThreadHandler;
    }

}
