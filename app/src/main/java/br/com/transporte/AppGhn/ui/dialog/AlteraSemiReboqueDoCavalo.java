package br.com.transporte.AppGhn.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.RequiresApi;

import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;

public class AlteraSemiReboqueDoCavalo {
    private final Context context;
    private CavaloDAO cavaloDao;
    private SemiReboque sr;
    private DialogAlteraSrCallBack dialogAlteraSrCallBack;
    private AutoCompleteTextView autoComplete;

    public AlteraSemiReboqueDoCavalo(Context context, SemiReboque sr) {
        this.context = context;
        this.sr = sr;
    }

    public void setDialogAlteraSrCallBack(DialogAlteraSrCallBack dialogAlteraSrCallBack) {
        this.dialogAlteraSrCallBack = dialogAlteraSrCallBack;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dialogAlteraSrCavalo() {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.altera_semireboque_cavalo, null);
        autoComplete = viewCriada.findViewById(R.id.dialog_alterasrcavalo_placa_cavalo_edittext);
        cavaloDao = new CavaloDAO();
        configuraAdapter();

        new AlertDialog.Builder(context)
                .setTitle(sr.getPlaca())
                .setView(viewCriada)
                .setPositiveButton("Alterar", (dialog, which) -> {
                    String placaDoCavaloAtual = cavaloDao.localizaPeloId(sr.getReferenciaCavalo()).getPlaca().toUpperCase(Locale.ROOT);
                    String placaDoCavaloDestino = autoComplete.getText().toString().toUpperCase(Locale.ROOT);

                    if (placaDoCavaloAtual.equals(placaDoCavaloDestino)) {
                        dialogAlteraSrCallBack.quandoFalhaEmAlterarSr("O Semi Reboque já está vinculado a este Cavalo.");
                    } else if (!cavaloDao.listaPlacas().contains(placaDoCavaloDestino)) {
                        dialogAlteraSrCallBack.quandoFalhaEmAlterarSr("Destino não localizado.");
                    } else {
                        Cavalo cavaloAtual = cavaloDao.retornaCavaloAtravesDaPlaca(placaDoCavaloAtual);
                        Cavalo cavaloDestino = cavaloDao.retornaCavaloAtravesDaPlaca(placaDoCavaloDestino);

                        int posicaoDoSrNaListaDoCavaloAtual = cavaloAtual.getPosicaoSr(sr);

                        cavaloAtual.removeSemiReboque(sr);
                        cavaloDestino.adicionaSemiReboque(sr);
                        sr.setReferenciaCavalo(cavaloDestino.getId());

                        dialogAlteraSrCallBack.quandoSucessoEmAlterarSr(posicaoDoSrNaListaDoCavaloAtual);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void configuraAdapter() {
        String[] placas = cavaloDao.listaPlacas().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, placas);
        autoComplete.setAdapter(adapter);
    }

    public interface DialogAlteraSrCallBack {
        void quandoFalhaEmAlterarSr(String txt);
        void quandoSucessoEmAlterarSr(int posicaoSrRemovido);
    }

}


