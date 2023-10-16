package br.com.transporte.AppGhn.ui.fragment.certificados.dialog;

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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.DataUtil;

public class DialogCertificadoDiretoDetalhe {

    private final Context context;
    private int ano;
    public CallbackCertificadoDialog callbackDialog;
    private SwitchMaterial switchExibicao, switchData;
    private TextView data;
    private ImageView cancelBtn, setaEsquerda, setaDireita;
    private Button btnfiltrar;
    private ConstraintLayout layoutSelecionaData;
    private Animation animacima, animabaixo;
    private ConstraintLayout layoutAno;

    public void setCallbackDialog(CallbackCertificadoDialog callbackDialog) {
        this.callbackDialog = callbackDialog;
    }

    public DialogCertificadoDiretoDetalhe(Context context) {
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

    public void show() {
        final Dialog dialog = getDialog();
        inicializaCamposDaView(dialog);
        inicializaAnimacao();
        int anoAtual = configuracaoInicialDosCampos();
        configuraBtnCancelar(dialog);
        configuraSwitch_Exibicao();
        configuraSwitch_ano();
        configuraSetaEsquerda();
        configuraSetaDireita(anoAtual);
        configuraBtnFiltrar(dialog);
        configuraDialog(dialog);
    }

    private void inicializaAnimacao() {
        animacima = AnimationUtils.loadAnimation(context, R.anim.slide_in);
        animabaixo = AnimationUtils.loadAnimation(context, R.anim.slide_out);
    }

    private void configuraSwitch_Exibicao() {
        switchExibicao.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                layoutAno.setVisibility(View.INVISIBLE);
                AnimationUtil.defineAnimacao(context, R.anim.slide_out_bottom, layoutAno);
            } else {
                layoutAno.setVisibility(View.VISIBLE);
                AnimationUtil.defineAnimacao(context, R.anim.slide_in_bottom, layoutAno);
            }
        });
    }

    private void inicializaCamposDaView(@NonNull Dialog dialog) {
        switchExibicao = dialog.findViewById(R.id.switch_btn);
        switchData = dialog.findViewById(R.id.switch_btn2);
        data = dialog.findViewById(R.id.ano);
        cancelBtn = dialog.findViewById(R.id.cancelButton);
        setaEsquerda = dialog.findViewById(R.id.esquerda);
        setaDireita = dialog.findViewById(R.id.direita);
        btnfiltrar = dialog.findViewById(R.id.btn_filtrar);
        layoutAno = dialog.findViewById(R.id.dialog_layout_ano);
        layoutSelecionaData = dialog.findViewById(R.id.dialog_layout_data);
    }

    private int configuracaoInicialDosCampos() {
        switchExibicao.setChecked(false);
        String dataEmString = data.getText().toString();
        ano = Integer.parseInt(dataEmString);
        return DataUtil.capturaDataDeHojeParaConfiguracaoInicial().getYear();
    }

    private static void configuraDialog(@NonNull Dialog dialog) {
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void configuraBtnFiltrar(Dialog dialog) {
        btnfiltrar.setOnClickListener(v -> {
            if (switchExibicao.isChecked() && switchData.isChecked()) {
                callbackDialog.buscaPor_todosEAno(ano);

            } else if (switchExibicao.isChecked() && !switchData.isChecked()) {
                callbackDialog.buscaPor_todos();

            } else if (!switchExibicao.isChecked()) {
                callbackDialog.buscaPor_ativos();

            }
            dialog.dismiss();

        });
    }

    private void configuraSetaDireita(int anoAtual) {
        setaDireita.setOnClickListener(v -> {
            if (ano < anoAtual) {
                ano = ano + 1;
                data.setText(String.valueOf(ano));
            } else {
                Toast.makeText(context, "Data limite para busca", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void configuraSetaEsquerda() {
        setaEsquerda.setOnClickListener(v -> {
            ano = ano - 1;
            data.setText(String.valueOf(ano));
        });
    }

    private void configuraSwitch_ano() {
        switchData.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutSelecionaData.setVisibility(View.VISIBLE);
                layoutSelecionaData.startAnimation(animacima);
            } else {
                layoutSelecionaData.setVisibility(View.INVISIBLE);
                layoutSelecionaData.startAnimation(animabaixo);
            }
        });
    }

    private void configuraBtnCancelar(Dialog dialog) {
        cancelBtn.setOnClickListener(v -> dialog.dismiss());
    }

    @NonNull
    private Dialog getDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_layout_certificado);
        return dialog;
    }

}
