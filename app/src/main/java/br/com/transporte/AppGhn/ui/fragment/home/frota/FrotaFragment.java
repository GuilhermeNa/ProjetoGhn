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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FrotaFragment extends Fragment implements
        DefineMotorista.DefineMotoristaCallback,
        FrotaMenuProviderHelper.MenuProviderCallback,
        CavaloAdapter.OnItemClickListener
{
    public static final String FROTA = "Frota";
    private FragmentFrotaBinding binding;
    private RoomCavaloDao cavaloDao;
    private CavaloAdapter adapter;
    private List<Cavalo> listaDeCavalos;
    private RecyclerView recyclerCavalos;
    private RoomSemiReboqueDao reboqueDao;
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

    private List<Cavalo> getListaDeCavalos() {
        return cavaloDao.todos();
    }

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cavaloDao = GhnDataBase.getInstance(this.requireContext()).getRoomCavaloDao();
        reboqueDao = GhnDataBase.getInstance(this.requireContext()).getRoomReboqueDao();
        listaDeCavalos = getListaDeCavalos();
        listaDeReboques = getListaDeReboques();
    }

    private List<SemiReboque> getListaDeReboques() {
        return reboqueDao.todos();
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
        configuraMenuProvider();
        configuraClickListenerParaExibirSemiReboques();
        configuraRecyclerSr();
        configuraRecyclerCavalo();
        configuraBtnNovoCavalo();
    }

    void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(FROTA);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void configuraMenuProvider() {
        FrotaMenuProviderHelper menuProviderHelper = new FrotaMenuProviderHelper(this, getListaDeCavalos(), getListaDeReboques());
        requireActivity().addMenuProvider(menuProviderHelper, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setMenuProviderCallback(this);
    }

    private void inicializaCamposDaView() {
        recyclerCavalos = binding.fragFrotaRecycler;
        recyclerSr = binding.fragFrotaSrRecycler;
        btnNovoCavalo = binding.fragFrotaCadastraNovo;
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
                recyclerSr.setVisibility(VISIBLE);
                janelaFechada = false;
            } else {
                imgIcSetaSr.startAnimation(animationFechamento);
                recyclerSr.setVisibility(GONE);
                janelaFechada = true;
            }
        });
        if (recyclerSr.getVisibility() == VISIBLE) {
            imgIcSetaSr.startAnimation(animationAbertura);
        }
    }

    private void configuraRecyclerSr() {
        if(listaDeReboques.isEmpty()){
            binding.layoutExibeReboques.setVisibility(GONE);
        } else {
            binding.layoutExibeReboques.setVisibility(VISIBLE);
        }
        adapterSr = new FrotaSrAdapter(this, listaDeReboques);
        recyclerSr.setAdapter(adapterSr);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.requireContext(), RecyclerView.HORIZONTAL, false);
        recyclerSr.setLayoutManager(layoutManager);
    }

    private void configuraRecyclerCavalo() {
        adapter = new CavaloAdapter(this, listaDeCavalos);
        recyclerCavalos.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    //----------------------------------------------------------------------------------------------
    //                                   On Context Item Selected                                 ||
    //----------------------------------------------------------------------------------------------

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        Cavalo cavalo = listaDeCavalos.get(posicao);

        if (item.getItemId() == R.id.defineMotorista) {
            DefineMotorista defineMotorista = new DefineMotorista(this.getContext(), cavalo);
            defineMotorista.configuraDialog();
            defineMotorista.setDefineMotoristaCallback(this);
        }
        return super.onContextItemSelected(item);
    }


    //----------------------------------------------------------------------------------------------
    //                                   CallBack Menu Provider Helper                            ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void realizaBusca(List<Cavalo> dataSet_searchView_cavalo, List<SemiReboque> dataSet_searchView_semiReboque) {
        adapter.atualiza(dataSet_searchView_cavalo);
        adapterSr.atualiza(dataSet_searchView_semiReboque);
    }

    @Override
    public void searchViewAtivada() {
        btnNovoCavalo.setVisibility(View.INVISIBLE);
        AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_out_bottom, btnNovoCavalo);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public void searchViewDesativada() {
        btnNovoCavalo.setVisibility(VISIBLE);
        AnimationUtil.defineAnimacao(this.requireContext(), R.anim.slide_in_bottom, btnNovoCavalo);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
    }

    //----------------------------------------------------------------------------------------------
    //                                   CallBack Define Motorista                                ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void quandoFalha(String txt) {
        MensagemUtil.snackBar(getView(), txt);
    }

    @Override
    public void quandoSucesso() {
        adapter.atualiza(cavaloDao.todos());
        Log.d("teste", "Deu");
    }

    //----------------------------------------------------------------------------------------------
    //                                   ClickListener Adapter Cavalo                             ||
    //----------------------------------------------------------------------------------------------

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
}
