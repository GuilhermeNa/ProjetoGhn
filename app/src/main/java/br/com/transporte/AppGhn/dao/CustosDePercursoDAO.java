package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class CustosDePercursoDAO {
    private final static ArrayList<CustosDePercurso> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------


 /*   public void adiciona(CustosDePercurso custo) {
        custo.setId(contadorDeIds);
        dao.add(custo);
        contadorDeIds++;
    }

    public void edita(CustosDePercurso custo) {
        CustosDePercurso custoEncontrado = localizaPeloId(custo.getId());
        if (custoEncontrado != null) {
            int posicaoDespesa = dao.indexOf(custoEncontrado);
            dao.set(posicaoDespesa, custo);
        }
    }

    public void deleta(int despesaId) {
        CustosDePercurso despesaEncontrada = localizaPeloId(despesaId);
        if (despesaEncontrada != null) {
            dao.remove(despesaEncontrada);
        }
    }*/


    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<CustosDePercurso> listaTodos() {
        return new ArrayList<>(dao);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CustosDePercurso> listaFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        ArrayList<CustosDePercurso> lista = new ArrayList<>();
        for (CustosDePercurso d : dao) {
            if (!d.getData().isBefore(dataInicial) && !d.getData().isAfter(dataFinal)) {
                lista.add(d);
            }
        }
        return lista;
    }

    public List<CustosDePercurso> listaPorCavalo(int cavaloId) {
        List<CustosDePercurso> lista = new ArrayList<>();
        for (CustosDePercurso c : dao) {
            if (c.getRefCavalo() == cavaloId) {
                lista.add(c);
            }
        }
        return lista;
    }
// esse cara VVVVVV
    public List<CustosDePercurso> listaPorCavaloEAberto(int cavaloId) {
        List<CustosDePercurso> listaPorPlaca = listaPorCavalo(cavaloId);
        List<CustosDePercurso> listaEmAberto = new ArrayList<>();

        for (CustosDePercurso c : listaPorPlaca) {
            if (c.getTipo() == TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO) {
                listaEmAberto.add(c);
            }
        }
        return listaEmAberto;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CustosDePercurso> listaFiltradaPorPlacaEData(int cavaloId, LocalDate dataInicial, LocalDate dataFinal) {
        List<CustosDePercurso> listaPorPlaca = listaPorCavalo(cavaloId);
        List<CustosDePercurso> lista = new ArrayList<>();
        for (CustosDePercurso c: listaPorPlaca) {
            if (!c.getData().isBefore(dataInicial) && !c.getData().isAfter(dataFinal)) {
                lista.add(c);
            }
        }
        return lista;
    }


    //---------------------------------- Outros Metodos ---------------------------------------------


    public CustosDePercurso localizaPeloId(int idDespesa) {
        CustosDePercurso despesaLocalizada = null;
        for (CustosDePercurso d : dao) {
            if (d.getId() == idDespesa) {
                despesaLocalizada = d;
            }
        }
        return despesaLocalizada;
    }

    //ta convertendo
    public String calculaDespesaTotal(List<CustosDePercurso> lista) {
        BigDecimal total = new BigDecimal("0.0");
        for (CustosDePercurso d : lista) {
            total = total.add(d.getValorCusto());
        }
        return FormataNumerosUtil.formataMoedaPadraoBr(total);
    }

    public String calculaDespesaNaoReembolsavel(List<CustosDePercurso> lista) {
        BigDecimal total = new BigDecimal("0.0");
        for (CustosDePercurso d : lista) {
            if (d.getTipo() == TipoCustoDePercurso.NAO_REEMBOLSAVEL) {
                total = total.add(d.getValorCusto());
            }
        }
        return FormataNumerosUtil.formataMoedaPadraoBr(total);
    }

    public BigDecimal calculaDespesaReembolsavel(List<CustosDePercurso> lista) {
        BigDecimal total = new BigDecimal("0.0");
        for (CustosDePercurso d : lista) {
            if (d.getTipo() == TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO) {
                total = total.add(d.getValorCusto());
            }
        }
        return total;
    }

}
