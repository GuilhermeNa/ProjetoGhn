package br.com.transporte.AppGhn.ui.activity.seguro.viewmodel;

import static br.com.transporte.AppGhn.ui.activity.utilActivity.FabAnimatorUtil.FAB_ESTA_ESCONDIDO;

import androidx.lifecycle.ViewModel;

public class SeguroActViewModel extends ViewModel {
    private boolean statusDeVisibilidadeDoFab = FAB_ESTA_ESCONDIDO;

    //----------------------------------------------------------------------------------------------

    public void alteraStatusDeVisibilidadeDoFab() {
        statusDeVisibilidadeDoFab = !statusDeVisibilidadeDoFab;
    }

    public boolean statusDeVisibilidadeDoFab() {
        return statusDeVisibilidadeDoFab;
    }

}
