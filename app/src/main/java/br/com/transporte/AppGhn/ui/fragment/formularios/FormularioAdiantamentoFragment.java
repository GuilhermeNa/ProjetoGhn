package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ADIANTAMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.databinding.FragmentFormularioAdiantamentoBinding;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioAdiantamentoFragment extends FormularioBaseFragment {
    private FragmentFormularioAdiantamentoBinding binding;
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de adiantamento que já existe.";
    private AdiantamentoDAO adiantamentoDao;
    private Adiantamento adiantamento;
    private EditText dataEdit, valorEdit, descricaoEdit;
    private TextInputLayout dataLayout;
    private Cavalo cavalo;
    private TextView placa, motorista;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioAdiantamentoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        adiantamentoDao = new AdiantamentoDAO();

        cavalo = recebeReferenciaExternaDeCavalo();
        int adiantamentoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(adiantamentoId);
        adiantamento = (Adiantamento) criaOuRecuperaObjeto(adiantamentoId);

        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuracoesAdicionaisUi();

    }

    private void configuracoesAdicionaisUi() {
        placa.setText(cavalo.getPlaca());
        motorista.setText(cavalo.getMotorista().getNome());
    }

    private Cavalo recebeReferenciaExternaDeCavalo() {
        CavaloDAO cavaloDao = new CavaloDAO();
        int cavaloId = getArguments().getInt(CHAVE_ID_CAVALO);

        return cavaloDao.localizaPeloId(cavaloId);
    }

    @Override
    public void inicializaCamposDaView() {
        dataLayout = binding.dataLayout;
        dataEdit = binding.data;
        valorEdit = binding.valor;
        descricaoEdit = binding.descricao;
        placa = binding.placa;
        motorista = binding.motorista;
    }

    @Override
    public Object criaOuRecuperaObjeto(int id) {
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            adiantamento = adiantamentoDao.localizaPeloId(id);
        } else {
            adiantamento = new Adiantamento();
        }
        return adiantamento;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxt = binding.subTitulo;
        subTxt.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(ConverteDataUtil.dataParaString(adiantamento.getData()));
        valorEdit.setText(FormataNumerosUtil.formataNumero(adiantamento.getValorTotal()));
        descricaoEdit.setText(adiantamento.getDescricao());
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
    }

    @Override
    public void vinculaDadosAoObjeto() {
        adiantamento.setData(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        adiantamento.setValorTotal(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(valorEdit.getText().toString())));
        adiantamento.setDescricao(descricaoEdit.getText().toString());
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        adiantamentoDao.edita(adiantamento);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        adiantamentoDao.adiciona(adiantamento);
    }

    @Override
    public int configuraObjetoNaCriacao() {
        adiantamento.setAdiantamentoJaFoiPago(false);
        adiantamento.setRefCavalo(cavalo.getId());
        adiantamento.setRefMotorista(cavalo.getMotorista().getId());
        adiantamento.setSaldoRestituido(new BigDecimal(BigInteger.ZERO));
        return 0;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        adiantamentoDao.deleta(adiantamento.getId());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(CANCELAR_REGISTRO_TITULO).
                        setMessage(CANCELA_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {
                            requireActivity().setResult(RESULT_CANCELED);
                            requireActivity().finish();
                        }).
                        setNegativeButton(NAO, null).show();
                break;


            case R.id.menu_formulario_salvar:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(ADICIONANDO_REGISTRO_TITULO).
                        setMessage(ADICIONA_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {
                            verificaSeCamposEstaoPreenchidos(this.getView());
                            if (isCompletoParaSalvar()) {
                                vinculaDadosAoObjeto();
                                adicionaObjetoNoBancoDeDados();
                                requireActivity().setResult(RESULT_OK);
                                requireActivity().finish();
                            } else {
                                setCompletoParaSalvar(true);
                            }
                        }).
                        setNegativeButton(NAO, null).
                        show();
                break;


            case R.id.menu_formulario_editar:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(EDITANDO_REGISTRO_TITULO).
                        setMessage(EDITANDO_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {

                            verificaSeCamposEstaoPreenchidos(this.getView());

                            if (isCompletoParaSalvar()) {
                                vinculaDadosAoObjeto();
                                editaObjetoNoBancoDeDados();
                                Intent intent = new Intent();
                                intent.putExtra(CHAVE_ADIANTAMENTO, adiantamento);
                                requireActivity().setResult(RESULT_EDIT, intent);
                                requireActivity().finish();
                            } else {
                                setCompletoParaSalvar(true);
                            }

                        }).
                        setNegativeButton(NAO, null).
                        show();
                break;


            case R.id.menu_formulario_apagar:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(APAGA_REGISTRO_TITULO).
                        setMessage(APAGA_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.putExtra(CHAVE_ID, adiantamento.getId());
                            requireActivity().setResult(RESULT_DELETE, intent);

                            deletaObjetoNoBancoDeDados();

                            requireActivity().finish();
                        }).
                        setNegativeButton(NAO, null).show();
                break;
        }
        return false;
    }
}