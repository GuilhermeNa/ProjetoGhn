package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioSemiReboqueBinding;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.repository.ReboqueRepository;
import br.com.transporte.AppGhn.tasks.reboque.AdicionaReboqueTask;
import br.com.transporte.AppGhn.tasks.reboque.AtualizaReboqueTask;
import br.com.transporte.AppGhn.tasks.reboque.DeletaReboqueTask;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioReboqueViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.FormularioReboqueViewModelFactory;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FormularioSemiReboqueFragment extends FormularioBaseFragment {
    private FragmentFormularioSemiReboqueBinding binding;
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de semi-reboque que já existe.";
    private EditText placaEdit, marcaEdit, anoEdit, modeloEdit, corEdit, renavamEdit, chassiEdit;
    private SemiReboque sr;
    private FormularioReboqueViewModel viewModel;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        long srId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(srId);
        sr = (SemiReboque) criaOuRecuperaObjeto(srId);
    }

    private void inicializaViewModel() {
        final ReboqueRepository repository = new ReboqueRepository(requireContext());
        final FormularioReboqueViewModelFactory factory = new FormularioReboqueViewModelFactory(repository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioReboqueViewModel.class);
    }

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
        Long reboqueId = (Long) id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            viewModel.localizaReboque(reboqueId).observe(this,
                    semiReboque -> {
                        if (semiReboque != null) {
                            viewModel.reboqueArmazenado = semiReboque;
                            sr = semiReboque;
                            bind();
                        }
                    });
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
        viewModel.salva(sr).observe(this,
                ignore -> {
                    requireActivity().setResult(RESULT_EDIT);
                    requireActivity().finish();
                });
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        viewModel.salva(sr).observe(this,
                id -> {
                    if (id != null) {
                        requireActivity().setResult(RESULT_OK);
                        requireActivity().finish();
                    }
                });
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        sr.setRefCavaloId(getReferenciaDeCavalo(CHAVE_ID_CAVALO));
        return null;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        viewModel.deleta().observe(this,
                erro -> {
                    if (erro == null) {
                        requireActivity().setResult(RESULT_DELETE);
                        requireActivity().finish();
                    } else {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                });
    }
}