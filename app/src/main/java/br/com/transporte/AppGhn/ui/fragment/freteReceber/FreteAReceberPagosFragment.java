package br.com.transporte.AppGhn.ui.fragment.freteReceber;

import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFreteAReceberPagosBinding;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.adapter.FreteAReceberPagoAdapter;
import br.com.transporte.AppGhn.ui.viewmodel.FreteAReceberActViewModel;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;

public class FreteAReceberPagosFragment extends Fragment {
    private FragmentFreteAReceberPagosBinding binding;
    private FreteAReceberPagoAdapter adapter;
    private RecyclerView recyclerView;
    public FreteAReceberActViewModel viewModel;
    private boolean primeiraExecucaoDoFragment;
    private LinearLayout alertaLayout;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(FreteAReceberActViewModel.class);
        if (savedInstanceState != null) {
            primeiraExecucaoDoFragment = savedInstanceState.getBoolean("execucaoFragFreteJaRecebido");
        } else {
            primeiraExecucaoDoFragment = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("execucaoFragFreteJaRecebido", primeiraExecucaoDoFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFreteAReceberPagosBinding.inflate(getLayoutInflater());
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
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.recycler;
        alertaLayout = binding.alertaLayout.alerta;
    }

    private void configuraRecycler() {
        if (primeiraExecucaoDoFragment) {
            final Handler handler = new Handler();
            final Runnable r = () -> {
                configuraAdapter();
                exibeRecyclerComAnimacao();
                primeiraExecucaoDoFragment = false;
            };
            handler.postDelayed(r, 400);
        } else {
            configuraAdapter();
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getDataSet_freteRecebido().size(), alertaLayout, recyclerView, VIEW_INVISIBLE);
        }
    }

    private void exibeRecyclerComAnimacao() {
        final LayoutAnimationController animSlideIn =
                AnimationUtils.loadLayoutAnimation(this.requireContext(), R.anim.layout_controller_animation_slide_in_left);
        final Handler handler = new Handler();
        final Runnable r = () -> {
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getDataSet_freteRecebido().size(), alertaLayout, recyclerView, VIEW_INVISIBLE);
            recyclerView.setLayoutAnimation(animSlideIn);
        };
        handler.postDelayed(r, 100);
    }

    private void configuraAdapter() {
        adapter = new FreteAReceberPagoAdapter(
                this,
                viewModel.getDataSet_freteRecebido(),
                viewModel.getDataSet_cavalo(),
                viewModel.getDataSet_Recebimento()
        );
        recyclerView.setAdapter(adapter);

        NavController controlador = Navigation.findNavController(requireView());
        adapter.setOnItemClickListener(freteId -> {
            NavDirections direction = FreteAReceberPagosFragmentDirections.actionNavFreteReceberPagoToNavFreteAReceberResumo(freteId);
            controlador.navigate(direction);
        });
    }

    public void actSolicitaAtualizacao() {
        adapter.atualiza(viewModel.getDataSet_freteRecebido(), viewModel.getDataSet_Recebimento());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getDataSet_freteRecebido().size(), binding.alertaLayout.alerta, recyclerView, VIEW_INVISIBLE);
    }

    public void exibeResultadoDeBusca(List<Frete> dataSetSearch) {
        adapter.exibeSearch(dataSetSearch);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSetSearch.size(), binding.alertaLayout.alerta, recyclerView, VIEW_INVISIBLE);
    }

}