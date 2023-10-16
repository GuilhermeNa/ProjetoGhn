package br.com.transporte.AppGhn.model.parcelas.factory;

import java.util.List;

public interface ParcelamentoInterface<T> {
    List<T> criaParcelas(Long chaveEstrangeira);

}
