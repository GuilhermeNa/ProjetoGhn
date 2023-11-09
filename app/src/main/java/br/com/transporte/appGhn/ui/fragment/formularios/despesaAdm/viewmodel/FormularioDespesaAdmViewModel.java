package br.com.transporte.appGhn.ui.fragment.formularios.despesaAdm.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.model.enums.TipoFormulario;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.DespesaAdmRepository;
import br.com.transporte.appGhn.repository.Resource;

public class FormularioDespesaAdmViewModel extends ViewModel {

    private final CavaloRepository cavaloRepository;
    private final DespesaAdmRepository despesaAdmRepository;

    public FormularioDespesaAdmViewModel(
            CavaloRepository cavaloRepository,
            DespesaAdmRepository despesaAdmRepository
    ) {
        this.cavaloRepository = cavaloRepository;
        this.despesaAdmRepository = despesaAdmRepository;
    }

    //----------------------------------------------------------------------------------------------

    private Long despesaId;
    public DespesaAdm despesaArmazenada;
    private TipoDespesa tipoDespesa;
    private TipoFormulario tipoFormulario;
    private List<String> listaDePlacas;
    private List<Cavalo> listaDeCavalos;

    public LiveData<DespesaAdm> localizaPeloId(final Long despesaId) {
        return despesaAdmRepository.localizaPeloId(despesaId);
    }

    public LiveData<Long> salva(@NonNull final DespesaAdm despesaAdm) {
        if (despesaAdm.getId() != null) {
            return despesaAdmRepository.edita(despesaAdm);
        } else {
            return despesaAdmRepository.adiciona(despesaAdm);
        }
    }

    public LiveData<String> deleta(){
        if(despesaArmazenada != null){
            return despesaAdmRepository.deleta(despesaArmazenada);
        } else {
            return new MutableLiveData<>("Despesa não encontrada");
        }
    }

    public LiveData<List<String>> carregaListaDePlacas() {
        return cavaloRepository.buscaPlacas();
    }

    public LiveData<Resource<List<Cavalo>>> buscaTodosCavalos() {
        return cavaloRepository.buscaCavalos();
    }

    //----------------------------------------------------------------------------------------------

    public void setIdDespesaAdmCarregado(final Long tentaCarregarDespesaId) {
        this.despesaId = tentaCarregarDespesaId;
    }

    public void setTipoDespesa(final TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public TipoFormulario getTipoFormulario() {
        return tipoFormulario;
    }

    public void setTipoFormulario(TipoFormulario tipoFormulario) {
        this.tipoFormulario = tipoFormulario;
    }

    public Long getDespesaId() {
        return despesaId;
    }

    public void setDespesaId(Long despesaId) {
        this.despesaId = despesaId;
    }

    public List<String> getListaDePlacas() {
        return listaDePlacas;
    }

    public void setListaDePlacas(List<String> listaDePlacas) {
        this.listaDePlacas = listaDePlacas;
    }

    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    public List<Cavalo> getListaDeCavalos() {
        return listaDeCavalos;
    }

    public void setListaDeCavalos(List<Cavalo> listaDeCavalos) {
        this.listaDeCavalos = listaDeCavalos;
    }

}
