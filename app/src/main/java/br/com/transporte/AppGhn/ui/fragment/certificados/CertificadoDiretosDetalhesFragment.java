package br.com.transporte.AppGhn.ui.fragment.certificados;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.databinding.FragmentCertificadosDiretosDetalhesBinding;
import br.com.transporte.AppGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.CertificadoDiretoAdapter;
import br.com.transporte.AppGhn.ui.fragment.certificados.dialog.CallbackCertificadoDialog;
import br.com.transporte.AppGhn.ui.fragment.certificados.dialog.DialogCertificadoDiretoDetalhe;
import br.com.transporte.AppGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class CertificadoDiretosDetalhesFragment extends Fragment {
    public static final String CERTIFICADO_JA_RENOVADO = "Certificado já renovado";
    private FragmentCertificadosDiretosDetalhesBinding binding;
    private List<DespesaCertificado> dataSet;
    private CertificadoDiretoAdapter adapter;
    private RoomDespesaCertificadoDao certificadoDao;
    private TextView motoristaTxtView;
    private int anoDeRequisicao_dataSet;
    private Cavalo cavalo;
    private LinearLayout buscaVazia;
    private GhnDataBase dataBase;
    private RecyclerView recycler;
    private TipoDeRequisicao_dataSet tipoDeRequisicao_dataSet;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        atualizaAdapterAposResultLauncher(REGISTRO_EDITADO);
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        atualizaAdapterAposResultLauncher(REGISTRO_APAGADO);
                        break;

                    case RESULT_UPDATE:
                        atualizaAdapterAposResultLauncher(REGISTRO_RENOVADO);
                }
            });

    private void atualizaAdapterAposResultLauncher(String msg) {
        atualizaDataSet();
        solicitaAtualizacaoAdapter();
        MensagemUtil.toast(requireContext(), msg);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        tipoDeRequisicao_dataSet = ATIVOS;
        cavalo = recebeIdArguments();
        atualizaDataSet();
    }

    private void inicializaDataBase() {
        dataBase = GhnDataBase.getInstance(requireContext());
        certificadoDao = dataBase.getRoomDespesaCertificadoDao();
    }

    private void atualizaDataSet() {
        // Atualiza o dataSet com base no tipo de solicitacao de busca que está em vigor,
        if (dataSet == null) dataSet = new ArrayList<>();
        dataSet = FiltraDespesasCertificado.listaPorTipoDespesa(certificadoDao.listaPorCavaloId(cavalo.getId()), DIRETA);

        switch (tipoDeRequisicao_dataSet){
            case ATIVOS:
                dataSet = FiltraDespesasCertificado.listaPorStatus(dataSet, true);
            break;

            case TODOS:
                dataSet = FiltraDespesasCertificado.listaPorTipoDespesa(certificadoDao.listaPorCavaloId(cavalo.getId()), DIRETA);
                break;

            case TODOS_E_ANO:
                dataSet = FiltraDespesasCertificado.listaPorAno(dataSet, anoDeRequisicao_dataSet);
                break;
        }
    }

    @NonNull
    @Contract(" -> new")
    private List<DespesaCertificado> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    private Cavalo recebeIdArguments() {
        RoomCavaloDao cavaloDao = dataBase.getRoomCavaloDao();
        Long cavaloId = CertificadoDiretosDetalhesFragmentArgs.fromBundle(getArguments()).getCavaloId();
 //       cavalo = cavaloDao.localizaPeloId(cavaloId);
        return cavalo;
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCertificadosDiretosDetalhesBinding.inflate(getLayoutInflater());
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
        configuraUi();
        configuraToolbar();
        configuraMenuProvider();
    }

    private void inicializaCamposDaView() {
        motoristaTxtView = binding.fragCertificadoDetalhesNome;
        buscaVazia = binding.buscaVazia;
        recycler = binding.fragCertificadoDetalhesRecycler;
    }

    private void configuraRecycler() {
        adapter = new CertificadoDiretoAdapter(this, getDataSet());
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(certificado -> {
            Intent intent = new Intent(this.requireActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CERTIFICADOS);
            intent.putExtra(CHAVE_ID, ((DespesaCertificado) certificado).getId());
            intent.putExtra(CHAVE_REQUISICAO, EDITANDO);
            intent.putExtra(CHAVE_DESPESA, DIRETA);
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraUi() {
        if (cavalo.getRefMotoristaId() != null) {
            RoomMotoristaDao motoristaDao = dataBase.getRoomMotoristaDao();
      //      String nome = motoristaDao.localizaPeloId(cavalo.getRefMotoristaId()).getNome();
       //     motoristaTxtView.setText(nome);
        } else {
            motoristaTxtView.setText(" ");
        }

        ExibirResultadoDaBusca_sucessoOuAlerta.configura(getDataSet().size(), buscaVazia, recycler, VIEW_INVISIBLE);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(cavalo.getPlaca());
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
    }

    private void configuraMenuProvider() {
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
                        NavController controlador = Navigation.findNavController(requireView());
                        controlador.popBackStack();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        DespesaCertificado certificado = getDataSet().get(posicao);

        if (item.getItemId() == R.id.renovarCertificado && !certificado.isValido()) {
            Toast.makeText(this.requireContext(), CERTIFICADO_JA_RENOVADO, Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.renovarCertificado) {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CERTIFICADOS);
            intent.putExtra(CHAVE_ID, certificado.getId());
            intent.putExtra(CHAVE_REQUISICAO, RENOVANDO);
            intent.putExtra(CHAVE_DESPESA, DIRETA);
            activityResultLauncher.launch(intent);
        }

        return super.onContextItemSelected(item);
    }

    //---------------------------------------------------------------------------------------

    private void showBottomDialog() {
        DialogCertificadoDiretoDetalhe dialog = new DialogCertificadoDiretoDetalhe(requireContext());
        dialog.show();
        dialog.setCallbackDialog(new CallbackCertificadoDialog() {
            @Override
            public void buscaPor_todosEAno(int ano) {
                tipoDeRequisicao_dataSet = TODOS_E_ANO;
                anoDeRequisicao_dataSet = ano;
                atualizaDataSet();
                solicitaAtualizacaoAdapter();
            }

            @Override
            public void buscaPor_todos() {
                tipoDeRequisicao_dataSet = TODOS;
                anoDeRequisicao_dataSet = 0;
                atualizaDataSet();
                solicitaAtualizacaoAdapter();
            }

            @Override
            public void buscaPor_ativos() {
                tipoDeRequisicao_dataSet = ATIVOS;
                anoDeRequisicao_dataSet = 0;
                atualizaDataSet();
                solicitaAtualizacaoAdapter();
            }
        });
    }

    private void solicitaAtualizacaoAdapter() {
        adapter.atualiza(getDataSet());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(getDataSet().size(), buscaVazia, recycler, VIEW_INVISIBLE);
    }

}

