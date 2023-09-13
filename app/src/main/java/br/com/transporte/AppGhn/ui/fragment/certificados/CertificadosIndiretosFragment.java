package br.com.transporte.AppGhn.ui.fragment.certificados;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_RENOVADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CERTIFICADOS;
import static br.com.transporte.AppGhn.ui.fragment.certificados.CertificadoDiretosDetalhesFragment.CERTIFICADO_JA_RENOVADO;
import static br.com.transporte.AppGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet.ATIVOS;
import static br.com.transporte.AppGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet.TODOS;
import static br.com.transporte.AppGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet.TODOS_E_ANO;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.databinding.FragmentCertificadosIndiretosBinding;
import br.com.transporte.AppGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.CertificadoIndiretoAdapter;
import br.com.transporte.AppGhn.ui.fragment.certificados.dialog.CallbackCertificadoDialog;
import br.com.transporte.AppGhn.ui.fragment.certificados.dialog.DialogCertificadoIndireto;
import br.com.transporte.AppGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class CertificadosIndiretosFragment extends Fragment {
    public static final String CERTIFICADOS = "Certificados";
    private FragmentCertificadosIndiretosBinding binding;
    private List<DespesaCertificado> dataSet;
    private CertificadoIndiretoAdapter adapter;
    private RoomDespesaCertificadoDao certificadoDao;
    private LinearLayout buscaVazia;
    private TipoDeRequisicao_dataSet tipoDeRequisicao_dataSet;
    private int anoDeRequisicao_dataSet;
    private RecyclerView recycler;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        atualizaAdapter(REGISTRO_EDITADO);
                        break;

                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;

                    case RESULT_DELETE:
                        atualizaAdapter(REGISTRO_APAGADO);
                        break;

                    case RESULT_UPDATE:
                        atualizaAdapter(REGISTRO_RENOVADO);
                        break;
                }
            });

    //------------------------------------- Metodos Publicos ---------------------------------------

    public void atualizaAdapter(String msg) {
        atualizaDataSet();
        atualizaUiRecycler();
        MensagemUtil.toast(requireContext(), msg);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GhnDataBase database = GhnDataBase.getInstance(requireContext());
        certificadoDao = database.getRoomDespesaCertificadoDao();
        tipoDeRequisicao_dataSet = ATIVOS;
        anoDeRequisicao_dataSet = 0;
        atualizaDataSet();
    }

    private void atualizaDataSet() {
        if (dataSet == null) dataSet = new ArrayList<>();
        dataSet = certificadoDao.listaPorTipo(INDIRETA);
        switch (tipoDeRequisicao_dataSet) {
            case TODOS_E_ANO:
                dataSet = FiltraDespesasCertificado.listaPorAno(dataSet, anoDeRequisicao_dataSet);
                break;
            case TODOS:
                //Lista já vem com todos por padrão.
                break;
            case ATIVOS:
                dataSet = FiltraDespesasCertificado.listaPorStatus(dataSet, true);
                break;
        }
    }

    @NonNull
    @Contract(" -> new")
    private List<DespesaCertificado> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCertificadosIndiretosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraToolbar();
        configuraUi();
    }

    private void configuraUi() {
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(getDataSet().size(), buscaVazia, recycler, VIEW_INVISIBLE);
    }

    private void inicializaCamposDaView() {
        buscaVazia = binding.buscaVazia;
        recycler = binding.recycler;
    }

    private void configuraRecycler() {
        adapter = new CertificadoIndiretoAdapter(this, getDataSet());
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(certificadoId -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CERTIFICADOS);
            intent.putExtra(CHAVE_ID, certificadoId);
            intent.putExtra(CHAVE_REQUISICAO, EDITANDO);
            intent.putExtra(CHAVE_DESPESA, INDIRETA);
            activityResultLauncher.launch(intent);
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        DespesaCertificado certificado = dataSet.get(posicao);

        if (item.getItemId() == R.id.renovarCertificado && !certificado.isValido()) {
            Toast.makeText(this.requireContext(), CERTIFICADO_JA_RENOVADO, Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.renovarCertificado) {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CERTIFICADOS);
            intent.putExtra(CHAVE_ID, certificado.getId());
            intent.putExtra(CHAVE_REQUISICAO, RENOVANDO);
            intent.putExtra(CHAVE_DESPESA, INDIRETA);
            activityResultLauncher.launch(intent);
        }

        return super.onContextItemSelected(item);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(CERTIFICADOS);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_certificados, menu);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.menu_certificados_visibilidade:
                        showBottomDialog();
                        break;

                    case R.id.menu_certificados_logout:
                        Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void showBottomDialog() {
        DialogCertificadoIndireto dialog = new DialogCertificadoIndireto(requireContext());
        dialog.show();
        dialog.setCallbackDialog(new CallbackCertificadoDialog() {
            @Override
            public void buscaPor_todosEAno(int ano) {
                tipoDeRequisicao_dataSet = TODOS_E_ANO;
                anoDeRequisicao_dataSet = ano;
                atualizaDataSet();
                atualizaUiRecycler();
            }

            @Override
            public void buscaPor_todos() {
                tipoDeRequisicao_dataSet = TODOS;
                anoDeRequisicao_dataSet = 0;
                atualizaDataSet();
                atualizaUiRecycler();
            }

            @Override
            public void buscaPor_ativos() {
                tipoDeRequisicao_dataSet = ATIVOS;
                anoDeRequisicao_dataSet = 0;
                atualizaDataSet();
                atualizaUiRecycler();
            }
        });

    }

    private void atualizaUiRecycler() {
        adapter.atualiza(getDataSet());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(getDataSet().size(), buscaVazia, recycler, VIEW_INVISIBLE);
    }

}