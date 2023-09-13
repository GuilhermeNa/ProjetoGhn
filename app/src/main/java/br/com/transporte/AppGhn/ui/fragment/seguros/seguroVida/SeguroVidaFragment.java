package br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_RENOVADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;
import static br.com.transporte.AppGhn.ui.fragment.seguros.TipoDeSeguro.VIDA;

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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
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
import br.com.transporte.AppGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.AppGhn.databinding.FragmentSeguroVidaBinding;
import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.SeguroVidaAdapter;
import br.com.transporte.AppGhn.ui.fragment.extensions.BuscaDeDadosSemResultado;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class SeguroVidaFragment extends Fragment {
    public static final String SEGUROS = "Seguros";
    private FragmentSeguroVidaBinding binding;
    private RoomDespesaSeguroVidaDao seguroDao;
    private List<DespesaComSeguroDeVida> dataSet;
    private SeguroVidaAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout avisoDeListaVaziaLayout;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();

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
                            atualizaAdapter(REGISTRO_APAGADO);
                            break;
                    }
                });
    }

    //------------------------------------ Metodos Publicos ----------------------------------------

    public void atualizaAdapter(String msg) {
        atualizaDataSet();
        adapter.atualiza(getDataSet());
        configuraUiBuscaSemResultados();
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

    private void atualizaDataSet() {
        if (dataSet == null) dataSet = new ArrayList<>();
        dataSet = seguroDao.listaPorValidade(true);
    }

    private void inicializaDataBase() {
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        seguroDao = dataBase.getRoomDespesaSeguroVidaDao();
    }

    @NonNull
    @Contract(" -> new")
    private List<DespesaComSeguroDeVida> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSeguroVidaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraRecycler();
        configuraToolbar();
        configuraUiBuscaSemResultados();
    }

    private void configuraUiBuscaSemResultados() {
        BuscaDeDadosSemResultado.substituiRecyclerPorAviso(dataSet.size(), recyclerView, avisoDeListaVaziaLayout);
    }

    private void inicializaCampos() {
        avisoDeListaVaziaLayout = binding.fragSegurosVazio;
        recyclerView = binding.fragSegurosRecycler;
    }

    private void configuraRecycler() {
        adapter = new SeguroVidaAdapter(this, getDataSet());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(seguro -> {
            NavController controlador = Navigation.findNavController(requireView());
            NavDirections direction = SeguroVidaFragmentDirections.actionNavSeguroOutrosToSeguroResumoFragment(((DespesaComSeguro) seguro).getId(), VIDA);
            controlador.navigate(direction);
        });
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(SEGUROS);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_padrao, menu);
                menu.removeItem(R.id.menu_padrao_search);
                menu.removeItem(R.id.menu_padrao_editar);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        DespesaComSeguroDeVida seguro = dataSet.get(posicao);

        switch (item.getItemId()) {
            case R.id.visualizaParcelas:
                ExibeParcelasVidaDialog dialog = new ExibeParcelasVidaDialog(this.requireContext());
                dialog.showBottomDialog(seguro);
                break;

            case R.id.renovarSeguro:
                Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_VIDA);
                intent.putExtra(CHAVE_REQUISICAO, RENOVANDO);
                intent.putExtra(CHAVE_ID, seguro.getId());
                activityResultLauncher.launch(intent);
                break;
        }
        return super.onContextItemSelected(item);

    }

    //----------------------------------------------------------------------------------------------
    //                                          OnResume                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        atualizaDataSet();
    }
}