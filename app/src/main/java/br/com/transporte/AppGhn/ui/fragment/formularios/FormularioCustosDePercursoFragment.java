package br.com.transporte.AppGhn.ui.fragment.formularios;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.util.MascaraMonetariaUtil.formatPriceSave;
import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.databinding.FragmentFormularioCustosPercursoBinding;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioCustosDePercursoFragment extends FormularioBaseFragment {
    private FragmentFormularioCustosPercursoBinding binding;
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de abastecimento que já existe.";
    private EditText dataEdit, valorEdit, descricaoEdit;
    private TextView reembolso;
    private CheckBox naoBox, simBox;
    private CustosDePercurso custo;
    private CustosDePercursoDAO custosDao;
    private TextInputLayout dataLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        custosDao = new CustosDePercursoDAO();
        int custoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(custoId);
        custo = (CustosDePercurso) criaOuRecuperaObjeto(custoId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioCustosPercursoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuraCheckBox();
    }

    private void configuraCheckBox() {
        naoBox.setOnClickListener(v -> desmarcaBox(simBox));
        simBox.setOnClickListener(v -> desmarcaBox(naoBox));
    }

    @Override
    public void inicializaCamposDaView() {
        dataEdit = binding.fragFormularioDespesaData;
        valorEdit = binding.fragFormularioDespesaValor;
        descricaoEdit = binding.fragFormularioDespesaDescricao;
        naoBox = binding.fragFormularioDespesaBoxNao;
        simBox = binding.fragFormularioDespesaBoxSim;
        reembolso = binding.fragFormularioDespesaReembolso;
        dataLayout = binding.fragFormularioDespesaLayoutData;
    }

    @Override
    public Object criaOuRecuperaObjeto(int id) {
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            custo = custosDao.localizaPeloId(id);
        } else {
            custo = new CustosDePercurso();
        }
        return custo;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxt = binding.fragFormularioDespesaSub;
        subTxt.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(FormataDataUtil.dataParaString(custo.getData()));
        valorEdit.setText(custo.getValorCusto().toPlainString());
        descricaoEdit.setText(custo.getDescricao());
        if (custo.getTipo() == TipoCustoDePercurso.NAO_REEMBOLSAVEL) {
            naoBox.setChecked(true);
        } else {
            simBox.setChecked(true);
        }
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

        if (!simBox.isChecked() && !naoBox.isChecked()) {
            reembolso.setError("");
            snackBar(view, "Escolha uma forma de pagamento");
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void vinculaDadosAoObjeto() {
        custo.setData(FormataDataUtil.stringParaData(dataEdit.getText().toString()));
        custo.setValorCusto(new BigDecimal(formatPriceSave(valorEdit.getText().toString())));
        custo.setDescricao(descricaoEdit.getText().toString());
        if (simBox.isChecked()) {
            custo.setTipo(TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO);
            reembolso.setError(null);
        } else if (naoBox.isChecked()) {
            custo.setTipo(TipoCustoDePercurso.NAO_REEMBOLSAVEL);
            reembolso.setError(null);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        custosDao.edita(custo);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        custosDao.adiciona(custo);
    }

    @Override
    public int configuraObjetoNaCriacao() {
        custo.setRefCavalo(getReferenciaDeCavalo(CHAVE_ID_CAVALO));
        custo.setApenasAdmEdita(false);
        return 0;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        custosDao.deleta(custo.getId());
    }

}