package br.com.transporte.appGhn.repository;

public interface RepositoryCallback<T> {
    void sucesso(T resultado);

    void falha(String msg);
}
