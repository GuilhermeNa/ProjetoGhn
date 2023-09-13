package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;

@Dao
public interface RoomDespesaSeguroVidaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesaComSeguroDeVida seguroDeVida);

    @Delete
    void deleta(DespesaComSeguroDeVida seguroDeVida);

    @Query("SELECT * FROM despesaComSeguroDeVida " +
            "WHERE id = :seguroId")
    DespesaComSeguroDeVida localizaPeloId(Long seguroId);

    @Query("SELECT * FROM DespesaComSeguroDeVida " +
            "WHERE ID = (SELECT MAX(ID) FROM DespesaComSeguroDeVida) ")
    DespesaComSeguroDeVida ultimoAdicionado();

    @Update
    void substitui(DespesaComSeguroDeVida seguro);

    @Query("SELECT * FROM DespesaComSeguroDeVida WHERE valido = :isValido")
    List<DespesaComSeguroDeVida> listaPorValidade(boolean isValido);

    @Query("SELECT * FROM DespesaComSeguroDeVida")
    List<DespesaComSeguroDeVida> todos();

}
