package br.com.transporte.appGhn.ui.activity.despesaAdm.extensions;

import android.widget.TextView;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;

public class BindData {

    public static void fromLocalDate(
            @NonNull final TextView dataView,
            final LocalDate data
    ) throws ObjetoNaoEncontrado {
        if (data != null) {
            final String dataEmString = ConverteDataUtil.dataParaString(data);
            dataView.setText(dataEmString);
        } else {
            throw new ObjetoNaoEncontrado("LocalDate recebido é nulo");
        }
    }

    public static void R$fromBigDecimal(
            @NonNull final TextView dataView,
            final BigDecimal data
    ) throws ObjetoNaoEncontrado {
        if (data != null) {
            final String bigDecimalEmString = FormataNumerosUtil.formataMoedaPadraoBr(data);
            dataView.setText(bigDecimalEmString);
        } else {
            throw new ObjetoNaoEncontrado("BigDecimal recebido é nulo");
        }
    }

    public static void fromBigDecimal(
            @NonNull final TextView dataView,
            final BigDecimal data
    ) throws ObjetoNaoEncontrado {
        if (data != null) {
            final String bigDecimalEmString = FormataNumerosUtil.formataNumero(data);
            dataView.setText(bigDecimalEmString);
        } else {
            throw new ObjetoNaoEncontrado("BigDecimal recebido é nulo");
        }
    }

    public static void fromInteger(
            @NonNull final TextView dataView,
            final int data
    ) throws ObjetoNaoEncontrado {
        if (data > -1) {
            final String intEmString = String.valueOf(data);
            dataView.setText(intEmString);
        } else {
            throw new ObjetoNaoEncontrado("int recebido é nulo");
        }
    }

    public static void fromString(
            final TextView dataView,
            final String string
    ) throws ObjetoNaoEncontrado {
        if (string != null) {
            dataView.setText(string);
        } else {
            throw new ObjetoNaoEncontrado("int recebido é nulo");
        }
    }



}
