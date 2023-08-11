package br.com.transporte.AppGhn.ui.fragment.formularios;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.Locale;

import br.com.transporte.AppGhn.databinding.FragmentFormularioImpostosBinding;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasImpostoDAO;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioImpostosFragment extends FormularioBaseFragment {
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de imposto que já existe.";
    private FragmentFormularioImpostosBinding binding;
    private TextInputLayout dataLayout, layoutRef;
    private AutoCompleteTextView nomeImpostoAutoComplete, referenciaEdit;
    private EditText valorEdit, dataEdit;
    private DespesasImpostoDAO impostoDao;
    private DespesasDeImposto imposto;
    private CavaloDAO cavaloDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        impostoDao = new DespesasImpostoDAO();
        cavaloDao = new CavaloDAO();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
    public Object criaOuRecuperaObjeto(int id) {
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            imposto = impostoDao.localizaPeloid(id);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        if(imposto.getNome().toUpperCase(Locale.ROOT).equals("IPVA")){
            String placa = cavaloDao.localizaPeloId(imposto.getRefCavalo()).getPlaca();
            referenciaEdit.setText(placa);
        }

        dataEdit.setText(FormataDataUtil.dataParaString(imposto.getData()));
        nomeImpostoAutoComplete.setText(imposto.getNome());
        valorEdit.setText(imposto.getValorDespesa().toPlainString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        if (!DespesasDeImposto.listaDeImpostos().contains(nomeImpostoAutoComplete.getText().toString().toUpperCase(Locale.ROOT))) {
            nomeImpostoAutoComplete.setError("Incorreto");
            nomeImpostoAutoComplete.getText().clear();
            if (!isCompletoParaSalvar()) setCompletoParaSalvar(true);
            MensagemUtil.snackBar(view, "Selecione um imposto válido");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void vinculaDadosAoObjeto() {
        imposto.setData(FormataDataUtil.stringParaData(dataEdit.getText().toString()));
        imposto.setNome(nomeImpostoAutoComplete.getText().toString());
        imposto.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));

        if (layoutRef.getVisibility() == View.VISIBLE) {
            int cavaloId = cavaloDao.retornaCavaloAtravesDaPlaca(referenciaEdit.getText().toString()).getId();
            imposto.setRefCavalo(cavaloId);
            imposto.setTipoDespesa(TipoDespesa.DIRETA);
        } else {
            imposto.setRefCavalo(0);
            imposto.setTipoDespesa(TipoDespesa.INDIRETA);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        impostoDao.edita(imposto);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        impostoDao.deleta(imposto.getId());
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
                if (s.toString().equals("IPVA")) {
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
        cavaloDao = new CavaloDAO();
        String[] cavalos = cavaloDao.listaPlacas().toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, cavalos);
        referenciaEdit.setAdapter(adapterCavalos);
    }

    private void configuraDropDownMenuDeImpostos() {
        String[] impostos = DespesasDeImposto.listaDeImpostos().toArray(new String[0]);
        ArrayAdapter<String> adapterImpostos = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, impostos);
        nomeImpostoAutoComplete.setAdapter(adapterImpostos);
    }
}