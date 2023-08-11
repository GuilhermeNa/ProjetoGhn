package br.com.transporte.AppGhn.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.MotoristaDAO;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;

public class DefineMotorista {
    private MotoristaDAO dao = new MotoristaDAO();
    private Motorista motorista;
    private Context context;
    private Cavalo cavalo;
    private String[] motoristas;
    private DefineMotoristaCallback defineMotoristaCallback;
    private String message, motoristaPreExistente;

    public DefineMotorista(Context context, Cavalo cavalo) {
        this.context = context;
        this.cavalo = cavalo;
    }

    public void setDefineMotoristaCallback(DefineMotoristaCallback defineMotoristaCallback){
        this.defineMotoristaCallback = defineMotoristaCallback;
    }

    public void dialogDefineMotorista() {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.define_motorista_xml, null);
        AutoCompleteTextView autoComplete = viewCriada.findViewById(R.id.dialog_define_motorista_auto_complete);

        motoristas = dao.listaNomes().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, motoristas);
        autoComplete.setAdapter(adapter);

        try{
            message = cavalo.getMotorista().getNome();
            motoristaPreExistente = cavalo.getMotorista().getNome().toUpperCase(Locale.ROOT);
        } catch (NullPointerException e) {
            e.getMessage();
            e.printStackTrace();
            message = "Nenhum motorista vinculado";
            motoristaPreExistente = " ";
        }

        new AlertDialog.Builder(context)
                .setTitle(cavalo.getPlaca())
                .setMessage(message)
                .setView(viewCriada)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    if (!dao.listaNomes().contains(autoComplete.getText().toString().toUpperCase(Locale.ROOT))) {
                      defineMotoristaCallback.quandoFalha("Não foi possivel fazer a alteração");
                    } else if(autoComplete.getText().toString().toUpperCase(Locale.ROOT).equals(motoristaPreExistente)){
                        defineMotoristaCallback.quandoFalha("Motorista já está alocado neste cavalo");
                    } else{
                            motorista = dao.localizaPeloNome(autoComplete.getText().toString());
                            cavalo.setMotorista(motorista);
                            defineMotoristaCallback.quandoSucesso();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    public interface DefineMotoristaCallback{
        void quandoFalha(String txt);
        void quandoSucesso();
    }




}
