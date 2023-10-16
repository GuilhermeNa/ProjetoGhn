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
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.TAG;
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
import android.util.Log;
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
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentCertificadosIndiretosBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.CertificadoIndiretoAdapter;
import br.com.transporte.AppGhn.ui.fragment.certificados.dialog.CallbackCertificadoDialog;
import br.com.transporte.AppGhn.ui.fragment.certificados.dialog.DialogCertificadoIndireto;
import br.com.transporte.AppGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet;
import br.com.transporte.AppGhn.ui.viewmodel.CertificadoActViewModel;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class CertificadosIndiretosFragment extends Fragment {
    public static int ANO_DEFAULT = LocalDate.now().getYear();
    private FragmentCertificadosIndiretosBinding binding;
    private CertificadoActViewModel viewModel;
    private CertificadoIndiretoAdapter adapter;
    private List<DespesaCertificado> dataSet;
    private RecyclerView recycler;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                switch (codigoResultado) {
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;
                    case RESULT_DELETE:
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                    case RESULT_UPDATE:
                        MensagemUtil.toast(requireContext(), REGISTRO_RENOVADO);
                        break;
                }
            });

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CertificadoActViewModel.class);
        defineParametrosDaRequisicaoDeData(ATIVOS, ANO_DEFAULT);
        Log.d(TAG, "onCreate: ");
    }

    private void defineParametrosDaRequisicaoDeData(
            final TipoDeRequisicao_dataSet tipo,
            final int ano
    ) {
        viewModel.defineParametrosDaRequisicaoCertificadoIndireto(tipo, ano);
    }

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
        configuraRecycler();
        solicitaAtualizacaoAdapter();
        configuraMenuProvider();
    }

    private void configuraRecycler() {
        recycler = binding.recycler;
        adapter = new CertificadoIndiretoAdapter(this, new ArrayList<>());
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

    public void solicitaAtualizacaoAdapter() {
        final LinearLayout alertaLayout = binding.buscaVazia;
        dataSet = viewModel.getDataCertificadoIndireto();
        adapter.atualiza(dataSet);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(
                dataSet.size(), alertaLayout,
                recycler, VIEW_INVISIBLE
        );
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
                        requireActivity().finish();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void showBottomDialog() {
        final DialogCertificadoIndireto dialog = new DialogCertificadoIndireto(requireContext());
        dialog.show();
        dialog.setCallbackDialog(new CallbackCertificadoDialog() {
            @Override
            public void buscaPor_todosEAno(int ano) {
                defineParametrosDaRequisicaoDeData(TODOS_E_ANO, ano);
                solicitaAtualizacaoAdapter();
            }

            @Override
            public void buscaPor_todos() {
                defineParametrosDaRequisicaoDeData(TODOS, ANO_DEFAULT);
                solicitaAtualizacaoAdapter();
            }

            @Override
            public void buscaPor_ativos() {
                defineParametrosDaRequisicaoDeData(ATIVOS, ANO_DEFAULT);
                solicitaAtualizacaoAdapter();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
