package br.com.transporte.appGhn.ui.viewmodel.areaMotoristaViewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;

import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.repository.AdiantamentoRepository;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.appGhn.repository.CustoDePercursoRepository;
import br.com.transporte.appGhn.repository.FreteRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.repository.Resource;
import br.com.transporte.appGhn.util.DataUtil;

public class AreaMotoristaActViewModel extends ViewModel {
    private final CustoDeAbastecimentoRepository abastecimentoRepository;
    private final CustoDePercursoRepository custoRepository;
    private final MotoristaRepository motoristaRepository;
    private final CavaloRepository cavaloRepository;
    private final FreteRepository freteRepository;
    private final AdiantamentoRepository adiantamentoRepository;
    private String NOME_MOTORISTA_ACESSADO;
    private Cavalo CAVALO_ACESSADO;
    private LocalDate sharedDataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
    private LocalDate sharedDataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
    public AreaMotoristaActViewModel(
            FreteRepository freteRepository,
            CustoDeAbastecimentoRepository abastecimentoRepository,
            CustoDePercursoRepository custoRepository,
            CavaloRepository cavaloRepository,
            MotoristaRepository motoristaRepository,
            AdiantamentoRepository adiantamentoRepository
    ) {
        this.freteRepository = freteRepository;
        this.abastecimentoRepository = abastecimentoRepository;
        this.custoRepository = custoRepository;
        this.cavaloRepository = cavaloRepository;
        this.motoristaRepository = motoristaRepository;
        this.adiantamentoRepository = adiantamentoRepository;
    }

    public void setCavaloAcessado(Cavalo CAVALO_ACESSADO) {
        this.CAVALO_ACESSADO = CAVALO_ACESSADO;
    }

    public void setNomeMotoristaAcessado(String NOME_MOTORISTA_ACESSADO) {
        this.NOME_MOTORISTA_ACESSADO = NOME_MOTORISTA_ACESSADO;
    }

    public String getNomeMotoristaAcessado() {
        return NOME_MOTORISTA_ACESSADO;
    }

    public Cavalo getCavaloAcessado() {
        return CAVALO_ACESSADO;
    }

    public LocalDate getSharedDataInicial() {
        return sharedDataInicial;
    }

    public void setSharedDataInicial(LocalDate sharedDataInicial) {
        this.sharedDataInicial = sharedDataInicial;
    }

    public LocalDate getSharedDataFinal() {
        return sharedDataFinal;
    }

    public void setSharedDataFinal(LocalDate sharedDataFinal) {
        this.sharedDataFinal = sharedDataFinal;
    }

    //----------------------------- Data padrao, usada para consulta -------------------------------

    private AreaMotoristaActViewModel_freteDataHelper freteDataHelper;
    private AreaMotoristaActViewModel_abastecimentoDataHelper abastecimentoDataHelper;
    private AreaMotoristaActViewModel_custoDataHelper custoDataHelper;
    private AreaMotoristaActViewModel_adiantamentoDataHelper adiantamentoDataHelper;

    public void configuraFreteHelper(List<Frete> listaFretesPorCavalo) {
        freteDataHelper = new AreaMotoristaActViewModel_freteDataHelper(listaFretesPorCavalo, sharedDataInicial, sharedDataFinal);
    }

    public void configuraAbastecimentoHelper(List<CustosDeAbastecimento> listaAbastecimentoPorCavalo) {
        abastecimentoDataHelper = new AreaMotoristaActViewModel_abastecimentoDataHelper(listaAbastecimentoPorCavalo, sharedDataInicial, sharedDataFinal);
    }

    public void configuraCustoHelper(List<CustosDePercurso> listaCustoPorCavalo) {
        custoDataHelper = new AreaMotoristaActViewModel_custoDataHelper(listaCustoPorCavalo, sharedDataInicial, sharedDataFinal);
    }

    public void configuraAdiantamentoHelper(List<Adiantamento> listaAdiantamentoPorCavalo) {
        adiantamentoDataHelper = new AreaMotoristaActViewModel_adiantamentoDataHelper(listaAdiantamentoPorCavalo);
    }

    public List<CustosDeAbastecimento> getList_abastecimentoTodos() {
        return abastecimentoDataHelper.getDataSet();
    }

    public List<CustosDeAbastecimento> getSharedList_abastecimento() {
        return abastecimentoDataHelper.getFragmentsSharedData();
    }

    public List<Adiantamento> getSharedList_adiantamento() {
        return adiantamentoDataHelper.getFragmentsSharedData();
    }

    public List<CustosDePercurso> getSharedList_custo() {
        return custoDataHelper.getFragmentsSharedData();
    }


    public List<Frete> getSharedList_frete() {
        return freteDataHelper.getFragmentsSharedData();
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Motorista> localizaMotorista(@Nullable final Long id) {
        return motoristaRepository.localizaMotorista(id);
    }

    public LiveData<Cavalo> localizaCavalo(final long id) {
        return cavaloRepository.localizaCavaloPeloId(id);
    }

    public LiveData<Resource<List<Frete>>> buscaFretesPorCavaloId(final long cavaloId) {
        return freteRepository.buscaFretesPorCavaloId(cavaloId);
    }

    public LiveData<Resource<List<CustosDeAbastecimento>>> buscaAbastecimentosPorCavaloId(
            final long cavaloId
    ) {
        return abastecimentoRepository.buscaAbastecimentosPorCavaloId(cavaloId);
    }

    public LiveData<Resource<List<CustosDePercurso>>> buscaCustoPercursoPorCavaloId(
            final long cavaloId
    ) {
        return custoRepository.buscaCustosPercursoPorCavaloId(cavaloId);
    }

    public LiveData<Resource<List<Adiantamento>>> buscaAdiantamentosPorCavaloId(
            final long cavaloId
    ) {
        return adiantamentoRepository.buscaAdiantamentosPorCavaloId(cavaloId);
    }

    public void solicitaAlteracaoDeSharedData() {
        freteDataHelper.atualizaSharedData(sharedDataInicial, sharedDataFinal);
        abastecimentoDataHelper.atualizaSharedData(sharedDataInicial, sharedDataFinal);
        custoDataHelper.atualizaSharedData(sharedDataInicial, sharedDataFinal);
    }


}
