package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioCavaloBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.tasks.cavalo.AdicionaCavaloTask;
import br.com.transporte.AppGhn.tasks.cavalo.AtualizaCavaloTask;
import br.com.transporte.AppGhn.tasks.cavalo.LocalizaCavaloTask;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioCavaloFragment extends FormularioBaseFragment {
    private FragmentFormularioCavaloBinding binding;
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de cavalo que já existe";
    private EditText placaEdit, versaoEdit, marcaEdit, anoEdit, modeloEdit, corEdit, renavamEdit, chassiEdit;
    private Cavalo cavalo;
    private RoomCavaloDao cavaloDao;
    private EditText comissaoEdit;
    private AtualizaCavaloTask atualizaCavaloTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GhnApplication application = new GhnApplication();
        executorService = application.getExecutorService();
        handler = application.getMainThreadHandler();
        cavaloDao = GhnDataBase.getInstance(requireContext()).getRoomCavaloDao();

        long cavaloId = verificaSeRecebeDadosExternos(CHAVE_ID_CAVALO);
        defineTipoEditandoOuCriando(cavaloId);
        cavalo = (Cavalo) criaOuRecuperaObjeto(cavaloId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioCavaloBinding.inflate(getLayoutInflater());
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
        comissaoEdit = binding.fragFormularioCavaloComissao;
        placaEdit = binding.fragFormularioCavaloPlaca;
        marcaEdit = binding.fragFormularioCavaloMarca;
        versaoEdit = binding.fragFormularioCavaloVersao;
        anoEdit = binding.fragFormularioCavaloAno;
        modeloEdit = binding.fragFormularioCavaloModelo;
        corEdit = binding.fragFormularioCavaloCor;
        renavamEdit = binding.fragFormularioCavaloRenavam;
        chassiEdit = binding.fragFormularioCavaloChassi;
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        Long cavaloId = (Long) id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            LocalizaCavaloTask localizaCavaloTask = new LocalizaCavaloTask(executorService, handler);
            localizaCavaloTask.solicitaBusca(cavaloDao, cavaloId, cavaloRecebido -> {
                        cavalo = cavaloRecebido;
                        bind();
                    }
            );

        } else {
            cavalo = new Cavalo();
        }
        return cavalo;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subEdit = binding.fragFormularioCavaloSub;
        subEdit.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        placaEdit.setText(cavalo.getPlaca());
        comissaoEdit.setText(cavalo.getComissaoBase().toPlainString());
        marcaEdit.setText(cavalo.getMarcaModelo());
        versaoEdit.setText(cavalo.getVersao());
        anoEdit.setText(cavalo.getAno());
        modeloEdit.setText(cavalo.getModelo());
        corEdit.setText(cavalo.getCor());
        renavamEdit.setText(cavalo.getRenavam());
        chassiEdit.setText(cavalo.getChassi());
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        comissaoEdit.addTextChangedListener(new MascaraMonetariaUtil(comissaoEdit));
    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampo(placaEdit);
        verificaCampo(marcaEdit);
        verificaCampo(versaoEdit);
        verificaCampo(anoEdit);
        verificaCampo(modeloEdit);
        verificaCampo(corEdit);
        verificaCampo(renavamEdit);
        verificaCampo(chassiEdit);
        verificaCampo(comissaoEdit);
    }

    @Override
    public void vinculaDadosAoObjeto() {
        cavalo.setComissaoBase(new BigDecimal(MascaraMonetariaUtil.formatPriceSave(comissaoEdit.getText().toString())));
        cavalo.setPlaca(placaEdit.getText().toString());
        cavalo.setMarcaModelo(marcaEdit.getText().toString());
        cavalo.setVersao(versaoEdit.getText().toString());
        cavalo.setAno(anoEdit.getText().toString());
        cavalo.setModelo(modeloEdit.getText().toString());
        cavalo.setCor(corEdit.getText().toString());
        cavalo.setRenavam(renavamEdit.getText().toString());
        cavalo.setChassi(chassiEdit.getText().toString());
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        atualizaCavaloTask = new AtualizaCavaloTask(executorService, handler);
        atualizaCavaloTask.solicitaAtualizacao(cavaloDao, cavalo, () -> {
            requireActivity().setResult(RESULT_EDIT);
            requireActivity().finish();
        });
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        AdicionaCavaloTask adicionaCavaloTask = new AdicionaCavaloTask(executorService, handler);
        adicionaCavaloTask.solicitaAdicao(cavaloDao, cavalo, id -> {
            requireActivity().setResult(RESULT_OK);
            requireActivity().finish();
        });
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        cavalo.setValido(true);
        return null;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        cavalo.setValido(false);
        atualizaCavaloTask.solicitaAtualizacao(cavaloDao, cavalo, () -> {
            requireActivity().setResult(RESULT_DELETE);
            requireActivity().finish();
        });
    }

}