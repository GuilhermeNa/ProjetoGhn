package br.com.transporte.AppGhn.ui.fragment.media.dialog;

import static br.com.transporte.AppGhn.util.BigDecimalConstantes.BIG_DECIMAL_UM;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class MediaBottomDialog {
    private final Context context;
    private final CustosDeAbastecimento flag1, flag2;
    private final Cavalo cavalo;
    private TextView data01Txt, km01Txt, data02Txt, km02Txt, kmRodadoTxT, litrosTxt, freteTxt,
            abastecimentoTxt, comissaoTxt, custosPercursoTxt, percentualTxt, mediaTxt;

    public MediaBottomDialog(Context context, CustosDeAbastecimento flag1, CustosDeAbastecimento flag2, Cavalo cavalo) {
        this.context = context;
        this.flag1 = organizaPorDatasFlag1(flag1, flag2);
        this.flag2 = organizaPorDatasFlag2(flag1, flag2);
        this.cavalo = cavalo;
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
        ManipulaDados dados = new ManipulaDados(context, cavalo, flag1, flag2);

        ui_configuraPeriodoAnalisado(flag1, data01Txt, km01Txt);
        ui_configuraPeriodoAnalisado(flag2, data02Txt, km02Txt);

        BigDecimal kmRodado = dados.getKmRodadoNoIntervalo();
        ui_configuraNumeros(kmRodadoTxT, kmRodado);

        BigDecimal totalLitros = dados.getTotalDeLitrosUsadosNoIntervalo();
        ui_configuraNumeros(litrosTxt, totalLitros);

        BigDecimal frete = dados.getFreteBrutoAuferidoNoPeriodo();
        ui_configuraValores(freteTxt, frete);

        BigDecimal abastecimento = dados.getAbastecimentoAcumuladoNoPeriodo();
        ui_configuraValores(abastecimentoTxt, abastecimento);

        BigDecimal comissao = dados.getComissaoPagaNoPeriodo();
        ui_configuraValores(comissaoTxt, comissao);

        BigDecimal custosPercurso = dados.getCustosDePercursoAcumuladoNoPeriodo();
        ui_configuraValores(custosPercursoTxt, custosPercurso);

        BigDecimal media = dados.getMediaDoPeriodo();
        mediaTxt.setText(media.toPlainString());

        BigDecimal percentual = dados.getPercentualDeLucroDoPeriodo();
        String percentualEmString = percentual.toPlainString() + " %";
        percentualTxt.setText(percentualEmString);

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
    private final RoomFreteDao freteDao;
    private final RoomCustosAbastecimentoDao abastecimentoDao;
    private final RoomCustosPercursoDao custosPercursoDao;
    private final CustosDeAbastecimento flag1, flag2;
    private final Cavalo cavalo;

    public ManipulaDados(Context context, Cavalo cavalo, CustosDeAbastecimento flag1, CustosDeAbastecimento flag2) {
        this.cavalo = cavalo;
        this.flag1 = flag1;
        this.flag2 = flag2;
        GhnDataBase dataBase = GhnDataBase.getInstance(context);
        freteDao = dataBase.getRoomFreteDao();
        abastecimentoDao = dataBase.getRoomCustosAbastecimentoDao();
        custosPercursoDao = dataBase.getRoomCustosPercursoDao();
    }

    //----------------------------------------
    // -> Metodos                           ||
    //----------------------------------------

    protected BigDecimal getKmRodadoNoIntervalo() {
        BigDecimal kmFlag1 = flag1.getMarcacaoKm();
        BigDecimal kmFlag2 = flag2.getMarcacaoKm();

        return kmFlag2.subtract(kmFlag1);
    }

    protected BigDecimal getTotalDeLitrosUsadosNoIntervalo() {
   //     List<CustosDeAbastecimento> dataSet = FiltraCustosAbastecimento.listaPorCavaloId(abastecimentoDao.todos(), cavalo.getId());
    //   CustosDeAbastecimento primeiroAbastConsiderado = dataSet.get(dataSet.indexOf(flag1) + 1);
    //    dataSet = FiltraCustosAbastecimento.listaPorData(dataSet, primeiroAbastConsiderado.getData(), flag2.getData());
     //   return dataSet.stream()
       //         .map(CustosDeAbastecimento::getQuantidadeLitros)
         //       .reduce(BigDecimal.ZERO, BigDecimal::add);
        return BigDecimal.ZERO;
    }

    protected BigDecimal getFreteBrutoAuferidoNoPeriodo() {
      /*  List<Frete> fretes = FiltraFrete.listaPorCavaloId(freteDao.todos(), cavalo.getId());
        fretes = FiltraFrete.listaPorData(fretes, flag1.getData(), flag2.getData());
        return fretes.stream()
                .map(Frete::getFreteBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);*/
        return BigDecimal.ZERO;
    }

    protected BigDecimal getAbastecimentoAcumuladoNoPeriodo() {
    /*    List<CustosDeAbastecimento> abastecimentos = FiltraCustosAbastecimento.listaPorCavaloId(abastecimentoDao.todos(), cavalo.getId());
        abastecimentos = FiltraCustosAbastecimento.listaPorData(abastecimentos, flag1.getData(), flag2.getData());
        return abastecimentos.stream()
                .map(CustosDeAbastecimento::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);*/
        return BigDecimal.ZERO;
    }

    protected BigDecimal getComissaoPagaNoPeriodo() {
    /*    List<Frete> fretes = FiltraFrete.listaPorCavaloId(freteDao.todos(), cavalo.getId());
        fretes = FiltraFrete.listaPorData(fretes, flag1.getData(), flag2.getData());
        return fretes.stream()
                .map(Frete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);*/
        return BigDecimal.ZERO;
    }

    protected BigDecimal getCustosDePercursoAcumuladoNoPeriodo() {
  /*      List<CustosDePercurso> custoPercurso = FiltraCustosPercurso.listaPorCavaloId(custosPercursoDao.todos(), cavalo.getId());
        custoPercurso = FiltraCustosPercurso.listaPorData(custoPercurso, flag1.getData(), flag2.getData());
        return custoPercurso.stream()
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);*/
        return BigDecimal.ZERO;
    }

    protected BigDecimal getMediaDoPeriodo() {
        BigDecimal kmRodado = getKmRodadoNoIntervalo();
        BigDecimal litrosUsados = getTotalDeLitrosUsadosNoIntervalo();
        return kmRodado.divide(litrosUsados, 2, RoundingMode.HALF_EVEN);
    }

    protected BigDecimal getPercentualDeLucroDoPeriodo() {
        BigDecimal freteBruto = getFreteBrutoAuferidoNoPeriodo();
        BigDecimal abastecimento = getAbastecimentoAcumuladoNoPeriodo();
        BigDecimal comissao = getComissaoPagaNoPeriodo();
        BigDecimal custosPercurso = getCustosDePercursoAcumuladoNoPeriodo();

        BigDecimal custos = abastecimento
                .add(comissao)
                .add(custosPercurso);

        if (Objects.equals(freteBruto, BigDecimal.ZERO)) return BigDecimal.ZERO;
        else
            return BIG_DECIMAL_UM.subtract(
                    custos.divide(freteBruto, 2, RoundingMode.HALF_EVEN)
            );
    }
}
