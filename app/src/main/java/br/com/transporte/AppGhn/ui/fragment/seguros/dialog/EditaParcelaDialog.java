package br.com.transporte.AppGhn.ui.fragment.seguros.dialog;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.databinding.DialogSeguroParcelaBinding;
import br.com.transporte.AppGhn.model.ParcelaDeSeguro;
import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class EditaParcelaDialog {
    public static final String TITULO_DIALOG = "Alterando Parcela";
    public static final String POSITIVE_BTN = "Alterar";
    public static final String NEGATIVE_BTN = "Cancelar";
    public static final String MESSAGE = "Nº da parcela : ";
    private DialogSeguroParcelaBinding binding;
    private final Context context;
    private final ParcelaDeSeguro parcela;
    private Callback callback;
    private ParcelaDeSeguroDAO parcelaDao;

    public EditaParcelaDialog(Context context, ParcelaDeSeguro parcela) {
        this.context = context;
        this.parcela = parcela;
        Log.d("teste", "editando parcela -> " + parcela.getNumeroDaParcela());
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dialogEditaParcela() {
        binding = DialogSeguroParcelaBinding.inflate(LayoutInflater.from(context));
        EditText dataEdit = binding.data;
        EditText valorEdit = binding.valor;
        CheckBox checkBox = binding.checkbox;
        TextView boxTxt = binding.boxTxt;
        parcelaDao = new ParcelaDeSeguroDAO();

        configuraUiCheckBox(checkBox, boxTxt);
        aplicaMascarasAosEditTexts(dataEdit, valorEdit);
        vinculaObjetoNaView(dataEdit, valorEdit);

        new AlertDialog.Builder(context)
                .setTitle(TITULO_DIALOG)
                .setMessage(MESSAGE + parcela.getNumeroDaParcela())
                .setView(binding.getRoot())
                .setPositiveButton(POSITIVE_BTN, (dialog, which) -> {
                    alteraParcela(dataEdit, valorEdit, checkBox);
                    callback.quandoFunciona();

                }).setNegativeButton(NEGATIVE_BTN, (dialog, which) -> callback.quandoFalha())
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void alteraParcela(EditText dataEdit, EditText valorEdit, CheckBox checkBox) {
        String campoData = dataEdit.getText().toString();
        LocalDate data = FormataDataUtil.stringParaData(campoData);
        parcela.setData(data);

        String campoValor = valorEdit.getText().toString();
        BigDecimal valor = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoValor));
        parcela.setValor(valor);

        boolean novoStatusDePagamento = defineNovoStatusDePagamento(checkBox);
        parcela.setPaga(novoStatusDePagamento);

        parcelaDao.edita(parcela);
    }

    private boolean defineNovoStatusDePagamento(CheckBox checkBox) {
        boolean parcelaFoiPaga = checkBox.getVisibility() == View.VISIBLE;
        boolean boxDesfazerPagamentoMarcado = checkBox.isChecked();
        boolean removeStatusDePagamentoRelizado = false;

        if(parcelaFoiPaga && boxDesfazerPagamentoMarcado){
            return removeStatusDePagamentoRelizado;
        } else if (checkBox.getVisibility() == View.VISIBLE && !checkBox.isChecked()){
            return !removeStatusDePagamentoRelizado;
        } else if(checkBox.getVisibility() == GONE){
            return removeStatusDePagamentoRelizado;
        }

        return false;
    }

    private void configuraUiCheckBox(CheckBox checkBox, TextView boxTxt) {
        if(!parcela.isPaga()){
            checkBox.setVisibility(GONE);
            boxTxt.setVisibility(GONE);
        }
    }

    private void aplicaMascarasAosEditTexts(EditText dataEdit, EditText valorEdit) {
        MascaraDataUtil.MascaraData(dataEdit);
        valorEdit.addTextChangedListener(new MascaraMonetariaUtil(valorEdit));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vinculaObjetoNaView(EditText dataEdit, EditText valorEdit) {
        dataEdit.setText(FormataDataUtil.dataParaString(parcela.getData()));
        valorEdit.setText(FormataNumerosUtil.formataNumero(parcela.getValor()));
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
