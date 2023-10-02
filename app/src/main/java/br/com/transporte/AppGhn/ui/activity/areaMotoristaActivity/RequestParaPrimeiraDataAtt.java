package br.com.transporte.AppGhn.ui.activity.areaMotoristaActivity;

public class RequestParaPrimeiraDataAtt {
    private boolean aguardandoPrimeiraAtt = true;
    private boolean freteDataInicialCarregada = false;
    private boolean abastecimentoDataInicialCarregada = false;
    private boolean custoDataInicialCarregada = false;
    private boolean adiantamentoDataInicialCarregada = false;

    public RequestCallback callback;

    public void setFreteDataInicialCarregada(boolean freteDataInicialCarregada) {
        this.freteDataInicialCarregada = freteDataInicialCarregada;
    }

    public void setAbastecimentoDataInicialCarregada(boolean abastecimentoDataInicialCarregada) {
        this.abastecimentoDataInicialCarregada = abastecimentoDataInicialCarregada;
    }

    public void setCustoDataInicialCarregada(boolean custoDataInicialCarregada) {
        this.custoDataInicialCarregada = custoDataInicialCarregada;
    }

    public void setAdiantamentoDataInicialCarregada(boolean adiantamentoDataInicialCarregada) {
        this.adiantamentoDataInicialCarregada = adiantamentoDataInicialCarregada;
    }

    public boolean isAguardandoPrimeiraAtt() {
        return aguardandoPrimeiraAtt;
    }

    public boolean isFreteDataInicialCarregada() {
        return freteDataInicialCarregada;
    }

    public boolean isAbastecimentoDataInicialCarregada() {
        return abastecimentoDataInicialCarregada;
    }

    public boolean isCustoDataInicialCarregada() {
        return custoDataInicialCarregada;
    }

    public boolean isAdiantamentoDataInicialCarregada() {
        return adiantamentoDataInicialCarregada;
    }

    public interface RequestCallback {
        void primeiraAttReady();
    }

    //----------------------------------------------------------------------------------------------

    public void requestParaPrimeiraAttDeFragment(final RequestCallback callback) {
        if (isAguardandoPrimeiraAtt()) {
            boolean atualizar = verificaSeDataEstaPronta();
            if (atualizar) {
                callback.primeiraAttReady();
                defineQuePrimeiraAtualizacaoJaAconteceu();
            }
        }
    }

    private void defineQuePrimeiraAtualizacaoJaAconteceu() {
        aguardandoPrimeiraAtt = false;
    }

    private boolean verificaSeDataEstaPronta() {
        boolean atualizar = true;
        if (!isFreteDataInicialCarregada()) atualizar = false;
        if (!isAbastecimentoDataInicialCarregada()) atualizar = false;
        if (!isCustoDataInicialCarregada()) atualizar = false;
        if (!isAdiantamentoDataInicialCarregada()) atualizar = false;
        return atualizar;
    }

}
