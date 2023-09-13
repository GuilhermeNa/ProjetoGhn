package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_TIPO_DESPESA;
import static br.com.transporte.AppGhn.ui.fragment.formularios.FormularioSeguroFrotaFragment.INCORRETO;
import static br.com.transporte.AppGhn.ui.fragment.formularios.FormularioSeguroFrotaFragment.SELECIONE_UM_CAVALO_VALIDO;
import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioDespesaAdmBinding;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioDespesaAdmFragment extends FormularioBaseFragment {
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de despesa que já existe.";
    public static final String ESCOLHA_UMA_TIPO_DE_DESPESA = "Escolha uma tipo de despesa";
    private FragmentFormularioDespesaAdmBinding binding;
    private EditText dataEdit, descricaoEdit, valorEdit;
    private TextInputLayout dataLayout, refLayout;
    private AutoCompleteTextView refCavaloEdit;
    private CheckBox diretaBox, indiretaBox;
    private TextView despesaTxtView;
    private RoomDespesaAdmDao despesaDao;
    private RoomCavaloDao cavaloDao;
    private DespesaAdm despesa;
    private List<String> listaDePlacas;
    private TipoDespesa tipoDespesa;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        despesaDao = dataBase.getRoomDespesaAdmDao();
        cavaloDao = dataBase.getRoomCavaloDao();
        listaDePlacas = FiltraCavalo.listaDePlacas(cavaloDao.todos());

        long despesaId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(despesaId);
        despesa = (DespesaAdm) criaOuRecuperaObjeto(despesaId);
        defineTipoDespesa(despesaId);
    }

    private void defineTipoDespesa(long id) {
        if(id == 0) {
            Bundle bundle = getArguments();
            tipoDespesa = (TipoDespesa) bundle.getSerializable(CHAVE_TIPO_DESPESA);
        }
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
            refLayout.setVisibility(VISIBLE);
            despesaTxtView.setError(null);
        });

        indiretaBox.setOnClickListener(v -> {
            desmarcaBox(diretaBox);
            refLayout.setVisibility(View.GONE);
            despesaTxtView.setError(null);
        });
    }

    private void configuraDropDownMenuDePlacas() {
        String[] cavalos = listaDePlacas.toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, cavalos);
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
    public Object criaOuRecuperaObjeto(Object id) {
        Long despesaId = (Long)id;
        if(getTipoFormulario() == TipoFormulario.EDITANDO){
            despesa = despesaDao.localizaPeloId(despesaId);
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
        if(tipoDespesa == TipoDespesa.DIRETA){
            diretaBox.setChecked(true);
            refLayout.setVisibility(VISIBLE);
        } else {
            indiretaBox.setChecked(true);
        }
    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        if(despesa.getTipoDespesa() == TipoDespesa.DIRETA){
            String placa = cavaloDao.localizaPeloId(despesa.getRefCavaloId()).getPlaca();
            refLayout.setVisibility(VISIBLE);
            refCavaloEdit.setText(placa);
            diretaBox.setChecked(true);
        } else if (despesa.getTipoDespesa() == TipoDespesa.INDIRETA){
            indiretaBox.setChecked(true);
        }
        dataEdit.setText(ConverteDataUtil.dataParaString(despesa.getData()));
        valorEdit.setText(despesa.getValorDespesa().toPlainString());
        descricaoEdit.setText(despesa.getDescricao());
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
        verificaCampo(valorEdit);
        verificaCampo(descricaoEdit);


        if(diretaBox.isChecked()){
            verificaCampo(refCavaloEdit);

            if (!listaDePlacas.contains(refCavaloEdit.getText().toString().toUpperCase(Locale.ROOT))) {
                refCavaloEdit.setError(INCORRETO);
                refCavaloEdit.getText().clear();
                if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
                MensagemUtil.snackBar(view, SELECIONE_UM_CAVALO_VALIDO);
            }
        }

        if (!diretaBox.isChecked() && !indiretaBox.isChecked()) {
            despesaTxtView.setError("");
            snackBar(view, ESCOLHA_UMA_TIPO_DE_DESPESA);
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }

    }

    @Override
    public void vinculaDadosAoObjeto() {
        despesa.setData(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        despesa.setDescricao(descricaoEdit.getText().toString());
        despesa.setValorDespesa(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));

        if(diretaBox.isChecked()){
            despesa.setTipoDespesa(TipoDespesa.DIRETA);
            Long cavaloId = cavaloDao.localizaPelaPlaca(refCavaloEdit.getText().toString().toUpperCase(Locale.ROOT)).getId();
            despesa.setRefCavaloId(cavaloId);
        } else if (indiretaBox.isChecked()){
            despesa.setTipoDespesa(TipoDespesa.INDIRETA);
            despesa.setRefCavaloId(0L);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        despesaDao.adiciona(despesa);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        despesaDao.deleta(despesa);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        despesaDao.adiciona(despesa);
    }

    @Override
    public Long configuraObjetoNaCriacao() {

        return null;
    }

}