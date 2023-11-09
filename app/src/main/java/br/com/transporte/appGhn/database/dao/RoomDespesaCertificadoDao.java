package br.com.transporte.appGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.model.enums.TipoCertificado;
import br.com.transporte.appGhn.model.enums.TipoDespesa;

@Dao
public interface RoomDespesaCertificadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(DespesaCertificado despesaCertificado);

    @Delete
    void deleta(DespesaCertificado despesaCertificado);

    @Update
    void atualiza(DespesaCertificado despesaCertificado);

    @Query("SELECT * FROM despesacertificado WHERE id = :despesaId")
    LiveData<DespesaCertificado> localizaPeloId(Long despesaId);

    @Query("SELECT * FROM despesaCertificado")
    LiveData<List<DespesaCertificado>> todos();

    @Query("SELECT * FROM despesacertificado WHERE tipoDespesa = :tipo")
    LiveData<List<DespesaCertificado>> listaPorTipo(TipoDespesa tipo);

    @Query("SELECT * FROM despesaCertificado WHERE refCavaloId = :cavaloId")
    LiveData<List<DespesaCertificado>> listaPorCavaloId(Long cavaloId);

    @Query("SELECT * FROM despesacertificado WHERE tipoDespesa = :tipo")
    LiveData<List<DespesaCertificado>> buscaPorTipo(TipoDespesa tipo);

    @Query("SELECT * FROM DespesaCertificado WHERE tipoDespesa = :tipo AND valido = :isValido AND tipoCertificado = :tipoCertificado")
    List<DespesaCertificado> buscaDuplicidadeQuandoDespesaIndireta(TipoDespesa tipo, boolean isValido, TipoCertificado tipoCertificado);

    @Query("SELECT * FROM DespesaCertificado WHERE valido = :isValido AND refCavaloId = :refCavaloId AND tipoCertificado = :tipoCertificado")
    List<DespesaCertificado> buscaDuplicidadeQuandoDespesaDireta(boolean isValido, Long refCavaloId, TipoCertificado tipoCertificado);

    @Query("SELECT * FROM DespesaCertificado")
    List<DespesaCertificado> buscaTodosParaTask();

    @Query("SELECT * FROM DespesaCertificado WHERE refCavaloId = :cavaloId")
    List<DespesaCertificado> buscaPorCavaloIdParaTask(Long cavaloId);


}
