package br.com.transporte.AppGhn.ui.fragment.media;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterAbastecimentoHelper.NULL_FLAG;
import static br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterAbastecimentoHelper.PRIMEIRA_FLAG_SELECIONADA;
import static br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterAbastecimentoHelper.SEGUNDA_FLAG_SELECIONADA;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.databinding.FragmentMediaBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.ui.fragment.media.dialog.MediaBottomDialog;
import br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterAbastecimentoHelper;
import br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaAdapterCavaloHelper;
import br.com.transporte.AppGhn.ui.fragment.media.helpers.MediaMenuProviderHelper;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class MediaFragment extends Fragment {
    private FragmentMediaBinding binding;
    public static final int POSICAO_CAVALO_EXIBIDO_NA_ABERTURA = 0;
    private MediaAdapterAbastecimentoHelper adapterDeAbastecimentoHelper;
    private MediaAdapterCavaloHelper adapterDeCavaloHelper;
    private MediaMenuProviderHelper menuProvider;
    private TextView placaTxt, dataCard1, kmCard1, dataCard2, kmCard2;
    private RecyclerView recyclerDeAbastecimentos;
    private ConstraintLayout constraintLayout;
    private CardView card1, card2;
    private Button btnFiltrar;
    private RoomCavaloDao cavaloDao;
    private List<Cavalo> dataSet_cavalos;
    private Cavalo cavaloExibido;
    private Runnable runnable;
    private Handler handler;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        atualizaDataSet();
        handler = new Handler();
        cavaloExibido = getDataSet().get(POSICAO_CAVALO_EXIBIDO_NA_ABERTURA);
        dataSet_cavalos = configuracaListaParaExibicao(getDataSet());
    }

    private void inicializaDataBase() {
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        cavaloDao = dataBase.getRoomCavaloDao();
    }

    private void atualizaDataSet() {
        if (dataSet_cavalos == null) dataSet_cavalos = new ArrayList<>();
    //    dataSet_cavalos = cavaloDao.todos();
    }

    @NonNull
    private List<Cavalo> configuracaListaParaExibicao(@NonNull List<Cavalo> lista) {
        lista.remove(POSICAO_CAVALO_EXIBIDO_NA_ABERTURA);
        return lista;
    }

    @NonNull
    @Contract(" -> new")
    private List<Cavalo> getDataSet() {
        return new ArrayList<>(dataSet_cavalos);
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
        inicializaCamposDaView();
        configuraToolbar();
        configuraAdapterDeCavaloHelper();
        configuraAdapterDeAbastecimentoHelper();
        configuraMenuProvider();
        configuraCLickRemoveCardDaViewEInsereNovamenteNaLista();
        configuraVisibilidadeDoBtnFiltrar();
        configuraUi_cavaloSelecionado(cavaloExibido.getPlaca());
        configuraBtnFiltrar();
    }

    private void configuraBtnFiltrar() {
        btnFiltrar.setOnClickListener(v -> {
            CustosDeAbastecimento flag1 = adapterDeAbastecimentoHelper.getFlagAbastecimento1();
            CustosDeAbastecimento flag2 = adapterDeAbastecimentoHelper.getFlagAbastecimento2();

            MediaBottomDialog dialog = new MediaBottomDialog(requireContext(), flag1, flag2, cavaloExibido);
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

    private void configuraMenuProvider() {
        menuProvider = new MediaMenuProviderHelper(this, dataSet_cavalos);
        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        menuProvider.setMenuProviderCallback(dataSet_searchView ->
                adapterDeCavaloHelper.atualizaAdapterDataSetPorSearch(dataSet_searchView));
    }

    private void configuraUi_cavaloSelecionado(String placa) {
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

    private void escondeCardView(CardView card, int animId) {
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
        adapterDeAbastecimentoHelper = new MediaAdapterAbastecimentoHelper(cavaloExibido, this.requireContext(), recyclerDeAbastecimentos);
        adapterDeAbastecimentoHelper.configuraAdapter();
        adapterDeAbastecimentoHelper.setAdapterCallback(this::configuraCLickListener);
    }

    private void configuraCLickListener(String flagSelecinada, CustosDeAbastecimento abastecimento) {
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

    private void vinculaDadosNaView(CustosDeAbastecimento abastecimento, TextView dataTxt, TextView kmTxt) {
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
        RecyclerView recyclerDeCavalos = binding.recyclerCavalos;

        adapterDeCavaloHelper = new MediaAdapterCavaloHelper(this.requireContext(), dataSet_cavalos, cavaloExibido, recyclerDeCavalos);
        adapterDeCavaloHelper.configuraRecyclerDeCavalos();

        adapterDeCavaloHelper.setAdapterCallback((cavaloClicado) -> {
            this.cavaloExibido = cavaloClicado;

            ui_animacaoExibeNovoCavaloSelecionado(cavaloClicado);
            ui_animacaoExibeNovaListaDeAbastecimentos();
            menuProvider.atualizaDataSet(adapterDeCavaloHelper.getAdapterDataSet());

            adapterDeAbastecimentoHelper.atualizaAdapterDeAbastecimentosAoSelecionarNovoCavalo(cavaloClicado.getId());
            removeVisibilidadeDoFiltro(btnFiltrar, GONE);
            removeVisibilidadeDoFiltro(card1, INVISIBLE);
            removeVisibilidadeDoFiltro(card2, INVISIBLE);
            removeVisibilidadeDoFiltro(constraintLayout, INVISIBLE);
        });
    }

    private void removeVisibilidadeDoFiltro(View view, int visibilidade) {
        if (view.getVisibility() == VISIBLE) {
            view.setVisibility(visibilidade);
            AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_out_bottom, view);
        }
    }

    private void ui_animacaoExibeNovoCavaloSelecionado(Cavalo cavalo) {
        CardView cardCavaloSelecionado = binding.cavaloSelecionado;

        cardCavaloSelecionado.setVisibility(INVISIBLE);
        configuraUi_cavaloSelecionado(cavalo.getPlaca());
        cardCavaloSelecionado.setVisibility(VISIBLE);
        AnimationUtil.defineAnimacao(this.requireContext(), R.anim.item_animation_slide_in_left, cardCavaloSelecionado);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

}



