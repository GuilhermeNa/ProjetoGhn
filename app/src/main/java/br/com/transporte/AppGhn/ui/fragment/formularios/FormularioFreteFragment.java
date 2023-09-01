package br.com.transporte.AppGhn.ui.fragment.formularios;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.util.BigDecimalConstantes.BIG_DECIMAL_DEZ;
import static br.com.transporte.AppGhn.util.MascaraMonetariaUtil.formatPriceSave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioFreteBinding;
import br.com.transporte.AppGhn.exception.ValorInvalidoException;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.helpers.FreteHelper;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioFreteFragment extends FormularioBaseFragment {
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de frete que já existe.";
    private FragmentFormularioFreteBinding binding;
    private EditText dataEdit, origemEdit, destinoEdit, empresaEdit, cargaEdit, freteBrutoEdit,
            descontosEdit, seguroCargaEdit, pesoEdit;
    private Frete frete;
    private RoomFreteDao freteDao;
    private TextInputLayout dataLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        freteDao = GhnDataBase.getInstance(requireContext()).getRoomFreteDao();
        long freteId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(freteId);
        frete = (Frete) criaOuRecuperaObjeto(freteId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentFormularioFreteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
    }

    @Override
    public void inicializaCamposDaView() {
        dataEdit = binding.fragFormularioFreteData;
        origemEdit = binding.fragFormularioFreteOrigem;
        destinoEdit = binding.fragFormularioFreteDestino;
        empresaEdit = binding.fragFormularioFreteEmpresa;
        cargaEdit = binding.fragFormularioFreteCarga;
        pesoEdit = binding.fragFormularioFretePeso;
        freteBrutoEdit = binding.fragFormularioFreteBruto;
        descontosEdit = binding.fragFormularioFreteDescontos;
        dataLayout = binding.fragFormularioFreteLayoutData;
        seguroCargaEdit = binding.fragFormularioSeguroCarga;
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        long freteId = (long)id;

        if(getTipoFormulario() == TipoFormulario.EDITANDO){
            frete = freteDao.localizaPeloId(freteId);
        } else {
            frete = new Frete();
        }
        return frete;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subEdit = binding.fragFormularioFreteSub;
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {}

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(ConverteDataUtil.dataParaString(frete.getData()));
        origemEdit.setText(frete.getOrigem());
        destinoEdit.setText(frete.getDestino());
        empresaEdit.setText(frete.getEmpresa());
        cargaEdit.setText(frete.getCarga());
        pesoEdit.setText(String.valueOf(frete.getPeso()));
        freteBrutoEdit.setText(frete.getFreteBruto().toPlainString());
        descontosEdit.setText(frete.getDescontos().toPlainString());
        seguroCargaEdit.setText(frete.getSeguroDeCarga().toPlainString());
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        configuraDataCalendario(dataLayout, dataEdit);
        MascaraDataUtil.MascaraData(dataEdit);
        pesoEdit.addTextChangedListener(new MascaraMonetariaUtil(pesoEdit));
        freteBrutoEdit.addTextChangedListener(new MascaraMonetariaUtil(freteBrutoEdit));
        descontosEdit.addTextChangedListener(new MascaraMonetariaUtil(descontosEdit));
        seguroCargaEdit.addTextChangedListener(new MascaraMonetariaUtil(seguroCargaEdit));
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(dataEdit, view);
        verificaCampo(origemEdit);
        verificaCampo(destinoEdit);
        verificaCampo(empresaEdit);
        verificaCampo(cargaEdit);
        verificaCampo(freteBrutoEdit);
        verificaCampo(descontosEdit);
        verificaCampo(seguroCargaEdit);
        verificaCampo(pesoEdit);
    }

    @Override
    public void vinculaDadosAoObjeto() {
        frete.setData(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        frete.setOrigem(origemEdit.getText().toString());
        frete.setDestino(destinoEdit.getText().toString());
        frete.setEmpresa(empresaEdit.getText().toString());
        frete.setCarga(cargaEdit.getText().toString());
        frete.setPeso(new BigDecimal(formatPriceSave(pesoEdit.getText().toString())));
        frete.setFreteBruto(new BigDecimal(formatPriceSave(freteBrutoEdit.getText().toString())));
        frete.setDescontos(new BigDecimal(formatPriceSave(descontosEdit.getText().toString())));
        frete.setSeguroDeCarga(new BigDecimal(formatPriceSave(seguroCargaEdit.getText().toString())));
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        freteDao.adiciona(frete);
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        freteDao.deleta(frete);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        freteDao.adiciona(frete);
    }

    @Override
    public int configuraObjetoNaCriacao() {
        Cavalo cavalo = recebeReferenciaExternaDeCavalo(CHAVE_ID_CAVALO);
        BigDecimal comissaoPercentual;
        try{
            comissaoPercentual = FreteHelper.vinculaComissaoAplicada(cavalo.getComissaoBase());
            frete.setComissaoPercentualAplicada(comissaoPercentual);
        } catch (ValorInvalidoException e) {
            e.printStackTrace();
            comissaoPercentual = BIG_DECIMAL_DEZ;
        }

        BigDecimal valorComissao = FreteHelper.calculaComissao(comissaoPercentual, this.frete.getFreteBruto());
        frete.setComissaoAoMotorista(valorComissao);

        BigDecimal valorLiquido = FreteHelper.calculaFreteLiquidoAReceber(this.frete.getFreteBruto(), this.frete.getDescontos(), this.frete.getSeguroDeCarga());
        frete.setFreteLiquidoAReceber(valorLiquido);

        frete.setComissaoJaFoiPaga(false);
        frete.setFreteJaFoiPago(false);
        frete.setApenasAdmEdita(false);
        frete.setRefCavaloId(cavalo.getId());
        return 0;
    }

}
