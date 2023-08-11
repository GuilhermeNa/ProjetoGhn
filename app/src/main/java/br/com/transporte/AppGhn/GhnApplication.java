package br.com.transporte.AppGhn;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete.ADIANTAMENTO;
import static br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete.SALDO;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.exception.ValorInvalidoException;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.despesas.ParcelaDeSeguro;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.model.enums.TipoAbastecimento;
import br.com.transporte.AppGhn.model.enums.TipoCertificado;
import br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.AppGhn.model.enums.TipoCustoManutencao;
import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasCertificadoDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.dao.CustosDeManutencaoDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.dao.DespesasSeguroDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.MotoristaDAO;
import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.dao.RecebimentoFreteDAO;
import br.com.transporte.AppGhn.dao.SemiReboqueDAO;
import br.com.transporte.AppGhn.util.FormataDataUtil;

public class GhnApplication extends Application {
    private FreteDAO freteDao = new FreteDAO();
    private CustosDePercursoDAO despesaDao = new CustosDePercursoDAO();
    private CustosDeAbastecimentoDAO abastecimentoDao = new CustosDeAbastecimentoDAO();
    private MotoristaDAO motoristaDao = new MotoristaDAO();
    private CavaloDAO cavaloDao = new CavaloDAO();
    private SemiReboqueDAO srDao = new SemiReboqueDAO();
    private DespesasCertificadoDAO certificadoDAO = new DespesasCertificadoDAO();
    private CustosDeManutencaoDAO manutencaoDao = new CustosDeManutencaoDAO();
    private DespesasAdmDAO despesaFinanceiraDao = new DespesasAdmDAO();
    private DespesasSeguroDAO segurosDao = new DespesasSeguroDAO();
    private RecebimentoFreteDAO recebimentoDao = new RecebimentoFreteDAO();
    ParcelaDeSeguroDAO parcelaSeguroDao = new ParcelaDeSeguroDAO();
    private Motorista m1, m2;
    private Cavalo cavalo1, cavalo2;
    private SemiReboque sr1, sr2, sr3, sr4;
    private Frete frete1, frete2;
    private CustosDePercurso despesa1, despesa2, despesa3, despesa4;
    private AdiantamentoDAO adiantamentoDao = new AdiantamentoDAO();
    private Frete frete3;
    private Frete frete4;
    private Frete frete5;
    private Frete frete6;
    private Frete frete7;
    private Frete frete8;
    private Frete frete9;
    private Frete frete10;
    private Frete frete11;
    private Frete frete12;
    private Frete frete13;
    private Frete frete14;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        criaMotoristas();
        criaSemiReboques();
        criaCavalos();
        try {
            criaFretes();
        } catch (ValorInvalidoException e) {
            e.printStackTrace();
        }
        criaAbastecimentos();
        criaDespesas();
        criaAdiantamentos();
        criaCertificado();
        criaCustosDemanutencao();
        criaDespesasFinanceiras();
        criaDespesasComSeguros();
        criaRecebimentoDeFrete();

        List<SemiReboque> lista1 = srDao.listaPorIdDeCavalo(1);
        List<SemiReboque> lista2 = srDao.listaPorIdDeCavalo(2);

        criaParcelas();

        //cria instancia do banco de dados para utilizar desta forma
        /*RoomCavaloDAO dao = Room.databaseBuilder(this, GhnDatabase.class, "ghn.db")
                .build().getRoomCavaloDAO();*/

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaParcelas() {
        ParcelaDeSeguro parcela1 = new ParcelaDeSeguro(1, FormataDataUtil.stringParaData("20/07/23"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela2 = new ParcelaDeSeguro(2, FormataDataUtil.stringParaData("20/08/23"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela3 = new ParcelaDeSeguro(3, FormataDataUtil.stringParaData("20/09/23"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela4 = new ParcelaDeSeguro(4, FormataDataUtil.stringParaData("20/10/23"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela5 = new ParcelaDeSeguro(5, FormataDataUtil.stringParaData("20/11/23"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela6 = new ParcelaDeSeguro(6, FormataDataUtil.stringParaData("20/12/23"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela7 = new ParcelaDeSeguro(7, FormataDataUtil.stringParaData("20/01/24"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela8 = new ParcelaDeSeguro(8, FormataDataUtil.stringParaData("20/02/24"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela9 = new ParcelaDeSeguro(9, FormataDataUtil.stringParaData("20/03/24"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela10 = new ParcelaDeSeguro(10, FormataDataUtil.stringParaData("20/04/24"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela11 = new ParcelaDeSeguro(11, FormataDataUtil.stringParaData("20/05/24"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);
        ParcelaDeSeguro parcela12 = new ParcelaDeSeguro(12, FormataDataUtil.stringParaData("20/06/24"), new BigDecimal("1666.66"), true,1, false, DIRETA,1);

        parcelaSeguroDao.adiciona(parcela1);
        parcelaSeguroDao.adiciona(parcela2);
        parcelaSeguroDao.adiciona(parcela3);
        parcelaSeguroDao.adiciona(parcela4);
        parcelaSeguroDao.adiciona(parcela5);
        parcelaSeguroDao.adiciona(parcela6);
        parcelaSeguroDao.adiciona(parcela7);
        parcelaSeguroDao.adiciona(parcela8);
        parcelaSeguroDao.adiciona(parcela9);
        parcelaSeguroDao.adiciona(parcela10);
        parcelaSeguroDao.adiciona(parcela11);
        parcelaSeguroDao.adiciona(parcela12);

        ParcelaDeSeguro parcela13 = new ParcelaDeSeguro(1, FormataDataUtil.stringParaData("20/08/23"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela14 = new ParcelaDeSeguro(2, FormataDataUtil.stringParaData("20/09/23"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela15 = new ParcelaDeSeguro(3, FormataDataUtil.stringParaData("20/10/23"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela16 = new ParcelaDeSeguro(4, FormataDataUtil.stringParaData("20/11/23"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela17 = new ParcelaDeSeguro(5, FormataDataUtil.stringParaData("20/12/23"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela18 = new ParcelaDeSeguro(6, FormataDataUtil.stringParaData("20/01/24"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela19 = new ParcelaDeSeguro(7, FormataDataUtil.stringParaData("20/02/24"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela20 = new ParcelaDeSeguro(8, FormataDataUtil.stringParaData("20/03/24"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela21 = new ParcelaDeSeguro(9, FormataDataUtil.stringParaData("20/04/24"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela22 = new ParcelaDeSeguro(10, FormataDataUtil.stringParaData("20/05/24"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela23 = new ParcelaDeSeguro(11, FormataDataUtil.stringParaData("20/06/24"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);
        ParcelaDeSeguro parcela24 = new ParcelaDeSeguro(12, FormataDataUtil.stringParaData("20/07/24"), new BigDecimal("800.00"), true,3, false, INDIRETA,0);

        parcelaSeguroDao.adiciona(parcela13);
        parcelaSeguroDao.adiciona(parcela14);
        parcelaSeguroDao.adiciona(parcela15);
        parcelaSeguroDao.adiciona(parcela16);
        parcelaSeguroDao.adiciona(parcela17);
        parcelaSeguroDao.adiciona(parcela18);
        parcelaSeguroDao.adiciona(parcela19);
        parcelaSeguroDao.adiciona(parcela20);
        parcelaSeguroDao.adiciona(parcela21);
        parcelaSeguroDao.adiciona(parcela22);
        parcelaSeguroDao.adiciona(parcela23);
        parcelaSeguroDao.adiciona(parcela24);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaRecebimentoDeFrete() {
        RecebimentoDeFrete recebimento = new RecebimentoDeFrete(FormataDataUtil.stringParaData("01/06/23"),
                new BigDecimal("10000.00"), "Ref. frete xyz bla bla bla", ADIANTAMENTO);

        RecebimentoDeFrete recebimento1 = new RecebimentoDeFrete(FormataDataUtil.stringParaData("01/06/23"),
                new BigDecimal("3900.00"), "Ref. frete xyz bla bla bla", SALDO);

        RecebimentoDeFrete recebimento2 = new RecebimentoDeFrete(FormataDataUtil.stringParaData("01/06/23"),
                new BigDecimal("10000.00"), "Ref. frete xyz bla bla bla", ADIANTAMENTO);

        recebimento.setRefFrete(1);
        recebimento1.setRefFrete(1);
        recebimento1.setRefFrete(2);

        recebimentoDao.adiciona(recebimento);
        recebimentoDao.adiciona(recebimento1);
        recebimentoDao.adiciona(recebimento2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaDespesasComSeguros() {
        DespesaComSeguroFrota seguros = new DespesaComSeguroFrota(FormataDataUtil.stringParaData("01/01/23"),
                FormataDataUtil.stringParaData("01/01/24"),
                FormataDataUtil.stringParaData("10/01/23"),
                new BigDecimal("20000.00"),
                12,
                new BigDecimal("1666.66"),
                "SegTruck",
                123456,
                "100% Fipe",
                new BigDecimal("200000.00"),
                new BigDecimal("200000.00"),
                new BigDecimal("20000.00"),
                new BigDecimal("20000.00"),
                new BigDecimal("10000.00"),
                "Sim", "Sim", 1);

        DespesaComSeguroFrota seguros1 = new DespesaComSeguroFrota(FormataDataUtil.stringParaData("01/01/23"),
                FormataDataUtil.stringParaData("01/01/24"),
                FormataDataUtil.stringParaData("10/01/23"),
                new BigDecimal("20000.00"),
                12,
                new BigDecimal("1666.66"),
                "SegTruck",
                123456,
                "100% Fipe",
                new BigDecimal("200000.00"),
                new BigDecimal("200000.00"),
                new BigDecimal("20000.00"),
                new BigDecimal("20000.00"),
                new BigDecimal("10000.00"),
                "Sim", "Sim", 2);
        seguros.setValido(true);
        seguros1.setValido(true);

        segurosDao.adiciona(seguros);
        segurosDao.adiciona(seguros1);

        DespesaComSeguroDeVida seguro2 = new DespesaComSeguroDeVida(FormataDataUtil.stringParaData("01/01/23"),
                FormataDataUtil.stringParaData("01/01/24"), FormataDataUtil.stringParaData("10/01/23"), new BigDecimal("800.00"), new BigDecimal("10000.00"),
                "Seguradora X", true, 12, 123456, 0,
                new BigDecimal("500000.00"),
                new BigDecimal("100000.00"),
                new BigDecimal("100000.00"));
        segurosDao.adiciona(seguro2);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaDespesasFinanceiras() {
        DespesaAdm despesaAdm = new DespesaAdm(FormataDataUtil.stringParaData("10/08/23"),
                new BigDecimal("5000.00"), "Financiamento OUX5555", 1);
        DespesaAdm despesaAdm1 = new DespesaAdm(FormataDataUtil.stringParaData("10/08/23"),
                new BigDecimal("5000.00"), "Financiamento RIF4E33", 2);
        DespesaAdm despesaAdm2 = new DespesaAdm(FormataDataUtil.stringParaData("10/08/23"),
                new BigDecimal("500.00"), "Contabilidade", 0);


        despesaAdm.setTipoDespesa(DIRETA);
        despesaAdm1.setTipoDespesa(DIRETA);
        despesaAdm2.setTipoDespesa(INDIRETA);
        despesaFinanceiraDao.adiciona(despesaAdm);
        despesaFinanceiraDao.adiciona(despesaAdm1);
        despesaFinanceiraDao.adiciona(despesaAdm2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaCustosDemanutencao() {
        CustosDeManutencao manutencao = new CustosDeManutencao(FormataDataUtil.stringParaData("01/07/23"), new BigDecimal("500.00"),
                "Cebolão", "Manutenção no filtro do ar", "1234586");
        CustosDeManutencao manutencao1 = new CustosDeManutencao(FormataDataUtil.stringParaData("02/07/23"), new BigDecimal("500.00"),
                "Cebolão", "Manutenção no eixo", "1234586");
        CustosDeManutencao manutencao2 = new CustosDeManutencao(FormataDataUtil.stringParaData("03/07/23"), new BigDecimal("500.00"),
                "Cebolão", "Manutenção no para-choque", "1234586");
        CustosDeManutencao manutencao3 = new CustosDeManutencao(FormataDataUtil.stringParaData("04/07/23"), new BigDecimal("500.00"),
                "Cebolão", "Manutenção no para-brisas", "1234586");
        manutencao.setRefCavalo(1);
        manutencao1.setRefCavalo(1);
        manutencao2.setRefCavalo(2);
        manutencao3.setRefCavalo(2);

        manutencao.setTipoCustoManutencao(TipoCustoManutencao.PERIODICA);
        manutencao1.setTipoCustoManutencao(TipoCustoManutencao.EXTRAORDINARIA);
        manutencao2.setTipoCustoManutencao(TipoCustoManutencao.PERIODICA);
        manutencao3.setTipoCustoManutencao(TipoCustoManutencao.EXTRAORDINARIA);

        manutencaoDao.adiciona(manutencao);
        manutencaoDao.adiciona(manutencao1);
        manutencaoDao.adiciona(manutencao2);
        manutencaoDao.adiciona(manutencao3);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaCertificado() {
        DespesaCertificado certificado = new DespesaCertificado(TipoCertificado.CRLV, "2023", 1234345567,
                FormataDataUtil.stringParaData("14/06/23"), FormataDataUtil.stringParaData("13/07/23")
                , 1, new BigDecimal("200.00"), DIRETA, true);

        DespesaCertificado certificado1 = new DespesaCertificado(TipoCertificado.CRONOTACOGRAFO, "2022", 1234567,
                FormataDataUtil.stringParaData("14/06/22"), FormataDataUtil.stringParaData("14/06/22")
                , 1, new BigDecimal("200.00"), DIRETA, false);

        DespesaCertificado certificado2 = new DespesaCertificado(TipoCertificado.CRLV, "2023", 1234567,
                FormataDataUtil.stringParaData("14/06/23"), FormataDataUtil.stringParaData("10/06/23")
                , 2, new BigDecimal("200.00"), DIRETA, false);

        DespesaCertificado certificado3 = new DespesaCertificado(TipoCertificado.CRONOTACOGRAFO, "2022", 1234567,
                FormataDataUtil.stringParaData("14/06/22"), FormataDataUtil.stringParaData("14/06/24")
                , 2, new BigDecimal("200.00"), DIRETA, true);

        certificadoDAO.adiciona(certificado);
        certificadoDAO.adiciona(certificado1);
        certificadoDAO.adiciona(certificado2);
        certificadoDAO.adiciona(certificado3);

        DespesaCertificado certificado4 = new DespesaCertificado(TipoCertificado.ANTT, "2023", 1234345567,
                FormataDataUtil.stringParaData("14/06/23"), FormataDataUtil.stringParaData("13/06/24")
                , 0, new BigDecimal("200.00"), INDIRETA, true);

        DespesaCertificado certificado5 = new DespesaCertificado(TipoCertificado.ANTT, "2022", 1234567,
                FormataDataUtil.stringParaData("14/06/22"), FormataDataUtil.stringParaData("14/06/23")
                , 0, new BigDecimal("200.00"), INDIRETA, false);
        certificadoDAO.adiciona(certificado4);
        certificadoDAO.adiciona(certificado5);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaAdiantamentos() {
        Adiantamento a1 = new Adiantamento(FormataDataUtil.stringParaData("09/06/23"),
                new BigDecimal("100.00"), "blablablablablalba", 1, 1);
        a1.setAdiantamentoJaFoiPago(false);
        a1.setRefCavalo(cavalo1.getId());
        a1.setRefMotorista(cavalo1.getMotorista().getId());
        a1.setSaldoRestituido(new BigDecimal("0.0"));

        Adiantamento a2 = new Adiantamento(FormataDataUtil.stringParaData("09/06/23"),
                new BigDecimal("100.00"), "blablablablablalba", 1, 1);
        a2.setAdiantamentoJaFoiPago(false);
        a2.setRefCavalo(cavalo1.getId());
        a2.setRefMotorista(cavalo1.getMotorista().getId());
        a2.setSaldoRestituido(new BigDecimal("0.0"));

        Adiantamento a3 = new Adiantamento(FormataDataUtil.stringParaData("09/06/23"),
                new BigDecimal("100.00"), "blablablablablalba", 2, 2);
        a3.setAdiantamentoJaFoiPago(false);
        a3.setRefCavalo(cavalo2.getId());
        a3.setRefMotorista(cavalo2.getMotorista().getId());
        a3.setSaldoRestituido(new BigDecimal("0.0"));

        adiantamentoDao.adiciona(a1);
        adiantamentoDao.adiciona(a2);
        adiantamentoDao.adiciona(a3);
    }

    private void criaSemiReboques() {
        sr1 = new SemiReboque("PJB1111", "RodoCaçamba D", "2011",
                "2012", "preto", "12345678911", "2G58745TRYUI934HJ", 1);

        sr2 = new SemiReboque("PJB2222", "Dollie123", "2011",
                "2012", "preto", "12345678911", "2G58745TRYUI934HJ", 1);

        sr3 = new SemiReboque("PJB3333", "RodoCaçamba T", "2011",
                "2012", "preto", "12345678911", "2G58745TRYUI934HJ", 2);

        sr4 = new SemiReboque("MSV1245", "Graneleiro", "2011",
                "2012", "preto", "12345678911", "2G58745TRYUI934HJ", 2);

        srDao.adiciona(sr1);
        srDao.adiciona(sr2);
        srDao.adiciona(sr3);
        srDao.adiciona(sr4);
    }

    private void criaCavalos() {
        cavalo1 = new Cavalo("SBF0A17",
                "123456", "123456789123456",
                "2010", "2011",
                "Volvo FH540",
                "Vermelho",
                "6x4", m1,
                new BigDecimal("11.00"), true);
        cavalo1.adicionaSemiReboque(sr1);
        cavalo1.adicionaSemiReboque(sr2);

        cavalo2 = new Cavalo("RIF6A07",
                "123456", "123456789123456",
                "2010", "2011",
                "Scania R450",
                "Vermelho",
                "6x4", m2,
                new BigDecimal("12.00"), true);
        cavalo2.adicionaSemiReboque(sr3);
        cavalo2.adicionaSemiReboque(sr4);

        cavaloDao.adiciona(cavalo1);
        cavaloDao.adiciona(cavalo2);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaDespesas() {
        despesa1 = new CustosDePercurso(FormataDataUtil.stringParaData("01/07/23"), new BigDecimal("23"), "Oleo hidraulico", TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO, 1);
        despesa2 = new CustosDePercurso(FormataDataUtil.stringParaData("02/07/23"), new BigDecimal("70"), "Guarda entre Pará e Ceará", TipoCustoDePercurso.NAO_REEMBOLSAVEL, 1);
        despesa3 = new CustosDePercurso(FormataDataUtil.stringParaData("03/07/23"), new BigDecimal("123"), "Faixa refletiva", TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO, 2);
        despesa4 = new CustosDePercurso(FormataDataUtil.stringParaData("04/07/23"), new BigDecimal("270"), "Balsa", TipoCustoDePercurso.REEMBOLSAVEL_JA_PAGO, 2);
        despesaDao.adiciona(despesa1);
        despesaDao.adiciona(despesa2);
        despesaDao.adiciona(despesa3);
        despesaDao.adiciona(despesa4);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaAbastecimentos() {
        CustosDeAbastecimento abastecimento1 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("01/01/23"), "Dito", new BigDecimal("10000.00"), new BigDecimal("665.5"),
                new BigDecimal("5.20"), new BigDecimal("3460.60"), TipoAbastecimento.TOTAL, 1, true);

        CustosDeAbastecimento abastecimento2 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("02/02/23"), "Boiadeiro", new BigDecimal("15000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("1935.02"), TipoAbastecimento.PARCIAL, 1, false);

        CustosDeAbastecimento abastecimento3 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("05/03/23"), "Boiadeiro", new BigDecimal("16000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("2795.02"), TipoAbastecimento.PARCIAL, 1, false);

        CustosDeAbastecimento abastecimento4 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("07/04/23"), "Boiadeiro", new BigDecimal("20000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("5035.02"), TipoAbastecimento.TOTAL, 1, true);

        CustosDeAbastecimento abastecimento5 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("07/05/23"), "Boiadeiro", new BigDecimal("30000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("1835.02"), TipoAbastecimento.PARCIAL, 1, false);

        CustosDeAbastecimento abastecimento6 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("07/06/23"), "Boiadeiro", new BigDecimal("40000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("3235.02"), TipoAbastecimento.PARCIAL, 1, false);

        CustosDeAbastecimento abastecimento7 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("07/07/23"), "Boiadeiro", new BigDecimal("55000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("3935.02"), TipoAbastecimento.TOTAL, 1, true);

        abastecimentoDao.adiciona(abastecimento1);
        abastecimentoDao.adiciona(abastecimento2);
        abastecimentoDao.adiciona(abastecimento3);
        abastecimentoDao.adiciona(abastecimento4);
        abastecimentoDao.adiciona(abastecimento5);
        abastecimentoDao.adiciona(abastecimento6);
        abastecimentoDao.adiciona(abastecimento7);

        CustosDeAbastecimento abastecimento8 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("01/01/23"), "Dito", new BigDecimal("10000.00"), new BigDecimal("665.5"),
                new BigDecimal("5.20"), new BigDecimal("3460.60"), TipoAbastecimento.TOTAL, 2, false);

        CustosDeAbastecimento abastecimento9 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("02/02/23"), "Boiadeiro", new BigDecimal("15000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("1935.02"), TipoAbastecimento.PARCIAL, 2, true);

        CustosDeAbastecimento abastecimento10 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("05/03/23"), "Boiadeiro", new BigDecimal("16000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("2795.02"), TipoAbastecimento.PARCIAL, 2, false);

        CustosDeAbastecimento abastecimento11 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("07/04/23"), "Boiadeiro", new BigDecimal("20000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("5035.02"), TipoAbastecimento.TOTAL, 2, false);

        CustosDeAbastecimento abastecimento12 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("07/05/23"), "Boiadeiro", new BigDecimal("25000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("1835.02"), TipoAbastecimento.PARCIAL, 2, false);

        CustosDeAbastecimento abastecimento13 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("07/06/23"), "Boiadeiro", new BigDecimal("30000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("3235.02"), TipoAbastecimento.PARCIAL, 2, false);

        CustosDeAbastecimento abastecimento14 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("07/07/23"), "Boiadeiro", new BigDecimal("35000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("3935.02"), TipoAbastecimento.TOTAL, 2, true);

        CustosDeAbastecimento abastecimento15 = new CustosDeAbastecimento(FormataDataUtil.stringParaData("06/07/23"), "Boiadeiro", new BigDecimal("32000.00"), new BigDecimal("470.32"),
                new BigDecimal("5.39"), new BigDecimal("3935.02"), TipoAbastecimento.TOTAL, 2, false);

        abastecimentoDao.adiciona(abastecimento8);
        abastecimentoDao.adiciona(abastecimento9);
        abastecimentoDao.adiciona(abastecimento10);
        abastecimentoDao.adiciona(abastecimento11);
        abastecimentoDao.adiciona(abastecimento12);
        abastecimentoDao.adiciona(abastecimento13);
        abastecimentoDao.adiciona(abastecimento14);
        abastecimentoDao.adiciona(abastecimento15);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaFretes() throws ValorInvalidoException {


        frete3 = new Frete(FormataDataUtil.stringParaData("02/01/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 1);
        frete3.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete3.getAdmFrete().setFreteBruto(new BigDecimal("15000.00"));
        frete3.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete3.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete3.getAdmFrete().setComissaoJaFoiPaga(false);
        frete3.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete3.getAdmFrete().calculaComissaoELiquido(frete3);
        frete3.getAdmFrete().setFreteJaFoiPago(false);
        frete3.setApenasAdmEdita(false);

        frete9 = new Frete(FormataDataUtil.stringParaData("02/01/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 2);
        frete9.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete9.getAdmFrete().setFreteBruto(new BigDecimal("15000.00"));
        frete9.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete9.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete9.getAdmFrete().setComissaoJaFoiPaga(false);
        frete9.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete9.getAdmFrete().calculaComissaoELiquido(frete9);
        frete9.getAdmFrete().setFreteJaFoiPago(false);
        frete9.setApenasAdmEdita(false);

        frete4 = new Frete(FormataDataUtil.stringParaData("02/02/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 1);
        frete4.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete4.getAdmFrete().setFreteBruto(new BigDecimal("11000.00"));
        frete4.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete4.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete4.getAdmFrete().setComissaoJaFoiPaga(false);
        frete4.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete4.getAdmFrete().calculaComissaoELiquido(frete4);
        frete4.getAdmFrete().setFreteJaFoiPago(false);
        frete4.setApenasAdmEdita(false);

        frete10 = new Frete(FormataDataUtil.stringParaData("02/02/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 2);
        frete10.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete10.getAdmFrete().setFreteBruto(new BigDecimal("12000.00"));
        frete10.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete10.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete10.getAdmFrete().setComissaoJaFoiPaga(false);
        frete10.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete10.getAdmFrete().calculaComissaoELiquido(frete10);
        frete10.getAdmFrete().setFreteJaFoiPago(false);
        frete10.setApenasAdmEdita(false);

        frete5 = new Frete(FormataDataUtil.stringParaData("02/03/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 1);
        frete5.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete5.getAdmFrete().setFreteBruto(new BigDecimal("19000.00"));
        frete5.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete5.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete5.getAdmFrete().setComissaoJaFoiPaga(false);
        frete5.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete5.getAdmFrete().calculaComissaoELiquido(frete5);
        frete5.getAdmFrete().setFreteJaFoiPago(false);
        frete5.setApenasAdmEdita(false);

        /*frete11 = new Frete(FormataDataUtil.stringParaData("02/03/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 2);
        frete11.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete11.getAdmFrete().setFreteBruto(new BigDecimal("12000.00"));
        frete11.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete11.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete11.getAdmFrete().setComissaoJaFoiPaga(false);
        frete11.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete11.getAdmFrete().calculaComissaoELiquido(frete11);
        frete11.getAdmFrete().setFreteJaFoiPago(false);
        frete11.setApenasAdmEdita(false);*/

        frete6 = new Frete(FormataDataUtil.stringParaData("02/04/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 1);
        frete6.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete6.getAdmFrete().setFreteBruto(new BigDecimal("16000.00"));
        frete6.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete6.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete6.getAdmFrete().setComissaoJaFoiPaga(false);
        frete6.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete6.getAdmFrete().calculaComissaoELiquido(frete6);
        frete6.getAdmFrete().setFreteJaFoiPago(false);
        frete6.setApenasAdmEdita(false);

        frete12 = new Frete(FormataDataUtil.stringParaData("02/04/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 2);
        frete12.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete12.getAdmFrete().setFreteBruto(new BigDecimal("12000.00"));
        frete12.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete12.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete12.getAdmFrete().setComissaoJaFoiPaga(false);
        frete12.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete12.getAdmFrete().calculaComissaoELiquido(frete12);
        frete12.getAdmFrete().setFreteJaFoiPago(false);
        frete12.setApenasAdmEdita(false);

        frete7 = new Frete(FormataDataUtil.stringParaData("02/05/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 1);
        frete7.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete7.getAdmFrete().setFreteBruto(new BigDecimal("13000.00"));
        frete7.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete7.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete7.getAdmFrete().setComissaoJaFoiPaga(false);
        frete7.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete7.getAdmFrete().calculaComissaoELiquido(frete7);
        frete7.getAdmFrete().setFreteJaFoiPago(false);
        frete7.setApenasAdmEdita(false);

        frete13 = new Frete(FormataDataUtil.stringParaData("02/05/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 2);
        frete13.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete13.getAdmFrete().setFreteBruto(new BigDecimal("11000.00"));
        frete13.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete13.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete13.getAdmFrete().setComissaoJaFoiPaga(false);
        frete13.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete13.getAdmFrete().calculaComissaoELiquido(frete13);
        frete13.getAdmFrete().setFreteJaFoiPago(false);
        frete13.setApenasAdmEdita(false);

        frete8 = new Frete(FormataDataUtil.stringParaData("02/06/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 1);
        frete8.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete8.getAdmFrete().setFreteBruto(new BigDecimal("18000.00"));
        frete8.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete8.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete8.getAdmFrete().setComissaoJaFoiPaga(false);
        frete8.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete8.getAdmFrete().calculaComissaoELiquido(frete8);
        frete8.getAdmFrete().setFreteJaFoiPago(false);
        frete8.setApenasAdmEdita(false);

        frete14 = new Frete(FormataDataUtil.stringParaData("02/06/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 2);
        frete14.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete14.getAdmFrete().setFreteBruto(new BigDecimal("11000.00"));
        frete14.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete14.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete14.getAdmFrete().setComissaoJaFoiPaga(false);
        frete14.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete14.getAdmFrete().calculaComissaoELiquido(frete14);
        frete14.getAdmFrete().setFreteJaFoiPago(false);
        frete14.setApenasAdmEdita(false);

        frete1 = new Frete(FormataDataUtil.stringParaData("01/07/23"), "Soja", "Santana", "Fazenda", "Soja", new BigDecimal("45000.00"), 1);
        frete1.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete1.getAdmFrete().setFreteBruto(new BigDecimal("14000.00"));
        frete1.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete1.getAdmFrete().setSeguroDeCarga(new BigDecimal("100.00"));
        frete1.getAdmFrete().setComissaoJaFoiPaga(false);
        frete1.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete1.getAdmFrete().calculaComissaoELiquido(frete1);
        frete1.getAdmFrete().setFreteJaFoiPago(false);
        frete1.setApenasAdmEdita(false);

        frete2 = new Frete(FormataDataUtil.stringParaData("02/07/23"), "Soja", "Horizonte", "Fazenda", "Soja", new BigDecimal("40000.00"), 2);
        frete2.setAdmFrete(new Frete.AdmFinanceiroFrete());
        frete2.getAdmFrete().setFreteBruto(new BigDecimal("10000.00"));
        frete2.getAdmFrete().setDescontos(new BigDecimal("0.00"));
        frete2.getAdmFrete().setSeguroDeCarga(new BigDecimal("50.00"));
        frete2.getAdmFrete().setComissaoJaFoiPaga(false);
        frete2.getAdmFrete().setComissaoPercentualAplicada(cavalo2.getComissaoBase());
        frete2.getAdmFrete().calculaComissaoELiquido(frete2);
        frete2.getAdmFrete().setFreteJaFoiPago(false);
        frete2.setApenasAdmEdita(false);

        freteDao.adiciona(frete1);
        freteDao.adiciona(frete2);
        freteDao.adiciona(frete3);
        freteDao.adiciona(frete4);
        freteDao.adiciona(frete5);
        freteDao.adiciona(frete6);
        freteDao.adiciona(frete7);
        freteDao.adiciona(frete8);
        freteDao.adiciona(frete9);
        freteDao.adiciona(frete10);
        //freteDao.adiciona(frete11);
        freteDao.adiciona(frete12);
        freteDao.adiciona(frete13);
        freteDao.adiciona(frete14);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criaMotoristas() {
        Bitmap img1 = BitmapFactory.decodeResource(getResources(), R.drawable.gildean);
        byte[] fotoEmBytes;
        ByteArrayOutputStream streamDaFotoEmBytes = new ByteArrayOutputStream();
        img1.compress(Bitmap.CompressFormat.PNG, 70, streamDaFotoEmBytes);
        fotoEmBytes = streamDaFotoEmBytes.toByteArray();
        String img1EmString = Base64.encodeToString(fotoEmBytes, Base64.DEFAULT);
        m1 = new Motorista(FormataDataUtil.stringParaData("01/01/70"), img1EmString, "Gildean Freire", "02545878965", "157893645876",
                FormataDataUtil.stringParaData("01/01/26"), FormataDataUtil.stringParaData("01/01/21"), new BigDecimal("2590.00"));

        Bitmap img2 = BitmapFactory.decodeResource(getResources(), R.drawable.roberto);
        byte[] fotoEmBytes2;
        ByteArrayOutputStream streamDaFotoEmBytes2 = new ByteArrayOutputStream();
        img2.compress(Bitmap.CompressFormat.PNG, 70, streamDaFotoEmBytes2);
        fotoEmBytes2 = streamDaFotoEmBytes2.toByteArray();
        String img2EmString = Base64.encodeToString(fotoEmBytes2, Base64.DEFAULT);
        m2 = new Motorista(FormataDataUtil.stringParaData("01/01/70"), img2EmString, "José Anacrizio", "02545878965", "157869345876",
                FormataDataUtil.stringParaData("01/01/26"), FormataDataUtil.stringParaData("01/01/21"), new BigDecimal("2590.00"));

        motoristaDao.adiciona(m1);
        motoristaDao.adiciona(m2);
    }


}
