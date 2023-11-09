package br.com.transporte.appGhn.ui.fragment.certificados;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_RENOVADO;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.RESULT_LOGOUT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CERTIFICADOS;
import static br.com.transporte.appGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet.ATIVOS;
import static br.com.transporte.appGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet.TODOS;
import static br.com.transporte.appGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet.TODOS_E_ANO;
import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

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
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentCertificadosDiretosDetalhesBinding;
import br.com.transporte.appGhn.filtros.FiltraCavalo;
import br.com.transporte.appGhn.filtros.FiltraMotorista;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.CertificadoDiretoAdapter;
import br.com.transporte.appGhn.ui.fragment.certificados.dialog.CallbackCertificadoDialog;
import br.com.transporte.appGhn.ui.fragment.certificados.dialog.DialogCertificadoDiretoDetalhe;
import br.com.transporte.appGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet;
import br.com.transporte.appGhn.ui.viewmodel.CertificadoActViewModel;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.appGhn.util.MensagemUtil;

public class CertificadoDiretosDetalhesFragment extends Fragment {
    public static final String CERTIFICADO_JA_RENOVADO = "Certificado já renovado";
    public static final int ANO_DEFAULT = LocalDate.now().getYear();
    private FragmentCertificadosDiretosDetalhesBinding binding;
    private CertificadoActViewModel viewModel;
    private CertificadoDiretoAdapter adapter;
    private TextView campoMotorista;
    private RecyclerView recycler;
    private Cavalo cavalo;
    private List<DespesaCertificado> dataSet;
    private LinearLayout alertaLayout;
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
        cavalo = recebeIdArguments();
        defineParametrosDaRequisicaoDeData(ATIVOS, ANO_DEFAULT, cavalo.getId());
    }

    private void defineParametrosDaRequisicaoDeData(
            final TipoDeRequisicao_dataSet tipo,
            final int ano,
            final Long cavaloId
    ) {
        viewModel.defineParametrosDaRequisicaoCertificadoDireto(tipo, ano, cavaloId);
    }

    private Cavalo recebeIdArguments() {
        final Long cavaloId = CertificadoDiretosDetalhesFragmentArgs
                .fromBundle(getArguments())
                .getCavaloId();
        return (Cavalo) FiltraCavalo.localizaPeloId(
                viewModel.cavalosArmazenados,
                cavaloId
        );
    }

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
        alertaLayout = binding.buscaVazia;
        configuraRecycler();
        solicitaAtualizacaoAdapter();
        configuraUi();
        configuraMenuProvider();

    }

    private void configuraRecycler() {
        recycler = binding.fragCertificadoDetalhesRecycler;
        adapter = new CertificadoDiretoAdapter(this, new ArrayList<>());
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

    public void solicitaAtualizacaoAdapter() {
        dataSet = viewModel.getDataCertificadoDireto();
        adapter.atualiza(dataSet);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(
                dataSet.size(), alertaLayout,
                recycler, VIEW_INVISIBLE
        );
    }
    private void configuraUi() {
        final TextView campoMotorista = binding.fragCertificadoDetalhesNome;
        if (cavalo.getRefMotoristaId() != null) {
            String nome = FiltraMotorista.localizaPeloId(
                    viewModel.motoristasArmazenados,
                    cavalo.getRefMotoristaId()
            ).toString();
            campoMotorista.setText(nome);
        } else {
            campoMotorista.setText("...");
        }
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
                        requireActivity().setResult(RESULT_LOGOUT);
                        requireActivity().finish();
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

    /** @noinspection UnusedAssignment*/
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
            intent.putExtra(CHAVE_DESPESA, DIRETA);
            activityResultLauncher.launch(intent);
        }

        return super.onContextItemSelected(item);
    }

    //---------------------------------------------------------------------------------------

    private void showBottomDialog() {
        final DialogCertificadoDiretoDetalhe dialog = new DialogCertificadoDiretoDetalhe(requireContext());
        dialog.show();
        dialog.setCallbackDialog(new CallbackCertificadoDialog() {
            @Override
            public void buscaPor_todosEAno(int ano) {
                viewModel.defineParametrosDaRequisicaoCertificadoDireto(
                        TODOS_E_ANO, ano, null
                );
                solicitaAtualizacaoAdapter();
            }

            @Override
            public void buscaPor_todos() {
                viewModel.defineParametrosDaRequisicaoCertificadoDireto(
                        TODOS, ANO_DEFAULT, null
                );
                solicitaAtualizacaoAdapter();
            }

            @Override
            public void buscaPor_ativos() {
                viewModel.defineParametrosDaRequisicaoCertificadoDireto(
                        ATIVOS, ANO_DEFAULT, null
                );
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


