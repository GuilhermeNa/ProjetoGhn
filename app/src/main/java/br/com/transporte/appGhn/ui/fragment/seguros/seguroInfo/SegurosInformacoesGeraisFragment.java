package br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.RESULT_LOGOUT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import br.com.transporte.AppGhn.databinding.FragmentSegurosInformacoesGeraisBinding;
import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.fragment.seguros.TipoDeSeguro;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo.domain.CarregaDataDoSeguroUseCase;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo.extensions.CarregaArgumentsExt;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo.extensions.SeguroInformacoesGeraisMenuProviderExt;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo.viewmodel.SegurosInformacoesGeraisViewModel;
import br.com.transporte.appGhn.util.MensagemUtil;

public class SegurosInformacoesGeraisFragment extends Fragment {

    public static final String SEGURO_COMPREENSIVO = "Seguro Compreensivo";
    public static final String SEGURO_DE_VIDA = "Seguro De Vida";
    private FragmentSegurosInformacoesGeraisBinding binding;
    private TextView campoTitulo;
    private NavController controlador;
    private SegurosInformacoesGeraisViewModel viewModel;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();

    @NonNull
    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int codigoResultado = result.getResultCode();
                    switch (codigoResultado) {
                        case RESULT_DELETE:
                            Toast.makeText(requireContext(), REGISTRO_APAGADO, Toast.LENGTH_SHORT).show();
                            controlador.popBackStack();
                            break;

                        case RESULT_EDIT:
                            MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                            break;

                        case RESULT_CANCELED:
                            Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
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
        tentaCarregarArguments();
        carregaDataEPreparaAmbienteUseCase();
    }

    private void inicializaViewModel() {
        final ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(SegurosInformacoesGeraisViewModel.class);
    }

    private void tentaCarregarArguments() {
        final CarregaArgumentsExt argumentsExt = new CarregaArgumentsExt(getArguments());
        argumentsExt.tentaCarregarArguments(new CarregaArgumentsExt.CarregaArgumentsSeguroInfoCallback() {
            @Override
            public void carregaId(Long id) {
                viewModel.setSeguroId(id);
            }

            @Override
            public void carregaTipo(TipoDeSeguro tipoDeSeguro) {
                viewModel.setTipoDeSeguro(tipoDeSeguro);
            }
        });
    }

    private void carregaDataEPreparaAmbienteUseCase() {
        CarregaDataDoSeguroUseCase dataUseCase =
                new CarregaDataDoSeguroUseCase(requireContext(), this);

        dataUseCase.getData(viewModel.getSeguroId(), viewModel.getTipoDeSeguro(),
                new CarregaDataDoSeguroUseCase.CarregaDataSeguroVidaUseCaseCallback() {
                    @Override
                    public void quandoSeguroFrota(DespesaComSeguroFrota seguroFrota, String placa) {
                        exibeLayoutFrota();
                        viewModel.setSeguroFrota(seguroFrota);
                        viewModel.setPlaca(placa);
                        vinculaCamposEmComumEntreOsDoisSeguros(seguroFrota);
                        vinculaSeguroFrota(seguroFrota);
                    }

                    @Override
                    public void quandoSeguroVida(DespesaComSeguroDeVida seguroVida) {
                        exibeLayoutVida();
                        viewModel.setSeguroVida(seguroVida);
                        viewModel.setPlaca(" ");
                        vinculaCamposEmComumEntreOsDoisSeguros(seguroVida);
                        vinculaSeguroVida(seguroVida);
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                     ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSegurosInformacoesGeraisBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private void exibeLayoutFrota() {
        final LinearLayout seguroAutoLayout = binding.fragSalarioDetalhesLayoutCoberturasAuto;
        seguroAutoLayout.setVisibility(View.VISIBLE);
    }

    private void exibeLayoutVida() {
        final LinearLayout seguroVidaLayout = binding.fragSalarioDetalhesLayoutCoberturasVida;
        seguroVidaLayout.setVisibility(View.VISIBLE);
    }

    private void vinculaCamposEmComumEntreOsDoisSeguros(@NonNull final DespesaComSeguro seguro) {
        final TextView campoDataInicial = binding.fragSalarioDetalhesInicioVigencia;
        final TextView campoDataFinal = binding.fragSalarioDetalhesFimVigencia;
        final TextView campoValorTotal = binding.fragSalarioDetalhesValorTotal;
        final TextView campoQuantidadeParcelas = binding.fragSalarioDetalhesParcelas;
        final TextView campoValorParcela = binding.fragSalarioDetalhesValorParcela;
        final TextView campoCia = binding.fragSalarioDetalhesCompanhia;
        final TextView campoNContrato = binding.fragSalarioDetalhesNumeroContrato;

        try {
            BindData.fromLocalDate(campoDataInicial, seguro.getDataInicial());
            BindData.fromLocalDate(campoDataFinal, seguro.getDataFinal());
            BindData.R$fromBigDecimal(campoValorTotal, seguro.getValorDespesa());
            BindData.fromInteger(campoQuantidadeParcelas, seguro.getParcelas());
            BindData.R$fromBigDecimal(campoValorParcela, seguro.getValorParcela());
            BindData.fromString(campoCia, seguro.getCompanhia());
            BindData.fromInteger(campoNContrato, seguro.getNContrato());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    private void vinculaSeguroFrota(@NonNull final DespesaComSeguroFrota seguro) {
        final TextView campoCoberturaCasco = binding.fragSalarioDetalhesCoberturaCasco;
        final TextView campoRcfMateriais = binding.fragSalarioDetalhesCoberturaRcfMateriais;
        final TextView campoRcfCorporais = binding.fragSalarioDetalhesCoberturaRcfCorporais;
        final TextView campoAppMorte = binding.fragSalarioDetalhesCoberturaAppMorte;
        final TextView campoAppInvalidez = binding.fragSalarioDetalhesCoberturaAppInvalidez;
        final TextView campoDanosMorais = binding.fragSalarioDetalhesCoberturaDanosMorais;
        final TextView campoCoberturaVidros = binding.fragSalarioDetalhesCoberturaVidros;
        final TextView campoAssist24h = binding.fragSalarioDetalhesCoberturaAssistencia;

        try {
            BindData.fromString(campoTitulo, SEGURO_COMPREENSIVO +" "+ viewModel.getPlaca());
            BindData.fromString(campoCoberturaCasco, seguro.getCoberturaCasco());
            BindData.R$fromBigDecimal(campoRcfMateriais, seguro.getCoberturaRcfMateriais());
            BindData.R$fromBigDecimal(campoRcfCorporais, seguro.getCoberturaRcfCorporais());
            BindData.R$fromBigDecimal(campoAppMorte, seguro.getCoberturaAppMorte());
            BindData.R$fromBigDecimal(campoAppInvalidez, seguro.getCoberturaAppInvalidez());
            BindData.R$fromBigDecimal(campoDanosMorais, seguro.getCoberturaDanosMorais());
            BindData.fromString(campoCoberturaVidros, seguro.getCoberturaVidros());
            BindData.fromString(campoAssist24h, seguro.getAssistencia24H());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    private void vinculaSeguroVida(@NonNull final DespesaComSeguroDeVida seguro) {
        final TextView campoSocios = binding.fragSalarioDetalhesCoberturaSocios;
        final TextView campoMotoristas = binding.fragSalarioDetalhesCoberturaMotoristas;

        try {
            BindData.fromString(campoTitulo, SEGURO_DE_VIDA);
            BindData.R$fromBigDecimal(campoSocios, seguro.getCoberturaSocios());
            BindData.R$fromBigDecimal(campoMotoristas, seguro.getCoberturaMotoristas());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        controlador = Navigation.findNavController(requireView());
        configuraMenuProvider();
    }

    private void inicializaCamposDaView() {
        campoTitulo = binding.fragSalarioDetalhesTitulo;
    }

    private void configuraMenuProvider() {
        final SeguroInformacoesGeraisMenuProviderExt menuProviderExt =
                new SeguroInformacoesGeraisMenuProviderExt(viewModel.getTipoDeSeguro());

        requireActivity().addMenuProvider(menuProviderExt, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderExt.setListener(
                new SeguroInformacoesGeraisMenuProviderExt.SeguroInformacoesGeraisMenuProviderListener() {
                    @Override
                    public void onLogoutClick() {
                        requireActivity().setResult(RESULT_LOGOUT);
                        requireActivity().finish();
                    }

                    @Override
                    public void onHomeClick() {
                        controlador.popBackStack();
                    }

                    @Override
                    public void onEditClickQuandoSeguroFrota() {
                        final Intent intent = new Intent(requireContext(), FormulariosActivity.class);
                        intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_FROTA);
                        intent.putExtra(CHAVE_ID, viewModel.getSeguroFrota().getId());
                        intent.putExtra(CHAVE_REQUISICAO, EDITANDO);
                        activityResultLauncher.launch(intent);
                    }

                    @Override
                    public void onEditClickQuandoSeguroVida() {
                        final Intent intent = new Intent(requireContext(), FormulariosActivity.class);
                        intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_VIDA);
                        intent.putExtra(CHAVE_ID, viewModel.getSeguroVida().getId());
                        intent.putExtra(CHAVE_REQUISICAO, EDITANDO);
                        activityResultLauncher.launch(intent);
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

