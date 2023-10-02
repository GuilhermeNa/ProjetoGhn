package br.com.transporte.AppGhn.repository;

import android.content.Context;

public class LoginRepository {
    public static final String CHAVE_LOGADO = "LOGADO";


    public LoginRepository(Context context) {

    }

    //----------------------------------------------------------------------------------------------

    public void login() {
       /* dataStore.edit()
                .putBoolean(CHAVE_LOGADO, true)
                .apply();*/
    }

    public boolean estaLogado() {
/*        return preferences.getBoolean(CHAVE_LOGADO, false);*/
        return true;
    }


}
