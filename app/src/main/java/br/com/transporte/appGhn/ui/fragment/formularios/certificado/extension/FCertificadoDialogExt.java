package br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension;


import android.app.AlertDialog;
import android.content.Context;

public class FCertificadoDialogExt {
    public static final String SIM = "Sim";
    public static final String NAO = "Não";
    private final Context context;
    private final String TITLE;
    private final String MESSAGE;

    public FCertificadoDialogExt(Context context, String title, String message) {
        this.context = context;
        TITLE = title;
        MESSAGE = message;
    }

    public interface DialogCallback {
        void onPositiveClick();
    }

    //----------------------------------------------------------------------------------------------

    public void run(final DialogCallback callback){
        new AlertDialog.Builder(context).
                setTitle(TITLE).
                setMessage(MESSAGE).
                setPositiveButton(SIM, (dialog, which) -> callback.onPositiveClick()).
                setNegativeButton(NAO, null).
                show();
    }

}
