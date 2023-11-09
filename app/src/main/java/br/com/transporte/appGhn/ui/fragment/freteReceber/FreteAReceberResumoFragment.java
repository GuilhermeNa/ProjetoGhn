package br.com.transporte.appGhn.ui.fragment.freteReceber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID_RECEBIMENTO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_RECEBIMENTO_FRETE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.transporte.AppGhn.databinding.FragmentFreteAReceberResumoBinding;
import br.com.transporte.appGhn.filtros.FiltraCavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.abstracts.Frota;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.RecebimentoFretesAdapter;
import br.com.transporte.appGhn.ui.viewmodel.FreteAReceberActViewModel;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class FreteAReceberResumoFragment extends Fragment {
    private FragmentFreteAReceberResumoBinding binding;
    private Frete frete;
    private TextView placaTxtView, origemTxtView, destinoTxtView, cargaTxtView, freteBrutoTxtView,
            freteLiquidoTxtView, seguroCargaTxtView, outrosDescontosTxtView, comissaoMotoristaTxtView;
    private RecebimentoFretesAdapter adapter;
    private FreteAReceberActViewModel viewModel;
    private final ActivityResultLauncher<Intent> activityResultLauncherEditaFrete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;
                    case RESULT_DELETE:
                        Navigation.findNavController(this.requireView()).popBackStack();
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;
                }
            });

    private final ActivityResultLauncher<Intent> activityResultLauncherRecebimento = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                switch (codigoResultado) {
                    case RESULT_OK:
                        MensagemUtil.toast(requireContext(), REGISTRO_CRIADO);
                        break;
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;
                    case RESULT_DELETE:
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                }
            });
    private TextView campoEmpresa;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(FreteAReceberActViewModel.class);
        Long freteId = recebeFreteIdArgument();
        recuperaObjFrete(freteId);
        observerRecebimentos(freteId);
    }

    @NonNull
    private Long recebeFreteIdArgument() {
        return FreteAReceberResumoFragmentArgs.fromBundle(getArguments()).getFreteId();
    }

    private void recuperaObjFrete(final Long id) {
        viewModel.localizaFrete(id).observe(this,
                freteRecebido -> {
                    if (freteRecebido != null) {
                        frete = freteRecebido;
                        configuraUi();
                    }
                });
    }

    private void observerRecebimentos(Long freteId) {
        viewModel.buscaRecebimentosPorFreteId(freteId).observe(this,
                lista -> {
                    if (lista != null) {
                        adapter.atualiza(lista);
                    }
                });
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
        configuraRecycler();
        configuraBtn();
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
        campoEmpresa = binding.fragFreteReceberResumoEmpresa;
    }

    private void configuraUi() {
        final Frota cavalo = FiltraCavalo.localizaPeloId(viewModel.getDataSet_cavalo(), frete.getRefCavaloId());
        String placa = "";
        if (cavalo != null) {
            placa = cavalo.getPlaca();
        }
        placaTxtView.setText(placa);
        campoEmpresa.setText(frete.getEmpresa());
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
        final RecyclerView recyclerView = binding.fragFreteReceberResumoRecycler;
        adapter = new RecebimentoFretesAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
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

    public void navegaParaFormulario() {
        Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_FRETE);
        intent.putExtra(CHAVE_ID, frete.getId());
        activityResultLauncherEditaFrete.launch(intent);
    }

    public void popBackStack() {
        NavController controlador = Navigation.findNavController(this.requireView());
        controlador.popBackStack();
    }

    public void actSolicitaAtualizacao() {
        configuraUi();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}