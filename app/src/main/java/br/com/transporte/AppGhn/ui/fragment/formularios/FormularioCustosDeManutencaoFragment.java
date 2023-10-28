package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

import android.content.Intent;
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

import br.com.transporte.AppGhn.databinding.FragmentFormularioCustosDeManutencaoBinding;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.enums.TipoCustoManutencao;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.repository.ManutencaoRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioManutencaoViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.FormularioManutencaoViewModelFactory;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioCustosDeManutencaoFragment extends FormularioBaseFragment {
    public static final String ESCOLHA_UM_TIPO_DE_CUSTO = "Escolha um tipo de custo";
    private FragmentFormularioCustosDeManutencaoBinding binding;
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de manutenção que já existe.";
    private CustosDeManutencao manutencao;
    private EditText dataEdit, empresaEdit, descricaoEdit, nNotaEdit, valorEdit;
    private TextInputLayout dataLayout;
    private CheckBox boxPeriodico, boxExtraordinario;
    private TextView tituloTxtView;
    private FormularioManutencaoViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        recebeReferenciaExternaDeCavalo(CHAVE_ID_CAVALO);
        long manutencaoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(manutencaoId);
        manutencao = (CustosDeManutencao) criaOuRecuperaObjeto(manutencaoId);
    }


    private void inicializaViewModel() {
        final ManutencaoRepository repository = new ManutencaoRepository(requireContext());
        final FormularioManutencaoViewModelFactory factory = new FormularioManutencaoViewModelFactory(repository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioManutencaoViewModel.class);
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        final Long manutencaoId = (Long) id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            viewModel.localizaManutencao(manutencaoId).observe(this,
                    custosDeManutencao -> {
                        if (custosDeManutencao != null) {
                            viewModel.manutencaoArmazenada = custosDeManutencao;
                            this.manutencao = custosDeManutencao;
                            bind();
                        }
                    });
        } else {
            manutencao = new CustosDeManutencao();
        }
        return manutencao;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioCustosDeManutencaoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
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
        boxExtraordinario.setOnClickListener(v -> desmarcaBox(boxPeriodico));
        boxPeriodico.setOnClickListener(v -> desmarcaBox(boxExtraordinario));
    }

    @Override
    public void inicializaCamposDaView() {
        dataLayout = binding.fragFormularioManutencaoLayoutData;
        dataEdit = binding.fragFormularioManutencaoData;
        empresaEdit = binding.fragFormularioManutencaoEmpresa;
        nNotaEdit = binding.fragFormularioManutencaoNota;
        descricaoEdit = binding.fragFormularioManutencaoDescricao;
        valorEdit = binding.fragFormularioManutencaoValor;
        boxPeriodico = binding.fragFormularioManutencaoPeriodico;
        boxExtraordinario = binding.fragFormularioManutencaoExtraordinario;
        tituloTxtView = binding.fragFormularioManutencaoTitulo;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxtView = binding.fragFormularioManutencaoSub;
        subTxtView.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(ConverteDataUtil.dataParaString(manutencao.getData()));
        empresaEdit.setText(manutencao.getEmpresa());
        nNotaEdit.setText(manutencao.getNNota());
        descricaoEdit.setText(manutencao.getDescricao());
        valorEdit.setText((manutencao.getValorCusto().toPlainString()));
        if (manutencao.getTipoCustoManutencao() == TipoCustoManutencao.PERIODICA) {
            boxPeriodico.setChecked(true);
        } else {
            boxExtraordinario.setChecked(true);
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
        verificaCampo(empresaEdit);
        verificaCampo(nNotaEdit);
        verificaCampo(descricaoEdit);
        verificaCampo(valorEdit);
        if (!boxPeriodico.isChecked() && !boxExtraordinario.isChecked()) {
            tituloTxtView.setError("");
            snackBar(view, ESCOLHA_UM_TIPO_DE_CUSTO);
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }
    }

    @Override
    public void vinculaDadosAoObjeto() {
        manutencao.setData(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        manutencao.setEmpresa(empresaEdit.getText().toString());
        manutencao.setNNota(nNotaEdit.getText().toString());
        manutencao.setDescricao(descricaoEdit.getText().toString());
        manutencao.setValorCusto(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));
        if (boxExtraordinario.isChecked()) {
            manutencao.setTipoCustoManutencao(TipoCustoManutencao.EXTRAORDINARIA);
            tituloTxtView.setError(null);
        } else if (boxPeriodico.isChecked()) {
            manutencao.setTipoCustoManutencao(TipoCustoManutencao.PERIODICA);
            tituloTxtView.setError(null);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        viewModel.salva(manutencao).observe(this,
                ignore -> {
                    final Intent intent = new Intent();
                    intent.putExtra("OBJ_ENVIADO", manutencao);
                    requireActivity().setResult(RESULT_EDIT, intent);
                    requireActivity().finish();
                });
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

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        viewModel.salva(manutencao).observe(this,
                id -> {
                    final Intent intent = new Intent();
                    manutencao.setId(id);
                    intent.putExtra("OBJ_ENVIADO", manutencao);
                    requireActivity().setResult(RESULT_OK, intent);
                    requireActivity().finish();
                });
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        manutencao.setRefCavaloId(cavaloRecebido.getId());
        return null;
    }
}