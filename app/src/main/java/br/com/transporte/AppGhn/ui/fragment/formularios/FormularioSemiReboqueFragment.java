package br.com.transporte.AppGhn.ui.fragment.formularios;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioSemiReboqueBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.dao.SemiReboqueDAO;

public class FormularioSemiReboqueFragment extends FormularioBaseFragment {
    private FragmentFormularioSemiReboqueBinding binding;
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de semi-reboque que já existe.";
    private EditText placaEdit, marcaEdit, anoEdit, modeloEdit, corEdit, renavamEdit, chassiEdit;
    private RoomSemiReboqueDao srDao;
    private SemiReboque sr;
    private Cavalo cavalo;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        srDao = GhnDataBase.getInstance(this.requireContext()).getRoomReboqueDao();
        cavalo = recebeReferenciaExternaDeCavalo(CHAVE_ID_CAVALO);
        int srId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(srId);
        sr = (SemiReboque) criaOuRecuperaObjeto(srId);
    }

    //----------------------------------------------------------------------------------------------
    //                                          On Create View                                    ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioSemiReboqueBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          On View Created                                   ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
    }

    @Override
    public void inicializaCamposDaView() {
        placaEdit = binding.fragFormularioSeemireboquePlaca;
        marcaEdit = binding.fragFormularioSeemireboqueMarca;
        anoEdit = binding.fragFormularioSeemireboqueAno;
        modeloEdit = binding.fragFormularioSeemireboqueModelo;
        corEdit = binding.fragFormularioSeemireboqueCor;
        renavamEdit = binding.fragFormularioSeemireboqueRenavam;
        chassiEdit = binding.fragFormularioSeemireboqueChassi;
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        Integer reboqueId = (Integer)id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            sr = srDao.localizaPeloId(reboqueId);
        } else {
            sr = new SemiReboque();
        }
        return sr;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subEdit = binding.fragFormularioSeemireboqueSub;
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        placaEdit.setText(sr.getPlaca());
        marcaEdit.setText(sr.getMarcaModelo());
        anoEdit.setText(sr.getAno());
        modeloEdit.setText(sr.getModelo());
        corEdit.setText(sr.getCor());
        renavamEdit.setText(sr.getRenavam());
        chassiEdit.setText(sr.getChassi());
    }

    @Override
    public void aplicaMascarasAosEditTexts() {

    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampo(placaEdit);
        verificaCampo(marcaEdit);
        verificaCampo(anoEdit);
        verificaCampo(modeloEdit);
        verificaCampo(corEdit);
        verificaCampo(renavamEdit);
        verificaCampo(chassiEdit);
    }

    @Override
    public void vinculaDadosAoObjeto() {
        sr.setPlaca(placaEdit.getText().toString());
        sr.setMarcaModelo(marcaEdit.getText().toString());
        sr.setAno(anoEdit.getText().toString());
        sr.setModelo(modeloEdit.getText().toString());
        sr.setCor(corEdit.getText().toString());
        sr.setRenavam(renavamEdit.getText().toString());
        sr.setChassi(chassiEdit.getText().toString());
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        srDao.adiciona(sr);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        srDao.adiciona(sr);
    }

    @Override
    public int configuraObjetoNaCriacao() {
        sr.setRefCavaloId(getReferenciaDeCavalo(CHAVE_ID_CAVALO));
        return 0;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        srDao.deleta(sr);
    }
}