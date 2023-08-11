package br.com.transporte.AppGhn.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.exception.ValorInvalidoException;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class AlteraComissao {
    private final Context context;
    private final Frete frete;
    private Callback callback;

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public AlteraComissao(Context context, Frete frete) {
        this.context = context;
        this.frete = frete;
    }

    public void dialogAlteraComissao(){
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.altera_comissao, null);
        EditText novoPercentualEdit = viewCriada.findViewById(R.id.dialog_novo_percentual_edit);
        novoPercentualEdit.addTextChangedListener(new MascaraMonetariaUtil(novoPercentualEdit));

        new AlertDialog.Builder(context)
                .setTitle("Alterando Comissão")
                .setMessage("Comissão atual: " + frete.getAdmFrete().getComissaoPercentualAplicada().toPlainString() + " %")
                .setView(viewCriada)
                .setPositiveButton("Salvar", (dialog, which) -> {

                    try{
                        String novoPercentual = novoPercentualEdit.getText().toString();
                        frete.getAdmFrete().setComissaoPercentualAplicada(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(novoPercentual)));
                        frete.getAdmFrete().setComissaoAoMotorista(frete.getAdmFrete().calculaComissao());
                        callback.quandoFunciona("Alteração realizada com sucesso");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        callback.quandoFalha("Preencha o campo");
                    } catch (ValorInvalidoException e) {
                        e.printStackTrace();
                        callback.quandoFalha(e.getMessage());
                    }
                }).setNegativeButton("Cancelar", (dialog, which) ->
                        callback.quandoCancela("Alteração cancelada"))
                .show();
    }

    public interface Callback {
        void quandoFunciona(String msg);
        void quandoFalha(String msg);
        void quandoCancela(String msg);
    }


}
