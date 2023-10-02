package br.com.transporte.AppGhn.repository;

public interface RepositoryCallback<T> {
    void sucesso(T resultado);

    void falha(String msg);
}
