package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida;

import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFormularioSeguroVidaBinding;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.repository.ParcelaSeguroVidaRepository;
import br.com.transporte.AppGhn.repository.SeguroVidaRepository;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioBaseFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.seguros.CarregaArgumentsSegurosExt;
import br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida.viewmodel.FormularioSeguroVidaViewModel;
import br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida.viewmodel.FormularioSeguroVidaViewModelFactory;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DevolveResultado;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioSeguroVidaFragment extends FormularioBaseFragment {
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de seguro que já existe.";
    public static final String SUB_TITULO_APP_BAR_RENOVANDO = "Você está renovando um seguro.";
    private FragmentFormularioSeguroVidaBinding binding;
    private EditText campoDataInicial, campoDataFinal, campoPremio, campoValor, campoCoberturaSocios,
            campoCoberturaMotoristas, campoParcelas, campoCia, campoNContrato, campoPrimeiraParcela;
    private TextInputLayout dataInicialLayout, dataFinalLayout, dataPrimeiraParcelaLayout;
    private TextView campoSubtitulo;
    private FormularioSeguroVidaViewModel viewModel;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        tentaCarregarArguments();
        tentaCarregarData();
    }

    private void inicializaViewModel() {
        final SeguroVidaRepository seguroRepository = new SeguroVidaRepository(requireContext());
        final ParcelaSeguroVidaRepository parcelaRepository = new ParcelaSeguroVidaRepository(requireContext());
        final FormularioSeguroVidaViewModelFactory factory = new FormularioSeguroVidaViewModelFactory(seguroRepository, parcelaRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioSeguroVidaViewModel.class);
    }

    private void tentaCarregarArguments() {
        final Long id =
                CarregaArgumentsSegurosExt.carregaId(getArguments());
        viewModel.setSeguroId(id);

        final TipoFormulario tipoFormulario =
                CarregaArgumentsSegurosExt.carregaTipoDeFormulario(getArguments());
        viewModel.setTipoFormulario(tipoFormulario);
    }

    private void tentaCarregarData() {
        final LiveData<DespesaComSeguroDeVida> observer =
                viewModel.carregaData();
        observer.observe(this, new Observer<DespesaComSeguroDeVida>() {
            @Override
            public void onChanged(DespesaComSeguroDeVida seguroDeVida) {
                observer.removeObserver(this);
                preparaAmbiente(seguroDeVida);
            }
        });
    }

    private void preparaAmbiente(final DespesaComSeguroDeVida seguroDeVida) {
        switch (viewModel.getTipoFormulario()) {
            case ADICIONANDO:
                viewModel.setSeguroArmazenado(new DespesaComSeguroDeVida());
                alteraUiParaModoCriacao();
                break;

            case EDITANDO:
                viewModel.setSeguroArmazenado(seguroDeVida);
                alteraUiParaModoEdicao();
                exibeObjetoEmCasoDeEdicao();
                break;

            case RENOVANDO:
                viewModel.setSeguroArmazenado(new DespesaComSeguroDeVida());
                viewModel.setSeguroRenovado(seguroDeVida);
                alteraUiParaModoRenovacao();
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioSeguroVidaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Toolbar toolbar = binding.toolbar;
        inicializaCamposDaView();
        configuraUi(toolbar);
    }

    @Override
    public void inicializaCamposDaView() {
        dataPrimeiraParcelaLayout = binding.dataPrimeiraParcelaLayout;
        campoPrimeiraParcela = binding.dataPrimeiraParcela;
        campoSubtitulo = binding.fragFormularioSeguroSub;
        dataInicialLayout = binding.fragFormularioSeguroLayoutDataInicial;
        dataFinalLayout = binding.fragFormularioSeguroLayoutDataFinal;
        campoDataInicial = binding.fragFormularioSeguroDataInicial;
        campoDataFinal = binding.fragFormularioSeguroDataFinal;
        campoPremio = binding.fragFormularioSeguroValorTotal;
        campoParcelas = binding.fragFormularioSeguroParcelas;
        campoValor = binding.fragFormularioSeguroValorParcela;
        campoCia = binding.fragFormularioSeguroCompanhia;
        campoNContrato = binding.fragFormularioSeguroNumeroContrato;
        campoCoberturaSocios = binding.fragFormularioSeguroCoberturaSocios;
        campoCoberturaMotoristas = binding.fragFormularioSeguroCoberturaMotoristas;
    }

    @Override
    public void alteraUiParaModoCriacao() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).setTitle(TITULO_APP_BAR_CRIANDO);
    }

    @Override
    public void alteraUiParaModoEdicao() {
        campoSubtitulo.setText(SUB_TITULO_APP_BAR_EDITANDO);
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).setTitle(TITULO_APP_BAR_EDITANDO);
    }

    private void alteraUiParaModoRenovacao() {
        campoSubtitulo.setText(SUB_TITULO_APP_BAR_RENOVANDO);
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).setTitle(TITULO_APP_BAR_RENOVANDO);
    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        try {
            BindData.fromLocalDate(campoDataInicial, viewModel.getSeguroArmazenado().getDataInicial());
            BindData.fromLocalDate(campoDataFinal, viewModel.getSeguroArmazenado().getDataFinal());
            BindData.fromLocalDate(campoPrimeiraParcela, viewModel.getSeguroArmazenado().getDataPrimeiraParcela());
            BindData.R$fromBigDecimal(campoPremio, viewModel.getSeguroArmazenado().getValorDespesa());
            BindData.fromInteger(campoParcelas, viewModel.getSeguroArmazenado().getParcelas());
            BindData.R$fromBigDecimal(campoValor, viewModel.getSeguroArmazenado().getValorParcela());
            BindData.fromString(campoCia, viewModel.getSeguroArmazenado().getCompanhia());
            BindData.fromInteger(campoNContrato, viewModel.getSeguroArmazenado().getNContrato());
            BindData.R$fromBigDecimal(campoCoberturaSocios, viewModel.getSeguroArmazenado().getCoberturaSocios());
            BindData.R$fromBigDecimal(campoCoberturaMotoristas, viewModel.getSeguroArmazenado().getCoberturaMotoristas());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        MascaraDataUtil.MascaraData(campoDataInicial);
        MascaraDataUtil.MascaraData(campoDataFinal);
        MascaraDataUtil.MascaraData(campoPrimeiraParcela);

        campoPremio.addTextChangedListener(new MascaraMonetariaUtil(campoPremio));
        campoValor.addTextChangedListener(new MascaraMonetariaUtil(campoValor));
        campoCoberturaSocios.addTextChangedListener(new MascaraMonetariaUtil(campoCoberturaSocios));
        campoCoberturaMotoristas.addTextChangedListener(new MascaraMonetariaUtil(campoCoberturaMotoristas));

        configuraDataCalendario(dataInicialLayout, campoDataInicial);
        configuraDataCalendario(dataFinalLayout, campoDataFinal);
        configuraDataCalendario(dataPrimeiraParcelaLayout, campoPrimeiraParcela);
    }

    //----------------------------------------------------------------------------------------------
    //                                          onMenuItemSelected                                ||
    //----------------------------------------------------------------------------------------------

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                requireActivity().finish();
                break;

            case R.id.menu_formulario_editar:
                tentaEditarSeguro();
                break;

            case R.id.menu_formulario_salvar:
                tentaSalvarOuRenovarSeguro();
                break;

            case R.id.menu_formulario_apagar:
                tentaApagarSeguro();
                break;
        }
        return false;
    }

    private void tentaEditarSeguro() {
        verificaSeCamposEstaoPreenchidos(requireView());
        exibeDialogQueEditaQuandoTodosOsCamposSaoValidos();
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(campoDataInicial, view);
        verificaCampoComData(campoDataFinal, view);
        verificaCampo(campoPremio);
        verificaCampo(campoParcelas);
        verificaCampo(campoValor);
        verificaCampo(campoCia);
        verificaCampo(campoNContrato);
        verificaCampo(campoCoberturaSocios);
        verificaCampo(campoCoberturaMotoristas);
        verificaCampo(campoPrimeiraParcela);
    }

    private void exibeDialogQueEditaQuandoTodosOsCamposSaoValidos() {
        if (isCompletoParaSalvar()) {
            new AlertDialog.Builder(requireContext())
                    .setTitle(APAGA_REGISTRO_TITULO)
                    .setMessage(APAGA_REGISTRO_TXT)
                    .setPositiveButton(SIM,
                            (dialog, which) -> {
                                vinculaDadosAoObjeto();
                                editaObjetoNoBancoDeDados();
                            })
                    .setNegativeButton(NAO, null)
                    .show();
        } else {
            setCompletoParaSalvar(true);
        }

    }

    private void tentaApagarSeguro() {
        new AlertDialog.Builder(this.getContext()).
                setTitle(APAGA_REGISTRO_TITULO).
                setMessage(APAGA_REGISTRO_TXT).
                setPositiveButton(SIM,
                        (dialog, which) -> {
                            deletaObjetoNoBancoDeDados();
                        }).
                setNegativeButton(NAO, null).show();
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        viewModel.deleta().observe(this,
                erro -> {
                    if(erro == null){
                        finalizaFormularioEDevolveResultado(RESULT_DELETE);
                    } else {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                });
    }

    private void tentaSalvarOuRenovarSeguro() {
        verificaSeCamposEstaoPreenchidos(getView());
        exibeDialogQueSalvaQuandoTodosOsCamposSaoValidos();
    }

    private void exibeDialogQueSalvaQuandoTodosOsCamposSaoValidos() {
        if (isCompletoParaSalvar()) {
            new AlertDialog.Builder(this.getContext()).
                    setTitle(ADICIONANDO_REGISTRO_TITULO).
                    setMessage(ADICIONA_REGISTRO_TXT).
                    setPositiveButton(SIM,
                            (dialog, which) ->
                            {
                                vinculaDadosAoObjeto();
                                switch (viewModel.getTipoFormulario()) {
                                    case ADICIONANDO:
                                        adicionaObjetoNoBancoDeDados();
                                        break;

                                    case RENOVANDO:
                                        renovaSeguroNoBancoDeDados();
                                        break;
                                }
                            }).
                    setNegativeButton(NAO, null).
                    show();
        } else {
            setCompletoParaSalvar(true);
        }
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        viewModel.salva().observe(this,
                id ->
                        criaESalvaParcelamentoDesteSeguro(id,
                                unused ->
                                        finalizaFormularioEDevolveResultado(RESULT_OK)));
    }

    public void criaESalvaParcelamentoDesteSeguro(
            final long id,
            final DevolveResultado<Void> callback
    ) {
        viewModel.criaESalvaParcelamentoDesteSeguro(id)
                .observe(this,
                        unused -> callback.devolveResultado(null));
    }

    private void finalizaFormularioEDevolveResultado(int resultCode) {
        requireActivity().setResult(resultCode);
        requireActivity().finish();
    }

    public void renovaSeguroNoBancoDeDados() {
        viewModel.renovaSeguro().observe(this,
                id ->
                        criaESalvaParcelamentoDesteSeguro(id,
                                unused ->
                                        finalizaFormularioEDevolveResultado(RESULT_UPDATE)));
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        viewModel.salva().observe(this,
                ignore -> {
                    requireActivity().setResult(RESULT_EDIT);
                    requireActivity().finish();
                });
    }

    @Override
    public void vinculaDadosAoObjeto() {
        final LocalDate dataPrimeiraParcela = ConverteDataUtil.stringParaData(campoPrimeiraParcela.getText().toString());
        viewModel.getSeguroArmazenado().setDataPrimeiraParcela(dataPrimeiraParcela);

        final LocalDate dataInicial = ConverteDataUtil.stringParaData(campoDataInicial.getText().toString());
        viewModel.getSeguroArmazenado().setDataInicial(dataInicial);

        final LocalDate dataFinal = ConverteDataUtil.stringParaData(campoDataFinal.getText().toString());
        viewModel.getSeguroArmazenado().setDataFinal(dataFinal);

        final BigDecimal valorDespesa = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoPremio.getText().toString()));
        viewModel.getSeguroArmazenado().setValorDespesa(valorDespesa);

        final int quantidadeParcelas = Integer.parseInt(campoParcelas.getText().toString());
        viewModel.getSeguroArmazenado().setParcelas(quantidadeParcelas);

        final BigDecimal valorParcela = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoValor.getText().toString()));
        viewModel.getSeguroArmazenado().setValorParcela(valorParcela);

        final String companhia = campoCia.getText().toString();
        viewModel.getSeguroArmazenado().setCompanhia(companhia);

        final int nContrato = Integer.parseInt(campoNContrato.getText().toString());
        viewModel.getSeguroArmazenado().setNContrato(nContrato);

        final BigDecimal coberturaSocios = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoCoberturaSocios.getText().toString()));
        viewModel.getSeguroArmazenado().setCoberturaSocios(coberturaSocios);

        final BigDecimal coberturaMotoristas = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoCoberturaMotoristas.getText().toString()));
        viewModel.getSeguroArmazenado().setCoberturaMotoristas(coberturaMotoristas);

        configuracoesAdicionaisDeSeguroQuandoEstaSendoAdicionadoOuRenovado();
    }

    private void configuracoesAdicionaisDeSeguroQuandoEstaSendoAdicionadoOuRenovado() {
        if (viewModel.getTipoFormulario() == ADICIONANDO || viewModel.getTipoFormulario() == RENOVANDO)
            viewModel.configuraSeguroQuandoEstamosInserindoUmNovo();
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
    protected void bind() {
        super.bind();
    }
}