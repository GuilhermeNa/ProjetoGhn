package br.com.transporte.appGhn.tasks;

public interface TaskCallback<T> {
    void finalizado(T t);

}
