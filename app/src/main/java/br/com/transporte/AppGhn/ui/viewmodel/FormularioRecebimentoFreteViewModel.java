package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.RecebimentoDeFreteRepository;
import br.com.transporte.AppGhn.repository.Resource;

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
