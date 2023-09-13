package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;

@Dao
public interface RoomParcela_seguroVidaDao {

    @Insert
    void adiciona(Parcela_seguroVida parcela);

    @Delete
    void deleta(Parcela_seguroVida parcela);

    @Query("SELECT * FROM Parcela_seguroVida WHERE refSeguroId = :seguroId")
    List<Parcela_seguroVida> listaPeloSeguroId(Long seguroId);

    @Query("SELECT * FROM Parcela_seguroVida")
    List<Parcela_seguroVida> todos();

    @Update
    void substitui(Parcela_seguroVida p);
}
