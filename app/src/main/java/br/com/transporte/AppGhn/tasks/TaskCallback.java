package br.com.transporte.AppGhn.tasks;

public interface TaskCallback<T> {
    void finalizado(T t);

}
