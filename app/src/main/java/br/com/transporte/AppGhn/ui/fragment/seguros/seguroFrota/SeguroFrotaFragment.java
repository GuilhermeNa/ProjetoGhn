package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_RENOVADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;
import static br.com.transporte.AppGhn.ui.fragment.seguros.TipoDeSeguro.FROTA;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaComSeguroFrotaDao;
import br.com.transporte.AppGhn.databinding.FragmentSeguroFrotaBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.SeguroFrotaAdapter;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class SeguroFrotaFragment extends Fragment {
    public static final String SEGUROS = "Seguros";
    private List<DespesaComSeguroFrota> dataSet;
    private FragmentSeguroFrotaBinding binding;
    private RoomDespesaComSeguroFrotaDao segurosDao;
    private RecyclerView recyclerView;
    private NavDirections direction;
    private SeguroFrotaAdapter adapter;
    private RoomCavaloDao cavaloDao;
    private LinearLayout buscaVazia;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();
    private ToolbarUtil toolbarUtil;

    @NonNull
    @Contract(" -> new")
    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_UPDATE:
                            atualizaAdapter(REGISTRO_RENOVADO);
                            break;

                        case RESULT_CANCELED:
                            MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                            break;
                    }
                });
    }

    private void atualizaAdapter(String msg) {
        atualizaDataSet();
        adapter.atualiza(getDataSet());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet.size(), buscaVazia, recyclerView, VIEW_INVISIBLE);
        MensagemUtil.toast(requireContext(), msg);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        atualizaDataSet();
    }

    private void inicializaDataBase() {
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        segurosDao = dataBase.getRoomDespesaComSeguroFrotaDao();
        cavaloDao = dataBase.getRoomCavaloDao();
    }

    @NonNull
    @Contract(" -> new")
    private List<DespesaComSeguroFrota> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    private void atualizaDataSet() {
        if (dataSet == null) dataSet = new ArrayList<>();
        dataSet = segurosDao.listaPorValidade(true);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSeguroFrotaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        NavController controlador = Navigation.findNavController(view);
        configuraRecycler(controlador);
        configuraToolbar();
        configuraMenuProvider();
        configuraUi();
    }

    private void inicializaCamposDaView() {
        buscaVazia = binding.fragSegurosVazio;
        recyclerView = binding.fragSegurosRecycler;
    }

    private void configuraUi() {
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet.size(), buscaVazia, recyclerView, VIEW_INVISIBLE);
    }

    private void configuraRecycler(NavController controlador) {
        adapter = new SeguroFrotaAdapter(this, getDataSet());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(seguroId -> {
            direction = SeguroFrotaFragmentDirections.actionNavSegurosFragmentToSeguroResumoFragment(seguroId, FROTA);
            controlador.navigate(direction);
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        DespesaComSeguroFrota seguro = dataSet.get(posicao);

        switch (item.getItemId()) {
            case R.id.visualizaParcelas:
                ExibeParcelasFrotaDialog dialog = new ExibeParcelasFrotaDialog(this.requireContext());
                dialog.showBottomDialog(seguro);
                break;

            case R.id.renovarSeguro:
                Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_FROTA);
                intent.putExtra(CHAVE_REQUISICAO, RENOVANDO);
                intent.putExtra(CHAVE_ID, seguro.getId());
                activityResultLauncher.launch(intent);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        toolbarUtil = new ToolbarUtil(SEGUROS);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
    }

    private void configuraMenuProvider() {
        SeguroFrotaMenuProviderHelper menuProviderHelper = new SeguroFrotaMenuProviderHelper(getDataSet(), cavaloDao);
        requireActivity().addMenuProvider(menuProviderHelper, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setCallbackMenuProvider(new SeguroFrotaMenuProviderHelper.CallbackMenuProvider() {
            @Override
            public void realizaBusca(List<DespesaComSeguroFrota> dataSet_search) {
                ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet_search.size(), buscaVazia, recyclerView, VIEW_INVISIBLE);
                adapter.atualiza(dataSet_search);
            }

            @Override
            public void searchViewAtiva() {
                toolbarUtil.setTitleAtivo(false);
            }

            @Override
            public void searchViewInativa() {
                toolbarUtil.setTitleAtivo(true);
            }

            @Override
            public void logoutClick() {
                MensagemUtil.toast(requireContext(), LOGOUT);
            }

            @Override
            public void homeClick() {
                requireActivity().finish();
            }
        });
    }
    //----------------------------------------------------------------------------------------------
    //                                     Metodos Publicos                                       ||
    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(String msg) {
        //Usado pela activity para solicitar atualizacao
        atualizaAdapter(msg);
    }


}