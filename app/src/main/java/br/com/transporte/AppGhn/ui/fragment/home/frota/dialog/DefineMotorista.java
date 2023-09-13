package br.com.transporte.AppGhn.ui.fragment.home.frota.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.filtros.FiltraMotorista;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.tasks.cavalo.AtualizaCavaloTask;

public class DefineMotorista {
    public static final String MSG_DE_FALHA = "Não foi possivel fazer a alteração";
    public static final String MSG_DE_SOBREPOSICAO = "Motorista já está alocado neste cavalo";
    private final RoomCavaloDao cavaloDao;
    private List<Motorista> dataSet_motorista;
    private List<String> listaDeNomes;
    private Motorista motorista;
    private final Context context;
    private final Cavalo cavalo;
    private DefineMotoristaCallback defineMotoristaCallback;
    private String nomeMotorista, motoristaPreExistente;
    private final RoomMotoristaDao motoristaDao;

    public DefineMotorista(List<Motorista> dataSetMotorista, Context context, Cavalo cavalo) {
        this.dataSet_motorista = dataSetMotorista;
        this.context = context;
        this.cavalo = cavalo;
        GhnDataBase dataBase = GhnDataBase.getInstance(context);
        motoristaDao = dataBase.getRoomMotoristaDao();
        cavaloDao = dataBase.getRoomCavaloDao();
    }

    public void setDefineMotoristaCallback(DefineMotoristaCallback defineMotoristaCallback) {
        this.defineMotoristaCallback = defineMotoristaCallback;
    }

    public interface DefineMotoristaCallback {
        void quandoFalha(String txt);

        void quandoSucesso();
    }

    //----------------------------------------------------------------------------------------------
    //                                            Dialog                                          ||
    //----------------------------------------------------------------------------------------------

    public void configuraDialog() {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.define_motorista_xml, null);
        AutoCompleteTextView autoComplete = viewCriada.findViewById(R.id.dialog_define_motorista_auto_complete);
        configuraAdapter(autoComplete);
        ui_configuraNomeDoMotoristaPreExistente();
        show(viewCriada, autoComplete);
    }

    private void ui_configuraNomeDoMotoristaPreExistente() {
        try {
            nomeMotorista = FiltraMotorista.localizaPeloId(dataSet_motorista, cavalo.getId()).getNome();
            motoristaPreExistente = nomeMotorista.toUpperCase(Locale.ROOT);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            nomeMotorista = "Nenhum motorista vinculado";
            motoristaPreExistente = " ";
        }
    }

    private void configuraAdapter(@NonNull AutoCompleteTextView autoComplete) {
        listaDeNomes = FiltraMotorista.listaDeNomes(dataSet_motorista);
        String[] arrayDeNomes = listaDeNomes.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayDeNomes);
        autoComplete.setAdapter(adapter);
    }

    private void show(View viewCriada, AutoCompleteTextView autoComplete) {
        new AlertDialog.Builder(context)
                .setTitle(cavalo.getPlaca())
                .setMessage(nomeMotorista)
                .setView(viewCriada)
                .setPositiveButton("Salvar", (dialog, which) -> {

                    List<String> listaDeNomesEmUper = getListaDeNomesEmUpperCase();

                    if (!listaDeNomesEmUper.contains(autoComplete.getText().toString().toUpperCase(Locale.ROOT)))
                        defineMotoristaCallback.quandoFalha(MSG_DE_FALHA);

                    else if (autoComplete.getText().toString().toUpperCase(Locale.ROOT).equals(motoristaPreExistente))
                        defineMotoristaCallback.quandoFalha(MSG_DE_SOBREPOSICAO);

                    else defineMotoristaParaOCavalo_sucesso(autoComplete);

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void defineMotoristaParaOCavalo_sucesso(@NonNull AutoCompleteTextView autoComplete) {
        motorista = FiltraMotorista.localizaPeloNome(dataSet_motorista, autoComplete.getText().toString());
        cavalo.setRefMotoristaId(motorista.getId());

        GhnApplication application = new GhnApplication();
        Handler handler = application.getMainThreadHandler();
        ExecutorService executorService = application.getExecutorService();
        AtualizaCavaloTask atualizaCavaloTask = new AtualizaCavaloTask(executorService, handler);
        atualizaCavaloTask.solicitaAtualizacao(cavaloDao, cavalo,
                () -> defineMotoristaCallback.quandoSucesso());
    }

    @NonNull
    private List<String> getListaDeNomesEmUpperCase() {
        List<String> listaDeNomesEmUper = new ArrayList<>();
        for (String s : listaDeNomes) {
            String nome = s.toUpperCase();
            listaDeNomesEmUper.add(nome);
        }
        return listaDeNomesEmUper;
    }

}
