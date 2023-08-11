package br.com.transporte.AppGhn.ui.fragment;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentSelecionaCavaloBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.enums.TipoSelecionaCavalo;
import br.com.transporte.AppGhn.ui.activity.AreaMotoristaActivity;
import br.com.transporte.AppGhn.ui.adapter.SelecionaCavaloAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;

public class SelecionaCavalo extends Fragment implements MenuProvider {
    private FragmentSelecionaCavaloBinding binding;
    private SelecionaCavaloAdapter adapter;
    private LinearLayout buscaVaziaLayout;
    private CavaloDAO cavaloDao;
    private NavController controlador;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelecionaCavaloBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        cavaloDao = new CavaloDAO();
        controlador = Navigation.findNavController(view);
        TipoSelecionaCavalo requisicao = SelecionaCavaloArgs.fromBundle(getArguments()).getTipoDirection();
        configuraToolbar();
        configuraRecycler();
        configuraNavegacao(requisicao);

    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Cavalos");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

    }

    private void inicializaCamposDaView() {
        recyclerView = binding.recycler;
        buscaVaziaLayout = binding.buscaVazia;
    }

    private void configuraNavegacao(TipoSelecionaCavalo requisicao) {
        adapter.setOnItemClickListener(cavalo -> {
            switch (requisicao){
                case MANUTENCAO:
                    NavDirections direction = SelecionaCavaloDirections.actionNavSelecionaCavaloFragmentToNavManutencaoDetalhes(((Cavalo) cavalo).getId());
                    controlador.navigate(direction);
                    break;

                case AREA_MOTORISTA:
                    Intent intent = new Intent(this.requireContext(), AreaMotoristaActivity.class);
                    intent.putExtra(CHAVE_ID_CAVALO, ((Cavalo) cavalo).getId());
                    startActivity(intent);
                    break;
            }
        });
    }

    private void configuraRecycler() {
        List<Cavalo> listaRecycler = cavaloDao.listaTodos();

        adapter = new SelecionaCavaloAdapter(this, listaRecycler);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        MenuItem busca = menu.findItem(R.id.menu_padrao_search);
        SearchView searchView = (SearchView) busca.getActionView();

        searchView.setOnSearchClickListener(v -> {
            logout.setVisible(false);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        });

        searchView.setOnCloseListener(() -> {
            logout.setVisible(true);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Cavalo> listaFiltrada = new ArrayList<>();

                for (Cavalo c : cavaloDao.listaTodos()) {
                    if (c.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        listaFiltrada.add(c);
                    }
                }

                if (listaFiltrada.isEmpty()) {
                    buscaVaziaLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    if (buscaVaziaLayout.getVisibility() == View.VISIBLE) {
                        buscaVaziaLayout.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    adapter.atualiza(listaFiltrada);
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show();
                break;


            case android.R.id.home:
                controlador.popBackStack();
                break;

        }
        return false;
    }
}