package br.com.transporte.appGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.appGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.appGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.appGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.appGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.appGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.appGhn.database.dao.RoomFreteDao;
import br.com.transporte.appGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.appGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.appGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.appGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.appGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.appGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.appGhn.filtros.FiltraFrete;
import br.com.transporte.appGhn.filtros.FiltraParcelaSeguroFrota;
import br.com.transporte.appGhn.filtros.FiltraParcelaSeguroVida;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaLucroLiquidoTask extends BaseTask {
    private RoomFreteDao freteDao;
    private RoomCustosAbastecimentoDao abastecimentoDao;
    private RoomCustosPercursoDao percursoDao;
    private RoomCustosDeManutencaoDao manutencaoDao;
    private RoomDespesaCertificadoDao certificadoDao;
    private RoomDespesaImpostoDao impostoDao;
    private RoomDespesaAdmDao admDao;
    private RoomParcela_seguroFrotaDao parcelaFrotaDao;
    private RoomParcela_seguroVidaDao parcelaVidaDao;

    public BuscaLucroLiquidoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final int ano,
            @Nullable final Long cavaloId,

            final RoomFreteDao freteDao,
            final RoomCustosAbastecimentoDao abastecimentoDao,
            final RoomCustosPercursoDao percursoDao,
            final RoomCustosDeManutencaoDao manutencaoDao,
            final RoomDespesaCertificadoDao certificadoDao,
            final RoomDespesaImpostoDao impostoDao,
            final RoomDespesaAdmDao admDao,
            final RoomParcela_seguroFrotaDao parcelaFrotaDao,
            final RoomParcela_seguroVidaDao parcelaVidaDao,
            final TaskCallback<ResourceData> callback
    ) {
        this.freteDao = freteDao;
        this.abastecimentoDao = abastecimentoDao;
        this.percursoDao = percursoDao;
        this.manutencaoDao = manutencaoDao;
        this.certificadoDao = certificadoDao;
        this.impostoDao = impostoDao;
        this.admDao = admDao;
        this.parcelaFrotaDao = parcelaFrotaDao;
        this.parcelaVidaDao = parcelaVidaDao;

        executor.execute(
                () -> {
                    final ResourceData resourceData = new ResourceData();

                    if(cavaloId == null){
                        realizaBuscaDeTodosAssincrona(resourceData, ano);
                    } else {
                        realizaBuscaPorCavaloAssincrona(resourceData, ano, cavaloId);
                    }

                    notificaResultado(resourceData, callback);
                });
    }

    private void realizaBuscaDeTodosAssincrona(
            @NonNull final ResourceData resourceData,
            final int ano
    ) {
        List<Frete> fretes = freteDao.buscaTodosParaTask();
        fretes = FiltraFrete.listaPorAno(fretes, ano);
        resourceData.setDataSetFrete(fretes);

        List<CustosDeAbastecimento> abastecimentos = abastecimentoDao.buscaTodosParaTask();
        abastecimentos = FiltraCustosAbastecimento.listaPorAno(abastecimentos, ano);
        resourceData.setDataSetAbastecimento(abastecimentos);

        List<CustosDePercurso> custosPercurso = percursoDao.buscaTodosParaTask();
        custosPercurso = FiltraCustosPercurso.listaPorAno(custosPercurso, ano);
        resourceData.setDataSetCustoPercurso(custosPercurso);

        List<CustosDeManutencao> manutencoes = manutencaoDao.buscaTodosParaTask();
        manutencoes = FiltraCustosManutencao.listaPorAno(manutencoes, ano);
        resourceData.setDataSetCustoManutencao(manutencoes);

        List<DespesaCertificado> certificados = certificadoDao.buscaTodosParaTask();
        certificados = FiltraDespesasCertificado.listaPorAno(certificados, ano);
        resourceData.setDataSetDespesaCertificado(certificados);

        List<DespesasDeImposto> impostos = impostoDao.buscaTodosParaTask();
        impostos = FiltraDespesasImposto.listaPorAno(impostos, ano);
        resourceData.setDataSetDespesaImposto(impostos);

        List<DespesaAdm> despesasAdm = admDao.buscaTodosParaTask();
        despesasAdm = FiltraDespesasAdm.listaPorAno(despesasAdm, ano);
        resourceData.setDataSetDespesaAdm(despesasAdm);

        List<Parcela_seguroFrota> parcelasFrota = parcelaFrotaDao.buscaTodosPagosParaTask();
        parcelasFrota = FiltraParcelaSeguroFrota.listaPorAno(parcelasFrota, ano);
        resourceData.setDataSetDespesaSeguroFrota(parcelasFrota);

        List<Parcela_seguroVida> parcelasVida = parcelaVidaDao.buscaTodosParaTask();
        parcelasVida = FiltraParcelaSeguroVida.listaPorAno(parcelasVida, ano);
        resourceData.setDataSetDespesaSeguroVida(parcelasVida);

    }

    private void realizaBuscaPorCavaloAssincrona(
            @NonNull final ResourceData resourceData,
            final int ano,
            final Long cavaloId
    ) {
        List<Frete> fretes = freteDao.buscaPorCavaloIdParaTask(cavaloId);
        fretes = FiltraFrete.listaPorAno(fretes, ano);
        resourceData.setDataSetFrete(fretes);

        List<CustosDeAbastecimento> abastecimentos = abastecimentoDao.buscaPorCavaloIdParaTask(cavaloId);
        abastecimentos = FiltraCustosAbastecimento.listaPorAno(abastecimentos, ano);
        resourceData.setDataSetAbastecimento(abastecimentos);

        List<CustosDePercurso> custosPercurso = percursoDao.buscaPorCavaloIdParaTask(cavaloId);
        custosPercurso = FiltraCustosPercurso.listaPorAno(custosPercurso, ano);
        resourceData.setDataSetCustoPercurso(custosPercurso);

        List<CustosDeManutencao> manutencoes = manutencaoDao.buscaPorCavaloIdParaTask(cavaloId);
        manutencoes = FiltraCustosManutencao.listaPorAno(manutencoes, ano);
        resourceData.setDataSetCustoManutencao(manutencoes);

        List<DespesaCertificado> certificados = certificadoDao.buscaPorCavaloIdParaTask(cavaloId);
        certificados = FiltraDespesasCertificado.listaPorAno(certificados, ano);
        resourceData.setDataSetDespesaCertificado(certificados);

        List<DespesasDeImposto> impostos = impostoDao.buscaPorCavaloIdParaTask(cavaloId);
        impostos = FiltraDespesasImposto.listaPorAno(impostos, ano);
        resourceData.setDataSetDespesaImposto(impostos);

        List<DespesaAdm> despesasAdm = admDao.buscaPorCavaloIdParaTask(cavaloId);
        despesasAdm = FiltraDespesasAdm.listaPorAno(despesasAdm, ano);
        resourceData.setDataSetDespesaAdm(despesasAdm);

        List<Parcela_seguroFrota> parcelasFrota = parcelaFrotaDao.buscaPorCavaloIdEPagasParaTask(cavaloId);
        parcelasFrota = FiltraParcelaSeguroFrota.listaPorAno(parcelasFrota, ano);
        resourceData.setDataSetDespesaSeguroFrota(parcelasFrota);

        List<Parcela_seguroVida> parcelasVida = parcelaVidaDao.buscaPorCavaloIdParaTask(cavaloId);
        parcelasVida = FiltraParcelaSeguroVida.listaPorAno(parcelasVida, ano);
        resourceData.setDataSetDespesaSeguroVida(parcelasVida);
    }

    private void notificaResultado(
            final ResourceData resourceData,
            @NonNull final TaskCallback<ResourceData> callback
    ) {
        handler.post(
                () -> callback.finalizado(resourceData));
    }

}
