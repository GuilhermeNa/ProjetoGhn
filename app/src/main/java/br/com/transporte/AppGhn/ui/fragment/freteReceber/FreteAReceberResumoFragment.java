package br.com.transporte.AppGhn.ui.fragment.freteReceber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_RECEBIMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_RECEBIMENTO_FRETE;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.AppGhn.databinding.FragmentFreteAReceberResumoBinding;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.RecebimentoFretesAdapter;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class FreteAReceberResumoFragment extends Fragment implements MenuProvider {
    public static final String DETALHES_DO_FRETE = "Detalhes do Frete";
    private FragmentFreteAReceberResumoBinding binding;
    private Frete frete;
    private TextView placaTxtView, origemTxtView, destinoTxtView, cargaTxtView, freteBrutoTxtView,
            freteLiquidoTxtView, seguroCargaTxtView, outrosDescontosTxtView, comissaoMotoristaTxtView;
    private RecebimentoFretesAdapter adapter;
    private List<RecebimentoDeFrete> dataSet_recebimentoFrete;
    private RoomRecebimentoFreteDao recebimentoDao;
    private GhnDataBase dataBase;
    private final ActivityResultLauncher<Intent> activityResultLauncherEditaFrete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        configuraUi();
                        Toast.makeText(this.requireContext(), REGISTRO_EDITADO, Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        atualizaDataSet();
                        Navigation.findNavController(this.requireView()).popBackStack();
                        Toast.makeText(this.requireContext(), REGISTRO_APAGADO, Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;
                }
            });

    private final ActivityResultLauncher<Intent> activityResultLauncherRecebimento = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                        atualizaAdapterAposResultLauncher(REGISTRO_CRIADO);
                        break;

                    case RESULT_EDIT:
                        atualizaAdapterAposResultLauncher(REGISTRO_EDITADO);
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        atualizaAdapterAposResultLauncher(REGISTRO_APAGADO);
                        break;
                }
            });

    private void atualizaAdapterAposResultLauncher(String registroCriado) {
        atualizaDataSet();
        adapter.atualiza(getDataSet());
        Toast.makeText(this.requireContext(), registroCriado, Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataSet();
        frete = recebeIdArgument();
        atualizaDataSet();
    }

    private void inicializaDataSet() {
        dataBase = GhnDataBase.getInstance(requireContext());
        recebimentoDao = dataBase.getRoomRecebimentoFreteDao();
    }

    private void atualizaDataSet() {
        dataSet_recebimentoFrete = recebimentoDao.listaPorFreteId(frete.getId());
    }

    @NonNull
    @Contract(" -> new")
    private List<RecebimentoDeFrete> getDataSet() {
        return new ArrayList<>(dataSet_recebimentoFrete);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFreteAReceberResumoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraUi();
        configuraRecycler();
        configuraBtn();
        configuraToolbar();
    }

    private void inicializaCamposDaView() {
        placaTxtView = binding.fragFreteReceberResumoPlaca;
        origemTxtView = binding.fragFreteReceberResumoOrigem;
        destinoTxtView = binding.fragFreteReceberResumoDestino;
        cargaTxtView = binding.fragFreteReceberResumoCarga;
        freteBrutoTxtView = binding.fragFreteReceberResumoFreteBruto;
        freteLiquidoTxtView = binding.fragFreteReceberResumoFreteLiquido;
        seguroCargaTxtView = binding.fragFreteReceberResumoSeguroCarga;
        outrosDescontosTxtView = binding.fragFreteReceberResumoOutrosDescontos;
        comissaoMotoristaTxtView = binding.fragFreteReceberResumoComissaoMotorista;
    }

    private void configuraUi() {
        RoomCavaloDao cavaloDao = dataBase.getRoomCavaloDao();
        String placa = cavaloDao.localizaPeloId(frete.getRefCavaloId()).getPlaca();

        placaTxtView.setText(placa);
        origemTxtView.setText(frete.getOrigem());
        destinoTxtView.setText(frete.getDestino());
        cargaTxtView.setText(frete.getCarga());
        freteBrutoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getFreteBruto()));
        freteLiquidoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getFreteLiquidoAReceber()));
        seguroCargaTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getSeguroDeCarga()));
        outrosDescontosTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getDescontos()));
        comissaoMotoristaTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getComissaoAoMotorista()));
    }

    private void configuraRecycler() {
        RecyclerView recyclerView = binding.fragFreteReceberResumoRecycler;
        adapter = new RecebimentoFretesAdapter(this, getDataSet());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(recebimentoId -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_RECEBIMENTO_FRETE);
            intent.putExtra(CHAVE_ID, frete.getId());
            intent.putExtra(CHAVE_ID_RECEBIMENTO, recebimentoId);
            activityResultLauncherRecebimento.launch(intent);
        });
    }

    private void configuraBtn() {
        Button btn = binding.btnAdd;
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_RECEBIMENTO_FRETE);
            intent.putExtra(CHAVE_ID, frete.getId());
            activityResultLauncherRecebimento.launch(intent);
        });
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(DETALHES_DO_FRETE);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private Frete recebeIdArgument() {
        RoomFreteDao freteDao = dataBase.getRoomFreteDao();
        Long freteId = FreteAReceberResumoFragmentArgs.fromBundle(getArguments()).getFreteId();
        frete = freteDao.localizaPeloId(freteId);
        return frete;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_search);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                Toast.makeText(this.requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_padrao_editar:
                Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_FRETE);
                intent.putExtra(CHAVE_ID, frete.getId());
                activityResultLauncherEditaFrete.launch(intent);
                break;

            case android.R.id.home:
                NavController controlador = Navigation.findNavController(this.requireView());
                controlador.popBackStack();
                break;
        }
        return false;
    }

}