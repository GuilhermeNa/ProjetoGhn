package br.com.transporte.AppGhn.ui.fragment.formularios.despesaAdm;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.INCORRETO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.SELECIONE_UM_CAVALO_VALIDO;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFormularioDespesaAdmBinding;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.DespesaAdmRepository;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioBaseFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.despesaAdm.domain.FDespesaAdmPreparaAmbienteUseCase;
import br.com.transporte.AppGhn.ui.fragment.formularios.despesaAdm.extensions.FDespesaAdmCarregaArgumentsExt;
import br.com.transporte.AppGhn.ui.fragment.formularios.despesaAdm.viewmodel.FormularioDespesaAdmViewModel;
import br.com.transporte.AppGhn.ui.fragment.formularios.despesaAdm.viewmodel.FormularioDespesaAdmViewModelFactory;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioDespesaAdmFragment extends FormularioBaseFragment {
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de despesa que já existe.";
    public static final String ESCOLHA_UMA_TIPO_DE_DESPESA = "Escolha uma tipo de despesa";
    private FragmentFormularioDespesaAdmBinding binding;
    private EditText campoData, campoDescricao, campoValor;
    private TextInputLayout dataLayout, refLayout;
    private AutoCompleteTextView autoCompletePlacas;
    private CheckBox diretaBox, indiretaBox;
    private TextView despesaTxtView;
    private FormularioDespesaAdmViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        tentaCarregarArguments();
        defineTipoFormulario();
        tentaCarregarDespesaAdm();
    }

    private void inicializaViewModel() {
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final DespesaAdmRepository despesaAdmRepository = new DespesaAdmRepository(requireContext());
        final FormularioDespesaAdmViewModelFactory factory = new FormularioDespesaAdmViewModelFactory(cavaloRepository, despesaAdmRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioDespesaAdmViewModel.class);
    }

    private void tentaCarregarArguments() {
        final FDespesaAdmCarregaArgumentsExt carregaArguments = new FDespesaAdmCarregaArgumentsExt(getArguments());
        viewModel.setIdDespesaAdmCarregado(carregaArguments.tentaCarregarDespesaId());
        viewModel.setTipoDespesa(carregaArguments.getTipoDespesa());
    }

    private void defineTipoFormulario() {
        if (viewModel.getDespesaId() != 0) {
            viewModel.setTipoFormulario(EDITANDO);
        } else {
            viewModel.setTipoFormulario(ADICIONANDO);
        }
    }

    private void tentaCarregarDespesaAdm() {
        viewModel.localizaPeloId(viewModel.getDespesaId()).observe(this,
                despesaAdm -> {
                    if(despesaAdm == null && viewModel.getTipoFormulario() == ADICIONANDO){
                        preparaAmbienteUseCase(null);
                    } else if (despesaAdm != null && viewModel.getTipoFormulario() == EDITANDO){
                        preparaAmbienteUseCase(despesaAdm);
                    }
                });
    }

    private void preparaAmbienteUseCase(final DespesaAdm despesaAdm) {
        final FDespesaAdmPreparaAmbienteUseCase preparaAmbienteUseCase =
                new FDespesaAdmPreparaAmbienteUseCase(
                        viewModel.getTipoDespesa(),
                        viewModel.getTipoFormulario()
                );
        preparaAmbienteUseCase.run(
                despesaAdm,
                new FDespesaAdmPreparaAmbienteUseCase
                        .ConfiguraAmbienteFormularioDespesaUseCase() {
                    @Override
                    public void adicionadoDespesaDireta() {
                        viewModel.despesaArmazenada = new DespesaAdm();
                        carregaCavalosEPlacas();
                        alteraUiParaModoCriacao();
                    }

                    @Override
                    public void editandoDespesaDireta() {
                        viewModel.despesaArmazenada = despesaAdm;
                        carregaCavalosEPlacas();
                        alteraUiParaModoEdicao();
                    }

                    @Override
                    public void adicionandoDespesaIndireta() {
                        viewModel.despesaArmazenada = new DespesaAdm();
                        alteraUiParaModoCriacao();
                    }

                    @Override
                    public void editandoDespesaIndireta() {
                        viewModel.despesaArmazenada = despesaAdm;
                        alteraUiParaModoEdicao();
                        exibeObjetoEmCasoDeEdicao();
                    }
                });
    }

    private void carregaCavalosEPlacas() {
        viewModel.buscaTodosCavalos().observe(this,
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.setListaDeCavalos(resource.getDado());
                        if(viewModel.getTipoFormulario() == EDITANDO){
                            exibeObjetoEmCasoDeEdicao();
                        }
                    }
                });

        viewModel.carregaListaDePlacas().observe(this,
                listaDePlacas -> {
                    if (listaDePlacas != null) {
                        viewModel.setListaDePlacas(listaDePlacas);
                        configuraDropDownMenuDePlacas();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioDespesaAdmBinding.inflate(getLayoutInflater());
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
    }

    private void configuraDropDownMenuDePlacas() {
        String[] cavalos = viewModel.getListaDePlacas().toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, cavalos);
        autoCompletePlacas.setAdapter(adapterCavalos);
    }

    @Override
    public void inicializaCamposDaView() {
        refLayout = binding.fragFormularioDespesaFinanceiraReferenciaCavaloLayout;
        dataLayout = binding.fragFormularioDespesaFinanceiraLayoutData;
        campoData = binding.fragFormularioDespesaFinanceiraData;
        autoCompletePlacas = binding.fragFormularioDespesaFinanceiraReferenciaCavalo;
        campoDescricao = binding.fragFormularioDespesaFinanceiraDescricao;
        campoValor = binding.fragFormularioDespesaFinanceiraValor;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        if (viewModel.getTipoDespesa() == DIRETA) {
            refLayout.setVisibility(VISIBLE);
        }
        final TextView subEdit = binding.fragFormularioDespesaFinanceiraSub;
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).setTitle(TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {
        if (viewModel.getTipoDespesa() == DIRETA) {
            refLayout.setVisibility(VISIBLE);
        }
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).setTitle(TITULO_APP_BAR_CRIANDO);
    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        try {
            if (viewModel.getTipoDespesa() == DIRETA) {
                final String placa = FiltraCavalo.localizaPeloId(
                        viewModel.getListaDeCavalos(),
                        viewModel.despesaArmazenada.getRefCavaloId()).getPlaca();
                BindData.fromString(autoCompletePlacas, placa);
            }
            BindData.fromLocalDate(campoData, viewModel.despesaArmazenada.getData());
            BindData.fromBigDecimal(campoValor, viewModel.despesaArmazenada.getValorDespesa());
            BindData.fromString(campoDescricao, viewModel.despesaArmazenada.getDescricao());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void bind() {
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        configuraDataCalendario(dataLayout, campoData);
        MascaraDataUtil.MascaraData(campoData);
        campoValor.addTextChangedListener(new MascaraMonetariaUtil(campoValor));
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(campoData, view);
        verificaCampo(campoValor);
        verificaCampo(campoDescricao);

        if (viewModel.getTipoDespesa() == DIRETA) {
            verificaCampo(autoCompletePlacas);

            if (!viewModel.getListaDePlacas()
                    .contains(
                            autoCompletePlacas.getText().toString().toUpperCase(Locale.ROOT))
            ) {
                autoCompletePlacas.setError(INCORRETO);
                autoCompletePlacas.getText().clear();
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                MensagemUtil.snackBar(view, SELECIONE_UM_CAVALO_VALIDO);
            }
        }
    }

    @Override
    public void vinculaDadosAoObjeto() {
        viewModel.despesaArmazenada.setData(ConverteDataUtil.stringParaData(campoData.getText().toString()));
        viewModel.despesaArmazenada.setDescricao(campoDescricao.getText().toString());
        viewModel.despesaArmazenada.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoValor.getText().toString())));

        if (viewModel.getTipoDespesa() == DIRETA) {
            viewModel.despesaArmazenada.setTipoDespesa(DIRETA);
            Long cavaloId = FiltraCavalo.localizaPelaPlaca(
                    viewModel.getListaDeCavalos(),
                    autoCompletePlacas.getText().toString().toUpperCase(Locale.ROOT)
            ).getId();
            viewModel.despesaArmazenada.setRefCavaloId(cavaloId);
        } else if (viewModel.getTipoDespesa() == INDIRETA) {
            viewModel.despesaArmazenada.setTipoDespesa(INDIRETA);
            viewModel.despesaArmazenada.setRefCavaloId(0L);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        viewModel.salva(viewModel.despesaArmazenada).observe(this,
                ignore -> {
                    if (ignore == null) {
                        requireActivity().setResult(RESULT_EDIT);
                        requireActivity().finish();
                    }
                });
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        viewModel.deleta().observe(this,
                erro -> {
                    requireActivity().setResult(RESULT_DELETE);
                    requireActivity().finish();
                });
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        viewModel.salva(viewModel.despesaArmazenada).observe(this,
                id -> {
                    if (id != null) {
                        requireActivity().setResult(RESULT_OK);
                        requireActivity().finish();
                    }
                });
    }

    @Override
    public Long configuraObjetoNaCriacao() {

        return null;
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        return null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_formularios, menu);
        if (viewModel.getTipoFormulario() == EDITANDO) {
            menu.removeItem(R.id.menu_formulario_salvar);
        } else {
            menu.removeItem(R.id.menu_formulario_apagar);
            menu.removeItem(R.id.menu_formulario_editar);
        }
    }

}