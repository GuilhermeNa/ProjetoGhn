package br.com.transporte.appGhn.ui.activity.comissao.helpers;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.appGhn.ui.viewmodel.ComissaoActViewModel;

public class ComissaoViewModelObserverHelper {
    private boolean aguardandoPrimeiraAtualizacaoDeUi = true;
    private final ComissaoActViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;
    private ComissaoPrimeiraAtualizacao primeiraAtt;
    private ObserverHelperCallback callback;

    public void setCallback(ObserverHelperCallback callback) {
        this.callback = callback;
    }

    public ComissaoViewModelObserverHelper(ComissaoActViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface ObserverHelperCallback {

        void dadosPreparadosParaInicializacao();

        void notificaAttEmFrete();

        void notificaAttEmAdiantamento();

        void notificaAttEmCustoDePercurso();

    }

    //----------------------------------------------------------------------------------------------

    public void configuraObservers() {
        primeiraAtt = new ComissaoPrimeiraAtualizacao();
        ComissaoDemaisAtualizacoes demaisAtt = new ComissaoDemaisAtualizacoes();
        observerCavalo();
        observerMotorista();
        observerAdiantamento();
        observerPercurso();
        observerFrete();
        observerSalario();
    }

    private void observerSalario() {
        viewModel.buscaCustosSalario().observe(lifecycleOwner,
                resource -> {

                    if (resource.getDado() != null) {
                        viewModel.armazenaDataBaseSalario(resource.getDado());
                        if (aguardandoPrimeiraAtualizacaoDeUi) {
                            primeiraAtt.setCustoSalarioCarregado(true);
                            notificaActSeTodosOsDadosEstiveremProntosParaPrimeiraAtt();
                        }
                    }
                });
    }

    private void observerFrete() {
        viewModel.buscaFretes().observe(lifecycleOwner,
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.armazenaDataBaseFrete(resource.getDado());
                        if (aguardandoPrimeiraAtualizacaoDeUi) {
                            primeiraAtt.setFreteCarregado(true);
                            notificaActSeTodosOsDadosEstiveremProntosParaPrimeiraAtt();
                        } else {
                            callback.notificaAttEmFrete();
                        }
                    }
                });
    }

    private void observerPercurso() {
        viewModel.buscaCustosPercurso().observe(lifecycleOwner,
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.setDataSetReembolso(resource.getDado());
                        if (aguardandoPrimeiraAtualizacaoDeUi) {
                            primeiraAtt.setCustoPercursoCarregado(true);
                            notificaActSeTodosOsDadosEstiveremProntosParaPrimeiraAtt();
                        } else {
                            callback.notificaAttEmCustoDePercurso();
                        }
                    }
                });
    }

    private void observerAdiantamento() {
        viewModel.buscaAdiantamentos().observe(lifecycleOwner,
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.setDataSetAdiantamento(resource.getDado());
                        if (aguardandoPrimeiraAtualizacaoDeUi) {
                            primeiraAtt.setAdiantamentoCarregado(true);
                            notificaActSeTodosOsDadosEstiveremProntosParaPrimeiraAtt();
                        } else {
                            callback.notificaAttEmAdiantamento();
                        }
                    }
                });
    }

    private void observerMotorista() {
        viewModel.buscaMotoristas().observe(lifecycleOwner,
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.setDataSet_motorista(resource.getDado());
                        if (aguardandoPrimeiraAtualizacaoDeUi) {
                            primeiraAtt.setMotoristaCarregado(true);
                            notificaActSeTodosOsDadosEstiveremProntosParaPrimeiraAtt();
                        }
                    }
                });
    }

    private void observerCavalo() {
        viewModel.buscaCavalos().observe(lifecycleOwner,
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.setDataSet_cavalo(resource.getDado());
                        if (aguardandoPrimeiraAtualizacaoDeUi) {
                            primeiraAtt.setCavaloCarregado(true);
                            notificaActSeTodosOsDadosEstiveremProntosParaPrimeiraAtt();
                        }
                    }
                });
    }

    private void notificaActSeTodosOsDadosEstiveremProntosParaPrimeiraAtt() {
        if (primeiraAtt.todosOsDadosProntos()) {
            callback.dadosPreparadosParaInicializacao();
            aguardandoPrimeiraAtualizacaoDeUi = false;
        }
    }

}

class ComissaoPrimeiraAtualizacao {
    private boolean cavaloCarregado = false;
    private boolean motoristaCarregado = false;
    private boolean freteCarregado = false;
    private boolean adiantamentoCarregado = false;
    private boolean custoPercursoCarregado = false;
    private boolean custoSalarioCarregado = false;

    public void setCavaloCarregado(boolean cavaloCarregado) {
        this.cavaloCarregado = cavaloCarregado;
    }

    public void setMotoristaCarregado(boolean motoristaCarregado) {
        this.motoristaCarregado = motoristaCarregado;
    }

    public void setFreteCarregado(boolean freteCarregado) {
        this.freteCarregado = freteCarregado;
    }

    public void setAdiantamentoCarregado(boolean adiantamentoCarregado) {
        this.adiantamentoCarregado = adiantamentoCarregado;
    }

    public void setCustoPercursoCarregado(boolean custoPercursoCarregado) {
        this.custoPercursoCarregado = custoPercursoCarregado;
    }

    public void setCustoSalarioCarregado(boolean custoSalarioCarregado) {
        this.custoSalarioCarregado = custoSalarioCarregado;
    }

    //----------------------------------------------------------------------------------------------

    public boolean todosOsDadosProntos() {
        boolean isReady = true;
        if (!cavaloCarregado) isReady = false;
        if (!motoristaCarregado) isReady = false;
        if (!freteCarregado) isReady = false;
        if (!adiantamentoCarregado) isReady = false;
        if (!custoPercursoCarregado) isReady = false;
        if (!custoSalarioCarregado) isReady = false;
        return isReady;
    }

}

class ComissaoDemaisAtualizacoes{
    private boolean freteCarregado = false;
    private boolean adiantamentoCarregado = false;
    private boolean custoPercursoCarregado = false;
    private boolean custoSalarioCarregado = false;

    public void setFreteCarregado(boolean freteCarregado) {
        this.freteCarregado = freteCarregado;
    }
    public void setAdiantamentoCarregado(boolean adiantamentoCarregado) {
        this.adiantamentoCarregado = adiantamentoCarregado;
    }
    public void setCustoPercursoCarregado(boolean custoPercursoCarregado) {
        this.custoPercursoCarregado = custoPercursoCarregado;
    }
    public void setCustoSalarioCarregado(boolean custoSalarioCarregado) {
        this.custoSalarioCarregado = custoSalarioCarregado;
    }

    //----------------------------------------------------------------------------------------------

    public boolean todosOsDadosProntos() {
        boolean isReady = true;
        if (!freteCarregado) isReady = false;
        if (!adiantamentoCarregado) isReady = false;
        if (!custoPercursoCarregado) isReady = false;
        if (!custoSalarioCarregado) isReady = false;
        return isReady;
    }

    public void resetaBooleanos() {
        //Resetado para que possa aguardar por uma nova atualizacao
        freteCarregado = false;
        adiantamentoCarregado = false;
        custoPercursoCarregado = false;
        custoSalarioCarregado = false;
    }

}
