package br.com.transporte.AppGhn.filtros;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.SalarioDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraSalario {
    private static final SalarioDAO dao = new SalarioDAO();

    @NonNull
    public static CustosDeSalario localizaPeloId(int salarioId) throws ObjetoNaoEncontrado {
        CustosDeSalario salarioLocalizada = null;

        for (CustosDeSalario s : dao.listaTodos()) {
            if (s.getId() == salarioId) {
                salarioLocalizada = s;
            }
        }

        if (salarioLocalizada != null) {
            return salarioLocalizada;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CustosDeSalario> listaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        return dao.listaTodos().stream()
                .filter(c -> DataUtil.capturaRange(c.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }


}
