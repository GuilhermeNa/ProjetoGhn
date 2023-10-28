package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.LUCRO_LIQUIDO;
import static br.com.transporte.AppGhn.model.enums.TipoMeses.MES_DEFAULT;

import androidx.annotation.NonNull;

import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.AppGhn.util.DataUtil;

public class InicializaObjetoQueArmazenaRequisicaoDeDadosExt {
    private static final TipoDeRequisicao TIPO_REQUISICAO_INICIAL = LUCRO_LIQUIDO;
    private static final int ANO_REQUISICAO_INICIAL = DataUtil.capturaDataDeHojeParaConfiguracaoInicial().getYear();
    private static final int MES_REQUISICAO_INICIAL = MES_DEFAULT.getRef();
    private static final Long CAVALO_REQUISICAO_INICIAL = null;
    private static Boolean RATEIO_REQUISICAO_INICIAL = true;

    @NonNull
    public static DataRequest run(){
        return new DataRequest(
                TIPO_REQUISICAO_INICIAL,
                CAVALO_REQUISICAO_INICIAL,
                ANO_REQUISICAO_INICIAL,
                MES_REQUISICAO_INICIAL,
                RATEIO_REQUISICAO_INICIAL
        );
    }

}
