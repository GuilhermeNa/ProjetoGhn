package br.com.transporte.AppGhn.ui.fragment.home.frota;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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
import static br.com.transporte.AppGhn.ui.fragment.home.frota.FrotaFragment.FROTA;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_GONE;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.databinding.FragmentFrotaBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.FrotaSrAdapter;
import br.com.transporte.AppGhn.ui.fragment.home.frota.adapters.CavaloAdapter;
import br.com.transporte.AppGhn.ui.fragment.home.frota.dialog.DefineMotorista;
import br.com.transporte.AppGhn.ui.fragment.home.frota.helpers.FrotaMenuProviderHelper;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FrotaFragment extends Fragment {
    public static final String FROTA = "Frota";
    // --------- Extensoes de Fragment
    private _Toolbar toolbarExt;
    private _RecyclerReboques recyclerReboqueExt;
    private _RecyclerCavalos recyclerCavaloExt;
    // --------- View
    private ConstraintLayout headerRecyclerReboque;
    private FragmentFrotaBinding binding;
    private RecyclerView recyclerCavalos;
    private Toolbar toolbar;
    private Button btnNovoCavalo;
    private RecyclerView recyclerReboques;
    private LinearLayout buscaVazia;
    // --------- DataBase
    private RoomCavaloDao cavaloDao;
    private RoomSemiReboqueDao reboqueDao;
    // --------- Listas
    private List<Cavalo> dataSetCavalo;
    private List<SemiReboque> dataSetReboque;
    // --------- Variaveis de Fragment
    private boolean janelaFechada = true;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                        Toast.makeText(requireContext(), REGISTRO_CRIADO, Toast.LENGTH_SHORT).show();
                        atualizaAposResultLauncher();
                        break;
                    case RESULT_DELETE:
                        Toast.makeText(requireContext(), REGISTRO_APAGADO, Toast.LENGTH_SHORT).show();
                        atualizaAposResultLauncher();
                        break;
                    case RESULT_EDIT:
                        Toast.makeText(requireContext(), REGISTRO_EDITADO, Toast.LENGTH_SHORT).show();
                        atualizaAposResultLauncher();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;
                }
            });

    private void atualizaAposResultLauncher() {
        atualizaDataSetCavalo();
        atualizaDataSetReboque();
        recyclerCavaloExt.atualizaAdapter(getDataSetCavalo());
        toolbarExt.atualizaDataDoMenuProvider(getDataSetCavalo(), getDataSetReboque());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(this.dataSetCavalo.size(), buscaVazia, recyclerCavalos, VIEW_INVISIBLE);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(this.dataSetReboque.size(), null, recyclerReboques, VIEW_GONE);
    }

    protected void atualizaDataSetCavalo() {
        if (dataSetCavalo == null) dataSetCavalo = new ArrayList<>();
        this.dataSetCavalo.clear();
        this.dataSetCavalo.addAll(cavaloDao.todos());
    }

    protected void atualizaDataSetReboque() {
        if (dataSetReboque == null) dataSetReboque = new ArrayList<>();
        this.dataSetReboque.clear();
        this.dataSetReboque.addAll(reboqueDao.todos());
    }

    protected List<Cavalo> getDataSetCavalo() {
        return new ArrayList<>(this.dataSetCavalo);
    }

    protected List<SemiReboque> getDataSetReboque() {
        return new ArrayList<>(this.dataSetReboque);
    }

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cavaloDao = GhnDataBase.getInstance(this.requireContext()).getRoomCavaloDao();
        reboqueDao = GhnDataBase.getInstance(this.requireContext()).getRoomReboqueDao();
        atualizaDataSetCavalo();
        atualizaDataSetReboque();
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
        configuraRecyclerReboque();
        configuraClickListenerParaExibirSemiReboques();
        configuraBtnNovoCavalo();
    }

    private void configuraRecyclerReboque() {
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSetReboque.size(), null, recyclerReboques, VIEW_GONE);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSetReboque.size(), null, headerRecyclerReboque, VIEW_GONE);
        recyclerReboqueExt = new _RecyclerReboques(this);
        recyclerReboqueExt.configura(recyclerReboques);
    }

    private void inicializaCamposDaView() {
        toolbar = binding.toolbar;
        buscaVazia = binding.fragFrotaVazio;
        recyclerCavalos = binding.fragFrotaRecycler;
        recyclerReboques = binding.fragFrotaSrRecycler;
        headerRecyclerReboque = binding.layoutExibeReboques;
        btnNovoCavalo = binding.fragFrotaCadastraNovo;
    }

    private void configuraToolbar() {
        toolbarExt = new _Toolbar(this);
        toolbarExt.configura(toolbar, getDataSetCavalo(), getDataSetReboque());
    }

    private void configuraRecyclerCavalo() {
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSetCavalo.size(), buscaVazia, recyclerCavalos, VIEW_INVISIBLE);
        recyclerCavaloExt = new _RecyclerCavalos(this);
        recyclerCavaloExt.configura(recyclerCavalos);
    }

    private void configuraBtnNovoCavalo() {
        btnNovoCavalo.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CAVALO);
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraClickListenerParaExibirSemiReboques() {
        ImageView imgIcSetaSr = binding.recItemSrSeta;
        Animation animationAbertura = AnimationUtils.loadAnimation(requireContext(), R.anim.seta_abertura);
        Animation animationFechamento = AnimationUtils.loadAnimation(requireContext(), R.anim.seta_fechamento);

        imgIcSetaSr.setOnClickListener(v -> {
            if (janelaFechada) {
                imgIcSetaSr.startAnimation(animationAbertura);
                recyclerReboques.setVisibility(VISIBLE);
                janelaFechada = false;
            } else {
                imgIcSetaSr.startAnimation(animationFechamento);
                recyclerReboques.setVisibility(GONE);
                janelaFechada = true;
            }
        });
        if (recyclerReboques.getVisibility() == VISIBLE) {
            imgIcSetaSr.startAnimation(animationAbertura);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = recyclerCavaloExt.getAdapterPos();
        Cavalo cavalo = getDataSetCavalo().get(posicao);

        if (item.getItemId() == R.id.defineMotorista) {
            DefineMotorista defineMotorista = new DefineMotorista(this.getContext(), cavalo);
            defineMotorista.configuraDialog();
            defineMotorista.setDefineMotoristaCallback(new DefineMotorista.DefineMotoristaCallback() {
                @Override
                public void quandoFalha(String txt) {
                    MensagemUtil.snackBar(getView(), txt);
                }

                @Override
                public void quandoSucesso() {
                    atualizaDataSetCavalo();
                    recyclerCavaloExt.atualizaAdapter(getDataSetCavalo());
                }
            });
        }
        return super.onContextItemSelected(item);
    }

//---------------------

    protected void atualizaAdaptersAposBuscarNaSearchView(@NonNull List<Cavalo> dataSet_searchView_cavalos, List<SemiReboque> dataSet_SearchView_reboques) {
        recyclerCavaloExt.atualizaAdapter(dataSet_searchView_cavalos);
        recyclerReboqueExt.atualizaAdapter(dataSet_SearchView_reboques);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet_searchView_cavalos.size(), buscaVazia, recyclerCavalos, VIEW_INVISIBLE);
    }

    protected void configuraInteracaoAoAcessarSearchView(boolean visivel) {
        if (visivel) {
            btnNovoCavalo.setVisibility(VISIBLE);
            AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_in_bottom, btnNovoCavalo);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        } else {
            btnNovoCavalo.setVisibility(View.GONE);
            AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_out_bottom, btnNovoCavalo);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

    }

    protected void navegaParaFormularioCavalo(Integer cavaloId) {
        Intent intent = new Intent(getContext(), FormulariosActivity.class);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_CAVALO);
        intent.putExtra(CHAVE_ID, cavaloId);
        activityResultLauncher.launch(intent);
    }

    protected void navegaParaFormularioReboque(Integer cavaloId, Integer reboqueId) {
        Intent intent = new Intent(getContext(), FormulariosActivity.class);
        if (reboqueId != null) intent.putExtra(CHAVE_ID, reboqueId);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_SEMIREBOQUE);
        intent.putExtra(CHAVE_ID_CAVALO, cavaloId);
        activityResultLauncher.launch(intent);
    }
}

//----------------------------------------------------------------------------------------------
//                                           Toolbar                                          ||
//----------------------------------------------------------------------------------------------

class _Toolbar {
    private final FrotaFragment fragment;
    private FrotaMenuProviderHelper menuProviderHelper;

    _Toolbar(FrotaFragment frotaFragment) {
        this.fragment = frotaFragment;
    }

    //----------------------------------

    protected void configura(
            Toolbar toolbar,
            List<Cavalo> copiaDataSetCavalos,
            List<SemiReboque> copiaDataSetReboques
    ) {
        configuraToolbar(toolbar);
        configuraMenuProvider(copiaDataSetCavalos, copiaDataSetReboques);
    }

    private void configuraToolbar(Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) fragment.requireActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);
            appCompatActivity.getSupportActionBar().setTitle(FROTA);
            appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
    }

    private void configuraMenuProvider(List<Cavalo> copiaDataSetCavalos, List<SemiReboque> copiaDataSetReboques) {
        menuProviderHelper = new FrotaMenuProviderHelper(fragment, copiaDataSetCavalos, copiaDataSetReboques);
        fragment.requireActivity().addMenuProvider(menuProviderHelper, fragment.getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setMenuProviderCallback(new FrotaMenuProviderHelper.MenuProviderCallback() {
            // Callback do menuProviderHelper:
            // Interage com o Fragment para atualizar a Ui enquanto usuario interage com a SearchView
            @Override
            public void realizaBusca(List<Cavalo> dataSet_searchView_cavalo, List<SemiReboque> dataSet_searchView_semiReboque) {
                fragment.atualizaAdaptersAposBuscarNaSearchView(dataSet_searchView_cavalo, dataSet_searchView_semiReboque);
            }

            @Override
            public void searchViewAtivada() {
                boolean searchViewAtiva = false;
                fragment.configuraInteracaoAoAcessarSearchView(searchViewAtiva);
            }

            @Override
            public void searchViewDesativada() {
                boolean searchViewInativa = true;
                fragment.configuraInteracaoAoAcessarSearchView(searchViewInativa);
            }
        });
    }

    // ------------------------------------- Publicos ----------------------------------------------

    public void atualizaDataDoMenuProvider(List<Cavalo> copiaDataSetCavalo, List<SemiReboque> copiaDataSetReboque) {
        menuProviderHelper.atualizaCavalos(copiaDataSetCavalo);
        menuProviderHelper.atualizaReboques(copiaDataSetReboque);
    }
}

//----------------------------------------------------------------------------------------------
//                                         Recycler Cavalos                                   ||
//----------------------------------------------------------------------------------------------

class _RecyclerCavalos extends FrotaFragment {
    private final FrotaFragment fragment;
    private CavaloAdapter adapter;

    _RecyclerCavalos(FrotaFragment fragment) {
        this.fragment = fragment;
    }

    //----------------------------------

    public void configura(@NonNull RecyclerView recycler) {
        adapter = new CavaloAdapter(fragment, fragment.getDataSetCavalo());
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new CavaloAdapter.OnItemClickListener() {
            // Callback do cavaloAdapter
            // Configura os comportamentos ao interagir com os itens da RecyclerView
            @Override
            public void onCLickEditaCavalo(Integer cavaloId) {
                fragment.navegaParaFormularioCavalo(cavaloId);
            }

            @Override
            public void onClickAdicionaReboque(Integer cavaloId) {
                fragment.navegaParaFormularioReboque(cavaloId, null);
            }

            @Override
            public void onClickEditaReboque(Integer reboqueId, Integer cavaloId) {
                fragment.navegaParaFormularioReboque(cavaloId, reboqueId);
            }
        });
    }

    //----------------------------------

    protected void atualizaAdapter(List<Cavalo> listaDeCavalos) {
        adapter.atualiza(listaDeCavalos);
    }

    public int getAdapterPos() {
        return adapter.getPosicao();
    }
}

//----------------------------------------------------------------------------------------------
//                                        Recycler Reboques                                   ||
//----------------------------------------------------------------------------------------------

class _RecyclerReboques {
    private final FrotaFragment fragment;
    private FrotaSrAdapter adapter;

    _RecyclerReboques(FrotaFragment fragment) {
        this.fragment = fragment;
    }

    //----------------------------------

    protected void configura(@NonNull RecyclerView recycler) {
        adapter = new FrotaSrAdapter(fragment, fragment.getDataSetReboque());
        recycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragment.requireContext(), RecyclerView.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
    }

    protected void atualizaAdapter(List<SemiReboque> lista) {
        adapter.atualiza(lista);
    }
}
