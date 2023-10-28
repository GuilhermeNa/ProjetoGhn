package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete.ADIANTAMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_RECEBIMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioRecebimentoFreteBinding;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.filtros.FiltraRecebimentoFrete;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.RecebimentoDeFreteRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioRecebimentoFreteViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.FormularioRecebimentoFreteViewModelFactory;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioRecebimentoFreteFragment extends FormularioBaseFragment {
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de Recebimento que já existe.";
    public static final String ALERTA_ADIANTAMENTO_PREEXISTENTE = "Já existe um adiantamento para esse Frete";
    public static final String ALERTA_CADASTRANDO_SALDO_SEM_ADIANTAMENTO = "Você ainda não registrou um adiantamento";
    public static final String ESCOLHA_UMA_FORMA_DE_PAGAMENTO = "Escolha uma forma de pagamento";
    private FragmentFormularioRecebimentoFreteBinding binding;
    private RoomFreteDao freteDao;
    private Bundle bundle;
    private RoomRecebimentoFreteDao recebimentoDao;
    private TextView restaReceberTxtView, tipoTxtView;
    private EditText dataEdit, descricaoEdit, valorEdit;
    private TextInputLayout dataLayout;
    private CheckBox adiantamentoBox, saldoBox;
    private RecebimentoDeFrete recebimento;
    private Frete frete;
    private FormularioRecebimentoFreteViewModel viewModel;
    private List<RecebimentoDeFrete> listaDeRecebimentosPorFreteId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        recebeReferenciaDeFreteExterno();
        long recebimentoId = verificaSeRecebeDadosExternos(CHAVE_ID_RECEBIMENTO);
        defineTipoEditandoOuCriando(recebimentoId);
        recebimento = (RecebimentoDeFrete) criaOuRecuperaObjeto(recebimentoId);
    }

    private void inicializaViewModel() {
        final RecebimentoDeFreteRepository recebimentoDeFreteRepository = new RecebimentoDeFreteRepository(requireContext());
        final FreteRepository freteRepository = new FreteRepository(requireContext());
        final FormularioRecebimentoFreteViewModelFactory factory = new FormularioRecebimentoFreteViewModelFactory(freteRepository, recebimentoDeFreteRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioRecebimentoFreteViewModel.class);
    }

    private void recebeReferenciaDeFreteExterno() {
        final long freteId = getArguments().getLong(CHAVE_ID);
        viewModel.localizaFrete(freteId).observe(this,
                freteRecebido -> {
                    if (freteRecebido != null) {
                        frete = freteRecebido;
                        observerRecebimentosPorFreteId();
                    }
                });
    }

    private void observerRecebimentosPorFreteId() {
        viewModel.buscaRecebimentosPorFreteId(frete.getId()).observe(this,
                lista -> {
                    if (lista != null) {
                        configuracoesAdicionaisUi(lista);
                        listaDeRecebimentosPorFreteId = lista;
                    }
                }
        );
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        long recebimentoId = (long) id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            viewModel.localizaRecebimento(recebimentoId).observe(this,
                    recebimentoDeFrete -> {
                        if (recebimentoDeFrete != null) {
                            viewModel.recebimentoArmazenado = recebimentoDeFrete;
                            recebimento = recebimentoDeFrete;
                            bind();
                        }
                    });
        } else {
            recebimento = new RecebimentoDeFrete();
        }
        return recebimento;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioRecebimentoFreteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                        On View Created                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuraCheckBox();
    }

    private void configuracoesAdicionaisUi(final List<RecebimentoDeFrete> lista) {
        BigDecimal recebido = CalculoUtil.somaValorTotalRecebido(lista);
        BigDecimal freteLiquidoAReceber = frete.getFreteLiquidoAReceber();
        BigDecimal emAberto = freteLiquidoAReceber.subtract(recebido);
        restaReceberTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(emAberto));
    }

    private void configuraCheckBox() {
        adiantamentoBox.setOnClickListener(v -> desmarcaBox(saldoBox));
        saldoBox.setOnClickListener(v -> desmarcaBox(adiantamentoBox));
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
    public void alteraUiParaModoEdicao() {
        TextView subEdit = binding.fragFormularioRecebimentoFreteSub;
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {
    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(ConverteDataUtil.dataParaString(recebimento.getData()));
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

    @Override
    public void aplicaMascarasAosEditTexts() {
        MascaraDataUtil.MascaraData(dataEdit);
        valorEdit.addTextChangedListener(new MascaraMonetariaUtil(valorEdit));
        configuraDataCalendario(dataLayout, dataEdit);
    }

    @Override
    public void vinculaDadosAoObjeto() {
        recebimento.setData(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        recebimento.setDescricao(descricaoEdit.getText().toString());
        recebimento.setValor(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));
        if (adiantamentoBox.isChecked()) {
            recebimento.setTipoRecebimentoFrete(ADIANTAMENTO);
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
            snackBar(view, ESCOLHA_UMA_FORMA_DE_PAGAMENTO);
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }
    }

    private void verificaSeJaExisteRegistrosDeAdiantamento(@NonNull View view) {
        RecebimentoDeFrete adiantamento = null;
        try {
            adiantamento = FiltraRecebimentoFrete.localizaPorTipo(listaDeRecebimentosPorFreteId, ADIANTAMENTO);
        } catch (ObjetoNaoEncontrado ignore) {
        }

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                requireActivity().setResult(RESULT_CANCELED);
                requireActivity().finish();
                break;


            case R.id.menu_formulario_salvar:
                verificaSeCamposEstaoPreenchidos(requireView());
                verificaSeJaExisteRegistrosDeAdiantamento(requireView());

                if (isCompletoParaSalvar()) {
                    new AlertDialog.Builder(this.getContext()).
                            setTitle(ADICIONANDO_REGISTRO_TITULO).
                            setMessage(ADICIONA_REGISTRO_TXT).
                            setPositiveButton(SIM, (dialog, which) -> {
                                vinculaDadosAoObjeto();
                                adicionaObjetoNoBancoDeDados();
                                requireActivity().setResult(RESULT_OK);
                                requireActivity().finish();
                            }).setNegativeButton(NAO, null).
                            show();
                } else {
                    setCompletoParaSalvar(true);
                }
                break;


            case R.id.menu_formulario_editar:
                verificaSeCamposEstaoPreenchidos(requireView());

                if (isCompletoParaSalvar()) {
                    new AlertDialog.Builder(this.getContext()).
                            setTitle(EDITANDO_REGISTRO_TITULO).
                            setMessage(EDITANDO_REGISTRO_TXT).
                            setPositiveButton(SIM, (dialog, which) -> {
                                vinculaDadosAoObjeto();
                                editaObjetoNoBancoDeDados();
                                requireActivity().setResult(RESULT_EDIT);
                                requireActivity().finish();
                            }).setNegativeButton(NAO, null).
                            show();
                } else {
                    setCompletoParaSalvar(true);
                }
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
        viewModel.salva(recebimento).observe(this,
                id -> {
                    if (id != null) {
                        requireActivity().setResult(RESULT_OK);
                        requireActivity().finish();
                    }
                });
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        viewModel.salva(recebimento).observe(this,
                ignore -> {
                    requireActivity().setResult(RESULT_EDIT);
                    requireActivity().finish();
                });
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        recebimento.setRefFreteId(frete.getId());
        return null;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        viewModel.deleta().observe(this,
                erro -> {
                    if (erro != null) {
                        requireActivity().setResult(RESULT_DELETE);
                        requireActivity().finish();
                    } else {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                });
    }

}