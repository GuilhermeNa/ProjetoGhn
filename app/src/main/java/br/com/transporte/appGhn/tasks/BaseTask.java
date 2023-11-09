package br.com.transporte.appGhn.tasks;

import android.os.Handler;

import java.util.concurrent.ExecutorService;

public abstract class BaseTask {
    protected final ExecutorService executor;
    protected final Handler handler;

    public BaseTask(ExecutorService executor, Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

}
