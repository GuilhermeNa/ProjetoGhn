package br.com.transporte.AppGhn.ui.fragment.formularios.certificado.extension;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.enums.TipoCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class FCertificadoGetListaCertificadosExt {

    public static List<String> run(final TipoDespesa tipoDespesa) {
        return TipoCertificado.listaCertificados().stream()
                .filter(t -> t.getTipoDespesa() == tipoDespesa)
                .map(TipoCertificado::getDescricao)
                .collect(Collectors.toList());
    }
}
