package br.com.transporte.AppGhn.ui.fragment.home.funcionarios;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.databinding.FragmentFuncionariosBinding;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.MotoristasAdapter;
import br.com.transporte.AppGhn.dao.MotoristaDAO;
import br.com.transporte.AppGhn.ui.fragment.ConstantesFragment;
import br.com.transporte.AppGhn.ui.fragment.extensions.BuscaDeDadosSemResultado;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FuncionariosFragment extends Fragment implements FuncionariosMenuProviderHelper.MenuProviderCallback{
    public static final String FUNCIONARIOS = "Funcionários";
    private FragmentFuncionariosBinding binding;
    private MotoristasAdapter adapter;
    private RoomMotoristaDao motoristaDao;
    private List<Motorista> listaDeFuncionarios;
    private RecyclerView recycler;
    private Button btn;
    private LinearLayout buscaVazia;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                        atualizaAdapter(REGISTRO_CRIADO);
                        break;

                    case RESULT_DELETE:
                        atualizaAdapter(REGISTRO_APAGADO);
                        break;

                    case RESULT_EDIT:
                        atualizaAdapter(REGISTRO_EDITADO);
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
    );

    private void atualizaAdapter(String txt) {
        listaDeFuncionarios = getListaDeFuncionarios();
        adapter.atualiza(listaDeFuncionarios);
        MensagemUtil.toast(requireContext(), txt);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        motoristaDao = GhnDataBase.getInstance(requireContext()).getRoomMotoristaDao();
        listaDeFuncionarios = getListaDeFuncionarios();
    }

    private List<Motorista> getListaDeFuncionarios() {
        return motoristaDao.todos();
    }

    //----------------------------------------------------------------------------------------------
    //                                            OnCreateView                                    ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFuncionariosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                           OnViewCreated                                    ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraToolbar();
        configuraMenuProvider();
        configuraRecycler();
        configuraBtnNovoFuncionario();
        configuraUi();
    }

    private void configuraUi() {
        BuscaDeDadosSemResultado.substituiRecyclerPorAviso(listaDeFuncionarios.size(), recycler, buscaVazia);
    }

    private void configuraMenuProvider() {
        FuncionariosMenuProviderHelper menuProviderHelper = new FuncionariosMenuProviderHelper(this);
        requireActivity().addMenuProvider(menuProviderHelper, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setMenuProviderCallback(this);
    }

    private void inicializaCampos() {
        btn = binding.btn;
        recycler = binding.recycler;
        buscaVazia = binding.buscaVazia;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(FUNCIONARIOS);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void configuraBtnNovoFuncionario() {
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

    //----------------------------------------------------------------------------------------------
    //                                           Callback                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void realizaBusca(List<Motorista> dataSet) {
        adapter.atualiza(dataSet);
        configuraUi();
    }

    @Override
    public void searchViewAtivada() {
        btn.setVisibility(GONE);
        AnimationUtil.defineAnimacao(requireContext(), R.anim.slide_out_bottom, btn);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public void searchViewDesativada() {
        btn.setVisibility(View.VISIBLE);
        AnimationUtil.defineAnimacao(requireContext(), R.anim.slide_in_bottom, btn);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
    }
}
