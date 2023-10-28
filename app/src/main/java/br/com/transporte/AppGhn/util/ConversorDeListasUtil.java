package br.com.transporte.AppGhn.util;

import static br.com.transporte.AppGhn.model.enums.TipoMeses.MES_DEFAULT;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.AppGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.AppGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.AppGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.filtros.FiltraParcelaSeguroFrota;
import br.com.transporte.AppGhn.filtros.FiltraParcelaSeguroVida;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;

public class ConversorDeListasUtil {
    private List<Frete> dataSet_frete;
    private List<CustosDeAbastecimento> dataSet_abastecimento;
    private List<CustosDePercurso> dataSet_custoPercurso;
    private List<CustosDeManutencao> dataSet_custoManutencao;
    private List<DespesaAdm> dataSet_despesaAdm;
    private List<DespesasDeImposto> dataSet_despesaImposto;
    private List<DespesaCertificado> dataSet_despesaCertificado;
    private List<Parcela_seguroVida> dataSet_parcelaSeguroVida;
    private List<Parcela_seguroFrota> dataSet_parcelaSeguroFrota;



    @NonNull
    public List<Frete> extraiListaDeFrete(@NonNull List<Object> copiaDataSet) {
        List<Frete> dataSet_frete = new ArrayList<>();
        Frete frete;
        for (Object o : copiaDataSet) {
            frete = (Frete) o;
            dataSet_frete.add(frete);
        }
        return dataSet_frete;
    }

    @NonNull
    public List<CustosDeAbastecimento> extraiListaDeCustoAbastecimento(@NonNull List<Object> copiaDataSet) {
        List<CustosDeAbastecimento> dataSet_abastecimento = new ArrayList<>();
        for (Object o : copiaDataSet) {
            dataSet_abastecimento.add((CustosDeAbastecimento) o);
        }
        return dataSet_abastecimento;
    }

    @NonNull
    public List<CustosDePercurso> extraiListaDeCustoPercurso(@NonNull List<Object> copiaDataSet) {
        List<CustosDePercurso> dataSet_custoPercurso = new ArrayList<>();
        for (Object o : copiaDataSet) {
            dataSet_custoPercurso.add((CustosDePercurso) o);
        }
        return dataSet_custoPercurso;
    }

    @NonNull
    public List<CustosDeManutencao> extraiListaDeCustoManutencao(@NonNull List<Object> copiaDataSet) {
        List<CustosDeManutencao> dataSet_custoManutencao = new ArrayList<>();
        for (Object o : copiaDataSet) {
            dataSet_custoManutencao.add((CustosDeManutencao) o);
        }
        return dataSet_custoManutencao;
    }

    @NonNull
    public List<DespesaAdm> extraiListaDespesaAdm(@NonNull List<Object> copiaDataSet) {
        List<DespesaAdm> dataSet_despesaAdm = new ArrayList<>();
        for (Object o : copiaDataSet) {
            dataSet_despesaAdm.add((DespesaAdm) o);
        }
        return dataSet_despesaAdm;
    }

    @NonNull
    public List<DespesasDeImposto> extraiListaDespesaImposto(@NonNull List<Object> copiaDataSet) {
        List<DespesasDeImposto> dataSet_despesaImposto = new ArrayList<>();
        for (Object o : copiaDataSet) {
            dataSet_despesaImposto.add((DespesasDeImposto) o);
        }
        return dataSet_despesaImposto;
    }

    @NonNull
    public List<DespesaCertificado> extraiListaDespesaCertificado(@NonNull List<Object> copiaDataSet) {
        List<DespesaCertificado> dataSet_despesaCertificado = new ArrayList<>();
        for (Object o : copiaDataSet) {
            dataSet_despesaCertificado.add((DespesaCertificado) o);
        }
        return dataSet_despesaCertificado;
    }

    @NonNull
    public List<Parcela_seguroFrota> extraiListaDespesaSeguroFrota(@NonNull List<Object> copiaDataSet) {
        List<Parcela_seguroFrota> dataSet_despesaSeguroFrota = new ArrayList<>();
        for (Object o : copiaDataSet) {
            dataSet_despesaSeguroFrota.add((Parcela_seguroFrota) o);
        }
        return dataSet_despesaSeguroFrota;
    }

    @NonNull
    public List<Parcela_seguroVida> extraiListaDespesaSeguroVida(@NonNull List<Object> copiaDataSet) {
        List<Parcela_seguroVida> dataSet_despesaSeguroVida = new ArrayList<>();
        for (Object o : copiaDataSet) {
            dataSet_despesaSeguroVida.add((Parcela_seguroVida) o);
        }
        return dataSet_despesaSeguroVida;
    }

    public void extraiLucro(@NonNull List<Object> copiaDataSet, int mes) {
        dataSet_frete = new ArrayList<>();
        dataSet_abastecimento = new ArrayList<>();
        dataSet_custoPercurso = new ArrayList<>();
        dataSet_custoManutencao = new ArrayList<>();
        dataSet_despesaAdm = new ArrayList<>();
        dataSet_despesaImposto = new ArrayList<>();
        dataSet_despesaCertificado = new ArrayList<>();
        dataSet_parcelaSeguroFrota = new ArrayList<>();
        dataSet_parcelaSeguroVida = new ArrayList<>();

        for (Object o : copiaDataSet) {
            if (o instanceof Frete) dataSet_frete.add((Frete) o);
            else if (o instanceof CustosDeAbastecimento)
                dataSet_abastecimento.add((CustosDeAbastecimento) o);
            else if (o instanceof CustosDePercurso)
                dataSet_custoPercurso.add((CustosDePercurso) o);
            else if (o instanceof CustosDeManutencao)
                dataSet_custoManutencao.add((CustosDeManutencao) o);
            else if (o instanceof DespesaAdm) dataSet_despesaAdm.add((DespesaAdm) o);
            else if (o instanceof DespesasDeImposto)
                dataSet_despesaImposto.add((DespesasDeImposto) o);
            else if (o instanceof DespesaCertificado)
                dataSet_despesaCertificado.add((DespesaCertificado) o);
            else if (o instanceof Parcela_seguroFrota)
                dataSet_parcelaSeguroFrota.add((Parcela_seguroFrota) o);
            else if (o instanceof Parcela_seguroVida)
                dataSet_parcelaSeguroVida.add((Parcela_seguroVida) o);
        }

        if (mes != MES_DEFAULT.getRef()) {
            dataSet_frete = FiltraFrete.listaDoMesSolicitado(dataSet_frete, mes);
            dataSet_abastecimento = FiltraCustosAbastecimento.listaDoMesSolicitado(dataSet_abastecimento, mes);
            dataSet_custoPercurso = FiltraCustosPercurso.listaDoMesSolicitado(dataSet_custoPercurso, mes);
            dataSet_custoManutencao = FiltraCustosManutencao.listaDoMesSolicitado(dataSet_custoManutencao, mes);
            dataSet_despesaAdm = FiltraDespesasAdm.listaPorMes(dataSet_despesaAdm, mes);
            dataSet_despesaImposto = FiltraDespesasImposto.listaPorMes(dataSet_despesaImposto, mes);
            dataSet_despesaCertificado = FiltraDespesasCertificado.listaDoMesSolicitado(dataSet_despesaCertificado, mes);
            dataSet_parcelaSeguroFrota = FiltraParcelaSeguroFrota.listaDoMesSolicitado(dataSet_parcelaSeguroFrota, mes);
            dataSet_parcelaSeguroVida = FiltraParcelaSeguroVida.listaDoMesSolicitado(dataSet_parcelaSeguroVida, mes);
        }


        // callbackListas.getListafrete(dataSet_frete);
 /*       callbackListas.getListaCustosAbastecimento(dataSet_abastecimento);
        callbackListas.getListaCustosDePercurso(dataSet_custoPercurso);
        callbackListas.getListaCustosDeManutencao(dataSet_custoManutencao);
        callbackListas.getListaDespesaAdm(dataSet_despesaAdm);
        callbackListas.getListaDespesaImposto(dataSet_despesaImposto);
        callbackListas.getListaDespesaCertificado(dataSet_despesaCertificado);
        callbackListas.getListaDespesaSeguroFrota(dataSet_parcelaSeguroFrota);
        callbackListas.getListaDespesaSeguroVida(dataSet_parcelaSeguroVida);*/
    }

    public List<Frete> getDataSet_frete() {
        return dataSet_frete;
    }

    public void setDataSet_frete(List<Frete> dataSet_frete) {
        this.dataSet_frete = dataSet_frete;
    }

    public List<CustosDeAbastecimento> getDataSet_abastecimento() {
        return dataSet_abastecimento;
    }

    public void setDataSet_abastecimento(List<CustosDeAbastecimento> dataSet_abastecimento) {
        this.dataSet_abastecimento = dataSet_abastecimento;
    }

    public List<CustosDePercurso> getDataSet_custoPercurso() {
        return dataSet_custoPercurso;
    }

    public void setDataSet_custoPercurso(List<CustosDePercurso> dataSet_custoPercurso) {
        this.dataSet_custoPercurso = dataSet_custoPercurso;
    }

    public List<CustosDeManutencao> getDataSet_custoManutencao() {
        return dataSet_custoManutencao;
    }

    public void setDataSet_custoManutencao(List<CustosDeManutencao> dataSet_custoManutencao) {
        this.dataSet_custoManutencao = dataSet_custoManutencao;
    }

    public List<DespesaAdm> getDataSet_despesaAdm() {
        return dataSet_despesaAdm;
    }

    public void setDataSet_despesaAdm(List<DespesaAdm> dataSet_despesaAdm) {
        this.dataSet_despesaAdm = dataSet_despesaAdm;
    }

    public List<DespesasDeImposto> getDataSet_despesaImposto() {
        return dataSet_despesaImposto;
    }

    public void setDataSet_despesaImposto(List<DespesasDeImposto> dataSet_despesaImposto) {
        this.dataSet_despesaImposto = dataSet_despesaImposto;
    }

    public List<DespesaCertificado> getDataSet_despesaCertificado() {
        return dataSet_despesaCertificado;
    }

    public void setDataSet_despesaCertificado(List<DespesaCertificado> dataSet_despesaCertificado) {
        this.dataSet_despesaCertificado = dataSet_despesaCertificado;
    }

    public List<Parcela_seguroVida> getDataSet_parcelaSeguroVida() {
        return dataSet_parcelaSeguroVida;
    }

    public void setDataSet_parcelaSeguroVida(List<Parcela_seguroVida> dataSet_parcelaSeguroVida) {
        this.dataSet_parcelaSeguroVida = dataSet_parcelaSeguroVida;
    }

    public List<Parcela_seguroFrota> getDataSet_parcelaSeguroFrota() {
        return dataSet_parcelaSeguroFrota;
    }

    public void setDataSet_parcelaSeguroFrota(List<Parcela_seguroFrota> dataSet_parcelaSeguroFrota) {
        this.dataSet_parcelaSeguroFrota = dataSet_parcelaSeguroFrota;
    }
/*    public interface CallbackListas{
        void getListafrete(List<Frete> lista_acumulado);
        void getListaCustosAbastecimento(List<CustosDeAbastecimento> lista_acumulado);
        void getListaCustosDePercurso(List<CustosDePercurso> lista_acumulado);
        void getListaCustosDeManutencao(List<CustosDeManutencao> lista_acumulado);
        void getListaDespesaAdm(List<DespesaAdm> lista_acumulado);
        void getListaDespesaImposto(List<DespesasDeImposto> lista_acumulado);
        void getListaDespesaCertificado(List<DespesaCertificado> lista_acumulado);
        void getListaDespesaSeguroFrota(List<Parcela_seguroFrota> lista_acumulado);
        void getListaDespesaSeguroVida(List<Parcela_seguroVida> lista_acumulado);
    }*/

}
