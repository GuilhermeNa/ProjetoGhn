package br.com.transporte.AppGhn.ui.fragment.formularios;

import android.view.View;

public interface FormulariosInterface {

    void inicializaCamposDaView();

    <T> T criaOuRecuperaObjeto(int id);

    void alteraUiParaModoEdicao();

    void alteraUiParaModoCriacao();

    void exibeObjetoEmCasoDeEdicao();

    void aplicaMascarasAosEditTexts();

    void verificaSeCamposEstaoPreenchidos(View view);

    void vinculaDadosAoObjeto();

    void editaObjetoNoBancoDeDados();

    void deletaObjetoNoBancoDeDados();

    void adicionaObjetoNoBancoDeDados();

    int configuraObjetoNaCriacao();

}
