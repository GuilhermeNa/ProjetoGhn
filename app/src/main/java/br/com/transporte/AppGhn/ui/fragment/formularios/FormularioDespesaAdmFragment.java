package br.com.transporte.AppGhn.ui.fragment.formularios;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.Locale;

import br.com.transporte.AppGhn.databinding.FragmentFormularioDespesaAdmBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioDespesaAdmFragment extends FormularioBaseFragment {
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de despesa que já existe.";
    private FragmentFormularioDespesaAdmBinding binding;
    private EditText dataEdit, descricaoEdit, valorEdit;
    private TextInputLayout dataLayout, refLayout;
    private AutoCompleteTextView refCavaloEdit;
    private CheckBox diretaBox, indiretaBox;
    private TextView despesaTxtView;
    private DespesasAdmDAO despesaDao;
    private CavaloDAO cavaloDao;
    private DespesaAdm despesa;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        despesaDao = new DespesasAdmDAO();
        cavaloDao = new CavaloDAO();

        int despesaId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(despesaId);
        despesa = (DespesaAdm) criaOuRecuperaObjeto(despesaId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioDespesaAdmBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuraCheckBox();
        configuraDropDownMenuDePlacas();
    }

    private void configuraCheckBox() {
        diretaBox.setOnClickListener(v -> {
            desmarcaBox(indiretaBox);
            refLayout.setVisibility(View.VISIBLE);
            despesaTxtView.setError(null);
        });

        indiretaBox.setOnClickListener(v -> {
            desmarcaBox(diretaBox);
            refLayout.setVisibility(View.GONE);
            despesaTxtView.setError(null);
        });
    }

    private void configuraDropDownMenuDePlacas() {
        String[] cavalos = cavaloDao.listaPlacas().toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, cavalos);
        refCavaloEdit.setAdapter(adapterCavalos);
    }

    @Override
    public void inicializaCamposDaView() {
        refLayout = binding.fragFormularioDespesaFinanceiraReferenciaCavaloLayout;
        diretaBox = binding.fragFormularioDespesaBoxDireta;
        indiretaBox = binding.fragFormularioDespesaBoxIndireta;
        dataLayout = binding.fragFormularioDespesaFinanceiraLayoutData;
        dataEdit = binding.fragFormularioDespesaFinanceiraData;
        refCavaloEdit = binding.fragFormularioDespesaFinanceiraReferenciaCavalo;
        descricaoEdit = binding.fragFormularioDespesaFinanceiraDescricao;
        valorEdit = binding.fragFormularioDespesaFinanceiraValor;
        despesaTxtView = binding.fragFormularioDespesaTituloCard;
    }

    @Override
    public Object criaOuRecuperaObjeto(int id) {
        if(getTipoFormulario() == TipoFormulario.EDITANDO){
            despesa = despesaDao.localizaPeloId(id);
        } else {
            despesa = new DespesaAdm();
        }
        return despesa;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subEdit = binding.fragFormularioDespesaFinanceiraSub;
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        if(despesa.getTipoDespesa() == TipoDespesa.DIRETA){
            String placa = cavaloDao.localizaPeloId(despesa.getRefCavalo()).getPlaca();
            refCavaloEdit.setText(placa);
            diretaBox.setChecked(true);
        } else if (despesa.getTipoDespesa() == TipoDespesa.INDIRETA){
            indiretaBox.setChecked(true);
        }
        dataEdit.setText(FormataDataUtil.dataParaString(despesa.getData()));
        valorEdit.setText(despesa.getValorDespesa().toPlainString());
        descricaoEdit.setText(despesa.getDescricao());
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
        verificaCampo(valorEdit);
        verificaCampo(descricaoEdit);


        if(diretaBox.isChecked()){
            verificaCampo(refCavaloEdit);

            if (!cavaloDao.listaPlacas().contains(refCavaloEdit.getText().toString().toUpperCase(Locale.ROOT))) {
                refCavaloEdit.setError("Incorreto");
                refCavaloEdit.getText().clear();
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                MensagemUtil.snackBar(view, "Selecione um cavalo válido");
            }
        }

        if (!diretaBox.isChecked() && !indiretaBox.isChecked()) {
            despesaTxtView.setError("");
            snackBar(view, "Escolha uma tipo de despesa");
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void vinculaDadosAoObjeto() {
        despesa.setData(FormataDataUtil.stringParaData(dataEdit.getText().toString()));
        despesa.setDescricao(descricaoEdit.getText().toString());
        despesa.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));

        if(diretaBox.isChecked()){
            despesa.setTipoDespesa(TipoDespesa.DIRETA);
            int cavaloId = cavaloDao.retornaCavaloAtravesDaPlaca(refCavaloEdit.getText().toString().toUpperCase(Locale.ROOT)).getId();
            despesa.setRefPlacaCavalo(cavaloId);
        } else if (indiretaBox.isChecked()){
            despesa.setTipoDespesa(TipoDespesa.INDIRETA);
            despesa.setRefPlacaCavalo(0);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        despesaDao.edita(despesa);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        despesaDao.deleta(despesa.getId());
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        despesaDao.adiciona(despesa);
    }

    @Override
    public int configuraObjetoNaCriacao() {

        return 0;
    }
}