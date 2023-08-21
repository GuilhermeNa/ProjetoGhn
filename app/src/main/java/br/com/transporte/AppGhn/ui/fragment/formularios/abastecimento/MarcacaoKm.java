package br.com.transporte.AppGhn.ui.fragment.formularios.abastecimento;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.exception.DataInvalida;
import br.com.transporte.AppGhn.exception.MarcacaoKmInvalida;
import br.com.transporte.AppGhn.exception.RegistroDuplicado;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.util.DataUtil;

public abstract class MarcacaoKm {
    private static final CustosDeAbastecimentoDAO abastecimentoDao = new CustosDeAbastecimentoDAO();
    private static LocalDate ultimaData;
    private static BigDecimal ultimaMarcacao;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean verificaMarcacaoKm(LocalDate dataASalvar, BigDecimal marcacaoASalvar, int cavaloId) throws MarcacaoKmInvalida, DataInvalida, RegistroDuplicado {
        List<CustosDeAbastecimento> listaPorCavalo = abastecimentoDao.listaPorCavalo(cavaloId);
        Comparator<CustosDeAbastecimento> ordenaPorDatas = Comparator.comparing(CustosDeAbastecimento::getData);
        Collections.sort(listaPorCavalo, ordenaPorDatas);
        List<CustosDeAbastecimento> listaRevertida = new ArrayList<>(listaPorCavalo);
        Collections.reverse(listaRevertida);
        int listaSize = listaPorCavalo.size();


        Comparator<CustosDeAbastecimento> ordenaPorDatas1 = Comparator.comparing(CustosDeAbastecimento::getMarcacaoKm);
        Collections.sort(listaPorCavalo, ordenaPorDatas1);


        boolean teste0 = verificaSeADataEhValida(dataASalvar); //----------------------------------- verificação funcionando
        if (teste0) return true;

        boolean teste1 = verificaSeListaEstaVazia(listaSize);  //----------------------------------- verificação funcionando
        if (teste1) return true;

        localizaUltimoCadastroDeAbastecimento(listaPorCavalo, listaSize);

        boolean teste2 = verificaSeDataEstaOk(dataASalvar, marcacaoASalvar);    //------------------ verificação funcionando
        if (teste2) return true;

        boolean teste3 = verificaSeDatasSaoIguais(dataASalvar, marcacaoASalvar, listaPorCavalo);   //---verificação funcionando
        if (teste3) return true;


        boolean teste4 = verificaSeDataEstaInferior(dataASalvar, marcacaoASalvar, listaRevertida);  //verificação funcionando
        if (teste4) return true;

        return false;
    }


    private static void localizaUltimoCadastroDeAbastecimento(List<CustosDeAbastecimento> listaPorCavalo, int listaSize) {
        CustosDeAbastecimento ultimoCusto = listaPorCavalo.get(listaSize - 1);
        ultimaMarcacao = ultimoCusto.getMarcacaoKm();
        ultimaData = ultimoCusto.getData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean verificaSeADataEhValida(LocalDate dataASalvar)
            throws DataInvalida {
        LocalDate dataDeHoje = DataUtil.capturaDataDeHojeParaConfiguracaoinicial();
        if (dataASalvar.isAfter(dataDeHoje)) {
            throw new DataInvalida("Data X");
        }

        return false;
    }

    private static boolean verificaSeListaEstaVazia(int listaSize) {
        return listaSize == 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean verificaSeDataEstaOk(LocalDate dataASalvar, BigDecimal marcacaoASalvar)
            throws MarcacaoKmInvalida {
        if (dataASalvar.isAfter(ultimaData)) {
            int compare = marcacaoASalvar.compareTo(ultimaMarcacao);

            switch (compare) {
                case 1:
                    return true;

                case 0:
                    throw new MarcacaoKmInvalida("Data Ok, Km =");

                case -1:
                    throw new MarcacaoKmInvalida("Data Ok, Km X");
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean verificaSeDatasSaoIguais(LocalDate dataASalvar, BigDecimal marcacaoASalvar, List<CustosDeAbastecimento> lista)
            throws RegistroDuplicado, MarcacaoKmInvalida {
        if (dataASalvar.isEqual(ultimaData)) {
            int compare = marcacaoASalvar.compareTo(ultimaMarcacao);

            switch (compare) {
                case 1:
                    return true;

                case 0:
                    throw new RegistroDuplicado("Data =, Km =");
                case -1:
                    if (lista.size() < 2) {
                        return true;
                    } else {
                        CustosDeAbastecimento custoMenosDois = lista.get(lista.size() - 2);
                        BigDecimal cMDoisMarcacao = custoMenosDois.getMarcacaoKm();
                        compare = marcacaoASalvar.compareTo(cMDoisMarcacao);

                        switch (compare) {
                            case 1:
                                return true;
                            case 0:
                                throw new MarcacaoKmInvalida("Data =, Km X, -1 =");
                            case -1:
                                throw new MarcacaoKmInvalida("Data =, Km X, -1 x");
                        }
                    }
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean verificaSeDataEstaInferior(LocalDate dataASalvar, BigDecimal marcacaoASalvar,
                                                      List<CustosDeAbastecimento> listaPorCavalo)
            throws MarcacaoKmInvalida, RegistroDuplicado {
        Comparator<CustosDeAbastecimento> ordenaPorMarcacaoKm = Comparator.comparing(CustosDeAbastecimento::getMarcacaoKm);
        Collections.sort(listaPorCavalo, ordenaPorMarcacaoKm);
        CustosDeAbastecimento custoAbaixo = null;
        CustosDeAbastecimento custoAcima = null;

        custoAbaixo = definePosicaoAbaixoNaLista(dataASalvar, listaPorCavalo);
        custoAcima = definePosicaoAcimaNaLista(listaPorCavalo, custoAbaixo);


        Log.d("teste", " ");
        Log.d("teste", "Lista ");
        for (CustosDeAbastecimento c : listaPorCavalo) {
            Log.d("teste", "pos: " + listaPorCavalo.indexOf(c) + ", data: " + c.getData() + ", km: " + c.getMarcacaoKm());
        }
        try {
            Log.d("teste", "custoAbaixo: " + custoAbaixo.getData());
        } catch (NullPointerException e) {
            Log.d("teste", "custoAbaixo: null");
        }
        Log.d("teste", "custoAcima: " + custoAcima.getData());


        if (custoAbaixo == null) {                                                                  //caso 2.1 -> funcionando
            if (dataASalvar.isBefore(custoAcima.getData())) {
                int compare = marcacaoASalvar.compareTo(custoAcima.getMarcacaoKm());
                switch (compare) {
                    case -1:
                        return true;

                    case 0:
                        throw new MarcacaoKmInvalida("Encontra X, Data ok, Km = acima");

                    case 1:
                        throw new MarcacaoKmInvalida("Encontra X, Data ok, Km x acima");
                }

            } else if (dataASalvar.isEqual(custoAcima.getData())) {                                 //caso 2.2
                int compare = marcacaoASalvar.compareTo(custoAcima.getMarcacaoKm());
                switch (compare) {
                    case -1:
                        return true;

                    case 0:
                        throw new RegistroDuplicado("Encontra X, Data =, Km = acima");

                    case 1:
                        CustosDeAbastecimento custoMenosDois = listaPorCavalo.get(1);
                        BigDecimal cMDoisMarcacao = custoMenosDois.getMarcacaoKm();
                        compare = marcacaoASalvar.compareTo(cMDoisMarcacao);

                        switch (compare) {
                            case -1:
                                return true;
                            case 0:
                                throw new MarcacaoKmInvalida("Encontra X, Data =, Km = acima, -1 = ");
                            case 1:
                                throw new MarcacaoKmInvalida("Encontra X, Data =, Km = acima, -1 x ");
                        }
                        return true;
                }
            }
        } else {
            if (dataASalvar.isAfter(custoAbaixo.getData()) && dataASalvar.isBefore(custoAcima.getData())) {
                int compareAbaixo = marcacaoASalvar.compareTo(custoAbaixo.getMarcacaoKm());
                int compareAcima = marcacaoASalvar.compareTo(custoAcima.getMarcacaoKm());
                int compare = compareAbaixo + compareAcima;

                if (compare == 0) {
                    return true;
                } else {
                    throw new MarcacaoKmInvalida("Encontra OK, Data OK, Km X");
                }


            } else {
                CustosDeAbastecimento custoDuasPosAcima = defineDuasPosicoesAcimaNaLista(listaPorCavalo, custoAbaixo);
                int compareAbaixo = marcacaoASalvar.compareTo(custoAbaixo.getMarcacaoKm());
                int compareAcima = marcacaoASalvar.compareTo(custoAcima.getMarcacaoKm());
                int compareDoisAcima = marcacaoASalvar.compareTo(custoDuasPosAcima.getMarcacaoKm());
                int compare = compareAbaixo + compareAcima + compareDoisAcima;

                if (compare == 1 || compare == -1){
                    return true;
                } else if (compare == 0){
                    throw new RegistroDuplicado("Encontra OK, Data =, Km =");
                } else {
                    throw new MarcacaoKmInvalida("Encontra OK, Data =, Km X");
                }
            }
        }
        return false;
    }

    private static CustosDeAbastecimento definePosicaoAcimaNaLista(List<CustosDeAbastecimento> listaPorCavalo, CustosDeAbastecimento custoAbaixo) {
        CustosDeAbastecimento custoAcima;
        if (custoAbaixo == null) {
            custoAcima = listaPorCavalo.get(0);
        } else {
            int i = listaPorCavalo.indexOf(custoAbaixo);
            custoAcima = listaPorCavalo.get(i + 1);
        }
        return custoAcima;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static CustosDeAbastecimento definePosicaoAbaixoNaLista(LocalDate dataASalvar, List<CustosDeAbastecimento> listaPorCavalo) {
        CustosDeAbastecimento custoAbaixo = null;
        for (CustosDeAbastecimento c : listaPorCavalo) {
            if (dataASalvar.isAfter(c.getData())) {
                custoAbaixo = c;
            }
        }
        return custoAbaixo;
    }

    private static CustosDeAbastecimento defineDuasPosicoesAcimaNaLista(List<CustosDeAbastecimento> listaPorCavalo, CustosDeAbastecimento custoAbaixo) {
        CustosDeAbastecimento custoDuasPosAcima;
        int i = listaPorCavalo.indexOf(custoAbaixo);
        custoDuasPosAcima = listaPorCavalo.get(i + 2);
        return custoDuasPosAcima;
    }
}
