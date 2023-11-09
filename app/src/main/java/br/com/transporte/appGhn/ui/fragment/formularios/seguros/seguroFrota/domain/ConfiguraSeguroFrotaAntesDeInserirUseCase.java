package br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.domain;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.repository.CavaloRepository;

public class ConfiguraSeguroFrotaAntesDeInserirUseCase {
    private final CavaloRepository cavaloRepository;
    private final LifecycleOwner lifecycleOwner;
    private final DespesaComSeguroFrota seguroArmazenado;
    private final String placaDigitadaNoCampoAutoComplete;

    public ConfiguraSeguroFrotaAntesDeInserirUseCase(
            final DespesaComSeguroFrota seguroArmazenado,
            final String placaString,
            final Context context,
            final LifecycleOwner lifecycleOwner
    ) {
        this.cavaloRepository = new CavaloRepository(context);
        this.seguroArmazenado = seguroArmazenado;
        this.placaDigitadaNoCampoAutoComplete = placaString;
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface FSeguroFrotaPreparaParaAddCallback {
        void quandoFinaliza(DespesaComSeguroFrota seguroPreparado);
    }

    //----------------------------------------------------------------------------------------------

    public void configuraSeguro(FSeguroFrotaPreparaParaAddCallback callback) {
        seguroArmazenado.setTipoDespesa(DIRETA);
        seguroArmazenado.setValido(true);
        buscaIdDoCavaloGeradorDaDespesaComSeguroEDefineChaveEstrangeira(callback);
    }

    private void buscaIdDoCavaloGeradorDaDespesaComSeguroEDefineChaveEstrangeira(
            final FSeguroFrotaPreparaParaAddCallback callback
    ) {
        final LiveData<Cavalo> observer =
                cavaloRepository.localizaPelaPlaca(placaDigitadaNoCampoAutoComplete);
        observer.observe(lifecycleOwner, new Observer<Cavalo>() {
            @Override
            public void onChanged(Cavalo cavalo) {
                observer.removeObserver(this);
                final Long chaveEstrangeira = cavalo.getId();
                seguroArmazenado.setRefCavaloId(chaveEstrangeira);
                callback.quandoFinaliza(seguroArmazenado);
            }
        });
    }

}
