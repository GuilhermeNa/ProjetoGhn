package br.com.transporte.AppGhn.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.model.Frete;

public class CentralSalariosEComissoes {

    public static BigDecimal getComissaoEmAberto(List<Frete> lista) {
        BigDecimal total = new BigDecimal("0.0");
        for (Frete f : lista) {
            if (!f.getAdmFrete().isComissaoJaFoiPaga()) {
                total = total.add(f.getAdmFrete().getComissaoAoMotorista());
            }
        }
        return total;
    }

    //Calcula Valores Totais

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static BigDecimal getComissaoTotalDevidaAosMotoristas(List<Frete> lista) {
        BigDecimal total = new BigDecimal("0.0");
        for (Frete f : lista) {
            total = total.add(f.getAdmFrete().getComissaoAoMotorista());
        }
        return total;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static BigDecimal getComissaoTotalQueJaFoiPagaAosMotoristas(List<Frete> lista) {
        BigDecimal total = new BigDecimal("0.0");
        for (Frete f : lista) {
            if (f.getAdmFrete().isComissaoJaFoiPaga()) {
                total = total.add(f.getAdmFrete().getComissaoAoMotorista());
            }
        }
        return total;
    }


}
