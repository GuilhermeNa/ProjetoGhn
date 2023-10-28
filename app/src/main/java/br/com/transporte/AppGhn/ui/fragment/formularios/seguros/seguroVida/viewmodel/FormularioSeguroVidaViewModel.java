package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.repository.ParcelaSeguroVidaRepository;
import br.com.transporte.AppGhn.repository.SeguroVidaRepository;
import br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida.domain.ConfiguraSeguroNaPrimeiraInsercaoUseCase;
import br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida.domain.RenovaSeguroUseCase;

public class FormularioSeguroVidaViewModel extends ViewModel {
    private final SeguroVidaRepository seguroRepository;
    private final ParcelaSeguroVidaRepository parcelaRepository;
    private Long seguroId;
    private DespesaComSeguroDeVida seguroArmazenado;
    private DespesaComSeguroDeVida seguroRenovado;
    private TipoFormulario tipoFormulario;

    public FormularioSeguroVidaViewModel(
            SeguroVidaRepository seguroRepository,
            ParcelaSeguroVidaRepository parcelaRepository
    ) {
        this.seguroRepository = seguroRepository;
        this.parcelaRepository = parcelaRepository;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<DespesaComSeguroDeVida> carregaData() {
        return seguroRepository.localizaPeloId(this.seguroId);
    }

    public void configuraSeguroQuandoEstamosInserindoUmNovo() {
        final ConfiguraSeguroNaPrimeiraInsercaoUseCase primeiraInsercaoUseCase =
                new ConfiguraSeguroNaPrimeiraInsercaoUseCase();
        primeiraInsercaoUseCase
                .configuraSeguroVida(this.seguroArmazenado);
    }

    public LiveData<Long> salva() {
        if (seguroArmazenado.getId() == null) {
            return seguroRepository.adiciona(seguroArmazenado);
        } else {
            return seguroRepository.edita(seguroArmazenado);
        }
    }

    public LiveData<String> deleta() {
        if(seguroArmazenado != null){
           return seguroRepository.deleta(seguroArmazenado);
        } else {
            return new MutableLiveData<>("Não foi possível localizar o seguro");
        }
    }

    public LiveData<Void> criaESalvaParcelamentoDesteSeguro(long id) {
        final List<Parcela_seguroVida> parcelasDoSeguro = seguroArmazenado.criaParcelas(id);
        return parcelaRepository.adicionaLista(parcelasDoSeguro);
    }

    public LiveData<Long> renovaSeguro() {
        final RenovaSeguroUseCase renovaSeguroUseCase =
                new RenovaSeguroUseCase(this.seguroArmazenado, this.seguroRenovado);
        return renovaSeguroUseCase
                .renovaSeguroVida(seguroRepository);
    }

    //----------------------------------------------------------------------------------------------


    public Long getSeguroId() {
        return seguroId;
    }

    public void setSeguroId(Long id) {
        this.seguroId = id;
    }

    public DespesaComSeguroDeVida getSeguroArmazenado() {
        return seguroArmazenado;
    }

    public void setSeguroArmazenado(DespesaComSeguroDeVida seguroArmazenado) {
        this.seguroArmazenado = seguroArmazenado;
    }

    public DespesaComSeguroDeVida getSeguroRenovado() {
        return seguroRenovado;
    }

    public void setSeguroRenovado(DespesaComSeguroDeVida seguroRenovado) {
        this.seguroRenovado = seguroRenovado;
    }

    public TipoFormulario getTipoFormulario() {
        return tipoFormulario;
    }

    public void setTipoFormulario(TipoFormulario tipoFormulario) {
        this.tipoFormulario = tipoFormulario;
    }



}
