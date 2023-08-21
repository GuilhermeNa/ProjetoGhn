package br.com.transporte.AppGhn.model.temporarios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class ObjetoTemporario_representaCavalo {

    private int id;
    private static BigDecimal valorAcumulado = BigDecimal.ZERO;
    private String placa, nome;
    private BigDecimal valor, percentual;


    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }

    public void setPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static BigDecimal getValorAcumulado() {
        return valorAcumulado;
    }

    public static void setValorAcumulado(BigDecimal valorAcumulado) {
        ObjetoTemporario_representaCavalo.valorAcumulado = valorAcumulado;
    }

 //------------------------------------- Outros Metodos --------------------------------------------

    public void definePercentual() {
        BigDecimal ONE_HUNDRED = new BigDecimal("100.00");
        BigDecimal divide;

        try {
            divide = getValor().divide(getValorAcumulado(), 2, RoundingMode.HALF_EVEN)
                    .multiply(ONE_HUNDRED).setScale(2, RoundingMode.HALF_EVEN);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            divide = BigDecimal.ZERO;
        }

        setPercentual(divide);
    }

    public static void resetaAcumulado() {
        setValorAcumulado(BigDecimal.ZERO);
    }

    public void adicionaValor(BigDecimal valor) {
        if (this.valor == null) this.valor = new BigDecimal(BigInteger.ZERO);
        setValor(this.valor.add(valor));
    }

    public void removeValor(BigDecimal valor) {
        if (this.valor == null) this.valor = new BigDecimal(BigInteger.ZERO);
        setValor(this.valor.subtract(valor));
    }

    public void adicionaAoSaldoAcumulado(BigDecimal valor) {
        setValorAcumulado(valorAcumulado.add(valor));
    }

    public void removeDoSaldoAcumulado(BigDecimal valor) {
        setValorAcumulado(valorAcumulado.subtract(valor));
    }
}
