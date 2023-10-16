package br.com.transporte.AppGhn.ui.fragment.formularios.despesaAdm.domain;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;

import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;

public class FDespesaAdmPreparaAmbienteUseCase {
    private final TipoDespesa tipoDespesa;
    private final TipoFormulario tipoFormulario;

    public FDespesaAdmPreparaAmbienteUseCase(
            TipoDespesa tipoDespesa,
            TipoFormulario tipoFormulario
    ) {
        this.tipoDespesa = tipoDespesa;
        this.tipoFormulario = tipoFormulario;
    }

    public interface ConfiguraAmbienteFormularioDespesaUseCase {
        void adicionadoDespesaDireta();

        void editandoDespesaDireta();

        void adicionandoDespesaIndireta();

        void editandoDespesaIndireta();
    }

    //----------------------------------------------------------------------------------------------

    public void run(
            final DespesaAdm despesaAdm,
            final ConfiguraAmbienteFormularioDespesaUseCase callback
    ) {
        if (despesaAdm == null) {
            quandoAdicionando(callback);
        } else {
            quandoEditando(callback);
        }
    }

    private void quandoAdicionando(final ConfiguraAmbienteFormularioDespesaUseCase callback) {
        if (tipoDespesa == DIRETA) {
            callback.adicionadoDespesaDireta();
        } else if (tipoDespesa == INDIRETA) {
            callback.adicionandoDespesaIndireta();
        }
    }

    private void quandoEditando(final ConfiguraAmbienteFormularioDespesaUseCase callback) {
        if (tipoDespesa == DIRETA) {
            callback.editandoDespesaDireta();
        } else if (tipoDespesa == INDIRETA) {
            callback.editandoDespesaIndireta();
        }
    }

}
