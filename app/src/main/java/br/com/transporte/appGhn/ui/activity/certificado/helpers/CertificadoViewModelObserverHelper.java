package br.com.transporte.appGhn.ui.activity.certificado.helpers;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.appGhn.ui.viewmodel.CertificadoActViewModel;

public class CertificadoViewModelObserverHelper {
    private boolean aguardandoPrimeiraAtualizacaoDeUi = true;
    private final CertificadoActViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;
    private ObserverHelperCallback callback;

    public void setCallback(ObserverHelperCallback callback) {
        this.callback = callback;
    }

    public CertificadoViewModelObserverHelper(CertificadoActViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface ObserverHelperCallback {
        void dadosPreparadosParaInicializacao();
        void certificadosCarregados();


    }

    //----------------------------------------------------------------------------------------------

    public void configuraObservers() {
        observerCavalo();
        observerCertificado();
        observerMotorista();
    }

    private void observerCavalo() {
        viewModel.buscaCavalos().observe(lifecycleOwner,
                resource -> {
                    if (resource.getDado() != null) {
                        if(aguardandoPrimeiraAtualizacaoDeUi){
                            viewModel.cavalosArmazenados = resource.getDado();
                            callback.dadosPreparadosParaInicializacao();
                            aguardandoPrimeiraAtualizacaoDeUi = false;
                        }
                    }
                });
    }

    private void observerCertificado() {
        viewModel.buscaCertificados().observe(lifecycleOwner,
                resource -> {
                    if(resource.getDado() != null) {
                        viewModel.certificadosArmazenados = resource.getDado();
                        callback.certificadosCarregados();
                    }
                });
    }

    private void observerMotorista(){
        viewModel.buscaMotoristas().observe(lifecycleOwner,
                resource -> {
                    if(resource.getDado() != null) {
                        viewModel.motoristasArmazenados = resource.getDado();
                    }
                });
    }

}

