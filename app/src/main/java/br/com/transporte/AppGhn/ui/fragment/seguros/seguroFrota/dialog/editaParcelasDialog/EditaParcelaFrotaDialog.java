package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.editaParcelasDialog;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.AppGhn.databinding.DialogSeguroParcelaBinding;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.editaParcelasDialog.domain.EditaParcelaUseCase;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.editaParcelasDialog.extensions.AlteraParcelaFrotaExt;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class EditaParcelaFrotaDialog {
    public static final String TITULO_DIALOG = "Alterando Parcela";
    public static final String POSITIVE_BTN = "Alterar";
    public static final String NEGATIVE_BTN = "Cancelar";
    public static final String MESSAGE = "Nº da parcela : ";
    private DialogSeguroParcelaBinding binding;
    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final Parcela_seguroFrota parcela;
    private Callback callback;
    private CheckBox checkBox;
    private EditText campoData;
    private EditText campoValor;

    public EditaParcelaFrotaDialog(
            Context context,
            Parcela_seguroFrota parcela,
            LifecycleOwner lifecycleOwner
    ) {
        this.context = context;
        this.parcela = parcela;
        this.lifecycleOwner = lifecycleOwner;
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
                .setPositiveButton(POSITIVE_BTN, (dialog, which) -> {

                    alteraParcela();

                }).setNegativeButton(NEGATIVE_BTN, (dialog, which) -> callback.quandoFalha())
                .show();
    }

    private void vinculaUi() {
        try {
            BindData.fromLocalDate(binding.data, parcela.getData());
            BindData.fromBigDecimal(binding.valor, parcela.getValor());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    private void alteraParcela() {
        AlteraParcelaFrotaExt.run(
                parcela,
                campoData,
                campoValor,
                checkBox
        );
        final EditaParcelaUseCase editaUseCase =
                new EditaParcelaUseCase(context, lifecycleOwner);
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
        campoData = binding.data;
        campoValor = binding.valor;

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
