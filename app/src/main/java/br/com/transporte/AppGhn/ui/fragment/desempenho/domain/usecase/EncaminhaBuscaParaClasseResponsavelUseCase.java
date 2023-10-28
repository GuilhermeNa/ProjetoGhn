package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.usecase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import br.com.transporte.AppGhn.repository.FragmentDesempenhoRepository;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaCustoAbastecimentoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaCustoManutencaoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaCustoPercursoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaDespesaAdmUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaDespesaCertificadoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaDespesaImpostoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaFreteUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaLucroLiquidoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaParcelaFrotaUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.SolicitaParcelaVidaUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class EncaminhaBuscaParaClasseResponsavelUseCase {
    private final FragmentDesempenhoRepository repository;
    private DataRequest dataRequest;

    public EncaminhaBuscaParaClasseResponsavelUseCase(FragmentDesempenhoRepository repositoryResource) {
        this.repository = repositoryResource;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<ResourceData> run(@NonNull final DataRequest dataRequest) {
        this.dataRequest = dataRequest;

        switch (dataRequest.getTipo()) {
            case FRETE_BRUTO:
            case FRETE_LIQUIDO:
            case COMISSAO:
                return solicitaFrete();

            case CUSTOS_ABASTECIMENTO:
            case LITRAGEM:
                return solicitaCustoAbastecimento();

            case CUSTOS_PERCURSO:
                return solicitaCustoPercurso();

            case CUSTOS_MANUTENCAO:
                return solicitaCustoManutencao();

            case DESPESA_CERTIFICADOS:
                return solicitaDespesaCertificado();

            case DESPESAS_IMPOSTOS:
                return solicitaDespesaImposto();

            case DESPESAS_ADM:
                return solicitaDespesaAdm();

            case DESPESA_SEGURO_FROTA:
                return solicitaDespesaSeguroFrota();

            case DESPESA_SEGURO_VIDA:
                return solicitaDespesaSeguroVida();

            case LUCRO_LIQUIDO:
                return solicitaLucroLiquido();

        }
        return null;
    }

    private LiveData<ResourceData> solicitaFrete() {
        final SolicitaFreteUseCase solicitaBrutoUseCase =
                new SolicitaFreteUseCase(repository);
        return solicitaBrutoUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaCustoAbastecimento() {
        final SolicitaCustoAbastecimentoUseCase solicitaCustoAbastecimentoUseCase =
                new SolicitaCustoAbastecimentoUseCase(repository);
        return solicitaCustoAbastecimentoUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaCustoPercurso() {
        final SolicitaCustoPercursoUseCase solicitaCustoPercursoUseCase =
                new SolicitaCustoPercursoUseCase(repository);
        return solicitaCustoPercursoUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaCustoManutencao() {
        final SolicitaCustoManutencaoUseCase solicitaCustoManutencaoUseCase =
                new SolicitaCustoManutencaoUseCase(repository);
        return solicitaCustoManutencaoUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaDespesaCertificado() {
        final SolicitaDespesaCertificadoUseCase solicitaDespesaCertificadoUseCase =
                new SolicitaDespesaCertificadoUseCase(repository);
        return solicitaDespesaCertificadoUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaDespesaImposto() {
        final SolicitaDespesaImpostoUseCase solicitaDespesaImpostoUseCase =
                new SolicitaDespesaImpostoUseCase(repository);
        return solicitaDespesaImpostoUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaDespesaAdm() {
        final SolicitaDespesaAdmUseCase solicitaDespesaAdmUseCase =
                new SolicitaDespesaAdmUseCase(repository);
        return solicitaDespesaAdmUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaDespesaSeguroFrota() {
        final SolicitaParcelaFrotaUseCase solicitaParcelaFrotaUseCase =
                new SolicitaParcelaFrotaUseCase(repository);
        return solicitaParcelaFrotaUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaDespesaSeguroVida() {
        final SolicitaParcelaVidaUseCase solicitaParcelaVidaUseCase =
                new SolicitaParcelaVidaUseCase(repository);
        return solicitaParcelaVidaUseCase.run(dataRequest);
    }

    private LiveData<ResourceData> solicitaLucroLiquido() {
        final SolicitaLucroLiquidoUseCase solicitaLucroLiquidoUseCase =
                new SolicitaLucroLiquidoUseCase(repository);
        return solicitaLucroLiquidoUseCase.run(dataRequest);
    }

}
