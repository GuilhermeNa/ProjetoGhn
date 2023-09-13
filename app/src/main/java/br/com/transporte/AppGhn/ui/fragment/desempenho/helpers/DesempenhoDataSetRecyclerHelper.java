package br.com.transporte.AppGhn.ui.fragment.desempenho.helpers;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoMeses.MES_DEFAULT;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.dao.ObjetoTemporario_representaCavaloDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.AppGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.AppGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.AppGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.AppGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.filtros.FiltraParcelaSeguroFrota;
import br.com.transporte.AppGhn.filtros.FiltraParcelaSeguroVida;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.model.temporarios.ObjetoTemporario_representaCavalo;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConversorDeListasUtil;

public class DesempenhoDataSetRecyclerHelper {
    public static final long SEM_REF_CAVALO = 0;
    public static final String MOTORISTA_INDEFINIDO = "Motorista indefinido";
    private final List<Object> copiaDataSet_object;
    private final List<Cavalo> dataSet_cavalo;
    private final RoomMotoristaDao motoristaDao;
    private static ObjetoTemporario_representaCavaloDAO objTemporarioDao;
    private static int cavaloSize;
    private final ConversorDeListasUtil conversorDeListas = new ConversorDeListasUtil();

    private static RoomFreteDao freteDao;
    private static RoomCustosAbastecimentoDao abastecimentoDao;
    private static RoomCustosPercursoDao custosPercursoDao;
    private static RoomCustosDeSalarioDao custosDeSalarioDao;
    private static RoomCustosDeManutencaoDao manutencaoDao;
    private static RoomDespesaCertificadoDao certificadoDao;
    private static RoomDespesaAdmDao admDao;
    private static RoomParcela_seguroFrotaDao parcelaFrotaDao;
    private static RoomParcela_seguroVidaDao parcelaVidaDao;
    private static RoomDespesaImpostoDao impostoDao;


    public DesempenhoDataSetRecyclerHelper(List<Object> copiaDataSet, @NonNull List<Cavalo> dataSet_cavalo, GhnDataBase database) {
        this.copiaDataSet_object = copiaDataSet;
        this.dataSet_cavalo = dataSet_cavalo;
        this.motoristaDao = database.getRoomMotoristaDao();
        freteDao = database.getRoomFreteDao();
        abastecimentoDao = database.getRoomCustosAbastecimentoDao();
        custosPercursoDao = database.getRoomCustosPercursoDao();
        custosDeSalarioDao = database.getRoomCustosDeSalarioDao();
        manutencaoDao = database.getRoomCustosDeManutencaoDao();
        certificadoDao = database.getRoomDespesaCertificadoDao();
        admDao = database.getRoomDespesaAdmDao();
        parcelaFrotaDao = database.getRoomParcela_seguroFrotaDao();
        parcelaVidaDao = database.getRoomParcela_seguroVidaDao();
        impostoDao = database.getRoomDespesaImpostoDao();

    }

    public List<ObjetoTemporario_representaCavalo> getDataSet() {
        return new ArrayList<>(objTemporarioDao.listaTodos());
    }

    public void atualizaCopiaDataSet(List<Object> novoDataSet) {
        this.copiaDataSet_object.clear();
        this.copiaDataSet_object.addAll(novoDataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Configura                                         ||
    //----------------------------------------------------------------------------------------------

    public void atualizaDataSet(@NonNull TipoDeRequisicao tipoRequisicao, int mes, boolean switchIsChecked, int ano) {
        configura();
        List<Frete> listaFretes;
        switch (tipoRequisicao) {
            case FRETE_BRUTO:
                listaFretes = conversorDeListas.extraiListaDeFrete(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef())

                    ConfiguraObjetosQuandoBuscaAnual.freteBruto(listaFretes);
                else
                    ConfiguraObjetosQuandoBuscaMensal.freteBruto(listaFretes, mes);
                break;

            case FRETE_LIQUIDO:
                listaFretes = conversorDeListas.extraiListaDeFrete(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.freteLiquido(listaFretes);
                else
                    ConfiguraObjetosQuandoBuscaMensal.freteLiquido(listaFretes, mes);
                break;

            case LUCRO_LIQUIDO:
                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.lucro(ano);
                    ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.lucro(mes);
                } else {
                    ConfiguraObjetosQuandoBuscaMensal.lucro(ano, mes);
                    ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.lucro(mes);
                }
                break;

            case CUSTOS_PERCURSO:
                List<CustosDePercurso> listaCustosPercurso = conversorDeListas.extraiListaDeCustoPercurso(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.custosDePercurso(listaCustosPercurso);
                else
                    ConfiguraObjetosQuandoBuscaMensal.custosDePercurso(listaCustosPercurso, mes);
                break;

            case COMISSAO:
                listaFretes = conversorDeListas.extraiListaDeFrete(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.comissao(listaFretes);
                else
                    ConfiguraObjetosQuandoBuscaMensal.comissao(listaFretes, mes);
                break;

            case CUSTOS_MANUTENCAO:
                List<CustosDeManutencao> listaManutencao = conversorDeListas.extraiListaDeCustoManutencao(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.custosDeManutencao(listaManutencao);
                else
                    ConfiguraObjetosQuandoBuscaMensal.custosDeManutencao(listaManutencao, mes);
                break;

            case CUSTOS_ABASTECIMENTO:
                List<CustosDeAbastecimento> listaAbastecimento = conversorDeListas.extraiListaDeCustoAbastecimento(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.custosDeAbastecimento(listaAbastecimento);
                else
                    ConfiguraObjetosQuandoBuscaMensal.custosDeAbastecimento(listaAbastecimento, mes);
                break;

            case DESPESAS_ADM:
                List<DespesaAdm> listaDespesaAdm = conversorDeListas.extraiListaDespesaAdm(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.despesasAdm(listaDespesaAdm);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.despesasAdm(listaDespesaAdm);
                } else {
                    ConfiguraObjetosQuandoBuscaMensal.despesasAdm(listaDespesaAdm, mes);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.despesasAdm(listaDespesaAdm, mes);
                }
                break;

            case DESPESA_CERTIFICADOS:
                List<DespesaCertificado> listaDespesaCertificado = conversorDeListas.extraiListaDespesaCertificado(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.despesasCertificado(listaDespesaCertificado);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.despesasCertificado(listaDespesaCertificado);
                } else {
                    ConfiguraObjetosQuandoBuscaMensal.despesasCertificado(listaDespesaCertificado, mes);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.despesasCertificado(listaDespesaCertificado, mes);
                }
                break;

            case DESPESA_SEGURO_FROTA:
                List<Parcela_seguroFrota> listaParcelasFrota = conversorDeListas.extraiListaDespesaSeguroFrota(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.despesasSeguroFrota(listaParcelasFrota);
                else
                    ConfiguraObjetosQuandoBuscaMensal.despesasSeguroFrota(listaParcelasFrota, mes);
                break;

            case DESPESA_SEGURO_VIDA:
                List<Parcela_seguroVida> listaParcelasVida = conversorDeListas.extraiListaDespesaSeguroVida(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.despesasSeguroVida(listaParcelasVida);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.despesasSeguroVida(listaParcelasVida);
                } else {
                    ConfiguraObjetosQuandoBuscaMensal.despesasSeguroVida(listaParcelasVida, mes);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.despesasSeguroVida(listaParcelasVida, mes);
                }
                break;

            case DESPESAS_IMPOSTOS:
                List<DespesasDeImposto> listaDespesaImposto = conversorDeListas.extraiListaDespesaImposto(copiaDataSet_object);
                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.despesasImposto(listaDespesaImposto);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.despesasImposto(listaDespesaImposto);
                } else {
                    ConfiguraObjetosQuandoBuscaMensal.despesasImposto(listaDespesaImposto, mes);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.despesasImposto(listaDespesaImposto, mes);
                }
        }
        for (ObjetoTemporario_representaCavalo d : objTemporarioDao.listaTodos()) {
            d.definePercentual();
        }
    }

    private void configura() {
        if (objTemporarioDao != null) objTemporarioDao.clear();

        objTemporarioDao = new ObjetoTemporario_representaCavaloDAO();
        cavaloSize = dataSet_cavalo.size();
        criaNovosObjTemp();
    }

    private void criaNovosObjTemp() {
        String nomeMotorista;
        for (Cavalo c : dataSet_cavalo) {
            try {
                nomeMotorista = motoristaDao.localizaPeloId(c.getRefMotoristaId()).getNome();
            } catch (NullPointerException e) {
                nomeMotorista = MOTORISTA_INDEFINIDO;
            }
            ObjetoTemporario_representaCavalo obj = new ObjetoTemporario_representaCavalo(
                    nomeMotorista,
                    c.getPlaca(),
                    c.getId()
            );
            objTemporarioDao.adiciona(obj);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          Busca Anual                                       ||
    //----------------------------------------------------------------------------------------------

    static class ConfiguraObjetosQuandoBuscaAnual {
        private static void freteBruto(List<Frete> listaFretes) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaFretes, obj.getId());
                valor = CalculoUtil.somaFreteBruto(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void freteLiquido(List<Frete> listaFretes) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaFretes, obj.getId());
                valor = CalculoUtil.somaFreteLiquido(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDePercurso(List<CustosDePercurso> listaCustosPercurso) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<CustosDePercurso> dataSet = FiltraCustosPercurso.listaPorCavaloId(listaCustosPercurso, obj.getId());
                valor = CalculoUtil.somaCustosDePercurso(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void comissao(List<Frete> listaFrete) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaFrete, obj.getId());
                valor = CalculoUtil.somaComissao(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDeManutencao(List<CustosDeManutencao> listaManutencao) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<CustosDeManutencao> dataSet = FiltraCustosManutencao.listaPorCavaloId(listaManutencao, obj.getId());
                valor = CalculoUtil.somaCustosDeManutencao(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDeAbastecimento(List<CustosDeAbastecimento> listaAbastecimento) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<CustosDeAbastecimento> dataSet = FiltraCustosAbastecimento.listaPorCavaloId(listaAbastecimento, obj.getId());
                valor = CalculoUtil.somaCustosDeAbastecimento(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasAdm(List<DespesaAdm> listaDespesaAdm) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaAdm> dataSet = FiltraDespesasAdm.listaPorCavaloId(listaDespesaAdm, obj.getId());
                valor = CalculoUtil.somaDespesasAdm(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasCertificado(List<DespesaCertificado> listaDespesaCertificado) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaCertificado> dataSet = FiltraDespesasCertificado.listaPorCavaloId(listaDespesaCertificado, obj.getId());
                valor = CalculoUtil.somaDespesasCertificado(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasSeguroFrota(List<Parcela_seguroFrota> listaParcelasFrota) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Parcela_seguroFrota> dataSet = FiltraParcelaSeguroFrota.listaPeloCavaloId(listaParcelasFrota, obj.getId());
                valor = CalculoUtil.somaParcelas_seguroFrota(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasSeguroVida(List<Parcela_seguroVida> listaParcelasVida) {
            List<Parcela_seguroVida> dataSet = FiltraParcelaSeguroVida.listaPeloCavaloId(listaParcelasVida, 0L);
            BigDecimal valorAcumulado = CalculoUtil.somaParcelas_seguroVida(dataSet);
            BigDecimal valorRateio = getRateio(valorAcumulado);
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                adicionaValorAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static void despesasImposto(List<DespesasDeImposto> listaDespesaImposto) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesasDeImposto> dataSet = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, obj.getId());
                valor = CalculoUtil.somaDespesaImposto(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void lucro(int ano) {
            List<Frete> listaFreteLiquido = FiltraFrete.listaPorAno(freteDao.todos(), ano);

            List<CustosDeAbastecimento> listaCustoAbastecimento = FiltraCustosAbastecimento.listaPorAno(abastecimentoDao.todos(), ano);
            List<CustosDePercurso> listaCustoPercurso = FiltraCustosPercurso.listaPorAno(custosPercursoDao.todos(), ano);
            List<CustosDeManutencao> listaCustoManutencao = FiltraCustosManutencao.listaPorAno(manutencaoDao.todos(), ano);
            List<DespesaAdm> listaDespesaAdm = FiltraDespesasAdm.listaPorAno(admDao.todos(), ano);
            List<DespesaCertificado> listaDespesaCertificado = FiltraDespesasCertificado.listaPorAno(certificadoDao.todos(), ano);

            List<Parcela_seguroFrota> listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorAno(parcelaFrotaDao.todos(), ano);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaParcelaSeguro, true);

            List<Parcela_seguroVida> listaParcelaSeguroVida = FiltraParcelaSeguroVida.listaPorAno(parcelaVidaDao.todos(), ano);
            listaParcelaSeguroVida = FiltraParcelaSeguroVida.listaPorStatusDePagamento(listaParcelaSeguroVida, true);

            List<DespesasDeImposto> listaDespesaImposto = FiltraDespesasImposto.listaPorAno(impostoDao.todos(), ano);

            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<Frete> dataSetFrete = FiltraFrete.listaPorCavaloId(listaFreteLiquido, obj.getId());
                BigDecimal liquido = CalculoUtil.somaFreteLiquido(dataSetFrete);
                BigDecimal comissao = CalculoUtil.somaComissao(dataSetFrete);
                adicionaValorAoObjetoTemporario(liquido, obj);
                subtraiValorAoObjetoTemporario(comissao, obj);

                List<CustosDeAbastecimento> dataSetAbastecimento = FiltraCustosAbastecimento.listaPorCavaloId(listaCustoAbastecimento, obj.getId());
                BigDecimal abastecimento = CalculoUtil.somaCustosDeAbastecimento(dataSetAbastecimento);
                subtraiValorAoObjetoTemporario(abastecimento, obj);

                List<CustosDePercurso> dataSetPercurso = FiltraCustosPercurso.listaPorCavaloId(listaCustoPercurso, obj.getId());
                BigDecimal percurso = CalculoUtil.somaCustosDePercurso(dataSetPercurso);
                subtraiValorAoObjetoTemporario(percurso, obj);

                List<CustosDeManutencao> dataSetManutencao = FiltraCustosManutencao.listaPorCavaloId(listaCustoManutencao, obj.getId());
                BigDecimal manutencao = CalculoUtil.somaCustosDeManutencao(dataSetManutencao);
                subtraiValorAoObjetoTemporario(manutencao, obj);

                List<DespesaAdm> dataSetAdm = FiltraDespesasAdm.listaPorCavaloId(listaDespesaAdm, obj.getId());
                BigDecimal adm = CalculoUtil.somaDespesasAdm(dataSetAdm);
                subtraiValorAoObjetoTemporario(adm, obj);

                List<DespesaCertificado> dataSetCertificado = FiltraDespesasCertificado.listaPorCavaloId(listaDespesaCertificado, obj.getId());
                BigDecimal certificado = CalculoUtil.somaDespesasCertificado(dataSetCertificado);
                subtraiValorAoObjetoTemporario(certificado, obj);

                List<Parcela_seguroFrota> dataSetParcelaSeguro = FiltraParcelaSeguroFrota.listaPeloCavaloId(listaParcelaSeguro, obj.getId());
                BigDecimal parcela = CalculoUtil.somaParcelas_seguroFrota(dataSetParcelaSeguro);
                subtraiValorAoObjetoTemporario(parcela, obj);

                List<Parcela_seguroVida> dataSetParcelaSeguroVida = FiltraParcelaSeguroVida.listaPeloCavaloId(listaParcelaSeguroVida, 0L);
                BigDecimal parcelaVida = CalculoUtil.somaParcelas_seguroVida(dataSetParcelaSeguroVida);
                subtraiValorAoObjetoTemporario(parcelaVida, obj);

                List<DespesasDeImposto> dataSetImposto = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, obj.getId());
                BigDecimal imposto = CalculoUtil.somaDespesaImposto(dataSetImposto);
                subtraiValorAoObjetoTemporario(imposto, obj);

            }

        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          Busca Mensal                                      ||
    //----------------------------------------------------------------------------------------------

    static class ConfiguraObjetosQuandoBuscaMensal {
        private static void despesasImposto(List<DespesasDeImposto> listaAnual, int mes) {
            List<DespesasDeImposto> lista = FiltraDespesasImposto.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesasDeImposto> dataSet = FiltraDespesasImposto.listaPorCavaloId(lista, obj.getId());
                valor = CalculoUtil.somaDespesaImposto(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasSeguroFrota(List<Parcela_seguroFrota> listaAnual, int mes) {
            List<Parcela_seguroFrota> lista = FiltraParcelaSeguroFrota.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Parcela_seguroFrota> dataSet = FiltraParcelaSeguroFrota.listaPeloCavaloId(lista, obj.getId());
                valor = CalculoUtil.somaParcelas_seguroFrota(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasSeguroVida(List<Parcela_seguroVida> listaAnual, int mes) {
            List<Parcela_seguroVida> lista = FiltraParcelaSeguroVida.listaPorMes(listaAnual, mes);
            BigDecimal valorAcumulado = CalculoUtil.somaParcelas_seguroVida(lista);
            BigDecimal valorRateio = getRateio(valorAcumulado);
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                adicionaValorAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static void despesasCertificado(List<DespesaCertificado> listaAnual, int mes) {
            List<DespesaCertificado> listaMensal = FiltraDespesasCertificado.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaCertificado> dataSet = FiltraDespesasCertificado.listaPorCavaloId(listaMensal, obj.getId());
                valor = CalculoUtil.somaDespesasCertificado(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasAdm(List<DespesaAdm> listaAnual, int mes) {
            List<DespesaAdm> listaMensal = FiltraDespesasAdm.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaAdm> dataSet = FiltraDespesasAdm.listaPorCavaloId(listaMensal, obj.getId());
                valor = CalculoUtil.somaDespesasAdm(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDeAbastecimento(List<CustosDeAbastecimento> listaAnual, int mes) {
            List<CustosDeAbastecimento> listaMensal = FiltraCustosAbastecimento.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<CustosDeAbastecimento> dataSet = FiltraCustosAbastecimento.listaPorCavaloId(listaMensal, obj.getId());
                valor = CalculoUtil.somaCustosDeAbastecimento(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDeManutencao(List<CustosDeManutencao> listaAnual, int mes) {
            List<CustosDeManutencao> listaMensal = FiltraCustosManutencao.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<CustosDeManutencao> dataSet = FiltraCustosManutencao.listaPorCavaloId(listaMensal, obj.getId());
                valor = CalculoUtil.somaCustosDeManutencao(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void comissao(List<Frete> listaAnual, int mes) {
            List<Frete> listaMensal = FiltraFrete.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaMensal, obj.getId());
                valor = CalculoUtil.somaComissao(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDePercurso(List<CustosDePercurso> listaAnual, int mes) {
            List<CustosDePercurso> listaMensal = FiltraCustosPercurso.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<CustosDePercurso> dataSet = FiltraCustosPercurso.listaPorCavaloId(listaMensal, obj.getId());
                valor = CalculoUtil.somaCustosDePercurso(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void freteLiquido(List<Frete> listaAnual, int mes) {
            List<Frete> listaMensal = FiltraFrete.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaMensal, obj.getId());
                valor = CalculoUtil.somaFreteLiquido(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void freteBruto(List<Frete> listaAnual, int mes) {
            List<Frete> listaMensal = FiltraFrete.listaPorMes(listaAnual, mes);
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaMensal, obj.getId());
                valor = CalculoUtil.somaFreteBruto(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        public static void lucro(int ano, int mes) {
            List<Frete> listaFreteLiquido = FiltraFrete.listaPorAno(freteDao.todos(), ano);
            listaFreteLiquido = FiltraFrete.listaPorMes(listaFreteLiquido, mes);

            List<CustosDeAbastecimento> listaCustoAbastecimento = FiltraCustosAbastecimento.listaPorAno(abastecimentoDao.todos(), ano);
            listaCustoAbastecimento = FiltraCustosAbastecimento.listaPorMes(listaCustoAbastecimento, mes);

            List<CustosDePercurso> listaCustoPercurso = FiltraCustosPercurso.listaPorAno(custosPercursoDao.todos(), ano);
            listaCustoPercurso = FiltraCustosPercurso.listaPorMes(listaCustoPercurso, mes);

            List<CustosDeManutencao> listaCustoManutencao = FiltraCustosManutencao.listaPorAno(manutencaoDao.todos(), ano);
            listaCustoManutencao = FiltraCustosManutencao.listaPorMes(listaCustoManutencao, mes);

            List<DespesaAdm> listaDespesaAdm = FiltraDespesasAdm.listaPorAno(admDao.todos(), ano);
            listaDespesaAdm = FiltraDespesasAdm.listaPorMes(listaDespesaAdm, mes);

            List<DespesaCertificado> listaDespesaCertificado = FiltraDespesasCertificado.listaPorAno(certificadoDao.todos(), ano);
            listaDespesaCertificado = FiltraDespesasCertificado.listaPorMes(listaDespesaCertificado, mes);

            List<Parcela_seguroFrota> listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorAno(parcelaFrotaDao.todos(), ano);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorMes(listaParcelaSeguro, mes);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaParcelaSeguro, true);

            List<Parcela_seguroVida> listaParcelaSeguroVida = FiltraParcelaSeguroVida.listaPorAno(parcelaVidaDao.todos(), ano);
            listaParcelaSeguroVida = FiltraParcelaSeguroVida.listaPorMes(listaParcelaSeguroVida, mes);
            listaParcelaSeguroVida = FiltraParcelaSeguroVida.listaPorStatusDePagamento(listaParcelaSeguroVida, true);

            List<DespesasDeImposto> listaDespesaImposto = FiltraDespesasImposto.listaPorAno(impostoDao.todos(), ano);
            listaDespesaImposto = FiltraDespesasImposto.listaPorMes(listaDespesaImposto, mes);


            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<Frete> dataSetFrete = FiltraFrete.listaPorCavaloId(listaFreteLiquido, obj.getId());
                BigDecimal liquido = CalculoUtil.somaFreteLiquido(dataSetFrete);
                BigDecimal comissao = CalculoUtil.somaComissao(dataSetFrete);
                adicionaValorAoObjetoTemporario(liquido, obj);
                subtraiValorAoObjetoTemporario(comissao, obj);

                List<CustosDeAbastecimento> dataSetAbastecimento = FiltraCustosAbastecimento.listaPorCavaloId(listaCustoAbastecimento, obj.getId());
                BigDecimal abastecimento = CalculoUtil.somaCustosDeAbastecimento(dataSetAbastecimento);
                subtraiValorAoObjetoTemporario(abastecimento, obj);

                List<CustosDePercurso> dataSetPercurso = FiltraCustosPercurso.listaPorCavaloId(listaCustoPercurso, obj.getId());
                BigDecimal percurso = CalculoUtil.somaCustosDePercurso(dataSetPercurso);
                subtraiValorAoObjetoTemporario(percurso, obj);

                List<CustosDeManutencao> dataSetManutencao = FiltraCustosManutencao.listaPorCavaloId(listaCustoManutencao, obj.getId());
                BigDecimal manutencao = CalculoUtil.somaCustosDeManutencao(dataSetManutencao);
                subtraiValorAoObjetoTemporario(manutencao, obj);

                List<DespesaAdm> dataSetAdm = FiltraDespesasAdm.listaPorCavaloId(listaDespesaAdm, obj.getId());
                BigDecimal adm = CalculoUtil.somaDespesasAdm(dataSetAdm);
                subtraiValorAoObjetoTemporario(adm, obj);

                List<DespesaCertificado> dataSetCertificado = FiltraDespesasCertificado.listaPorCavaloId(listaDespesaCertificado, obj.getId());
                BigDecimal certificado = CalculoUtil.somaDespesasCertificado(dataSetCertificado);
                subtraiValorAoObjetoTemporario(certificado, obj);

                List<Parcela_seguroFrota> dataSetParcelaSeguro = FiltraParcelaSeguroFrota.listaPeloCavaloId(listaParcelaSeguro, obj.getId());
                BigDecimal parcela = CalculoUtil.somaParcelas_seguroFrota(dataSetParcelaSeguro);
                subtraiValorAoObjetoTemporario(parcela, obj);

                List<Parcela_seguroVida> dataSetParcelaSeguroVida = FiltraParcelaSeguroVida.listaPeloCavaloId(listaParcelaSeguroVida, 0L);
                BigDecimal parcelasegvida = CalculoUtil.somaParcelas_seguroVida(dataSetParcelaSeguroVida);
                subtraiValorAoObjetoTemporario(parcelasegvida, obj);

                List<DespesasDeImposto> dataSetImposto = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, obj.getId());
                BigDecimal imposto = CalculoUtil.somaDespesaImposto(dataSetImposto);
                subtraiValorAoObjetoTemporario(imposto, obj);

            }
        }

    }

    //----------------------------------------------------------------------------------------------
    //                                        Busca Rateio Anual                                  ||
    //----------------------------------------------------------------------------------------------

    static class ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual {

        private static void despesasSeguroVida(List<Parcela_seguroVida> listaAnual) {
            BigDecimal valorAcumuladoParaTodos;
            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Parcela_seguroVida> despesaAcumulada = FiltraParcelaSeguroVida.listaPeloCavaloId(listaAnual, SEM_REF_CAVALO);
                valorAcumuladoParaTodos = CalculoUtil.somaParcelas_seguroVida(despesaAcumulada);
                valorRateio = getRateio(valorAcumuladoParaTodos);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static void despesasCertificado(List<DespesaCertificado> listaAnual) {
            List<DespesaCertificado> lista = FiltraDespesasCertificado.listaPorTipoDespesa(listaAnual, INDIRETA);
            BigDecimal valorAcumuladoParaTodos;
            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaCertificado> despesaAcumulada = FiltraDespesasCertificado.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorAcumuladoParaTodos = CalculoUtil.somaDespesasCertificado(despesaAcumulada);
                valorRateio = getRateio(valorAcumuladoParaTodos);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static void despesasAdm(List<DespesaAdm> listaAnual) {
            List<DespesaAdm> lista = FiltraDespesasAdm.listaPorTipo(listaAnual, INDIRETA);
            BigDecimal valorAcumuladoParaTodos;
            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaAdm> despesaAcumulada = FiltraDespesasAdm.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorAcumuladoParaTodos = CalculoUtil.somaDespesasAdm(despesaAcumulada);
                valorRateio = getRateio(valorAcumuladoParaTodos);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static void despesasImposto(List<DespesasDeImposto> listaAnual) {
            List<DespesasDeImposto> lista = FiltraDespesasImposto.listaPorTipo(listaAnual, INDIRETA);
            BigDecimal valorAcumuladoParaTodos;
            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesasDeImposto> despesaAcumulada = FiltraDespesasImposto.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorAcumuladoParaTodos = CalculoUtil.somaDespesaImposto(despesaAcumulada);
                valorRateio = getRateio(valorAcumuladoParaTodos);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        public static void lucro(int ano) {
            List<DespesaAdm> listaDespesaAdm = FiltraDespesasAdm.listaPorAno(admDao.todos(), ano);
            listaDespesaAdm = FiltraDespesasAdm.listaPorTipo(listaDespesaAdm, INDIRETA);

            List<DespesaCertificado> listaDespesaCertificado = FiltraDespesasCertificado.listaPorAno(certificadoDao.todos(), ano);
            listaDespesaCertificado = FiltraDespesasCertificado.listaPorTipoDespesa(listaDespesaCertificado, INDIRETA);

            List<Parcela_seguroFrota> listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorAno(parcelaFrotaDao.todos(), ano);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaParcelaSeguro, true);

            List<Parcela_seguroVida> listaParcelaVida = FiltraParcelaSeguroVida.listaPorAno(parcelaVidaDao.todos(), ano);
            listaParcelaVida = FiltraParcelaSeguroVida.listaPorStatusDePagamento(listaParcelaVida, true);

            List<DespesasDeImposto> listaDespesaImposto = FiltraDespesasImposto.listaPorAno(impostoDao.todos(), ano);
            listaDespesaImposto = FiltraDespesasImposto.listaPorTipo(listaDespesaImposto, INDIRETA);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaAdm> dataSetAdm = FiltraDespesasAdm.listaPorCavaloId(listaDespesaAdm, SEM_REF_CAVALO);
                BigDecimal adm = CalculoUtil.somaDespesasAdm(dataSetAdm);
                valorRateio = getRateio(adm);
                removeRateioAoObjetoTemporario(valorRateio, obj);

                List<DespesaCertificado> dataSetCertificado = FiltraDespesasCertificado.listaPorCavaloId(listaDespesaCertificado, SEM_REF_CAVALO);
                BigDecimal certificado = CalculoUtil.somaDespesasCertificado(dataSetCertificado);
                valorRateio = getRateio(certificado);
                removeRateioAoObjetoTemporario(valorRateio, obj);

                List<Parcela_seguroFrota> dataSetParcelaSeguro = FiltraParcelaSeguroFrota.listaPeloCavaloId(listaParcelaSeguro, SEM_REF_CAVALO);
                BigDecimal parcela = CalculoUtil.somaParcelas_seguroFrota(dataSetParcelaSeguro);
                valorRateio = getRateio(parcela);
                removeRateioAoObjetoTemporario(valorRateio, obj);

                List<Parcela_seguroVida> dataSetParcelaSeguroVida = FiltraParcelaSeguroVida.listaPeloCavaloId(listaParcelaVida, 0L);
                BigDecimal parcelavida = CalculoUtil.somaParcelas_seguroVida(dataSetParcelaSeguroVida);
                valorRateio = getRateio(parcelavida);
                removeRateioAoObjetoTemporario(valorRateio, obj);

                List<DespesasDeImposto> dataSetImposto = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, SEM_REF_CAVALO);
                BigDecimal imposto = CalculoUtil.somaDespesaImposto(dataSetImposto);
                valorRateio = getRateio(imposto);
                removeRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

    }

    //----------------------------------------------------------------------------------------------
    //                                        Busca Rateio Mensal                                 ||
    //----------------------------------------------------------------------------------------------

    private static class ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal {
        private static void despesasImposto(List<DespesasDeImposto> listaAnual, int mes) {
            List<DespesasDeImposto> lista = configuraListaConformeParametrosBuscado_rateioMensalImposto(listaAnual, mes);
            BigDecimal valorTotalASerRateado = CalculoUtil.somaDespesaImposto(lista);
            BigDecimal valorRateio = getRateio(valorTotalASerRateado);
            for (ObjetoTemporario_representaCavalo o : objTemporarioDao.listaTodos()) {
                adicionaRateioAoObjetoTemporario(valorRateio, o);
            }
        }

        private static List<DespesasDeImposto> configuraListaConformeParametrosBuscado_rateioMensalImposto(List<DespesasDeImposto> listaAnual, int mes) {
            List<DespesasDeImposto> lista = FiltraDespesasImposto.listaPorMes(listaAnual, mes);
            lista = FiltraDespesasImposto.listaPorTipo(lista, INDIRETA);
            return lista;
        }

        private static void despesasSeguroVida(List<Parcela_seguroVida> listaAnual, int mes) {
            List<Parcela_seguroVida> lista = configuraListaConformeParametrosBuscado_rateioMensalSeguro(listaAnual, mes);
            BigDecimal valorTotalASerRateado = CalculoUtil.somaParcelas_seguroVida(lista);
            BigDecimal valorRateio = getRateio(valorTotalASerRateado);
            for (ObjetoTemporario_representaCavalo o : objTemporarioDao.listaTodos()) {
                adicionaRateioAoObjetoTemporario(valorRateio, o);
            }
        }

        private static List<Parcela_seguroVida> configuraListaConformeParametrosBuscado_rateioMensalSeguro(List<Parcela_seguroVida> listaAnual, int mes) {
            return FiltraParcelaSeguroVida.listaPorMes(listaAnual, mes);
        }

        private static void despesasCertificado(List<DespesaCertificado> listaAnual, int mes) {
            List<DespesaCertificado> lista = configuraListaConformeParametrosBuscado_rateioMensalCertificado(listaAnual, mes);
            BigDecimal valorTotalASerRateado = CalculoUtil.somaDespesasCertificado(lista);
            BigDecimal valorRateio = getRateio(valorTotalASerRateado);
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static List<DespesaCertificado> configuraListaConformeParametrosBuscado_rateioMensalCertificado(List<DespesaCertificado> listaAnual, int mes) {
            List<DespesaCertificado> lista = FiltraDespesasCertificado.listaPorMes(listaAnual, mes);
            lista = FiltraDespesasCertificado.listaPorTipoDespesa(lista, INDIRETA);
            return lista;
        }

        private static void despesasAdm(List<DespesaAdm> listaAnual, int mes) {
            List<DespesaAdm> lista = configuraListaConformeParametrosBuscado_rateioMensalAdm(listaAnual, mes);
            BigDecimal valorTotalASerRateado = CalculoUtil.somaDespesasAdm(lista);
            BigDecimal valorRateio = getRateio(valorTotalASerRateado);
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static List<DespesaAdm> configuraListaConformeParametrosBuscado_rateioMensalAdm(List<DespesaAdm> listaAnual, int mes) {
            List<DespesaAdm> lista = FiltraDespesasAdm.listaPorMes(listaAnual, mes);
            lista = FiltraDespesasAdm.listaPorTipo(lista, INDIRETA);
            return lista;
        }

        public static void lucro(int mes) {
            List<DespesaAdm> listaDespesaAdm = FiltraDespesasAdm.listaPorAno(admDao.todos(), mes);
            listaDespesaAdm = FiltraDespesasAdm.listaPorTipo(listaDespesaAdm, INDIRETA);

            List<DespesaCertificado> listaDespesaCertificado = FiltraDespesasCertificado.listaPorAno(certificadoDao.todos(), mes);
            listaDespesaCertificado = FiltraDespesasCertificado.listaPorTipoDespesa(listaDespesaCertificado, INDIRETA);

            List<Parcela_seguroFrota> listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorAno(parcelaFrotaDao.todos(), mes);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaParcelaSeguro, true);

            List<Parcela_seguroVida> listaParcelaSeguroVida = FiltraParcelaSeguroVida.listaPorAno(parcelaVidaDao.todos(), mes);
            listaParcelaSeguroVida = FiltraParcelaSeguroVida.listaPorStatusDePagamento(listaParcelaSeguroVida, true);

            List<DespesasDeImposto> listaDespesaImposto = FiltraDespesasImposto.listaPorAno(impostoDao.todos(), mes);
            listaDespesaImposto = FiltraDespesasImposto.listaPorTipo(listaDespesaImposto, INDIRETA);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaAdm> dataSetAdm = FiltraDespesasAdm.listaPorCavaloId(listaDespesaAdm, SEM_REF_CAVALO);
                BigDecimal adm = CalculoUtil.somaDespesasAdm(dataSetAdm);
                valorRateio = getRateio(adm);
                removeRateioAoObjetoTemporario(valorRateio, obj);

                List<DespesaCertificado> dataSetCertificado = FiltraDespesasCertificado.listaPorCavaloId(listaDespesaCertificado, SEM_REF_CAVALO);
                BigDecimal certificado = CalculoUtil.somaDespesasCertificado(dataSetCertificado);
                valorRateio = getRateio(certificado);
                removeRateioAoObjetoTemporario(valorRateio, obj);

                List<Parcela_seguroFrota> dataSetParcelaSeguro = FiltraParcelaSeguroFrota.listaPeloCavaloId(listaParcelaSeguro, SEM_REF_CAVALO);
                BigDecimal parcela = CalculoUtil.somaParcelas_seguroFrota(dataSetParcelaSeguro);
                valorRateio = getRateio(parcela);
                removeRateioAoObjetoTemporario(valorRateio, obj);

                List<Parcela_seguroVida> dataSetParcelaSeguroVida = FiltraParcelaSeguroVida.listaPeloCavaloId(listaParcelaSeguroVida, SEM_REF_CAVALO);
                BigDecimal parcelaVida = CalculoUtil.somaParcelas_seguroVida(dataSetParcelaSeguroVida);
                valorRateio = getRateio(parcelaVida);
                removeRateioAoObjetoTemporario(valorRateio, obj);

                List<DespesasDeImposto> dataSetImposto = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, SEM_REF_CAVALO);
                BigDecimal imposto = CalculoUtil.somaDespesaImposto(dataSetImposto);
                valorRateio = getRateio(imposto);
                removeRateioAoObjetoTemporario(valorRateio, obj);
            }


        }



    }

    //----------------------------------------------------------------------------------------------
    //                                         Outros metodos                                     ||
    //----------------------------------------------------------------------------------------------


    private static BigDecimal getRateio(@NonNull BigDecimal valor) {
        BigDecimal quantidadeCavalos = new BigDecimal(cavaloSize);
        return valor.divide(quantidadeCavalos, 2, RoundingMode.HALF_EVEN);
    }

    private static void adicionaValorAoObjetoTemporario(BigDecimal valor, @NonNull ObjetoTemporario_representaCavalo obj) {
        obj.adicionaValor(valor);
        obj.adicionaAoSaldoAcumulado(valor);
    }

    private static void subtraiValorAoObjetoTemporario(BigDecimal valor, @NonNull ObjetoTemporario_representaCavalo obj) {
        obj.removeValor(valor);
        obj.removeDoSaldoAcumulado(valor);
    }

    private static void adicionaRateioAoObjetoTemporario(BigDecimal valorRateio, @NonNull ObjetoTemporario_representaCavalo d) {
        d.adicionaValor(valorRateio);
        d.adicionaAoSaldoAcumulado(valorRateio);
    }

    private static void removeRateioAoObjetoTemporario(BigDecimal valorRateio, @NonNull ObjetoTemporario_representaCavalo d) {
        d.removeValor(valorRateio);
        d.removeDoSaldoAcumulado(valorRateio);
    }

}
