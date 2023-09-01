package br.com.transporte.AppGhn.ui.fragment.formularios;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.formularios.FormularioSeguroFrotaFragment.INCORRETO;

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

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasImpostoDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioImpostosBinding;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        impostoDao = dataBase.getRoomDespesaImpostoDao();
        cavaloDao = dataBase.getRoomCavaloDao();
        int impostoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(impostoId);
        imposto = (DespesasDeImposto) criaOuRecuperaObjeto(impostoId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioImpostosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuraDropDownMenuDeImpostos();
        configuraDropDownMenuDeReferenciasParaCavalos();
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
    public Object criaOuRecuperaObjeto(Object id) {
        Long impostoId = (Long)id;

        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            imposto = impostoDao.localizaPeloId(impostoId);
        } else {
            imposto = new DespesasDeImposto();
        }
        return imposto;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxtView = binding.fragFormularioImpostoSub;
        subTxtView.setText(SUB_TITULO_APP_BAR_EDITANDO);
        if(imposto.getRefCavalo() > 0){
            layoutRef.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        if(imposto.getNome().toUpperCase(Locale.ROOT).equals(IPVA)){
            String placa = cavaloDao.localizaPeloId(imposto.getRefCavalo()).getPlaca();
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
            Integer cavaloId = cavaloDao.localizaPelaPlaca(referenciaEdit.getText().toString()).getId();
            imposto.setRefCavalo(cavaloId);
            imposto.setTipoDespesa(TipoDespesa.DIRETA);
        } else {
            imposto.setRefCavalo(0);
            imposto.setTipoDespesa(TipoDespesa.INDIRETA);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        impostoDao.adiciona(imposto);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        impostoDao.deleta(imposto);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        impostoDao.adiciona(imposto);
    }

    @Override
    public int configuraObjetoNaCriacao() {

        return 0;
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

    private void configuraDropDownMenuDeReferenciasParaCavalos() {
        List<String> listaDePlacas = FiltraCavalo.listaDePlacas(cavaloDao.todos());
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