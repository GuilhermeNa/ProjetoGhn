package br.com.transporte.appGhn.model.parcelas.factory;

import java.util.List;

public interface ParcelamentoInterface<T> {
    List<T> criaParcelas(Long chaveEstrangeira);

}
