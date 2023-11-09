package br.com.transporte.appGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.RecebimentoDeFrete;
import br.com.transporte.appGhn.repository.FreteRepository;
import br.com.transporte.appGhn.repository.RecebimentoDeFreteRepository;

public class FormularioRecebimentoFreteViewModel extends ViewModel {
    private final FreteRepository freteRepository;
    private final RecebimentoDeFreteRepository recebimentoRepository;

    public FormularioRecebimentoFreteViewModel(
            final FreteRepository freteRepository,
            final RecebimentoDeFreteRepository recebimentoRepository
    ) {
        this.freteRepository = freteRepository;
        this.recebimentoRepository = recebimentoRepository;
    }

    //----------------------------------------------------------------------------------------------
    public RecebimentoDeFrete recebimentoArmazenado;

    public LiveData<Frete> localizaFrete(final long id) {
        return freteRepository.localizaFrete(id);
    }

    public LiveData<RecebimentoDeFrete> localizaRecebimento(final Long id) {
        return recebimentoRepository.localizaPeloId(id);
    }

    public LiveData<List<RecebimentoDeFrete>> buscaRecebimentosPorFreteId(final Long id) {
        return recebimentoRepository.buscaRecebimentosPorFreteId(id);
    }

    public LiveData<Long> salva(@NonNull final RecebimentoDeFrete recebimento) {
        if (recebimento.getId() > 0) {
            return recebimentoRepository.substitui(recebimento);
        } else {
            return recebimentoRepository.adiciona(recebimento);
        }
    }

    public LiveData<String> deleta(){
        if(recebimentoArmazenado != null){
            return recebimentoRepository.deleta(recebimentoArmazenado);
        } else {
            return new MutableLiveData<>("Recebimento não encontrado");
        }
    }


}
