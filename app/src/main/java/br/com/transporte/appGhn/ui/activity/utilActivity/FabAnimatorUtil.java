package br.com.transporte.appGhn.ui.activity.utilActivity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.transporte.AppGhn.R;

public class FabAnimatorUtil {
    public static final boolean FAB_ESTA_VISIVEL = true;
    public static final boolean FAB_ESTA_ESCONDIDO = false;
    private final FloatingActionButton fabEsquerda, fabDireita;
    private final Animation animacaoAbreFabDaEsquerda, animacaoFechaFabDaEsquerda,
            animacaoAbreFabDaDireita, animacaoFechaFabDaDireita;

    public FabAnimatorUtil(
            final Context context,
            @NonNull final FloatingActionButton fabEsquerda,
            @NonNull final FloatingActionButton fabDireita
    ) {
        this.fabEsquerda = fabEsquerda;
        this.fabDireita = fabDireita;
        animacaoAbreFabDaEsquerda = AnimationUtils.loadAnimation(context, R.anim.fab_animacao_noroeste_abre);
        animacaoFechaFabDaEsquerda = AnimationUtils.loadAnimation(context, R.anim.fab_animacao_noroeste_fecha);
        animacaoAbreFabDaDireita = AnimationUtils.loadAnimation(context, R.anim.fab_animacao_nordeste_abre);
        animacaoFechaFabDaDireita = AnimationUtils.loadAnimation(context, R.anim.fab_animacao_nordeste_fecha);
    }

    public interface FinalizaCallback {
        void finaliza();
    }

    //----------------------------------------------------------------------------------------------

    public void animaFabs(
            final boolean statusDeVisibilidadeDoFab,
            final FinalizaCallback callback
    ) {
        if (statusDeVisibilidadeDoFab == FAB_ESTA_ESCONDIDO) {
            abreFab(fabEsquerda, animacaoAbreFabDaEsquerda);
            abreFab(fabDireita, animacaoAbreFabDaDireita);
        } else if (statusDeVisibilidadeDoFab == FAB_ESTA_VISIVEL){
            fechaFab(fabEsquerda, animacaoFechaFabDaEsquerda);
            fechaFab(fabDireita, animacaoFechaFabDaDireita);
        }
        callback.finaliza();
    }

    private void abreFab(
            @NonNull final FloatingActionButton fab,
            final Animation animacao
    ) {
        fab.setVisibility(VISIBLE);
        fab.startAnimation(animacao);
    }

    private void fechaFab(
            @NonNull final FloatingActionButton fab,
            final Animation animacao
    ) {
        fab.setVisibility(INVISIBLE);
        fab.startAnimation(animacao);
    }

}
