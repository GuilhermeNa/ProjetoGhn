package br.com.transporte.AppGhn.ui.fragment.home;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFuncionariosBinding;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.MotoristasAdapter;
import br.com.transporte.AppGhn.dao.MotoristaDAO;
import br.com.transporte.AppGhn.ui.fragment.ConstantesFragment;

public class FuncionariosFragment extends Fragment implements MenuProvider {
    public static final String FUNCIONARIOS = "Funcionários";
    private FragmentFuncionariosBinding binding;
    private MotoristasAdapter adapter;
    private MotoristaDAO dao;
    private List<Motorista> listaDeFuncionarios;
    private RecyclerView recycler;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                    case RESULT_DELETE:
                    case RESULT_EDIT:
                        atualizaAdapter();
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
    );

    private void atualizaAdapter() {
        listaDeFuncionarios = getListaDeFuncionarios();
        adapter.atualiza(listaDeFuncionarios);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new MotoristaDAO();
        listaDeFuncionarios = getListaDeFuncionarios();
    }

    private List<Motorista> getListaDeFuncionarios() {
        return dao.listaTodos();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFuncionariosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();

        configuraToolbar();
        configuraRecycler();
        configuraBtnNovoFuncionario();
    }

    private void inicializaCampos() {
        recycler = binding.recycler;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(FUNCIONARIOS);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void configuraBtnNovoFuncionario() {
        Button btn = binding.btn;
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(ConstantesFragment.CHAVE_FORMULARIO, ConstantesFragment.VALOR_MOTORISTA);
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraRecycler() {
        adapter = new MotoristasAdapter(this, listaDeFuncionarios);
        recycler.setAdapter(adapter);
        LinearLayoutManager LayoutManager = new GridLayoutManager(getContext(), 2);
        recycler.setLayoutManager(LayoutManager);

        adapter.setonItemClickListener((idMotorista) -> {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(ConstantesFragment.CHAVE_ID, (Integer) idMotorista);
            intent.putExtra(ConstantesFragment.CHAVE_FORMULARIO, ConstantesFragment.VALOR_MOTORISTA);
            activityResultLauncher.launch(intent);

        });
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        MenuItem busca = menu.findItem(R.id.menu_padrao_search);
        SearchView search = (SearchView) busca.getActionView();

        Objects.requireNonNull(search).setOnSearchClickListener(v -> {
            logout.setVisible(false);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        });

        search.setOnCloseListener(() -> {
            logout.setVisible(true);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
            return false;
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Motorista> listaFiltrada = new ArrayList<>();
                LinearLayout vazio = binding.buscaVazia;

                for (Motorista m : dao.listaTodos()) {
                    if (m.getNome().toLowerCase().contains(newText.toLowerCase())) {
                        listaFiltrada.add(m);
                    }
                }
                if (listaFiltrada.isEmpty()) {
                    vazio.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.INVISIBLE);
                } else {
                    if (vazio.getVisibility() == View.VISIBLE) {
                        vazio.setVisibility(View.INVISIBLE);
                        recycler.setVisibility(View.VISIBLE);
                    }
                    adapter.setListaFiltrada(listaFiltrada);
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
                Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                NavController controlador = Navigation.findNavController(requireView());
                controlador.popBackStack();
                break;
        }
        return false;
    }

}
