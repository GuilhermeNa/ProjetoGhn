package br.com.transporte.AppGhn.ui.fragment.media;

import static android.view.View.INVISIBLE;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.databinding.FragmentMediaBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.ui.adapter.MediaAdapter_Abastecimentos;
import br.com.transporte.AppGhn.ui.adapter.MediaAdapter_Cavalos;
import br.com.transporte.AppGhn.util.AnimationUtil;

public class MediaFragment extends Fragment {
    public static final int CAVALO_ID_PARA_PRIMEIRA_BUSCA = 1;
    private FragmentMediaBinding binding;
    private CustosDeAbastecimentoDAO abastecimentoDao;
    private MediaAdapter_Abastecimentos adapterDeAbastecimentos;
    private TextView placaTxt;
    private CavaloDAO cavaloDao;
    private RecyclerView recyclerDeAbastecimentos;
    private Handler handler;
    private MediaAdapter_Cavalos adapterDeCavalos;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        abastecimentoDao = new CustosDeAbastecimentoDAO();
        cavaloDao = new CavaloDAO();
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMediaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraUi();
        configuraToolbar();
        configuraRecyclerDeCavalos();
        configuraRecyclerDeAbastecimentos();
        configuraMenuProvider(adapterDeCavalos);
    }

    private void inicializaCamposDaView() {
        placaTxt = binding.cavaloPlaca;
        recyclerDeAbastecimentos = binding.recycler;
    }

    private void configuraUi() {
        String placa = cavaloDao.listaTodos().get(1).getPlaca();
        placaTxt.setText(placa);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Média");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

    }

    private void configuraMenuProvider(MediaAdapter_Cavalos adapterDeCavalos) {
        MenuProvider menuProvider = new MenuProviderHelperMedia(this, adapterDeCavalos);
        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    //----------------------------------------------
    // -> Recycler de Cavalos <- |||||||||||||||||||
    //----------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraRecyclerDeCavalos() {
        CardView cardCavaloSelecionado = binding.cavaloSelecionado;
        RecyclerView recyclerDeCavalos = binding.recyclerCavalos;

        configuraAdapter(recyclerDeCavalos);
        configuraLayoutManager(recyclerDeCavalos);
        configuraAdapterClickListener(cardCavaloSelecionado);
    }

    private void configuraAdapter(RecyclerView recyclerDeCavalos) {
        adapterDeCavalos = new MediaAdapter_Cavalos(this.requireContext(), getListaDeCavalos());
        recyclerDeCavalos.setAdapter(adapterDeCavalos);
    }

    private void configuraLayoutManager(RecyclerView recyclerDeCavalos) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerDeCavalos.setLayoutManager(layoutManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraAdapterClickListener(CardView cardCavaloSelecionado) {
        adapterDeCavalos.setOnItemClickListener((cavalo, posicao) -> {
            adapterDeCavalos.teste(posicao);

            handler = new Handler();
            animacaoExibeNovoCavaloSelecionado(cardCavaloSelecionado, cavalo);
            animacaoExibeNovaListaDeAbastecimentos(cavalo.getId());

        });
    }

    private void animacaoExibeNovoCavaloSelecionado(CardView cardCavaloSelecionado, Cavalo cavalo) {
        cardCavaloSelecionado.setVisibility(INVISIBLE);
        placaTxt.setText(cavalo.getPlaca());
        cardCavaloSelecionado.setVisibility(View.VISIBLE);
        AnimationUtil.defineAnimacao(this.requireContext(), R.anim.item_animation_slide_in_left, cardCavaloSelecionado);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void animacaoExibeNovaListaDeAbastecimentos(int cavaloId) {
        final LayoutAnimationController animController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_controller_animation_slide_out_down);

        recyclerDeAbastecimentos.setLayoutAnimation(animController);
        Runnable runnable = () -> {
            recyclerDeAbastecimentos.setVisibility(INVISIBLE);
            atualizaAdapterDeAbastecimentos(cavaloId);
        };
        handler.postDelayed(runnable, 200);
    }

    private List<Cavalo> getListaDeCavalos() {
        return cavaloDao.listaTodos();
    }

    //----------------------------------------------------
    // -> Recycler de Abastecimentos <- ||||||||||||||||||
    //----------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraRecyclerDeAbastecimentos() {
        configuraAdapter();
        configuraTouchHelper();
    }

    private void configuraTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallbackAbastecimentos(adapterDeAbastecimentos));
        itemTouchHelper.attachToRecyclerView(recyclerDeAbastecimentos);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraAdapter() {
        adapterDeAbastecimentos = new MediaAdapter_Abastecimentos(getListaDeAbastecimentosComFlags(CAVALO_ID_PARA_PRIMEIRA_BUSCA), this.requireContext());
        recyclerDeAbastecimentos.setAdapter(adapterDeAbastecimentos);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void atualizaAdapterDeAbastecimentos(int cavaloId) {
        final LayoutAnimationController animController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_controller_animation_slide_in_left);

        devolveVisibilidadeAoRecycler();
        adapterDeAbastecimentos.atualiza(getListaDeAbastecimentosComFlags(cavaloId));
        recyclerDeAbastecimentos.setLayoutAnimation(animController);
        recyclerDeAbastecimentos.scheduleLayoutAnimation();
    }

    private void devolveVisibilidadeAoRecycler() {
        if (recyclerDeAbastecimentos.getVisibility() == INVISIBLE) {
            recyclerDeAbastecimentos.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<CustosDeAbastecimento> getListaDeAbastecimentosComFlags(int cavaloId) {
        return abastecimentoDao.listaTodos().stream()
                .filter(c -> c.getRefCavalo() == cavaloId)
                .filter(CustosDeAbastecimento::isFlagAbastecimentoTotal)
                .collect(Collectors.toList());
    }

}
