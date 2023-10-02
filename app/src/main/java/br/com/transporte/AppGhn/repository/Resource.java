package br.com.transporte.AppGhn.repository;

public class Resource<T> {
    private final T dado;
    private final String erro;

    public Resource(T dado, String erro) {
        this.dado = dado;
        this.erro = erro;
    }

    public T getDado() {
        return dado;
    }

    public String getErro() {
        return erro;
    }

}