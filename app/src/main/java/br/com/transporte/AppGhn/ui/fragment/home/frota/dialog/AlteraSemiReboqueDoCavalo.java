package br.com.transporte.AppGhn.ui.fragment.home.frota.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;

public class AlteraSemiReboqueDoCavalo {
    private final Context context;
    private final SemiReboque reboque;
    private GhnDataBase dataBase;
    private DialogAlteraSrCallBack dialogAlteraSrCallBack;
    private AutoCompleteTextView autoComplete;
    private List<String> listaDePlacas;
    private RoomCavaloDao cavaloDao;

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
                    String placaDoCavaloAtual = cavaloDao.localizaPeloId(reboque.getRefCavaloId()).getPlaca().toUpperCase(Locale.ROOT);
                    String placaDoCavaloDestino = autoComplete.getText().toString().toUpperCase(Locale.ROOT);

                    if (placaDoCavaloAtual.equals(placaDoCavaloDestino)) {
                        dialogAlteraSrCallBack.quandoFalhaEmAlterarSr("O Semi Reboque já está vinculado a este Cavalo.");
                    } else if (!listaDePlacas.contains(placaDoCavaloDestino)) {
                        dialogAlteraSrCallBack.quandoFalhaEmAlterarSr("Destino não localizado.");
                    } else {
                        Cavalo cavaloAtual = cavaloDao.localizaPelaPlaca(placaDoCavaloAtual);
                        Cavalo cavaloDestino = cavaloDao.localizaPelaPlaca(placaDoCavaloDestino);

                        reboque.setRefCavaloId(cavaloDestino.getId());
                        reboqueDao.adiciona(reboque);
                        dialogAlteraSrCallBack.quandoSucessoEmAlterarSr(reboque);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    //-------------------------------------------
    // -> Configura Adapter                    ||
    //-------------------------------------------

    private void configuraAdapter() {
        listaDePlacas = getListaDePlacas();
        String[] arrayDePlacas = listaDePlacas.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayDePlacas);
        autoComplete.setAdapter(adapter);
    }

    private List<String> getListaDePlacas() {
        List<Cavalo> listaDeCavalos = cavaloDao.todos();
        return listaDePlacas = FiltraCavalo.listaDePlacas(listaDeCavalos);
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


