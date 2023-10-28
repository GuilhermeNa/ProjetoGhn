package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.exibeParcelasDialog;

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
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.AppGhn.ui.adapter.BottomSeguroParcelaAdapter;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.editaParcelasDialog.EditaParcelaFrotaDialog;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.editaParcelasDialog.domain.EditaParcelaUseCase;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.exibeParcelasDialog.domain.PacelasFrotaDataUseCase;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.domain.model.DespesaSeguroFrotaObject;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class ExibeParcelasFrotaDialog {
    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private HashMap<Integer, Boolean> mapComParcelas;
    private Button btn;
    private List<Parcela_seguroFrota> dataSet = new ArrayList<>();
    private BottomSeguroParcelaAdapter adapterParcela;
    private PacelasFrotaDataUseCase dataUseCase;

    public ExibeParcelasFrotaDialog(Context context, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
    }

    private void getDataSetUseCase(@NonNull DespesaSeguroFrotaObject seguroObject) {
        dataUseCase = new PacelasFrotaDataUseCase(context, lifecycleOwner);
        dataUseCase
                .getParcelasRelacionadasAEsteSeguroId(
                        seguroObject.getIdSeguro(),
                        parcelas -> {
                            dataSet = parcelas;
                            adapterParcela.defineDataSet(dataSet);
                        });
    }

    @NonNull
    @Contract(" -> new")
    private List<Parcela_seguroFrota> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

    public void showBottomDialog(DespesaSeguroFrotaObject seguroObject) {
        getDataSetUseCase(seguroObject);
        final Dialog dialog = getDialog();
        inicializaCamposDaView(dialog);
        vinculaUi(seguroObject, dialog);
        configuraBtnCancel(dialog);
        configuraRecyclerDialog(seguroObject, dialog);
        configuraBtnPagar(seguroObject, dialog);
        configuraParametrosDeExibicaoDoDialog(dialog);
    }

    private void configuraBtnPagar(DespesaSeguroFrotaObject seguro, Dialog dialog) {
        btn.setOnClickListener(v -> {
            boolean precisaBaixar = verificaSeExisteAlgumaParcelaASerBaixada();
            if (precisaBaixar) {
                fazABaixaDaParcelaPaga(seguro, dialog);
            }
        });
    }

    private void vinculaUi(
            @NonNull DespesaSeguroFrotaObject seguroObject,
            @NonNull Dialog dialog
    ) {
        final TextView campoPlaca = dialog.findViewById(R.id.titulo_parcela_seguro);
        try {
            BindData.fromString(campoPlaca, seguroObject.getPlaca());
        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    private void fazABaixaDaParcelaPaga(@NonNull DespesaSeguroFrotaObject seguro, Dialog dialog) {
        dataUseCase.atualizaDataSet(seguro.getIdSeguro());

        for (Map.Entry<Integer, Boolean> pair : mapComParcelas.entrySet()) {
            boolean estaParcelaPrecisaSerAlterada = pair.getValue();

            if (estaParcelaPrecisaSerAlterada) {
                Optional<Parcela_seguroFrota> parcelaOptional = getDataSet().stream()
                        .filter(p -> p.getNumeroDaParcela() == pair.getKey()).findFirst();
                parcelaOptional.ifPresent(p -> {
                            p.setPaga(true);
                            final EditaParcelaUseCase editaUseCase = new EditaParcelaUseCase(context, lifecycleOwner);
                            editaUseCase.editaParcela(p, () -> {
                                dialog.dismiss();
                                Toast.makeText(context, BAIXA_REGISTRADA_COM_SUCESSO, Toast.LENGTH_SHORT).show();
                            });
                        });
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
        final ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(v -> dialog.dismiss());
    }

    private void configuraParametrosDeExibicaoDoDialog(@NonNull Dialog dialog) {
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @NonNull
    private Dialog getDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_layout_seguro);
        return dialog;
    }

    private void configuraRecyclerDialog(DespesaSeguroFrotaObject seguroObject, @NonNull Dialog dialog) {
        final RecyclerView recyclerDialog = dialog.findViewById(R.id.recycler_pagamento_seguro);
        adapterParcela = new BottomSeguroParcelaAdapter(context, getDataSet());
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
            public void onItemClick(Parcela_seguroFrota parcela) {
                final EditaParcelaFrotaDialog dialogParcela = new EditaParcelaFrotaDialog(context, parcela, lifecycleOwner);
                dialogParcela.dialogEditaParcela();

                dialogParcela.setCallback(new EditaParcelaFrotaDialog.Callback() {
                    @Override
                    public void quandoFunciona() {
                        MensagemUtil.toast(context, REGISTRO_ALTERADO);
                    }

                    @Override
                    public void quandoFalha() {
                        MensagemUtil.toast(context, NENHUMA_ALTERACAO_REALIZADA);
                    }
                });
            }
        });
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
