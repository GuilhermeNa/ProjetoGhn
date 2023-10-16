package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroFrota.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ParcelaSeguroFrotaRepository;
import br.com.transporte.AppGhn.repository.SeguroFrotaRepository;

public class FormularioSeguroFrotaViewModel extends ViewModel {
    private final CavaloRepository cavaloRepository;
    private final SeguroFrotaRepository seguroRepository;
    private final ParcelaSeguroFrotaRepository parcelaRepository;

    public FormularioSeguroFrotaViewModel(
            CavaloRepository cavaloRepository,
            SeguroFrotaRepository seguroRepository,
            ParcelaSeguroFrotaRepository parcelaRepository
    ) {
        this.cavaloRepository = cavaloRepository;
        this.seguroRepository = seguroRepository;
        this.parcelaRepository = parcelaRepository;
    }

    //---------------------------------------------------------------------------------------------

    private Long seguroId;
    private DespesaComSeguroFrota seguroRenovado;
    private DespesaComSeguroFrota seguroArmazenado;
    private Cavalo cavaloRelacionado;
    private List<String> listaDePlacas;
    private TipoFormulario tipoFormulario;

    public LiveData<List<String>> carregaListaDePlacas() {
        return cavaloRepository.buscaPlacas();
    }

    public LiveData<DespesaComSeguroFrota> localizaPeloId(final Long id) {
        return seguroRepository.localizaPorId(id);
    }

    public LiveData<Cavalo> localizaCavaloPeloId(final Long cavaloId) {
        return cavaloRepository.localizaCavaloPeloId(cavaloId);
    }

    public LiveData<String> deleta() {
        if (seguroArmazenado != null) {
            return seguroRepository.deleta(seguroArmazenado);
        } else {
            return new MutableLiveData<>("Seguro não encontrado");
        }
    }

    public LiveData<Long> salva(@NonNull final DespesaComSeguroFrota seguroArmazenado) {
        if (seguroArmazenado.getId() != null) {
            return seguroRepository.edita(seguroArmazenado);
        } else {
            return seguroRepository.adiciona(seguroArmazenado);
        }
    }

    public LiveData<Void> adicionaListaParcelas(final List<Parcela_seguroFrota> listaParcelas){
        return parcelaRepository.adicionaLista(listaParcelas);
    }


    //----------------------------------------------------------------------------------------------


    public DespesaComSeguroFrota getSeguroRenovado() {
        return seguroRenovado;
    }

    public void setSeguroRenovado(DespesaComSeguroFrota seguroRenovado) {
        this.seguroRenovado = seguroRenovado;
    }

    public TipoFormulario getTipoFormulario() {
        return tipoFormulario;
    }

    public void setTipoFormulario(TipoFormulario tipoFormulario) {
        this.tipoFormulario = tipoFormulario;
    }

    public Long getSeguroId() {
        return seguroId;
    }

    public void setSeguroId(Long seguroId) {
        this.seguroId = seguroId;
    }

    public DespesaComSeguroFrota getSeguroArmazenado() {
        return seguroArmazenado;
    }

    public void setSeguroArmazenado(DespesaComSeguroFrota seguroArmazenado) {
        this.seguroArmazenado = seguroArmazenado;
    }

    public List<String> getListaDePlacas() {
        return listaDePlacas;
    }

    public void setListaDePlacas(List<String> listaDePlacas) {
        this.listaDePlacas = listaDePlacas;
    }

    public Cavalo getCavaloRelacionado() {
        return cavaloRelacionado;
    }

    public void setCavaloRelacionado(Cavalo cavaloRelacionado) {
        this.cavaloRelacionado = cavaloRelacionado;
    }

    public LiveData<Long> renova(
            final DespesaComSeguroFrota seguroArmazenado,
            final DespesaComSeguroFrota seguroRenovado
    ) {
        seguroRepository.edita(seguroRenovado);
        return seguroRepository.adiciona(seguroArmazenado);
    }




}
