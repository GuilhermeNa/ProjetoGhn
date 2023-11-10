package br.com.transporte.appGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static br.com.transporte.appGhn.model.enums.TipoCustoDePercurso.NAO_REEMBOLSAVEL;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.formularios.FormularioRecebimentoFreteFragment.ESCOLHA_UMA_FORMA_DE_PAGAMENTO;
import static br.com.transporte.appGhn.util.MascaraMonetariaUtil.formatPriceSave;
import static br.com.transporte.appGhn.util.MensagemUtil.snackBar;

import android.os.Bundle;
import android.view.LayoutInflater;
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

import br.com.transporte.AppGhn.databinding.FragmentFormularioCustosPercursoBinding;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.appGhn.model.enums.TipoFormulario;
import br.com.transporte.appGhn.repository.CustoDePercursoRepository;
import br.com.transporte.appGhn.ui.viewmodel.FormularioCustoPercursoViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.FormularioCustoPercursoViewModelFactory;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.MascaraDataUtil;
import br.com.transporte.appGhn.util.MascaraMonetariaUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class FormularioCustosDePercursoFragment extends FormularioBaseFragment {
    private FragmentFormularioCustosPercursoBinding binding;
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de abastecimento que já existe.";
    private EditText dataEdit, valorEdit, descricaoEdit;
    private TextView reembolso;
    private CheckBox naoBox, simBox;
    private TextInputLayout dataLayout;
    private FormularioCustoPercursoViewModel viewModel;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        recebeReferenciaExternaDeCavalo(CHAVE_ID_CAVALO);
        long custoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(custoId);
        criaOuRecuperaObjeto(custoId);
    }

    private void inicializaViewModel() {
        final CustoDePercursoRepository repository = new CustoDePercursoRepository(requireContext());
        final FormularioCustoPercursoViewModelFactory factory = new FormularioCustoPercursoViewModelFactory(repository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioCustoPercursoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioCustosPercursoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                       OnViewCreated                                        ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuraCheckBox();
    }

    private void configuraCheckBox() {
        naoBox.setOnClickListener(v -> desmarcaBox(simBox));
        simBox.setOnClickListener(v -> desmarcaBox(naoBox));
    }

    @Override
    public void inicializaCamposDaView() {
        dataEdit = binding.fragFormularioDespesaData;
        valorEdit = binding.fragFormularioDespesaValor;
        descricaoEdit = binding.fragFormularioDespesaDescricao;
        naoBox = binding.fragFormularioDespesaBoxNao;
        simBox = binding.fragFormularioDespesaBoxSim;
        reembolso = binding.fragFormularioDespesaReembolso;
        dataLayout = binding.fragFormularioDespesaLayoutData;
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        Long custoId = (Long) id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            viewModel.localizaPeloId(custoId).observe(this,
                    custoRecebido -> {
                        if (custoRecebido != null) {
                            viewModel.custoArmazenado = custoRecebido;
                            bind();
                        }
                    });
        } else {
            viewModel.custoArmazenado = new CustosDePercurso();
        }
        return null;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxt = binding.fragFormularioDespesaSub;
        subTxt.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(ConverteDataUtil.dataParaString(viewModel.custoArmazenado.getData()));
        valorEdit.setText(viewModel.custoArmazenado.getValorCusto().toPlainString());
        descricaoEdit.setText(viewModel.custoArmazenado.getDescricao());
        if (viewModel.custoArmazenado.getTipo() == NAO_REEMBOLSAVEL) {
            naoBox.setChecked(true);
        } else {
            simBox.setChecked(true);
        }
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        configuraDataCalendario(dataLayout, dataEdit);
        MascaraDataUtil.MascaraData(dataEdit);
        valorEdit.addTextChangedListener(new MascaraMonetariaUtil(valorEdit));
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(dataEdit, view);
        verificaCampo(valorEdit);
        verificaCampo(descricaoEdit);

        if (!simBox.isChecked() && !naoBox.isChecked()) {
            reembolso.setError("");
            snackBar(view, ESCOLHA_UMA_FORMA_DE_PAGAMENTO);
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }
    }

    @Override
    public void vinculaDadosAoObjeto() {
        viewModel.custoArmazenado.setData(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        viewModel.custoArmazenado.setValorCusto(new BigDecimal(formatPriceSave(valorEdit.getText().toString())));
        viewModel.custoArmazenado.setDescricao(descricaoEdit.getText().toString());

        if (simBox.isChecked() && viewModel.custoArmazenado.getTipo() == null) {
            viewModel.custoArmazenado.setTipo(TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO);
            reembolso.setError(null);

        } else if (simBox.isChecked() && viewModel.custoArmazenado.getTipo() == NAO_REEMBOLSAVEL) {
            viewModel.custoArmazenado.setTipo(TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO);
            reembolso.setError(null);

        } else if (simBox.isChecked()) {
            reembolso.setError(null);

        } else if (naoBox.isChecked()) {
            viewModel.custoArmazenado.setTipo(NAO_REEMBOLSAVEL);
            reembolso.setError(null);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        viewModel.salva(viewModel.custoArmazenado).observe(this,
                ignore -> {
                    requireActivity().setResult(RESULT_EDIT);
                    requireActivity().finish();
                });
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        viewModel.salva(viewModel.custoArmazenado).observe(this,
                id -> {
                    if (id != null) {
                        requireActivity().setResult(RESULT_OK);
                        requireActivity().finish();
                    }
                });
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        viewModel.custoArmazenado.setRefCavaloId(cavaloRecebido.getId());
        viewModel.custoArmazenado.setApenasAdmEdita(false);
        return null;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        viewModel.deleta().observe(this,
                erro -> {
                    if (erro == null) {
                        requireActivity().setResult(RESULT_DELETE);
                        requireActivity().finish();
                    } else {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                });
    }

}