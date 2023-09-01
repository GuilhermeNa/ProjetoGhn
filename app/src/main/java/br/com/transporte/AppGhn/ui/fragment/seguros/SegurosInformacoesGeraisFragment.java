package br.com.transporte.AppGhn.ui.fragment.seguros;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasSeguroDAO;
import br.com.transporte.AppGhn.databinding.FragmentSegurosInformacoesGeraisBinding;
import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class SegurosInformacoesGeraisFragment extends Fragment {


    private FragmentSegurosInformacoesGeraisBinding binding;
    private TextView tituloTxtView, subTxtView, dataInicioTxtView, dataFinalTxtView, valorTotalTxtView, parcelasQntTxtView, parcelaValorTxtView,
            companhiaTxtView, nContratoTxtView, coberturaCascoTxtView, coberturaRcfMateriaisTxtView, coberturaRcfCorporaisTxtView, coberturaAppMorteTxtView,
            coberturaAppInvalidezTxtView, coberturaVidrosTxtView, coberturaDanosMoraisTxtView, assistencia24HTxtView, sociosTxtView, motoristasTxtView;
    private NavController controlador;
    private DespesaComSeguro seguro;
    private Callback callback;
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
                            callback.atualizaFrotaAdapter();
                            break;

                        case RESULT_EDIT:
                            atualizaUiAposRetornoResult();
                            break;

                        case RESULT_CANCELED:
                            Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

    private void atualizaUiAposRetornoResult() {
        configuraUi(seguro);
        Toast.makeText(requireContext(), REGISTRO_EDITADO, Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DespesasSeguroDAO segurosDao = new DespesasSeguroDAO();
        seguro = recebeReferenciaExternaDeSeguro(segurosDao);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSegurosInformacoesGeraisBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        controlador = Navigation.findNavController(requireView());
        configuraUi(seguro);
        configuraToolbar();
    }

    private void configuraUi(@NonNull DespesaComSeguro seguro) {
        String sub = R.string.seguro_auto + " "+ seguro.getRefCavalo();
        subTxtView.setText(sub);
        tituloTxtView.setText(R.string.seguro_compreensivo);
        dataInicioTxtView.setText(ConverteDataUtil.dataParaString(seguro.getDataInicial()));
        dataFinalTxtView.setText(ConverteDataUtil.dataParaString(seguro.getDataFinal()));
        valorTotalTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguro.getValorDespesa()));
        parcelasQntTxtView.setText(String.valueOf(seguro.getParcelas()));
        parcelaValorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguro.getValorParcela()));
        companhiaTxtView.setText(seguro.getCompanhia());
        nContratoTxtView.setText(String.valueOf(seguro.getNContrato()));

        if (seguro instanceof DespesaComSeguroFrota) {
            LinearLayout seguroAutoLayout = binding.fragSalarioDetalhesLayoutCoberturasAuto;
            seguroAutoLayout.setVisibility(View.VISIBLE);

            DespesaComSeguroFrota seguroAuto = (DespesaComSeguroFrota) seguro;
            coberturaCascoTxtView.setText(seguroAuto.getCoberturaCasco());
            coberturaRcfMateriaisTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguroAuto.getCoberturaRcfMateriais()));
            coberturaRcfCorporaisTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguroAuto.getCoberturaRcfCorporais()));
            coberturaAppMorteTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguroAuto.getCoberturaAppMorte()));
            coberturaAppInvalidezTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguroAuto.getCoberturaAppInvalidez()));
            coberturaDanosMoraisTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguroAuto.getCoberturaDanosMorais()));
            coberturaVidrosTxtView.setText(seguroAuto.getCoberturaVidros());
            assistencia24HTxtView.setText(seguroAuto.getAssistencia24H());
        } else if (seguro instanceof DespesaComSeguroDeVida) {
            LinearLayout seguroVidaLayout = binding.fragSalarioDetalhesLayoutCoberturasVida;
            seguroVidaLayout.setVisibility(View.VISIBLE);

            DespesaComSeguroDeVida seguroDeVida = (DespesaComSeguroDeVida) seguro;
            sociosTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguroDeVida.getCoberturaSocios()));
            motoristasTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguroDeVida.getCoberturaMotoristas()));
            subTxtView.setText(R.string.seguro_de_vida);
            tituloTxtView.setText(R.string.demais_ramos);
        }
    }

    private DespesaComSeguro recebeReferenciaExternaDeSeguro(@NonNull DespesasSeguroDAO segurosDao) {
        int seguroId = (int) SegurosInformacoesGeraisFragmentArgs.fromBundle(getArguments()).getSeguroId();
        return segurosDao.localizaPeloId(seguroId);
    }

    private void inicializaCamposDaView() {
        tituloTxtView = binding.fragSalarioDetalhesTitulo;
        subTxtView = binding.fragSalarioDetalhesSub;
        dataInicioTxtView = binding.fragSalarioDetalhesInicioVigencia;
        dataFinalTxtView = binding.fragSalarioDetalhesFimVigencia;
        valorTotalTxtView = binding.fragSalarioDetalhesValorTotal;
        parcelasQntTxtView = binding.fragSalarioDetalhesParcelas;
        parcelaValorTxtView = binding.fragSalarioDetalhesValorParcela;
        companhiaTxtView = binding.fragSalarioDetalhesCompanhia;
        nContratoTxtView = binding.fragSalarioDetalhesNumeroContrato;

        coberturaCascoTxtView = binding.fragSalarioDetalhesCoberturaCasco;
        coberturaRcfMateriaisTxtView = binding.fragSalarioDetalhesCoberturaRcfMateriais;
        coberturaRcfCorporaisTxtView = binding.fragSalarioDetalhesCoberturaRcfCorporais;
        coberturaAppMorteTxtView = binding.fragSalarioDetalhesCoberturaAppMorte;
        coberturaAppInvalidezTxtView = binding.fragSalarioDetalhesCoberturaAppInvalidez;
        coberturaDanosMoraisTxtView = binding.fragSalarioDetalhesCoberturaDanosMorais;
        coberturaVidrosTxtView = binding.fragSalarioDetalhesCoberturaVidros;
        assistencia24HTxtView = binding.fragSalarioDetalhesCoberturaAssistencia;

        sociosTxtView = binding.fragSalarioDetalhesCoberturaSocios;
        motoristasTxtView = binding.fragSalarioDetalhesCoberturaMotoristas;

    }

    private void configuraToolbar() {
        String placa;
        if (seguro instanceof DespesaComSeguroFrota) {
            CavaloDAO cavaloDao = new CavaloDAO();
            placa = cavaloDao.localizaPeloId(seguro.getRefCavalo()).getPlaca();
        } else {
            placa = "-";
        }

        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(placa);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(new MenuProvider() {
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
                        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_padrao_editar:
                        Intent intent = new Intent(requireContext(), FormulariosActivity.class);

                        if (seguro instanceof DespesaComSeguroFrota) {
                            intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_FROTA);

                        } else if (seguro instanceof DespesaComSeguroDeVida) {
                            intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_VIDA);
                        }

                        intent.putExtra(CHAVE_REQUISICAO, EDITANDO);
                        intent.putExtra(CHAVE_ID, seguro.getId());
                        activityResultLauncher.launch(intent);
                        break;

                    case android.R.id.home:
                        controlador.popBackStack();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnAttach                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof Callback){
            callback = (Callback) context;
        } else {
            throw new ClassCastException();
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          Interface                                         ||
    //----------------------------------------------------------------------------------------------

    public interface Callback {
        void atualizaFrotaAdapter();
    }

}