package br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.INCORRETO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.JA_EXISTE_UM_SEGURO_FROTA_PARA_ESTE_ITEM;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.SELECIONE_UM_CAVALO_VALIDO;
import static br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoConstantesExt.SUB_TITULO_APP_BAR_RENOVANDO;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFormularioSeguroAutoBinding;
import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.model.enums.TipoFormulario;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.ParcelaSeguroFrotaRepository;
import br.com.transporte.appGhn.repository.SeguroFrotaRepository;
import br.com.transporte.appGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioBaseFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.CarregaArgumentsSegurosExt;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.domain.BuscaPorDuplicidadeDeSeguroFrotaAoAdicionarUseCase;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.domain.ConfiguraSeguroFrotaAntesDeInserirUseCase;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.domain.FSeguroFrotaCarregaDataUseCase;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.domain.FSeguroFrotaPreparaAmbienteUseCase;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.viewmodel.FormularioSeguroFrotaFragmentViewModelFactory;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.viewmodel.FormularioSeguroFrotaViewModel;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.DevolveResultado;
import br.com.transporte.appGhn.util.MascaraDataUtil;
import br.com.transporte.appGhn.util.MascaraMonetariaUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class FormularioSeguroFrotaFragment extends FormularioBaseFragment {
    private FragmentFormularioSeguroAutoBinding binding;
    private FormularioSeguroFrotaViewModel viewModel;
    private EditText campoDataInicial, campoDataFinal, campoValor, campoParcela, campoValorParcela,
            campoCia, campoNContrato, campoCoberturaCasco, campoRcfMateriais, campoRcfCorporais,
            campoAppMorte, campoAppInvalidez, campoDanosMorais, campoVidros, campoAssistencia24h,
            campoPrimeiraParcela;
    private AutoCompleteTextView autoCompletePlacas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        tentaCarregarArguments();
        tentaCarregarData();
    }

    private void inicializaViewModel() {
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final SeguroFrotaRepository seguroRepository = new SeguroFrotaRepository(requireContext());
        final ParcelaSeguroFrotaRepository parcelaRepository = new ParcelaSeguroFrotaRepository(requireContext());
        final FormularioSeguroFrotaFragmentViewModelFactory factory =
                new FormularioSeguroFrotaFragmentViewModelFactory(cavaloRepository, seguroRepository, parcelaRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioSeguroFrotaViewModel.class);
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
        final FSeguroFrotaCarregaDataUseCase carregaDataExt =
                new FSeguroFrotaCarregaDataUseCase(viewModel, this);
        carregaDataExt.getData(
                new FSeguroFrotaCarregaDataUseCase.FSeguroFrotaCarregaDataCallback() {
                    @Override
                    public void carregaListaDePlacas(List<String> listaDePlacas) {
                        viewModel.setListaDePlacas(listaDePlacas);
                        configuraDropDownMenuDeCavalos();
                    }

                    @Override
                    public void carregaSeguroFrota(DespesaComSeguroFrota seguroCarregado) {
                        preparaAmbienteUseCase(seguroCarregado);
                    }

                    @Override
                    public void carregaCavaloRelacionadoAoSeguro(Cavalo cavalo) {
                        viewModel.setCavaloRelacionado(cavalo);
                    }
                });
    }

    private void preparaAmbienteUseCase(final DespesaComSeguroFrota seguroCarregado) {
        final FSeguroFrotaPreparaAmbienteUseCase preparaAmbienteUseCase =
                new FSeguroFrotaPreparaAmbienteUseCase();

        preparaAmbienteUseCase.run(viewModel.getTipoFormulario(),
                new FSeguroFrotaPreparaAmbienteUseCase.FSeguroFrotaPreparaAmbienteUseCaseCallback() {
                    @Override
                    public void quandoAdicionando() {
                        viewModel.setSeguroArmazenado(new DespesaComSeguroFrota());
                        alteraUiParaModoCriacao();
                    }

                    @Override
                    public void quandoEditando() {
                        if (seguroCarregado != null) {
                            viewModel.setSeguroArmazenado(seguroCarregado);
                            alteraUiParaModoEdicao();
                            exibeObjetoEmCasoDeEdicao();
                        }
                    }

                    @Override
                    public void quandoRenovando() {
                        viewModel.setSeguroArmazenado(new DespesaComSeguroFrota());
                        viewModel.setSeguroRenovado(seguroCarregado);
                        alteraUiParaModoRenovacao();
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioSeguroAutoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private void configuraDropDownMenuDeCavalos() {
        final String[] cavalos = viewModel.getListaDePlacas().toArray(new String[0]);
        final ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, cavalos);
        autoCompletePlacas.setAdapter(adapterCavalos);
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
        campoRcfMateriais = binding.fragFormularioSeguroRcfDanosMateriais;
        campoRcfCorporais = binding.fragFormularioSeguroRcfDanosCorporais;
        autoCompletePlacas = binding.fragFormularioSeguroRefCaminhao;
        campoCoberturaCasco = binding.fragFormularioSeguroCoberturaCasco;
        campoAssistencia24h = binding.fragFormularioSeguroAssistencia24h;
        campoValorParcela = binding.fragFormularioSeguroValorParcela;
        campoAppInvalidez = binding.fragFormularioSeguroAppInvalidez;
        campoNContrato = binding.fragFormularioSeguroNumeroContrato;
        campoDataInicial = binding.fragFormularioSeguroDataInicial;
        campoDanosMorais = binding.fragFormularioSeguroDanosMorais;
        campoDataFinal = binding.fragFormularioSeguroDataFinal;
        campoCia = binding.fragFormularioSeguroCompanhia;
        campoPrimeiraParcela = binding.dataPrimeiraParcela;
        campoAppMorte = binding.fragFormularioSeguroAppMorte;
        campoParcela = binding.fragFormularioSeguroParcelas;
        campoValor = binding.fragFormularioSeguroValorTotal;
        campoVidros = binding.fragFormularioSeguroVidros;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        Spannable text = new SpannableString(TITULO_APP_BAR_EDITANDO); // Substitua "Título" pelo texto desejado
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(text);
    }

    @Override
    public void alteraUiParaModoCriacao() {
        Spannable text = new SpannableString(TITULO_APP_BAR_CRIANDO); // Substitua "Título" pelo texto desejado
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(text);

    }

    private void alteraUiParaModoRenovacao() {
        final TextInputLayout refCavaloLayout = binding.fragFormularioSeguroLayoutRefCaminhao;
        final TextView subEdit = binding.fragFormularioSeguroSub;

        Spannable text = new SpannableString(TITULO_APP_BAR_RENOVANDO); // Substitua "Título" pelo texto desejado
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(text);

        autoCompletePlacas.setVisibility(GONE);
        refCavaloLayout.setVisibility(GONE);

        final String placa = viewModel.getCavaloRelacionado().getPlaca();
        autoCompletePlacas.setText(placa);

        final String subtitulo = SUB_TITULO_APP_BAR_RENOVANDO + placa;
        subEdit.setText(subtitulo);
    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        try {
            BindData.fromString(autoCompletePlacas, viewModel.getCavaloRelacionado().getPlaca());
            BindData.fromLocalDate(campoPrimeiraParcela, viewModel.getSeguroArmazenado().getDataPrimeiraParcela());
            BindData.fromLocalDate(campoDataInicial, viewModel.getSeguroArmazenado().getDataInicial());
            BindData.fromLocalDate(campoDataFinal, viewModel.getSeguroArmazenado().getDataFinal());
            BindData.fromBigDecimal(campoValor, viewModel.getSeguroArmazenado().getValorDespesa());
            BindData.fromInteger(campoParcela, viewModel.getSeguroArmazenado().getParcelas());
            BindData.fromBigDecimal(campoValorParcela, viewModel.getSeguroArmazenado().getValorParcela());
            BindData.fromString(campoCia, viewModel.getSeguroArmazenado().getCompanhia());
            BindData.fromInteger(campoNContrato, viewModel.getSeguroArmazenado().getNContrato());
            BindData.fromString(campoCoberturaCasco, viewModel.getSeguroArmazenado().getCoberturaCasco());
            BindData.fromBigDecimal(campoRcfMateriais, viewModel.getSeguroArmazenado().getCoberturaRcfMateriais());
            BindData.fromBigDecimal(campoRcfCorporais, viewModel.getSeguroArmazenado().getCoberturaRcfCorporais());
            BindData.fromBigDecimal(campoAppMorte, viewModel.getSeguroArmazenado().getCoberturaAppMorte());
            BindData.fromBigDecimal(campoAppInvalidez, viewModel.getSeguroArmazenado().getCoberturaAppInvalidez());
            BindData.fromBigDecimal(campoDanosMorais, viewModel.getSeguroArmazenado().getCoberturaDanosMorais());
            BindData.fromString(campoCoberturaCasco, viewModel.getSeguroArmazenado().getCoberturaCasco());
            BindData.fromString(campoVidros, viewModel.getSeguroArmazenado().getCoberturaVidros());
            BindData.fromString(campoAssistencia24h, viewModel.getSeguroArmazenado().getAssistencia24H());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        final TextInputLayout dataFinalLayout = binding.fragFormularioSeguroLayoutDataFinal;
        final TextInputLayout dataPrimeiraParcelaLayout = binding.dataPrimeiraParcelaLayout;
        final TextInputLayout dataInicialLayout = binding.fragFormularioSeguroLayoutDataInicial;

        configuraDataCalendario(dataInicialLayout, campoDataInicial);
        configuraDataCalendario(dataFinalLayout, campoDataFinal);
        configuraDataCalendario(dataPrimeiraParcelaLayout, campoPrimeiraParcela);

        MascaraDataUtil.MascaraData(campoDataInicial);
        MascaraDataUtil.MascaraData(campoDataFinal);
        MascaraDataUtil.MascaraData(campoPrimeiraParcela);

        campoValor.addTextChangedListener(new MascaraMonetariaUtil(campoValor));
        campoValorParcela.addTextChangedListener(new MascaraMonetariaUtil(campoValorParcela));
        campoRcfMateriais.addTextChangedListener(new MascaraMonetariaUtil(campoRcfMateriais));
        campoRcfCorporais.addTextChangedListener(new MascaraMonetariaUtil(campoRcfCorporais));
        campoAppMorte.addTextChangedListener(new MascaraMonetariaUtil(campoAppMorte));
        campoAppInvalidez.addTextChangedListener(new MascaraMonetariaUtil(campoAppInvalidez));
        campoDanosMorais.addTextChangedListener(new MascaraMonetariaUtil(campoDanosMorais));
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
                tentaSalvarOuRenovarSeguroUseCase();
                break;

            case R.id.menu_formulario_apagar:
                tentaApagarSeguro();
                break;
        }
        return false;
    }

    private void tentaApagarSeguro() {
        new AlertDialog.Builder(this.getContext()).
                setTitle(APAGA_REGISTRO_TITULO).
                setMessage(APAGA_REGISTRO_TXT).
                setPositiveButton(SIM, (dialog, which) -> deletaObjetoNoBancoDeDados()).
                setNegativeButton(NAO, null).show();
    }

    private void tentaEditarSeguro() {
        verificaSeCamposEstaoPreenchidos(getView());
        exibeDialogQueEditaQuandoTodosOsCamposSaoValidos();
    }

    private void exibeDialogQueEditaQuandoTodosOsCamposSaoValidos() {
        if (isCompletoParaSalvar()) {
            new AlertDialog.Builder(this.getContext()).
                    setTitle(EDITANDO_REGISTRO_TITULO).
                    setMessage(EDITANDO_REGISTRO_TXT).
                    setPositiveButton(SIM,
                            (dialog, which) -> {
                                vinculaDadosAoObjeto();
                                editaObjetoNoBancoDeDados();
                            }).
                    setNegativeButton(NAO, null).
                    show();
        } else {
            setCompletoParaSalvar(true);
        }
    }

    private void tentaSalvarOuRenovarSeguroUseCase() {
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
                                preparaSeguroParaAdicao(
                                        seguroPreparado ->
                                        {
                                            switch (viewModel.getTipoFormulario()) {
                                                case ADICIONANDO:
                                                    buscaPorDuplicidade(
                                                            seguroPreparado.getRefCavaloId(),
                                                            (Boolean temSeguroDuplicado) ->
                                                            {
                                                                if (temSeguroDuplicado && isCompletoParaSalvar()) {
                                                                    setCompletoParaSalvar(false);
                                                                    autoCompletePlacas.setError("");
                                                                    MensagemUtil.toast(requireContext(), JA_EXISTE_UM_SEGURO_FROTA_PARA_ESTE_ITEM);
                                                                } else {
                                                                    adicionaSeguro(seguroPreparado);
                                                                }
                                                            });
                                                    break;

                                                case RENOVANDO:
                                                    preparaSeguroRenovado();
                                                    renovaSeguroNoBancoDeDados(seguroPreparado);
                                                    break;
                                            }
                                        });
                            }).
                    setNegativeButton(NAO, null).
                    show();
        } else {
            setCompletoParaSalvar(true);
        }
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(campoDataInicial, view);
        verificaCampoComData(campoDataFinal, view);
        verificaCampoComData(campoPrimeiraParcela, view);
        verificaCampo(autoCompletePlacas);
        verificaCampo(campoValor);
        verificaCampo(campoParcela);
        verificaCampo(campoValorParcela);
        verificaCampo(campoCia);
        verificaCampo(campoNContrato);
        verificaCampo(campoCoberturaCasco);
        verificaCampo(campoRcfMateriais);
        verificaCampo(campoRcfCorporais);
        verificaCampo(campoAppMorte);
        verificaCampo(campoAppInvalidez);
        verificaCampo(campoDanosMorais);
        verificaCampo(campoAssistencia24h);
        verificaSeAPlacaDigitadaEhValida();
    }

    private void verificaSeAPlacaDigitadaEhValida() {
        boolean placaInexistente =
                !viewModel.getListaDePlacas()
                        .contains(autoCompletePlacas.getText().toString().toUpperCase(Locale.ROOT));

        if (placaInexistente) {
            autoCompletePlacas.setError(INCORRETO);
            autoCompletePlacas.getText().clear();
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
            MensagemUtil.toast(this.requireContext(), SELECIONE_UM_CAVALO_VALIDO);
        }
    }

    @Override
    public void vinculaDadosAoObjeto() {
        final LocalDate dataInicial = ConverteDataUtil.stringParaData(campoDataInicial.getText().toString());
        viewModel.getSeguroArmazenado().setDataInicial(dataInicial);

        final LocalDate dataFinal = ConverteDataUtil.stringParaData(campoDataFinal.getText().toString());
        viewModel.getSeguroArmazenado().setDataFinal(dataFinal);

        final LocalDate dataPrimeiraParcela = ConverteDataUtil.stringParaData(campoPrimeiraParcela.getText().toString());
        viewModel.getSeguroArmazenado().setDataPrimeiraParcela(dataPrimeiraParcela);

        final BigDecimal valor = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoValor.getText().toString()));
        viewModel.getSeguroArmazenado().setValorDespesa(valor);

        final int parcelas = Integer.parseInt(campoParcela.getText().toString());
        viewModel.getSeguroArmazenado().setParcelas(parcelas);

        final BigDecimal valorParcela = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoValorParcela.getText().toString()));
        viewModel.getSeguroArmazenado().setValorParcela(valorParcela);

        final String companhia = campoCia.getText().toString();
        viewModel.getSeguroArmazenado().setCompanhia(companhia);

        final int numeroContrato = Integer.parseInt(campoNContrato.getText().toString());
        viewModel.getSeguroArmazenado().setNContrato(numeroContrato);

        final String coberturaCasco = campoCoberturaCasco.getText().toString();
        viewModel.getSeguroArmazenado().setCoberturaCasco(coberturaCasco);

        final BigDecimal rcfDanosMateriais = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoRcfMateriais.getText().toString()));
        viewModel.getSeguroArmazenado().setCoberturaRcfMateriais(rcfDanosMateriais);

        final BigDecimal rcfDanosCorporais = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoRcfCorporais.getText().toString()));
        viewModel.getSeguroArmazenado().setCoberturaRcfCorporais(rcfDanosCorporais);

        final BigDecimal appMorte = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoAppMorte.getText().toString()));
        viewModel.getSeguroArmazenado().setCoberturaAppMorte(appMorte);

        final BigDecimal appInvalidez = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoAppInvalidez.getText().toString()));
        viewModel.getSeguroArmazenado().setCoberturaAppInvalidez(appInvalidez);

        final BigDecimal danosMorais = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoDanosMorais.getText().toString()));
        viewModel.getSeguroArmazenado().setCoberturaDanosMorais(danosMorais);

        final String coberturaVidros = campoVidros.getText().toString();
        viewModel.getSeguroArmazenado().setCoberturaVidros(coberturaVidros);

        final String assistencia24h = campoAssistencia24h.getText().toString();
        viewModel.getSeguroArmazenado().setAssistencia24H(assistencia24h);
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
    public void editaObjetoNoBancoDeDados() {
        viewModel.salva(viewModel.getSeguroArmazenado())
                .observe(this,
                        ignore -> {
                            requireActivity().setResult(RESULT_EDIT);
                            requireActivity().finish();
                        });
    }

    public void adicionaSeguro(final DespesaComSeguroFrota seguroPreparado) {
        viewModel.salva(seguroPreparado)
                .observe(this,
                        id ->
                                criaESalvaParcelamentoDesteSeguro(seguroPreparado, id,
                                        () ->
                                                finalizaFormularioEDevolveResultado(RESULT_OK)));
    }

    private void finalizaFormularioEDevolveResultado(int resultCode) {
        requireActivity().setResult(resultCode);
        requireActivity().finish();
    }

    public void renovaSeguroNoBancoDeDados(final DespesaComSeguroFrota seguroPreparado) {
        viewModel.renova(seguroPreparado, viewModel.getSeguroRenovado())
                .observe(this,
                        id ->
                                criaESalvaParcelamentoDesteSeguro(seguroPreparado, id,
                                        () ->
                                                finalizaFormularioEDevolveResultado(RESULT_UPDATE)));
    }

    private void criaESalvaParcelamentoDesteSeguro(
            @NonNull final DespesaComSeguroFrota seguroPreparado,
            final Long id,
            final FormularioSeguroFrotaAdicionadaFinalizada callback
    ) {
        final List<Parcela_seguroFrota> parcelasDoSeguro = seguroPreparado.criaParcelas(id);
        viewModel.adicionaListaParcelas(parcelasDoSeguro).observe(this,
                quandoFinaliza ->
                        callback.parcelasCriadasEInseridasComSucesso());
    }

    private void preparaSeguroRenovado() {
        viewModel.getSeguroRenovado().setValido(false);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {

    }

    public void preparaSeguroParaAdicao(@NonNull final FormularioSeguroFrotaCallback callback) {
        final ConfiguraSeguroFrotaAntesDeInserirUseCase adicionaExt =
                new ConfiguraSeguroFrotaAntesDeInserirUseCase(
                        viewModel.getSeguroArmazenado(),
                        autoCompletePlacas.getText().toString(),
                        requireContext(),
                        this
                );
        adicionaExt.configuraSeguro(callback::seguroPreparadoParaAdicao);
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        return null;
    }

    @Override
    protected void bind() {
        super.bind();
    }

    private void buscaPorDuplicidade(
            final long idDoCavaloRelacionadoAoSeguro,
            @NonNull final DevolveResultado<Boolean> callback
    ) {
        final BuscaPorDuplicidadeDeSeguroFrotaAoAdicionarUseCase buscaDuplicidade =
                new BuscaPorDuplicidadeDeSeguroFrotaAoAdicionarUseCase(requireContext(), this);

        buscaDuplicidade.solicitaBusca(
                idDoCavaloRelacionadoAoSeguro,
                callback
        );
    }

    @Override
    public <T> T criaOuRecuperaObjeto(T id) {
        return null;
    }

    private interface FormularioSeguroFrotaCallback {
        void seguroPreparadoParaAdicao(DespesaComSeguroFrota seguroPreparado);
    }

    private interface FormularioSeguroFrotaAdicionadaFinalizada {
        void parcelasCriadasEInseridasComSucesso();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
