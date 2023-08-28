package br.com.transporte.AppGhn.ui.fragment.home;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEMIREBOQUE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.SemiReboqueDAO;
import br.com.transporte.AppGhn.databinding.FragmentFrotaBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.CavaloAdapter;
import br.com.transporte.AppGhn.ui.adapter.FrotaSrAdapter;
import br.com.transporte.AppGhn.ui.dialog.DefineMotorista;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FrotaFragment extends Fragment implements DefineMotorista.DefineMotoristaCallback {
    public static final String FROTA = "Frota";
    private FragmentFrotaBinding binding;
    private CavaloDAO cavaloDao;
    private CavaloAdapter adapter;
    private List<Cavalo> listaDeCavalos;
    private RecyclerView recyclerCavalos;
    private SemiReboqueDAO srDao;
    private List<SemiReboque> listaDeReboques;
    private RecyclerView recyclerSr;
    private Button btnNovoCavalo;
    private boolean janelaFechada = true;
    private FrotaSrAdapter adapterSr;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                        Toast.makeText(requireContext(), REGISTRO_CRIADO, Toast.LENGTH_SHORT).show();
                        atualizaAdapter();
                        break;
                    case RESULT_DELETE:
                        Toast.makeText(requireContext(), REGISTRO_APAGADO, Toast.LENGTH_SHORT).show();
                        atualizaAdapter();
                        break;
                    case RESULT_EDIT:
                        Toast.makeText(requireContext(), REGISTRO_EDITADO, Toast.LENGTH_SHORT).show();
                        atualizaAdapter();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;
                }
            });

    private void atualizaAdapter() {
        adapter.atualiza(getListaDeCavalos());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cavaloDao = new CavaloDAO();
        srDao = new SemiReboqueDAO();
        listaDeCavalos = getListaDeCavalos();
        listaDeReboques = getListaDeReboques();
    }

    private List<SemiReboque> getListaDeReboques() {
        return srDao.listaTodos();
    }

    private List<Cavalo> getListaDeCavalos() {
        return cavaloDao.listaValidos();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFrotaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraToolbar();
        configuraRecyclerCavalo();
        configuraRecyclerSr();
        configuraListeners();
        configuraBtnNovoCavalo();
        configuraAnimacaoSubListaSr();
    }

    private void inicializaCamposDaView() {
        recyclerCavalos = binding.fragFrotaRecycler;
        recyclerSr = binding.fragFrotaSrRecycler;
        btnNovoCavalo = binding.fragFrotaCadastraNovo;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        Cavalo cavalo = listaDeCavalos.get(posicao);

        if (item.getItemId() == R.id.defineMotorista) {
            DefineMotorista defineMotorista = new DefineMotorista(this.getContext(), cavalo);
            defineMotorista.dialogDefineMotorista();
            defineMotorista.setDefineMotoristaCallback(this);
        }
        return super.onContextItemSelected(item);
    }

    private void configuraBtnNovoCavalo() {
        btnNovoCavalo.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CAVALO);
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraRecyclerCavalo() {
        adapter = new CavaloAdapter(this, listaDeCavalos);
        recyclerCavalos.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerCavalos.setLayoutManager(layoutManager);
    }

    private void configuraRecyclerSr() {
        adapterSr = new FrotaSrAdapter(this, listaDeReboques);
        recyclerSr.setAdapter(adapterSr);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.requireContext(), RecyclerView.HORIZONTAL, false);
        recyclerSr.setLayoutManager(layoutManager);
    }

    private void configuraListeners() {
        adapter.setOnItemClickListener(new CavaloAdapter.OnItemClickListener() {
            @Override
            public void onEditaCavaloClick(int idCavalo) {
                Intent intent = new Intent(getContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_CAVALO);
                intent.putExtra(CHAVE_ID, idCavalo);
                activityResultLauncher.launch(intent);
            }

            @Override
            public void onNovoSrClick(int idCavalo) {
                Intent intent = new Intent(getContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_SEMIREBOQUE);
                intent.putExtra(CHAVE_ID_CAVALO, idCavalo);
                activityResultLauncher.launch(intent);
            }

            @Override
            public void onEditaSrClick(int idSr, int idCavalo) {
                Intent intent = new Intent(getContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_SEMIREBOQUE);
                intent.putExtra(CHAVE_ID, idSr);
                intent.putExtra(CHAVE_ID_CAVALO, idCavalo);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void configuraAnimacaoSubListaSr() {
        ImageView imgIcSetaSr = binding.recItemSrSeta;
        Animation animationAbertura = AnimationUtils.loadAnimation(requireContext(), R.anim.seta_abertura);
        Animation animationFechamento = AnimationUtils.loadAnimation(requireContext(), R.anim.seta_fechamento);

        imgIcSetaSr.setOnClickListener(v -> {
            if (janelaFechada) {
                imgIcSetaSr.startAnimation(animationAbertura);
                recyclerSr.setVisibility(View.VISIBLE);
                janelaFechada = false;
            } else {
                imgIcSetaSr.startAnimation(animationFechamento);
                recyclerSr.setVisibility(View.GONE);
                janelaFechada = true;
            }

        });

        if (recyclerSr.getVisibility() == View.VISIBLE) {
            imgIcSetaSr.startAnimation(animationAbertura);
        }

    }

    @Override
    public void quandoFalha(String txt) {
        MensagemUtil.snackBar(getView(), txt);
    }

    @Override
    public void quandoSucesso() {
        adapter.atualiza(cavaloDao.listaTodos());
    }

    void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(FROTA);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(new MenuProvider() {
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
                    btnNovoCavalo.setVisibility(View.GONE);
                });

                search.setOnCloseListener(() -> {
                    logout.setVisible(true);
                    Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
                    btnNovoCavalo.setVisibility(View.VISIBLE);
                    return false;
                });

                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        LinearLayout buscaVazia = binding.fragFrotaVazio;

                        List<Cavalo> listaFiltrada = buscaPorCavalosNoBd(newText);
                        List<SemiReboque> listaSrFiltrada = buscaPorSrNoBd(newText);


                        if (listaFiltrada.isEmpty() && listaSrFiltrada.isEmpty()) {
                            buscaVazia.setVisibility(View.VISIBLE);
                            recyclerCavalos.setVisibility(View.INVISIBLE);
                        } else {
                            if (buscaVazia.getVisibility() == View.VISIBLE) {
                                buscaVazia.setVisibility(View.INVISIBLE);
                                recyclerCavalos.setVisibility(View.VISIBLE);
                            }
                            adapter.atualiza(listaFiltrada);
                        }

                        if (listaSrFiltrada.isEmpty()) {
                            recyclerSr.setVisibility(View.GONE);
                        } else {
                            if (recyclerSr.getVisibility() == View.GONE && !janelaFechada) {
                                recyclerSr.setVisibility(View.VISIBLE);
                            }
                            adapterSr.atualiza(listaSrFiltrada);
                        }
                        return false;
                    }

                    @NonNull
                    private List<Cavalo> buscaPorCavalosNoBd(String newText) {
                        List<Cavalo> listaFiltrada = new ArrayList<>();
                        for (Cavalo c : getListaDeCavalos()) {
                            if (c.getPlaca().toLowerCase().contains(newText.toLowerCase())) {
                                listaFiltrada.add(c);
                            }
                        }
                        return listaFiltrada;
                    }

                    @NonNull
                    private List<SemiReboque> buscaPorSrNoBd(String newText) {
                        List<SemiReboque> listaSrFiltrada = new ArrayList<>();
                        for (SemiReboque s : getListaDeReboques()) {
                            if (s.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                                listaSrFiltrada.add(s);
                            }
                        }
                        return listaSrFiltrada;
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
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
}