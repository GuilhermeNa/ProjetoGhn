package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

@Dao
public interface RoomDespesaCertificadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesaCertificado despesaCertificado);

    @Delete
    void deleta(DespesaCertificado despesaCertificado);

    @Query("SELECT * FROM despesacertificado WHERE id = :despesaId")
    DespesaCertificado localizaPeloId(Long despesaId);

    @Query("SELECT * FROM despesaCertificado")
    List<DespesaCertificado> todos();

    @Query("SELECT * FROM despesacertificado WHERE tipoDespesa = :tipo")
    List<DespesaCertificado> listaPorTipo(TipoDespesa tipo);

    @Query("SELECT * FROM despesaCertificado WHERE refCavaloId = :cavaloId")
    List<DespesaCertificado> listaPorCavaloId(Long cavaloId);

}
