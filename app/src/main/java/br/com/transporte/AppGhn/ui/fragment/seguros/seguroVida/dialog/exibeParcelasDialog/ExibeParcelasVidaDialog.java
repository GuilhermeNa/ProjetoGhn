package br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.dialog.exibeParcelasDialog;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.dialog.editaParcelaDialog.EditaParcelaVidaDialog;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.dialog.exibeParcelasDialog.domain.EditaParcelaSeguroVidaUseCase;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.dialog.exibeParcelasDialog.domain.ParcelasVidaDataUseCase;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class ExibeParcelasVidaDialog {
    public static final String TITULO_DIALOG = "Seguro Vida";
    private HashMap<Integer, Boolean> mapComParcelas;
    private List<Parcela_seguroVida> listaDeparcelas;
    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private Button btn;
    private BottomSeguroVidaParcelaAdapter adapterParcela;
    private List<Parcela_seguroVida> dataSet = new ArrayList<>();
    private ParcelasVidaDataUseCase dataUseCase;


    public ExibeParcelasVidaDialog(
            Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
    }

    public List<Parcela_seguroVida> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

    public void showBottomDialog(DespesaComSeguroDeVida seguro) {
        getDataUseCase(seguro);
        final Dialog dialog = getDialog();
        inicializaCamposDaView(dialog);
        vinculaUi(dialog);
        configuraBtnCancel(dialog);

        configuraRecyclerDialog(seguro, dialog);
        configuraBtnPagar(seguro, dialog);
        configuraParametrosDeExibicaoDoDialog(dialog);
    }

    private void vinculaUi(@NonNull Dialog dialog) {
        final TextView campoTitulo = dialog.findViewById(R.id.titulo_parcela_seguro);
        try {
            BindData.fromString(campoTitulo, TITULO_DIALOG);
        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    private void getDataUseCase(@NonNull DespesaComSeguroDeVida seguro) {
        dataUseCase = new ParcelasVidaDataUseCase(context, lifecycleOwner);
        dataUseCase
                .getParcelasRelacionadasAEsteSeguroId(
                        seguro.getId(),
                        parcelas -> {
                            dataSet = parcelas;
                            adapterParcela.defineDataSet(dataSet);
                        });
    }

    private void configuraBtnPagar(DespesaComSeguroDeVida seguro, Dialog dialog) {
        btn.setOnClickListener(v -> {
            boolean precisaBaixar = verificaSeExisteAlgumaParcelaASerBaixada();
            if (precisaBaixar) {
                fazABaixaDaParcelaPaga(seguro, dialog);
            }

            dialog.dismiss();
            Toast.makeText(context, BAIXA_REGISTRADA_COM_SUCESSO, Toast.LENGTH_SHORT).show();

        });
    }

    private void fazABaixaDaParcelaPaga(DespesaComSeguroDeVida seguro, Dialog dialog) {
        // List<Parcela_seguroVida> listaDeParcelas = getListaDeparcelas(seguro);

        for (Map.Entry<Integer, Boolean> pair : mapComParcelas.entrySet()) {
            boolean estaParcelaPrecisaSerAlterada = pair.getValue();

            if (estaParcelaPrecisaSerAlterada) {
                Optional<Parcela_seguroVida> parcelaOptional = getDataSet().stream()
                        .filter(p -> p.getNumeroDaParcela() == pair.getKey()).findFirst();
                parcelaOptional.ifPresent(p -> {
                    p.setPaga(true);
                    final EditaParcelaSeguroVidaUseCase editaUseCase = new EditaParcelaSeguroVidaUseCase(context, lifecycleOwner);
                    editaUseCase.editaParcela(p, () -> {
                        dialog.dismiss();
                        MensagemUtil.toast(context, BAIXA_REGISTRADA_COM_SUCESSO);
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
        ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);
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

    private void configuraRecyclerDialog(DespesaComSeguroDeVida seguro, @NonNull Dialog dialog) {
        final RecyclerView recyclerDialog = dialog.findViewById(R.id.recycler_pagamento_seguro);
        adapterParcela = new BottomSeguroVidaParcelaAdapter(getDataSet(), context);
        recyclerDialog.setAdapter(adapterParcela);

        adapterParcela.setOnItemCLickListener(new BottomSeguroVidaParcelaAdapter.OnItemCLickListener() {
            @Override
            public void onBoxClick(HashMap<Integer, Boolean> map) {
                mapComParcelas = map;
                Animation animaCima = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                Animation animaBaixo = AnimationUtils.loadAnimation(context, R.anim.slide_out);

                boolean atualizacaoNecessaria = map.containsValue(true);
                configuraVisualizacaoDoBtn(btn, animaCima, animaBaixo, atualizacaoNecessaria);
            }

            @Override
            public void onItemClick(Parcela_seguroVida parcela) {
                final EditaParcelaVidaDialog dialogParcela = new EditaParcelaVidaDialog(context, parcela, lifecycleOwner);
                dialogParcela.dialogEditaParcela();

                dialogParcela.setCallback(new EditaParcelaVidaDialog.Callback() {
                    @Override
                    public void quandoFunciona() {
                        //listaDeparcelas = getListaDeparcelas(seguro);
                        //adapterParcela.atualiza(listaDeparcelas);
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
