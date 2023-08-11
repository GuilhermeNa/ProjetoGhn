package br.com.transporte.AppGhn.dao;

import static br.com.transporte.AppGhn.model.enums.TipoAbastecimento.TOTAL;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.exception.ListaNaoEncontrada;
import br.com.transporte.AppGhn.exception.MarcacaoKmComDataJaRegistrada;
import br.com.transporte.AppGhn.exception.MarcacaoKmInvalida;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;

public class CustosDeAbastecimentoDAO {
    private final static ArrayList<CustosDeAbastecimento> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------


    public void adiciona(CustosDeAbastecimento abastecimento) {
        abastecimento.setId(contadorDeIds);
        dao.add(abastecimento);
        contadorDeIds++;
    }

    public void edita(CustosDeAbastecimento abastecimento) {
        CustosDeAbastecimento abastecimentoEncontrado = localizaPeloId(abastecimento.getId());
        if (abastecimentoEncontrado != null) {
            int posicaoAbastecimento = dao.indexOf(abastecimentoEncontrado);
            dao.set(posicaoAbastecimento, abastecimento);
        }
    }

    public void deleta(int abastecimentoId) {
        CustosDeAbastecimento abastecimentoEncontrado = localizaPeloId(abastecimentoId);
        if (abastecimentoEncontrado != null) {
            dao.remove(abastecimentoEncontrado);
        }
    }

    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<CustosDeAbastecimento> listaTodos() {
        return new ArrayList<>(dao);
    }

    public List<CustosDeAbastecimento> listaCriaFlag(int cavaloId){
        return dao.stream()
                .filter(a -> a.getTipo() == TOTAL)
                .filter(a -> a.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CustosDeAbastecimento> listaFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        List<CustosDeAbastecimento> lista = new ArrayList<>();
        for (CustosDeAbastecimento a : dao) {
            if (!a.getData().isBefore(dataInicial) && !a.getData().isAfter(dataFinal)) {
                lista.add(a);
            }
        }
        return lista;
    }

    public List<CustosDeAbastecimento> listaPorCavalo(int cavaloId) {
        List<CustosDeAbastecimento> lista = new ArrayList<>();
        for (CustosDeAbastecimento c : dao) {
            if (c.getRefCavalo() == cavaloId) {
                lista.add(c);
            }
        }
        return lista;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CustosDeAbastecimento> listaFiltradaPorCavaloEData(int cavaloId, LocalDate dataInicial, LocalDate dataFinal) {
        List<CustosDeAbastecimento> listaPorCavalo = listaPorCavalo(cavaloId);
        List<CustosDeAbastecimento> lista = new ArrayList<>();

        for (CustosDeAbastecimento c : listaPorCavalo) {
            if (!c.getData().isBefore(dataInicial) && !c.getData().isAfter(dataFinal)) {
                lista.add(c);
            }
        }
        return lista;
    }

    //---------------------------------- Outros Metodos ---------------------------------------------


    public CustosDeAbastecimento localizaPeloId(int idAbastecimento) {
        CustosDeAbastecimento abastecimentoLocalizado = null;
        for (CustosDeAbastecimento a : dao) {
            if (a.getId() == idAbastecimento) {
                abastecimentoLocalizado = a;
            }
        }
        return abastecimentoLocalizado;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public BigDecimal calculaKmPercorrido(int cavaloId, LocalDate dataInicial, LocalDate dataFinal) {
        List<CustosDeAbastecimento> listaTodos = listaPorCavalo(cavaloId);
        List<CustosDeAbastecimento> listaFiltrada = listaFiltradaPorCavaloEData(cavaloId, dataInicial, dataFinal);

        Comparator<CustosDeAbastecimento> comparing = Comparator.comparing(CustosDeAbastecimento::getMarcacaoKm);
        Collections.sort(listaTodos, comparing);
        Collections.sort(listaFiltrada, comparing);

        CustosDeAbastecimento dMenos1;
        CustosDeAbastecimento d0;
        CustosDeAbastecimento dMais1;

        if (listaFiltrada.size() == 0) {
            throw new IndexOutOfBoundsException("Lista filtrada não encontrada");
        } else if (listaTodos.size() == 0) {
            throw new IndexOutOfBoundsException("Lista total não encontrada");
        }
        d0 = listaFiltrada.get(0);
        dMais1 = listaFiltrada.get(listaFiltrada.size() - 1);
        int d0PosicaoGeral = listaTodos.indexOf(d0);

        try {
            dMenos1 = listaTodos.get(d0PosicaoGeral - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            dMenos1 = listaTodos.get(d0PosicaoGeral);
        }

        return dMais1.getMarcacaoKm().subtract(dMenos1.getMarcacaoKm());
    }


    //----------------------------------------------------------------------------------------------


    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean getMarcacaoKmDoUltimoAbastecimento(BigDecimal marcacaoAtual, int cavaloId, LocalDate novaData)
            throws MarcacaoKmInvalida, MarcacaoKmComDataJaRegistrada, ListaNaoEncontrada {
        List<CustosDeAbastecimento> listaPorCavalo = listaPorCavalo(cavaloId);
        Comparator<CustosDeAbastecimento> ordenaPorDatas = Comparator.comparing(CustosDeAbastecimento::getData);
        Collections.sort(listaPorCavalo, ordenaPorDatas);
        int sizeLista = listaPorCavalo.size();
        CustosDeAbastecimento ultimoCustoAdicionadoALista = null;

        if (sizeLista == 0) {
            throw new ListaNaoEncontrada("Lista filtrada não encontrada");

        } else if (sizeLista > 0) {
            ultimoCustoAdicionadoALista = listaPorCavalo.get(sizeLista - 1);
            BigDecimal ultimaMarcacaoAdicionada = ultimoCustoAdicionadoALista.getMarcacaoKm();
            LocalDate ultimaDataAdicionada = ultimoCustoAdicionadoALista.getData();


            if (novaData.isAfter(ultimaDataAdicionada)) {
                return true;
                //return ultimoCustoAdicionadoALista.getMarcacaoKm();

            } else if (novaData.isEqual(ultimaDataAdicionada)) {
                int compareRegistroComDatasIguais = marcacaoAtual.compareTo(ultimaMarcacaoAdicionada);
                if (compareRegistroComDatasIguais < 0) {
                    if (sizeLista >= 2) {
                        CustosDeAbastecimento custoAnteriorADuplicacaoDeDatas = listaPorCavalo.get(sizeLista - 2);
                        compareRegistroComDatasIguais = marcacaoAtual.
                                compareTo(custoAnteriorADuplicacaoDeDatas.getMarcacaoKm());
                        if (compareRegistroComDatasIguais < 0) {
                            throw new MarcacaoKmComDataJaRegistrada("Marcação km inferior a do abastecimento anterior a esta data");
                        } else {
                            return true;
                        }
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            }


            if (novaData.isBefore(ultimaDataAdicionada)) {
                CustosDeAbastecimento cMaisUm = null;
                CustosDeAbastecimento cMenosUm = null;
                Collections.reverse(listaPorCavalo);

                for (CustosDeAbastecimento c : listaPorCavalo) {
                    if (c.getData().isAfter(novaData)) {
                        cMaisUm = c;
                    }
                }

                cMenosUm = getCMenosUm(listaPorCavalo, cMaisUm);

                if (novaData.isBefore(cMaisUm.getData()) && novaData.isAfter(cMenosUm.getData())) {
                    BigDecimal marcacaoKmCMaisUm = cMaisUm.getMarcacaoKm();
                    BigDecimal marcacaoKmCmenosUm = cMenosUm.getMarcacaoKm();
                    int compareMenosUm = marcacaoAtual.compareTo(marcacaoKmCmenosUm);
                    int compareMaisUm = marcacaoAtual.compareTo(marcacaoKmCMaisUm);

                    if (compareMenosUm < 0 || compareMaisUm > 0) {
                        throw new MarcacaoKmInvalida("Marcação km invalida");
                    }
                }
            } else if (novaData.isEqual(ultimoCustoAdicionadoALista.getData())) {


            }

        }

        return true;
    }

    private CustosDeAbastecimento getCMenosUm(List<CustosDeAbastecimento> listaPorCavalo, CustosDeAbastecimento cMaisUm) {
        CustosDeAbastecimento cMenosUm;
        int posicaoCMaisUmNaLista = listaPorCavalo.indexOf(cMaisUm);
        cMenosUm = listaPorCavalo.get(posicaoCMaisUmNaLista + 1);
        return cMenosUm;
    }


}
