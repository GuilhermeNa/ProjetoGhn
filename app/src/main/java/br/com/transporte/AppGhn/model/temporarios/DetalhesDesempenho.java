package br.com.transporte.AppGhn.model.temporarios;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DetalhesDesempenho {

    private int id;
    private static BigDecimal valorAcumulado = BigDecimal.ZERO;

    private String placa;
    private BigDecimal valor;
    private String nome;
    private BigDecimal percentual;


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
        DetalhesDesempenho.valorAcumulado = valorAcumulado;
    }

    public void adicionaAoSaldoAcumulado(BigDecimal valor) {
        setValorAcumulado(valorAcumulado.add(valor));
    }

    public void definePercentual() {
        BigDecimal ONE_HUNDRED = new BigDecimal("100.00");
        BigDecimal divide;

        try{
            divide = getValor().divide(getValorAcumulado(), 2, RoundingMode.HALF_EVEN)
                    .multiply(ONE_HUNDRED).setScale(2, RoundingMode.HALF_EVEN);
        } catch (ArithmeticException e){
            e.printStackTrace();
            divide = BigDecimal.ZERO;
        }

        setPercentual(divide);
    }

    public static void resetaAcumulado(){
        setValorAcumulado(BigDecimal.ZERO);
    }


}
