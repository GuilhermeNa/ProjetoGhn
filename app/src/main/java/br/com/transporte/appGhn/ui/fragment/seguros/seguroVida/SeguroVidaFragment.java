package br.com.transporte.appGhn.ui.fragment.seguros.seguroVida;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_RENOVADO;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.RESULT_LOGOUT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;
import static br.com.transporte.appGhn.ui.fragment.seguros.TipoDeSeguro.VIDA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
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
import br.com.transporte.AppGhn.databinding.FragmentSeguroVidaBinding;
import br.com.transporte.appGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.SeguroVidaAdapter;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.dialog.exibeParcelasDialog.ExibeParcelasVidaDialog;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.model.BuscaDataSeguroVidaUseCase;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.viewmodel.SeguroVidaFragmentViewModel;
import br.com.transporte.appGhn.util.MensagemUtil;

public class SeguroVidaFragment extends Fragment {
    private FragmentSeguroVidaBinding binding;
    private SeguroVidaAdapter adapter;
    private RecyclerView recyclerView;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();
    private SeguroVidaFragmentViewModel viewModel;

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
                            MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                            break;
                    }
                });
    }

    //------------------------------------ Metodos Publicos ----------------------------------------

    public void atualizaAdapter(final List<DespesaComSeguroDeVida> dataSet) {
        final LinearLayout alertaLayout = binding.fragSegurosVazio;
        adapter.atualiza(dataSet);
        exibeAlertaDeListagemDeDadosVazia(dataSet, alertaLayout);
    }

    private void exibeAlertaDeListagemDeDadosVazia(
            @NonNull final List<DespesaComSeguroDeVida> dataSet,
            final LinearLayout alertaLayout
    ) {
        if (dataSet.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            alertaLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            alertaLayout.setVisibility(View.INVISIBLE);
        }
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
        final ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(SeguroVidaFragmentViewModel.class);
    }

    private void getDataUseCase() {
        final BuscaDataSeguroVidaUseCase getDataUseCase =
                new BuscaDataSeguroVidaUseCase(requireContext(), this);
        getDataUseCase.buscaDataRelacionadaSeguroVida(
                dataSet -> {
                    viewModel.setDataSet(dataSet);
                    atualizaAdapter(dataSet);
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSeguroVidaBinding.inflate(getLayoutInflater());
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
        adapter = new SeguroVidaAdapter(this, viewModel.getDataSet());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(seguro -> {
            NavController controlador = Navigation.findNavController(requireView());
            NavDirections direction = SeguroVidaFragmentDirections.actionNavSeguroOutrosToSeguroResumoFragment(((DespesaComSeguro) seguro).getId(), VIDA);
            controlador.navigate(direction);
        });
    }

    private void configuraMenuProvider() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        requireActivity().setResult(RESULT_LOGOUT);
                        requireActivity().finish();
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    /** @noinspection UnusedAssignment*/
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        final DespesaComSeguroDeVida seguro = viewModel.getDataSet().get(posicao);

        switch (item.getItemId()) {
            case R.id.visualizaParcelas:

                final ExibeParcelasVidaDialog dialog = new ExibeParcelasVidaDialog(requireContext(), this);
                dialog.showBottomDialog(seguro);
                break;

            case R.id.renovarSeguro:
                final Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_VIDA);
                intent.putExtra(CHAVE_REQUISICAO, RENOVANDO);
                intent.putExtra(CHAVE_ID, seguro.getId());
                activityResultLauncher.launch(intent);
                break;
        }
        return super.onContextItemSelected(item);

    }

    //----------------------------------------------------------------------------------------------
    //                                          OnResume                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}