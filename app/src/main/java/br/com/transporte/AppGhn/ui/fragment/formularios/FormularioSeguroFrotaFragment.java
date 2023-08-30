package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasSeguroDAO;
import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.databinding.FragmentFormularioSeguroAutoBinding;
import br.com.transporte.AppGhn.model.ParcelaDeSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioSeguroFrotaFragment extends FormularioBaseFragment {
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de seguro que já existe.";
    public static final String SUB_TITULO_APP_BAR_RENOVANDO = "Você está renovando o seguro do cavalo ";
    public static final String JA_EXISTE_UM_SEGURO_FROTA_PARA_ESTE_ITEM = "Já existe um seguro frota para este item";
    public static final String SELECIONE_UM_CAVALO_VALIDO = "Selecione um cavalo válido";
    public static final String INCORRETO = "Incorreto";
    private EditText dataInicialEdit, dataFinalEdit, valorEdit, parcelasEdit, valorParcelasEdit, companhiaEdit, nContratoEdit, coberturaCascoEdit,
            rcfMateriaisEdit, rcfCorporaisEdit, appMorteEdit, appInvalidezEdit, danosMoraisEdit, vidrosEdit, assistenciaEdit, dataPrimeiraParcelaEdit;
    private TextInputLayout dataFinalLayout, dataInicialLayout, dataPrimeiraParcelaLayout, refCavaloLayout;
    private DespesaComSeguroFrota seguro, seguroQueEstaSendoSubstituido;
    private FragmentFormularioSeguroAutoBinding binding;
    private AutoCompleteTextView refCavaloAutoComplete;
    private DespesasSeguroDAO seguroDao;
    private CavaloDAO cavaloDao;
    private TextView subEdit;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cavaloDao = new CavaloDAO();
        seguroDao = new DespesasSeguroDAO();

        int seguroId = verificaSeRecebeDadosExternos(CHAVE_ID);
        configuraTipoDeRecebimento();
        seguro = (DespesaComSeguroFrota) criaOuRecuperaObjeto(seguroId);

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
    public Object criaOuRecuperaObjeto(int id) {
        switch (getTipoFormulario()) {
            case EDITANDO:
                seguro = (DespesaComSeguroFrota) seguroDao.localizaPeloId(id);
                break;

            case ADICIONANDO:
                seguro = new DespesaComSeguroFrota();
                break;

            case RENOVANDO:
                seguro = new DespesaComSeguroFrota();
                seguroQueEstaSendoSubstituido = (DespesaComSeguroFrota) seguroDao.localizaPeloId(id);
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
        binding = FragmentFormularioSeguroAutoBinding.inflate(getLayoutInflater());
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
        configuraDropDownMenuDeCavalos();

    }

    private void configuraDropDownMenuDeCavalos() {
        String[] cavalos = cavaloDao.listaPlacas().toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, cavalos);
        refCavaloAutoComplete.setAdapter(adapterCavalos);
    }

    @Override
    public void inicializaCamposDaView() {
        subEdit = binding.fragFormularioSeguroSub;
        dataPrimeiraParcelaLayout = binding.dataPrimeiraParcelaLayout;
        dataPrimeiraParcelaEdit = binding.dataPrimeiraParcela;
        refCavaloLayout = binding.fragFormularioSeguroLayoutRefCaminhao;
        refCavaloAutoComplete = binding.fragFormularioSeguroRefCaminhao;
        dataInicialLayout = binding.fragFormularioSeguroLayoutDataInicial;
        dataInicialEdit = binding.fragFormularioSeguroDataInicial;
        dataFinalLayout = binding.fragFormularioSeguroLayoutDataFinal;
        dataFinalEdit = binding.fragFormularioSeguroDataFinal;
        valorEdit = binding.fragFormularioSeguroValorTotal;
        parcelasEdit = binding.fragFormularioSeguroParcelas;
        valorParcelasEdit = binding.fragFormularioSeguroValorParcela;
        companhiaEdit = binding.fragFormularioSeguroCompanhia;
        nContratoEdit = binding.fragFormularioSeguroNumeroContrato;
        coberturaCascoEdit = binding.fragFormularioSeguroCoberturaCasco;
        rcfMateriaisEdit = binding.fragFormularioSeguroRcfDanosMateriais;
        rcfCorporaisEdit = binding.fragFormularioSeguroRcfDanosCorporais;
        appMorteEdit = binding.fragFormularioSeguroAppMorte;
        appInvalidezEdit = binding.fragFormularioSeguroAppInvalidez;
        danosMoraisEdit = binding.fragFormularioSeguroDanosMorais;
        vidrosEdit = binding.fragFormularioSeguroVidros;
        assistenciaEdit = binding.fragFormularioSeguroAssistencia24h;
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
        String placaDoCavaloQueEstaTendoOSeguroRenovado = cavaloDao.localizaPeloId(seguroQueEstaSendoSubstituido.getRefCavalo()).getPlaca();
        String subtitulo = SUB_TITULO_APP_BAR_RENOVANDO + placaDoCavaloQueEstaTendoOSeguroRenovado;
        subEdit.setText(subtitulo);

        refCavaloAutoComplete.setText(placaDoCavaloQueEstaTendoOSeguroRenovado);

        refCavaloAutoComplete.setVisibility(GONE);
        refCavaloLayout.setVisibility(GONE);
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
        String placaDoCavaloQueEstaSendoEditado = cavaloDao.localizaPeloId(seguro.getRefCavalo()).getPlaca();
        
        refCavaloAutoComplete.setText(placaDoCavaloQueEstaSendoEditado);
        dataPrimeiraParcelaEdit.setText(ConverteDataUtil.dataParaString(seguro.getDataPrimeiraParcela()));
        dataInicialEdit.setText(ConverteDataUtil.dataParaString(seguro.getDataInicial()));
        dataFinalEdit.setText(ConverteDataUtil.dataParaString(seguro.getDataFinal()));
        valorEdit.setText(FormataNumerosUtil.formataNumero(seguro.getValorDespesa()));
        parcelasEdit.setText(String.valueOf(seguro.getParcelas()));
        valorParcelasEdit.setText(FormataNumerosUtil.formataNumero(seguro.getValorParcela()));
        companhiaEdit.setText(seguro.getCompanhia());
        nContratoEdit.setText(String.valueOf(seguro.getnContrato()));
        coberturaCascoEdit.setText(seguro.getCoberturaCasco());
        rcfMateriaisEdit.setText(FormataNumerosUtil.formataNumero(seguro.getCoberturaRcfMateriais()));
        rcfCorporaisEdit.setText(FormataNumerosUtil.formataNumero(seguro.getCoberturaRcfCorporais()));
        appMorteEdit.setText(FormataNumerosUtil.formataNumero(seguro.getCoberturaAppMorte()));
        appInvalidezEdit.setText(FormataNumerosUtil.formataNumero(seguro.getCoberturaAppInvalidez()));
        danosMoraisEdit.setText(FormataNumerosUtil.formataNumero(seguro.getCoberturaDanosMorais()));
        vidrosEdit.setText(seguro.getCoberturaVidros());
        assistenciaEdit.setText(seguro.getAssistencia24H());
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        configuraDataCalendario(dataInicialLayout, dataInicialEdit);
        configuraDataCalendario(dataFinalLayout, dataFinalEdit);
        configuraDataCalendario(dataPrimeiraParcelaLayout, dataPrimeiraParcelaEdit);

        MascaraDataUtil.MascaraData(dataInicialEdit);
        MascaraDataUtil.MascaraData(dataFinalEdit);
        MascaraDataUtil.MascaraData(dataPrimeiraParcelaEdit);

        valorEdit.addTextChangedListener(new MascaraMonetariaUtil(valorEdit));
        valorParcelasEdit.addTextChangedListener(new MascaraMonetariaUtil(valorParcelasEdit));
        rcfMateriaisEdit.addTextChangedListener(new MascaraMonetariaUtil(rcfMateriaisEdit));
        rcfCorporaisEdit.addTextChangedListener(new MascaraMonetariaUtil(rcfCorporaisEdit));
        appMorteEdit.addTextChangedListener(new MascaraMonetariaUtil(appMorteEdit));
        appInvalidezEdit.addTextChangedListener(new MascaraMonetariaUtil(appInvalidezEdit));
        danosMoraisEdit.addTextChangedListener(new MascaraMonetariaUtil(danosMoraisEdit));
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(dataInicialEdit, view);
        verificaCampoComData(dataFinalEdit, view);
        verificaCampoComData(dataPrimeiraParcelaEdit, view);
        verificaCampo(refCavaloAutoComplete);
        verificaCampo(valorEdit);
        verificaCampo(parcelasEdit);
        verificaCampo(valorParcelasEdit);
        verificaCampo(companhiaEdit);
        verificaCampo(nContratoEdit);
        verificaCampo(coberturaCascoEdit);
        verificaCampo(rcfMateriaisEdit);
        verificaCampo(rcfCorporaisEdit);
        verificaCampo(appMorteEdit);
        verificaCampo(appInvalidezEdit);
        verificaCampo(danosMoraisEdit);
        verificaCampo(assistenciaEdit);
        verificaSeAPlacaDigitadaEhValida();
    }

    private void verificaSeAPlacaDigitadaEhValida() {
        boolean placaInexistente = !cavaloDao.listaPlacas().contains(refCavaloAutoComplete.getText().toString().toUpperCase(Locale.ROOT));

        if (placaInexistente) {
            refCavaloAutoComplete.setError(INCORRETO);
            refCavaloAutoComplete.getText().clear();
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
            MensagemUtil.toast(this.requireContext(), SELECIONE_UM_CAVALO_VALIDO);
        }
    }

    @Override
    public void vinculaDadosAoObjeto() {
        seguro.setDataInicial(ConverteDataUtil.stringParaData(dataInicialEdit.getText().toString()));
        seguro.setDataFinal(ConverteDataUtil.stringParaData(dataFinalEdit.getText().toString()));
        seguro.setDataPrimeiraParcela(ConverteDataUtil.stringParaData(dataPrimeiraParcelaEdit.getText().toString()));
        seguro.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));
        seguro.setParcelas(Integer.parseInt(parcelasEdit.getText().toString()));
        seguro.setValorParcela(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorParcelasEdit.getText().toString())));
        seguro.setCompanhia(companhiaEdit.getText().toString());
        seguro.setnContrato(Integer.parseInt(nContratoEdit.getText().toString()));
        seguro.setCoberturaCasco(coberturaCascoEdit.getText().toString());
        seguro.setCoberturaRcfMateriais(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(rcfMateriaisEdit.getText().toString())));
        seguro.setCoberturaRcfCorporais(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(rcfCorporaisEdit.getText().toString())));
        seguro.setCoberturaAppMorte(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(appMorteEdit.getText().toString())));
        seguro.setCoberturaAppInvalidez(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(appInvalidezEdit.getText().toString())));
        seguro.setCoberturaDanosMorais(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(danosMoraisEdit.getText().toString())));
        seguro.setCoberturaVidros(vidrosEdit.getText().toString());
        seguro.setAssistencia24H(assistenciaEdit.getText().toString());
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        seguroDao.edita(seguro);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        seguroDao.deleta(seguro.getId());
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        int cavaloId = configuraObjetoNaCriacao();
        seguroDao.adiciona(seguro);
        configuraParcelamento(cavaloId);
    }

    @Override
    public int configuraObjetoNaCriacao() {
        String placa = refCavaloAutoComplete.getText().toString();
        int cavaloId = cavaloDao.retornaCavaloAtravesDaPlaca(placa).getId();

        seguro.setTipoDespesa(DIRETA);
        seguro.setRefCavalo(cavaloId);
        seguro.setValido(true);

        if(getTipoFormulario() == RENOVANDO){
            seguroQueEstaSendoSubstituido.setValido(false);
        }
        return cavaloId;
    }

    private void configuraParcelamento(int cavaloId){
        int chaveEstrangeira = seguro.getId();

        String campoParcelas = parcelasEdit.getText().toString();
        int numeroDeParcelas = getNumeroDeParcelas(campoParcelas);

        String campoValorParcela = valorParcelasEdit.getText().toString();
        String valorParcelaFormatada = MascaraMonetariaUtil.formatPriceSave(campoValorParcela);
        BigDecimal valorDeCadaParcela = new BigDecimal(valorParcelaFormatada);

        String campoDataPrimeiraParcela = dataPrimeiraParcelaEdit.getText().toString();
        LocalDate dataPrimeiraParcela = ConverteDataUtil.stringParaData(campoDataPrimeiraParcela);

        ParcelaDeSeguro parcelaDeSeguro;
        ParcelaDeSeguroDAO parcelaDeSeguroDao = new ParcelaDeSeguroDAO();

        for(int i = 0; i < numeroDeParcelas; i++){
            parcelaDeSeguro = new ParcelaDeSeguro();
            parcelaDeSeguro.setTipoDespesa(DIRETA);
            parcelaDeSeguro.setRefCavaloId(cavaloId);
            parcelaDeSeguro.setRefSeguro(chaveEstrangeira);
            parcelaDeSeguro.setNumeroDaParcela(i+1);
            parcelaDeSeguro.setValor(valorDeCadaParcela);
            parcelaDeSeguro.setValido(true);
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
                    procuraPorSeguroDuplicado();
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

    private void procuraPorSeguroDuplicado(){

        if(getTipoFormulario() == ADICIONANDO){

            String placa = refCavaloAutoComplete.getText().toString();
            int cavaloId = cavaloDao.retornaCavaloAtravesDaPlaca(placa).getId();

            List<DespesaComSeguroFrota> listaDeSegurosFrotaAtivos =  seguroDao.listaSegurosFrota();

            Optional<DespesaComSeguroFrota> buscaDuplicidade = listaDeSegurosFrotaAtivos.stream()
                    .filter(s -> s.isValido() && s.getRefCavalo() == cavaloId).findAny();

            if( buscaDuplicidade.isPresent()) {
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                refCavaloAutoComplete.setError("");
                MensagemUtil.toast(this.requireContext(), JA_EXISTE_UM_SEGURO_FROTA_PARA_ESTE_ITEM);
            }
        }
    }

}