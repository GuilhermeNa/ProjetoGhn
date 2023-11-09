package br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.dialog.editaParcelaDialog.extensions;

import static android.view.View.GONE;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.MascaraMonetariaUtil;

public class AlteraParcelaVidaExt {

    public static void run(
            @NonNull final Parcela_seguroVida parcela,
            @NonNull final EditText campoData,
            @NonNull final EditText campoValor,
            @NonNull final CheckBox checkBox
    ){
        alteraData(parcela, campoData);
        alteraValor(parcela, campoValor);
        alteraStatusDePagamento(parcela, checkBox);
    }

    private static void alteraData(
            @NonNull final Parcela_seguroVida parcela,
            @NonNull final EditText campoData
    ) {
        final String dataString = campoData.getText().toString();
        final LocalDate data = ConverteDataUtil.stringParaData(dataString);
        parcela.setData(data);
    }

    private static void alteraValor(
            @NonNull final Parcela_seguroVida parcela,
            @NonNull final EditText campoValor
    ) {
        final String valorString = campoValor.getText().toString();
        final BigDecimal valor = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorString));
        parcela.setValor(valor);
    }

    private static void alteraStatusDePagamento(
            @NonNull final Parcela_seguroVida parcela,
            @NonNull final CheckBox checkBox
    ){
        boolean novoStatusDePagamento = defineNovoStatusDePagamentoQueDeveSerDefinidoNaParcela(checkBox);
        parcela.setPaga(novoStatusDePagamento);
    }

    private static boolean defineNovoStatusDePagamentoQueDeveSerDefinidoNaParcela(@NonNull CheckBox checkBox) {
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

}
