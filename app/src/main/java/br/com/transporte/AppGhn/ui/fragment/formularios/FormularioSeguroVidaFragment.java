package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.DespesasSeguroDAO;
import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.databinding.FragmentFormularioSeguroVidaBinding;
import br.com.transporte.AppGhn.model.ParcelaDeSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioSeguroVidaFragment extends FormularioBaseFragment {
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de seguro que já existe.";
    public static final String SUB_TITULO_APP_BAR_RENOVANDO = "Você está renovando um seguro.";
    private FragmentFormularioSeguroVidaBinding binding;
    private EditText dataInicialEdit, dataFinalEdit, valorPremioEdit, valorParcela, coberturaSociosEdit, coberturaMotoristasEdit,
            parcelasEdit, ciaEdit, nContratoEdit, dataPrimeiraParcela;
    private TextInputLayout dataInicialLayout, dataFinalLayout, dataPrimeiraParcelaLayout;
    private DespesaComSeguroDeVida seguro, seguroQueEstaSendoSubstituido;
    private DespesasSeguroDAO seguroDao;
    private TextView subEdit;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seguroDao = new DespesasSeguroDAO();

        int seguroId = verificaSeRecebeDadosExternos(CHAVE_ID);
        configuraTipoDeRecebimento();
        seguro = (DespesaComSeguroDeVida) criaOuRecuperaObjeto(seguroId);
    }

    private void configuraTipoDeRecebimento() {
        Bundle bundle = getArguments();
        TipoFormulario tipoRequisicao = (TipoFormulario) Objects.requireNonNull(bundle).getSerializable(CHAVE_REQUISICAO);

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
    public Object criaOuRecuperaObjeto(int seguroId) {

        switch (getTipoFormulario()) {
            case EDITANDO:
                seguro = (DespesaComSeguroDeVida) seguroDao.localizaPeloId(seguroId);
                break;

            case ADICIONANDO:
                seguro = new DespesaComSeguroDeVida();
                break;

            case RENOVANDO:
                seguro = new DespesaComSeguroDeVida();
                seguroQueEstaSendoSubstituido = (DespesaComSeguroDeVida) seguroDao.localizaPeloId(seguroId);
                break;
        }

        return seguro;
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
        inicializaCamposDaView();
        configuraUi();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
    }

    @Override
    public void inicializaCamposDaView() {
        dataPrimeiraParcelaLayout = binding.dataPrimeiraParcelaLayout;
        dataPrimeiraParcela = binding.dataPrimeiraParcela;
        subEdit = binding.fragFormularioSeguroSub;
        dataInicialLayout = binding.fragFormularioSeguroLayoutDataInicial;
        dataFinalLayout = binding.fragFormularioSeguroLayoutDataFinal;
        dataInicialEdit = binding.fragFormularioSeguroDataInicial;
        dataFinalEdit = binding.fragFormularioSeguroDataFinal;
        valorPremioEdit = binding.fragFormularioSeguroValorTotal;
        parcelasEdit = binding.fragFormularioSeguroParcelas;
        valorParcela = binding.fragFormularioSeguroValorParcela;
        ciaEdit = binding.fragFormularioSeguroCompanhia;
        nContratoEdit = binding.fragFormularioSeguroNumeroContrato;
        coberturaSociosEdit = binding.fragFormularioSeguroCoberturaSocios;
        coberturaMotoristasEdit = binding.fragFormularioSeguroCoberturaMotoristas;
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
                alteraUiParaModoRenovacao();
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(TITULO_APP_BAR_RENOVANDO);
                break;

        }
    }

    private void alteraUiParaModoRenovacao() {
        subEdit.setText(SUB_TITULO_APP_BAR_RENOVANDO);
    }

    @Override
    public void alteraUiParaModoEdicao() {
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {}

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataPrimeiraParcela.setText(ConverteDataUtil.dataParaString(seguro.getDataPrimeiraParcela()));
        dataInicialEdit.setText(ConverteDataUtil.dataParaString(seguro.getDataInicial()));
        dataFinalEdit.setText(ConverteDataUtil.dataParaString(seguro.getDataFinal()));
        valorPremioEdit.setText(FormataNumerosUtil.formataNumero(seguro.getValorDespesa()));
        parcelasEdit.setText(String.valueOf(seguro.getParcelas()));
        valorParcela.setText(FormataNumerosUtil.formataNumero(seguro.getValorParcela()));
        ciaEdit.setText(seguro.getCompanhia());
        nContratoEdit.setText(String.valueOf(seguro.getnContrato()));
        coberturaSociosEdit.setText(FormataNumerosUtil.formataNumero(seguro.getCoberturaSocios()));
        coberturaMotoristasEdit.setText(FormataNumerosUtil.formataNumero(seguro.getCoberturaMotoristas()));
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        MascaraDataUtil.MascaraData(dataInicialEdit);
        MascaraDataUtil.MascaraData(dataFinalEdit);
        MascaraDataUtil.MascaraData(dataPrimeiraParcela);

        valorPremioEdit.addTextChangedListener(new MascaraMonetariaUtil(valorPremioEdit));
        valorParcela.addTextChangedListener(new MascaraMonetariaUtil(valorParcela));
        coberturaSociosEdit.addTextChangedListener(new MascaraMonetariaUtil(coberturaSociosEdit));
        coberturaMotoristasEdit.addTextChangedListener(new MascaraMonetariaUtil(coberturaMotoristasEdit));

        configuraDataCalendario(dataInicialLayout, dataInicialEdit);
        configuraDataCalendario(dataFinalLayout, dataFinalEdit);
        configuraDataCalendario(dataPrimeiraParcelaLayout, dataPrimeiraParcela);
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(dataInicialEdit, view);
        verificaCampoComData(dataFinalEdit, view);
        verificaCampo(valorPremioEdit);
        verificaCampo(parcelasEdit);
        verificaCampo(valorParcela);
        verificaCampo(ciaEdit);
        verificaCampo(nContratoEdit);
        verificaCampo(coberturaSociosEdit);
        verificaCampo(coberturaMotoristasEdit);
        verificaCampo(dataPrimeiraParcela);
    }

    @Override
    public void vinculaDadosAoObjeto() {
        seguro.setDataPrimeiraParcela(ConverteDataUtil.stringParaData(dataPrimeiraParcela.getText().toString()));
        seguro.setDataInicial(ConverteDataUtil.stringParaData(dataInicialEdit.getText().toString()));
        seguro.setDataFinal(ConverteDataUtil.stringParaData(dataFinalEdit.getText().toString()));
        seguro.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorPremioEdit.getText().toString())));
        seguro.setParcelas(Integer.parseInt(parcelasEdit.getText().toString()));
        seguro.setValorParcela(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorParcela.getText().toString())));
        seguro.setCompanhia(ciaEdit.getText().toString());
        seguro.setnContrato(Integer.parseInt(nContratoEdit.getText().toString()));
        seguro.setCoberturaSocios(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(coberturaSociosEdit.getText().toString())));
        seguro.setCoberturaMotoristas(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(coberturaMotoristasEdit.getText().toString())));
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        seguroDao.edita(seguro);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        seguroDao.adiciona(seguro);
        configuraParcelamento();
    }

    private void configuraParcelamento() {
        int chaveEstrangeira = seguro.getId();

        String campoParcelas = parcelasEdit.getText().toString();
        int numeroDeParcelas = getNumeroDeParcelas(campoParcelas);

        String campoValorParcela = valorParcela.getText().toString();
        String valorParcelaFormatada = MascaraMonetariaUtil.formatPriceSave(campoValorParcela);
        BigDecimal valorDeCadaParcela = new BigDecimal(valorParcelaFormatada);

        String campoDataPrimeiraParcela = dataPrimeiraParcela.getText().toString();
        LocalDate dataPrimeiraParcela = ConverteDataUtil.stringParaData(campoDataPrimeiraParcela);

        ParcelaDeSeguro parcelaDeSeguro;
        ParcelaDeSeguroDAO parcelaDeSeguroDao = new ParcelaDeSeguroDAO();

        for(int i = 0; i < numeroDeParcelas; i++){
            parcelaDeSeguro = new ParcelaDeSeguro();
            parcelaDeSeguro.setRefCavalo(0);
            parcelaDeSeguro.setRefSeguro(chaveEstrangeira);
            parcelaDeSeguro.setNumeroDaParcela(i+1);
            parcelaDeSeguro.setValor(valorDeCadaParcela);
            parcelaDeSeguro.setValido(true);
            parcelaDeSeguro.setTipoDespesa(INDIRETA);
            parcelaDeSeguro.setPaga(false);
            parcelaDeSeguro.setData(dataPrimeiraParcela.plusMonths(i));

            parcelaDeSeguroDao.adiciona(parcelaDeSeguro);
        }
    }

    private int getNumeroDeParcelas(String campoParcelas) {
        int parcelas = Integer.parseInt(campoParcelas);

        if(parcelas > 0 && parcelas <= 12){
            return parcelas;
        }
        return 12;
    }

    @Override
    public int configuraObjetoNaCriacao() {
        seguro.setTipoDespesa(INDIRETA);
        seguro.setValido(true);
        seguro.setRefCavalo(0);

        if(getTipoFormulario() == RENOVANDO){
            seguroQueEstaSendoSubstituido.setValido(false);
        }
        return 0;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        seguroDao.deleta(seguro.getId());
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
                            deletaParcelasNoBancoDeDados();
                            requireActivity().setResult(RESULT_DELETE);
                            requireActivity().finish();
                        }).
                        setNegativeButton(NAO, null).show();

                break;

        }
        return false;
    }

    private void deletaParcelasNoBancoDeDados() {
        ParcelaDeSeguroDAO parcelaDao = new ParcelaDeSeguroDAO();

        List<ParcelaDeSeguro> listaDeParcelasParaApagar = parcelaDao.listaParcelasDoSeguro(seguro.getId());
        for(ParcelaDeSeguro p: listaDeParcelasParaApagar){
            parcelaDao.delete(p.getId());
        }
    }

}