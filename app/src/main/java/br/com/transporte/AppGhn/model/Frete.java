package br.com.transporte.AppGhn.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import br.com.transporte.AppGhn.exception.ValorInvalidoException;

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
    private long id;
    private Integer refCavaloId;

    private String origem, destino, empresa, carga;
    //private AdmFinanceiroFrete admFrete;
    private boolean apenasAdmEdita;
    private BigDecimal peso;
    private LocalDate data;

    public Frete(LocalDate data, String origem, String destino, String empresa, String carga, BigDecimal peso, Integer refCavalo) {
        this.data = data;
        this.origem = origem;
        this.destino = destino;
        this.empresa = empresa;
        this.carga = carga;
        this.peso = peso;
        this.refCavaloId = refCavalo;
    }

    public Frete() {
    }


    //---------------------------------- Getters and Setters --------------------------------------

  /*  public AdmFinanceiroFrete getAdmFrete() {
        return admFrete;
    }*/

   /* public void setAdmFrete(AdmFinanceiroFrete admFrete) {
        this.admFrete = admFrete;
    }*/

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean temIdValido() {
        return this.id > 0;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isApenasAdmEdita() {
        return apenasAdmEdita;
    }

    public void setApenasAdmEdita(boolean apenasAdmEdita) {
        this.apenasAdmEdita = apenasAdmEdita;
    }

    public Integer getRefCavaloId() {
        return refCavaloId;
    }

    public void setRefCavaloId(Integer refCavaloId) {
        this.refCavaloId = refCavaloId;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    //----------------------------------------------------------------------------------------------
    //---------------------------------- Inner Class -----------------------------------------------

    @Entity(/*foreignKeys =
    @ForeignKey(
            entity = Frete.class,
            parentColumns = "id",
            childColumns = "refFreteId",
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
    )*/)
    public static class AdmFinanceiroFrete {
        @PrimaryKey(autoGenerate = true)
        private long id;
        private long refFreteId;

        private BigDecimal freteBruto;
        private BigDecimal freteLiquidoAReceber;
        private BigDecimal seguroDeCarga;
        private BigDecimal descontos;
        private BigDecimal comissaoAoMotorista;
        private BigDecimal comissaoPercentualAplicada;
        private boolean comissaoJaFoiPaga;
        private boolean freteJaFoiPago;


        //---------------------------------- Getters and Setters -----------------------------------


        public long getRefFreteId() {
            return refFreteId;
        }

        public void setRefFreteId(long refFreteId) {
            this.refFreteId = refFreteId;
        }

        public static void teste() {
        }


        public BigDecimal getFreteBruto() {
            return freteBruto;
        }

        public void setFreteBruto(BigDecimal freteBruto) {
            this.freteBruto = freteBruto;
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

        public BigDecimal getFreteLiquidoAReceber() {
            return freteLiquidoAReceber;
        }

        public void setFreteLiquidoAReceber(BigDecimal freteLiquido) {
            this.freteLiquidoAReceber = freteLiquido;
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

        public void setComissaoPercentualAplicada(BigDecimal comissaoPercentualAplicada) throws ValorInvalidoException {
            BigDecimal ONE_HUNDRED = new BigDecimal("100.00");
            int compare = ONE_HUNDRED.compareTo(comissaoPercentualAplicada);
            if (compare <= 0) {
                throw new ValorInvalidoException("Comissão não pode ser superior a 100%");
            } else {
                this.comissaoPercentualAplicada = comissaoPercentualAplicada;
            }
        }

        public boolean isComissaoJaFoiPaga() {
            return comissaoJaFoiPaga;
        }

        public void setComissaoJaFoiPaga(boolean comissaoJaFoiPaga) {
            this.comissaoJaFoiPaga = comissaoJaFoiPaga;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public boolean isFreteJaFoiPago() {
            return freteJaFoiPago;
        }

        public void setFreteJaFoiPago(boolean freteJaFoiPago) {
            this.freteJaFoiPago = freteJaFoiPago;
        }


        //---------------------------------- Outros Metodos ----------------------------------------

        public void setComissaoPercentualAplicadaIgnorandoTryCatch(BigDecimal comissaoPercentualAplicada) {
            this.comissaoPercentualAplicada = comissaoPercentualAplicada;
        }

        public void calculaComissaoELiquido(Frete frete) {
            frete.getAdmFrete().setComissaoAoMotorista(frete.getAdmFrete().calculaComissao());
            frete.getAdmFrete().setFreteLiquidoAReceber(frete.getAdmFrete().calculaFreteLiquidoAReceber());
        }

        public BigDecimal calculaComissao() {
            BigDecimal ONE_HUNDRED = new BigDecimal("100.00");
            return getComissaoPercentualAplicada().divide(ONE_HUNDRED, 2, RoundingMode.HALF_EVEN)
                    .multiply(freteBruto).setScale(2, RoundingMode.HALF_EVEN);
        }

        public BigDecimal calculaFreteLiquidoAReceber() {
            return this.freteBruto.subtract(this.descontos).subtract(this.seguroDeCarga);

        }

    }

}

