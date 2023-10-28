package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.FRAGMENTS_LISTENER;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.ON_SEARCH_CLEAR;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.ON_SEARCH_CLICK;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_RENOVADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;
import static br.com.transporte.AppGhn.ui.fragment.seguros.TipoDeSeguro.FROTA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentSeguroFrotaBinding;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.SeguroFrotaRepository;
import br.com.transporte.AppGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.SeguroFrotaAdapter;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.exibeParcelasDialog.ExibeParcelasFrotaDialog;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.domain.BuscaDataSeguroFrotaUseCase;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.domain.model.DespesaSeguroFrotaObject;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.extensions.SeguroFrotaMenuProviderHelper;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.viewmodel.SeguroFrotaViewModel;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.viewmodel.SeguroFrotaViewModelFactory;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class SeguroFrotaFragment extends Fragment {
    private FragmentSeguroFrotaBinding binding;
    private RecyclerView recyclerView;
    private NavDirections direction;
    private SeguroFrotaAdapter adapter;
    private SeguroFrotaViewModel viewModel;
    private SeguroFrotaMenuProviderHelper menuProviderExt;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();

    @NonNull
    @Contract(" -> new")
    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_UPDATE:
                            MensagemUtil.toast(requireContext(), REGISTRO_RENOVADO);
                            break;

                        case RESULT_CANCELED:
                            MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                            break;

                        case RESULT_OK:
                            MensagemUtil.toast(requireContext(), REGISTRO_CRIADO);
                            break;
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        getDataUseCase();
    }

    private void inicializaViewModel() {
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final SeguroFrotaRepository frotaRepository = new SeguroFrotaRepository(requireContext());
        final SeguroFrotaViewModelFactory factory = new SeguroFrotaViewModelFactory(cavaloRepository, frotaRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(SeguroFrotaViewModel.class);
    }

    private void getDataUseCase() {
        final BuscaDataSeguroFrotaUseCase dataUseCase =
                new BuscaDataSeguroFrotaUseCase(requireContext(), this);
        dataUseCase.buscaDataRelacionadaAoSeguroFrota(
                seguroObjects -> {
                    if (seguroObjects != null) {
                        viewModel.setDataSet(seguroObjects);
                        menuProviderExt.atualizaDataSet(seguroObjects);
                        atualizaUi(seguroObjects);
                    }
                });
    }

    private void atualizaUi(final List<DespesaSeguroFrotaObject> dataSet) {
        final LinearLayout alertaLayout = binding.fragSegurosVazio;
        adapter.atualiza(dataSet);
        if(dataSet.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            alertaLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            alertaLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSeguroFrotaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configuraRecycler();
        configuraMenuProvider();
    }

    private void configuraRecycler() {
        recyclerView = binding.fragSegurosRecycler;
        adapter = new SeguroFrotaAdapter(this, viewModel.getDataSet());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(seguroId -> {
            final NavController controlador = Navigation.findNavController(requireView());
            direction = SeguroFrotaFragmentDirections.actionNavSegurosFragmentToSeguroResumoFragment(seguroId, FROTA);
            controlador.navigate(direction);
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        final DespesaSeguroFrotaObject seguroObject = viewModel.getDataSet().get(posicao);

        switch (item.getItemId()) {
            case R.id.visualizaParcelas:
                ExibeParcelasFrotaDialog dialog = new ExibeParcelasFrotaDialog(requireContext(), this);
                dialog.showBottomDialog(seguroObject);
                break;

            case R.id.renovarSeguro:
                Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_FROTA);
                intent.putExtra(CHAVE_REQUISICAO, RENOVANDO);
                intent.putExtra(CHAVE_ID, seguroObject.getIdSeguro());
                activityResultLauncher.launch(intent);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void configuraMenuProvider() {
        menuProviderExt = new SeguroFrotaMenuProviderHelper(
                viewModel.getDataSet()
        );
        requireActivity().addMenuProvider(
                menuProviderExt, getViewLifecycleOwner(), Lifecycle.State.RESUMED
        );
        menuProviderExt.setCallbackMenuProvider(
                new SeguroFrotaMenuProviderHelper.CallbackMenuProvider() {
                    @Override
                    public void realizaBusca(List<DespesaSeguroFrotaObject> dataSet_search) {
                        atualizaUi(dataSet_search);
                    }

                    @Override
                    public void searchViewAtiva() {
                        final Bundle bundle = new Bundle();
                        bundle.putString(ON_SEARCH_CLICK, null);
                        getParentFragmentManager().setFragmentResult(FRAGMENTS_LISTENER, bundle);
                    }

                    @Override
                    public void searchViewInativa() {
                        final Bundle bundle = new Bundle();
                        bundle.putString(ON_SEARCH_CLEAR, null);
                        getParentFragmentManager().setFragmentResult(FRAGMENTS_LISTENER, bundle);
                    }

                    @Override
                    public void logoutClick() {
                        MensagemUtil.toast(requireContext(), LOGOUT);
                    }

                    @Override
                    public void homeClick() {
                        requireActivity().finish();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}