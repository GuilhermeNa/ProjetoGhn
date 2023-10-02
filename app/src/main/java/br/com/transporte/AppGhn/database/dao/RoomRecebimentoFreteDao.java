package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.RecebimentoDeFrete;

@Dao
public interface RoomRecebimentoFreteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(RecebimentoDeFrete recebimento);

    @Delete
    void deleta(RecebimentoDeFrete recebimento);

    @Update
    void substitui(RecebimentoDeFrete recebimento);

    @Query("SELECT * FROM recebimentoDeFrete")
    LiveData<List<RecebimentoDeFrete>> todos();

    @Query("SELECT * FROM recebimentoDeFrete WHERE id = :recebimentoId")
    LiveData<RecebimentoDeFrete> localizaPeloId(Long recebimentoId);

    @Query("SELECT * FROM recebimentoDeFrete WHERE refFreteId = :freteId")
    LiveData<List<RecebimentoDeFrete>> listaPorFreteId(Long freteId);

    @Query("SELECT * FROM recebimentoDeFrete WHERE tipoRecebimentoFrete = :tipo")
    LiveData<List<RecebimentoDeFrete>> listaPorTipo(String tipo);



}
