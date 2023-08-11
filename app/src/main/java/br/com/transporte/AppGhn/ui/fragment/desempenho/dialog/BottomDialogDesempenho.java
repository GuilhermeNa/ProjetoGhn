package br.com.transporte.AppGhn.ui.fragment.desempenho.dialog;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.BottomLayoutDesempenhoBinding;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.ui.adapter.viewpager.ViewPagerDesempenhoAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.DatePickerUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class BottomDialogDesempenho {

    private BottomLayoutDesempenhoBinding binding;
    public static final String SELECIONA_TIPO = "seleciona_tipo";
    public static final String CHAVE_TIPO = "chave_tipo";
    private final Context context;
    private final Fragment fragment;
    private Callback callback;
    private int ano;
    private SwitchMaterial switchMaterial;
    private boolean validoParaFiltrar;
    private AutoCompleteTextView autoComplete;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public BottomDialogDesempenho(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showBottomDialog() {
        final Dialog dialog = getDialog();
        configuraParametrosDeExibicaoDoDialog(dialog);
        inicializaCamposDaView();
        configuraBtnCancela(dialog);
        configuraLayoutAno();
        configuraLayoutPlaca();
        configuraViewPager(dialog);
        configuraRequisicaoDeFiltragem(dialog);
    }

    private void configuraRequisicaoDeFiltragem(Dialog dialog) {
        fragment.getChildFragmentManager().setFragmentResultListener(SELECIONA_TIPO, this.fragment, (requestKey, result) -> {
            int cavaloId = getCavaloId();

            if (validoParaFiltrar) {
                TipoDeRequisicao tipo = (TipoDeRequisicao) result.getSerializable(CHAVE_TIPO);
                callback.filtragemConcluida(tipo, ano, cavaloId);
                dialog.dismiss();
            }
        });
    }

    private void inicializaCamposDaView() {
        switchMaterial = binding.switchBtn;
        autoComplete = binding.placa;
    }

    private void configuraBtnCancela(Dialog dialog) {
        ImageView cancela = binding.cancelButton;
        cancela.setOnClickListener(v -> dialog.dismiss());
    }

    private int getCavaloId() {
        validoParaFiltrar = true;

        if (switchMaterial.isChecked()) {
            return verificaValidadeDaPlaca();
        } else {
            return 0;
        }
    }

    private int verificaValidadeDaPlaca() {
        String placa = autoComplete.getText().toString();

        if (listaPlacas().contains(placa.toUpperCase(Locale.ROOT))) {

            CavaloDAO cavaloDao = new CavaloDAO();
            return cavaloDao.retornaCavaloAtravesDaPlaca(placa).getId();

        } else {

            MensagemUtil.toast(context, "Placa não encontrada.");
            validoParaFiltrar = false;
            return 0;

        }
    }

    private void configuraLayoutPlaca() {
        TextInputLayout placaLayout = binding.placaInputlayout;

        configuraDropDownMenuDeReferenciasParaCavalos();

        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                configuraVisualizacao(placaLayout, VISIBLE);

            } else {
                configuraVisualizacao(placaLayout, INVISIBLE);

            }
        });
    }

    private void configuraVisualizacao(TextInputLayout placaLayout, int visibilidade) {
        placaLayout.setVisibility(visibilidade);
        autoComplete.setVisibility(visibilidade);

        if (visibilidade == VISIBLE){
            AnimationUtil.defineAnimacao(context, R.anim.slide_in_right, placaLayout);
            AnimationUtil.defineAnimacao(context, R.anim.slide_in_right, autoComplete);
        } else if (visibilidade == INVISIBLE){
            AnimationUtil.defineAnimacao(context, R.anim.slide_out_right, placaLayout);
            AnimationUtil.defineAnimacao(context, R.anim.slide_out_right, autoComplete);
        }

    }

    private void configuraDropDownMenuDeReferenciasParaCavalos() {
        String[] cavalos = listaPlacas().toArray(new String[0]);
        ArrayAdapter<String> adapterCavalos = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cavalos);
        autoComplete.setAdapter(adapterCavalos);

    }

    private List<String> listaPlacas() {
        CavaloDAO cavaloDao = new CavaloDAO();
        return cavaloDao.listaPlacas();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraLayoutAno() {
        TextView anoTxtParaAnimacao = binding.anoAnimation;
        ImageView esquerdaImg = binding.esquerda;
        ImageView direitaImg = binding.direita;
        TextView anoTxt = binding.ano;

        int anoAtual = DatePickerUtil.capturaDataDeHojeParaConfiguracaoinicial().getYear();
        configuraAno(anoAtual, anoTxt);

        clickEsquerda(anoTxtParaAnimacao, esquerdaImg, anoTxt);
        clickDireita(anoTxtParaAnimacao, direitaImg, anoTxt, anoAtual);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clickDireita(TextView anoTxtParaAnimacao, ImageView direitaImg, TextView anoTxt, int anoAtual) {
        direitaImg.setOnClickListener(v -> {
            if (ano < anoAtual) {
                anoTxtParaAnimacao.setText(String.valueOf(ano));
                anoTxtParaAnimacao.setVisibility(VISIBLE);

                ano = ano + 1;
                configuraAno(ano, anoTxt);

                anoTxtParaAnimacao.setVisibility(View.GONE);
                AnimationUtil.defineAnimacao(context, R.anim.ano_slide_out_left, anoTxtParaAnimacao);
                AnimationUtil.defineAnimacao(context, R.anim.ano_slide_in_right, anoTxt);
            } else {
                Toast.makeText(this.context, "Data limite para busca", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clickEsquerda(TextView anoTxtParaAnimacao, ImageView esquerdaImg, TextView anoTxt) {
        esquerdaImg.setOnClickListener(v -> {
            anoTxtParaAnimacao.setText(String.valueOf(ano));
            anoTxtParaAnimacao.setVisibility(VISIBLE);

            ano = ano - 1;
            configuraAno(ano, anoTxt);

            anoTxtParaAnimacao.setVisibility(View.GONE);
            AnimationUtil.defineAnimacao(context, R.anim.ano_slide_out_right, anoTxtParaAnimacao);
            AnimationUtil.defineAnimacao(context, R.anim.ano_slide_in_left, anoTxt);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraAno(int ano, TextView anoTxt) {
        this.ano = ano;
        String anoEmString = Integer.toString(this.ano);
        anoTxt.setText(anoEmString);
    }

    private void configuraViewPager(Dialog dialog) {
        ViewPager2 viewPager = dialog.findViewById(R.id.pager);
        TabLayout tabLayout = dialog.findViewById(R.id.tab_layout);

        ViewPagerDesempenhoAdapter adapter = new ViewPagerDesempenhoAdapter(this.fragment);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {

                    switch (position) {
                        case 0:
                            tab.setText("Receita");
                            break;

                        case 1:
                            tab.setText("Custos");
                            break;

                        case 2:
                            tab.setText("Despesas");
                            break;
                    }
                }).attach();
    }

    private void configuraParametrosDeExibicaoDoDialog(Dialog dialog) {
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @NonNull
    private Dialog getDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = BottomLayoutDesempenhoBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());
        return dialog;
    }

    public interface Callback {
        void filtragemConcluida(TipoDeRequisicao tipo, int ano, int cavaloId);
    }


}

