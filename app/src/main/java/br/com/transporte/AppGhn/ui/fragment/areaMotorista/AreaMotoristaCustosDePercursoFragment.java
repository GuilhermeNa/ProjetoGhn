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
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaCustosDePercursoBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.CustosDePercursoAdapter;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class AreaMotoristaCustosDePercursoFragment extends Fragment implements DateRangePickerUtil.CallbackDatePicker{
    private FragmentAreaMotoristaCustosDePercursoBinding binding;
    private CustosDePercursoAdapter adapter;
    private CustosDePercursoDAO custosDao;
    private LinearLayout dataLayout, buscaVazia;
    private TextView dataInicialTxt, dataFinalTxt, despesaAcumuladaTxt;
    private LocalDate dataInicial, dataFinal;
    private Cavalo cavalo;
    private List<CustosDePercurso> listaDeCustos;
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
                            ui_totalCustos();
                            getParentFragmentManager().setFragmentResult(KEY_ACTION_ADAPTER, null);
                            break;

                        case RESULT_CANCELED:
                            atualizaAposResultado(NENHUMA_ALTERACAO_REALIZADA);
                            break;

                        case RESULT_DELETE:
                            atualizaAposResultado(REGISTRO_APAGADO);
                            ui_totalCustos();
                            getParentFragmentManager().setFragmentResult(KEY_ACTION_ADAPTER, null);
                            break;
                    }
                }
        );
    }

    private void atualizaAposResultado(String msg){
        listaDeCustos = getListaDeCustos();
        adapter.atualiza(listaDeCustos);
        MensagemUtil.toast(requireContext(), msg);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        custosDao = new CustosDePercursoDAO();
        configuracaoInicialDateRange();
        configuracaoInicialListaDeCustos();
    }

    private void configuracaoInicialListaDeCustos() {
        CavaloDAO cavaloDao = new CavaloDAO();
        int cavaloId = requireArguments().getInt(CHAVE_ID_CAVALO);
        cavalo = cavaloDao.localizaPeloId(cavaloId);
        listaDeCustos = getListaDeCustos();
    }

    private void configuracaoInicialDateRange() {
        dateRange = new DateRangePickerUtil(getParentFragmentManager());
        dataInicial = dateRange.getDataInicialEmLocalDate();
        dataFinal = dateRange.getDataFinalEmLocalDate();
    }

    @NonNull
    private List<CustosDePercurso> getListaDeCustos() {
        List<CustosDePercurso> lista = custosDao.listaFiltradaPorPlacaEData(cavalo.getId(), dataInicial, dataFinal);
        lista.sort(Comparator.comparing(CustosDePercurso::getData));
        Collections.reverse(lista);
        return lista;
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAreaMotoristaCustosDePercursoBinding.inflate(inflater, container, false);
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
        configuraUi();
        configuraDateRangePicker();
    }

    private void inicializaCampos() {
        recycler = binding.fragAreaMotoristaDespesaRecycler;
        despesaAcumuladaTxt = binding.fragDespesaValorTotal;
        dataLayout = binding.fragDespesaMes;
        dataInicialTxt = binding.fragDespesaMesDtInicial;
        dataFinalTxt = binding.fragDespesaMesDtFinal;
        buscaVazia = binding.fragDespesaVazio;
    }

    //-------------------------------------
    // -> Configura Recycler             ||
    //-------------------------------------

    private void configuraRecycler() {
        adapter = new CustosDePercursoAdapter(this, listaDeCustos);
        recycler.setAdapter(adapter);
        configuraRecyclerListener();
    }

    private void configuraRecyclerListener() {
        adapter.setOnItemClickListener((idDespesa) -> {
            Intent intent = new Intent(getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_ID, (Integer) idDespesa);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
            activityResultLauncher.launch(intent);
        });
    }

    //--------------------------------
    // -> Configura Ui              ||
    //--------------------------------

    private void configuraUi() {
        ui_data();
        ui_totalCustos();
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaDeCustos.size(), buscaVazia, recycler, "INVISIBLE");
    }

    private void ui_totalCustos() {
        BigDecimal somaCustos = CalculoUtil.somaCustosDePercurso(listaDeCustos);
        despesaAcumuladaTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaCustos));
    }

    private void ui_data() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    private void configuraDateRangePicker() {
      dateRange.build(dataLayout);
      dateRange.setCallbackDatePicker(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (atualizacaoSolicitadaPelaActivity) {
            atualizaUi();
            resetaSolicitacaoDeAtualizacao();
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaDeCustos.size(), buscaVazia, recycler, "INVISIBLE");
        }
    }

    private void resetaSolicitacaoDeAtualizacao() {
        atualizacaoSolicitadaPelaActivity = false;
    }

    public void atualizaUi() {
        listaDeCustos = getListaDeCustos();
        adapter.atualiza(listaDeCustos);
        ui_totalCustos();
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
        listaDeCustos = getListaDeCustos();
        adapter.atualiza(listaDeCustos);
        configuraUi();
    }

}
