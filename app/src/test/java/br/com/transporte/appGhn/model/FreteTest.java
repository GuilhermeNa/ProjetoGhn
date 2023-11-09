package br.com.transporte.appGhn.model;

import static br.com.transporte.appGhn.util.BigDecimalConstantes.BIG_DECIMAL_CEM;
import static br.com.transporte.appGhn.util.BigDecimalConstantes.BIG_DECIMAL_DEZ;
import static br.com.transporte.appGhn.util.BigDecimalConstantes.BIG_DECIMAL_UM;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import br.com.transporte.appGhn.exception.ValorInvalidoException;
import br.com.transporte.appGhn.util.ConverteDataUtil;

public class FreteTest {
    private final Frete FRETE = new Frete();

    @Test
    public void deve_DevolverOrigem_QuandoRecebeOrigem() {
        FRETE.setOrigem("origem");
        Assert.assertEquals("origem", FRETE.getOrigem());
    }

    @Test
    public void deve_DevolverDestino_QuandoRecebeDestino() {
        FRETE.setDestino("destino");
        Assert.assertEquals("destino", FRETE.getDestino());
    }

    @Test
    public void deve_DevolverId_QuandoRecebeId() {
        //TODO
        //frete.setId(1L);
        //Assert.assertEquals(1L, frete.getId());
    }

    @Test
    public void deve_DevolverCavaloId_QuandoRecebeCavaloId() {
        //TODO
        //frete.setRefCavaloId(1L);
        //Assert.assertEquals(1L, frete.getRefCavaloId());
    }

    @Test
    public void deve_DevolverEmpresa_QuandoRecebeEmpresa() {
        FRETE.setEmpresa("empresa");
        Assert.assertEquals("empresa", FRETE.getEmpresa());
    }

    @Test
    public void deve_DevolverCarga_Quando_recebeCarga() {
        FRETE.setCarga("carga");
        Assert.assertEquals("carga", FRETE.getCarga());
    }

    @Test
    public void deve_DevolverPeso_QuandoRecebePeso() {
        FRETE.setPeso(BigDecimal.TEN);
        Assert.assertEquals(BigDecimal.TEN, FRETE.getPeso());
    }

    @Test
    public void deve_DevolverData_QuandoRecebeData() {
        FRETE.setData(ConverteDataUtil.stringParaData("01/01/01"));
        Assert.assertEquals(ConverteDataUtil.stringParaData("01/01/01"), FRETE.getData());
    }

    @Test
    public void deve_DevolverFreteBruto_QuandoRecebeFreteBruto() {
        FRETE.setFreteBruto(BigDecimal.TEN);
        Assert.assertEquals(BigDecimal.TEN, FRETE.getFreteBruto());
    }

    @Test
    public void deve_DevolverFreteLiquidoRebeber_QuandoRecebeFreteLiquidoReceber(){
        FRETE.setFreteLiquidoAReceber(BIG_DECIMAL_UM);
        Assert.assertEquals(BIG_DECIMAL_UM, FRETE.getFreteLiquidoAReceber());
    }

    @Test
    public void deve_CalcularFreteLiquido_Usando_FreteBruto_Descontos_SeguroDeCarga() {
        FRETE.setFreteBruto(BigDecimal.TEN);
        FRETE.setDescontos(BIG_DECIMAL_UM);
        FRETE.setSeguroDeCarga(BIG_DECIMAL_UM);

        BigDecimal freteBruto = FRETE.getFreteBruto();
        BigDecimal descontos = FRETE.getDescontos();
        BigDecimal seguroDeCarga = FRETE.getSeguroDeCarga();

        BigDecimal resultado = freteBruto
                .subtract(descontos)
                .subtract(seguroDeCarga);

        FRETE.setFreteLiquidoAReceber(resultado);

        Assert.assertEquals(new BigDecimal("8.00"), FRETE.getFreteLiquidoAReceber());
    }

    @Test
    public void deve_DevolverSeguroDeCarga_QuandoRecebeSeguroDeCarga() {
        FRETE.setSeguroDeCarga(BIG_DECIMAL_UM);
        Assert.assertEquals(BIG_DECIMAL_UM, FRETE.getSeguroDeCarga());
    }

    @Test
    public void deve_DevolverDesconto_QuandoRecebeDesconto() {
        FRETE.setDescontos(BIG_DECIMAL_UM);
        Assert.assertEquals(BIG_DECIMAL_UM, FRETE.getDescontos());
    }

    @Test
    public void testGetComissaoAoMotorista() {
        FRETE.setComissaoAoMotorista(BigDecimal.TEN);
        Assert.assertEquals(BigDecimal.TEN, FRETE.getComissaoAoMotorista());
    }

    @Test
    public void testGetComissaoPercentualAplicada() {
        FRETE.setComissaoPercentualAplicada(BigDecimal.TEN);
        Assert.assertEquals(BigDecimal.TEN, FRETE.getComissaoPercentualAplicada());
    }

    @Test
    public void deve_DevolverApenasAdmEdita_QuandoRecebeApenasAdmEdita() {
        FRETE.setApenasAdmEdita(true);
        Assert.assertTrue(FRETE.isApenasAdmEdita());
    }

    @Test
    public void deve_DevolverComissaoJaFoiPaga_QuandoRecebeComissaoJaFoiPaga() {
        FRETE.setComissaoJaFoiPaga(true);
        Assert.assertTrue(FRETE.isComissaoJaFoiPaga());
    }

    @Test
    public void deve_DevolverFreteJaFoiPago_QuandoRecebeFreteJaFoiPago() {
        FRETE.setFreteJaFoiPago(true);
        Assert.assertTrue(FRETE.isFreteJaFoiPago());
    }

    @Test
    public void deve_CalcularComissaoAoMotorista_QuandoCalculaUsando_ComissaoPercentual_FreteBruto() {
        FRETE.setFreteBruto(BIG_DECIMAL_CEM);
        FRETE.setComissaoPercentualAplicada(BigDecimal.TEN);
        FRETE.calculaComissao();
        BigDecimal comissaoAoMotorista = FRETE.getComissaoAoMotorista();
        Assert.assertEquals(BIG_DECIMAL_DEZ, comissaoAoMotorista);
    }

    @Test
    public void deve_CalcularFreteLiquido_QuandoCalculaUsando_FreteBruto_Descontos_DespesaSeguro() {
        FRETE.setFreteBruto(BigDecimal.TEN);
        FRETE.setDescontos(BIG_DECIMAL_UM);
        FRETE.setSeguroDeCarga(BIG_DECIMAL_UM);
        FRETE.calculaFreteLiquidoAReceber();
        BigDecimal freteLiquidoAReceber = FRETE.getFreteLiquidoAReceber();
        Assert.assertEquals(new BigDecimal("8.00"), freteLiquidoAReceber);
    }

    @Test
    public void deve_VincularComissaoPercentual_QuandoRecebeComissaoPercentual() throws ValorInvalidoException {
        FRETE.vinculaComissaoAplicada(BigDecimal.TEN);
        BigDecimal comissaoPercentualAplicada = FRETE.getComissaoPercentualAplicada();
        Assert.assertEquals(BigDecimal.TEN, comissaoPercentualAplicada);
    }





}