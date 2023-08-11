package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;

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
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentSeguroFrotaBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.SeguroFrotaAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasSeguroDAO;
import br.com.transporte.AppGhn.ui.fragment.extensions.BuscaDeDadosSemResultado;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class SeguroFrotaFragment extends Fragment {
    private List<DespesaComSeguroFrota> listaDeSegurosFrota;
    private FragmentSeguroFrotaBinding binding;
    private DespesasSeguroDAO segurosDao;
    private RecyclerView recyclerView;
    private NavDirections direction;
    private SeguroFrotaAdapter adapter;
    private CavaloDAO cavaloDao;
    private LinearLayout listaVaziaLayout;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();

    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_UPDATE:
                            atualizaAdapter("Registro renovado");
                            break;

                        case RESULT_CANCELED:
                            MensagemUtil.toast(this.requireContext(), "Nenhuma alteração realizada");
                            break;
                    }
                });
    }

    //--------------------------------------- Metodos Publicos -------------------------------------

    public void atualizaAdapter(String msg) {
        listaDeSegurosFrota = getListaDeSegurosFrota();
        adapter.atualiza(listaDeSegurosFrota);
        configuraUiBuscaSemResultados();
        MensagemUtil.toast(requireContext(), msg);
    }

    public void atualizaLista() {
        listaDeSegurosFrota = getListaDeSegurosFrota();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        segurosDao = new DespesasSeguroDAO();
        listaDeSegurosFrota = getListaDeSegurosFrota();
        cavaloDao = new CavaloDAO();
    }

    private List<DespesaComSeguroFrota> getListaDeSegurosFrota() {
        return segurosDao.listaSegurosFrota();
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
        configuraUiBuscaSemResultados();
    }

    private void inicializaCamposDaView() {
        listaVaziaLayout = binding.fragSegurosVazio;
    }

    private void configuraUiBuscaSemResultados() {
        BuscaDeDadosSemResultado.substituiRecyclerPorAviso(listaDeSegurosFrota.size(), recyclerView, listaVaziaLayout);
    }

    private void configuraRecycler(NavController controlador) {
        recyclerView = binding.fragSegurosRecycler;

        adapter = new SeguroFrotaAdapter(this, listaDeSegurosFrota);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(seguro -> {
            direction = SeguroFrotaFragmentDirections.actionNavSegurosFragmentToSeguroResumoFragment(((DespesaComSeguro) seguro).getId());
            controlador.navigate(direction);
        });
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        DespesaComSeguroFrota seguro = listaDeSegurosFrota.get(posicao);

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
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Seguros");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(new MenuProvider() {

            private SearchView search;
            private MenuItem logout;

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                configuracaoInicialDeMenu(menu, menuInflater);
                inicializaCamposDaToolbar(menu);
                configuraUiAoClicarNaLupa();
                configuraInteracaoComSearchView();
            }

            private void configuraInteracaoComSearchView() {

                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        List<DespesaComSeguroFrota> dataSet = new ArrayList<>();
                        realizaBuscaNoBancoDeDados(newText, dataSet);
                        configuraUiDuranteABusca(dataSet);
                        return false;
                    }

                    private void realizaBuscaNoBancoDeDados(String newText, List<DespesaComSeguroFrota> lista) {
                        for (DespesaComSeguroFrota d : getListaDeSegurosFrota()) {
                            String placa = cavaloDao.localizaPeloId(d.getRefCavalo()).getPlaca();
                            if (placa.toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                                lista.add(d);
                            }
                        }
                    }

                    private void configuraUiDuranteABusca(List<DespesaComSeguroFrota> lista) {
                        BuscaDeDadosSemResultado.substituiRecyclerPorAviso(lista.size(), recyclerView, listaVaziaLayout);
                        adapter.atualiza(lista);
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                });
            }

            private void configuraUiAoClicarNaLupa() {
                search.setOnSearchClickListener(v -> {
                    logout.setVisible(false);
                    Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
                });
                search.setOnCloseListener(() -> {
                    logout.setVisible(true);
                    Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
                    return false;
                });
            }

            private void inicializaCamposDaToolbar(@NonNull Menu menu) {
                logout = menu.findItem(R.id.menu_padrao_logout);
                MenuItem busca = menu.findItem(R.id.menu_padrao_search);
                search = (SearchView) busca.getActionView();
            }

            private void configuracaoInicialDeMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_padrao, menu);
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
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

    }


}