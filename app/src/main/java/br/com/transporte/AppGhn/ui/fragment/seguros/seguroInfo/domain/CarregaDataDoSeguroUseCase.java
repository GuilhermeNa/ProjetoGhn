package br.com.transporte.AppGhn.ui.fragment.seguros.seguroInfo.domain;

import static br.com.transporte.AppGhn.ui.fragment.seguros.TipoDeSeguro.FROTA;
import static br.com.transporte.AppGhn.ui.fragment.seguros.TipoDeSeguro.VIDA;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.SeguroFrotaRepository;
import br.com.transporte.AppGhn.repository.SeguroVidaRepository;
import br.com.transporte.AppGhn.ui.fragment.seguros.TipoDeSeguro;

public class CarregaDataDoSeguroUseCase {
    private final SeguroFrotaRepository frotaRepository;
    private final SeguroVidaRepository vidaRepository;
    private final CavaloRepository cavaloRepository;
    private final LifecycleOwner lifecycleOwner;

    public CarregaDataDoSeguroUseCase(
            Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.lifecycleOwner = lifecycleOwner;
        this.cavaloRepository = new CavaloRepository(context);
        this.frotaRepository = new SeguroFrotaRepository(context);
        this.vidaRepository = new SeguroVidaRepository(context);
    }

    public interface CarregaDataSeguroVidaUseCaseCallback {
        void quandoSeguroFrota(DespesaComSeguroFrota seguroFrota, String placa);

        void quandoSeguroVida(DespesaComSeguroDeVida seguroVida);
    }

    //----------------------------------------------------------------------------------------------

    public void getData(
            final Long id,
            final TipoDeSeguro tipoDeSeguro,
            final CarregaDataSeguroVidaUseCaseCallback callback
    ) {
        if (tipoDeSeguro == FROTA) {
            buscaDataQuandoRecebermosUmSeguroFrota(id, callback);
        } else if (tipoDeSeguro == VIDA) {
            buscaDataQuandoRecebermosUmSeguroVida(id, callback);
        }
    }

    private void buscaDataQuandoRecebermosUmSeguroVida(
            final Long id,
            @NonNull final CarregaDataSeguroVidaUseCaseCallback callback
    ) {
        vidaRepository.localizaPeloId(id)
                .observe(lifecycleOwner,
                        callback::quandoSeguroVida
                );
    }

    private void buscaDataQuandoRecebermosUmSeguroFrota(
            final Long id,
            @NonNull final CarregaDataSeguroVidaUseCaseCallback callback
    ) {
        frotaRepository.localizaPorId(id)
                .observe(lifecycleOwner,
                        seguroFrota -> buscaPlacaDoCavaloReferenteAEsteSeguro(seguroFrota, callback)
                );
    }

    private void buscaPlacaDoCavaloReferenteAEsteSeguro(
            @NonNull final DespesaComSeguroFrota seguroFrota,
            final CarregaDataSeguroVidaUseCaseCallback callback
    ) {
        cavaloRepository.localizaCavaloPeloId(seguroFrota.getRefCavaloId()).observe(lifecycleOwner,
                cavalo -> {
                    final String placa = cavalo.getPlaca();
                    callback.quandoSeguroFrota(seguroFrota, placa);
                });
    }

}
