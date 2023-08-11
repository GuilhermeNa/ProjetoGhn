package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_RECEBIMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFormularioRecebimentoFreteBinding;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.RecebimentoFreteDAO;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioRecebimentoFreteFragment extends FormularioBaseFragment {
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de Recebimento que já existe.";
    public static final String ALERTA_ADIANTAMENTO_PREEXISTENTE = "Já existe um adiantamento para esse Frete";
    public static final String ALERTA_CADASTRANDO_SALDO_SEM_ADIANTAMENTO = "Você ainda não registrou um adiantamento";
    private FragmentFormularioRecebimentoFreteBinding binding;
    private FreteDAO freteDao;
    private Bundle bundle;
    private RecebimentoFreteDAO recebimentoDao;
    private TextView restaReceberTxtView, tipoTxtView;
    private EditText dataEdit, descricaoEdit, valorEdit;
    private TextInputLayout dataLayout;
    private CheckBox adiantamentoBox, saldoBox;
    private RecebimentoDeFrete recebimento;
    private Frete frete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioRecebimentoFreteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        freteDao = new FreteDAO();
        recebimentoDao = new RecebimentoFreteDAO();

        bundle = getArguments();
        frete = recebeReferenciaDeFreteExterno();
        int recebimentoId = verificaSeRecebeDadosExternos(CHAVE_ID_RECEBIMENTO);
        defineTipoEditandoOuCriando(recebimentoId);
        recebimento = (RecebimentoDeFrete) criaOuRecuperaObjeto(recebimentoId);

        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuracoesAdicionaisUi();
        configuraCheckBox();

    }

    private void configuracoesAdicionaisUi() {
        BigDecimal freteLiquidoAReceber = frete.getAdmFrete().getFreteLiquidoAReceber();
        BigDecimal recebido = recebimentoDao.valorRecebido(frete.getId());
        BigDecimal emAberto = freteLiquidoAReceber.subtract(recebido);

        restaReceberTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(emAberto));
    }

    private void configuraCheckBox() {
        adiantamentoBox.setOnClickListener(v -> {
            desmarcaBox(saldoBox);
        });

        saldoBox.setOnClickListener(v -> {
            desmarcaBox(adiantamentoBox);
        });

    }

    private Frete recebeReferenciaDeFreteExterno() {
        int freteId = bundle.getInt(CHAVE_ID);
        frete = freteDao.localizaPeloId(freteId);

        return frete;
    }

    @Override
    public void inicializaCamposDaView() {
        restaReceberTxtView = binding.fragFormularioRecebimentoFreteRestaReceber;
        dataLayout = binding.fragFormularioRecebimentoFreteLayoutData;
        dataEdit = binding.fragFormularioRecebimentoFreteData;
        descricaoEdit = binding.fragFormularioRecebimentoFreteDescricao;
        valorEdit = binding.fragFormularioRecebimentoFreteValor;
        adiantamentoBox = binding.fragFormularioRecebimentoFreteAdiantamentoBox;
        saldoBox = binding.fragFormularioRecebimentoFreteSaldoBox;
        tipoTxtView = binding.fragFormularioRecebimentoFreteTipo;
    }

    @Override
    public Object criaOuRecuperaObjeto(int id) {
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            recebimento = recebimentoDao.localizaPeloId(id);
        } else {
            recebimento = new RecebimentoDeFrete();
        }
        return recebimento;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subEdit = binding.fragFormularioRecebimentoFreteSub;
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(FormataDataUtil.dataParaString(recebimento.getData()));
        descricaoEdit.setText(recebimento.getDescricao());
        valorEdit.setText(FormataNumerosUtil.formataNumero(recebimento.getValor()));

        switch (recebimento.getTipoRecebimentoFrete()) {
            case ADIANTAMENTO:
                adiantamentoBox.setChecked(true);
                break;

            case SALDO:
                saldoBox.setChecked(true);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void aplicaMascarasAosEditTexts() {
        MascaraDataUtil.MascaraData(dataEdit);
        valorEdit.addTextChangedListener(new MascaraMonetariaUtil(valorEdit));
        configuraDataCalendario(dataLayout, dataEdit);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void vinculaDadosAoObjeto() {
        recebimento.setData(FormataDataUtil.stringParaData(dataEdit.getText().toString()));
        recebimento.setDescricao(descricaoEdit.getText().toString());
        recebimento.setValor(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));

        if (adiantamentoBox.isChecked()) {
            recebimento.setTipoRecebimentoFrete(TipoRecebimentoFrete.ADIANTAMENTO);
        } else if (saldoBox.isChecked()) {
            recebimento.setTipoRecebimentoFrete(TipoRecebimentoFrete.SALDO);
        }

    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(dataEdit, getView());
        verificaCampo(descricaoEdit);
        verificaCampo(valorEdit);
        if (!adiantamentoBox.isChecked() && !saldoBox.isChecked()) {
            tipoTxtView.setError("");
            snackBar(view, "Escolha uma forma de pagamento");
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }
    }

    private void verificaSeJaExisteRegistrosDeAdiantamento(@NonNull View view) {
        RecebimentoDeFrete adiantamento = recebimentoDao.retornaAdiantamento(frete.getId());
        boolean jaExisteAdiantamento = adiantamento != null;
        boolean naoExisteAdiantamento = adiantamento == null;

        boolean tipoCriando = getTipoFormulario() == TipoFormulario.ADICIONANDO;
        boolean tipoEditando = getTipoFormulario() == TipoFormulario.EDITANDO;
        boolean inserindoUmSegundoRegistroDeAdiantamento = false;

        try {
            inserindoUmSegundoRegistroDeAdiantamento = !adiantamento.equals(recebimento);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (adiantamentoBox.isChecked() && jaExisteAdiantamento && tipoCriando) {
            snackBar(view, ALERTA_ADIANTAMENTO_PREEXISTENTE);
            adiantamentoBox.setChecked(false);
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);

        } else if (adiantamentoBox.isChecked() && jaExisteAdiantamento && tipoEditando
                && inserindoUmSegundoRegistroDeAdiantamento) {
            snackBar(view, ALERTA_ADIANTAMENTO_PREEXISTENTE);
            adiantamentoBox.setChecked(false);
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }

        if (saldoBox.isChecked() && naoExisteAdiantamento) {
            snackBar(view, ALERTA_CADASTRANDO_SALDO_SEM_ADIANTAMENTO);
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(CANCELAR_REGISTRO_TITULO).
                        setMessage(CANCELA_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {
                            requireActivity().setResult(RESULT_CANCELED);
                            requireActivity().finish();
                        }).
                        setNegativeButton(NAO, null).show();
                break;


            case R.id.menu_formulario_salvar:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(ADICIONANDO_REGISTRO_TITULO).
                        setMessage(ADICIONA_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {
                            verificaSeCamposEstaoPreenchidos(this.requireView());
                            verificaSeJaExisteRegistrosDeAdiantamento(this.requireView());
                            if (isCompletoParaSalvar()) {
                                vinculaDadosAoObjeto();
                                adicionaObjetoNoBancoDeDados();
                                requireActivity().setResult(RESULT_OK);
                                requireActivity().finish();
                            } else {
                                setCompletoParaSalvar(true);
                            }
                        }).
                        setNegativeButton(NAO, null).
                        show();
                break;


            case R.id.menu_formulario_editar:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(EDITANDO_REGISTRO_TITULO).
                        setMessage(EDITANDO_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {
                            verificaSeCamposEstaoPreenchidos(this.getView());
                            if (isCompletoParaSalvar()) {
                                vinculaDadosAoObjeto();
                                editaObjetoNoBancoDeDados();
                                requireActivity().setResult(RESULT_EDIT);
                                requireActivity().finish();
                            } else {
                                setCompletoParaSalvar(true);
                            }
                        }).
                        setNegativeButton(NAO, null).
                        show();
                break;


            case R.id.menu_formulario_apagar:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(APAGA_REGISTRO_TITULO).
                        setMessage(APAGA_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {
                            deletaObjetoNoBancoDeDados();
                            requireActivity().setResult(RESULT_DELETE);
                            requireActivity().finish();
                        }).
                        setNegativeButton(NAO, null).show();
                break;
        }
        return false;
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        recebimentoDao.edita(recebimento);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        recebimentoDao.adiciona(recebimento);
    }

    @Override
    public int configuraObjetoNaCriacao() {
        recebimento.setRefFrete(frete.getId());
        return 0;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        recebimentoDao.deleta(recebimento);
    }

}