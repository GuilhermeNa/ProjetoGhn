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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioAdiantamentoBinding;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.repository.AdiantamentoRepository;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioAdiantamentoViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioBaseViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.FormularioAdiantamentoViewModelFactory;
import br.com.transporte.AppGhn.ui.viewmodel.factory.FormularioBaseViewModelFactory;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioAdiantamentoFragment extends FormularioBaseFragment {
    private FragmentFormularioAdiantamentoBinding binding;
    public static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de adiantamento que já existe.";
    private Adiantamento adiantamento;
    private EditText dataEdit, valorEdit, descricaoEdit;
    private TextInputLayout dataLayout;
    private TextView placa, campoMotorista;
    private FormularioAdiantamentoViewModel viewModel;
    private Motorista motorista;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        recebeReferenciaExternaDeCavalo(CHAVE_ID_CAVALO);
        long adiantamentoId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(adiantamentoId);
        adiantamento = (Adiantamento) criaOuRecuperaObjeto(adiantamentoId);

    }

    private void inicializaViewModel() {
        final AdiantamentoRepository adiantamentoRepository = new AdiantamentoRepository(requireContext());
        final MotoristaRepository motoristaRepository = new MotoristaRepository(requireContext());
        final FormularioAdiantamentoViewModelFactory factory = new FormularioAdiantamentoViewModelFactory(motoristaRepository, adiantamentoRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FormularioAdiantamentoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioAdiantamentoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
    }

    private void configuracoesAdicionaisUi(final Motorista motorista) {
        placa.setText(cavaloRecebido.getPlaca());
        if (motorista != null) {
            campoMotorista.setText(motorista.getNome());
        } else {
            campoMotorista.setText("...");
        }
    }

    @Override
    public void inicializaCamposDaView() {
        dataLayout = binding.dataLayout;
        dataEdit = binding.data;
        valorEdit = binding.valor;
        descricaoEdit = binding.descricao;
        placa = binding.placa;
        campoMotorista = binding.motorista;
    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        Long adiantamentoId = (Long) id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            viewModel.localizaAdiantamento(adiantamentoId).observe(this,
                    adiantamentoRecebido -> {
                        if (adiantamentoRecebido != null) {
                            viewModel.adiantamentoArmazenado = adiantamentoRecebido;
                            adiantamento = adiantamentoRecebido;
                            bind();
                        }
                    });
        } else {
            adiantamento = new Adiantamento();
        }

        return adiantamento;
    }

    @Override
    protected void recebeReferenciaExternaDeCavalo(String chave) {
        CavaloRepository repository = new CavaloRepository(requireContext());
        FormularioBaseViewModelFactory factory = new FormularioBaseViewModelFactory(repository);
        ViewModelProvider provedor = new ViewModelProvider(this, factory);
        FormularioBaseViewModel viewModel = provedor.get(FormularioBaseViewModel.class);

        final Bundle bundle = getArguments();
        final long cavaloId = bundle.getLong(chave);

        viewModel.localizaCavalo(cavaloId).observe(this,
                cavalo -> {
                    if (cavalo != null) {
                        cavaloRecebido = cavalo;
                        buscaMotorista(cavaloRecebido.getRefMotoristaId());
                    }
                });

    }

    private void buscaMotorista(@NonNull Long motoristaId) {
        viewModel.localizaMotorista(motoristaId).observe(this,
                motoristaRecebido -> {
                    motorista = motoristaRecebido;
                    configuracoesAdicionaisUi(motoristaRecebido);
                });
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxt = binding.subTitulo;
        subTxt.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {
        configuracoesAdicionaisUi(motorista);
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
        viewModel.salva(adiantamento).observe(this,
                ignore -> {
                    Intent intent = new Intent();
                    intent.putExtra(CHAVE_ADIANTAMENTO, adiantamento);
                    requireActivity().setResult(RESULT_EDIT, intent);
                    requireActivity().finish();
                });
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        configuraObjetoNaCriacao();
        viewModel.salva(adiantamento).observe(this,
                id -> {
                    requireActivity().setResult(RESULT_OK);
                    requireActivity().finish();
                });
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        adiantamento.setAdiantamentoJaFoiPago(false);
        adiantamento.setRefCavaloId(cavaloRecebido.getId());
        adiantamento.setRefMotoristaId(cavaloRecebido.getRefMotoristaId());
        adiantamento.setSaldoRestituido(new BigDecimal(BigInteger.ZERO));
        return null;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        viewModel.deleta().observe(this,
                ignore -> {
                    Intent intent = new Intent();
                    intent.putExtra(CHAVE_ID, adiantamento.getId());
                    requireActivity().setResult(RESULT_DELETE, intent);
                    requireActivity().finish();
                });
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
                            deletaObjetoNoBancoDeDados();
                        }).
                        setNegativeButton(NAO, null).show();
                break;
        }
        return false;
    }
}