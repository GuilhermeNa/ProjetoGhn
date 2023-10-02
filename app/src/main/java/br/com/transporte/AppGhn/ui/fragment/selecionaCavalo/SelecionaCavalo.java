package br.com.transporte.AppGhn.ui.fragment.selecionaCavalo;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.CAVALOS;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.databinding.FragmentSelecionaCavaloBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.enums.TipoSelecionaCavalo;
import br.com.transporte.AppGhn.ui.activity.areaMotoristaActivity.AreaMotoristaActivity;
import br.com.transporte.AppGhn.ui.adapter.SelecionaCavaloAdapter;
import br.com.transporte.AppGhn.ui.viewmodel.MainActViewModel;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class SelecionaCavalo extends Fragment {
    private FragmentSelecionaCavaloBinding binding;
    private SelecionaCavaloAdapter adapter;
    private LinearLayout alertaView;
    private NavController controlador;
    private RecyclerView recyclerView;
    private TipoSelecionaCavalo requisicao;
    private ToolbarUtil toolbarUtil;
    private SelecionaCavaloMenuProviderHelper menuProviderHelper;
    private List<Cavalo> dataSet;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requisicao = SelecionaCavaloArgs.fromBundle(getArguments()).getTipoDirection();
        configuraViewModel();
        Log.d("teste", "onCreate");
    }

    private void configuraViewModel() {
        MainActViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainActViewModel.class);
        observerData_Cavalos(viewModel);
    }

    private void observerData_Cavalos(@NonNull MainActViewModel viewModel) {
        viewModel.buscaCavalos().observe(this,
                resource -> {
                    List<Cavalo> listaCavalos = resource.getDado();
                    String erro = resource.getErro();
                    int listaSize;
                    if (listaCavalos != null) {
                        dataSet = listaCavalos;
                        adapter.atualiza(listaCavalos);
                        menuProviderHelper.atualizaDataSet(listaCavalos);
                        listaSize = listaCavalos.size();
                        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaSize, alertaView, recyclerView, VIEW_INVISIBLE);
                    }
                    if (erro != null) {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelecionaCavaloBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                         OnView Created                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraToolbar();
        configuraMenuProvider();
        controlador = Navigation.findNavController(view);
        configuraRecycler();
        Log.d("teste", "onViewCreated");

    }

    private void inicializaCamposDaView() {
        recyclerView = binding.recycler;
        alertaView = binding.buscaVazia;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        toolbarUtil = new ToolbarUtil(CAVALOS);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
    }

    private void configuraMenuProvider() {
        menuProviderHelper = new SelecionaCavaloMenuProviderHelper(new ArrayList<>());
        requireActivity().addMenuProvider(menuProviderHelper, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setCallback(new SelecionaCavaloMenuProviderHelper.MenuProviderCallback() {
            @Override
            public void buscaFinalizada(List<Cavalo> dataSet_search) {
                adapter.atualiza(dataSet_search);
                ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet_search.size(), alertaView, recyclerView, VIEW_INVISIBLE);
            }

            @Override
            public void onSearchSelecionada() {
                toolbarUtil.setTitleAtivo(false);
            }

            @Override
            public void onSearchDesselecionada() {
                toolbarUtil.setTitleAtivo(true);
            }

            @Override
            public void onLogoutClick() {
                MensagemUtil.toast(requireContext(), LOGOUT);
            }

            @Override
            public void onHomeClick() {
                controlador.popBackStack();
            }
        });
    }

    private void configuraRecycler() {
        adapter = new SelecionaCavaloAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        configuraNavegacao();
    }

    private void configuraNavegacao() {
        adapter.setOnItemClickListener(
                cavaloId -> {
                    switch (requisicao) {
                        case MANUTENCAO:
                            NavDirections direction = SelecionaCavaloDirections
                                    .actionNavSelecionaCavaloFragmentToNavManutencaoDetalhes(cavaloId);
                            controlador.navigate(direction);
                            break;

                        case AREA_MOTORISTA:
                            Intent intent = new Intent(this.requireContext(), AreaMotoristaActivity.class);
                            intent.putExtra(CHAVE_ID_CAVALO, cavaloId);
                            startActivity(intent);
                            break;
                    }
                });
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        if(dataSet != null){
            adapter.atualiza(dataSet);
            menuProviderHelper.atualizaDataSet(dataSet);
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet.size(), alertaView, recyclerView, VIEW_INVISIBLE);
        }
    }

}