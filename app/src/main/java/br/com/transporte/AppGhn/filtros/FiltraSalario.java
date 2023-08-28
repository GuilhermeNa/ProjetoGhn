package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

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

        if (salarioLocalizada != null) return salarioLocalizada;
        else throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    public List<CustosDeSalario> listaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        return dao.listaTodos().stream()
                .filter(c -> DataUtil.verificaSeEstaNoRange(c.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

}
