package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeSalario;

@Dao
public interface RoomCustosDeSalarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(CustosDeSalario custosDeSalario);

    @Delete
    void deleta(CustosDeSalario custosDeSalario);

    @Query("SELECT * FROM custosdesalario WHERE id = :custoSalarioId")
    CustosDeSalario localizaPeloId(Long custoSalarioId);

    @Query("SELECT * FROM CustosDeSalario")
    List<CustosDeSalario> todos();
}
