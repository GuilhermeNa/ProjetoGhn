package br.com.transporte.appGhn.ui.fragment.home.frota;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CAVALO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEMIREBOQUE;
import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFrotaBinding;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.repository.ReboqueRepository;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.FrotaSrAdapter;
import br.com.transporte.appGhn.ui.fragment.home.frota.adapters.CavaloAdapter;
import br.com.transporte.appGhn.ui.fragment.home.frota.dialog.DefineMotorista;
import br.com.transporte.appGhn.ui.fragment.home.frota.extension.FrotaFragmentViewExt;
import br.com.transporte.appGhn.ui.fragment.home.frota.helpers.FrotaMenuProviderHelper;
import br.com.transporte.appGhn.ui.viewmodel.FrotaViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.FrotaViewModelFactory;
import br.com.transporte.appGhn.util.AnimationUtil;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.appGhn.util.MensagemUtil;

public class FrotaFragment extends Fragment {
    public static final String FROTA = "Frota";
    // --------- View
    private FragmentFrotaBinding binding;
    private RecyclerView recyclerCavalos;
    private Button btnNovoCavalo;
    private RecyclerView recyclerReboques;
    private LinearLayout buscaVazia;

    // --------- Variaveis de Fragment
    private FrotaMenuProviderHelper menuProviderHelper;
    private CavaloAdapter adapterCavalo;
    private FrotaSrAdapter adapterReboques;
    private FrotaViewModel viewModel;
    private TextView headerReboques;
    private TextView headerCavalos;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                switch (codigoResultado) {
                    case RESULT_OK:
                        MensagemUtil.toast(requireContext(), REGISTRO_CRIADO);
                        break;
                    case RESULT_DELETE:
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;
                }
            });
    private NavController controlador;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        buscaData();
    }

    private void inicializaViewModel() {
        final MotoristaRepository motoristaRepository = new MotoristaRepository(requireContext());
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final ReboqueRepository reboqueRepository = new ReboqueRepository(requireContext());
        final FrotaViewModelFactory factory =
                new FrotaViewModelFactory(
                        reboqueRepository,
                        motoristaRepository,
                        cavaloRepository
                );
        final ViewModelProvider provedor = new ViewModelProvider(this, factory);
        viewModel = provedor.get(FrotaViewModel.class);
    }

    private void buscaData() {
        dataMotorista();
        dataReboque();
    }

    private void dataMotorista() {
        viewModel.buscaMotoristas().observe(this,
                resource -> {
                    final List<Motorista> listaMotorista = resource.getDado();
                    final String erro = resource.getErro();
                    if (listaMotorista != null) {
                        viewModel.setDataSetMotorista(listaMotorista);
                        adapterCavalo.atualizaDataSet_motorista(viewModel.getDataSetMotorista());
                        dataCavalo();
                    }
                    if (erro != null) {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                });
    }

    private void dataCavalo() {
        viewModel.buscaCavalos().observe(this,
                resource -> {
                    final List<Cavalo> listaCavalos = resource.getDado();
                    final String erro = resource.getErro();
                    int listaSize = 0;
                    if (listaCavalos != null) {
                        listaSize = listaCavalos.size();
                        viewModel.setDataSetCavalo(listaCavalos);
                        menuProviderHelper.atualizaCavalos(viewModel.getDataSetCavalo());
                        adapterCavalo.atualiza(viewModel.getDataSetCavalo());
                    }
                    if (erro != null) {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                    ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaSize, buscaVazia, recyclerCavalos, VIEW_INVISIBLE);
                });
    }

    private void dataReboque() {
        viewModel.buscaReboques().observe(this,
                resource -> {
                    final List<SemiReboque> listaReboques = resource.getDado();
                    final String erro = resource.getErro();
                    if (listaReboques != null) {
                        viewModel.setDataSetReboque(listaReboques);
                        menuProviderHelper.atualizaReboques(listaReboques);
                        adapterReboques.atualiza(listaReboques);
                        adapterCavalo.atualizaDataSet_reboque(listaReboques);
                        adapterCavalo.atualizaInner();
                    }
                    if (erro != null) {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                });
    }

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
        controlador =
                Navigation.findNavController(requireView());

        inicializaCamposDaView();
        configuraMenuProviderHelper();
        configuraRecyclerCavalo();
        configuraRecyclerReboque();
        configuraBtnNovoCavalo();
    }

    private void inicializaCamposDaView() {
        buscaVazia = binding.fragFrotaVazio;
        recyclerCavalos = binding.fragFrotaRecycler;
        recyclerReboques = binding.fragFrotaSrRecycler;
        headerReboques = binding.fragmentFrotaHeaderReboques;
        btnNovoCavalo = binding.fragFrotaCadastraNovo;
        headerCavalos = binding.fragmentFrotaHeaderCavalos;
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
                final NavDirections direction =
                        FrotaFragmentDirections.actionGlobalNavLogin();

                controlador.navigate(direction);
            }

            @Override
            public void onHomeClick() {

                controlador.popBackStack();
            }
        });

    }

    private void configuraRecyclerReboque() {
        adapterReboques = new FrotaSrAdapter(this, new ArrayList<>());
        recyclerReboques.setAdapter(adapterReboques);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
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

    /** @noinspection UnusedAssignment*/
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapterCavalo.getPosicao();
        Cavalo cavalo = viewModel.getDataSetCavalo().get(posicao);

        if (item.getItemId() == R.id.defineMotorista) {
           final DefineMotorista dialogDefineMotorista =
                    new DefineMotorista(viewModel.getDataSetMotorista(), this.getContext(), cavalo);
            dialogDefineMotorista.configuraDialog();
            dialogDefineMotorista.setDefineMotoristaCallback(new DefineMotorista.DefineMotoristaCallback() {
                @Override
                public void quandoFalha(String txt) {
                    MensagemUtil.snackBar(getView(), txt);
                }

                @Override
                public void quandoSucesso() {
                    MensagemUtil.toast(requireContext(), "Motorista definido com sucesso");
                }
            });
        }
        return super.onContextItemSelected(item);
    }

//---------------------

    protected void atualizaAdaptersAposBuscarNaSearchView(
            @NonNull final List<Cavalo> dataSet_searchView_cavalos,
            final List<SemiReboque> dataSet_SearchView_reboques
    ) {
        adapterCavalo.atualiza(dataSet_searchView_cavalos);
        adapterReboques.atualiza(dataSet_SearchView_reboques);

        FrotaFragmentViewExt.defineVisibilidadeDasListasEHeaders(
                dataSet_searchView_cavalos.size(), dataSet_SearchView_reboques.size(),
                new FrotaFragmentViewExt.VisibilidadeCallback() {
                    @Override
                    public void todasAsListasEstaoVazias() {
                        cavalosSetVisibility(INVISIBLE);
                        reboquesSetVisibility(GONE);
                        alertaSetVisibility(VISIBLE);
                    }

                    @Override
                    public void apenasListaDeCavalosVazia() {
                        cavalosSetVisibility(INVISIBLE);
                        reboquesSetVisibility(VISIBLE);
                        alertaSetVisibility(GONE);
                    }

                    @Override
                    public void apenasListaDeReboquesVazia() {
                        cavalosSetVisibility(VISIBLE);
                        reboquesSetVisibility(GONE);
                        alertaSetVisibility(GONE);
                    }

                    @Override
                    public void temInformacaoParaExibir() {
                        cavalosSetVisibility(VISIBLE);
                        reboquesSetVisibility(VISIBLE);
                        alertaSetVisibility(GONE);
                    }

                    private void alertaSetVisibility(final int visibilidade) {
                        if (buscaVazia.getVisibility() != visibilidade) {
                            buscaVazia.setVisibility(visibilidade);
                        }
                    }

                    private void cavalosSetVisibility(int visibilidade) {
                        if (recyclerCavalos.getVisibility() != visibilidade) {
                            recyclerCavalos.setVisibility(visibilidade);
                            headerCavalos.setVisibility(visibilidade);
                        }
                    }

                    private void reboquesSetVisibility(int visibilidade) {
                        if (recyclerReboques.getVisibility() != visibilidade) {
                            recyclerReboques.setVisibility(visibilidade);
                            headerReboques.setVisibility(visibilidade);
                        }
                    }
                });
    }

    protected void configuraInteracaoAoAcessarSearchView(boolean visivel) {
        if (visivel) {
            headerReboques.setVisibility(GONE);
            recyclerReboques.setVisibility(GONE);
            btnNovoCavalo.setVisibility(VISIBLE);
            AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_in_bottom, btnNovoCavalo);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        } else {
            headerReboques.setVisibility(VISIBLE);
            recyclerReboques.setVisibility(VISIBLE);
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
        final Intent intent = new Intent(getContext(), FormulariosActivity.class);
        if (reboqueId != null) intent.putExtra(CHAVE_ID, reboqueId);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_SEMIREBOQUE);
        intent.putExtra(CHAVE_ID_CAVALO, cavaloId);
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!viewModel.getDataSetCavalo().isEmpty()) {
            adapterCavalo.atualiza(viewModel.getDataSetCavalo());
            menuProviderHelper.atualizaCavalos(viewModel.getDataSetCavalo());
            menuProviderHelper.atualizaReboques(viewModel.getDataSetReboque());
            btnNovoCavalo.setVisibility(VISIBLE);
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getDataSetCavalo().size(), buscaVazia, recyclerCavalos, VIEW_INVISIBLE);
        }
    }

}

