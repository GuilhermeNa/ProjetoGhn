package br.com.transporte.AppGhn.ui.fragment.freteReceber.freteEmAberto;

import static br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment.CANCELAR;
import static br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment.CONFIRMAR;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFreteAReceberBinding;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.filtros.FiltraRecebimentoFrete;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.freteEmAberto.adapter.FreteAReceberAdapter;
import br.com.transporte.AppGhn.ui.viewmodel.FreteAReceberActViewModel;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FreteAReceberEmAbertoFragment extends Fragment {
    public static final String VOCE_CONFIRMA_O_FECHAMENTO = "Você confirma o fechamento?";
    public static final String FRETE_FECHADO = "Frete fechado";
    public static final String VALOR_RECEBIDO_NAO_CONFERE = "Valor recebido nao confere";
    private FragmentFreteAReceberBinding binding;
    private RecyclerView recyclerView;
    private NavDirections direction;
    private boolean primeiraExecucaoDoFragment;
    private FreteAReceberAdapter adapter;
    public FreteAReceberActViewModel viewModel;
    private LinearLayout alertaLayout;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(FreteAReceberActViewModel.class);
        if (savedInstanceState != null) {
            primeiraExecucaoDoFragment = savedInstanceState.getBoolean("execucaoFragFreteEmAberto");
        } else {
            primeiraExecucaoDoFragment = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("execucaoFragFreteEmAberto", primeiraExecucaoDoFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFreteAReceberBinding.inflate(getLayoutInflater());
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
        recyclerView = binding.recItemFreteReceberRecycler;
        alertaLayout = binding.alertaLayout.alerta;
    }

    private void configuraRecycler() {
        if(primeiraExecucaoDoFragment){
            final Handler handler = new Handler();
            final Runnable r = ()->{
                configuraAdapter();
                exibeRecyclerComAnimacao();
                primeiraExecucaoDoFragment = false;
            };
            handler.postDelayed(r, 400);
        } else {
            configuraAdapter();
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getDataSet_freteEmAberto().size(), alertaLayout, recyclerView, VIEW_INVISIBLE);
        }
    }

    private void exibeRecyclerComAnimacao() {
        final LayoutAnimationController animSlideIn =
                AnimationUtils.loadLayoutAnimation(this.requireContext(), R.anim.layout_controller_animation_slide_in_left);
        final Handler handler = new Handler();
        final Runnable r = ()->{
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getDataSet_freteEmAberto().size(), alertaLayout, recyclerView, VIEW_INVISIBLE);
            recyclerView.setLayoutAnimation(animSlideIn);
        };
        handler.postDelayed(r, 100);
    }

    private void configuraAdapter() {
        adapter = new FreteAReceberAdapter(
                this,
                viewModel.getDataSet_freteEmAberto(),
                viewModel.getDataSet_cavalo(),
                viewModel.getDataSet_Recebimento()
        );
        recyclerView.setAdapter(adapter);
        NavController controlador = Navigation.findNavController(requireView());
        adapter.setOnItemClickListener(freteId -> {
            direction = FreteAReceberEmAbertoFragmentDirections.actionNavFreteReceberToNavFreteAReceberResumo(freteId);
            controlador.navigate(direction);
        });

    }

    public void actSolicitaAtualizacao() {
        adapter.atualiza(viewModel.getDataSet_freteEmAberto(), viewModel.getDataSet_Recebimento());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getDataSet_freteEmAberto().size(), alertaLayout, recyclerView, VIEW_INVISIBLE);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        Frete frete = viewModel.getDataSet_freteEmAberto().get(posicao);
        BigDecimal valorLiquidoAReceber = frete.getFreteLiquidoAReceber();

        List<RecebimentoDeFrete> listaRecebimento = FiltraRecebimentoFrete.listaPeloIdDoFrete(viewModel.getDataSet_Recebimento(), frete.getId());
        BigDecimal valorTotalRecebido = FiltraRecebimentoFrete.valorTotalRecebido(listaRecebimento);
        String placa = FiltraCavalo.localizaPeloId(viewModel.getDataSet_cavalo(), frete.getRefCavaloId()).getPlaca();

        if (item.getItemId() == R.id.FechaFrete) {
            new AlertDialog.Builder(this.requireContext())
                    .setTitle(placa + " " + frete.getDestino())
                    .setMessage(VOCE_CONFIRMA_O_FECHAMENTO)
                    .setPositiveButton(CONFIRMAR, (dialog, which) -> {
                        if (valorLiquidoAReceber.compareTo(valorTotalRecebido) == 0) {
                            frete.setFreteJaFoiPago(true);
                            viewModel.salvaFrete(frete).observe(this,
                                    ignore -> {
                                        MensagemUtil.toast(requireContext(), FRETE_FECHADO);
                                    });
                        } else {
                            MensagemUtil.snackBar(getView(), VALOR_RECEBIDO_NAO_CONFERE);
                        }
                    })
                    .setNegativeButton(CANCELAR, null)
                    .show();
        }
        return super.onContextItemSelected(item);
    }

    public void exibeResultadoDeBusca(List<Frete> dataSetSearch) {
        adapter.exibeSearch(dataSetSearch);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSetSearch.size(), binding.alertaLayout.alerta, recyclerView, VIEW_INVISIBLE);
    }

}