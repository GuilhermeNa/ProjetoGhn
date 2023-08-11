package br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;

import br.com.transporte.AppGhn.databinding.FragmentSeguroVidaBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.SeguroVidaAdapter;
import br.com.transporte.AppGhn.dao.DespesasSeguroDAO;
import br.com.transporte.AppGhn.ui.fragment.extensions.BuscaDeDadosSemResultado;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class SeguroVidaFragment extends Fragment {
    private FragmentSeguroVidaBinding binding;
    private DespesasSeguroDAO seguroDao;
    private List<DespesaComSeguroDeVida> listaDeSegurosVida;
    private SeguroVidaAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout avisoDeListaVaziaLayout;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();

    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch(resultCode){
                        case RESULT_UPDATE:
                            atualizaAdapter("Registro renovado");
                            break;

                        case RESULT_CANCELED:
                            atualizaAdapter("Registro apagado");
                            break;
                    }
                });
    }

    //------------------------------------ Metodos Publicos ----------------------------------------

    public void atualizaAdapter(String msg){
        listaDeSegurosVida = getListaDeSegurosVida();
        adapter.atualiza(listaDeSegurosVida);
        configuraUiBuscaSemResultados();
        MensagemUtil.toast(requireContext(), msg);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seguroDao = new DespesasSeguroDAO();
        listaDeSegurosVida = getListaDeSegurosVida();
    }

    private List<DespesaComSeguroDeVida> getListaDeSegurosVida() {
        return seguroDao.listaSegurosVidaValidos();
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
        BuscaDeDadosSemResultado.substituiRecyclerPorAviso(listaDeSegurosVida.size(), recyclerView, avisoDeListaVaziaLayout);
    }

    private void inicializaCampos() {
        avisoDeListaVaziaLayout = binding.fragSegurosVazio;
        recyclerView = binding.fragSegurosRecycler;
    }

    private void configuraRecycler() {
        adapter = new SeguroVidaAdapter(this, listaDeSegurosVida);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(seguro -> {
            NavController controlador = Navigation.findNavController(requireView());
            NavDirections direction = SeguroVidaFragmentDirections.actionNavSeguroOutrosToSeguroResumoFragment(((DespesaComSeguro) seguro).getId());
            controlador.navigate(direction);
        });
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Seguros");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
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
                        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
       posicao = adapter.getPosicao();
        DespesaComSeguroDeVida seguro = listaDeSegurosVida.get(posicao);

        switch (item.getItemId()){
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
        listaDeSegurosVida = getListaDeSegurosVida();
    }
}