package br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesPagas;

import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.databinding.FragmentComissoesPagasBinding;
import br.com.transporte.appGhn.model.custos.CustosDeSalario;
import br.com.transporte.appGhn.ui.adapter.SalariosAdapter;
import br.com.transporte.appGhn.ui.viewmodel.ComissaoActViewModel;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;

public class ComissoesPagasFragment extends Fragment {
    private FragmentComissoesPagasBinding binding;
    private SalariosAdapter adapter;
    private RecyclerView recyclerView;
    private ComissaoActViewModel viewModel;

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
        binding = FragmentComissoesPagasBinding.inflate(getLayoutInflater());
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
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getListaSalarioComFiltro().size(), binding.alertaLayout.alerta, recyclerView, VIEW_INVISIBLE);
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.recycler;
    }

    private void configuraRecycler() {
        adapter = new SalariosAdapter(
                this,
                viewModel.getDataSetBaseFrete(),
                viewModel.getListaSalarioComFiltro(),
                viewModel.getDataSet_cavalo(),
                viewModel.getDataSet_motorista(),
                viewModel.getDataSetAdiantamento(),
                viewModel.getDataSetReembolso()
        );
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(
                salarioId -> {
                    NavController controlador = Navigation.findNavController(this.requireView());
                    NavDirections direction = ComissoesPagasFragmentDirections.actionNavComissoesPagasToNavComissoesPagasDetalhes(salarioId);
                    controlador.navigate(direction);
                });
    }

    public void actNotificaAtualizacao_frete() {
        //todo
    }

    public void actSolicitaAtt_adiantamento() {
        //todo
    }

    public void actSolicitaAtt_custoPercurso() {
        //todo
    }

    public void actNotificaBuscaNaSearch(List<CustosDeSalario> listaSearch) {
        adapter.exibeResultadoSearch(listaSearch);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaSearch.size(), binding.alertaLayout.alerta, recyclerView, VIEW_INVISIBLE);
    }

    public void actNotificaNovaBuscaPorData() {
        adapter.atualiza(viewModel.getListaSalarioComFiltro(), viewModel.getDataSetBaseFrete());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getListaSalarioComFiltro().size(), binding.alertaLayout.alerta, recyclerView, VIEW_INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.atualiza(viewModel.getListaSalarioComFiltro(), viewModel.getDataSetBaseFrete());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}