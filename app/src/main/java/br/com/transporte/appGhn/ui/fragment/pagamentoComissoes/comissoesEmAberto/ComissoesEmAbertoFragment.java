package br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesEmAberto;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_ADIANTAMENTO;
import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentComissoesBinding;
import br.com.transporte.appGhn.filtros.FiltraCavalo;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.ComissoesAdapter;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesEmAberto.extensions.CalculaValoresParaUi;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesEmAberto.extensions.Resource;
import br.com.transporte.appGhn.ui.viewmodel.ComissaoActViewModel;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class ComissoesEmAbertoFragment extends Fragment {
    public static final String COMISSAO_EM_ABERTO = "Comissao em Aberto";
    public static final String DEFINA_UM_MOTORISTA_PARA_ESTE_CAVALO = "Defina um motorista para este cavalo";
    private FragmentComissoesBinding binding;
    private ComissoesAdapter adapter;
    private ProgressBar progressBarView;
    private TextView campoValorPago, campoValorTotal, campoValorEmAberto;
    private RecyclerView recycler;
    private BigDecimal evolucaoProgressBar;
    private ComissaoActViewModel viewModel;
    private LinearLayout alertaLayout;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                switch (codigoResultado) {
                    case RESULT_OK:
                        MensagemUtil.toast(requireContext(), REGISTRO_CRIADO);
                        break;
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;
                    case RESULT_DELETE:
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                }
            });

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ComissaoActViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentComissoesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaViewCompartilhadosPorMetodos();
        configuraRecycler();
       // configuraUi();
    }

    private void inicializaCamposDaViewCompartilhadosPorMetodos() {
        recycler = binding.recycler;
        alertaLayout = binding.alertaLayout.alerta;
        progressBarView = binding.progressBar;
        campoValorPago = binding.pagoValor;
        campoValorTotal = binding.totalComissaoValor;
        campoValorEmAberto = binding.emAbertoValor;
    }

    private void configuraRecycler() {
        adapter = new ComissoesAdapter(
                this,
                viewModel.getDataSet_cavalo(),
                viewModel.getDataSet_motorista(),
                viewModel.getListaFreteComFiltro(),
                viewModel.getDataSetAdiantamento(),
                viewModel.getDataSetReembolso()
        );
        recycler.setAdapter(adapter);
        NavController controlador = Navigation.findNavController(requireView());
        adapter.setOnItemClickListener(cavaloId -> {
            Cavalo cavalo = (Cavalo) FiltraCavalo.localizaPeloId(viewModel.getDataSet_cavalo(), cavaloId);
            if (Objects.requireNonNull(cavalo).getRefMotoristaId() != null) {
                NavDirections direction = ComissoesEmAbertoFragmentDirections.actionNavComissoesToNavComissoesDetalhes(cavaloId);
                controlador.navigate(direction);
            } else {
                MensagemUtil.snackBar(getView(), DEFINA_UM_MOTORISTA_PARA_ESTE_CAVALO);
            }
        });
    }

    private void configuraUi() {
        final CalculaValoresParaUi calculoHelper = new CalculaValoresParaUi(viewModel.getListaFreteComFiltro());
        Resource resource = calculoHelper.getResource();

        String valorTotal = FormataNumerosUtil.formataMoedaPadraoBr(resource.getComissaoTotalDevidaAosMotoristas());
        campoValorTotal.setText(valorTotal);

        String valorPago = FormataNumerosUtil.formataMoedaPadraoBr(resource.getComissaoTotalQueJaFoiPagaAosMotoristas());
        campoValorPago.setText(valorPago);

        String valorEmAberto = FormataNumerosUtil.formataMoedaPadraoBr(resource.getComissaoEmAbertoASerPaga());
        campoValorEmAberto.setText(valorEmAberto);

        progressBarView.setProgress(resource.getEvolucaoProgressBar().intValue());
    }

    /** @noinspection UnusedAssignment*/
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        Cavalo cavalo = viewModel.getDataSet_cavalo().get(posicao);

        if (item.getItemId() == R.id.concedeAdiantamento) {
            if (cavalo.getRefMotoristaId() != null) {
                Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_ADIANTAMENTO);
                intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
                activityResultLauncher.launch(intent);
            } else {
                MensagemUtil.snackBar(requireView(), DEFINA_UM_MOTORISTA_PARA_ESTE_CAVALO);
            }
        }
        return super.onContextItemSelected(item);
    }

    public void actNotificaAtualizacao_frete() {
        adapter.atualiza(
                viewModel.getListaFreteComFiltro(),
                viewModel.getDataSetAdiantamento(),
                viewModel.getDataSetReembolso()
        );
    }

    public void actNotificaAtualizacao_adiantamento() {
        adapter.atualiza(null, viewModel.getDataSetAdiantamento(), null);
    }

    public void actNotificaAtualizacao_salario() {
        //todo
    }

    @Override
    public void onResume() {
        super.onResume();
        configuraUi();
    }

    public void actNotificaBuscaNaSearch(List<Cavalo> listaSearch) {
        adapter.atualizaDataSetCavalo(listaSearch);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaSearch.size(), alertaLayout, recycler, VIEW_INVISIBLE);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaSearch.size(), null, binding.constraint, VIEW_INVISIBLE);
    }

    public void actNotificaNovaBuscaPorData() {
        adapter.atualiza(viewModel.getListaFreteComFiltro(), null, null);
        configuraUi();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

