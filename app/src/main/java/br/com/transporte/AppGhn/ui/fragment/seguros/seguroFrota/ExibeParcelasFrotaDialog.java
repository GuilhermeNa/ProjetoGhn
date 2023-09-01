package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.BAIXA_REGISTRADA_COM_SUCESSO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_ALTERADO;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomParcelaSeguroDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.ParcelaDeSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.ui.adapter.BottomSeguroParcelaAdapter;
import br.com.transporte.AppGhn.ui.fragment.seguros.dialog.EditaParcelaDialog;

public class ExibeParcelasFrotaDialog {
    private final Context context;
    private final RoomParcelaSeguroDao parcelaDao;
    private final GhnDataBase dataBase;
    private HashMap<Integer, Boolean> mapComParcelas;
    private Button btn;
    private List<ParcelaDeSeguro> listaDeparcelas;

    public ExibeParcelasFrotaDialog(Context context) {
        this.context = context;
        dataBase = GhnDataBase.getInstance(context);
        parcelaDao = dataBase.getRoomParcelaSeguroDao();
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

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

    private void configuraBtnPagar(DespesaComSeguroFrota seguro, Dialog dialog) {
        btn.setOnClickListener(v -> {
            boolean precisaBaixar = verificaSeExisteAlgumaParcelaASerBaixada();
            if(precisaBaixar){
                fazABaixaDaParcelaPaga(seguro);
            }

            dialog.dismiss();
            Toast.makeText(context, BAIXA_REGISTRADA_COM_SUCESSO, Toast.LENGTH_SHORT).show();

        });
    }

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

    private void inicializaCamposDaView(@NonNull Dialog dialog) {
        btn = dialog.findViewById(R.id.btn);
    }

    private boolean verificaSeExisteAlgumaParcelaASerBaixada() {
        return btn.getVisibility() == VISIBLE;
    }

    private void configuraBtnCancel(@NonNull Dialog dialog) {
        ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(v -> dialog.dismiss());
    }

    private void configuraPlaca(@NonNull Dialog dialog, @NonNull Cavalo cavalo) {
        TextView placaTxt = dialog.findViewById(R.id.titulo_parcela_seguro);
        placaTxt.setText(cavalo.getPlaca());
    }

    private void configuraParametrosDeExibicaoDoDialog(@NonNull Dialog dialog) {
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private Cavalo getCavalo(@NonNull DespesaComSeguroFrota seguro) {
        RoomCavaloDao cavaloDao = dataBase.getRoomCavaloDao();
        return cavaloDao.localizaPeloId(seguro.getRefCavalo());
    }

    @NonNull
    private Dialog getDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_layout_seguro);
        return dialog;
    }

    private void configuraRecyclerDialog(DespesaComSeguroFrota seguro, @NonNull Dialog dialog) {
        RecyclerView recyclerDialog = dialog.findViewById(R.id.recycler_pagamento_seguro);
        listaDeparcelas = getListaDeparcelas(seguro);

        BottomSeguroParcelaAdapter adapterParcela = new BottomSeguroParcelaAdapter(listaDeparcelas, context);
        recyclerDialog.setAdapter(adapterParcela);

        adapterParcela.setOnItemCLickListener(new BottomSeguroParcelaAdapter.OnItemCLickListener() {
            @Override
            public void onBoxClick(HashMap<Integer, Boolean> map) {
                mapComParcelas = map;
                Animation animaCima = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                Animation animaBaixo = AnimationUtils.loadAnimation(context, R.anim.slide_out);

                boolean atualizacaoNecessaria = map.containsValue(true);
                configuraVisualizacaoDoBtn(btn, animaCima, animaBaixo, atualizacaoNecessaria);
            }

            @Override
            public void onItemClick(ParcelaDeSeguro parcela) {
                EditaParcelaDialog dialogParcela = new EditaParcelaDialog(context, parcela);
                dialogParcela.dialogEditaParcela();

                dialogParcela.setCallback(new EditaParcelaDialog.Callback() {
                    @Override
                    public void quandoFunciona() {
                        listaDeparcelas = getListaDeparcelas(seguro);
                        adapterParcela.atualiza(listaDeparcelas);
                        Toast.makeText(context, REGISTRO_ALTERADO, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void quandoFalha() {
                        Toast.makeText(context, NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private List<ParcelaDeSeguro> getListaDeparcelas(@NonNull DespesaComSeguroFrota seguro) {
        return parcelaDao.listaPeloSeguroId(seguro.getId());
    }

    private void configuraVisualizacaoDoBtn(Button btn, Animation animaCima, Animation animaBaixo, boolean atualizacaoNecessaria) {
        if (atualizacaoNecessaria && btn.getVisibility() == INVISIBLE) {
            btn.setVisibility(View.VISIBLE);
            btn.startAnimation(animaCima);
        } else if (!atualizacaoNecessaria && btn.getVisibility() == VISIBLE) {
            btn.setVisibility(INVISIBLE);
            btn.startAnimation(animaBaixo);
        }
    }

}
