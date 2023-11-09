package br.com.transporte.appGhn.ui.fragment.formularios.certificado.domain;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.RENOVANDO;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.appGhn.model.enums.TipoCertificado;
import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.model.enums.TipoFormulario;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CertificadoRepository;

public class FCertificadoChecaDuplicidadeAoSalvarUseCase {
    public static final long VALOR_NULO = 0L;
    private final CertificadoRepository certificadoRepository;
    private final CavaloRepository cavaloRepository;
    private final TipoDespesa tipoDespesa;
    private final TipoFormulario tipoFormulario;
    private final LifecycleOwner lifecycleOwner;

    public FCertificadoChecaDuplicidadeAoSalvarUseCase(
            LifecycleOwner lifecycleOwner,
            Context context,
            TipoDespesa tipoDespesa,
            TipoFormulario tipoFormulario
    ) {
        this.lifecycleOwner = lifecycleOwner;
        this.tipoFormulario = tipoFormulario;
        this.tipoDespesa = tipoDespesa;
        this.cavaloRepository = new CavaloRepository(context);
        this.certificadoRepository = new CertificadoRepository(context);
    }

    public interface ChecaDuplicidadeCallback {
        void quandoRenovando();
        void quandoNaoTemCertificadoDuplicado(Long cavaloId);
        void quandoTemCertificadoDuplicado();
    }

    //----------------------------------------------------------------------------------------------

    public void run(
            final String nomeCertificado,
            final String placaDoCavalo,
            final ChecaDuplicidadeCallback callback
    ) {
        final TipoCertificado tipoCertificadoAdicionado = TipoCertificado.valueOf(nomeCertificado);
        verificaQuandoRenovandoDespesa(callback);
        verificaQuandoAdicionandoDespesaIndireta(tipoCertificadoAdicionado, callback);
        verificaQuandoAdicionandoDespesaDireta(tipoCertificadoAdicionado, placaDoCavalo, callback);
    }

    private void verificaQuandoRenovandoDespesa(final ChecaDuplicidadeCallback callback) {
        if(tipoFormulario == RENOVANDO){
            callback.quandoRenovando();
        }
    }

    private void verificaQuandoAdicionandoDespesaIndireta(
            final TipoCertificado tipoCertificado,
            final ChecaDuplicidadeCallback callback
    ) {
        if (tipoFormulario == ADICIONANDO && tipoDespesa == INDIRETA) {
            certificadoRepository.buscaPorDuplicidadeQuandoDespesaIndireta(tipoDespesa, tipoCertificado, true)
                    .observe(lifecycleOwner,
                            listaCertificados -> {
                                if (listaCertificados.isEmpty()) {
                                    callback.quandoNaoTemCertificadoDuplicado(VALOR_NULO);
                                } else {
                                    callback.quandoTemCertificadoDuplicado();
                                }
                            });
        }
    }

    private void verificaQuandoAdicionandoDespesaDireta(
            final TipoCertificado tipo,
            final String placaCavalo,
            final ChecaDuplicidadeCallback callback
    ) {
        if (tipoFormulario == ADICIONANDO && tipoDespesa == DIRETA) {
           cavaloRepository.localizaPelaPlaca(placaCavalo).observe(lifecycleOwner,
                    cavaloRecebido -> {
                        if (cavaloRecebido != null) {
                            buscaListaDeCertificadosDesteCavalo(cavaloRecebido.getId(), tipo, callback);
                        }
                    });
        }
    }

    private void buscaListaDeCertificadosDesteCavalo(
            final Long id,
            final TipoCertificado tipo,
            final ChecaDuplicidadeCallback callback
    ) {
        certificadoRepository.buscaDuplicidadeQuandoDespesaDireta(true, id, tipo)
                .observe(lifecycleOwner,
                        listaCertificados -> {
                            if (listaCertificados.isEmpty()) {
                                callback.quandoNaoTemCertificadoDuplicado(id);
                            } else {
                                callback.quandoTemCertificadoDuplicado();
                            }
                        });
    }

}
