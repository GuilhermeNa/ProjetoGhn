package br.com.transporte.AppGhn.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.model.abstracts.Frota;

@TypeConverters({
        ConversorBigDecimal.class,
        ConversorLocalDate.class,
        ConversorMotorista.class
})
@Database(entities = {
        Frota.class,
        Cavalo.class,
        SemiReboque.class,
        Motorista.class
}, version = 1, exportSchema = true)
public abstract class GhnDataBase extends RoomDatabase {
    private static final String GHN_DB = "ghn.db";
    private static GhnDataBase instance = null;

    //----------------------------------------------------------------------------------------------

    @NonNull
    public static GhnDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, GhnDataBase.class, GHN_DB)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------------

    public abstract RoomCavaloDao getRoomCavaloDao();

    public abstract RoomMotoristaDao getRoomMotoristaDao();
}
