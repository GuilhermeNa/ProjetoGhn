package br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.dialog.editaParcelaDialog;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.AppGhn.databinding.DialogSeguroParcelaBinding;
import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.dialog.editaParcelaDialog.domain.EditaParcelaVidaUseCase;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.dialog.editaParcelaDialog.extensions.AlteraParcelaVidaExt;
import br.com.transporte.appGhn.util.MascaraDataUtil;
import br.com.transporte.appGhn.util.MascaraMonetariaUtil;

public class EditaParcelaVidaDialog {
    public static final String TITULO_DIALOG = "Alterando Parcela";
    public static final String POSITIVE_BTN = "Alterar";
    public static final String NEGATIVE_BTN = "Cancelar";
    public static final String MESSAGE = "Nº da parcela : ";
    private DialogSeguroParcelaBinding binding;
    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final Parcela_seguroVida parcela;
    private Callback callback;
    private CheckBox checkBox;
    private EditText campoData;
    private EditText campoValor;

    public EditaParcelaVidaDialog(Context context, Parcela_seguroVida parcela, LifecycleOwner lifeCycleOwner) {
        this.context = context;
        this.parcela = parcela;
        this.lifecycleOwner = lifeCycleOwner;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

    public void dialogEditaParcela() {
        binding = DialogSeguroParcelaBinding.inflate(LayoutInflater.from(context));
        vinculaUi();
        configuraUiCheckBox();
        aplicaMascarasAosEditTexts();
        exibeDialog();
    }

    private void exibeDialog() {
        new AlertDialog.Builder(context)
                .setTitle(TITULO_DIALOG)
                .setMessage(MESSAGE + parcela.getNumeroDaParcela())
                .setView(binding.getRoot())
                .setPositiveButton(POSITIVE_BTN, (dialog, which) -> alteraParcela())
                .setNegativeButton(NEGATIVE_BTN, (dialog, which) -> callback.quandoFalha())
                .show();
    }

    private void vinculaUi() {
        campoData = binding.data;
        campoValor = binding.valor;
        try {
            BindData.fromLocalDate(campoData, parcela.getData());
            BindData.fromBigDecimal(campoValor, parcela.getValor());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }

    }

    private void alteraParcela() {
        AlteraParcelaVidaExt.run(
                parcela,
                campoData,
                campoValor,
                checkBox
        );
        final EditaParcelaVidaUseCase editaUseCase =
                new EditaParcelaVidaUseCase(context, lifecycleOwner);
        editaUseCase.editaParcela(parcela,
                () -> callback.quandoFunciona());

    }

    private void configuraUiCheckBox() {
        checkBox = binding.checkbox;
        if (!parcela.isPaga()) {
            checkBox.setVisibility(GONE);
            binding.boxTxt.setVisibility(GONE);
        }
    }

    private void aplicaMascarasAosEditTexts() {
        MascaraDataUtil.MascaraData(campoData);
        campoValor.addTextChangedListener(new MascaraMonetariaUtil(campoValor));
    }

    //-------------------------------------- Metodos Publicos --------------------------------------

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Interface                                         ||
    //----------------------------------------------------------------------------------------------

    public interface Callback {
        void quandoFunciona();

        void quandoFalha();
    }

}
