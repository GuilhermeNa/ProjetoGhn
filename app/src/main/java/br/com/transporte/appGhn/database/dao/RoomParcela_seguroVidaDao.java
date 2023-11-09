package br.com.transporte.appGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;

@Dao
public interface RoomParcela_seguroVidaDao {

    @Insert
    Long adiciona(Parcela_seguroVida parcela);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adicionaTodos(List<Parcela_seguroVida> listaParcelas);

    @Delete
    void deleta(Parcela_seguroVida parcela);

    @Query("SELECT * FROM Parcela_seguroVida WHERE refSeguroId = :seguroId")
    LiveData<List<Parcela_seguroVida>> listaPeloSeguroId(Long seguroId);

    @Query("SELECT * FROM Parcela_seguroVida")
    LiveData<List<Parcela_seguroVida>> todos();

    @Update
    void substitui(Parcela_seguroVida p);

    @Query("SELECT * FROM Parcela_seguroVida")
    List<Parcela_seguroVida> buscaTodosParaTask();

    @Query("SELECT * FROM Parcela_seguroVida WHERE refSeguroId = :cavaloId")
    List<Parcela_seguroVida> buscaPorCavaloIdParaTask(Long cavaloId);

}
