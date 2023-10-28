package br.com.transporte.AppGhn.ui.fragment.media.dialog;

import static br.com.transporte.AppGhn.util.BigDecimalConstantes.BIG_DECIMAL_UM;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.repository.Resource;
import br.com.transporte.AppGhn.ui.fragment.media.viewmodel.MediaFragmentViewModel;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class MediaBottomDialog {
    private final Context context;
    private final CustosDeAbastecimento flag1, flag2;
    private final MediaFragmentViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;
    private TextView data01Txt, km01Txt, data02Txt, km02Txt, kmRodadoTxT, litrosTxt, freteTxt,
            abastecimentoTxt, comissaoTxt, custosPercursoTxt, percentualTxt, mediaTxt;

    public MediaBottomDialog(
            Context context,
            CustosDeAbastecimento flag1,
            CustosDeAbastecimento flag2,
            MediaFragmentViewModel viewModel,
            LifecycleOwner lifecycleOwner
    ) {
        this.context = context;
        this.flag1 = organizaPorDatasFlag1(flag1, flag2);
        this.flag2 = organizaPorDatasFlag2(flag1, flag2);
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    private CustosDeAbastecimento organizaPorDatasFlag2(@NonNull CustosDeAbastecimento flag1, @NonNull CustosDeAbastecimento flag2) {
        if (flag1.getData().isAfter(flag2.getData())) return flag1;
        return flag2;
    }

    @NonNull
    private CustosDeAbastecimento organizaPorDatasFlag1(@NonNull CustosDeAbastecimento flag1, @NonNull CustosDeAbastecimento flag2) {
        if (flag1.getData().isBefore(flag2.getData())) return flag1;
        return flag2;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Show                                              ||
    //----------------------------------------------------------------------------------------------

    public void showBottomDialog() {
        final Dialog dialog = getDialog();
        configuraParametrosDeExibicaoDoDialog(dialog);
        inicializaCamposDaView(dialog);
        configuraBtnCancela(dialog);
        ui_Configura();
    }

    private void configuraBtnCancela(@NonNull Dialog dialog) {
        ImageView btnCancel = dialog.findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void inicializaCamposDaView(@NonNull Dialog dialog) {
        mediaTxt = dialog.findViewById(R.id.media);
        percentualTxt = dialog.findViewById(R.id.percentual);
        data01Txt = dialog.findViewById(R.id.data_flag1);
        km01Txt = dialog.findViewById(R.id.km_flag1);
        data02Txt = dialog.findViewById(R.id.data_flag2);
        km02Txt = dialog.findViewById(R.id.km_flag2);
        kmRodadoTxT = dialog.findViewById(R.id.km_rodado);
        litrosTxt = dialog.findViewById(R.id.total_litros);
        freteTxt = dialog.findViewById(R.id.frete_bruto);
        abastecimentoTxt = dialog.findViewById(R.id.abastecimento);
        comissaoTxt = dialog.findViewById(R.id.comissao);
        custosPercursoTxt = dialog.findViewById(R.id.custos_percurso);
    }

    @NonNull
    private Dialog getDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.media_bottom_dialog);
        return dialog;
    }

    private void configuraParametrosDeExibicaoDoDialog(@NonNull Dialog dialog) {
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    //-----------------------------
    // -> Configura Ui           ||
    //-----------------------------

    private void ui_Configura() {
        ManipulaDados dados = new ManipulaDados(lifecycleOwner, viewModel, flag1, flag2);

        ui_configuraPeriodoAnalisado(flag1, data01Txt, km01Txt);
        ui_configuraPeriodoAnalisado(flag2, data02Txt, km02Txt);

        dados.run(new ManipulaDados.ValoresRefAbastecimentoCallback() {
            @Override
            public void kmRodado(BigDecimal kmRodado) {
                ui_configuraNumeros(kmRodadoTxT, kmRodado);
            }

            @Override
            public void litrosUsados(BigDecimal litros) {
                ui_configuraNumeros(litrosTxt, litros);
            }

            @Override
            public void custoComAbastecimento(BigDecimal valor) {
                ui_configuraValores(abastecimentoTxt, valor);
            }

            @Override
            public void freteAcumuladoNoPeriodo(BigDecimal valor) {
                ui_configuraValores(freteTxt, valor);
            }

            @Override
            public void comissaoPagaNoPeriodo(BigDecimal valor) {
                ui_configuraValores(comissaoTxt, valor);
            }

            @Override
            public void custosDePercurso(BigDecimal valor) {
                ui_configuraValores(custosPercursoTxt, valor);
            }

            @Override
            public void mediaDoPeriodo(BigDecimal media) {
                mediaTxt.setText(media.toPlainString());
            }

            @Override
            public void percentualDeLucro(BigDecimal valor) {
                final String percentualEmString = valor.toPlainString() + " %";
                percentualTxt.setText(percentualEmString);
            }
        });

    }

    private void ui_configuraNumeros(@NonNull TextView viewTxt, BigDecimal valor) {
        String valorFormatado = FormataNumerosUtil.formataNumero(valor);
        viewTxt.setText(valorFormatado);
    }

    private void ui_configuraValores(@NonNull TextView viewTxt, BigDecimal valor) {
        String valorFormatado = FormataNumerosUtil.formataMoedaPadraoBr(valor);
        viewTxt.setText(valorFormatado);
    }

    private void ui_configuraPeriodoAnalisado(@NonNull CustosDeAbastecimento flag, @NonNull TextView dataTxt, @NonNull TextView kmTxt) {
        String data = ConverteDataUtil.dataParaString(flag.getData());
        dataTxt.setText(data);

        String km = flag.getMarcacaoKm().toPlainString();
        kmTxt.setText(km);
    }

}

//--------------------------------------------------------------------------------------------------
//                                          Manipula Dados                                        ||
//--------------------------------------------------------------------------------------------------

class ManipulaDados {
    private final MediaFragmentViewModel viewModel;
    private final CustosDeAbastecimento flag1, flag2;
    private final LifecycleOwner lifecycleOwner;
    private BigDecimal kmRodadoNoIntervalo;
    private BigDecimal valorFrete;
    private BigDecimal valorComissao;
    private BigDecimal valorCustoDePercurso;
    private BigDecimal valorAbastecimentosNoIntervalo;

    public ManipulaDados(LifecycleOwner lifecycleOwner, MediaFragmentViewModel viewModel, CustosDeAbastecimento flag1, CustosDeAbastecimento flag2) {
        this.flag1 = flag1;
        this.flag2 = flag2;
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface ValoresRefAbastecimentoCallback {
        void kmRodado(BigDecimal kmRodado);

        void litrosUsados(BigDecimal litros);

        void custoComAbastecimento(BigDecimal valor);

        void freteAcumuladoNoPeriodo(BigDecimal valor);

        void comissaoPagaNoPeriodo(BigDecimal valor);

        void custosDePercurso(BigDecimal valor);

        void mediaDoPeriodo(BigDecimal media);

        void percentualDeLucro(BigDecimal valor);

    }

    //----------------------------------------
    // -> Metodos                           ||
    //----------------------------------------

    protected void run(final ValoresRefAbastecimentoCallback callback) {
        getKmRodadoNoIntervalo(callback);
        buscaValoresRefAbastecimento(callback);
        buscaValoresRefFrete(callback);
        getCustosDePercursoAcumuladoNoPeriodo(callback);

        final Handler r = new Handler();
        r.postDelayed(
                () -> {
                    getPercentualDeLucroDoPeriodo(callback);
                }, 500);

    }

    private void getKmRodadoNoIntervalo(@NonNull final ValoresRefAbastecimentoCallback callback) {
        final BigDecimal kmFlag1 = flag1.getMarcacaoKm();
        final BigDecimal kmFlag2 = flag2.getMarcacaoKm();
        kmRodadoNoIntervalo = kmFlag2.subtract(kmFlag1);
        callback.kmRodado(kmRodadoNoIntervalo);
    }

    private void buscaValoresRefAbastecimento(final ValoresRefAbastecimentoCallback callback) {
        final LiveData<Resource<List<CustosDeAbastecimento>>> observer = viewModel.carregaAbastecimentosPorCavaloId();
        observer.observe(lifecycleOwner,
                new Observer<Resource<List<CustosDeAbastecimento>>>() {
                    @Override
                    public void onChanged(final Resource<List<CustosDeAbastecimento>> resource) {
                        observer.removeObserver(this);
                        getLitrosUsadosNoPercurso(resource.getDado());
                        getCustoComAbastecimentoDoPeriodo(resource.getDado());
                        getMediaDoPeriodo();
                    }

                    private void getLitrosUsadosNoPercurso(@NonNull List<CustosDeAbastecimento> custosDeAbastecimentos) {
                        CustosDeAbastecimento primeiroAbastConsiderado;
                        try{
                            primeiroAbastConsiderado = custosDeAbastecimentos.get(custosDeAbastecimentos.indexOf(flag1) + 1);
                        } catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                            primeiroAbastConsiderado = custosDeAbastecimentos.get(custosDeAbastecimentos.indexOf(flag1));
                        }

                        custosDeAbastecimentos = FiltraCustosAbastecimento.listaPorData(custosDeAbastecimentos, primeiroAbastConsiderado.getData(), flag2.getData());

                        final BigDecimal valor = custosDeAbastecimentos.stream()
                                .map(CustosDeAbastecimento::getQuantidadeLitros)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        callback.litrosUsados(valor);

                    }

                    private void getCustoComAbastecimentoDoPeriodo(List<CustosDeAbastecimento> custosDeAbastecimentos) {
                        custosDeAbastecimentos = FiltraCustosAbastecimento.listaPorData(custosDeAbastecimentos, flag1.getData(), flag2.getData());

                        valorAbastecimentosNoIntervalo = custosDeAbastecimentos.stream()
                                .map(CustosDeAbastecimento::getValorCusto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        callback.custoComAbastecimento(valorAbastecimentosNoIntervalo);
                    }

                    private void getMediaDoPeriodo() {
                        BigDecimal media;
                        try{
                            media = kmRodadoNoIntervalo
                                    .divide(valorAbastecimentosNoIntervalo, 2, RoundingMode.HALF_EVEN);
                        } catch (ArithmeticException e){
                            media = BigDecimal.ZERO;
                        }
                        callback.mediaDoPeriodo(media);
                    }
                });
    }

    private void buscaValoresRefFrete(final ValoresRefAbastecimentoCallback callback) {
        final LiveData<List<Frete>> observer = viewModel.carregaFretes(flag1.getData(), flag2.getData());
        observer.observe(lifecycleOwner, new Observer<List<Frete>>() {

            @Override
            public void onChanged(List<Frete> fretes) {
                observer.removeObserver(this);
                getFreteAuferidoNoPeriodo(fretes);
                getComissaoPagaNoPeriodo(fretes);
            }

            private void getComissaoPagaNoPeriodo(@NonNull final List<Frete> fretes) {
                valorComissao = fretes.stream()
                        .map(Frete::getComissaoAoMotorista)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                callback.comissaoPagaNoPeriodo(valorComissao);
            }

            private void getFreteAuferidoNoPeriodo(@NonNull final List<Frete> fretes) {
                valorFrete = fretes.stream()
                        .map(Frete::getFreteBruto)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                callback.freteAcumuladoNoPeriodo(valorFrete);
            }
        });
    }

    private void getCustosDePercursoAcumuladoNoPeriodo(final ValoresRefAbastecimentoCallback callback) {
        final LiveData<List<CustosDePercurso>> observer = viewModel.carregaCustosDePercurso(flag1.getData(), flag2.getData());
        observer.observe(lifecycleOwner, new Observer<List<CustosDePercurso>>() {
            @Override
            public void onChanged(List<CustosDePercurso> custosDePercurso) {
                observer.removeObserver(this);

                valorCustoDePercurso = custosDePercurso.stream()
                        .map(CustosDePercurso::getValorCusto)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                callback.custosDePercurso(valorCustoDePercurso);
            }
        });
    }

    private void getPercentualDeLucroDoPeriodo(final ValoresRefAbastecimentoCallback callback) {
        final BigDecimal custos = valorAbastecimentosNoIntervalo
                .add(valorComissao)
                .add(valorCustoDePercurso);

        if (Objects.equals(valorFrete, BigDecimal.ZERO))
            callback.percentualDeLucro(BigDecimal.ZERO);
        else
            callback.percentualDeLucro(BIG_DECIMAL_UM.subtract(custos.divide(valorFrete, 2, RoundingMode.HALF_EVEN))
            );
    }

}
