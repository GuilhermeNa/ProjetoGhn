package br.com.transporte.AppGhn.ui.fragment.formularios.abastecimento;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.util.MascaraMonetariaUtil.formatPriceSave;
import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioAbastecimentoBinding;
import br.com.transporte.AppGhn.exception.DataInvalida;
import br.com.transporte.AppGhn.exception.MarcacaoKmInvalida;
import br.com.transporte.AppGhn.exception.RegistroDuplicado;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.enums.TipoAbastecimento;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioBaseFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioAbastecimentoFragment extends FormularioBaseFragment {
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de abastecimento que já existe.";
    private FragmentFormularioAbastecimentoBinding binding;
    private EditText dataEdit, postoEdit, marcacaoKmEdit, qntLitrosEdit, valorLitroEdit, totalAbastEdit;
    private CheckBox totalBox, parcialBox;
    private TextView tipoAbastecimentoTxt;
    private TextInputLayout dataLayout;
    private RoomCustosAbastecimentoDao abastecimentoDao;
    private CustosDeAbastecimento abastecimento;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        abastecimentoDao = GhnDataBase.getInstance(requireContext()).getRoomCustosAbastecimentoDao();

        long abastecimentoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(abastecimentoId);
        abastecimento = (CustosDeAbastecimento) criaOuRecuperaObjeto(abastecimentoId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioAbastecimentoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuraCheckBox();
    }

    private void configuraCheckBox() {
        totalBox.setOnClickListener(v -> {
            desmarcaBox(parcialBox);
        });
        parcialBox.setOnClickListener(v -> {
            desmarcaBox(totalBox);
        });
    }

    @Override
    public void inicializaCamposDaView() {
        dataEdit = binding.fragFormularioAbastecimentoData;
        postoEdit = binding.fragFormularioAbastecimentoPosto;
        marcacaoKmEdit = binding.fragFormularioAbastecimentoMarcacaoKm;
        qntLitrosEdit = binding.fragFormularioAbastecimentoQntLitros;
        valorLitroEdit = binding.fragFormularioAbastecimentoValorLitro;
        totalAbastEdit = binding.fragFormularioAbastecimentoTotalAbastecimento;
        totalBox = binding.fragFormularioAbastecimentoAVista;
        parcialBox = binding.fragFormularioAbastecimentoAPrazo;
        tipoAbastecimentoTxt = binding.fragFormularioAbastecimentoFormaPagamento;
        dataLayout = binding.fragFormularioAbastecimentoLayoutData;
    }


    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        long idAbastecimento = (long)id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            abastecimento = abastecimentoDao.localizaPeloId(idAbastecimento);
        } else {
            abastecimento = new CustosDeAbastecimento();
        }
        return abastecimento;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxt = binding.fragFormularioAbastecimentoSub;
        subTxt.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {}

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(ConverteDataUtil.dataParaString(abastecimento.getData()));
        postoEdit.setText(abastecimento.getPosto());
        marcacaoKmEdit.setText(abastecimento.getMarcacaoKm().toPlainString());
        qntLitrosEdit.setText(abastecimento.getQuantidadeLitros().toPlainString());
        valorLitroEdit.setText(abastecimento.getValorLitro().toPlainString());
        totalAbastEdit.setText(abastecimento.getValorCusto().toPlainString());
        if (abastecimento.getTipo() == TipoAbastecimento.TOTAL) {
            totalBox.setChecked(true);
        } else {
            parcialBox.setChecked(true);
        }
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        MascaraDataUtil.MascaraData(dataEdit);
        marcacaoKmEdit.addTextChangedListener(new MascaraMonetariaUtil(marcacaoKmEdit));
        qntLitrosEdit.addTextChangedListener(new MascaraMonetariaUtil(qntLitrosEdit));
        valorLitroEdit.addTextChangedListener(new MascaraMonetariaUtil(valorLitroEdit));
        totalAbastEdit.addTextChangedListener(new MascaraMonetariaUtil(totalAbastEdit));
        configuraDataCalendario(dataLayout, dataEdit);

    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(dataEdit, view);
        verificaCampo(postoEdit);
        verificaCampo(marcacaoKmEdit);
        verificaCampo(qntLitrosEdit);
        verificaCampo(valorLitroEdit);
        verificaCampo(totalAbastEdit);
        verificaCheckBox(view);

        if (isCompletoParaSalvar()) {
            BigDecimal marcacaoASalvar = new BigDecimal(MascaraMonetariaUtil.formatPriceSave(marcacaoKmEdit.getText().toString()));
            LocalDate dataASalvar = ConverteDataUtil.stringParaData(dataEdit.getText().toString());

            try {
                MarcacaoKm.verificaMarcacaoKm(dataASalvar, marcacaoASalvar, getReferenciaDeCavalo(CHAVE_ID_CAVALO));
            } catch (MarcacaoKmInvalida | DataInvalida  | RegistroDuplicado e) {
                e.printStackTrace();
                e.getMessage();
                setCompletoParaSalvar(false);
                switch (e.getMessage()) {
                    case "Data Ok, Km =":
                        Log.d("teste", "Erro -> Data Ok, Km =");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Data Ok, Km X":
                        Log.d("teste", "Erro -> Data Ok, Km X");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Data X":
                        Log.d("teste", "Erro -> Data X");
                        dataEdit.setError("Corrigir");
                        break;

                    case "Data =, Km =":
                        Log.d("teste", "Data =, Km =");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Data =, Km X, -1 =":
                        Log.d("teste", "Data =, Km X, -1 =");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Data =, Km X, -1 x":
                        Log.d("teste", "Data =, Km X, -1 x");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Encontra X, Data ok, Km = acima":
                        Log.d("teste", "Encontra X, Data ok, Km = acima");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Encontra X, Data ok, Km x acima":
                        Log.d("teste", "Encontra X, Data ok, Km x acima");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Encontra X, Data =, Km = acima":
                        Log.d("teste", "Encontra X, Data =, Km = acima");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Encontra X, Data =, Km = acima, -1 = ":
                        Log.d("teste", "Encontra X, Data =, Km = acima, -1 = ");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Encontra X, Data =, Km = acima, -1 x ":
                        Log.d("teste", "Encontra X, Data =, Km = acima, -1 x ");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Encontra OK, Data OK, Km X":
                        Log.d("teste", "Encontra OK, Data OK, Km X");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Encontra OK, Data =, Km X":
                        Log.d("teste", "Encontra OK, Data =, Km X");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;

                    case "Encontra OK, Data =, Km =":
                        Log.d("teste", "Encontra OK, Data =, Km =");
                        dataEdit.setError("Corrigir");
                        marcacaoKmEdit.setError("Corrigir");
                        break;
                }
            }
        }
    }

    private void verificaCheckBox(View view) {
        if (!totalBox.isChecked() && !parcialBox.isChecked()) {
            tipoAbastecimentoTxt.setError("");
            snackBar(view, "Escolha uma forma de pagamento");
            if (isCompletoParaSalvar()) setCompletoParaSalvar(false);
        }
    }

    @Override
    public void vinculaDadosAoObjeto() {
        abastecimento.setData(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        abastecimento.setPosto(postoEdit.getText().toString());
        abastecimento.setMarcacaoKm(new BigDecimal(formatPriceSave(marcacaoKmEdit.getText().toString())));
        abastecimento.setQuantidadeLitros(new BigDecimal(formatPriceSave(qntLitrosEdit.getText().toString())));
        abastecimento.setValorLitro(new BigDecimal(formatPriceSave(valorLitroEdit.getText().toString())));
        abastecimento.setValorCusto(new BigDecimal(formatPriceSave(totalAbastEdit.getText().toString())));
        defineTipo();
    }

    private void defineTipo() {
        if (totalBox.isChecked()) {
            abastecimento.setTipo(TipoAbastecimento.TOTAL);
            abastecimento.setFlagAbastecimentoTotal(true);
            tipoAbastecimentoTxt.setError(null);
        } else if (parcialBox.isChecked()) {
            abastecimento.setTipo(TipoAbastecimento.PARCIAL);
            abastecimento.setFlagAbastecimentoTotal(false);
            tipoAbastecimentoTxt.setError(null);
        }
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        abastecimentoDao.adiciona(abastecimento);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        abastecimentoDao.deleta(abastecimento);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        abastecimentoDao.adiciona(abastecimento);
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        abastecimento.setRefCavaloId(getReferenciaDeCavalo(CHAVE_ID_CAVALO));
        abastecimento.setApenasAdmEdita(false);
        return null;
    }
}
