package br.com.transporte.AppGhn.ui.fragment.certificados;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.certificado.CertificadosActivity.ACT_LISTENER;
import static br.com.transporte.AppGhn.ui.activity.certificado.CertificadosActivity.SEARCH_CLICK;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
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
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentCertificadosDiretosBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.adapter.CertificadoAdapter;
import br.com.transporte.AppGhn.ui.fragment.certificados.helpers.CertificadosListenersResult;
import br.com.transporte.AppGhn.ui.viewmodel.CertificadoActViewModel;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class  CertificadosDiretosFragment extends Fragment {
    private FragmentCertificadosDiretosBinding binding;
    private RecyclerView recycler;
    private CertificadoActViewModel viewModel;
    private LinearLayout alertaLayout;
    private CertificadoAdapter adapter;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CertificadoActViewModel.class);
    }

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
        configuraMenuProviderHelper();
        ExibirResultadoDaBusca_sucessoOuAlerta
                .configura(
                        viewModel.cavalosArmazenados.size(),
                        alertaLayout, recycler, VIEW_INVISIBLE
                );

    }

    private void inicializaCampos() {
        alertaLayout = binding.alertaLayout.alerta;
        recycler = binding.recItemCertificadosRecycler;
    }

    private void configuraRecycler(View view) {
        adapter = new CertificadoAdapter(
                this,
                viewModel.cavalosArmazenados,
                viewModel.certificadosArmazenados
        );
        recycler.setAdapter(adapter);

        final LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recycler.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(cavaloId -> {
            final NavController controlador = Navigation.findNavController(view);
            final NavDirections direction =
                    CertificadosDiretosFragmentDirections
                            .actionNavCertificadosDiretosToNavCertificadosDiretosDetalhes(cavaloId);
            controlador.navigate(direction);
        });
    }

    private void configuraMenuProviderHelper() {
        final CertificadoDiretoMenuProviderHelper menuProviderHelper =
                new CertificadoDiretoMenuProviderHelper(new ArrayList<>(viewModel.cavalosArmazenados));
        requireActivity().addMenuProvider(menuProviderHelper, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setCallback(new CertificadoDiretoMenuProviderHelper.CertificadoDiretoMenuProviderCallback() {
            @Override
            public void onSearchCLick() {
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
                CertificadosListenersResult.notificaSearchClicked(ACT_LISTENER, SEARCH_CLICK, getParentFragmentManager(), true);
            }

            @Override
            public void onSearchClear() {
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
                CertificadosListenersResult.notificaSearchClicked(ACT_LISTENER, SEARCH_CLICK, getParentFragmentManager(), false);
            }

            @Override
            public void realizaBusca(List<Cavalo> dataSetSearch) {
                adapter.atualiza(dataSetSearch);
                ExibirResultadoDaBusca_sucessoOuAlerta
                        .configura(
                                viewModel.cavalosArmazenados.size(),
                                alertaLayout, recycler, VIEW_INVISIBLE
                        );
            }

            @Override
            public void onLogoutClick() {
                MensagemUtil.toast(requireContext(), LOGOUT);
            }

            @Override
            public void onHomeClick() {
                requireActivity().finish();
            }
        });


    }

    public void solicitaAtualizacaoAdapter() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class CertificadoDiretoMenuProviderHelper implements MenuProvider {
    private final List<Cavalo> dataSet;
    private CertificadoDiretoMenuProviderCallback callback;

    public void setCallback(CertificadoDiretoMenuProviderCallback callback) {
        this.callback = callback;
    }

    public CertificadoDiretoMenuProviderHelper(List<Cavalo> dataSet) {
        this.dataSet = dataSet;
    }

    public interface CertificadoDiretoMenuProviderCallback {
        void onSearchCLick();

        void onSearchClear();

        void realizaBusca(final List<Cavalo> dataSetSearch);

        void onLogoutClick();

        void onHomeClick();
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.removeItem(R.id.menu_padrao_editar);
        final MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        final MenuItem busca = menu.findItem(R.id.menu_padrao_search);
        final SearchView searchView = (SearchView) busca.getActionView();

        Objects.requireNonNull(searchView).setOnSearchClickListener(v -> {
            logout.setVisible(false);
            callback.onSearchCLick();
        });

        searchView.setOnCloseListener(() -> {
            logout.setVisible(true);
            callback.onSearchClear();
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Cavalo> listaFiltrada = new ArrayList<>();
                for (Cavalo c : dataSet) {
                    if (c.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        listaFiltrada.add(c);
                    }
                }
                callback.realizaBusca(listaFiltrada);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                callback.onLogoutClick();
                break;

            case android.R.id.home:
                callback.onHomeClick();
                break;
        }
        return false;
    }
}
