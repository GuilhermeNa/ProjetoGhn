package br.com.transporte.AppGhn.ui.activity.despesaAdm.viewmodel;

import static br.com.transporte.AppGhn.ui.activity.utilActivity.FabAnimatorUtil.FAB_ESTA_ESCONDIDO;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

import br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.DespesasAdmDiretasFragment;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.indireta.DespesasAdmIndiretasFragment;
import br.com.transporte.AppGhn.util.DataUtil;

public class DespesaAdmActViewModel extends ViewModel {

    private boolean statusDeVisibilidadeDoFab = FAB_ESTA_ESCONDIDO;
    private LocalDate dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
    private LocalDate dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
    private DespesasAdmDiretasFragment fragmentDespesaDireta;
    private DespesasAdmIndiretasFragment fragmentDespesaIndireta;


    //----------------------------------------------------------------------------------------------

    public void alteraStatusDeVisibilidadeDoFab() {
        statusDeVisibilidadeDoFab = !statusDeVisibilidadeDoFab;
    }

    public DespesasAdmDiretasFragment getFragmentDespesaDireta() {
        return fragmentDespesaDireta;
    }

    public void setFragmentDespesaDireta(DespesasAdmDiretasFragment fragmentDespesaDireta) {
        this.fragmentDespesaDireta = fragmentDespesaDireta;
    }

    public DespesasAdmIndiretasFragment getFragmentDespesaIndireta() {
        return fragmentDespesaIndireta;
    }

    public void setFragmentDespesaIndireta(DespesasAdmIndiretasFragment fragmentDespesaIndireta) {
        this.fragmentDespesaIndireta = fragmentDespesaIndireta;
    }

    public boolean statusDeVisibilidadeDoFab() {
        return statusDeVisibilidadeDoFab;
    }

    public void setStatusDeVisibilidadeDoFab(boolean statusDeVisibilidadeDoFab) {
        this.statusDeVisibilidadeDoFab = statusDeVisibilidadeDoFab;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

}
