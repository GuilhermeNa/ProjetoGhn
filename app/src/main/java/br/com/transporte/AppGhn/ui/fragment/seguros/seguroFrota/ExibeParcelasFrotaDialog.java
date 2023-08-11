package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.despesas.ParcelaDeSeguro;
import br.com.transporte.AppGhn.ui.adapter.BottomSeguroParcelaAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.ui.fragment.seguros.dialog.EditaParcelaDialog;

public class ExibeParcelasFrotaDialog {
    private final Context context;
    private final ParcelaDeSeguroDAO parcelaDao = new ParcelaDeSeguroDAO();
    private HashMap<Integer, Boolean> mapComParcelas;
    private Button btn;
    private List<ParcelaDeSeguro> listaDeparcelas;

    public ExibeParcelasFrotaDialog(Context context) {
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showBottomDialog(DespesaComSeguroFrota seguro) {
        final Dialog dialog = getDialog();
        inicializaCamposDaView(dialog);
        Cavalo cavalo = getCavalo(seguro);
        configuraPlaca(dialog, cavalo);
        configuraBtnCancel(dialog);
        configuraRecyclerDialog(seguro, dialog);
        configuraBtnPagar(seguro, dialog);
        configuraParametrosDeExibicaoDoDialog(dialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraBtnPagar(DespesaComSeguroFrota seguro, Dialog dialog) {
        btn.setOnClickListener(v -> {
            boolean precisaBaixar = verificaSeExisteAlgumaParcelaASerBaixada();
            if(precisaBaixar){
                fazABaixaDaParcelaPaga(seguro);
            }

            dialog.dismiss();
            Toast.makeText(context, "Baixa registrada com sucesso", Toast.LENGTH_SHORT).show();

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fazABaixaDaParcelaPaga(DespesaComSeguroFrota seguro) {
        List<ParcelaDeSeguro> listaDeParcelas = getListaDeparcelas(seguro);

        for(Map.Entry<Integer, Boolean> pair: mapComParcelas.entrySet()){
            boolean estaParcelaPrecisaSerAlterada = pair.getValue();

            if(estaParcelaPrecisaSerAlterada){
               Optional<ParcelaDeSeguro> parcelaOptional = listaDeParcelas.stream()
                       .filter(p -> p.getNumeroDaParcela() == pair.getKey()).findFirst();
                parcelaOptional.ifPresent(p -> p.setPaga(true));
            }
        }
    }

    private void inicializaCamposDaView(Dialog dialog) {
        btn = dialog.findViewById(R.id.btn);
    }

    private boolean verificaSeExisteAlgumaParcelaASerBaixada() {
        return btn.getVisibility() == VISIBLE;
    }

    private void configuraBtnCancel(Dialog dialog) {
        ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(v -> dialog.dismiss());
    }

    private void configuraPlaca(Dialog dialog, Cavalo cavalo) {
        TextView placaTxt = dialog.findViewById(R.id.titulo_parcela_seguro);
        placaTxt.setText(cavalo.getPlaca());
    }

    private void configuraParametrosDeExibicaoDoDialog(Dialog dialog) {
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private Cavalo getCavalo(DespesaComSeguroFrota seguro) {
        CavaloDAO cavaloDao = new CavaloDAO();
        return cavaloDao.localizaPeloId(seguro.getRefCavalo());
    }

    @NonNull
    private Dialog getDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_layout_seguro);
        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraRecyclerDialog(DespesaComSeguroFrota seguro, Dialog dialog) {
        RecyclerView recyclerDialog = dialog.findViewById(R.id.recycler_pagamento_seguro);
        listaDeparcelas = getListaDeparcelas(seguro);

        BottomSeguroParcelaAdapter adapterParcela = new BottomSeguroParcelaAdapter(listaDeparcelas, context);
        recyclerDialog.setAdapter(adapterParcela);

        adapterParcela.setOnItemCLickListener(new BottomSeguroParcelaAdapter.OnItemCLickListener() {
            @Override
            public void onBoxClick(HashMap<Integer, Boolean> map) {
                mapComParcelas = map;
                Animation animacima = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                Animation animabaixo = AnimationUtils.loadAnimation(context, R.anim.slide_out);

                boolean atualizacaoNecessaria = map.containsValue(true);
                configuraVisualizacaoDoBtn(btn, animacima, animabaixo, atualizacaoNecessaria);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(ParcelaDeSeguro parcela) {
                EditaParcelaDialog dialogParcela = new EditaParcelaDialog(context, parcela);
                dialogParcela.dialogEditaParcela();

                dialogParcela.setCallback(new EditaParcelaDialog.Callback() {
                    @Override
                    public void quandoFunciona() {
                        listaDeparcelas = getListaDeparcelas(seguro);
                        adapterParcela.atualiza(listaDeparcelas);
                        Toast.makeText(context, "Registro alterado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void quandoFalha() {
                        Toast.makeText(context, "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<ParcelaDeSeguro> getListaDeparcelas(DespesaComSeguroFrota seguro) {
        return parcelaDao.listaParcelasDoSeguro(seguro.getId());
    }

    private void configuraVisualizacaoDoBtn(Button btn, Animation animacima, Animation animabaixo, boolean atualizacaoNecessaria) {
        if (atualizacaoNecessaria && btn.getVisibility() == INVISIBLE) {
            btn.setVisibility(View.VISIBLE);
            btn.startAnimation(animacima);
        } else if (!atualizacaoNecessaria && btn.getVisibility() == VISIBLE) {
            btn.setVisibility(INVISIBLE);
            btn.startAnimation(animabaixo);
        }
    }

}
