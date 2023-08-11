package br.com.transporte.AppGhn.ui.database;

import androidx.room.RoomDatabase;

//@Database(entities = {Cavalo.class}, version = 1, exportSchema = false)
public abstract class GhnDatabase extends RoomDatabase {
    public abstract RoomCavaloDAO getRoomCavaloDAO();

}
