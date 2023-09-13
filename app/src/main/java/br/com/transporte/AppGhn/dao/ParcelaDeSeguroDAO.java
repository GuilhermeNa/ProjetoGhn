package br.com.transporte.AppGhn.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;

public class ParcelaDeSeguroDAO {
    private static final List<Parcela_seguroFrota> dao = new ArrayList<>();
    private static int contadorDeIds = 1;

  /*  public void adiciona(@NonNull ParcelaDeSeguro parcela){
        parcela.setId(contadorDeIds);
        dao.add(parcela);
        contadorDeIds++;
    }

    public void edita(@NonNull ParcelaDeSeguro parcela){
        Optional<ParcelaDeSeguro> parcelaOptional = localizaPeloId(parcela.getId());
        parcelaOptional.ifPresent(p -> {
            int posicao = dao.indexOf(p);
            dao.set(posicao, parcela);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void delete(int parcelaId){
        Optional<ParcelaDeSeguro> parcelaOptional = localizaPeloId(parcelaId);
        parcelaOptional.ifPresent(dao::remove);
    }*/

    public List<Parcela_seguroFrota> listaTodos() {
        return new ArrayList<>(dao);
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    public List<Parcela_seguroFrota> listaParcelasDoSeguro(Long id) {
        return dao.stream()
                .filter(p -> p.getRefSeguro() == id)
                .collect(Collectors.toList());
    }

    public List<Parcela_seguroFrota> listaParcelasDoCavalo(Long id) {
        return dao.stream()
                .filter(p -> p.getRefCavaloId() == id)
                .collect(Collectors.toList());
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    private Optional<Parcela_seguroFrota> localizaPeloId(Long parcelaId){
        return dao.stream()
                .filter(p -> p.getId() == parcelaId)
                .findAny();
    }

}
