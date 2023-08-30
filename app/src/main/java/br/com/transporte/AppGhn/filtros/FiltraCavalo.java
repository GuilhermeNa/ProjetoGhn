package br.com.transporte.AppGhn.filtros;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;

public class FiltraCavalo {
    public static List<String> listaDePlacas (List<Cavalo> listaDeCavalos){
        List<String> dataSet = new ArrayList<>();
        for (Cavalo c : listaDeCavalos) {
            dataSet.add(c.getPlaca());
        }
        return dataSet;
    }
}
