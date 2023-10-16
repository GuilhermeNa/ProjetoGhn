package br.com.transporte.AppGhn.ui.fragment.formularios.certificado.domain;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.ADICIONANDO;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.repository.CavaloRepository;

public class ConfiguraTiposUseCase {
    private final CavaloRepository repository;
    private final LifecycleOwner lifecycleOwner;
    private final TipoFormulario tipoFormulario;
    private final TipoDespesa tipoDespesa;

    public ConfiguraTiposUseCase(
            Context context,
            LifecycleOwner lifecycleOwner,
            TipoFormulario tipoFormulario,
            TipoDespesa tipoDespesa
    ) {
        repository = new CavaloRepository(context);
        this.lifecycleOwner = lifecycleOwner;
        this.tipoFormulario = tipoFormulario;
        this.tipoDespesa = tipoDespesa;
    }

    public interface ConfiguraTipoUseCaseCallback {
        void adicionandoDespesaDireta();

        void adicionandoDespesaIndireta();

        void renovandoDespesaDireta(Cavalo cavalo);

        void renovandoDespesaIndireta();

        void editandoDespesaDireta(Cavalo cavalo);

        void editandoDespesaIndireta();
    }

    //----------------------------------------------------------------------------------------------

    public void run(
            final DespesaCertificado certificadoRecebido,
            final ConfiguraTipoUseCaseCallback callback
    ) {
        if (certificadoRecebido == null) {
            configuraTipoParaQuandoEstamosAdicionandoNovoCertificado(callback);
        } else {
            switch (tipoFormulario) {
                case RENOVANDO:
                    configuraTipoParaQuandoEstamosRenovandoCertificado(certificadoRecebido, callback);
                    break;

                case EDITANDO:
                    configuraTipoParaQuandoEstamosEditandoCertificado(certificadoRecebido, callback);
                    break;
            }
        }
    }

    private void configuraTipoParaQuandoEstamosEditandoCertificado(
            final DespesaCertificado certificadoRecebido,
            final ConfiguraTipoUseCaseCallback callback
    ) {
        if (tipoDespesa == DIRETA) {
            buscaCavaloVinculado(
                    certificadoRecebido.getRefCavaloId(),
                    callback::editandoDespesaDireta
            );
        } else if (tipoDespesa == INDIRETA) {
            callback.editandoDespesaIndireta();
        }
    }

    private void configuraTipoParaQuandoEstamosRenovandoCertificado(
            final DespesaCertificado certificadoRecebido,
            final ConfiguraTipoUseCaseCallback callback
    ) {
        if (tipoDespesa == DIRETA) {
            buscaCavaloVinculado(
                    certificadoRecebido.getRefCavaloId(),
                    callback::renovandoDespesaDireta
            );
        } else if (tipoDespesa == INDIRETA) {
            callback.renovandoDespesaIndireta();
        }
    }

    private void configuraTipoParaQuandoEstamosAdicionandoNovoCertificado(final ConfiguraTipoUseCaseCallback callback) {
        if (tipoFormulario == ADICIONANDO && tipoDespesa == DIRETA) {
            callback.adicionandoDespesaDireta();
        } else if (tipoFormulario == ADICIONANDO && tipoDespesa == INDIRETA) {
            callback.adicionandoDespesaIndireta();
        }
    }

    private void buscaCavaloVinculado(
            final Long id,
            final QuandoFinalizaCallback callback
    ) {
        repository.localizaCavaloPeloId(id).observe(lifecycleOwner,
                cavalo -> {
                    if (cavalo != null) {
                        callback.quandoFinaliza(cavalo);
                    }
                });
    }

    public interface QuandoFinalizaCallback {
        void quandoFinaliza(Cavalo cavalo);
    }

}
