package br.com.transporte.AppGhn.model.despesas;

import androidx.room.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.parcelas.factory.ParcelasFactory;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.parcelas.factory.ParcelamentoInterface;

@Entity
public class DespesaComSeguroFrota extends DespesaComSeguro implements ParcelamentoInterface<Parcela_seguroFrota> {
    private String coberturaCasco, assistencia24H, coberturaVidros;
    private BigDecimal coberturaRcfMateriais, coberturaRcfCorporais, coberturaAppMorte,
            coberturaAppInvalidez, coberturaDanosMorais;

    public DespesaComSeguroFrota(LocalDate dataInicial, LocalDate dataFinal, LocalDate dataPrimeiraParcela, BigDecimal valorTotal, int parcelas,
                                 BigDecimal valorParcela, String companhia, int nContrato, String coberturaCasco, BigDecimal coberturaRcfMateriais,
                                 BigDecimal coberturaRcfCorporais, BigDecimal coberturaAppMorte, BigDecimal coberturaAppinvalidez,
                                 BigDecimal coberturaDanosMorais, String coberturaVidros, String assistencia24H, Long refCavalo) {
        super.setData(dataInicial);
        super.setDataInicial(dataInicial);
        super.setDataFinal(dataFinal);
        super.setDataPrimeiraParcela(dataPrimeiraParcela);
        super.setValorDespesa(valorTotal);
        super.setParcelas(parcelas);
        super.setValorParcela(valorParcela);
        super.setCompanhia(companhia);
        super.setNContrato(nContrato);
        this.coberturaCasco = coberturaCasco;
        this.coberturaRcfMateriais = coberturaRcfMateriais;
        this.coberturaRcfCorporais = coberturaRcfCorporais;
        this.coberturaAppMorte = coberturaAppMorte;
        this.coberturaAppInvalidez = coberturaAppinvalidez;
        this.coberturaDanosMorais = coberturaDanosMorais;
        this.coberturaVidros = coberturaVidros;
        this.assistencia24H = assistencia24H;
        super.setRefCavaloId(refCavalo);
    }

    public DespesaComSeguroFrota() {

    }

    public String getCoberturaCasco() {
        return coberturaCasco;
    }

    public void setCoberturaCasco(String coberturaCasco) {
        this.coberturaCasco = coberturaCasco;
    }

    public BigDecimal getCoberturaRcfMateriais() {
        return coberturaRcfMateriais;
    }

    public void setCoberturaRcfMateriais(BigDecimal coberturaRcfMateriais) {
        this.coberturaRcfMateriais = coberturaRcfMateriais;
    }

    public BigDecimal getCoberturaRcfCorporais() {
        return coberturaRcfCorporais;
    }

    public void setCoberturaRcfCorporais(BigDecimal coberturaRcfCorporais) {
        this.coberturaRcfCorporais = coberturaRcfCorporais;
    }

    public BigDecimal getCoberturaAppMorte() {
        return coberturaAppMorte;
    }

    public void setCoberturaAppMorte(BigDecimal coberturaAppMorte) {
        this.coberturaAppMorte = coberturaAppMorte;
    }

    public BigDecimal getCoberturaAppInvalidez() {
        return coberturaAppInvalidez;
    }

    public void setCoberturaAppInvalidez(BigDecimal coberturaAppInvalidez) {
        this.coberturaAppInvalidez = coberturaAppInvalidez;
    }

    public BigDecimal getCoberturaDanosMorais() {
        return coberturaDanosMorais;
    }

    public void setCoberturaDanosMorais(BigDecimal coberturaDanosMorais) {
        this.coberturaDanosMorais = coberturaDanosMorais;
    }

    public String getCoberturaVidros() {
        return coberturaVidros;
    }

    public void setCoberturaVidros(String coberturaVidros) {
        this.coberturaVidros = coberturaVidros;
    }

    public String getAssistencia24H() {
        return assistencia24H;
    }

    public void setAssistencia24H(String assistencia24H) {
        this.assistencia24H = assistencia24H;
    }

    public boolean temIdValido() {
        return super.getId() > 0;
    }

    @Override
    public List<Parcela_seguroFrota> criaParcelas(
            final Long chaveEstrangeira
    ) {
        final List<Parcela_seguroFrota> listaDeParcelas = new ArrayList<>();
        int quantidadeDeParcelas = ParcelasFactory
                .configQuantidadeDeParcelas(this.getParcelas());

        Parcela_seguroFrota parcelaDeSeguro;
        for (int i = 0; i < quantidadeDeParcelas; i++) {
            parcelaDeSeguro = new Parcela_seguroFrota();
            parcelaDeSeguro.setData(this.getDataPrimeiraParcela().plusMonths(i));
            parcelaDeSeguro.setTipoDespesa(this.getTipoDespesa());
            parcelaDeSeguro.setRefCavaloId(this.getRefCavaloId());
            parcelaDeSeguro.setValor(this.getValorParcela());
            parcelaDeSeguro.setRefSeguro(chaveEstrangeira);
            parcelaDeSeguro.setNumeroDaParcela(i + 1);
            parcelaDeSeguro.setValido(true);
            parcelaDeSeguro.setPaga(false);

            listaDeParcelas.add(parcelaDeSeguro);
        }
        return listaDeParcelas;
    }

}
