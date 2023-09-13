package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;

@Dao
public interface RoomParcela_seguroFrotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(Parcela_seguroFrota parcela);

    @Delete
    void deleta(Parcela_seguroFrota parcela);

    @Update
    void substitui(Parcela_seguroFrota parcela);

    @Query("SELECT * FROM Parcela_seguroFrota WHERE id = :parcelaId")
    Parcela_seguroFrota localizaPeloId(Long parcelaId);

    @Query("SELECT * FROM Parcela_seguroFrota WHERE refSeguroId = :seguroId")
    List<Parcela_seguroFrota> listaPeloSeguroId(Long seguroId);

    @Query("SELECT * FROM Parcela_seguroFrota")
    List<Parcela_seguroFrota> todos();

    @Query("SELECT * FROM Parcela_seguroFrota WHERE refCavaloId = :cavaloId")
    List<Parcela_seguroFrota> listaPeloCavaloId(Long cavaloId);
}
