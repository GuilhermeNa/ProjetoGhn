package br.com.transporte.appGhn.model;

import static br.com.transporte.appGhn.util.BigDecimalConstantes.BIG_DECIMAL_CEM;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import br.com.transporte.appGhn.exception.ValorInvalidoException;

@Entity(foreignKeys =
@ForeignKey(
        entity = Cavalo.class,
        parentColumns = "id",
        childColumns = "refCavaloId",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
))
public class Frete implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long refCavaloId;
    private String origem, destino, empresa, carga;
    private BigDecimal peso;
    private LocalDate data;
    private BigDecimal freteBruto;
    private BigDecimal freteLiquidoAReceber;
    private BigDecimal seguroDeCarga;
    private BigDecimal descontos;
    private BigDecimal comissaoAoMotorista;
    private BigDecimal comissaoPercentualAplicada;
    private boolean apenasAdmEdita;
    private boolean comissaoJaFoiPaga;
    private boolean freteJaFoiPago;

    //---------------------------------- Getters and Setters --------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefCavaloId() {
        return refCavaloId;
    }

    public void setRefCavaloId(Long refCavaloId) {
        this.refCavaloId = refCavaloId;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getFreteBruto() {
        return freteBruto;
    }

    public void setFreteBruto(BigDecimal freteBruto) {
        this.freteBruto = freteBruto;
    }

    public BigDecimal getFreteLiquidoAReceber() {
        return freteLiquidoAReceber;
    }

    public void setFreteLiquidoAReceber(BigDecimal freteLiquidoAReceber) {
        this.freteLiquidoAReceber = freteLiquidoAReceber;
    }

    public BigDecimal getSeguroDeCarga() {
        return seguroDeCarga;
    }

    public void setSeguroDeCarga(BigDecimal seguroDeCarga) {
        this.seguroDeCarga = seguroDeCarga;
    }

    public BigDecimal getDescontos() {
        return descontos;
    }

    public void setDescontos(BigDecimal descontos) {
        this.descontos = descontos;
    }

    public BigDecimal getComissaoAoMotorista() {
        return comissaoAoMotorista;
    }

    public void setComissaoAoMotorista(BigDecimal comissaoAoMotorista) {
        this.comissaoAoMotorista = comissaoAoMotorista;
    }

    public BigDecimal getComissaoPercentualAplicada() {
        return comissaoPercentualAplicada;
    }

    public void setComissaoPercentualAplicada(BigDecimal comissaoPercentualAplicada) {
        this.comissaoPercentualAplicada = comissaoPercentualAplicada;
    }

    public boolean isApenasAdmEdita() {
        return apenasAdmEdita;
    }

    public void setApenasAdmEdita(boolean apenasAdmEdita) {
        this.apenasAdmEdita = apenasAdmEdita;
    }

    public boolean isComissaoJaFoiPaga() {
        return comissaoJaFoiPaga;
    }

    public void setComissaoJaFoiPaga(boolean comissaoJaFoiPaga) {
        this.comissaoJaFoiPaga = comissaoJaFoiPaga;
    }

    public boolean isFreteJaFoiPago() {
        return freteJaFoiPago;
    }

    public void setFreteJaFoiPago(boolean freteJaFoiPago) {
        this.freteJaFoiPago = freteJaFoiPago;
    }

    public void calculaComissao() {
        BigDecimal comissaoAoMotorista = comissaoPercentualAplicada.divide(BIG_DECIMAL_CEM, 2, RoundingMode.HALF_EVEN)
                .multiply(freteBruto).setScale(2, RoundingMode.HALF_EVEN);
        setComissaoAoMotorista(comissaoAoMotorista);
    }

    public void calculaFreteLiquidoAReceber() {
        BigDecimal freteLiquido = freteBruto.subtract(descontos).subtract(seguroDeCarga);
        setFreteLiquidoAReceber(freteLiquido);
    }

    public void vinculaComissaoAplicada(BigDecimal comissaoPercentualAplicada) throws ValorInvalidoException {
        int compare = BIG_DECIMAL_CEM.compareTo(comissaoPercentualAplicada);
        if (compare <= 0) {
            throw new ValorInvalidoException("Comissão não pode ser superior a 100%");
        } else {
            setComissaoPercentualAplicada(comissaoPercentualAplicada);
        }


    }

}

