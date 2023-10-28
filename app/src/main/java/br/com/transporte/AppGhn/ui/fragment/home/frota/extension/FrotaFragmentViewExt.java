package br.com.transporte.AppGhn.ui.fragment.home.frota.extension;

public class FrotaFragmentViewExt {

    public interface VisibilidadeCallback {
        void todasAsListasEstaoVazias();

        void apenasListaDeCavalosVazia();

        void apenasListaDeReboquesVazia();

        void temInformacaoParaExibir();

    }

    public static void defineVisibilidadeDasListasEHeaders(
            final int lista1,
            final int lista2,
            final VisibilidadeCallback callback
    ) {
        boolean todasAsListasEstaoVazias = lista1 + lista2 == 0;
        boolean apenasListaDeCavalosVazia = lista1 == 0;
        boolean apenasListaDeReboquesVazia = lista2 == 0;

        if (todasAsListasEstaoVazias) {
            callback.todasAsListasEstaoVazias();

        } else if (apenasListaDeCavalosVazia) {
            callback.apenasListaDeCavalosVazia();

        } else if (apenasListaDeReboquesVazia) {
            callback.apenasListaDeReboquesVazia();

        } else {
            callback.temInformacaoParaExibir();
        }

    }

}
