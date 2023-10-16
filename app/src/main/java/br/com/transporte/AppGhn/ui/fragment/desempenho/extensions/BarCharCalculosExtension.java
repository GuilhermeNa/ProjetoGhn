package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.COMISSAO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.CUSTOS_ABASTECIMENTO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.CUSTOS_MANUTENCAO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.CUSTOS_PERCURSO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_ADM;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_IMPOSTOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_CERTIFICADOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_SEGURO_FROTA;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_SEGURO_VIDA;

import android.content.Context;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaComSeguroFrotaDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroVidaDao;
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

public class BarCharCalculosExtension {
    private final RoomFreteDao freteDao;
    private final RoomCustosDeManutencaoDao custosManutencaoDao;
    private final RoomCustosPercursoDao custosPercursoDao;
    private final RoomCustosAbastecimentoDao abastecimentoDao;
    private final RoomDespesaImpostoDao impostoDao;
    private final RoomDespesaAdmDao despesasAdmDao;
    private final RoomDespesaCertificadoDao certificadosDao;
    private final RoomDespesaComSeguroFrotaDao seguroFrotaDao;
    private final RoomDespesaSeguroVidaDao seguroVidaDao;
    private final RoomParcela_seguroFrotaDao parcela_seguroFrotaDao;
    private final RoomParcela_seguroVidaDao parcela_seguroVidaDao;
    private final RoomCavaloDao cavaloDao;
    private final RoomCustosDeSalarioDao salarioDao;
    public static final Long NAO_ESPECIFICA_BUSCA_POR_ID = 0L;

    public BarCharCalculosExtension(Context context) {
        GhnDataBase dataBase = GhnDataBase.getInstance(context);
        freteDao = dataBase.getRoomFreteDao();
        custosManutencaoDao = dataBase.getRoomCustosDeManutencaoDao();
        custosPercursoDao = dataBase.getRoomCustosPercursoDao();
        abastecimentoDao = dataBase.getRoomCustosAbastecimentoDao();
        impostoDao = dataBase.getRoomDespesaImpostoDao();
        despesasAdmDao = dataBase.getRoomDespesaAdmDao();
        certificadosDao = dataBase.getRoomDespesaCertificadoDao();
        seguroFrotaDao = dataBase.getRoomDespesaComSeguroFrotaDao();
        seguroVidaDao = dataBase.getRoomDespesaSeguroVidaDao();
        parcela_seguroFrotaDao = dataBase.getRoomParcela_seguroFrotaDao();
        parcela_seguroVidaDao = dataBase.getRoomParcela_seguroVidaDao();
        cavaloDao = dataBase.getRoomCavaloDao();
        salarioDao = dataBase.getRoomCustosDeSalarioDao();
    }

    @NonNull
    public HashMap<Integer, BigDecimal> getHashMap_ChaveMes_ValorResultado(int ano, @NonNull TipoDeRequisicao tipo, Long id) {

        HashMap<Integer, BigDecimal> hashMap = new HashMap<>();

        List<Object> listaDeObjetos;

        List<List<Object>> listaDeListas_Meses = criaListaDeListas();

        switch (tipo) {
            case FRETE_BRUTO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(frete -> ((Frete) frete).getFreteBruto())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case FRETE_LIQUIDO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(frete -> ((Frete) frete).getFreteLiquidoAReceber())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case COMISSAO:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(frete -> ((Frete) frete).getComissaoAoMotorista())
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

            case DESPESA_SEGURO_FROTA:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(parcela -> ((Parcela_seguroFrota) parcela).getValor())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hashMap.put(i, soma);
                }
                break;

            case DESPESA_SEGURO_VIDA:
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, tipo, id);

                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, tipo);

                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(parcela -> ((Parcela_seguroVida) parcela).getValor())
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
                            .map(frete -> ((Frete) frete).getFreteLiquidoAReceber())
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
                            .map(frete -> ((Frete) frete).getComissaoAoMotorista())
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
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, DESPESA_SEGURO_FROTA, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, DESPESA_SEGURO_FROTA);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(parcela -> ((Parcela_seguroFrota) parcela).getValor())
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
                listaDeObjetos = getListaDeDadosFiltradaPeloAnoDesejado(ano, DESPESA_SEGURO_VIDA, id);
                separaOsDadosEmSeusRespectivosMeses(listaDeObjetos, listaDeListas_Meses, DESPESA_SEGURO_VIDA);
                for (int i = 0; i < 12; i++) {
                    BigDecimal soma = listaDeListas_Meses.get(i).stream()
                            .map(parcela -> ((Parcela_seguroVida) parcela).getValor())
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

    private void separaOsDadosEmSeusRespectivosMeses(List<Object> listaDeObjetos, List<List<Object>> listaDeListas_Meses, @NonNull TipoDeRequisicao tipo) {

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

            case DESPESA_SEGURO_FROTA:
                for (Object parcela : listaDeObjetos) {
                    int mes = ((Parcela_seguroFrota) parcela).getData().getMonthValue();

                    for (int i = 0; i < 12; i++) {
                        if (mes == i + 1) {
                            listaDeListas_Meses.get(i).add(parcela);
                        }
                    }
                }

                break;

            case DESPESA_SEGURO_VIDA:
                for (Object parcela : listaDeObjetos) {
                    int mes = ((Parcela_seguroVida) parcela).getData().getMonthValue();

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
    private List<Object> getListaDeDadosFiltradaPeloAnoDesejado(int ano, @NonNull TipoDeRequisicao tipo, Long id) {

        switch (tipo) {
            case FRETE_BRUTO:
            case FRETE_LIQUIDO:
            case COMISSAO:
            case LUCRO_LIQUIDO:

           /*     if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return freteDao.todos().stream()
                            .filter(frete -> frete.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return freteDao.todos().stream()
                            .filter(frete -> frete.getData().getYear() == ano)
                            .filter(frete -> frete.getRefCavaloId() == id)
                            .collect(Collectors.toList());
                }

            case LITRAGEM:
            case CUSTOS_ABASTECIMENTO:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return abastecimentoDao.todos().stream()
                            .filter(abastecimento -> abastecimento.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return abastecimentoDao.todos().stream()
                            .filter(abastecimento -> abastecimento.getData().getYear() == ano)
                            .filter(abastecimento -> abastecimento.getRefCavaloId() == id)
                            .collect(Collectors.toList());
                }

            case CUSTOS_PERCURSO:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return custosPercursoDao.todos().stream()
                            .filter(custosDePercurso -> custosDePercurso.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return custosPercursoDao.todos().stream()
                            .filter(custosDePercurso -> custosDePercurso.getData().getYear() == ano)
                            .filter(custosDePercurso -> custosDePercurso.getRefCavaloId() == id)
                            .collect(Collectors.toList());
                }*/

            case CUSTOS_MANUTENCAO:
              /*  if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return custosManutencaoDao.todos().stream()
                            .filter(custosDeManutencao -> custosDeManutencao.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return custosManutencaoDao.todos().stream()
                            .filter(custosDeManutencao -> custosDeManutencao.getData().getYear() == ano)
                            .filter(custosDeManutencao -> custosDeManutencao.getRefCavaloId() == id)
                            .collect(Collectors.toList());
                }*/

            case DESPESAS_ADM:
           /*     if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return despesasAdmDao.buscaTodos().stream()
                            .filter(despesaAdm -> despesaAdm.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return despesasAdmDao.buscaTodos().stream()
                            .filter(despesaAdm -> despesaAdm.getData().getYear() == ano)
                            .filter(despesaAdm -> despesaAdm.getRefCavaloId() == id)
                            .collect(Collectors.toList());
                }*/

            case DESPESAS_IMPOSTOS:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
               /*     return impostoDao.todos().stream()
                            .filter(despesaDeImposto -> despesaDeImposto.getData().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return impostoDao.todos().stream()
                            .filter(despesaDeImposto -> despesaDeImposto.getData().getYear() == ano)
                            .filter(despesaDeImposto -> despesaDeImposto.getRefCavaloId() == id)
                            .collect(Collectors.toList());*/
                }

            case DESPESA_CERTIFICADOS:
                if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
             /*       return certificadosDao.todos().stream()
                            .filter(despesaCertificado -> despesaCertificado.getDataDeEmissao().getYear() == ano)
                            .collect(Collectors.toList());
                } else {
                    return certificadosDao.todos().stream()
                            .filter(despesaCertificado -> despesaCertificado.getDataDeEmissao().getYear() == ano)
                            .filter(despesaCertificado -> despesaCertificado.getRefCavaloId() == id)
                            .collect(Collectors.toList());*/
                }

            case DESPESA_SEGURO_FROTA:
            /*    if (id == NAO_ESPECIFICA_BUSCA_POR_ID) {
                    return parcela_seguroFrotaDao.todos().stream()
                            .filter(p -> p.getTipoDespesa() == DIRETA)
                            .filter(p -> p.getData().getYear() == ano)
                            .filter(Parcela::isPaga)
                            .collect(Collectors.toList());
                } else {
                    return parcela_seguroFrotaDao.listaPeloCavaloId(id).stream()
                            .filter(p -> p.getTipoDespesa() == DIRETA)
                            .filter(p -> p.getData().getYear() == ano)
                            .filter(Parcela::isPaga)
                            .collect(Collectors.toList());
                }*/

            case DESPESA_SEGURO_VIDA:
             /*   return parcela_seguroVidaDao.todos().stream()
                        .filter(parcelaDeSeguro -> parcelaDeSeguro.getData().getYear() == ano)
                        .filter(Parcela::isPaga)
                        .collect(Collectors.toList());*/
        }

        return null;
    }
}

