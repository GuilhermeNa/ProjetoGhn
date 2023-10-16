package br.com.transporte.AppGhn.ui.fragment.despesasAdm.indireta;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_TIPO_DESPESA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentDespesasAdmIndiretasBinding;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.viewmodel.DespesaAdmActViewModel;
import br.com.transporte.AppGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.DespesasAdmIndiretasAdapter;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.indireta.domain.BuscaDespesasAdmIndiretasUseCase;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.indireta.viewmodel.DespesaAdmIndiretaViewModel;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class DespesasAdmIndiretasFragment extends Fragment {

    private FragmentDespesasAdmIndiretasBinding binding;
    private RecyclerView recyclerView;
    private DespesasAdmIndiretasAdapter adapter;
    private DespesaAdmActViewModel sharedViewModel;
    private DespesaAdmIndiretaViewModel viewModel;
    private BuscaDespesasAdmIndiretasUseCase getDataUseCase;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSharedViewModel();
        initViewModelDoFragment();
        getDataUseCase();
    }

    private void initSharedViewModel() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(DespesaAdmActViewModel.class);
    }

    private void initViewModelDoFragment() {
        viewModel = new DespesaAdmIndiretaViewModel();
    }

    private void getDataUseCase() {
        getDataUseCase = new BuscaDespesasAdmIndiretasUseCase(requireContext(), this);
        getDataUseCase.buscaDataRelacionadaAsDespesasAdmIndiretas(
                sharedViewModel.getDataInicial(),
                sharedViewModel.getDataFinal(),
                listaDespesas -> {
                    if (listaDespesas != null) {
                        viewModel.setDataSet(listaDespesas);
                        atualizaUi(listaDespesas);
                    }
                });
    }

    private void atualizaUi(final List<DespesaAdm> dataSet) {
        final LinearLayout aletaLayout = binding.alerta.alerta;
        adapter.atualiza(dataSet);
        configuraUi(dataSet);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(
                dataSet.size(), aletaLayout,
                recyclerView, VIEW_INVISIBLE
        );
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDespesasAdmIndiretasBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraToolbar();
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.fragItemDespesasFinanceirasRecycler;
    }

    private void configuraRecycler() {
        adapter = new DespesasAdmIndiretasAdapter(this, viewModel.getDataSet());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(despesaId -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_DESPESA_ADM);
            intent.putExtra(CHAVE_TIPO_DESPESA, TipoDespesa.INDIRETA);
            intent.putExtra(CHAVE_ID, (despesaId));
            startActivity(intent);
        });
    }

    private void configuraUi(final List<DespesaAdm> dataSet) {
        final TextView campoData = binding.valorPago;
        final BigDecimal valorTotal = CalculoUtil.somaDespesasAdm(dataSet);

        try {
            BindData.R$fromBigDecimal(campoData, valorTotal);
        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    private void configuraToolbar() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_padrao, menu);
                menu.removeItem(R.id.menu_padrao_editar);
                menu.removeItem(R.id.menu_padrao_search);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        MensagemUtil.toast(requireContext(), LOGOUT);
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void atualizaPorBuscaNaData() {
        getDataUseCase.solicitaAtualizacaoDeDados(
                sharedViewModel.getDataInicial(),
                sharedViewModel.getDataFinal()
        );
    }
}