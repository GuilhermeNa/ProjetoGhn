package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

@Dao
public interface RoomDespesaSeguroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesaComSeguro seguro);

    @Delete
    void deleta(DespesaComSeguro seguro);

    @Query("SELECT * FROM despesacomseguro WHERE id = :seguroId")
    DespesaComSeguro localizaPeloId(Long seguroId);

    @Query("SELECT * FROM DespesaComSeguro WHERE tipoDespesa = :tipo")
    List<DespesaComSeguroFrota> listaPorSeguroFrota(TipoDespesa tipo);

/*    @Query("SELECT * FROM DespesaComSeguro WHERE tipoDespesa = :tipo")
    List<DespesaComSeguroDeVida> listaPorSeguroVida(TipoDespesa tipo);*/
}
