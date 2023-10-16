package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroFrota.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroFrota.viewmodel.FormularioSeguroFrotaViewModel;

public class FSeguroFrotaCarregaDataUseCase {
    private final FormularioSeguroFrotaViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;
    private Context context;

    public FSeguroFrotaCarregaDataUseCase(
            FormularioSeguroFrotaViewModel viewModel,
            LifecycleOwner lifecycleOwner
    ) {
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface FSeguroFrotaCarregaDataCallback {
        void carregaListaDePlacas(List<String> listaDePlacas);

        void carregaSeguroFrota(DespesaComSeguroFrota seguroCarregado);

        void carregaCavaloRelacionadoAoSeguro(Cavalo cavalo);
    }

    //----------------------------------------------------------------------------------------------

    public void getData(final FSeguroFrotaCarregaDataCallback callback) {
        carregaListaDePlacas(callback);
        tentaCarregarSeguro(callback);
    }

    private void carregaListaDePlacas(@NonNull final FSeguroFrotaCarregaDataCallback callback) {
        final LiveData<List<String>> observer =
                viewModel.carregaListaDePlacas();
        observer.observe(
                lifecycleOwner, new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> listaDePlacas) {
                        removeObserverAposPrimeiroRecebimentoDeData();
                        notificaResultado(listaDePlacas);
                    }

                    private void removeObserverAposPrimeiroRecebimentoDeData() {
                        observer.removeObserver(this);
                    }

                    private void notificaResultado(final List<String> listaDePlacas) {
                        callback.carregaListaDePlacas(listaDePlacas);
                    }
                });
    }

    private void tentaCarregarSeguro(@NonNull final FSeguroFrotaCarregaDataCallback callback) {
        final LiveData<DespesaComSeguroFrota> observer =
                viewModel.localizaPeloId(viewModel.getSeguroId());
        observer.observe(
                lifecycleOwner, new Observer<DespesaComSeguroFrota>() {
                    @Override
                    public void onChanged(DespesaComSeguroFrota seguroCarregado) {
                        removeObserverAposPrimeiroRecebimentoDeData();
                        if (seguroCarregado != null) {
                            carregaCavaloReferenteAEsteSeguro(seguroCarregado, callback);
                        } else {
                            notificaResultadoQuandoNaoLocalizouOSeguro();
                        }
                    }

                    private void removeObserverAposPrimeiroRecebimentoDeData() {
                        observer.removeObserver(this);
                    }

                    private void notificaResultadoQuandoNaoLocalizouOSeguro() {
                        callback.carregaSeguroFrota(null);
                    }
                });
    }

    private void carregaCavaloReferenteAEsteSeguro(
            @NonNull final DespesaComSeguroFrota seguroCarregado,
            final FSeguroFrotaCarregaDataCallback callback
    ) {
        final LiveData<Cavalo> observer =
                viewModel.localizaCavaloPeloId(seguroCarregado.getRefCavaloId());
        observer.observe(
                lifecycleOwner, new Observer<Cavalo>() {
                    @Override
                    public void onChanged(Cavalo cavalo) {
                        removeObserverAposPrimeiroRecebimentoDeData();
                        notificaResultado(seguroCarregado, callback, cavalo);
                    }

                    private void removeObserverAposPrimeiroRecebimentoDeData() {
                        observer.removeObserver(this);
                    }

                    private void notificaResultado(
                            @NonNull final DespesaComSeguroFrota seguroCarregado,
                            @NonNull final FSeguroFrotaCarregaDataCallback callback,
                            final Cavalo cavalo
                    ) {
                        callback.carregaCavaloRelacionadoAoSeguro(cavalo);
                        callback.carregaSeguroFrota(seguroCarregado);
                    }
                });
    }

}
