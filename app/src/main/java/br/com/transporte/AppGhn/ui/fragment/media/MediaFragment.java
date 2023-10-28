package br.com.transporte.AppGhn.ui.fragment.media;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterAbastecimentoHelper.NULL_FLAG;
import static br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterAbastecimentoHelper.PRIMEIRA_FLAG_SELECIONADA;
import static br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterAbastecimentoHelper.SEGUNDA_FLAG_SELECIONADA;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentMediaBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.AppGhn.repository.CustoDePercursoRepository;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.Resource;
import br.com.transporte.AppGhn.ui.fragment.media.dialog.MediaBottomDialog;
import br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterAbastecimentoHelper;
import br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterCavaloHelper;
import br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaMenuProviderHelper;
import br.com.transporte.AppGhn.ui.fragment.media.viewmodel.MediaFragmentViewModel;
import br.com.transporte.AppGhn.ui.fragment.media.viewmodel.MediaFragmentViewModelFactory;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class MediaFragment extends Fragment {
    private FragmentMediaBinding binding;
    private MediaAdapterAbastecimentoHelper adapterDeAbastecimentoHelper;
    private MediaAdapterCavaloHelper adapterDeCavaloHelper;
    private MediaMenuProviderHelper menuProvider;
    private TextView placaTxt, dataCard1, kmCard1, dataCard2, kmCard2;
    private RecyclerView recyclerDeAbastecimentos;
    private ConstraintLayout constraintLayout;
    private CardView card1, card2;
    private Button btnFiltrar;
    private Runnable runnable;
    private Handler handler;
    private MediaFragmentViewModel viewModel;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();

        handler = new Handler();
    }

    private void inicializaViewModel() {
        final CavaloRepository repository = new CavaloRepository(requireContext());
        final CustoDeAbastecimentoRepository abastecimentoRepository = new CustoDeAbastecimentoRepository(requireContext());
        final FreteRepository freteRepository = new FreteRepository(requireContext());
        final CustoDePercursoRepository custoRepository = new CustoDePercursoRepository(requireContext());
        final MediaFragmentViewModelFactory factory = new MediaFragmentViewModelFactory(repository, abastecimentoRepository, freteRepository, custoRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(MediaFragmentViewModel.class);
    }

    private void carregaDados() {
        final LiveData<Resource<List<Cavalo>>> observer =
                viewModel.carrregaCavalos();
        observer.observe(getViewLifecycleOwner(), new Observer<Resource<List<Cavalo>>>() {
            @Override
            public void onChanged(Resource<List<Cavalo>> resource) {
                observer.removeObserver(this);
                if(resource.getDado().isEmpty()){
                    final NavController controler = Navigation.findNavController(requireView());
                    controler.popBackStack();
                } else {
                    viewModel.setDataSetCavalos(resource.getDado());
                    viewModel.configuraDadosDeCavaloUseCase();
                    adapterDeCavaloHelper.atualizaData(viewModel.getDataSetCavalos());
                    carregaAbastecimentos();
                }
            }
        });
    }

    private void carregaAbastecimentos() {
        final LiveData<Resource<List<CustosDeAbastecimento>>> observer =
                viewModel.carregaAbastecimentosDoCavaloSelecionado();
        observer.observe(getViewLifecycleOwner(),
                new Observer<Resource<List<CustosDeAbastecimento>>>() {
                    @Override
                    public void onChanged(Resource<List<CustosDeAbastecimento>> resource) {
                        observer.removeObserver(this);
                        MediaFragment.this.enviaDataParaView(resource.getDado());
                        MediaFragment.this.bindPlaca();
                    }
                });
    }

    private void enviaDataParaView(
            final List<CustosDeAbastecimento> listaAbastecimento
    ) {
        menuProvider.atualizaDataSet(viewModel.getDataSetCavalos());
        adapterDeAbastecimentoHelper.atualizaAdapterDeAbastecimentosAoSelecionarNovoCavalo(listaAbastecimento);
        adapterDeCavaloHelper.setCavaloArmazenado(viewModel.getCavaloSelecionado());
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        carregaDados();
        inicializaCamposDaView();
        configuraToolbar();
        configuraAdapterDeCavaloHelper();
        configuraAdapterDeAbastecimentoHelper();
        configuraMenuProvider();
        configuraCLickRemoveCardDaViewEInsereNovamenteNaLista();
        //configuraVisibilidadeDoBtnFiltrar();
        configuraBtnFiltrar();
    }

    private void inicializaCamposDaView() {
        constraintLayout = binding.constraintItens;
        card1 = binding.cardItem1;
        card2 = binding.cardItem2;
        dataCard1 = binding.item1MediaData;
        kmCard1 = binding.item1MediaKm;
        dataCard2 = binding.item2MediaData;
        kmCard2 = binding.item2MediaKm;
        btnFiltrar = binding.btnFiltrar;
        placaTxt = binding.cavaloPlaca;
        recyclerDeAbastecimentos = binding.recycler;
    }

    private void configuraBtnFiltrar() {
        btnFiltrar.setOnClickListener(v -> {
            CustosDeAbastecimento flag1 = adapterDeAbastecimentoHelper.getFlagAbastecimento1();
            CustosDeAbastecimento flag2 = adapterDeAbastecimentoHelper.getFlagAbastecimento2();

            MediaBottomDialog dialog = new MediaBottomDialog(requireContext(), flag1, flag2, viewModel, this);
            dialog.showBottomDialog();

        });
    }

    private void configuraVisibilidadeDoBtnFiltrar() {
        if (card1.getVisibility() == VISIBLE && card2.getVisibility() == VISIBLE) {
            runnable = () -> {
                btnFiltrar.setVisibility(VISIBLE);
                AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_in_bottom, btnFiltrar);
            };

        } else {
            runnable = () -> {
                btnFiltrar.setVisibility(GONE);
                AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_out_bottom, btnFiltrar);
            };
        }

        handler.postDelayed(runnable, 450);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Média");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void configuraMenuProvider() {
        menuProvider = new MediaMenuProviderHelper(this, new ArrayList<>());
        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        menuProvider.setMenuProviderCallback(dataSet_searchView ->
                adapterDeCavaloHelper.atualizaAdapterDataSetPorSearch(dataSet_searchView));
    }

    private void bindPlaca() {
        final String placa = viewModel.getCavaloSelecionado().getPlaca();
        placaTxt.setText(placa);
    }

    //-------------------------------------------------------------------------
    // -> UI - Click que remove Card                                         ||
    //-------------------------------------------------------------------------

    private void configuraCLickRemoveCardDaViewEInsereNovamenteNaLista() {
        card1.setOnClickListener(v -> {
            escondeCardView(card1, R.anim.slide_out_left);
            configuraVisibilidadeDoBtnFiltrar();
        });

        card2.setOnClickListener(v -> {
            escondeCardView(card2, R.anim.slide_out_right);
            configuraVisibilidadeDoBtnFiltrar();
        });
    }

    private void escondeCardView(@NonNull CardView card, int animId) {
        card.setVisibility(INVISIBLE);
        AnimationUtil.defineAnimacao(this.requireContext(), animId, card);
        if (card == card1) {
            adapterDeAbastecimentoHelper.removeCardDaViewEInsereNovamenteNaLista(PRIMEIRA_FLAG_SELECIONADA);
        } else {
            adapterDeAbastecimentoHelper.removeCardDaViewEInsereNovamenteNaLista(SEGUNDA_FLAG_SELECIONADA);
        }
    }

    //----------------------------------------------------
    // -> Adapter Abastecimento                         ||
    //----------------------------------------------------

    private void configuraAdapterDeAbastecimentoHelper() {
        adapterDeAbastecimentoHelper = new MediaAdapterAbastecimentoHelper(viewModel.getCavaloSelecionado(), this.requireContext(), recyclerDeAbastecimentos);
        adapterDeAbastecimentoHelper.configuraAdapter();
        adapterDeAbastecimentoHelper.setAdapterCallback(this::configuraCLickListener);
    }

    private void configuraCLickListener(@NonNull String flagSelecinada, CustosDeAbastecimento abastecimento) {
        switch (flagSelecinada) {

            case PRIMEIRA_FLAG_SELECIONADA:
                if (constraintLayout.getVisibility() != VISIBLE) {
                    constraintLayout.setVisibility(VISIBLE);
                    AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_in_bottom, constraintLayout);
                    runnable = () -> {
                        card1.setVisibility(VISIBLE);
                        AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_in_left, card1);
                        vinculaDadosNaView(abastecimento, dataCard1, kmCard1);
                    };
                    handler.postDelayed(runnable, 450);

                } else {
                    card1.setVisibility(VISIBLE);
                    AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_in_left, card1);
                    vinculaDadosNaView(abastecimento, dataCard1, kmCard1);
                }
                configuraVisibilidadeDoBtnFiltrar();
                break;

            case SEGUNDA_FLAG_SELECIONADA:
                card2.setVisibility(VISIBLE);
                AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_in_right, card2);
                vinculaDadosNaView(abastecimento, dataCard2, kmCard2);
                configuraVisibilidadeDoBtnFiltrar();
                break;

            case NULL_FLAG:
                MensagemUtil.toast(this.requireContext(), "Exclua um item antes de adicionar");
        }
    }

    private void vinculaDadosNaView(@NonNull CustosDeAbastecimento abastecimento, @NonNull TextView dataTxt, @NonNull TextView kmTxt) {
        String data = ConverteDataUtil.dataParaString(abastecimento.getData());
        dataTxt.setText(data);

        String km = FormataNumerosUtil.formataNumero(abastecimento.getMarcacaoKm());
        String kmFormatado = "KM " + km;
        kmTxt.setText(kmFormatado);
    }

    //----------------------------------------------------
    // -> Adapter Cavalo                                ||
    //----------------------------------------------------

    private void configuraAdapterDeCavaloHelper() {
        final RecyclerView recyclerDeCavalos = binding.recyclerCavalos;

        adapterDeCavaloHelper = new MediaAdapterCavaloHelper(this.requireContext(), new ArrayList<>(), viewModel.getCavaloSelecionado(), recyclerDeCavalos);
        adapterDeCavaloHelper.configuraRecyclerDeCavalos();

        adapterDeCavaloHelper.setAdapterCallback((cavaloClicado) -> {
            viewModel.setCavaloSelecionado(cavaloClicado);

            ui_animacaoExibeNovoCavaloSelecionado();
            ui_animacaoExibeNovaListaDeAbastecimentos();

            carregaAbastecimentos();
            removeVisibilidadeDoFiltro(btnFiltrar, GONE);
            removeVisibilidadeDoFiltro(card1, INVISIBLE);
            removeVisibilidadeDoFiltro(card2, INVISIBLE);
            removeVisibilidadeDoFiltro(constraintLayout, INVISIBLE);
        });
    }

    private void removeVisibilidadeDoFiltro(@NonNull View view, int visibilidade) {
        if (view.getVisibility() == VISIBLE) {
            view.setVisibility(visibilidade);
            AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_out_bottom, view);
        }
    }

    private void ui_animacaoExibeNovoCavaloSelecionado() {
        CardView cardCavaloSelecionado = binding.cavaloSelecionado;
        cardCavaloSelecionado.setVisibility(INVISIBLE);
        bindPlaca();
        cardCavaloSelecionado.setVisibility(VISIBLE);
        AnimationUtil.defineAnimacao(this.requireContext(), R.anim.item_animation_slide_in_left, cardCavaloSelecionado);
    }

    private void ui_animacaoExibeNovaListaDeAbastecimentos() {
        final LayoutAnimationController animSlideOut = AnimationUtils.loadLayoutAnimation(this.requireContext(), R.anim.layout_controller_animation_slide_out_down);
        final LayoutAnimationController animSlideIn = AnimationUtils.loadLayoutAnimation(this.requireContext(), R.anim.layout_controller_animation_slide_in_left);

        recyclerDeAbastecimentos.setVisibility(INVISIBLE);
        recyclerDeAbastecimentos.setLayoutAnimation(animSlideOut);

        runnable = () -> {
            recyclerDeAbastecimentos.setVisibility(VISIBLE);
            recyclerDeAbastecimentos.setLayoutAnimation(animSlideIn);
            recyclerDeAbastecimentos.scheduleLayoutAnimation();
        };
        handler.postDelayed(runnable, 200);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        handler.removeCallbacksAndMessages(null);
    }
}



