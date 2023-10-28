package br.com.transporte.AppGhn.dataSource.local;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.repository.RepositoryCallback;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaAbastecimentosPorAnoECavaloIdTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaCertificadosPorAnoECavaloIdTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaCustosPercursoPorAnoECavaloIdTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaDespesasAdmPorAnoECavaloIdTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaDespesasFrotaPorAnoECavaloIdTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaDespesasVidaPorAnoECavaloIdTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaFretePorAnoECavaloIdTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaImpostosPorAnoECavaloIdTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaLucroLiquidoTask;
import br.com.transporte.AppGhn.tasks.fragmentDesempenho.BuscaManutencaoPorAnoECavaloIdTask;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class LocalDesempenhoFragmentDataSource {
    private final ExecutorService executor;
    private final Handler handler;
    private final RoomFreteDao freteDao;
    private final RoomCustosAbastecimentoDao abastecimentoDao;
    private final RoomCavaloDao cavaloDao;
    private final RoomMotoristaDao motoristaDao;
    private final RoomCustosPercursoDao percursoDao;
    private final RoomCustosDeManutencaoDao manutencaoDao;
    private final RoomDespesaCertificadoDao certificadoDao;
    private final RoomDespesaImpostoDao impostoDao;
    private final RoomDespesaAdmDao admDao;
    private final RoomParcela_seguroFrotaDao parcelaFrotaDao;
    private final RoomParcela_seguroVidaDao parcelaVidaDao;


    public LocalDesempenhoFragmentDataSource(Context context) {
        final GhnApplication application = new GhnApplication();
        final GhnDataBase dataBase = GhnDataBase.getInstance(context);
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();

        abastecimentoDao = dataBase.getRoomCustosAbastecimentoDao();
        parcelaFrotaDao = dataBase.getRoomParcela_seguroFrotaDao();
        parcelaVidaDao = dataBase.getRoomParcela_seguroVidaDao();
        certificadoDao = dataBase.getRoomDespesaCertificadoDao();
        manutencaoDao = dataBase.getRoomCustosDeManutencaoDao();
        percursoDao = dataBase.getRoomCustosPercursoDao();
        impostoDao = dataBase.getRoomDespesaImpostoDao();
        motoristaDao = dataBase.getRoomMotoristaDao();
        admDao = dataBase.getRoomDespesaAdmDao();
        cavaloDao = dataBase.getRoomCavaloDao();
        freteDao = dataBase.getRoomFreteDao();
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<List<Cavalo>> buscaCavalos() {
        return cavaloDao.todos();
    }

    public LiveData<List<Motorista>> buscaMotoristas() {
        return motoristaDao.todos();
    }

    public void buscaFretesPorAnoEOuCavaloId(
            final int ano,
            @Nullable final Long cavaloId,
            @NonNull RepositoryCallback<ResourceData> callback
    ) {
        final BuscaFretePorAnoECavaloIdTask task = new BuscaFretePorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(freteDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaAbastecimentosPorAnoEOuCavaloId(
            final int ano,
            final Long cavaloId,
            @NonNull final RepositoryCallback<ResourceData> callback
    ) {
        final BuscaAbastecimentosPorAnoECavaloIdTask task = new BuscaAbastecimentosPorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(abastecimentoDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaCustosPercursoPorAnoEOuCavaloId(
            final int ano,
            final Long cavaloId,
            @NonNull final RepositoryCallback<ResourceData> callback
    ) {
        final BuscaCustosPercursoPorAnoECavaloIdTask task = new BuscaCustosPercursoPorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(percursoDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaManutencaoPorAnoEOuCavaloId(
            final int ano,
            final Long cavaloId,
            @NonNull final RepositoryCallback<ResourceData> callback
    ) {
        final BuscaManutencaoPorAnoECavaloIdTask task = new BuscaManutencaoPorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(manutencaoDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaCertificadosPorAnoEOuCavaloId(
            final int ano,
            final Long cavaloId,
            @NonNull final RepositoryCallback<ResourceData> callback
    ) {
        final BuscaCertificadosPorAnoECavaloIdTask task = new BuscaCertificadosPorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(certificadoDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaImpostosPorAnoEOuCavaloId(
            final int ano,
            final Long cavaloId,
            @NonNull final RepositoryCallback<ResourceData> callback
    ) {
        final BuscaImpostosPorAnoECavaloIdTask task = new BuscaImpostosPorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(impostoDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaDespesasAdmPorAnoEOuCavaloId(
            final int ano,
            final Long cavaloId,
            @NonNull final RepositoryCallback<ResourceData> callback
    ) {
        final BuscaDespesasAdmPorAnoECavaloIdTask task = new BuscaDespesasAdmPorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(admDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaDespesaFrotaPorAnoEOuCavaloId(
            final int ano,
            final Long cavaloId,
            @NonNull final RepositoryCallback<ResourceData> callback
    ) {
        final BuscaDespesasFrotaPorAnoECavaloIdTask task = new BuscaDespesasFrotaPorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(parcelaFrotaDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaDespesaVidaPorAnoEOuCavaloId(
            final int ano,
            final Long cavaloId,
            @NonNull final RepositoryCallback<ResourceData> callback
    ) {
        final BuscaDespesasVidaPorAnoECavaloIdTask task = new BuscaDespesasVidaPorAnoECavaloIdTask(executor, handler);
        task.solicitaBusca(parcelaVidaDao, ano, cavaloId,
                callback::sucesso);
    }

    public void buscaLucroLiquidoPorAnoEOuCavaloId(
            final int ano,
            @Nullable final Long cavaloId,
            @NonNull RepositoryCallback<ResourceData> callback
    ) {
        final BuscaLucroLiquidoTask task = new BuscaLucroLiquidoTask(executor, handler);
        task.solicitaBusca(
                ano, cavaloId, freteDao, abastecimentoDao, percursoDao, manutencaoDao,
                certificadoDao, impostoDao, admDao, parcelaFrotaDao, parcelaVidaDao,
                callback::sucesso);
    }

}
