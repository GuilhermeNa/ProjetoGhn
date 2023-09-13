package br.com.transporte.AppGhn.ui.fragment.home.frota;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_GONE;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.databinding.FragmentFrotaBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.tasks.cavalo.BuscaTodosCavalosTask;
import br.com.transporte.AppGhn.tasks.BuscaTodosMotoristasTask;
import br.com.transporte.AppGhn.tasks.reboque.BuscaTodosReboquesTask;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.FrotaSrAdapter;
import br.com.transporte.AppGhn.ui.fragment.home.frota.adapters.CavaloAdapter;
import br.com.transporte.AppGhn.ui.fragment.home.frota.dialog.DefineMotorista;
import br.com.transporte.AppGhn.ui.fragment.home.frota.helpers.FrotaMenuProviderHelper;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class FrotaFragment extends Fragment {
    public static final String FROTA = "Frota";
    // --------- View
    private ConstraintLayout headerRecyclerReboque;
    private FragmentFrotaBinding binding;
    private RecyclerView recyclerCavalos;
    private Button btnNovoCavalo;
    private RecyclerView recyclerReboques;
    private LinearLayout buscaVazia;
    // --------- DataBase
    private RoomCavaloDao cavaloDao;
    private RoomSemiReboqueDao reboqueDao;
    // --------- Listas
    private List<Cavalo> dataSetCavalo;
    private List<SemiReboque> dataSetReboque;
    private List<Motorista> dataSetMotorista;

    // --------- Variaveis de Fragment
    private boolean janelaFechada = true;
    private ExecutorService executorService;
    private Handler handler;
    private FrotaMenuProviderHelper menuProviderHelper;
    private CavaloAdapter adapterCavalo;
    private FrotaSrAdapter adapterReboques;
    private RoomMotoristaDao motoristaDao;
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
        atualizaDataSetCavalo(executorService, handler);
        atualizaDataSetReboque(executorService, handler);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(this.dataSetCavalo.size(), buscaVazia, recyclerCavalos, VIEW_INVISIBLE);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(this.dataSetReboque.size(), null, recyclerReboques, VIEW_GONE);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(this.dataSetReboque.size(), null, headerRecyclerReboque, VIEW_GONE);
    }

    protected List<Cavalo> getDataSetCavalo() {
        return new ArrayList<>(this.dataSetCavalo);
    }

    protected List<SemiReboque> getDataSetReboque() {
        return new ArrayList<>(this.dataSetReboque);
    }

    protected List<Motorista> getDataSetMotorista(){
        return new ArrayList<>(this.dataSetMotorista);
    }

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        inicializaThreadSecundaria();
        atualizaDataSetMotorista(executorService, handler);
        atualizaDataSetReboque(executorService, handler);
        atualizaDataSetCavalo(executorService, handler);
    }

    private void inicializaThreadSecundaria() {
        final GhnApplication application = new GhnApplication();
        executorService = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    private void inicializaDataBase() {
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        cavaloDao = dataBase.getRoomCavaloDao();
        reboqueDao = dataBase.getRoomReboqueDao();
        motoristaDao = dataBase.getRoomMotoristaDao();
    }

    private void atualizaDataSetMotorista(ExecutorService executorService, Handler handler) {
        BuscaTodosMotoristasTask buscaMotoristaTask = new BuscaTodosMotoristasTask(executorService, handler);
        buscaMotoristaTask.solicitaBusca(motoristaDao, todosMotoristas -> {
            if (dataSetMotorista == null) dataSetMotorista = new ArrayList<>();
            this.dataSetMotorista.clear();
            this.dataSetMotorista.addAll(todosMotoristas);
            adapterCavalo.atualizaDataSet_motorista(getDataSetMotorista());
        });
    }

    protected void atualizaDataSetReboque(ExecutorService executorService, Handler handler) {
        final BuscaTodosReboquesTask reboqueTask = new BuscaTodosReboquesTask(executorService, handler);
        reboqueTask.solicitaBusca(reboqueDao, todosReboques -> {
            if (dataSetReboque == null) dataSetReboque = new ArrayList<>();
            this.dataSetReboque.clear();
            this.dataSetReboque.addAll(todosReboques);
            menuProviderHelper.atualizaReboques(getDataSetReboque());
            adapterReboques.atualiza(getDataSetReboque());
            adapterCavalo.atualizaDataSet_reboque(getDataSetReboque());
        });
    }

    protected void atualizaDataSetCavalo(ExecutorService executorService, Handler handler) {
        final BuscaTodosCavalosTask cavalosTask = new BuscaTodosCavalosTask(executorService, handler);
        cavalosTask.solicitaBusca(cavaloDao, todosCavalos -> {
            if (dataSetCavalo == null) dataSetCavalo = new ArrayList<>();
            this.dataSetCavalo.clear();
            this.dataSetCavalo.addAll(todosCavalos);
            menuProviderHelper.atualizaCavalos(getDataSetCavalo());
            adapterCavalo.atualiza(getDataSetCavalo());
        });
    }

    //----------------------------------------------------------------------------------------------
    //                                          On Create View                                    ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFrotaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          On View Created                                   ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraToolbar();
        configuraMenuProviderHelper();
        configuraRecyclerCavalo();
        configuraRecyclerReboque();
        configuraClickListenerParaExibirSemiReboques();
        configuraBtnNovoCavalo();
    }

    private void inicializaCamposDaView() {
        buscaVazia = binding.fragFrotaVazio;
        recyclerCavalos = binding.fragFrotaRecycler;
        recyclerReboques = binding.fragFrotaSrRecycler;
        headerRecyclerReboque = binding.layoutExibeReboques;
        btnNovoCavalo = binding.fragFrotaCadastraNovo;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(FROTA);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
    }

    private void configuraMenuProviderHelper() {
        menuProviderHelper = new FrotaMenuProviderHelper();
        requireActivity().addMenuProvider(menuProviderHelper, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setCallBack(new FrotaMenuProviderHelper.MenuProviderCallback() {
            @Override
            public void realizaBusca(List<Cavalo> dataSet_searchView_cavalo, List<SemiReboque> dataSet_searchView_semiReboque) {
                atualizaAdaptersAposBuscarNaSearchView(dataSet_searchView_cavalo, dataSet_searchView_semiReboque);
            }

            @Override
            public void searchViewAtivada() {
                boolean searchViewAtiva = false;
                configuraInteracaoAoAcessarSearchView(searchViewAtiva);
            }

            @Override
            public void searchViewDesativada() {
                boolean searchViewInativa = true;
                configuraInteracaoAoAcessarSearchView(searchViewInativa);
            }

            @Override
            public void onLogoutClick() {
                MensagemUtil.toast(requireContext(), LOGOUT);
            }

            @Override
            public void onHomeClick() {
                NavController controlador = Navigation.findNavController(requireView());
                controlador.popBackStack();
            }
        });

    }

    private void configuraRecyclerReboque() {
        adapterReboques = new FrotaSrAdapter(this, new ArrayList<>());
        recyclerReboques.setAdapter(adapterReboques);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        recyclerReboques.setLayoutManager(layoutManager);
    }

    private void configuraRecyclerCavalo() {
        adapterCavalo = new CavaloAdapter(this);
        recyclerCavalos.setAdapter(adapterCavalo);
        adapterCavalo.setOnItemClickListener(new CavaloAdapter.OnItemClickListener() {
            @Override
            public void onCLickEditaCavalo(Long cavaloId) {
                navegaParaFormularioCavalo(cavaloId);
            }

            @Override
            public void onClickAdicionaReboque(Long cavaloId) {
                navegaParaFormularioReboque(cavaloId, null);
            }

            @Override
            public void onClickEditaReboque(Long reboqueId, Long cavaloId) {
                navegaParaFormularioReboque(cavaloId, reboqueId);
            }
        });
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
        posicao = adapterCavalo.getPosicao();
        Cavalo cavalo = getDataSetCavalo().get(posicao);

        if (item.getItemId() == R.id.defineMotorista) {
            DefineMotorista dialogDefineMotorista = new DefineMotorista(getDataSetMotorista(), this.getContext(), cavalo);
            dialogDefineMotorista.configuraDialog();
            dialogDefineMotorista.setDefineMotoristaCallback(new DefineMotorista.DefineMotoristaCallback() {
                @Override
                public void quandoFalha(String txt) {
                    MensagemUtil.snackBar(getView(), txt);
                }

                @Override
                public void quandoSucesso() {
                    atualizaDataSetCavalo(executorService, handler);
                }
            });
        }
        return super.onContextItemSelected(item);
    }

//---------------------

    protected void atualizaAdaptersAposBuscarNaSearchView(@NonNull List<Cavalo> dataSet_searchView_cavalos, List<SemiReboque> dataSet_SearchView_reboques) {
        adapterCavalo.atualiza(dataSet_searchView_cavalos);
        adapterReboques.atualiza(dataSet_SearchView_reboques);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet_searchView_cavalos.size(), buscaVazia, recyclerCavalos, VIEW_INVISIBLE);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet_SearchView_reboques.size(), null, recyclerReboques, VIEW_GONE);
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

    protected void navegaParaFormularioCavalo(Long cavaloId) {
        Intent intent = new Intent(getContext(), FormulariosActivity.class);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_CAVALO);
        intent.putExtra(CHAVE_ID, cavaloId);
        activityResultLauncher.launch(intent);
    }

    protected void navegaParaFormularioReboque(Long cavaloId, Long reboqueId) {
        Intent intent = new Intent(getContext(), FormulariosActivity.class);
        if (reboqueId != null) intent.putExtra(CHAVE_ID, reboqueId);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_SEMIREBOQUE);
        intent.putExtra(CHAVE_ID_CAVALO, cavaloId);
        activityResultLauncher.launch(intent);
    }
}

//----------------------------------------------------------------------------------------------
//                                        Recycler Reboques                                   ||
//----------------------------------------------------------------------------------------------

