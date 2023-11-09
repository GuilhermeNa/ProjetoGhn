package br.com.transporte.appGhn.ui.fragment.formularios.certificado;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.INCORRETO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.SELECIONE_UM_CAVALO_VALIDO;
import static br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoConstantesExt.ATIVO;
import static br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoConstantesExt.CONFIRMA_A_RENOVACAO_DE_UM_NOVO_REGISTRO;
import static br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoConstantesExt.JA_EXISTE_UM;
import static br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoConstantesExt.PARA_A_PLACA;
import static br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoConstantesExt.RENOVANDO_REGISTRO;
import static br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoConstantesExt.SELECIONE_UM_CERTIFICADO_VALIDO;
import static br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoConstantesExt.SUB_TITULO_APP_BAR_RENOVANDO;
import static br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroVida.FormularioSeguroVidaFragment.SUB_TITULO_APP_BAR_EDITANDO;

import android.annotation.SuppressLint;
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
import br.com.transporte.AppGhn.databinding.FragmentFormularioCertificadosBinding;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.model.enums.TipoCertificado;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CertificadoRepository;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioBaseFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.certificado.domain.ConfiguraTiposUseCase;
import br.com.transporte.appGhn.ui.fragment.formularios.certificado.domain.FCertificadoChecaDuplicidadeAoSalvarUseCase;
import br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoCarregaArgumentsExt;
import br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoDialogExt;
import br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension.FCertificadoGetListaCertificadosExt;
import br.com.transporte.appGhn.ui.fragment.formularios.certificado.viewmodel.FormularioCertificadoViewModel;
import br.com.transporte.appGhn.ui.fragment.formularios.certificado.viewmodel.FormularioCertificadoViewModelFactory;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.MascaraDataUtil;
import br.com.transporte.appGhn.util.MascaraMonetariaUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class FormularioCertificadosFragment extends FormularioBaseFragment {
    private FragmentFormularioCertificadosBinding binding;
    private FormularioCertificadoViewModel viewModel;
    private TextView subtitulo;
    private AutoCompleteTextView autoCompleteCertificado, autoCompletePlaca;
    private TextInputLayout dataExpedicaoInputLayout, dataVencimentoInputLayout,
            layoutPlaca, certificadoLayout;
    private EditText campoDataExpedicao, campoAnoRef, campoNumeroDoc,
            campoValor, campoDtVencimento;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        tentaCarregarArguments();
        tentaCarregarCertificado();
        carregaListaDePlacas();
        carregaListaDeCertificados();
    }

    private void inicializaViewModel() {
        final CertificadoRepository certificadoRepository = new CertificadoRepository(requireContext());
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final FormularioCertificadoViewModelFactory factory = new FormularioCertificadoViewModelFactory(certificadoRepository, cavaloRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioCertificadoViewModel.class);
    }

    private void tentaCarregarArguments() {
        final FCertificadoCarregaArgumentsExt carregaArguments =
                new FCertificadoCarregaArgumentsExt(getArguments());

        viewModel.setTipoDespesa(carregaArguments.getTipoDespesa());
        viewModel.setTipoFormulario(carregaArguments.getTipoRequisicao());
        viewModel.setIdCertificadoCarregado(carregaArguments.tentaCarregarCertificadoId());
    }

    private void tentaCarregarCertificado() {
        viewModel.localizaCertificado(viewModel.getIdCertificadoCarregado())
                .observe(this,
                        this::preparaAmbienteComBaseNosTipos
                );
    }

    private void preparaAmbienteComBaseNosTipos(final DespesaCertificado certificadoRecebido) {
        final ConfiguraTiposUseCase tipoUseCase =
                new ConfiguraTiposUseCase(
                        requireContext(), this,
                        viewModel.getTipoFormulario(), viewModel.getTipoDespesa()
                );
        tipoUseCase.run(certificadoRecebido, new ConfiguraTiposUseCase.ConfiguraTipoUseCaseCallback() {
            @Override
            public void adicionandoDespesaDireta() {
                viewModel.setCertificadoArmazenado(new DespesaCertificado());
                alteraUiParaModoCriacao();
            }

            @Override
            public void adicionandoDespesaIndireta() {
                viewModel.setCertificadoArmazenado(new DespesaCertificado());
                alteraUiParaModoCriacao();
            }

            @Override
            public void renovandoDespesaDireta(Cavalo cavalo) {
                viewModel.setCertificadoArmazenado(new DespesaCertificado());
                viewModel.setCertificadoRenovado(certificadoRecebido);
                viewModel.setCavaloArmazenado(cavalo);
                alteraUiParaModoRenovacao(cavalo);
            }

            @Override
            public void renovandoDespesaIndireta() {
                viewModel.setCertificadoArmazenado(new DespesaCertificado());
                viewModel.setCertificadoRenovado(certificadoRecebido);
                alteraUiParaModoRenovacao(null);
            }

            @Override
            public void editandoDespesaDireta(Cavalo cavalo) {
                viewModel.setCertificadoArmazenado(certificadoRecebido);
                viewModel.setCavaloArmazenado(cavalo);
                alteraUiParaModoEdicao();
                exibeObjetoEmCasoDeEdicao();
            }

            @Override
            public void editandoDespesaIndireta() {
                viewModel.setCertificadoArmazenado(certificadoRecebido);
                alteraUiParaModoEdicao();
                exibeObjetoEmCasoDeEdicao();
            }
        });
    }

    private void carregaListaDePlacas() {
        viewModel.buscaPlacas().observe(this,
                placas -> {
                    if (placas != null) {
                        viewModel.setListaDePlacas(placas);
                        configuraDropDownMenuDePlacas();
                    }
                });
    }

    private void carregaListaDeCertificados() {
        final List<String> listaCertificados =
                FCertificadoGetListaCertificadosExt.run(viewModel.getTipoDespesa());

        viewModel.setListaDeCertificados(listaCertificados);
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                     ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioCertificadosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        final Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuraDropDownMenuDeCertificados();
    }

    @Override
    public void inicializaCamposDaView() {
        layoutPlaca = binding.fragFormularioCertificadoRefCavaloLayout;
        subtitulo = binding.fragFormularioCertificadoSub;
        dataExpedicaoInputLayout = binding.fragFormularioCertificadoLayoutDataExpedicao;
        campoDataExpedicao = binding.fragFormularioCertificadoDataExpedicao;
        certificadoLayout = binding.fragFormularioCertificadoNomeDocumentoLayout;
        autoCompleteCertificado = binding.fragFormularioCertificadoNomeDocumento;
        autoCompletePlaca = binding.fragFormularioCertificadoRefCavalo;
        campoAnoRef = binding.fragFormularioCertificadoAnoReferencia;
        campoNumeroDoc = binding.fragFormularioCertificadoNumeroDocumento;
        campoValor = binding.fragFormularioCertificadoValorCertificado;
        dataVencimentoInputLayout = binding.fragFormularioCertificadoLayoutDataVencimento;
        campoDtVencimento = binding.fragFormularioCertificadoDataVencimento;
    }

    private void configuraDropDownMenuDePlacas() {
        final AutoCompleteTextView campoPlacaAutoComplete = binding.fragFormularioCertificadoRefCavalo;
        final String[] cavalos = viewModel.getListaDePlacas().toArray(new String[0]);
        final ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, cavalos);
        campoPlacaAutoComplete.setAdapter(adapterCavalos);
    }

    private void configuraDropDownMenuDeCertificados() {
        String[] certificados = viewModel.getListaDeCertificados().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, certificados);
        autoCompleteCertificado.setAdapter(adapter);
    }

    @Override
    public void alteraUiParaModoEdicao() {
        if (viewModel.getTipoDespesa() == INDIRETA) {
            layoutPlaca.setVisibility(GONE);
            autoCompletePlaca.setVisibility(GONE);
        }
        subtitulo.setText(SUB_TITULO_APP_BAR_EDITANDO);

        Spannable text = new SpannableString(TITULO_APP_BAR_EDITANDO); // Substitua "Título" pelo texto desejado
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(text);

    }

    @Override
    public void alteraUiParaModoCriacao() {
        if (viewModel.getTipoDespesa() == INDIRETA) {
            autoCompletePlaca.setVisibility(GONE);
            layoutPlaca.setVisibility(GONE);
        }

        Spannable text = new SpannableString(TITULO_APP_BAR_CRIANDO); // Substitua "Título" pelo texto desejado
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(text);

    }

    public void alteraUiParaModoRenovacao(final Cavalo cavalo) {
        Spannable text = new SpannableString(TITULO_APP_BAR_RENOVANDO); // Substitua "Título" pelo texto desejado
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(text);

        String subTitulo;
        if (viewModel.getTipoDespesa() == DIRETA) {
            subTitulo = SUB_TITULO_APP_BAR_RENOVANDO + " " + viewModel.getCertificadoRenovado().getTipoCertificado().getDescricao() + PARA_A_PLACA + cavalo.getPlaca();
            autoCompletePlaca.setText(cavalo.getPlaca());

        } else {
            subTitulo = SUB_TITULO_APP_BAR_RENOVANDO + " " + viewModel.getCertificadoRenovado().getTipoCertificado().getDescricao();
        }

        autoCompleteCertificado.setText(viewModel.getCertificadoRenovado().getTipoCertificado().getDescricao());
        autoCompleteCertificado.setVisibility(GONE);
        certificadoLayout.setVisibility(GONE);
        autoCompletePlaca.setVisibility(GONE);
        layoutPlaca.setVisibility(GONE);
        subtitulo.setText(subTitulo);
    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        if (viewModel.getTipoDespesa() == DIRETA) {
            final String placa = viewModel.getCavaloArmazenado().getPlaca();
            autoCompletePlaca.setText(placa);
        }

        final String dataExpedicao = ConverteDataUtil.dataParaString(
                viewModel.getCertificadoArmazenado().getDataDeEmissao());
        campoDataExpedicao.setText(dataExpedicao);

        final String descricao = viewModel.getCertificadoArmazenado().getTipoCertificado().getDescricao();
        autoCompleteCertificado.setText(descricao);

        final String anoReferencia = viewModel.getCertificadoArmazenado().getAno();
        campoAnoRef.setText(anoReferencia);

        final String numeroDoc = String.valueOf(
                viewModel.getCertificadoArmazenado().getNumeroDoDocumento());
        campoNumeroDoc.setText(numeroDoc);

        final String valor = viewModel.getCertificadoArmazenado().getValorDespesa().toPlainString();
        campoValor.setText(valor);

        final String dataVencimento = ConverteDataUtil.dataParaString(
                viewModel.getCertificadoArmazenado().getDataDeVencimento());
        campoDtVencimento.setText(dataVencimento);
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        configuraDataCalendario(dataExpedicaoInputLayout, campoDataExpedicao);
        configuraDataCalendario(dataVencimentoInputLayout, campoDtVencimento);
        MascaraDataUtil.MascaraData(campoDataExpedicao);
        MascaraDataUtil.MascaraData(campoDtVencimento);
        campoValor.addTextChangedListener(new MascaraMonetariaUtil(campoValor));
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(campoDataExpedicao, view);
        verificaCampoComData(campoDtVencimento, view);
        verificaCampo(campoAnoRef);
        verificaCampo(campoNumeroDoc);
        verificaCampo(campoValor);
        verificaCampo(autoCompleteCertificado);

        if (!viewModel.getListaDeCertificados()
                .contains(
                        autoCompleteCertificado.getText().toString().toUpperCase(Locale.ROOT))
        ) {
            autoCompleteCertificado.setError(INCORRETO);
            autoCompleteCertificado.getText().clear();
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
            MensagemUtil.snackBar(view, SELECIONE_UM_CERTIFICADO_VALIDO);
        }

        if (viewModel.getTipoDespesa() == DIRETA) {
            if (!viewModel.getListaDePlacas()
                    .contains(
                            autoCompletePlaca.getText().toString().toUpperCase(Locale.ROOT))
            ) {
                autoCompletePlaca.setError(INCORRETO);
                autoCompletePlaca.getText().clear();
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                MensagemUtil.snackBar(view, SELECIONE_UM_CAVALO_VALIDO);
            }
        }
    }

    @Override
    public void vinculaDadosAoObjeto() {
        final LocalDate dataEmissao = ConverteDataUtil.stringParaData(campoDataExpedicao.getText().toString());
        viewModel.getCertificadoArmazenado().setDataDeEmissao(dataEmissao);

        final String anoReferencia = campoAnoRef.getText().toString();
        viewModel.getCertificadoArmazenado().setAno(anoReferencia);

        final long numeroDocumento = Long.parseLong(campoNumeroDoc.getText().toString());
        viewModel.getCertificadoArmazenado().setNumeroDoDocumento(numeroDocumento);

        final BigDecimal valor = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(campoValor.getText().toString()));
        viewModel.getCertificadoArmazenado().setValorDespesa(valor);

        final LocalDate dataVencimento = ConverteDataUtil.stringParaData(campoDtVencimento.getText().toString());
        viewModel.getCertificadoArmazenado().setDataDeVencimento(dataVencimento);

        final String nomeDoCertificado = autoCompleteCertificado.getText().toString();
        final TipoCertificado tipo = TipoCertificado.valueOf(nomeDoCertificado);
        viewModel.getCertificadoArmazenado().setTipoCertificado(tipo);
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        viewModel.salva(viewModel.getCertificadoArmazenado()).observe(this,
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
        viewModel.salva(viewModel.getCertificadoArmazenado()).observe(this,
                id -> {
                    if (id != null) {
                        if (viewModel.getTipoFormulario() == ADICIONANDO) {
                            requireActivity().setResult(RESULT_OK);
                        }
                        requireActivity().finish();
                    }
                });
    }

    public void renovaCertificadoNoBancoDeDados() {
        viewModel.renova(
                viewModel.getCertificadoArmazenado(),
                viewModel.getCertificadoRenovado()).observe(this,
                id -> {
                    if (id != null) {
                        if (viewModel.getTipoFormulario() == RENOVANDO) {
                            requireActivity().setResult(RESULT_UPDATE);
                            requireActivity().finish();
                        }
                    }
                });
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                requireActivity().finish();
                break;

            case R.id.menu_formulario_salvar:
                verificaSeCamposEstaoPreenchidos(this.getView());
                if (isCompletoParaSalvar()) {
                    tentaSalvarNoBancoDeDados();
                } else {
                    setCompletoParaSalvar(true);
                }
                break;

            case R.id.menu_formulario_editar:
                verificaSeCamposEstaoPreenchidos(this.getView());
                if (isCompletoParaSalvar()) {
                    final FCertificadoDialogExt dialog = new FCertificadoDialogExt(
                            requireContext(), EDITANDO_REGISTRO_TITULO, EDITANDO_REGISTRO_TXT);
                    dialog.run(() -> {
                        vinculaDadosAoObjeto();
                        editaObjetoNoBancoDeDados();
                    });
                } else {
                    setCompletoParaSalvar(true);
                }
                break;

            case R.id.menu_formulario_apagar:
                final FCertificadoDialogExt dialog = new FCertificadoDialogExt(
                        requireContext(), APAGA_REGISTRO_TITULO, APAGA_REGISTRO_TXT);
                dialog.run(this::deletaObjetoNoBancoDeDados);
                break;
        }
        return false;
    }

    private void tentaSalvarNoBancoDeDados() {
        final FCertificadoChecaDuplicidadeAoSalvarUseCase checaDuplicidadeUseCase =
                new FCertificadoChecaDuplicidadeAoSalvarUseCase(
                        getViewLifecycleOwner(), requireContext(), viewModel.getTipoDespesa(), viewModel.getTipoFormulario()
                );
        final String certificadoEmString = autoCompleteCertificado.getText().toString();
        final String placa = autoCompletePlaca.getText().toString();
        checaDuplicidadeUseCase.run(certificadoEmString, placa,
                new FCertificadoChecaDuplicidadeAoSalvarUseCase.ChecaDuplicidadeCallback() {
                    @Override
                    public void quandoRenovando() {
                        final FCertificadoDialogExt dialog = new FCertificadoDialogExt(
                                requireContext(), RENOVANDO_REGISTRO, CONFIRMA_A_RENOVACAO_DE_UM_NOVO_REGISTRO);
                        dialog.run(() -> {
                            vinculaDadosAoObjeto();
                            if (viewModel.getTipoDespesa() == DIRETA) {
                                configuraCertificadoNaCriacao(
                                        viewModel.getCavaloArmazenado().getId());
                            } else {
                                configuraCertificadoNaCriacao(null);
                            }
                            viewModel.getCertificadoRenovado().setValido(false);
                            renovaCertificadoNoBancoDeDados();
                        });
                    }

                    @Override
                    public void quandoNaoTemCertificadoDuplicado(Long cavaloId) {
                        final FCertificadoDialogExt dialog = new FCertificadoDialogExt(
                                requireContext(), ADICIONANDO_REGISTRO_TITULO, ADICIONA_REGISTRO_TXT);
                        dialog.run(() -> {
                            vinculaDadosAoObjeto();
                            configuraCertificadoNaCriacao(cavaloId);
                            adicionaObjetoNoBancoDeDados();
                        });
                    }

                    @Override
                    public void quandoTemCertificadoDuplicado() {
                        autoCompleteCertificado.setError("");
                        final String txt = JA_EXISTE_UM +
                                certificadoEmString
                                + ATIVO;
                        MensagemUtil.toast(requireContext(), txt);
                    }
                }
        );
    }

    private void configuraCertificadoNaCriacao(final Long cavaloId) {
        viewModel.getCertificadoArmazenado()
                .setRefCavaloId(cavaloId);
        viewModel.getCertificadoArmazenado()
                .setTipoDespesa(viewModel.getTipoDespesa());
        viewModel.getCertificadoArmazenado()
                .setValido(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public <T> T criaOuRecuperaObjeto(T id) {
        return null;
    }

    @Override
    protected void bind() {

    }

    @Override
    public Long configuraObjetoNaCriacao() {

        return null;
    }

}
