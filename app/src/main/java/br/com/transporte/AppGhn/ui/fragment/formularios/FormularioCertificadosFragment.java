package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.AppGhn.ui.fragment.formularios.FormularioSeguroFrotaFragment.INCORRETO;
import static br.com.transporte.AppGhn.ui.fragment.formularios.FormularioSeguroFrotaFragment.SELECIONE_UM_CAVALO_VALIDO;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioCertificadosBinding;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioCertificadosFragment extends FormularioBaseFragment {
    public static final String JA_EXISTE_UM = "Já existe um ";
    public static final String ATIVO = " ativo.";
    public static final String SELECIONE_UM_CERTIFICADO_VALIDO = "Selecione um certificado válido";
    public static final String PARA_A_PLACA = ", para a placa ";
    private FragmentFormularioCertificadosBinding binding;
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de certificado que já existe.";
    private static final String SUB_TITULO_APP_BAR_RENOVANDO = "Você está renovando um ";
    private EditText dataExpedicaoEdit, anoReferenciaEdit, nDocumentoEdit, valorDocumentoEdit, dataVencimentoEdit;
    private TextInputLayout dataExpedicaoInputLayout, dataVencimentoInputLayout, placaLayout, certificadoLayout;
    private AutoCompleteTextView certificadoAutoComplete, placaAutoComplete;
    private DespesaCertificado certificadoQueEstaSendoSubstituido, certificado;
    private List<String> listaCertificadosEmString;
    private RoomDespesaCertificadoDao certificadoDao;
    private TipoDespesa tipoDespesa;
    private RoomCavaloDao cavaloDao;
    private TextView subEdit;
    private List<String> listaDePlacas;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        certificadoDao = dataBase.getRoomDespesaCertificadoDao();
        cavaloDao = dataBase.getRoomCavaloDao();
   //     listaDePlacas = FiltraCavalo.listaDePlacas(cavaloDao.todos());
        long certificadoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        configuraTipoDeRecebimento();
        certificado = (DespesaCertificado) criaOuRecuperaObjeto(certificadoId);
    }

    private void configuraTipoDeRecebimento() {
        Bundle bundle = getArguments();
        tipoDespesa = (TipoDespesa) bundle.getSerializable(CHAVE_DESPESA);

        TipoFormulario tipoRequisicao = (TipoFormulario) bundle.getSerializable(CHAVE_REQUISICAO);
        switch (Objects.requireNonNull(tipoRequisicao)) {
            case EDITANDO:
                setTipoFormulario(EDITANDO);
                break;

            case ADICIONANDO:
                setTipoFormulario(ADICIONANDO);
                break;

            case RENOVANDO:
                setTipoFormulario(RENOVANDO);
                break;
        }
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        Long certificadoId = (Long)id;

        switch (getTipoFormulario()) {
            case EDITANDO:
                certificado = certificadoDao.localizaPeloId(certificadoId);
                break;

            case ADICIONANDO:
                certificado = new DespesaCertificado();
                break;

            case RENOVANDO:
                certificado = new DespesaCertificado();
                certificadoQueEstaSendoSubstituido = certificadoDao.localizaPeloId(certificadoId);
                break;
        }

        return certificado;
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioCertificadosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraUi();
        configuraDropDownMenuDeCertificados();
        configuraDropDownMenuDePlacas();
    }

    @Override
    public void inicializaCamposDaView() {
        placaLayout = binding.fragFormularioCertificadoRefCavaloLayout;
        subEdit = binding.fragFormularioCertificadoSub;
        dataExpedicaoInputLayout = binding.fragFormularioCertificadoLayoutDataExpedicao;
        dataExpedicaoEdit = binding.fragFormularioCertificadoDataExpedicao;
        certificadoLayout = binding.fragFormularioCertificadoNomeDocumentoLayout;
        certificadoAutoComplete = binding.fragFormularioCertificadoNomeDocumento;
        placaAutoComplete = binding.fragFormularioCertificadoRefCavalo;
        anoReferenciaEdit = binding.fragFormularioCertificadoAnoReferencia;
        nDocumentoEdit = binding.fragFormularioCertificadoNumeroDocumento;
        valorDocumentoEdit = binding.fragFormularioCertificadoValorCertificado;
        dataVencimentoInputLayout = binding.fragFormularioCertificadoLayoutDataVencimento;
        dataVencimentoEdit = binding.fragFormularioCertificadoDataVencimento;
    }

    private void configuraUi() {
        Toolbar toolbar = binding.toolbar;
        configuraToolbar(toolbar);
        aplicaMascarasAosEditTexts();

        switch (getTipoFormulario()) {
            case EDITANDO:
                alteraUiParaModoEdicao();
                exibeObjetoEmCasoDeEdicao();
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(TITULO_APP_BAR_EDITANDO);
                break;

            case ADICIONANDO:
                alteraUiParaModoCriacao();
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(TITULO_APP_BAR_CRIANDO);
                break;

            case RENOVANDO:
                configuraUiParaModoRenovacao();
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(TITULO_APP_BAR_RENOVANDO);
                break;

        }

        if (tipoDespesa == INDIRETA) {
            placaLayout.setVisibility(GONE);
            placaAutoComplete.setVisibility(GONE);
        }

    }

    private void configuraUiParaModoRenovacao() {
        String subTitulo;

        if (tipoDespesa == DIRETA) {
   //         String placa = cavaloDao.localizaPeloId(certificadoQueEstaSendoSubstituido.getRefCavaloId()).getPlaca();
    //        subTitulo = SUB_TITULO_APP_BAR_RENOVANDO + " " + certificadoQueEstaSendoSubstituido.getTipoCertificado().getDescricao() + PARA_A_PLACA + placa;
    //        placaAutoComplete.setText(placa);

            placaAutoComplete.setVisibility(GONE);
            placaLayout.setVisibility(GONE);

        } else {
            subTitulo = SUB_TITULO_APP_BAR_RENOVANDO + " " + certificadoQueEstaSendoSubstituido.getTipoCertificado().getDescricao();

        }

//        subEdit.setText(subTitulo);
        certificadoAutoComplete.setText(certificadoQueEstaSendoSubstituido.getTipoCertificado().getDescricao());

        certificadoAutoComplete.setVisibility(GONE);
        certificadoLayout.setVisibility(GONE);
    }

    @Override
    public void alteraUiParaModoEdicao() {
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {
    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        if (tipoDespesa == DIRETA) {
  //          String placa = cavaloDao.localizaPeloId(certificado.getRefCavaloId()).getPlaca();
   //         placaAutoComplete.setText(placa);
        }

        dataExpedicaoEdit.setText(ConverteDataUtil.dataParaString(certificado.getDataDeEmissao()));
        certificadoAutoComplete.setText(certificado.getTipoCertificado().getDescricao());
        anoReferenciaEdit.setText(certificado.getAno());
        nDocumentoEdit.setText(String.valueOf(certificado.getNumeroDoDocumento()));
        valorDocumentoEdit.setText(certificado.getValorDespesa().toPlainString());
        dataVencimentoEdit.setText(ConverteDataUtil.dataParaString(certificado.getDataDeVencimento()));
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        configuraDataCalendario(dataExpedicaoInputLayout, dataExpedicaoEdit);
        configuraDataCalendario(dataVencimentoInputLayout, dataVencimentoEdit);
        MascaraDataUtil.MascaraData(dataExpedicaoEdit);
        MascaraDataUtil.MascaraData(dataVencimentoEdit);
        valorDocumentoEdit.addTextChangedListener(new MascaraMonetariaUtil(valorDocumentoEdit));
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(dataExpedicaoEdit, view);
        verificaCampoComData(dataVencimentoEdit, view);
        verificaCampo(anoReferenciaEdit);
        verificaCampo(nDocumentoEdit);
        verificaCampo(valorDocumentoEdit);
        verificaCampo(certificadoAutoComplete);

        if (!listaCertificadosEmString.contains(certificadoAutoComplete.getText().toString().toUpperCase(Locale.ROOT))) {
            certificadoAutoComplete.setError(INCORRETO);
            certificadoAutoComplete.getText().clear();
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
            MensagemUtil.snackBar(view, SELECIONE_UM_CERTIFICADO_VALIDO);
        }

        if (tipoDespesa == DIRETA) {
            if (!listaDePlacas.contains(placaAutoComplete.getText().toString().toUpperCase(Locale.ROOT))) {
                placaAutoComplete.setError(INCORRETO);
                placaAutoComplete.getText().clear();
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                MensagemUtil.snackBar(view, SELECIONE_UM_CAVALO_VALIDO);
            }
        }
    }

    @Override
    public void vinculaDadosAoObjeto() {
        certificado.setDataDeEmissao(ConverteDataUtil.stringParaData(dataExpedicaoEdit.getText().toString()));
        certificado.setAno(anoReferenciaEdit.getText().toString());
        certificado.setNumeroDoDocumento(Long.parseLong(nDocumentoEdit.getText().toString()));
        certificado.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorDocumentoEdit.getText().toString())));
        certificado.setDataDeVencimento(ConverteDataUtil.stringParaData(dataVencimentoEdit.getText().toString()));

        String nomeDoCertificado = certificadoAutoComplete.getText().toString();
        TipoCertificado tipo = TipoCertificado.valueOf(nomeDoCertificado);
        certificado.setTipoCertificado(tipo);
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        certificadoDao.adiciona(certificado);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        certificadoDao.deleta(certificado);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        certificadoDao.adiciona(certificado);
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        if (tipoDespesa == DIRETA) {
            Long cavaloId = cavaloDao.localizaPelaPlaca(placaAutoComplete.getText().toString()).getId();
            certificado.setRefCavaloId(cavaloId);
            certificado.setTipoDespesa(DIRETA);

        } else {
            certificado.setRefCavaloId(0L);
            certificado.setTipoDespesa(INDIRETA);

        }
        certificado.setValido(true);

        if (getTipoFormulario() == RENOVANDO) {
            certificadoQueEstaSendoSubstituido.setValido(false);
        }
        return null;
    }

    private void configuraDropDownMenuDeCertificados() {
        if (tipoDespesa == DIRETA) {
            listaCertificadosEmString = TipoCertificado.listaCertificados().stream()
                    .filter(t -> t.getTipoDespesa() == DIRETA)
                    .map(TipoCertificado::getDescricao)
                    .collect(Collectors.toList());
        } else {
            listaCertificadosEmString = TipoCertificado.listaCertificados().stream()
                    .filter(t -> t.getTipoDespesa() == INDIRETA)
                    .map(TipoCertificado::getDescricao)
                    .collect(Collectors.toList());
        }

        String[] certificados = listaCertificadosEmString.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, certificados);
        certificadoAutoComplete.setAdapter(adapter);
    }

    private void configuraDropDownMenuDePlacas() {
        String[] cavalos = listaDePlacas.toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, cavalos);
        placaAutoComplete.setAdapter(adapterCavalos);
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
                    procuraPorCertificadoDuplicado();
                }

                if (isCompletoParaSalvar()) {
                    new AlertDialog.Builder(this.getContext()).
                            setTitle(ADICIONANDO_REGISTRO_TITULO).
                            setMessage(ADICIONA_REGISTRO_TXT).
                            setPositiveButton(SIM, (dialog, which) -> {

                                vinculaDadosAoObjeto();
                                adicionaObjetoNoBancoDeDados();

                                if (getTipoFormulario() == ADICIONANDO) {
                                    requireActivity().setResult(RESULT_OK);
                                } else if (getTipoFormulario() == RENOVANDO) {
                                    requireActivity().setResult(RESULT_UPDATE);
                                }

                                requireActivity().finish();

                            }).
                            setNegativeButton(NAO, null).
                            show();
                } else {
                    setCompletoParaSalvar(true);
                }

                break;

            case R.id.menu_formulario_editar:
                verificaSeCamposEstaoPreenchidos(this.getView());

                if (isCompletoParaSalvar()) {
                    new AlertDialog.Builder(this.getContext()).
                            setTitle(EDITANDO_REGISTRO_TITULO).
                            setMessage(EDITANDO_REGISTRO_TXT).
                            setPositiveButton(SIM, (dialog, which) -> {
                                vinculaDadosAoObjeto();
                                editaObjetoNoBancoDeDados();
                                requireActivity().setResult(RESULT_EDIT);
                                requireActivity().finish();
                            }).
                            setNegativeButton(NAO, null).
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

    private void procuraPorCertificadoDuplicado() {
        String nomeDoCertificado = certificadoAutoComplete.getText().toString();
        TipoCertificado tipo = TipoCertificado.valueOf(nomeDoCertificado);

        if (getTipoFormulario() == ADICIONANDO && tipoDespesa == INDIRETA) {

            List<DespesaCertificado> listaDeCertificados = certificadoDao.listaPorTipo(INDIRETA);

            Optional<DespesaCertificado> buscaDuplicidade = listaDeCertificados.stream()
                    .filter(d -> d.isValido() && d.getTipoCertificado() == tipo)
                    .findAny();

            if (buscaDuplicidade.isPresent()) {
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                certificadoAutoComplete.setError("");
                String txt = JA_EXISTE_UM + tipo + ATIVO;
                Toast.makeText(this.requireContext(), txt, Toast.LENGTH_SHORT).show();
            }

        } else if (getTipoFormulario() == ADICIONANDO && tipoDespesa == DIRETA) {

            Long cavaloId = cavaloDao.localizaPelaPlaca(placaAutoComplete.getText().toString()).getId();

            List<DespesaCertificado> listaDeCertificadosPorCavalo = certificadoDao.listaPorCavaloId(cavaloId);

            Optional<DespesaCertificado> buscaDuplicidade = listaDeCertificadosPorCavalo.stream()
                    .filter(d -> d.isValido() && d.getTipoCertificado() == tipo)
                    .findAny();
            if (buscaDuplicidade.isPresent()) {
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                certificadoAutoComplete.setError("");
                String txt = JA_EXISTE_UM + tipo + ATIVO;
                Toast.makeText(this.requireContext(), txt, Toast.LENGTH_SHORT).show();
            }
        }
    }
}