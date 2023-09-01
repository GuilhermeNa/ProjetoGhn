package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class DespesasCertificadoDAO {
    private final static ArrayList<DespesaCertificado> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------


 /*   public void adiciona(DespesaCertificado certificado){
        dao.add(certificado);
        certificado.setId(contadorDeIds);
        contadorDeIds++;
    }

    public void edita(DespesaCertificado certificado) {
        DespesaCertificado certificadoEncontrado = localizaPeloId(certificado.getId());
        if(certificadoEncontrado != null){
            int posicaoCertificado = dao.indexOf(certificadoEncontrado);
            dao.set(posicaoCertificado, certificado);
        }
    }

    public void deleta(int idCertificado) {
        DespesaCertificado certificadoLocalizado = localizaPeloId(idCertificado);
        if(certificadoLocalizado != null){
            dao.remove(certificadoLocalizado);
        }
    }*/


    //---------------------------------- Retorna Listas ---------------------------------------------

    public List<DespesaCertificado> listaTodos(){
        return new ArrayList<>(dao);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<DespesaCertificado> listaFiltradaPorCavalo(int cavaloId) {
        return dao.stream()
                .filter(c -> c.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<DespesaCertificado> listaTodosOsCertificadosIndiretos() {
        List<DespesaCertificado> listaInterna;

        listaInterna = dao.stream()
                .filter(c -> c.getTipoDespesa() == TipoDespesa.INDIRETA)
                .collect(Collectors.toList());

        return listaInterna;
    }


    //---------------------------------- Outros Metodos ---------------------------------------------


    public DespesaCertificado localizaPeloId(int idCertificado) {
        DespesaCertificado certificadoLocalizado = null;
        for (DespesaCertificado c : dao) {
            if (c.getId() == idCertificado) {
                certificadoLocalizado = c;
            }
        }
        return certificadoLocalizado;
    }


}
