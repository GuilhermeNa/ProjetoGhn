package br.com.transporte.AppGhn.ui.fragment.media.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.AppGhn.repository.CustoDePercursoRepository;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.Resource;
import br.com.transporte.AppGhn.ui.fragment.media.domain.DadosCavaloUseCase;

public class MediaFragmentViewModel extends ViewModel {
    private static final int POSICAO_INICIAL = 0;
    private Cavalo cavaloSelecionado;
    private List<Cavalo> dataSetCavalos;
    private final CavaloRepository cavaloRepository;
    private final CustoDeAbastecimentoRepository abastecimentoRepository;
    private final FreteRepository freteRepository;
    private final CustoDePercursoRepository custosRepository;
    private int posicaoDoCavaloSelecionado = POSICAO_INICIAL;

    public MediaFragmentViewModel(
            CavaloRepository cavaloRepository,
            CustoDeAbastecimentoRepository abastecimentoRepository,
            FreteRepository freteRepository,
            CustoDePercursoRepository custosRepository
    ) {
        this.cavaloRepository = cavaloRepository;
        this.abastecimentoRepository = abastecimentoRepository;
        this.freteRepository = freteRepository;
        this.custosRepository = custosRepository;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Resource<List<Cavalo>>> carrregaCavalos() {
        return cavaloRepository.buscaCavalos();
    }

    public void configuraDadosDeCavaloUseCase() {
        cavaloSelecionado = DadosCavaloUseCase.getCavaloSelecionado(dataSetCavalos, POSICAO_INICIAL);
        DadosCavaloUseCase.removeCavaloSelecionadoDaLista(dataSetCavalos, POSICAO_INICIAL);
    }

    public LiveData<Resource<List<CustosDeAbastecimento>>> carregaAbastecimentosDoCavaloSelecionado() {
        return abastecimentoRepository.buscaAbastecimentosPorCavaloId(cavaloSelecionado.getId());
    }

    public LiveData<Resource<List<CustosDeAbastecimento>>> carregaAbastecimentosPorCavaloId() {
        return abastecimentoRepository.buscaAbastecimentosPorCavaloId(cavaloSelecionado.getId());
    }

    public LiveData<List<Frete>> carregaFretes(
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        return freteRepository.bucaFretePorCavaloEData(cavaloSelecionado.getId(), dataInicial, dataFinal);
    }

    public LiveData<List<CustosDePercurso>> carregaCustosDePercurso(
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        return custosRepository.buscaCustoPorCavaloEData(cavaloSelecionado.getId(), dataInicial, dataFinal);
    }

    //----------------------------------------------------------------------------------------------

    public int getPosicaoDoCavaloSelecionado() {
        return posicaoDoCavaloSelecionado;
    }

    public void setPosicaoDoCavaloSelecionado(int posicaoDoCavaloSelecionado) {
        this.posicaoDoCavaloSelecionado = posicaoDoCavaloSelecionado;
    }

    public Cavalo getCavaloSelecionado() {
        return cavaloSelecionado;
    }

    public void setCavaloSelecionado(Cavalo cavaloSelecionado) {
        this.cavaloSelecionado = cavaloSelecionado;
    }

    public List<Cavalo> getDataSetCavalos() {
        return new ArrayList<>(dataSetCavalos);
    }

    public void setDataSetCavalos(List<Cavalo> dataSetCavalos) {
        this.dataSetCavalos = dataSetCavalos;
    }

}



