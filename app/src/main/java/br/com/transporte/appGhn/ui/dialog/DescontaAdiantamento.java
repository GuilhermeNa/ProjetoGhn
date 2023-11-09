package br.com.transporte.appGhn.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.exception.ValorInvalidoException;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.MascaraMonetariaUtil;

public class DescontaAdiantamento {
    private final Adiantamento adiantamento;
    private final Context context;
    private Callback callback;
    private BigDecimal novoValorADescontar;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public DescontaAdiantamento(Context context, Adiantamento adiantamento) {
        this.context = context;
        this.adiantamento = adiantamento;
    }

    /** @noinspection ResultOfMethodCallIgnored*/
    public void dialogDescontaAdiantamento() {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.desconta_adiantamento, null);
        EditText valorEdit = viewCriada.findViewById(R.id.desconta_adiantamento_valor);
        valorEdit.addTextChangedListener(new MascaraMonetariaUtil(valorEdit));

        new AlertDialog.Builder(context)
                .setTitle("Total Adiantamento: "+ FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.getValorTotal()))
                .setMessage("Resta Descontar: "+ FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.restaReembolsar()))
                .setView(viewCriada)
                .setPositiveButton("Salvar", (dialog, which) -> {

                    try {
                        novoValorADescontar = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString()));
                        verificaValidadeDoNovoDesconto(novoValorADescontar);
                        callback.quandoFunciona(adiantamento.getId(), novoValorADescontar,"Valor alterado com sucesso");
                    } catch(ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                        callback.quandoFalha("Preencha o campo valor.");
                    }
                    catch (ValorInvalidoException e) {
                        e.printStackTrace();
                        e.getMessage();
                        callback.quandoFalha(e.getMessage());
                    }

                })
                .setNegativeButton("Cancelar", (dialog, which) ->
                        callback.quandoCancela("Alteração cancelada"))
                .show();
    }

    public void verificaValidadeDoNovoDesconto(BigDecimal novoValorADescontar) throws ValorInvalidoException {
        int compare = adiantamento.restaReembolsar().compareTo(novoValorADescontar);
        boolean valorInvalido = compare < 0;

        if (valorInvalido) {
            throw new ValorInvalidoException("Restituição superior ao que foi pago em forma de adiantamento");
        }
    }

    public interface Callback {
        void quandoFunciona(Long id, BigDecimal novoValor, String msg);
        void quandoFalha(String msg);
        void quandoCancela(String msg);
    }

}
