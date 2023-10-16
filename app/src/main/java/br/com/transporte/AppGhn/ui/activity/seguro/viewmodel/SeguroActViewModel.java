package br.com.transporte.AppGhn.ui.activity.seguro.viewmodel;

import static br.com.transporte.AppGhn.ui.activity.utilActivity.FabAnimatorUtil.FAB_ESTA_ESCONDIDO;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

import br.com.transporte.AppGhn.util.DataUtil;

public class SeguroActViewModel extends ViewModel {
    private boolean statusDeVisibilidadeDoFab = FAB_ESTA_ESCONDIDO;
    private LocalDate dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
    private LocalDate dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();


    //----------------------------------------------------------------------------------------------

    public void alteraStatusDeVisibilidadeDoFab() {
        statusDeVisibilidadeDoFab = !statusDeVisibilidadeDoFab;
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
