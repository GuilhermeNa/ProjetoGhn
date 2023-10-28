package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers;

import static br.com.transporte.AppGhn.util.BigDecimalConstantes.BIG_DECIMAL_CEM;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class MappedRecylerData {

    private static BigDecimal valorAcumulado = BigDecimal.ZERO;
    private Long
            cavaloId;
    private String
            placa, nome;
    private BigDecimal
            valor, percentual;

    public MappedRecylerData(String nome, String placa, Long id) {
        this.nome = nome;
        this.placa = placa;
        this.cavaloId = id;
    }

    public MappedRecylerData() {

    }

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

    public Long getCavaloId() {
        return cavaloId;
    }

    public void setCavaloId(Long cavaloId) {
        this.cavaloId = cavaloId;
    }

    public static BigDecimal getValorAcumulado() {
        return valorAcumulado;
    }

    public static void setValorAcumulado(BigDecimal valorAcumulado) {
        MappedRecylerData.valorAcumulado = valorAcumulado;
    }

    //------------------------------------- Outros Metodos --------------------------------------------

    public void definePercentual(final BigDecimal valorTotal) {
        BigDecimal divide;

        try {
            divide = this.valor
                    .divide(valorTotal, 2, RoundingMode.HALF_EVEN)
                    .multiply(BIG_DECIMAL_CEM).setScale(2, RoundingMode.HALF_EVEN);

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

    public void subtraiValor(BigDecimal valor) {
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
