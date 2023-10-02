package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeSalario;

@Dao
public interface RoomCustosDeSalarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(CustosDeSalario custosDeSalario);

    @Delete
    void deleta(CustosDeSalario custosDeSalario);

    @Update
    void altera(CustosDeSalario custosDeSalario);

    @Query("SELECT * FROM custosdesalario WHERE id = :custoSalarioId")
    LiveData<CustosDeSalario> localizaPeloId(Long custoSalarioId);

    @Query("SELECT * FROM CustosDeSalario")
    LiveData<List<CustosDeSalario>> todos();
}
