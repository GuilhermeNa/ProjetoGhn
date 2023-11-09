package br.com.transporte.appGhn.ui.fragment.desempenho.domain.buscaData;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;

public class HashMapSimula12MesesExt {

    @NonNull
    public static HashMap<Integer, BigDecimal> criaHashComRangeDe0a11(){
        final HashMap<Integer, BigDecimal> hashMap = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            hashMap.put(i, BigDecimal.ZERO);
        }
        return hashMap;
    }
}
