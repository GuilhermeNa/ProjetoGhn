package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoMeses.MES_DEFAULT;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.CriaObjetoTemporarioParaExibirNaRecyclerExtension.ConfiguraObjetosQuandoBuscaAnual.custoManutencaoDao;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.CriaObjetoTemporarioParaExibirNaRecyclerExtension.ConfiguraObjetosQuandoBuscaAnual.custosAbastecimentoDao;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.CriaObjetoTemporarioParaExibirNaRecyclerExtension.ConfiguraObjetosQuandoBuscaAnual.custosPercursoDao;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.CriaObjetoTemporarioParaExibirNaRecyclerExtension.ConfiguraObjetosQuandoBuscaAnual.despesaAdmDao;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.CriaObjetoTemporarioParaExibirNaRecyclerExtension.ConfiguraObjetosQuandoBuscaAnual.despesaCertificadoDao;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.CriaObjetoTemporarioParaExibirNaRecyclerExtension.ConfiguraObjetosQuandoBuscaAnual.despesaImpostoDao;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.dao.CustosDeManutencaoDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.dao.DespesasCertificadoDAO;
import br.com.transporte.AppGhn.dao.DespesasImpostoDAO;
import br.com.transporte.AppGhn.dao.ObjetoTemporario_representaCavaloDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.AppGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.AppGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.AppGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.filtros.FiltraParcelaSeguroFrota;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.temporarios.ObjetoTemporario_representaCavalo;
import br.com.transporte.AppGhn.util.CalculoUtil;

public class CriaObjetoTemporarioParaExibirNaRecyclerExtension {

    public static final long SEM_REF_CAVALO = 0;
    private static ObjetoTemporario_representaCavaloDAO objTemporarioDao;
    private static TipoDeRequisicao tipo;
    private static int ano, mes, cavaloSize;

    private static int getAno() {
        return ano;
    }

    private static void setAno(int ano) {
        CriaObjetoTemporarioParaExibirNaRecyclerExtension.ano = ano;
    }

    private static TipoDeRequisicao getTipo() {
        return tipo;
    }

    private static void setTipo(TipoDeRequisicao tipo) {
        CriaObjetoTemporarioParaExibirNaRecyclerExtension.tipo = tipo;
    }

    public static void setMes(int mes) {
        CriaObjetoTemporarioParaExibirNaRecyclerExtension.mes = mes;
    }

    private static List<ObjetoTemporario_representaCavalo> getListaDetalhes() {
        return objTemporarioDao.listaTodos();
    }

//---------------------------------------------------------------------------------------------

    public static List<ObjetoTemporario_representaCavalo> solicitaDataAnual(int ano, TipoDeRequisicao tipo, boolean switchIsCheched) {
        setTipo(tipo);
        setAno(ano);
        setMes(MES_DEFAULT.getRef());

        configuracaoInicialDeObjetoTemporario();
        configuraListaDeObjetosDeAcordoComSolicitacao(switchIsCheched);

        return getListaDetalhes();
    }

    public static List<ObjetoTemporario_representaCavalo> solicitaDataMensal(int mes, boolean switchIsCheched) {
        setMes(mes);
        configuracaoInicialDeObjetoTemporario();
        configuraListaDeObjetosDeAcordoComSolicitacao(switchIsCheched);
        return getListaDetalhes();
    }

    public static List<ObjetoTemporario_representaCavalo> solicitaDadosPorSwitch(boolean isChecked, int mes) {
        if (mes > MES_DEFAULT.getRef()) {
            return solicitaDataMensal(mes, isChecked);
        } else {
            return solicitaDataAnual(getAno(), getTipo(), isChecked);
        }
    }

    private static void configuraListaDeObjetosDeAcordoComSolicitacao(boolean switchIsChecked) {
        FreteDAO freteDao;
        ParcelaDeSeguroDAO parcelaDao;
        List<Parcela_seguroFrota> listaAnualParcelasPagas;

        switch (getTipo()) {
            case FRETE_BRUTO:
                freteDao = new FreteDAO();
                List<Frete> listaAnualFrete = FiltraFrete.listaPorAno(freteDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.freteBruto(listaAnualFrete);
                else
                    ConfiguraObjetosQuandoBuscaMensal.freteBruto(listaAnualFrete, mes);
                break;


            case FRETE_LIQUIDO:
                freteDao = new FreteDAO();
                List<Frete> listaAnualFreteLiquido = FiltraFrete.listaPorAno(freteDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.freteLiquido(listaAnualFreteLiquido);
                else
                    ConfiguraObjetosQuandoBuscaMensal.freteLiquido(listaAnualFreteLiquido, mes);
                break;


            case LUCRO_LIQUIDO:
                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.lucro();
                    ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.lucro();
                } else {
                    ConfiguraObjetosQuandoBuscaMensal.lucro();
                    ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.lucro();
                }
                break;


            case CUSTOS_PERCURSO:
                CustosDePercursoDAO custosDePercursoDao = new CustosDePercursoDAO();
                List<CustosDePercurso> listaAnualCustosDePercurso = FiltraCustosPercurso.listaPorAno(custosDePercursoDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.custosDePercurso(listaAnualCustosDePercurso);
                else
                    ConfiguraObjetosQuandoBuscaMensal.custosDePercurso(listaAnualCustosDePercurso, mes);
                break;


            case COMISSAO:
                freteDao = new FreteDAO();
                List<Frete> listaAnualDeComissao = FiltraFrete.listaPorAno(freteDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.comissao(listaAnualDeComissao);
                else
                    ConfiguraObjetosQuandoBuscaMensal.comissao(listaAnualDeComissao, mes);
                break;


            case CUSTOS_MANUTENCAO:
                CustosDeManutencaoDAO manutencaoDao = new CustosDeManutencaoDAO();
                List<CustosDeManutencao> listaAnualDeManutencao = FiltraCustosManutencao.listaPorAno(manutencaoDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.custosDeManutencao(listaAnualDeManutencao);
                else
                    ConfiguraObjetosQuandoBuscaMensal.custosDeManutencao(listaAnualDeManutencao, mes);
                break;


            case CUSTOS_ABASTECIMENTO:
                CustosDeAbastecimentoDAO abastecimentoDao = new CustosDeAbastecimentoDAO();
                List<CustosDeAbastecimento> listaAnualDeAbastecimento = FiltraCustosAbastecimento.listaPorAno(abastecimentoDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.custosDeAbastecimento(listaAnualDeAbastecimento);
                else
                    ConfiguraObjetosQuandoBuscaMensal.custosDeAbastecimento(listaAnualDeAbastecimento, mes);
                break;


            case DESPESAS_ADM:
                DespesasAdmDAO despesaAdmDao = new DespesasAdmDAO();
                List<DespesaAdm> listaAnualDespesaAdm = FiltraDespesasAdm.listaPorAno(despesaAdmDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.despesasAdm(listaAnualDespesaAdm);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.despesasAdm(listaAnualDespesaAdm);
                } else {
                    ConfiguraObjetosQuandoBuscaMensal.despesasAdm(listaAnualDespesaAdm, mes);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.despesasAdm(listaAnualDespesaAdm, mes);
                }
                break;


            case DESPESA_CERTIFICADOS:
                DespesasCertificadoDAO certificadoDao = new DespesasCertificadoDAO();
                List<DespesaCertificado> listaAnualDespesaCertificado = FiltraDespesasCertificado.listaPorAno(certificadoDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.despesasCertificado(listaAnualDespesaCertificado);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.despesasCertificado(listaAnualDespesaCertificado);
                } else {
                    ConfiguraObjetosQuandoBuscaMensal.despesasCertificado(listaAnualDespesaCertificado, mes);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.despesasCertificado(listaAnualDespesaCertificado, mes);
                }
                break;


            case DESPESA_SEGURO_FROTA:
                parcelaDao = new ParcelaDeSeguroDAO();
                listaAnualParcelasPagas = FiltraParcelaSeguroFrota.listaPorAno(parcelaDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef())
                    ConfiguraObjetosQuandoBuscaAnual.despesasSeguro(listaAnualParcelasPagas, DIRETA);
                else
                    ConfiguraObjetosQuandoBuscaMensal.despesasSeguro(listaAnualParcelasPagas, mes, DIRETA);
                break;


            case DESPESA_SEGURO_VIDA:
                parcelaDao = new ParcelaDeSeguroDAO();
                listaAnualParcelasPagas = FiltraParcelaSeguroFrota.listaPorAno(parcelaDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.despesasSeguro(listaAnualParcelasPagas, INDIRETA);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.despesasSeguroIndireto(listaAnualParcelasPagas);

                } else {
                    ConfiguraObjetosQuandoBuscaMensal.despesasSeguro(listaAnualParcelasPagas, mes, INDIRETA);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.despesasSeguroIndireto(listaAnualParcelasPagas, mes);
                }
                break;


            case DESPESAS_IMPOSTOS:
                DespesasImpostoDAO impostoDao = new DespesasImpostoDAO();
                List<DespesasDeImposto> listaAnualImpostos = FiltraDespesasImposto.listaPorAno(impostoDao.listaTodos(), ano);

                if (mes == MES_DEFAULT.getRef()) {
                    ConfiguraObjetosQuandoBuscaAnual.despesasImposto(listaAnualImpostos);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual.despesasImposto(listaAnualImpostos);

                } else {
                    ConfiguraObjetosQuandoBuscaMensal.despesasImposto(listaAnualImpostos, mes);
                    if (switchIsChecked)
                        ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal.despesasImposto(listaAnualImpostos, mes);
                }
        }

        for (ObjetoTemporario_representaCavalo d : getListaDetalhes()) {
            d.definePercentual();
        }
    }

    private static void configuracaoInicialDeObjetoTemporario() {
        objTemporarioDao = new ObjetoTemporario_representaCavaloDAO();
        objTemporarioDao.clear();
        ObjetoTemporario_representaCavalo.resetaAcumulado();

        CavaloDAO cavaloDao = new CavaloDAO();
        List<Cavalo> listaCavalos = cavaloDao.listaTodos();
        cavaloSize = listaCavalos.size();

        for (Cavalo c : listaCavalos) {

            ObjetoTemporario_representaCavalo d = new ObjetoTemporario_representaCavalo();
            String nome = getNome(c);

            d.setPlaca(c.getPlaca());
            d.setNome(nome);
            d.setId(c.getId());
            objTemporarioDao.adiciona(d);
        }
    }

    private static String getNome(@NonNull Cavalo cavalo) {
        if (cavalo.getMotorista().getNome() != null) {
            return cavalo.getMotorista().getNome();
        } else {
            return "S/M";
        }
    }


    //----------------------------------------------------------------------------------------------
    //                                          Inner Class                                       ||
    //----------------------------------------------------------------------------------------------

    static class ConfiguraObjetosQuandoBuscaAnual {
        static FreteDAO freteDao = new FreteDAO();
        static CustosDeAbastecimentoDAO custosAbastecimentoDao = new CustosDeAbastecimentoDAO();
        static CustosDePercursoDAO custosPercursoDao = new CustosDePercursoDAO();
        static CustosDeManutencaoDAO custoManutencaoDao = new CustosDeManutencaoDAO();
        static DespesasAdmDAO despesaAdmDao = new DespesasAdmDAO();
        static DespesasCertificadoDAO despesaCertificadoDao = new DespesasCertificadoDAO();
        static ParcelaDeSeguroDAO parcelaDao = new ParcelaDeSeguroDAO();
        static DespesasImpostoDAO despesaImpostoDao = new DespesasImpostoDAO();

        private static void freteBruto(List<Frete> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaAnual, obj.getId());
           //     valor = CalculoUtil.somaFreteBruto(dataSet);

          //      adicionaValorAoObjetoTemporario(valor, obj);
            }

        }

        private static void freteLiquido(List<Frete> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaAnual, obj.getId());
          //      valor = CalculoUtil.somaFreteLiquido(dataSet);

          //      adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDePercurso(List<CustosDePercurso> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<CustosDePercurso> dataSet = FiltraCustosPercurso.listaPorCavaloId(listaAnual, obj.getId());
                valor = CalculoUtil.somaCustosDePercurso(dataSet);

                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void comissao(List<Frete> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaAnual, obj.getId());
         //       valor = CalculoUtil.somaComissao(dataSet);

           //     adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDeManutencao(List<CustosDeManutencao> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<CustosDeManutencao> dataSet = FiltraCustosManutencao.listaPorCavaloId(listaAnual, obj.getId());
                valor = CalculoUtil.somaCustosDeManutencao(dataSet);

                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void custosDeAbastecimento(List<CustosDeAbastecimento> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<CustosDeAbastecimento> dataSet = FiltraCustosAbastecimento.listaPorCavaloId(listaAnual, obj.getId());
                valor = CalculoUtil.somaCustosDeAbastecimento(dataSet);

                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasAdm(List<DespesaAdm> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<DespesaAdm> dataSet = FiltraDespesasAdm.listaPorCavaloId(listaAnual, obj.getId());
                valor = CalculoUtil.somaDespesasAdm(dataSet);

                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasCertificado(List<DespesaCertificado> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<DespesaCertificado> dataSet = FiltraDespesasCertificado.listaPorCavaloId(listaAnual, obj.getId());
                valor = CalculoUtil.somaDespesasCertificado(dataSet);

                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasSeguro(List<Parcela_seguroFrota> listaAnual, TipoDespesa tipo) {
            List<Parcela_seguroFrota> lista = FiltraParcelaSeguroFrota.listaPorTipo(listaAnual, tipo);
            lista = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(lista, true);

            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<Parcela_seguroFrota> dataSet = FiltraParcelaSeguroFrota.listaPeloCavaloId(lista, obj.getId());
                valor = CalculoUtil.somaParcelas_seguroFrota(dataSet);

                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasImposto(List<DespesasDeImposto> listaAnual) {
            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesasDeImposto> dataSet = FiltraDespesasImposto.listaPorCavaloId(listaAnual, obj.getId());
                valor = CalculoUtil.somaDespesaImposto(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void lucro() {
            List<Frete> listaFreteLiquido = FiltraFrete.listaPorAno(freteDao.listaTodos(), ano);

            List<CustosDeAbastecimento> listaCustoAbastecimento = FiltraCustosAbastecimento.listaPorAno(custosAbastecimentoDao.listaTodos(), ano);
            List<CustosDePercurso> listaCustoPercurso = FiltraCustosPercurso.listaPorAno(custosPercursoDao.listaTodos(), ano);
            List<CustosDeManutencao> listaCustoManutencao = FiltraCustosManutencao.listaPorAno(custoManutencaoDao.listaTodos(), ano);
            List<DespesaAdm> listaDespesaAdm = FiltraDespesasAdm.listaPorAno(despesaAdmDao.listaTodos(), ano);
            List<DespesaCertificado> listaDespesaCertificado = FiltraDespesasCertificado.listaPorAno(despesaCertificadoDao.listaTodos(), ano);

            List<Parcela_seguroFrota> listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorAno(parcelaDao.listaTodos(), ano);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaParcelaSeguro, true);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorTipo(listaParcelaSeguro, DIRETA);

            List<DespesasDeImposto> listaDespesaImposto = FiltraDespesasImposto.listaPorAno(despesaImpostoDao.listaTodos(), ano);

            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<Frete> dataSetFrete = FiltraFrete.listaPorCavaloId(listaFreteLiquido, obj.getId());
         //       BigDecimal liquido = CalculoUtil.somaFreteLiquido(dataSetFrete);
         //       BigDecimal comissao = CalculoUtil.somaComissao(dataSetFrete);
         //       adicionaValorAoObjetoTemporario(liquido, obj);
         //       subtraiValorAoObjetoTemporario(comissao, obj);

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

                List<DespesasDeImposto> dataSetImposto = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, obj.getId());
                BigDecimal imposto = CalculoUtil.somaDespesaImposto(dataSetImposto);
                subtraiValorAoObjetoTemporario(imposto, obj);

            }

        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          Inner Class                                       ||
    //----------------------------------------------------------------------------------------------

    private static class ConfiguraObjetosQuandoBuscaMensal {

        private static void despesasImposto(List<DespesasDeImposto> listaAnual, int mes) {
            List<DespesasDeImposto> lista = FiltraDespesasImposto.listaPorMes(listaAnual, mes);

            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesasDeImposto> dataSet = FiltraDespesasImposto.listaPorCavaloId(lista, obj.getId());
                valor = CalculoUtil.somaDespesaImposto(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void despesasSeguro(List<Parcela_seguroFrota> listaAnual, int mes, TipoDespesa tipo) {
            List<Parcela_seguroFrota> lista = configuraListaConformeParametrosBuscados_seguro(listaAnual, mes, tipo);

            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Parcela_seguroFrota> dataSet = FiltraParcelaSeguroFrota.listaPeloCavaloId(lista, obj.getId());
                valor = CalculoUtil.somaParcelas_seguroFrota(dataSet);
                adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static List<Parcela_seguroFrota> configuraListaConformeParametrosBuscados_seguro(List<Parcela_seguroFrota> listaAnual, int mes, TipoDespesa tipo) {
            List<Parcela_seguroFrota> lista = FiltraParcelaSeguroFrota.listaPorMes(listaAnual, mes);
            lista = FiltraParcelaSeguroFrota.listaPorTipo(lista, tipo);
            lista = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(lista, true);
            return lista;
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
       //         valor = CalculoUtil.somaComissao(dataSet);
       //         adicionaValorAoObjetoTemporario(valor, obj);
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
          //      valor = CalculoUtil.somaFreteLiquido(dataSet);
          //      adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        private static void freteBruto(List<Frete> listaAnual, int mes) {
            List<Frete> listaMensal = FiltraFrete.listaPorMes(listaAnual, mes);

            BigDecimal valor;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Frete> dataSet = FiltraFrete.listaPorCavaloId(listaMensal, obj.getId());
        //        valor = CalculoUtil.somaFreteBruto(dataSet);
        //        adicionaValorAoObjetoTemporario(valor, obj);
            }
        }

        public static void lucro() {
            List<Frete> listaFreteLiquido = FiltraFrete.listaPorAno(ConfiguraObjetosQuandoBuscaAnual.freteDao.listaTodos(), ano);
            listaFreteLiquido = FiltraFrete.listaPorMes(listaFreteLiquido, mes);

            List<CustosDeAbastecimento> listaCustoAbastecimento = FiltraCustosAbastecimento.listaPorAno(custosAbastecimentoDao.listaTodos(), ano);
            listaCustoAbastecimento = FiltraCustosAbastecimento.listaPorMes(listaCustoAbastecimento, mes);

            List<CustosDePercurso> listaCustoPercurso = FiltraCustosPercurso.listaPorAno(custosPercursoDao.listaTodos(), ano);
            listaCustoPercurso = FiltraCustosPercurso.listaPorMes(listaCustoPercurso, mes);

            List<CustosDeManutencao> listaCustoManutencao = FiltraCustosManutencao.listaPorAno(custoManutencaoDao.listaTodos(), ano);
            listaCustoManutencao = FiltraCustosManutencao.listaPorMes(listaCustoManutencao, mes);

            List<DespesaAdm> listaDespesaAdm = FiltraDespesasAdm.listaPorAno(despesaAdmDao.listaTodos(), ano);
            listaDespesaAdm = FiltraDespesasAdm.listaPorMes(listaDespesaAdm, mes);

            List<DespesaCertificado> listaDespesaCertificado = FiltraDespesasCertificado.listaPorAno(despesaCertificadoDao.listaTodos(), ano);
            listaDespesaCertificado = FiltraDespesasCertificado.listaPorMes(listaDespesaCertificado, mes);

            List<Parcela_seguroFrota> listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorAno(ConfiguraObjetosQuandoBuscaAnual.parcelaDao.listaTodos(), ano);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorMes(listaParcelaSeguro, mes);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorTipo(listaParcelaSeguro, DIRETA);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaParcelaSeguro, true);

            List<DespesasDeImposto> listaDespesaImposto = FiltraDespesasImposto.listaPorAno(despesaImpostoDao.listaTodos(), ano);
            listaDespesaImposto = FiltraDespesasImposto.listaPorMes(listaDespesaImposto, mes);


            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {

                List<Frete> dataSetFrete = FiltraFrete.listaPorCavaloId(listaFreteLiquido, obj.getId());
       //         BigDecimal liquido = CalculoUtil.somaFreteLiquido(dataSetFrete);
       //         BigDecimal comissao = CalculoUtil.somaComissao(dataSetFrete);
       //         adicionaValorAoObjetoTemporario(liquido, obj);
        //        subtraiValorAoObjetoTemporario(comissao, obj);

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

                List<DespesasDeImposto> dataSetImposto = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, obj.getId());
                BigDecimal imposto = CalculoUtil.somaDespesaImposto(dataSetImposto);
                subtraiValorAoObjetoTemporario(imposto, obj);

            }


        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          Inner Class                                       ||
    //----------------------------------------------------------------------------------------------

    private static class ConfiguraRateioDeDespesasIndiretasQuandoBuscaAnual {

        private static void despesasSeguroIndireto(List<Parcela_seguroFrota> listaAnual) {
            List<Parcela_seguroFrota> lista = configuraListaConformeParametrosBuscado_rateioMensalSeguro(listaAnual);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Parcela_seguroFrota> dataSet = FiltraParcelaSeguroFrota.listaPeloCavaloId(lista, SEM_REF_CAVALO);
                valorRateio = CalculoUtil.somaParcelas_seguroFrota(dataSet);
                valorRateio = getRateio(valorRateio);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static List<Parcela_seguroFrota> configuraListaConformeParametrosBuscado_rateioMensalSeguro(List<Parcela_seguroFrota> listaAnual) {
            List<Parcela_seguroFrota> lista = FiltraParcelaSeguroFrota.listaPorTipo(listaAnual, INDIRETA);
            lista = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(lista, true);
            return lista;
        }

        private static void despesasCertificado(List<DespesaCertificado> listaAnual) {
            List<DespesaCertificado> lista = FiltraDespesasCertificado.listaPorTipoDespesa(listaAnual, INDIRETA);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaCertificado> dataSet = FiltraDespesasCertificado.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorRateio = CalculoUtil.somaDespesasCertificado(dataSet);
                valorRateio = getRateio(valorRateio);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static void despesasAdm(List<DespesaAdm> listaAnual) {
            List<DespesaAdm> lista = FiltraDespesasAdm.listaPorTipo(listaAnual, INDIRETA);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaAdm> dataSet = FiltraDespesasAdm.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorRateio = CalculoUtil.somaDespesasAdm(dataSet);
                valorRateio = getRateio(valorRateio);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static void despesasImposto(List<DespesasDeImposto> listaAnual) {
            List<DespesasDeImposto> lista = FiltraDespesasImposto.listaPorTipo(listaAnual, INDIRETA);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesasDeImposto> dataSet = FiltraDespesasImposto.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorRateio = CalculoUtil.somaDespesaImposto(dataSet);
                valorRateio = getRateio(valorRateio);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        public static void lucro() {
            List<DespesaAdm> listaDespesaAdm = FiltraDespesasAdm.listaPorAno(despesaAdmDao.listaTodos(), ano);
            listaDespesaAdm = FiltraDespesasAdm.listaPorTipo(listaDespesaAdm, INDIRETA);

            List<DespesaCertificado> listaDespesaCertificado = FiltraDespesasCertificado.listaPorAno(despesaCertificadoDao.listaTodos(), ano);
            listaDespesaCertificado = FiltraDespesasCertificado.listaPorTipoDespesa(listaDespesaCertificado, INDIRETA);

            List<Parcela_seguroFrota> listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorAno(ConfiguraObjetosQuandoBuscaAnual.parcelaDao.listaTodos(), ano);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaParcelaSeguro, true);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorTipo(listaParcelaSeguro, INDIRETA);

            List<DespesasDeImposto> listaDespesaImposto = FiltraDespesasImposto.listaPorAno(despesaImpostoDao.listaTodos(), ano);
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

                List<DespesasDeImposto> dataSetImposto = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, SEM_REF_CAVALO);
                BigDecimal imposto = CalculoUtil.somaDespesaImposto(dataSetImposto);
                valorRateio = getRateio(imposto);
                removeRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

    }

    //----------------------------------------------------------------------------------------------
    //                                          Inner Class                                       ||
    //----------------------------------------------------------------------------------------------

    private static class ConfiguraRateioDeDespesasIndiretasQuandoBuscaMensal {

        private static void despesasImposto(List<DespesasDeImposto> listaAnual, int mes) {
            List<DespesasDeImposto> lista = configuraListaConformeParametrosBuscado_rateioMensalImposto(listaAnual, mes);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesasDeImposto> dataSet = FiltraDespesasImposto.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorRateio = CalculoUtil.somaDespesaImposto(dataSet);
                valorRateio = getRateio(valorRateio);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static List<DespesasDeImposto> configuraListaConformeParametrosBuscado_rateioMensalImposto(List<DespesasDeImposto> listaAnual, int mes) {
            List<DespesasDeImposto> lista = FiltraDespesasImposto.listaPorMes(listaAnual, mes);
            lista = FiltraDespesasImposto.listaPorTipo(lista, INDIRETA);
            return lista;
        }

        private static void despesasSeguroIndireto(List<Parcela_seguroFrota> listaAnual, int mes) {
            List<Parcela_seguroFrota> lista = configuraListaConformeParametrosBuscado_rateioMensalSeguro(listaAnual, mes);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<Parcela_seguroFrota> dataSet = FiltraParcelaSeguroFrota.listaPeloCavaloId(lista, SEM_REF_CAVALO);
                valorRateio = CalculoUtil.somaParcelas_seguroFrota(dataSet);
                valorRateio = getRateio(valorRateio);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static List<Parcela_seguroFrota> configuraListaConformeParametrosBuscado_rateioMensalSeguro(List<Parcela_seguroFrota> listaAnual, int mes) {
            List<Parcela_seguroFrota> lista = FiltraParcelaSeguroFrota.listaPorMes(listaAnual, mes);
            lista = FiltraParcelaSeguroFrota.listaPorTipo(lista, INDIRETA);
            lista = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(lista, true);
            return lista;
        }

        private static void despesasCertificado(List<DespesaCertificado> listaAnual, int mes) {
            List<DespesaCertificado> lista = configuraListaConformeParametrosBuscado_rateioMensalCertificado(listaAnual, mes);

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaCertificado> dataSet = FiltraDespesasCertificado.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorRateio = CalculoUtil.somaDespesasCertificado(dataSet);
                valorRateio = getRateio(valorRateio);
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

            BigDecimal valorRateio;
            for (ObjetoTemporario_representaCavalo obj : objTemporarioDao.listaTodos()) {
                List<DespesaAdm> dataSet = FiltraDespesasAdm.listaPorCavaloId(lista, SEM_REF_CAVALO);
                valorRateio = CalculoUtil.somaDespesasAdm(dataSet);
                valorRateio = getRateio(valorRateio);
                adicionaRateioAoObjetoTemporario(valorRateio, obj);
            }
        }

        private static List<DespesaAdm> configuraListaConformeParametrosBuscado_rateioMensalAdm(List<DespesaAdm> listaAnual, int mes) {
            List<DespesaAdm> lista = FiltraDespesasAdm.listaPorMes(listaAnual, mes);
            lista = FiltraDespesasAdm.listaPorTipo(lista, INDIRETA);
            return lista;
        }

        public static void lucro() {
            List<DespesaAdm> listaDespesaAdm = FiltraDespesasAdm.listaPorAno(despesaAdmDao.listaTodos(), mes);
            listaDespesaAdm = FiltraDespesasAdm.listaPorTipo(listaDespesaAdm, INDIRETA);

            List<DespesaCertificado> listaDespesaCertificado = FiltraDespesasCertificado.listaPorAno(despesaCertificadoDao.listaTodos(), mes);
            listaDespesaCertificado = FiltraDespesasCertificado.listaPorTipoDespesa(listaDespesaCertificado, INDIRETA);

            List<Parcela_seguroFrota> listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorAno(ConfiguraObjetosQuandoBuscaAnual.parcelaDao.listaTodos(), mes);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorStatusDePagamento(listaParcelaSeguro, true);
            listaParcelaSeguro = FiltraParcelaSeguroFrota.listaPorTipo(listaParcelaSeguro, INDIRETA);

            List<DespesasDeImposto> listaDespesaImposto = FiltraDespesasImposto.listaPorAno(despesaImpostoDao.listaTodos(), mes);
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

                List<DespesasDeImposto> dataSetImposto = FiltraDespesasImposto.listaPorCavaloId(listaDespesaImposto, SEM_REF_CAVALO);
                BigDecimal imposto = CalculoUtil.somaDespesaImposto(dataSetImposto);
                valorRateio = getRateio(imposto);
                removeRateioAoObjetoTemporario(valorRateio, obj);
            }


        }
    }

    //--------

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
