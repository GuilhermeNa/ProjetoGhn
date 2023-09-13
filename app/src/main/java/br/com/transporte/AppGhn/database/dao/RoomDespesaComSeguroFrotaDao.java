package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;

@Dao
public interface RoomDespesaComSeguroFrotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesaComSeguroFrota seguroFrota);

    @Delete
    void deleta(DespesaComSeguroFrota seguroFrota);

    @Update
    void substitui(DespesaComSeguroFrota seguroFrota);

    @Query("SELECT * FROM despesaComSeguroFrota WHERE id = :seguroId")
    DespesaComSeguroFrota localizaPeloId(Long seguroId);

    @Query("SELECT * FROM DespesaComSeguroFrota")
    List<DespesaComSeguroFrota> todos();

    @Query("SELECT * FROM DespesaComSeguroFrota WHERE valido = :isValido")
    List<DespesaComSeguroFrota> listaPorValidade(boolean isValido);

    @Query("SELECT * FROM DespesaComSeguroFrota " +
            "WHERE ID = (SELECT MAX(ID) FROM DespesaComSeguroFrota) ")
    DespesaComSeguroDeVida ultimoAdicionado();

}
