package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.INCORRETO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioImpostosBinding;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ImpostoRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioImpostoViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.FormularioImpostoViewModelFactory;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioImpostosFragment extends FormularioBaseFragment {
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de imposto que já existe.";
    public static final String IPVA = "IPVA";
    public static final String SELECIONE_UM_IMPOSTO_VALIDO = "Selecione um imposto válido";
    private FragmentFormularioImpostosBinding binding;
    private TextInputLayout dataLayout, layoutRef;
    private AutoCompleteTextView nomeImpostoAutoComplete, referenciaEdit;
    private EditText valorEdit, dataEdit;
    private RoomDespesaImpostoDao impostoDao;
    private DespesasDeImposto imposto;
    private RoomCavaloDao cavaloDao;
    private FormularioImpostoViewModel viewModel;
    private List<Cavalo> listaCavalos;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        long impostoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(impostoId);
        imposto = (DespesasDeImposto) criaOuRecuperaObjeto(impostoId);
    }

    private void inicializaViewModel() {
        final ImpostoRepository impostoRepository = new ImpostoRepository(requireContext());
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final FormularioImpostoViewModelFactory factory = new FormularioImpostoViewModelFactory(impostoRepository, cavaloRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioImpostoViewModel.class);
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        Long impostoId = (Long) id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            viewModel.localizaImposto(impostoId).observe(this,
                    despesasDeImposto -> {
                        if (despesasDeImposto != null) {
                            viewModel.impostoArmazenado = despesasDeImposto;
                            imposto = despesasDeImposto;
                            buscaCavalos();

                        }
                    });
        } else {
            imposto = new DespesasDeImposto();
        }
        return imposto;
    }

    private void buscaCavalos() {
        viewModel.buscaCavalos().observe(this,
                resource -> {
                    if (resource.getDado() != null) {
                        listaCavalos = resource.getDado();
                        configuraDropDownMenuDeReferenciasParaCavalos(resource.getDado());
                        bind();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioImpostosBinding.inflate(getLayoutInflater());
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
        configuraDropDownMenuDeImpostos();
        configuraVisibilidadeDoCampoReferenciasParaCavalos();
    }

    @Override
    public void inicializaCamposDaView() {
        dataLayout = binding.fragFormularioImpostoLayoutData;
        dataEdit = binding.fragFormularioImpostoData;
        layoutRef = binding.fragFormularioImpostoReferenciaLayout;
        referenciaEdit = binding.fragFormularioImpostoReferencia;
        nomeImpostoAutoComplete = binding.fragFormularioImpostoNome;
        valorEdit = binding.fragFormularioImpostoValor;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxtView = binding.fragFormularioImpostoSub;
        subTxtView.setText(SUB_TITULO_APP_BAR_EDITANDO);
        if (imposto.getRefCavaloId() > 0) {
            layoutRef.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void alteraUiParaModoCriacao() {
    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        if (imposto.getNome().toUpperCase(Locale.ROOT).equals(IPVA)) {
            String placa = Objects.requireNonNull(FiltraCavalo.localizaPeloId(listaCavalos, imposto.getRefCavaloId())).getPlaca();
            referenciaEdit.setText(placa);
        }
        dataEdit.setText(ConverteDataUtil.dataParaString(imposto.getData()));
        nomeImpostoAutoComplete.setText(imposto.getNome());
        valorEdit.setText(imposto.getValorDespesa().toPlainString());
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
        verificaCampo(nomeImpostoAutoComplete);
        verificaCampo(valorEdit);

        if (layoutRef.getVisibility() == View.VISIBLE) {
            verificaCampo(referenciaEdit);
        }

        if (!TipoDeImposto.listaDeImpostos().contains(nomeImpostoAutoComplete.getText().toString().toUpperCase(Locale.ROOT))) {
            nomeImpostoAutoComplete.setError(INCORRETO);
            nomeImpostoAutoComplete.getText().clear();
            if (!isCompletoParaSalvar()) setCompletoParaSalvar(true);
            MensagemUtil.snackBar(view, SELECIONE_UM_IMPOSTO_VALIDO);
        }

    }

    @Override
    public void vinculaDadosAoObjeto() {
        imposto.setData(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        imposto.setNome(nomeImpostoAutoComplete.getText().toString());
        imposto.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));

        if (layoutRef.getVisibility() == View.VISIBLE) {
            Long cavaloId = FiltraCavalo.localizaPelaPlaca(listaCavalos, referenciaEdit.getText().toString()).getId();
            imposto.setRefCavaloId(cavaloId);
            imposto.setTipoDespesa(TipoDespesa.DIRETA);
        } else {
            imposto.setRefCavaloId(0L);
            imposto.setTipoDespesa(TipoDespesa.INDIRETA);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        viewModel.salva(imposto).observe(this,
                ignore -> {
                    requireActivity().setResult(RESULT_EDIT);
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
        viewModel.salva(imposto).observe(this,
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

    private void configuraVisibilidadeDoCampoReferenciasParaCavalos() {
        nomeImpostoAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(IPVA)) {
                    layoutRef.setVisibility(View.VISIBLE);
                } else {
                    if (layoutRef.getVisibility() == View.VISIBLE) {
                        layoutRef.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void configuraDropDownMenuDeReferenciasParaCavalos(List<Cavalo> listaCavalos) {
        List<String> listaDePlacas = FiltraCavalo.listaDePlacas(listaCavalos);
        String[] cavalos = listaDePlacas.toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, cavalos);
        referenciaEdit.setAdapter(adapterCavalos);
    }

    private void configuraDropDownMenuDeImpostos() {
        String[] impostos = TipoDeImposto.listaDeImpostos().toArray(new String[0]);
        ArrayAdapter<String> adapterImpostos = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, impostos);
        nomeImpostoAutoComplete.setAdapter(adapterImpostos);
    }
}