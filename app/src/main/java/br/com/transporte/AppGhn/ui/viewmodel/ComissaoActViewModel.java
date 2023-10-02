package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.filtros.FiltraSalario;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.repository.AdiantamentoRepository;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.CustoDePercursoRepository;
import br.com.transporte.AppGhn.repository.CustoDeSalarioRepository;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.repository.Resource;
import br.com.transporte.AppGhn.util.DataUtil;

public class ComissaoActViewModel extends ViewModel {
    private final CavaloRepository cavaloRepository;
    private final MotoristaRepository motoristaRepository;
    private final FreteRepository freteRepository;
    private final AdiantamentoRepository adiantamentoRepository;
    private final CustoDePercursoRepository custoDePercursoRepository;
    private final CustoDeSalarioRepository salarioRepository;

    public ComissaoActViewModel(
            CavaloRepository cavaloRepository,
            MotoristaRepository motoristaRepository,
            FreteRepository freteRepository,
            AdiantamentoRepository adiantamentoRepository,
            CustoDePercursoRepository custoDePercursoRepository,
            CustoDeSalarioRepository salarioRepository
    ) {
        this.cavaloRepository = cavaloRepository;
        this.motoristaRepository = motoristaRepository;
        this.freteRepository = freteRepository;
        this.adiantamentoRepository = adiantamentoRepository;
        this.custoDePercursoRepository = custoDePercursoRepository;
        this.salarioRepository = salarioRepository;
    }

    //----------------------------------------------------------------------------------------------

    private List<Cavalo> dataSet_cavalo;
    private List<Motorista> dataSet_motorista;
    private LocalDate dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
    private LocalDate dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
    private final ComissaoViewModelDataSetFrete armazenaFrete = new ComissaoViewModelDataSetFrete();
    private final ComissaoViewModelDataSetSalario armazenaSalario = new ComissaoViewModelDataSetSalario();
    private List<Adiantamento> dataSetAdiantamento;
    private List<CustosDePercurso> dataSetReembolso;

    public void setDataSet_cavalo(List<Cavalo> dataSet_cavalo) {
        this.dataSet_cavalo = dataSet_cavalo;
    }

    public void setDataSet_motorista(List<Motorista> dataSet_motorista) {
        this.dataSet_motorista = dataSet_motorista;
    }

    public List<Cavalo> getDataSet_cavalo() {
        return new ArrayList<>(dataSet_cavalo);
    }

    public List<Motorista> getDataSet_motorista() {
        return new ArrayList<>(dataSet_motorista);
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public List<Adiantamento> getDataSetAdiantamento() {
        return new ArrayList<>(dataSetAdiantamento);
    }

    public void setDataSetAdiantamento(List<Adiantamento> dataSetAdiantamento) {
        this.dataSetAdiantamento = dataSetAdiantamento;
    }

    public List<CustosDePercurso> getDataSetReembolso() {
        return new ArrayList<>(dataSetReembolso);
    }

    public void setDataSetReembolso(List<CustosDePercurso> dataSetReembolso) {
        this.dataSetReembolso = dataSetReembolso;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Resource<List<Cavalo>>> buscaCavalos() {
        return cavaloRepository.buscaCavalos();
    }

    public LiveData<Resource<List<Motorista>>> buscaMotoristas() {
        return motoristaRepository.buscaMotoristas();
    }

    public LiveData<Resource<List<Frete>>> buscaFretes() {
        return freteRepository.buscaFretes();
    }

    public LiveData<Resource<List<Adiantamento>>> buscaAdiantamentos() {
        return adiantamentoRepository.buscaAdiantamentos();
    }

    public LiveData<Resource<List<CustosDePercurso>>> buscaCustosPercurso() {
        return custoDePercursoRepository.buscaCustosPercurso();
    }

    public LiveData<Resource<List<CustosDeSalario>>> buscaCustosSalario() {
        return salarioRepository.buscaCustosSalario();
    }

    public LiveData<Long> salvaFrete(@NonNull final Frete frete) {
        return freteRepository.editaFrete(frete);
    }

    public LiveData<Long> salvaAdiantamento(@NonNull final Adiantamento adiantamento) {
        return adiantamentoRepository.adiciona(adiantamento);
    }

    public void editaCusto(final CustosDePercurso custoPercurso) {
        custoDePercursoRepository.editCustoPercurso(custoPercurso);
    }

    public LiveData<Long> adicionaSalario(CustosDeSalario salario) {
        return salarioRepository.adiciona(salario);
    }

    public LiveData<CustosDeSalario> localizaSalario(final Long id){
        return salarioRepository.localizaSalario(id);
    }

    public  void armazenaDataBaseFrete(final List<Frete> dataSetFrete){
        armazenaFrete.setDataSetBase(dataSetFrete);
    }

    public  void armazenaDataBaseSalario(final List<CustosDeSalario> dataSetSalario){
        armazenaSalario.setDataSetBase(dataSetSalario);
    }

    public void solicitaAlteracaoDeDataSetPorNovoFiltroDeData(){
        final List<Frete> freteFiltrado = FiltraFrete.listaPorData(armazenaFrete.getDataSetBase(), dataInicial, dataFinal);
        armazenaFrete.setDataSetComFiltroAplicado(freteFiltrado);

        final List<CustosDeSalario> salarioFiltrado = FiltraSalario.listaPorData(armazenaSalario.getDataSetBase(), dataInicial, dataFinal);
        armazenaSalario.setDataSetComFiltroAplicado(salarioFiltrado);
    }

    public List<Frete> getListaFreteComFiltro(){
        return new ArrayList<>(armazenaFrete.getDataSetComFiltroAplicado());
    }

    public List<CustosDeSalario> getListaSalarioComFiltro(){
        return new ArrayList<>(armazenaSalario.getDataSetComFiltroAplicado());
    }

    public List<Frete> getDataSetBaseFrete(){
        return new ArrayList<>(armazenaFrete.getDataSetBase());
    }

}

class ComissaoViewModelDataSetFrete{
    private List<Frete> dataSetBase;
    private List<Frete> dataSetComFiltroAplicado;

    public List<Frete> getDataSetBase() {
        return dataSetBase;
    }

    public void setDataSetBase(List<Frete> dataSetBase) {
        this.dataSetBase = dataSetBase;
    }

    public List<Frete> getDataSetComFiltroAplicado() {
        return dataSetComFiltroAplicado;
    }

    public void setDataSetComFiltroAplicado(List<Frete> dataSetComFiltroAplicado) {
        this.dataSetComFiltroAplicado = dataSetComFiltroAplicado;
    }
}

class ComissaoViewModelDataSetSalario{
    private List<CustosDeSalario> dataSetBase;
    private List<CustosDeSalario> dataSetComFiltroAplicado;

    public List<CustosDeSalario> getDataSetBase() {
        return dataSetBase;
    }

    public void setDataSetBase(List<CustosDeSalario> dataSetBase) {
        this.dataSetBase = dataSetBase;
    }

    public List<CustosDeSalario> getDataSetComFiltroAplicado() {
        return dataSetComFiltroAplicado;
    }

    public void setDataSetComFiltroAplicado(List<CustosDeSalario> dataSetComFiltroAplicado) {
        this.dataSetComFiltroAplicado = dataSetComFiltroAplicado;
    }

}



