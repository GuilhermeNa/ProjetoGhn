package br.com.transporte.AppGhn.ui.fragment.desempenho.helpers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;

public class DesempenhoDataSetHelper {
    private TipoDeRequisicao tipo;
    private int ano;
    private final GhnDataBase dataBase;
    private List<Object> dataSet;

    // Dao's
    private RoomCustosAbastecimentoDao custosAbastecimentoDao;
    private RoomCustosDeManutencaoDao custosDeManutencaoDao;
    private RoomParcela_seguroFrotaDao parcelaSeguroFrotaDao;
    private RoomDespesaCertificadoDao certificadoDao;
    private RoomDespesaImpostoDao despesaImpostoDao;
    private RoomCustosPercursoDao custosPercursoDao;
    private RoomParcela_seguroVidaDao parcelaSeguroVidaDao;
    private RoomDespesaAdmDao despesaAdmDao;
    private RoomFreteDao freteDao;

    public DesempenhoDataSetHelper(Context context) {
        dataBase = GhnDataBase.getInstance(context);
        dataSet = Collections.emptyList();
    }

    //----------------------------------------------------------------------------------------------
    //                                    Get Data Set                                            ||
    //----------------------------------------------------------------------------------------------

    public List<Object> getDataSet(int ano, TipoDeRequisicao tipo) {
        if (dataSet == null) dataSet = new ArrayList<>();
        if (!dataSet.isEmpty()) dataSet.clear();
        this.tipo = tipo;
        this.ano = ano;

        switch (tipo) {
            case FRETE_BRUTO:
            case FRETE_LIQUIDO:
            case COMISSAO:
                insereFretesNoDataSet();
                break;
            case CUSTOS_ABASTECIMENTO:
            case LITRAGEM:
                insereAbastecimentosNoDataSet();
                break;
            case CUSTOS_PERCURSO:
                insereCustosDePercursoNoDataSet();
                break;
            case CUSTOS_MANUTENCAO:
                insereCustosDeManutencaoNoDataSet();
                break;
            case DESPESAS_ADM:
                insereDespesasAdmNoDataSet();
                break;
            case DESPESAS_IMPOSTOS:
                insereDespesasDeImpostoNoDataSet();
                break;
            case DESPESA_CERTIFICADOS:
                insereDespesasDeCertificadoNoDataSet();
                break;
            case DESPESA_SEGURO_FROTA:
                insereDespesasComSeguroFrotaNoDataSet();
                break;
            case DESPESA_SEGURO_VIDA:
                insereDespesasComSeguroVidaNoDataSet();
                break;
            case LUCRO_LIQUIDO:
                insereFretesNoDataSet();
                insereAbastecimentosNoDataSet();
                insereCustosDePercursoNoDataSet();
                insereCustosDeManutencaoNoDataSet();
                insereDespesasAdmNoDataSet();
                insereDespesasDeImpostoNoDataSet();
                insereDespesasDeCertificadoNoDataSet();
                insereDespesasComSeguroFrotaNoDataSet();
                insereDespesasComSeguroVidaNoDataSet();
                break;
        }
        return dataSet;
    }

    private void insereDespesasComSeguroVidaNoDataSet() {
        if (parcelaSeguroVidaDao == null)
            parcelaSeguroVidaDao = dataBase.getRoomParcela_seguroVidaDao();
       // List<Parcela_seguroVida> listaAnual = FiltraParcelaSeguroVida.listaPorAno(parcelaSeguroVidaDao.todos(), ano);
       // listaAnual = FiltraParcelaSeguroVida.listaPorStatusDePagamento(listaAnual, true);
       // dataSet = new ArrayList<>(listaAnual);
    }

    private void insereDespesasComSeguroFrotaNoDataSet() {
        if (parcelaSeguroFrotaDao == null)
            parcelaSeguroFrotaDao = dataBase.getRoomParcela_seguroFrotaDao();
      //  List<Parcela_seguroFrota> listaAnual = FiltraParcelaSeguroFrota.listaPorAno(parcelaSeguroFrotaDao.todos(), ano);
      //  listaAnual = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaAnual, true);
       // dataSet = new ArrayList<>(listaAnual);
    }

    private void insereDespesasDeCertificadoNoDataSet() {
      /*  if (certificadoDao == null) certificadoDao = dataBase.getRoomDespesaCertificadoDao();
        List<DespesaCertificado> listaAnual = FiltraDespesasCertificado.listaPorAno(certificadoDao.todos(), ano);
        dataSet = new ArrayList<>(listaAnual);*/
    }

    private void insereDespesasDeImpostoNoDataSet() {
        if (despesaImpostoDao == null) despesaImpostoDao = dataBase.getRoomDespesaImpostoDao();
       // List<DespesasDeImposto> listaAnual = FiltraDespesasImposto.listaPorAno(despesaImpostoDao.todos(), ano);
       // dataSet = new ArrayList<>(listaAnual);
    }

    private void insereDespesasAdmNoDataSet() {
        if (despesaAdmDao == null) despesaAdmDao = dataBase.getRoomDespesaAdmDao();
        //List<DespesaAdm> listaAnual = FiltraDespesasAdm.listaPorAno(despesaAdmDao.buscaTodos(), ano);
      //  dataSet = new ArrayList<>(listaAnual);
    }

    private void insereCustosDeManutencaoNoDataSet() {
        if (custosDeManutencaoDao == null)
            custosDeManutencaoDao = dataBase.getRoomCustosDeManutencaoDao();
      //  List<CustosDeManutencao> listaAnual = FiltraCustosManutencao.listaPorAno(custosDeManutencaoDao.todos(), ano);
       // dataSet = new ArrayList<>(listaAnual);
    }

    private void insereCustosDePercursoNoDataSet() {
        if (custosPercursoDao == null) custosPercursoDao = dataBase.getRoomCustosPercursoDao();
    //    List<CustosDePercurso> listaAnual = FiltraCustosPercurso.listaPorAno(custosPercursoDao.todos(), ano);
    //    dataSet = new ArrayList<>(listaAnual);
    }

    private void insereAbastecimentosNoDataSet() {
        if (custosAbastecimentoDao == null)
            custosAbastecimentoDao = dataBase.getRoomCustosAbastecimentoDao();
   //     List<CustosDeAbastecimento> listaAnual = FiltraCustosAbastecimento.listaPorAno(custosAbastecimentoDao.todos(), ano);
   //     dataSet = new ArrayList<>(listaAnual);
    }

    private void insereFretesNoDataSet() {
        if (freteDao == null) freteDao = dataBase.getRoomFreteDao();
   //     List<Frete> listaAnual = FiltraFrete.listaPorAno(freteDao.todos(), ano);
   //     dataSet = new ArrayList<>(listaAnual);
    }


}
