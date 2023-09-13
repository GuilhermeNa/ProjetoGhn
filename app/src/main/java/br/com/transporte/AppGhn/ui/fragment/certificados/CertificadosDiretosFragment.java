package br.com.transporte.AppGhn.ui.fragment.certificados;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.annotation.SuppressLint;
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

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.databinding.FragmentCertificadosDiretosBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.adapter.CertificadoAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class CertificadosDiretosFragment extends Fragment {
    public static final String CERTIFICADOS = "Certificados";
    private List<Cavalo> dataSet;
    private FragmentCertificadosDiretosBinding binding;
    private CertificadoAdapter adapter;
    private RecyclerView recycler;
    private RoomCavaloDao cavaloDao;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        atualizaDataSet();
    }

    private void atualizaDataSet() {
        if (dataSet == null) dataSet = new ArrayList<>();
        dataSet = cavaloDao.todos();
    }

    private void inicializaDataBase() {
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        cavaloDao = dataBase.getRoomCavaloDao();
    }

    @NonNull
    @Contract(" -> new")
    private List<Cavalo> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCertificadosDiretosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                        OnViewCreated                                       ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraRecycler(view);
        configuraToolbar();
    }

    private void inicializaCampos() {
        recycler = binding.recItemCertificadosRecycler;
    }

    private void configuraRecycler(View view) {
        adapter = new CertificadoAdapter(getDataSet(), this);
        recycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recycler.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(cavaloId -> {
            NavController controlador = Navigation.findNavController(view);
            NavDirections direction = CertificadosDiretosFragmentDirections.actionNavCertificadosDiretosToNavCertificadosDiretosDetalhes(cavaloId);
            controlador.navigate(direction);
        });
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(CERTIFICADOS);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_padrao, menu);
                menu.removeItem(R.id.menu_padrao_editar);
                MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
                MenuItem busca = menu.findItem(R.id.menu_padrao_search);
                SearchView searchView = (SearchView) busca.getActionView();

                Objects.requireNonNull(searchView).setOnSearchClickListener(v -> {
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
                        LinearLayout vazio = binding.fragCertificadoVazio;
                        List<Cavalo> listaFiltrada = new ArrayList<>();

                        for (Cavalo c : getDataSet()) {
                            if (c.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                                listaFiltrada.add(c);
                            }
                        }

                        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaFiltrada.size(), vazio, recycler, VIEW_INVISIBLE);
                        adapter.atualiza(listaFiltrada);
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
                        Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

    }

    public void atualizaAdapter(String msg) {
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show();
        atualizaDataSet();
        adapter.atualiza(getDataSet());
    }

}