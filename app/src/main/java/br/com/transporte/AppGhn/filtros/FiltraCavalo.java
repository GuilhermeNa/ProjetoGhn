package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.abstracts.Frota;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;

public class FiltraCavalo {
    @NonNull
    public static List<String> listaDePlacas(@NonNull List<Cavalo> listaDeCavalos) {
        List<String> dataSet = new ArrayList<>();
        for (Cavalo c : listaDeCavalos) {
            dataSet.add(c.getPlaca());
        }
        return dataSet;
    }

    @Nullable
    public static Frota localizaPeloId(@NonNull List<Cavalo> listaDeCavalos, Long refCavaloId) {
        Cavalo cavaloEncontrado = null;

        for (Cavalo c : listaDeCavalos) {
            if (c.getId() == refCavaloId) {
                cavaloEncontrado = c;
            }
        }

        if (cavaloEncontrado != null) {
            return cavaloEncontrado;
        }
        return null;
    }

    public static Cavalo localizaPelaPlaca(@NonNull List<Cavalo> listaDeCavalos, String placaDoCavaloDestino) {
        Cavalo cavaloEncontrado = null;
        placaDoCavaloDestino = placaDoCavaloDestino.toUpperCase(Locale.ROOT);
        for (Cavalo c : listaDeCavalos) {
            if (c.getPlaca().toUpperCase(Locale.ROOT).equals(placaDoCavaloDestino)) {
                cavaloEncontrado = c;
            }
        }
        return cavaloEncontrado;
    }
}
