package br.com.transporte.AppGhn.ui.fragment.media.helpers;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.ui.adapter.MediaAdapter_Abastecimentos;

public class MediaAdapterAbastecimentoHelper {
    public static final String PRIMEIRA_FLAG_SELECIONADA = "primeira_flag";
    public static final String SEGUNDA_FLAG_SELECIONADA = "segunda_flag";
    public static final String NULL_FLAG = "null_flag";
    private CustosDeAbastecimento flagAbastecimento1, flagAbastecimento2;
    private MediaAdapter_Abastecimentos adapter;
    private final Cavalo primeiroCavaloExibido;
    private final Context context;
    private final RecyclerView recyclerDeAbastecimentos;
    private AdapterCallback adapterCallback;
    private List<CustosDeAbastecimento> dataSet;

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    public MediaAdapterAbastecimentoHelper(Cavalo primeiroCavaloExibido, Context context, RecyclerView recyclerDeAbastecimentos) {
        this.primeiroCavaloExibido = primeiroCavaloExibido;
        this.context = context;
        this.recyclerDeAbastecimentos = recyclerDeAbastecimentos;
    }

    public CustosDeAbastecimento getFlagAbastecimento1() {
        return flagAbastecimento1;
    }

    public CustosDeAbastecimento getFlagAbastecimento2() {
        return flagAbastecimento2;
    }

//----------------------------------------------------------------------------------------------
    //                                          Configuracao                                      ||
    //----------------------------------------------------------------------------------------------

    public void configuraAdapter() {
        adapter = new MediaAdapter_Abastecimentos(getListaDeAbastecimentosComFlags(primeiroCavaloExibido.getId()), context);
        recyclerDeAbastecimentos.setAdapter(adapter);

        adapter.setOnItemClickListener((posicao, abastecimento) -> {
            String flag = armazenaItemSelecionado(abastecimento);
            if (flag != NULL_FLAG) {
                adapter.remove(posicao);
            }
            adapterCallback.onClick(flag, abastecimento);
        });
    }

    private List<CustosDeAbastecimento> getListaDeAbastecimentosComFlags(Long cavaloId) {
        RoomCustosAbastecimentoDao abastecimentoDao = GhnDataBase.getInstance(context).getRoomCustosAbastecimentoDao();
        /*return abastecimentoDao.todos().stream()
                .filter(c -> c.getRefCavaloId() == cavaloId)
                .filter(CustosDeAbastecimento::isFlagAbastecimentoTotal)
                .collect(Collectors.toList());*/
        return new ArrayList<>();
    }

    private String armazenaItemSelecionado(CustosDeAbastecimento abastecimento) {
        if (flagAbastecimento1 == null) {
            flagAbastecimento1 = abastecimento;
            return PRIMEIRA_FLAG_SELECIONADA;
        } else if (flagAbastecimento2 == null) {
            flagAbastecimento2 = abastecimento;
            return SEGUNDA_FLAG_SELECIONADA;
        }
        return NULL_FLAG;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Metodos Publicos                                  ||
    //----------------------------------------------------------------------------------------------

    public void atualizaAdapterDeAbastecimentosAoSelecionarNovoCavalo(Long cavaloId) {
        resetaDataSetEFlags(flagAbastecimento1);
        resetaDataSetEFlags(flagAbastecimento2);
        if (flagAbastecimento1 != null) flagAbastecimento1 = null;
        if (flagAbastecimento2 != null) flagAbastecimento2 = null;
        adapter.atualiza(getListaDeAbastecimentosComFlags(cavaloId));
    }

    private void resetaDataSetEFlags(CustosDeAbastecimento flagAbastecimento) {
        int posicaoAInserir;

        if (flagAbastecimento != null) {
            dataSet = adapter.getDataSet();
            posicaoAInserir = ReinsereNoDataset.getPosicaoAInserir(dataSet, flagAbastecimento);
            adapter.adiciona(posicaoAInserir, flagAbastecimento);
        }
    }

    public void removeCardDaViewEInsereNovamenteNaLista(String flagSelecionada) {
        int posicaoAInserir;
        dataSet = adapter.getDataSet();
        switch (flagSelecionada) {
            case PRIMEIRA_FLAG_SELECIONADA:
                posicaoAInserir = ReinsereNoDataset.getPosicaoAInserir(dataSet, flagAbastecimento1);
                adapter.adiciona(posicaoAInserir, flagAbastecimento1);
                flagAbastecimento1 = null;
                break;

            case SEGUNDA_FLAG_SELECIONADA:
                posicaoAInserir = ReinsereNoDataset.getPosicaoAInserir(dataSet, flagAbastecimento2);
                adapter.adiciona(posicaoAInserir, flagAbastecimento2);
                flagAbastecimento2 = null;
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          Callback                                          ||
    //----------------------------------------------------------------------------------------------

    public interface AdapterCallback {
        void onClick(String flagSelecinada, CustosDeAbastecimento abastecimento);
    }

}

//----------------------------------------------------------------------------------------------
//                                          CLass                                             ||
//----------------------------------------------------------------------------------------------

class ReinsereNoDataset {

    public static int getPosicaoAInserir(List<CustosDeAbastecimento> dataSet, CustosDeAbastecimento abastecimentoAInserir) {
        int dtSize = verificaTamanhoDaLista(dataSet);

        boolean teste1 = verificaSeDatasetEstaVazio(dtSize);
        if (teste1) return 0;

        return verificaPosicaoAInserir(dataSet, abastecimentoAInserir);
    }

    private static int verificaPosicaoAInserir(List<CustosDeAbastecimento> dataSet, CustosDeAbastecimento abastecimentoAInserir) {
        CustosDeAbastecimento abastecimentoLocalizado = null;
        Collections.sort(dataSet, Comparator.comparing(CustosDeAbastecimento::getData));

        for (CustosDeAbastecimento c : dataSet) {
            if (abastecimentoAInserir.getData().isBefore(c.getData())) {
                abastecimentoLocalizado = c;
                break;
            }
        }

        if (abastecimentoLocalizado == null) {
            return dataSet.size();
        } else {
            return dataSet.indexOf(abastecimentoLocalizado);
        }

    }

    private static boolean verificaSeDatasetEstaVazio(int dtSize) {
        return dtSize == 0;
    }

    private static int verificaTamanhoDaLista(List<CustosDeAbastecimento> dataSet) {
        return dataSet.size();
    }
}
