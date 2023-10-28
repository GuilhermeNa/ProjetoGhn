package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import static br.com.transporte.AppGhn.model.enums.TipoMeses.MES_DEFAULT;

import androidx.annotation.NonNull;

import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;

public class VerificaRequisicaoExt {

    public static boolean seBuscaTodosOsCavalosUseCase(@NonNull final DataRequest dataRequest){
        if(dataRequest.getCavaloId() == null) return true;
        else return false;
    }

    public static boolean seBuscaMesEspecifico(@NonNull final DataRequest dataRequest){
        if(dataRequest.getMes() != MES_DEFAULT.getRef()) return true;
        else return false;
    }

    public static boolean seSolicitaRateioDeDespesas(@NonNull final DataRequest dataRequest){
        if(dataRequest.isExibirRateio()) return true;
        else return false;
    }

}
