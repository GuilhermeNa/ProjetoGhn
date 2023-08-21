package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.COMISSAO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.CUSTOS_ABASTECIMENTO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.CUSTOS_MANUTENCAO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.CUSTOS_PERCURSO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_ADM;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_IMPOSTOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_CERTIFICADOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_SEGUROS_DIRETOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_SEGUROS_INDIRETOS;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.dao.CustosDeManutencaoDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.dao.DespesasCertificadoDAO;
import br.com.transporte.AppGhn.dao.DespesasImpostoDAO;
import br.com.transporte.AppGhn.dao.DespesasSeguroDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.dao.SalarioDAO;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.ParcelaDeSeguro;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;

public class BarCharCalculosExtension {
    private static final FreteDAO freteDao = new FreteDAO();
    private static final CustosDeManutencaoDAO custosManutencaoDao = new CustosDeManutencaoDAO();
    private static final CustosDePercursoDAO custosPercursoDao = new CustosDePercursoDAO();
    private static final CustosDeAbastecimentoDAO abastecimentoDao = new CustosDeAbastecimentoDAO();
    private static final DespesasImpostoDAO impostoDao = new DespesasImpostoDAO();
    private static final DespesasAdmDAO despesasAdmDao = new DespesasAdmDAO();
    private static final DespesasCertificadoDAO certificadosDao = new DespesasCertificadoDAO();
    private static final DespesasSeguroDAO seguroDao = new DespesasSeguroDAO();
    private static final ParcelaDeSeguroDAO parcelaDao = new ParcelaDeSeguroDAO();
    private static final CavaloDAO cavaloDao = new CavaloDAO();

    private static final SalarioDAO salarioDao = new SalarioDAO();
    public static final int NAO_ESPECIFICA_BUSCA_POR_ID = 0;


    @NonNull
    public static HashMap<Integer, BigDecimal> getHashMap_ChaveMes_ValorResultado(int ano, @NonNull TipoDeRequisicao tipo, int id) {

        HashMap<Integer, BigDecimal> hashMap = new HashMap<>();

        List<Object> listaDeObjetos;

        List<List<Object>> listaDeListas_Meses = criaListaDeListas();

        switch (tipo) {
            case FRETE_BRUTO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(frete -> ((Frete) frete).getAdmFrete().getFreteBruto())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case FRETE_LIQUIDO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(frete -> ((Frete) frete).getAdmFrete().getFreteLiquidoAReceber())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case COMISSAO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(frete -> ((Frete) frete).getAdmFrete().getComissaoAoMotorista())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;


            case LITRAGEM:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(abastecimento -> ((CustosDeAbastecimento) abastecimento).getQuantidadeLitros())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case CUSTOS_ABASTECIMENTO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(abastecimento -> ((CustosDeAbastecimento) abastecimento).getValorCusto())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case CUSTOS_PERCURSO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(custosPercurso -> ((CustosDePercurso) custosPercurso).getValorCusto())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case CUSTOS_MANUTENCAO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(custosManutencao -> ((CustosDeManutencao) custosManutencao).getValorCusto())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case DESPESAS_ADM:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(despesaAdm -> ((DespesaAdm) despesaAdm).getValorDespesa())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case DESPESAS_IMPOSTOS:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(despesaDeImposto -> ((DespesasDeImposto) despesaDeImposto).getValorDespesa())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case DESPESA_CERTIFICADOS:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(despesaCertificado -> ((DespesaCertificado) despesaCertificado).getValorDespesa())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case DESPESA_SEGUROS_DIRETOS:

            case DESPESA_SEGUROS_INDIRETOS:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(parcela -> ((ParcelaDeSeguro) parcela).getValor())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            //--------------------------------------------------------------------------------------
            //                                  LUCRO LIQUIDO                                     //
            //--------------------------------------------------------------------------------------

            case LUCRO_LIQUIDO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(frete -> ((Frete) frete).getAdmFrete().getFreteLiquidoAReceber())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }

                // ---------
                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, CUSTOS_PERCURSO, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, CUSTOS_PERCURSO);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(custoPercurso -> ((CustosDePercurso) custoPercurso).getValorCusto())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                // ---------
                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, COMISSAO, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, COMISSAO);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(frete -> ((Frete) frete).getAdmFrete().getComissaoAoMotorista())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                // ---------
                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, CUSTOS_MANUTENCAO, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, CUSTOS_MANUTENCAO);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(manutencao -> ((CustosDeManutencao) manutencao).getValorCusto())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                // ---------
                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, CUSTOS_ABASTECIMENTO, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, CUSTOS_ABASTECIMENTO);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(abastecimento -> ((CustosDeAbastecimento) abastecimento).getValorCusto())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                // ---------
                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, DESPESAS_ADM, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, DESPESAS_ADM);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(adm -> ((DespesaAdm) adm).getValorDespesa())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                // ---------
                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, DESPESA_CERTIFICADOS, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, DESPESA_CERTIFICADOS);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(certificado -> ((DespesaCertificado) certificado).getValorDespesa())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                // ---------
                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, DESPESA_SEGUROS_DIRETOS, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, DESPESA_SEGUROS_DIRETOS);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(parcela -> ((ParcelaDeSeguro) parcela).getValor())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                // ---------

                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, DESPESA_SEGUROS_INDIRETOS, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, DESPESA_SEGUROS_INDIRETOS);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(parcela -> ((ParcelaDeSeguro) parcela).getValor())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                // ---------
                listaDeObjetos.clear();
                listaDeListas_Meses.clear();
                listaDeListas_Meses = criaListaDeListas();
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, DESPESAS_IMPOSTOS, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, DESPESAS_IMPOSTOS);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(imposto -> ((DespesasDeImposto) imposto).getValorDespesa())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (soma != null) {
                        BigDecimal resultado = hashMap.get(i).subtract(soma);
                        hashMap.put(i, resultado);
                    }
                }

                break;

        }

        return hashMap;
    }

    @NonNull
    private static List<List<Object>> criaListaDeListas() {
        List<List<Object>> listaSeparadaPorMeses = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            listaSeparadaPorMeses.add(new ArrayList<>());
        }
        return listaSeparadaPorMeses;
    }

    private static void separaOsDadosEmSeusRespectivosMeses(List<Object> listaDeObjetos, List<List<Object>> listaDeListas_Meses, @NonNull TipoDeRequisicao tipo) {

        switch (tipo) {
            case FRETE_BRUTO:
            case FRETE_LIQUIDO:
            case COMISSAO:
            case LUCRO_LIQUIDO:

                for (Object frete : listaDeObjetos) {
                    int mes = ((Frete) frete).getData().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(frete);
                        }
                    }
                }

                break;

            case LITRAGEM:
            case CUSTOS_ABASTECIMENTO:

                for (Object abastecimento : listaDeObjetos) {
                    int mes = ((CustosDeAbastecimento) abastecimento).getData().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(abastecimento);
                        }
                    }
                }
                break;

            case CUSTOS_PERCURSO:
                for (Object custosPercurso : listaDeObjetos) {
                    int mes = ((CustosDePercurso) custosPercurso).getData().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(custosPercurso);
                        }
                    }
                }
                break;

            case CUSTOS_MANUTENCAO:
                for (Object custosManutencao : listaDeObjetos) {
                    int mes = ((CustosDeManutencao) custosManutencao).getData().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(custosManutencao);
                        }
                    }
                }
                break;

            case DESPESAS_ADM:
                for (Object despesaAdm : listaDeObjetos) {
                    int mes = ((DespesaAdm) despesaAdm).getData().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(despesaAdm);
                        }
                    }
                }
                break;

            case DESPESAS_IMPOSTOS:
                for (Object despesaImposto : listaDeObjetos) {
                    int mes = ((DespesasDeImposto) despesaImposto).getData().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(despesaImposto);
                        }
                    }
                }
                break;

            case DESPESA_CERTIFICADOS:
                for (Object despesaCertificado : listaDeObjetos) {
                    int mes = ((DespesaCertificado) despesaCertificado).getDataDeEmissao().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(despesaCertificado);
                        }
                    }
                }
                break;

            case DESPESA_SEGUROS_DIRETOS:

            case DESPESA_SEGUROS_INDIRETOS:
                for (Object parcela : listaDeObjetos) {
                    int mes = ((ParcelaDeSeguro) parcela).getData().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(parcela);
                        }
                    }
                }
                break;
        }
    }


    @NonNull
    private static List<Object> getListaDeDadosFiltradaPeloAnoDesejado(int ano, @NonNull TipoDeRequisicao tipo, int id) {

        switch (tipo) {
            case FRETE_BRUTO:
            case FRETE_LIQUIDO:
            case COMISSAO:
            case LUCRO_LIQUIDO:

                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return freteDao.listaTodos().stream()
                            .filter(frete -> frete.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return freteDao.listaTodos().stream()
                            .filter(frete -> frete.getData().getYear() == ano)
                            .filter(frete -> frete.getRefCavalo() == id)
                            .collect(Collectors.toList());
                }

            case LITRAGEM:
            case CUSTOS_ABASTECIMENTO:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return abastecimentoDao.listaTodos().stream()
                            .filter(abastecimento -> abastecimento.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return abastecimentoDao.listaTodos().stream()
                            .filter(abastecimento -> abastecimento.getData().getYear() == ano)
                            .filter(abastecimento -> abastecimento.getRefCavalo() == id)
                            .collect(Collectors.toList());
                }

            case CUSTOS_PERCURSO:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return custosPercursoDao.listaTodos().stream()
                            .filter(custosDePercurso -> custosDePercurso.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return custosPercursoDao.listaTodos().stream()
                            .filter(custosDePercurso -> custosDePercurso.getData().getYear() == ano)
                            .filter(custosDePercurso -> custosDePercurso.getRefCavalo() == id)
                            .collect(Collectors.toList());
                }

            case CUSTOS_MANUTENCAO:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return custosManutencaoDao.listaTodos().stream()
                            .filter(custosDeManutencao -> custosDeManutencao.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return custosManutencaoDao.listaTodos().stream()
                            .filter(custosDeManutencao -> custosDeManutencao.getData().getYear() == ano)
                            .filter(custosDeManutencao -> custosDeManutencao.getRefCavalo() == id)
                            .collect(Collectors.toList());
                }

            case DESPESAS_ADM:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return despesasAdmDao.listaTodos().stream()
                            .filter(despesaAdm -> despesaAdm.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return despesasAdmDao.listaTodos().stream()
                            .filter(despesaAdm -> despesaAdm.getData().getYear() == ano)
                            .filter(despesaAdm -> despesaAdm.getRefCavalo() == id)
                            .collect(Collectors.toList());
                }

            case DESPESAS_IMPOSTOS:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return impostoDao.listaTodos().stream()
                            .filter(despesaDeImposto -> despesaDeImposto.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return impostoDao.listaTodos().stream()
                            .filter(despesaDeImposto -> despesaDeImposto.getData().getYear() == ano)
                            .filter(despesaDeImposto -> despesaDeImposto.getRefCavalo() == id)
                            .collect(Collectors.toList());
                }

            case DESPESA_CERTIFICADOS:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return certificadosDao.listaTodos().stream()
                            .filter(despesaCertificado -> despesaCertificado.getDataDeEmissao().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return certificadosDao.listaTodos().stream()
                            .filter(despesaCertificado -> despesaCertificado.getDataDeEmissao().getYear() == ano)
                            .filter(despesaCertificado -> despesaCertificado.getRefCavalo() == id)
                            .collect(Collectors.toList());
                }

            case DESPESA_SEGUROS_DIRETOS:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return parcelaDao.listaTodos().stream()
                            .filter(p -> p.getTipoDespesa() == DIRETA)
                            .filter(p -> p.getData().getYear() == ano)
                            .filter(Parcela::isPaga)
                            .collect(Collectors.toList());
                } else {
                    return parcelaDao.listaParcelasDoCavalo(id).stream()
                            .filter(p -> p.getTipoDespesa() == DIRETA)
                            .filter(p -> p.getData().getYear() == ano)
                            .filter(Parcela::isPaga)
                            .collect(Collectors.toList());

                }

            case DESPESA_SEGUROS_INDIRETOS:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return parcelaDao.listaTodos().stream()
                            .filter(parcelaDeSeguro -> parcelaDeSeguro.getTipoDespesa() == INDIRETA)
                            .filter(parcelaDeSeguro -> parcelaDeSeguro.getData().getYear() == ano)
                            .filter(Parcela::isPaga)
                            .collect(Collectors.toList());
                }
        }

        return null;
    }
}

