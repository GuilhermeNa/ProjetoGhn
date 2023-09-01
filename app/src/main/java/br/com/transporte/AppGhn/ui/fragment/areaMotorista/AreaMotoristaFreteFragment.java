package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;
import static br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaResumoFragment.KEY_ACTION_ADAPTER;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaFreteBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.FreteAdapter;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class AreaMotoristaFreteFragment extends Fragment implements DateRangePickerUtil.CallbackDatePicker {
    private FragmentAreaMotoristaFreteBinding binding;
    private FreteAdapter adapter;
    private FreteDAO freteDao;
    private LocalDate dataInicial, dataFinal;
    private TextView dataInicialTxt, dataFinalTxt, freteAcumuladoTxt;
    private LinearLayout dataLayout, buscaVazia;
    private List<Frete> listaDeFretes;
    private Cavalo cavalo;
    private DateRangePickerUtil dateRange;
    private RecyclerView recycler;
    private boolean atualizacaoSolicitadaPelaActivity = false;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch(resultCode){
                        case RESULT_EDIT:
                            atualizaAposResultado(REGISTRO_EDITADO);
                            ui_totalFreteLiquido();
                            getParentFragmentManager().setFragmentResult(KEY_ACTION_ADAPTER, null);
                            break;

                        case RESULT_CANCELED:
                            atualizaAposResultado(NENHUMA_ALTERACAO_REALIZADA);
                            break;

                        case RESULT_DELETE:
                        atualizaAposResultado(REGISTRO_APAGADO);
                            ui_totalFreteLiquido();
                            getParentFragmentManager().setFragmentResult(KEY_ACTION_ADAPTER, null);
                            break;
                    }
                }
        );
    }

    private void atualizaAposResultado(String msg){
        listaDeFretes = getListaDeFretes(cavalo);
        adapter.atualiza(listaDeFretes);
        MensagemUtil.toast(requireContext(), msg);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        freteDao = new FreteDAO();
        configuracaoInicialDateRange();
        configuracaoInicialListaDeFretes();
    }

    private void configuracaoInicialListaDeFretes() {
        CavaloDAO cavaloDao = new CavaloDAO();
        int cavaloId = requireArguments().getInt(CHAVE_ID_CAVALO);
        cavalo = cavaloDao.localizaPeloId(cavaloId);
        listaDeFretes = getListaDeFretes(cavalo);
    }

    private void configuracaoInicialDateRange() {
        dateRange = new DateRangePickerUtil(getParentFragmentManager());
        dataInicial = dateRange.getDataInicialEmLocalDate();
        dataFinal = dateRange.getDataFinalEmLocalDate();
    }

    @NonNull
    private List<Frete> getListaDeFretes(@NonNull Cavalo cavalo) {
        List<Frete> lista = freteDao.listaFiltradaPorCavaloEData(cavalo.getId(), dataInicial, dataFinal);
        lista.sort(Comparator.comparing(Frete::getData));
        Collections.reverse(lista);
        return lista;
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAreaMotoristaFreteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          onViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraRecycler();
        configuraDateRangePicker();
        configuraUi();
    }

    private void inicializaCampos() {
        recycler = binding.fragAreaMotoristaFreteRecycler;
        freteAcumuladoTxt = binding.fragFreteValorTotal;
        dataLayout = binding.fragFreteMes;
        dataInicialTxt = binding.fragFreteMesDtInicial;
        dataFinalTxt = binding.fragFreteMesDtFinal;
        buscaVazia = binding.fragFreteVazio;
    }

    private void configuraDateRangePicker() {
        dateRange.build(dataLayout);
        dateRange.setCallbackDatePicker(this);
    }

    //-------------------------------------
    // -> Configura Recycler             ||
    //-------------------------------------

    private void configuraRecycler() {
        adapter = new FreteAdapter(this, listaDeFretes);
        recycler.setAdapter(adapter);
        configuraRecyclerListener();
    }

    private void configuraRecyclerListener() {
        adapter.setOnItemClickListener((idFrete) -> {
            Intent intent = new Intent(getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_FRETE);
            intent.putExtra(CHAVE_ID, (Integer) idFrete);
            activityResultLauncher.launch(intent);
        });
    }

    //--------------------------------
    // -> Configura Ui              ||
    //--------------------------------

    private void configuraUi() {
        ui_data();
        ui_totalFreteLiquido();
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaDeFretes.size(), buscaVazia, recycler, "INVISIBLE");
    }

    private void ui_totalFreteLiquido() {
   //     BigDecimal freteLiquido = CalculoUtil.somaFreteLiquido(listaDeFretes);
   //     freteAcumuladoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(freteLiquido));
    }

    private void ui_data() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (atualizacaoSolicitadaPelaActivity) {
            atualizaUi();
            resetaSolicitacaoDeAtualizacao();
        }
    }

    private void resetaSolicitacaoDeAtualizacao() {
        atualizacaoSolicitadaPelaActivity = false;
    }

    private void atualizaUi(){
        listaDeFretes = getListaDeFretes(cavalo);
        adapter.atualiza(listaDeFretes);
        ui_totalFreteLiquido();
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaDeFretes.size(), buscaVazia, recycler, "INVISIBLE");
    }

    //----------------------------------------------------------------------------------------------
    //                                       Metodos publicos                                     ||
    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(){
        this.atualizacaoSolicitadaPelaActivity = true;
    }

    //----------------------------------------------------------------------------------------------
    //                                          CallBack                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void selecionaDataComSucesso(LocalDate dataInicial, LocalDate dataFinal) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        listaDeFretes = getListaDeFretes(cavalo);
        adapter.atualiza(listaDeFretes);
        configuraUi();
    }
}

