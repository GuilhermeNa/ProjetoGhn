package br.com.transporte.AppGhn.filtros;


import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import br.com.transporte.AppGhn.dao.MotoristaDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;

public class FiltraMotorista {
    private final static MotoristaDAO dao = new MotoristaDAO();

    public static Motorista localizaPeloId(int morotistaId) throws ObjetoNaoEncontrado {
        Motorista motoristaLocalizado = null;

        for (Motorista m : dao.listaTodos()) {
            if (m.getId() == morotistaId) {
                motoristaLocalizado = m;
            }
        }

        if (motoristaLocalizado != null) {
            return motoristaLocalizado;
        }

        throw new ObjetoNaoEncontrado(OBJETO_NULL);
    }

}