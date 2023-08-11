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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
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
import br.com.transporte.AppGhn.databinding.FragmentFormularioCertificadosBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasCertificadoDAO;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioCertificadosFragment extends FormularioBaseFragment {
    private FragmentFormularioCertificadosBinding binding;
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de certificado que já existe.";
    private static final String SUB_TITULO_APP_BAR_RENOVANDO = "Você está renovando um ";
    private EditText dataExpedicaoEdit, anoReferenciaEdit, nDocumentoEdit, valorDocumentoEdit, dataVencimentoEdit;
    private TextInputLayout dataExpedicaoInputLayout, dataVencimentoInputLayout, placaLayout, certificadoLayout;
    private AutoCompleteTextView certificadoAutoComplete, placaAutoComplete;
    private DespesaCertificado certificadoQueEstaSendoSubstituido, certificado;
    private List<String> listaCertificadosEmString;
    private DespesasCertificadoDAO certificadoDao;
    private TipoDespesa tipoDespesa;
    private CavaloDAO cavaloDao;
    private TextView subEdit;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        certificadoDao = new DespesasCertificadoDAO();
        cavaloDao = new CavaloDAO();

        int certificadoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        configuraTipoDeRecebimento();
        certificado = (DespesaCertificado) criaOuRecuperaObjeto(certificadoId);
    }

    private void configuraTipoDeRecebimento() {
        Bundle bundle = getArguments();
        tipoDespesa = (TipoDespesa) bundle.getSerializable(CHAVE_DESPESA);

        TipoFormulario tipoRequisicao = (TipoFormulario) bundle.getSerializable(CHAVE_REQUISICAO);
        switch (tipoRequisicao) {
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
    public Object criaOuRecuperaObjeto(int id) {
        switch (getTipoFormulario()) {
            case EDITANDO:
                certificado = certificadoDao.localizaPeloId(id);
                break;

            case ADICIONANDO:
                certificado = new DespesaCertificado();
                break;

            case RENOVANDO:
                certificado = new DespesaCertificado();
                certificadoQueEstaSendoSubstituido = certificadoDao.localizaPeloId(id);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            String placa = cavaloDao.localizaPeloId(certificadoQueEstaSendoSubstituido.getRefCavalo()).getPlaca();
            subTitulo = SUB_TITULO_APP_BAR_RENOVANDO + " " + certificadoQueEstaSendoSubstituido.getTipoCertificado().getDescricao() + ", para a placa " + placa;
            placaAutoComplete.setText(placa);

            placaAutoComplete.setVisibility(GONE);
            placaLayout.setVisibility(GONE);

        } else {
            subTitulo = SUB_TITULO_APP_BAR_RENOVANDO + " " + certificadoQueEstaSendoSubstituido.getTipoCertificado().getDescricao();

        }

        subEdit.setText(subTitulo);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        if (tipoDespesa == DIRETA) {
            String placa = cavaloDao.localizaPeloId(certificado.getRefCavalo()).getPlaca();
            placaAutoComplete.setText(placa);
        }

        dataExpedicaoEdit.setText(FormataDataUtil.dataParaString(certificado.getDataDeEmissao()));
        certificadoAutoComplete.setText(certificado.getTipoCertificado().getDescricao());
        anoReferenciaEdit.setText(certificado.getAno());
        nDocumentoEdit.setText(String.valueOf(certificado.getNumeroDoDocumento()));
        valorDocumentoEdit.setText(certificado.getValorDespesa().toPlainString());
        dataVencimentoEdit.setText(FormataDataUtil.dataParaString(certificado.getDataDeVencimento()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            certificadoAutoComplete.setError("Incorreto");
            certificadoAutoComplete.getText().clear();
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
            MensagemUtil.snackBar(view, "Selecione um certificado válido");
        }

        if (tipoDespesa == DIRETA) {
            if (!cavaloDao.listaPlacas().contains(placaAutoComplete.getText().toString().toUpperCase(Locale.ROOT))) {
                placaAutoComplete.setError("Incorreto");
                placaAutoComplete.getText().clear();
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                MensagemUtil.snackBar(view, "Selecione um cavalo válido");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void vinculaDadosAoObjeto() {
        certificado.setDataDeEmissao(FormataDataUtil.stringParaData(dataExpedicaoEdit.getText().toString()));
        certificado.setAno(anoReferenciaEdit.getText().toString());
        certificado.setNumeroDoDocumento(Long.parseLong(nDocumentoEdit.getText().toString()));
        certificado.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorDocumentoEdit.getText().toString())));
        certificado.setDataDeVencimento(FormataDataUtil.stringParaData(dataVencimentoEdit.getText().toString()));

        String nomeDoCertificado = certificadoAutoComplete.getText().toString();
        TipoCertificado tipo = TipoCertificado.valueOf(nomeDoCertificado);
        certificado.setTipoCertificado(tipo);
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        certificadoDao.edita(certificado);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        certificadoDao.deleta(certificado.getId());
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        certificadoDao.adiciona(certificado);
    }

    @Override
    public int configuraObjetoNaCriacao() {
        if (tipoDespesa == DIRETA) {
            int cavaloId = cavaloDao.retornaCavaloAtravesDaPlaca(placaAutoComplete.getText().toString()).getId();
            certificado.setRefCavalo(cavaloId);
            certificado.setTipoDespesa(DIRETA);

        } else {
            certificado.setRefCavalo(0);
            certificado.setTipoDespesa(INDIRETA);

        }
        certificado.setValido(true);

        if (getTipoFormulario() == RENOVANDO) {
            certificadoQueEstaSendoSubstituido.setValido(false);
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, certificados);
        certificadoAutoComplete.setAdapter(adapter);
    }

    private void configuraDropDownMenuDePlacas() {
        String[] cavalos = cavaloDao.listaPlacas().toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, cavalos);
        placaAutoComplete.setAdapter(adapterCavalos);
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void procuraPorCertificadoDuplicado() {

        String nomeDoCertificado = certificadoAutoComplete.getText().toString();
        TipoCertificado tipo = TipoCertificado.valueOf(nomeDoCertificado);

        if (getTipoFormulario() == ADICIONANDO && tipoDespesa == INDIRETA) {

            List<DespesaCertificado> listaDeCertificados = certificadoDao.listaTodosOsCertificadosIndiretos();

            Optional<DespesaCertificado> buscaDuplicidade = listaDeCertificados.stream()
                    .filter(d -> d.isValido() && d.getTipoCertificado() == tipo)
                    .findAny();

            if (buscaDuplicidade.isPresent()) {
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                certificadoAutoComplete.setError("");
                Toast.makeText(this.requireContext(), "Já existe um " + tipo + " ativo.", Toast.LENGTH_SHORT).show();
            }

        } else if (getTipoFormulario() == ADICIONANDO && tipoDespesa == DIRETA) {

            int cavaloId = cavaloDao.retornaCavaloAtravesDaPlaca(placaAutoComplete.getText().toString()).getId();

            List<DespesaCertificado> listaDeCertificadosPorCavalo = certificadoDao.listaFiltradaPorCavalo(cavaloId);

            Optional<DespesaCertificado> buscaDuplicidade = listaDeCertificadosPorCavalo.stream()
                    .filter(d -> d.isValido() && d.getTipoCertificado() == tipo)
                    .findAny();
            if (buscaDuplicidade.isPresent()) {
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                certificadoAutoComplete.setError("");
                Toast.makeText(this.requireContext(), "Já existe um " + tipo + " ativo.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}