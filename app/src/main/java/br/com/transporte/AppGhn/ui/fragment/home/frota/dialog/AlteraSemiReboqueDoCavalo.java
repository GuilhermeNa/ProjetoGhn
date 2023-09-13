package br.com.transporte.AppGhn.ui.fragment.home.frota.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.tasks.cavalo.AdicionaCavaloTask;
import br.com.transporte.AppGhn.tasks.cavalo.BuscaTodosCavalosTask;
import br.com.transporte.AppGhn.tasks.reboque.AdicionaReboqueTask;
import br.com.transporte.AppGhn.tasks.reboque.AtualizaReboqueTask;

public class AlteraSemiReboqueDoCavalo {
    private final Context context;
    private final SemiReboque reboque;
    private GhnDataBase dataBase;
    private DialogAlteraSrCallBack dialogAlteraSrCallBack;
    private AutoCompleteTextView autoComplete;
    private List<String> listaDePlacas;
    private RoomCavaloDao cavaloDao;
    private List<Cavalo> listaDeCavalos;
    private ExecutorService executorService;
    private Handler handler;

    public AlteraSemiReboqueDoCavalo(Context context, SemiReboque sr) {
        this.context = context;
        this.reboque = sr;
        dataBase = GhnDataBase.getInstance(context);
    }

    public void setDialogAlteraSrCallBack(DialogAlteraSrCallBack dialogAlteraSrCallBack) {
        this.dialogAlteraSrCallBack = dialogAlteraSrCallBack;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show Dialog                                       ||
    //----------------------------------------------------------------------------------------------

    public void dialogAlteraSrCavalo() {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.altera_semireboque_cavalo, null);
        autoComplete = viewCriada.findViewById(R.id.dialog_alterasrcavalo_placa_cavalo_edittext);
        cavaloDao = dataBase.getRoomCavaloDao();
        RoomSemiReboqueDao reboqueDao = dataBase.getRoomReboqueDao();

        configuraAdapter();

        new AlertDialog.Builder(context)
                .setTitle(reboque.getPlaca())
                .setView(viewCriada)
                .setPositiveButton("Alterar", (dialog, which) -> {
                    String placaDoCavaloAtual = FiltraCavalo.localizaPeloId(listaDeCavalos, reboque.getRefCavaloId()).getPlaca().toUpperCase(Locale.ROOT);
                    String placaDoCavaloDestino = autoComplete.getText().toString().toUpperCase(Locale.ROOT);

                    if (placaDoCavaloAtual.equals(placaDoCavaloDestino)) {
                        dialogAlteraSrCallBack.quandoFalhaEmAlterarSr("O Semi Reboque já está vinculado a este Cavalo.");
                    } else if (!listaDePlacas.contains(placaDoCavaloDestino)) {
                        dialogAlteraSrCallBack.quandoFalhaEmAlterarSr("Destino não localizado.");
                    } else {
                        Cavalo cavaloDestino = FiltraCavalo.localizaPelaPlaca(listaDeCavalos, placaDoCavaloDestino);

                        reboque.setRefCavaloId(cavaloDestino.getId());

                        AtualizaReboqueTask atualizaReboqueTask = new AtualizaReboqueTask(executorService, handler);
                        atualizaReboqueTask.solicitaAtualizacao(reboqueDao, reboque, () -> {
                            dialogAlteraSrCallBack.quandoSucessoEmAlterarSr(reboque);
                        });
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    //-------------------------------------------
    // -> Configura Adapter                    ||
    //-------------------------------------------

    private void configuraLista() {
        String[] arrayDePlacas = listaDePlacas.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayDePlacas);
        autoComplete.setAdapter(adapter);
    }

    private void configuraAdapter() {
        GhnApplication application = new GhnApplication();
        executorService = application.getExecutorService();
        handler = application.getMainThreadHandler();
        BuscaTodosCavalosTask buscaTodosCavalosTask = new BuscaTodosCavalosTask(executorService, handler);
        buscaTodosCavalosTask.solicitaBusca(cavaloDao, todosCavalos -> {
            listaDeCavalos = todosCavalos;
            listaDePlacas = FiltraCavalo.listaDePlacas(listaDeCavalos);
            configuraLista();
        });
    }

    //----------------------------------------------------------------------------------------------
    //                                          Interface                                         ||
    //----------------------------------------------------------------------------------------------

    public interface DialogAlteraSrCallBack {
        void quandoFalhaEmAlterarSr(String txt);

        // Id do semireboque que teve seu cavalo alterado,
        // será enviado :
        // -> Inner Adapter para aplicar comportamento de remoção da Recycler
        // -> Adapter Pai para atualizar o novo cavalo que está recebendo o reboque
        void quandoSucessoEmAlterarSr(SemiReboque reboqueId);
    }

}


