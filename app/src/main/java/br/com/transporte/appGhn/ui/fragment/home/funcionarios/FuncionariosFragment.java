package br.com.transporte.appGhn.ui.fragment.home.funcionarios;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFuncionariosBinding;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.fragment.ConstantesFragment;
import br.com.transporte.appGhn.ui.fragment.home.funcionarios.adapters.MotoristasAdapter;
import br.com.transporte.appGhn.ui.viewmodel.FuncionariosViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.FuncionariosViewModelFactory;
import br.com.transporte.appGhn.util.AnimationUtil;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.appGhn.util.MensagemUtil;

public class FuncionariosFragment extends Fragment {
    public static final String FUNCIONARIOS = "Funcionários";
    private FragmentFuncionariosBinding binding;
    private MotoristasAdapter adapter;
    private RecyclerView recycler;
    private Button btn;
    private FuncionariosMenuProviderHelper menuProviderHelper;
    private FuncionariosViewModel viewModel;
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
            }
    );
    private LinearLayout buscaVazia;
    private NavController controlador;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        solicitaDataDataParaViewModel();
    }

    private void inicializaViewModel() {
        final MotoristaRepository repository = new MotoristaRepository(requireContext());
        final FuncionariosViewModelFactory factory = new FuncionariosViewModelFactory(repository);
        final ViewModelProvider provedor = new ViewModelProvider(this, factory);
        viewModel = provedor.get(FuncionariosViewModel.class);
    }

    private void solicitaDataDataParaViewModel() {
        viewModel.buscaMotoristas().observe(this,
                resource -> {
                    List<Motorista> listaMotorista = resource.getDado();
                    String erro = resource.getErro();
                    int listaSize = 0;
                    if (listaMotorista != null) {
                        viewModel.setDataSet(listaMotorista);
                        atualiza();
                        listaSize = listaMotorista.size();
                    }
                    if (erro != null) {
                        MensagemUtil.toast(requireContext(), erro);
                    }
                    configuraExibicaoDeAlertaParaListaVazia(listaSize);
                });
    }

    private void atualiza() {
        if (viewModel.getDataSet() != null)
            adapter.atualiza(viewModel.getDataSet());
        if (menuProviderHelper != null)
            menuProviderHelper.atualizaDataSet(viewModel.getDataSet());
    }

    private void configuraExibicaoDeAlertaParaListaVazia(int listaSize) {
        buscaVazia = binding.buscaVazia;
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaSize, buscaVazia, recycler, VIEW_INVISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFuncionariosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controlador =
                Navigation.findNavController(requireView());

        inicializaCampos();
        configuraMenuProvider();
        configuraRecycler();
        configuraBtnNovoFuncionario();

    }

    private void inicializaCampos() {
        btn = binding.btn;
        recycler = binding.recycler;
    }

    private void configuraMenuProvider() {
        menuProviderHelper = new FuncionariosMenuProviderHelper(new ArrayList<>());
        requireActivity().addMenuProvider(menuProviderHelper, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setCallBack(new FuncionariosMenuProviderHelper.MenuProviderCallback() {

            @Override
            public void realizaBusca(List<Motorista> lista_searchView) {
                adapter.atualiza(lista_searchView);
                configuraExibicaoDeAlertaParaListaVazia(lista_searchView.size());
            }

            @Override
            public void searchViewAtivada() {
                btn.setVisibility(GONE);
                AnimationUtil.defineAnimacao(requireContext(), R.anim.slide_out_bottom, btn);
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
            }

            @Override
            public void searchViewDesativada() {
                btn.setVisibility(View.VISIBLE);
                AnimationUtil.defineAnimacao(requireContext(), R.anim.slide_in_bottom, btn);
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(FUNCIONARIOS);
            }

            @Override
            public void onLogoutClick() {
                final NavDirections direction =
                        FuncionariosFragmentDirections.actionGlobalNavLogin();

                controlador.navigate(direction);
            }

            @Override
            public void onHomeCLick() {
                controlador.popBackStack();
            }
        });
    }

    private void configuraRecycler() {
        adapter = new MotoristasAdapter(this, new ArrayList<>());
        recycler.setAdapter(adapter);
        LinearLayoutManager LayoutManager = new GridLayoutManager(getContext(), 2);
        recycler.setLayoutManager(LayoutManager);
        adapter.setonItemClickListener(id -> {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(ConstantesFragment.CHAVE_ID, id);
            intent.putExtra(ConstantesFragment.CHAVE_FORMULARIO, ConstantesFragment.VALOR_MOTORISTA);
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraBtnNovoFuncionario() {
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(ConstantesFragment.CHAVE_FORMULARIO, ConstantesFragment.VALOR_MOTORISTA);
            activityResultLauncher.launch(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!viewModel.getDataSet().isEmpty()) {
            adapter.atualiza(viewModel.getDataSet());
            menuProviderHelper.atualizaDataSet(viewModel.getDataSet());
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getDataSet().size(), buscaVazia, recycler, VIEW_INVISIBLE);
        }
    }

}
