package br.com.transporte.appGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;

@Dao
public interface RoomParcela_seguroFrotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(Parcela_seguroFrota parcela);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adicionaTodos(List<Parcela_seguroFrota> lista);

    @Delete
    void deleta(Parcela_seguroFrota parcela);

    @Update
    void substitui(Parcela_seguroFrota parcela);

    @Query("SELECT * FROM Parcela_seguroFrota WHERE id = :parcelaId")
    LiveData<Parcela_seguroFrota> localizaPeloId(Long parcelaId);

    @Query("SELECT * FROM Parcela_seguroFrota WHERE refSeguroId = :seguroId")
    LiveData<List<Parcela_seguroFrota>> listaPeloSeguroId(Long seguroId);

    @Query("SELECT * FROM Parcela_seguroFrota")
    LiveData<List<Parcela_seguroFrota>> todos();

    @Query("SELECT * FROM Parcela_seguroFrota WHERE refCavaloId = :cavaloId")
    LiveData<List<Parcela_seguroFrota>> listaPeloCavaloId(Long cavaloId);

    @Query("SELECT * FROM Parcela_seguroFrota")
    List<Parcela_seguroFrota> buscaTodosParaTask();

    @Query("SELECT * FROM Parcela_seguroFrota WHERE refCavaloId = :cavaloId")
    List<Parcela_seguroFrota> buscaPorCavaloIdParaTask(Long cavaloId);

    @Query("SELECT * FROM Parcela_seguroFrota WHERE paga")
    List<Parcela_seguroFrota> buscaTodosPagosParaTask();

    @Query("SELECT * FROM Parcela_seguroFrota WHERE refCavaloId = :cavaloId AND paga")
    List<Parcela_seguroFrota> buscaPorCavaloIdEPagasParaTask(Long cavaloId);

}
