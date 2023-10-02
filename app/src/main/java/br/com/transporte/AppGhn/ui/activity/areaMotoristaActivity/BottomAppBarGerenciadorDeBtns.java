package br.com.transporte.AppGhn.ui.activity.areaMotoristaActivity;

import android.graphics.Color;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BottomAppBarGerenciadorDeBtns {
    public static final String COR_BTN_SELECIONADO = "#E74C3C";
    public static final String COR_BTN_DESSELECIONADO = "#FFFFFF";
    private final int INDICE_EXIBIDO_INICIALMENTE;
    private final List<BottomAppBarBtn> listaDeBtns;
    private int ultimaPosicaoAcessada;

    public BottomAppBarGerenciadorDeBtns(int INDICE_EXIBIDO_INICIALMENTE) {
        this.INDICE_EXIBIDO_INICIALMENTE = INDICE_EXIBIDO_INICIALMENTE;
        this.ultimaPosicaoAcessada = INDICE_EXIBIDO_INICIALMENTE;
        this.listaDeBtns = new ArrayList<>();
    }

//-------------------------------------------------------

    public void setListaDeBtns(final BottomAppBarBtn... btns) {
        listaDeBtns.addAll(Arrays.asList(btns));
        insereCorNoBtnAbertoInicialmente();
    }

    private void insereCorNoBtnAbertoInicialmente() {
        for (BottomAppBarBtn b: listaDeBtns){
            if(b.getIndice() == INDICE_EXIBIDO_INICIALMENTE){
                insereCor(b);
            }
        }
    }

    public void mudaCorDaBottomAppBarEmTransicoes(final int novaPosicao) {
        if (ultimaPosicaoAcessada != novaPosicao) {
            final BottomAppBarBtn btnInicial = identificaBtnAtravesDoIndice(ultimaPosicaoAcessada);
            final BottomAppBarBtn novoBtn = identificaBtnAtravesDoIndice(novaPosicao);
            removeCor(btnInicial);
            insereCor(novoBtn);
            ultimaPosicaoAcessada = novaPosicao;
        }
    }

    private BottomAppBarBtn identificaBtnAtravesDoIndice(int posicao) {
        BottomAppBarBtn btn = null;
        for (BottomAppBarBtn b : listaDeBtns) {
            if (b.getIndice() == posicao) {
                btn = b;
            }
        }
        return btn;
    }

    private void insereCor(@NonNull final BottomAppBarBtn btn) {
        btn.getImgView().setColorFilter(Color.parseColor(COR_BTN_SELECIONADO));
        btn.getTxtView().setTextColor(Color.parseColor(COR_BTN_SELECIONADO));
    }

    private void removeCor(@NonNull final BottomAppBarBtn btn) {
        btn.getImgView().clearColorFilter();
        btn.getTxtView().setTextColor(Color.parseColor(COR_BTN_DESSELECIONADO));
    }

}

