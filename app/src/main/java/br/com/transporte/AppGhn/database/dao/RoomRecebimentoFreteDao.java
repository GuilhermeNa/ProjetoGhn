package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.RecebimentoDeFrete;

@Dao
public interface RoomRecebimentoFreteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(RecebimentoDeFrete recebimento);

    @Delete
    void deleta(RecebimentoDeFrete recebimento);

    @Query("SELECT * FROM recebimentoDeFrete")
    List<RecebimentoDeFrete> todos();

    @Query("SELECT * FROM recebimentoDeFrete WHERE id = :recebimentoId")
    RecebimentoDeFrete localizaPeloId(long recebimentoId);

    @Query("SELECT * FROM recebimentoDeFrete WHERE refFreteId = :freteId")
    List<RecebimentoDeFrete> listaPorFreteId(long freteId);

}
