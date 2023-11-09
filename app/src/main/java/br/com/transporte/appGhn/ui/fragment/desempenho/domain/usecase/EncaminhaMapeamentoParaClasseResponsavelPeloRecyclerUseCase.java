package br.com.transporte.appGhn.ui.fragment.desempenho.domain.usecase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperComissaoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperCustoAbastecimentoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperCustoManutencaoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperCustoPercursoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperDespesaAdmUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperDespesaCertificadoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperDespesaImpostoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperFreteBrutoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperFreteLiquidoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperLitragemUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperLucroLiquidoUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperSeguroFrotaUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler.RecyclerMapperSeguroVidaUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class EncaminhaMapeamentoParaClasseResponsavelPeloRecyclerUseCase {

    private final ResourceData resource;
    private List<MappedRecylerData> dataSetMappedRecyclerData;
    private DataRequest dataRequest;

    public EncaminhaMapeamentoParaClasseResponsavelPeloRecyclerUseCase(ResourceData resourceDesempenho) {
        this.resource = resourceDesempenho;
    }

    //----------------------------------------------------------------------------------------------

    public List<MappedRecylerData> run(@NonNull final DataRequest dataRequest) {
        this.dataSetMappedRecyclerData = inicializaDataSetMappedRecyclerData();
        this.dataRequest = dataRequest;

        switch (dataRequest.getTipo()) {
            case FRETE_BRUTO:
                return mapeiaFreteBruto();

            case FRETE_LIQUIDO:
                return mapeiaFreteLiquido();

            case COMISSAO:
                return mapeiaCustoComissao();

            case CUSTOS_ABASTECIMENTO:
                return mapeiaCustoAbastecimento();

            case LITRAGEM:
                return mapeiaLitragem();

            case CUSTOS_PERCURSO:
                return mapeiaCustoPercurso();

            case CUSTOS_MANUTENCAO:
                return mapeiaCustoManutencao();

            case LUCRO_LIQUIDO:
                return mapeiaLucroLiquido();

            case DESPESA_CERTIFICADOS:
                return mapeiaDespesaCertificado();

            case DESPESAS_IMPOSTOS:
                return mapeiaDespesaImposto();

            case DESPESAS_ADM:
                return mapeiaDespesaAdm();

            case DESPESA_SEGURO_FROTA:
                return mapeiaDespesaSeguroFrota();

            case DESPESA_SEGURO_VIDA:
                return mapeiaDespesaSeguroVida();
        }

        return dataSetMappedRecyclerData;
    }

    @NonNull
    private List<MappedRecylerData> inicializaDataSetMappedRecyclerData() {
        final List<MappedRecylerData> listaCavaloDesempenhoMapper = new ArrayList<>();

        for (Cavalo c : resource.getDataSetCavalo()) {
            final MappedRecylerData d = new MappedRecylerData();
            final Optional<Motorista> motorista = resource.getDataSetMotorista().stream()
                    .filter(m -> Objects.equals(m.getId(), c.getRefMotoristaId())).findAny();

            motorista.ifPresent(m -> d.setNome(m.toString()));
            d.setPlaca(c.getPlaca());
            d.setCavaloId(c.getId());
            listaCavaloDesempenhoMapper.add(d);
        }
        return listaCavaloDesempenhoMapper;
    }

    private List<MappedRecylerData> mapeiaFreteBruto() {
        final RecyclerMapperFreteBrutoUseCase mapper =
                new RecyclerMapperFreteBrutoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaCustoAbastecimento() {
        final RecyclerMapperCustoAbastecimentoUseCase mapper =
                new RecyclerMapperCustoAbastecimentoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaLitragem() {
        final RecyclerMapperLitragemUseCase mapper =
                new RecyclerMapperLitragemUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaCustoPercurso() {
        final RecyclerMapperCustoPercursoUseCase mapper =
                new RecyclerMapperCustoPercursoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaCustoManutencao() {
        final RecyclerMapperCustoManutencaoUseCase mapper =
                new RecyclerMapperCustoManutencaoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaLucroLiquido() {
        final RecyclerMapperLucroLiquidoUseCase mapper =
                new RecyclerMapperLucroLiquidoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaDespesaCertificado() {
        final RecyclerMapperDespesaCertificadoUseCase mapper =
                new RecyclerMapperDespesaCertificadoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaDespesaImposto() {
        final RecyclerMapperDespesaImpostoUseCase mapper =
                new RecyclerMapperDespesaImpostoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaDespesaAdm() {
        final RecyclerMapperDespesaAdmUseCase mapper =
                new RecyclerMapperDespesaAdmUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaDespesaSeguroFrota() {
        final RecyclerMapperSeguroFrotaUseCase mapper =
                new RecyclerMapperSeguroFrotaUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaDespesaSeguroVida() {
        final RecyclerMapperSeguroVidaUseCase mapper =
                new RecyclerMapperSeguroVidaUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaCustoComissao() {
        final RecyclerMapperComissaoUseCase mapper =
                new RecyclerMapperComissaoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

    private List<MappedRecylerData> mapeiaFreteLiquido() {
        final RecyclerMapperFreteLiquidoUseCase mapper =
                new RecyclerMapperFreteLiquidoUseCase(resource, dataRequest);
        return mapper.mapeiaCavaloDesempenhoParaExibirNaRecycler(dataSetMappedRecyclerData);
    }

}
