package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoMeses.MES_DEFAULT;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasCertificadoDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.dao.CustosDeManutencaoDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.dao.DespesasImpostoDAO;
import br.com.transporte.AppGhn.dao.DetalhesDesempenhoDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.despesas.ParcelaDeSeguro;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.model.temporarios.DetalhesDesempenho;

public class DetalhesDoPeriodoExtension {
    private static List<CustosDeAbastecimento> dataSetCustosDeAbastecimento;
    private static List<CustosDeManutencao> dataSetCustosDeManutencao;
    private static List<DespesaCertificado> dataSetDespesaCertificado;
    private static List<CustosDePercurso> dataSetCustosDePercurso;
    private static List<DespesasDeImposto> dataSetDespesaImposto;
    private static List<ParcelaDeSeguro> dataSetParcelaSeguro;
    private static List<DespesaAdm> dataSetDespesaAdm;
    private static List<Frete> dataSetFrete;

    private static DetalhesDesempenhoDAO detalhesDao;
    private static TipoDeRequisicao tipo;
    private static int ano;
    private static boolean dataSetInicializado;
    private static int cavaloSize;

    private static int getAno() {
        return ano;
    }

    private static void setAno(int ano) {
        DetalhesDoPeriodoExtension.ano = ano;
    }

    private static TipoDeRequisicao getTipo() {
        return tipo;
    }

    private static void setTipo(TipoDeRequisicao tipo) {
        DetalhesDoPeriodoExtension.tipo = tipo;
    }

    private static List<DetalhesDesempenho> getListaDetalhes() {
        return detalhesDao.listaTodos();
    }

    private static boolean isDataSetInicializado() {
        return dataSetInicializado;
    }

    private static void setDataSetInicializado(boolean dataSetInicializado) {
        DetalhesDoPeriodoExtension.dataSetInicializado = dataSetInicializado;
    }

//---------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<DetalhesDesempenho> solicitaData(int ano, TipoDeRequisicao tipo, boolean switchIsCheched) {
        setDataSetInicializado(true);
        setTipo(tipo);
        setAno(ano);

        buscaInterna_Data();
        criaObjetosParaExibicao();
        configuraListaDeObjetosDeAcordoComSolicitacao(MES_DEFAULT.getRef(), switchIsCheched);

        return getListaDetalhes();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<DetalhesDesempenho> solicitaDataDoMes(int mes, boolean switchIsCheched) {
        criaObjetosParaExibicao();
        configuraListaDeObjetosDeAcordoComSolicitacao(mes, switchIsCheched);

        return getListaDetalhes();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<DetalhesDesempenho> solicitaDadosPorSwitch(boolean isChecked, int mes) {
        if(mes > MES_DEFAULT.getRef()){
            return solicitaDataDoMes(mes, isChecked);
        } else {
            return solicitaData(getAno(), getTipo(), isChecked);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void buscaInterna_Data() {
        CustosDeAbastecimentoDAO abastecimentoDao = new CustosDeAbastecimentoDAO();
        DespesasCertificadoDAO certificadoDao = new DespesasCertificadoDAO();
        CustosDePercursoDAO custosPercursoDao = new CustosDePercursoDAO();
        CustosDeManutencaoDAO manutencaoDao = new CustosDeManutencaoDAO();
        ParcelaDeSeguroDAO parcelaDao = new ParcelaDeSeguroDAO();
        DespesasImpostoDAO impostoDao = new DespesasImpostoDAO();
        DespesasAdmDAO admDao = new DespesasAdmDAO();
        FreteDAO freteDao = new FreteDAO();

        switch (tipo) {
            case FRETE_BRUTO:
            case FRETE_LIQUIDO:
            case COMISSAO:

                dataSetFrete = freteDao.listaTodos().stream()
                        .filter(f -> f.getData().getYear() == getAno())
                        .collect(Collectors.toList());

                break;

            case LUCRO_LIQUIDO:

                // fazer depois

                break;

            case CUSTOS_PERCURSO:

                dataSetCustosDePercurso = custosPercursoDao.listaTodos().stream()
                        .filter(c -> c.getData().getYear() == getAno())
                        .collect(Collectors.toList());

                break;

            case CUSTOS_MANUTENCAO:

                dataSetCustosDeManutencao = manutencaoDao.listaTodos().stream()
                        .filter(c -> c.getData().getYear() == getAno())
                        .collect(Collectors.toList());

                break;

            case CUSTOS_ABASTECIMENTO:

                dataSetCustosDeAbastecimento = abastecimentoDao.listaTodos().stream()
                        .filter(c -> c.getData().getYear() == getAno())
                        .collect(Collectors.toList());

                break;

            case DESPESAS_ADM:

                dataSetDespesaAdm = admDao.listaTodos().stream()
                        .filter(c -> c.getData().getYear() == getAno())
                        .collect(Collectors.toList());

                break;

            case DESPESA_CERTIFICADOS:

                dataSetDespesaCertificado = certificadoDao.listaTodos().stream()
                        .filter(c -> c.getDataDeEmissao().getYear() == getAno())
                        .collect(Collectors.toList());

                break;

            case DESPESA_SEGUROS_DIRETOS:

                dataSetParcelaSeguro = parcelaDao.listaTodos().stream()
                        .filter(p -> p.getTipoDespesa() == DIRETA)
                        .filter(p -> p.getData().getYear() == getAno())
                        .filter(Parcela::isPaga)
                        .collect(Collectors.toList());

                break;

            case DESPESA_SEGUROS_INDIRETOS:

                dataSetParcelaSeguro = parcelaDao.listaTodos().stream()
                        .filter(p -> p.getTipoDespesa() == INDIRETA)
                        .filter(p -> p.getData().getYear() == getAno())
                        .filter(Parcela::isPaga)
                        .collect(Collectors.toList());

                break;

            case DESPESAS_IMPOSTOS:

                dataSetDespesaImposto = impostoDao.listaTodos().stream()
                        .filter(d -> d.getData().getYear() == getAno())
                        .collect(Collectors.toList());

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void configuraListaDeObjetosDeAcordoComSolicitacao(int mes, boolean switchIsChecked) {
        BigDecimal valor;

        switch (getTipo()) {
            case FRETE_BRUTO:

                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetFrete.stream().filter(f -> f.getRefCavalo() == d.getId())
                                .map(Frete::getAdmFrete)
                                .map(Frete.AdmFinanceiroFrete::getFreteBruto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    } else {
                        valor = dataSetFrete.stream()
                                .filter(f -> f.getRefCavalo() == d.getId())
                                .filter(f -> f.getData().getMonthValue() == mes)
                                .map(Frete::getAdmFrete)
                                .map(Frete.AdmFinanceiroFrete::getFreteBruto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case FRETE_LIQUIDO:

                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetFrete.stream()
                                .filter(f -> f.getRefCavalo() == d.getId())
                                .map(Frete::getAdmFrete)
                                .map(Frete.AdmFinanceiroFrete::getFreteLiquidoAReceber)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    } else {
                        valor = dataSetFrete.stream()
                                .filter(f -> f.getRefCavalo() == d.getId())
                                .filter(f -> f.getData().getMonthValue() == mes)
                                .map(Frete::getAdmFrete)
                                .map(Frete.AdmFinanceiroFrete::getFreteLiquidoAReceber)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case LUCRO_LIQUIDO:

                break;

            case CUSTOS_PERCURSO:

                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetCustosDePercurso.stream()
                                .filter(c -> c.getRefCavalo() == d.getId())
                                .map(CustosDePercurso::getValorCusto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    } else {
                        valor = dataSetCustosDePercurso.stream()
                                .filter(c -> c.getRefCavalo() == d.getId())
                                .filter(c -> c.getData().getMonthValue() == mes)
                                .map(CustosDePercurso::getValorCusto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case COMISSAO:
                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetFrete.stream()
                                .filter(f -> f.getRefCavalo() == d.getId())
                                .map(Frete::getAdmFrete)
                                .map(Frete.AdmFinanceiroFrete::getComissaoAoMotorista)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    } else {
                        valor = dataSetFrete.stream()
                                .filter(f -> f.getRefCavalo() == d.getId())
                                .filter(f -> f.getData().getMonthValue() == mes)
                                .map(Frete::getAdmFrete)
                                .map(Frete.AdmFinanceiroFrete::getComissaoAoMotorista)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case CUSTOS_MANUTENCAO:
                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetCustosDeManutencao.stream()
                                .filter(c -> c.getRefCavalo() == d.getId())
                                .map(CustosDeManutencao::getValorCusto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    } else {
                        valor = dataSetCustosDeManutencao.stream()
                                .filter(c -> c.getRefCavalo() == d.getId())
                                .filter(c -> c.getData().getMonthValue() == mes)
                                .map(CustosDeManutencao::getValorCusto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case CUSTOS_ABASTECIMENTO:
                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetCustosDeAbastecimento.stream()
                                .filter(c -> c.getRefCavalo() == d.getId())
                                .map(CustosDeAbastecimento::getValorCusto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    } else {
                        valor = dataSetCustosDeAbastecimento.stream()
                                .filter(c -> c.getRefCavalo() == d.getId())
                                .filter(c -> c.getData().getMonthValue() == mes)
                                .map(CustosDeAbastecimento::getValorCusto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case DESPESAS_ADM:
                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {

                        valor = dataSetDespesaAdm.stream()
                                .filter(desp -> desp.getRefCavalo() == d.getId())
                                .map(DespesaAdm::getValorDespesa)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (switchIsChecked) {
                            BigDecimal rateioDespesasIndiretas = dataSetDespesaAdm.stream()
                                    .filter(desp -> desp.getTipoDespesa() == INDIRETA)
                                    .map(DespesaAdm::getValorDespesa)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            rateioDespesasIndiretas = calculaRateio(rateioDespesasIndiretas);
                            valor = valor.add(rateioDespesasIndiretas);
                        }

                    } else {
                        valor = dataSetDespesaAdm.stream()
                                .filter(desp -> desp.getRefCavalo() == d.getId())
                                .filter(desp -> desp.getData().getMonthValue() == mes)
                                .map(DespesaAdm::getValorDespesa)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (switchIsChecked) {
                            BigDecimal rateioDespesasIndiretas = dataSetDespesaAdm.stream()
                                    .filter(desp -> desp.getData().getMonthValue() == mes)
                                    .filter(desp -> desp.getTipoDespesa() == INDIRETA)
                                    .map(DespesaAdm::getValorDespesa)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            rateioDespesasIndiretas = calculaRateio(rateioDespesasIndiretas);
                            valor = valor.add(rateioDespesasIndiretas);
                        }
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case DESPESA_CERTIFICADOS:
                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetDespesaCertificado.stream()
                                .filter(desp -> desp.getRefCavalo() == d.getId())
                                .map(DespesaCertificado::getValorDespesa)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (switchIsChecked) {
                            BigDecimal rateioDespesasIndiretas = dataSetDespesaCertificado.stream()
                                    .filter(desp -> desp.getTipoDespesa() == INDIRETA)
                                    .map(DespesaCertificado::getValorDespesa)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            rateioDespesasIndiretas = calculaRateio(rateioDespesasIndiretas);
                            valor = valor.add(rateioDespesasIndiretas);
                        }

                    } else {
                        valor = dataSetDespesaCertificado.stream()
                                .filter(desp -> desp.getRefCavalo() == d.getId())
                                .filter(desp -> desp.getDataDeEmissao().getMonthValue() == mes)
                                .map(DespesaCertificado::getValorDespesa)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (switchIsChecked) {
                            BigDecimal rateioDespesasIndiretas = dataSetDespesaCertificado.stream()
                                    .filter(desp -> desp.getDataDeEmissao().getMonthValue() == mes)
                                    .filter(desp -> desp.getTipoDespesa() == INDIRETA)
                                    .map(DespesaCertificado::getValorDespesa)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            rateioDespesasIndiretas = calculaRateio(rateioDespesasIndiretas);
                            valor = valor.add(rateioDespesasIndiretas);
                        }
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case DESPESA_SEGUROS_DIRETOS:
                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetParcelaSeguro.stream()
                                .filter(p -> p.getRefCavalo() == d.getId())
                                .filter(p -> p.getTipoDespesa() == DIRETA)
                                .filter(ParcelaDeSeguro::isPaga)
                                .map(ParcelaDeSeguro::getValor)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    } else {
                        valor = dataSetParcelaSeguro.stream()
                                .filter(desp -> desp.getRefCavalo() == d.getId())
                                .filter(desp -> desp.getData().getMonthValue() == mes)
                                .filter(desp -> desp.getTipoDespesa() == DIRETA)
                                .map(ParcelaDeSeguro::getValor)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case DESPESA_SEGUROS_INDIRETOS:
                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetParcelaSeguro.stream()
                                .filter(p -> p.getTipoDespesa() == INDIRETA)
                                .filter(ParcelaDeSeguro::isPaga)
                                .map(ParcelaDeSeguro::getValor)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    } else {
                        valor = dataSetParcelaSeguro.stream()
                                .filter(desp -> desp.getData().getMonthValue() == mes)
                                .filter(desp -> desp.getTipoDespesa() == INDIRETA)
                                .map(ParcelaDeSeguro::getValor)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }

                    valor = calculaRateio(valor);
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;

            case DESPESAS_IMPOSTOS:

                for (DetalhesDesempenho d : getListaDetalhes()) {
                    if (mes == MES_DEFAULT.getRef()) {
                        valor = dataSetDespesaImposto.stream()
                                .filter(desp -> desp.getRefCavalo() == d.getId())
                                .map(DespesasDeImposto::getValorDespesa)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (switchIsChecked) {
                            BigDecimal rateioDespesasIndiretas = dataSetDespesaImposto.stream()
                                    .filter(desp -> desp.getTipoDespesa() == INDIRETA)
                                    .map(DespesasDeImposto::getValorDespesa)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            rateioDespesasIndiretas = calculaRateio(rateioDespesasIndiretas);
                            valor = valor.add(rateioDespesasIndiretas);
                        }

                    } else {
                        valor = dataSetDespesaImposto.stream()
                                .filter(desp -> desp.getRefCavalo() == d.getId())
                                .filter(desp -> desp.getData().getMonthValue() == mes)
                                .map(DespesasDeImposto::getValorDespesa)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (switchIsChecked) {
                            BigDecimal rateioDespesasIndiretas = dataSetDespesaImposto.stream()
                                    .filter(desp -> desp.getData().getMonthValue() == mes)
                                    .filter(desp -> desp.getTipoDespesa() == INDIRETA)
                                    .map(DespesasDeImposto::getValorDespesa)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            rateioDespesasIndiretas = calculaRateio(rateioDespesasIndiretas);
                            valor = valor.add(rateioDespesasIndiretas);
                        }

                    }
                    d.setValor(valor);
                    d.adicionaAoSaldoAcumulado(valor);
                }
                break;
        }

        for (DetalhesDesempenho d : getListaDetalhes()) {
            d.definePercentual();
        }

    }

    private static BigDecimal calculaRateio(BigDecimal valor) {
        BigDecimal quantidadeCavalos = new BigDecimal(cavaloSize);
        BigDecimal rateio = valor.divide(quantidadeCavalos, 2, RoundingMode.HALF_EVEN);
        return rateio;
    }

    private static void criaObjetosParaExibicao() {
        detalhesDao = new DetalhesDesempenhoDAO();
        detalhesDao.clear();
        DetalhesDesempenho.resetaAcumulado();

        CavaloDAO cavaloDao = new CavaloDAO();
        List<Cavalo> listaCavalos = cavaloDao.listaTodos();
        cavaloSize = listaCavalos.size();

        for (Cavalo c : listaCavalos) {

            DetalhesDesempenho d = new DetalhesDesempenho();
            String nome = getNome(c);

            d.setPlaca(c.getPlaca());
            d.setNome(nome);
            d.setId(c.getId());
            detalhesDao.adiciona(d);
        }
    }

    private static String getNome(Cavalo cavalo) {

        if (cavalo.getMotorista().getNome() != null) {
            return cavalo.getMotorista().getNome();
        } else {
            return "S/M";
        }

    }


}
