package br.com.transporte.AppGhn.ui.fragment.desempenho.model;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.CertificadoRepository;
import br.com.transporte.AppGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.AppGhn.repository.CustoDePercursoRepository;
import br.com.transporte.AppGhn.repository.DespesaAdmRepository;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.ImpostoRepository;
import br.com.transporte.AppGhn.repository.ManutencaoRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.repository.ParcelaSeguroFrotaRepository;
import br.com.transporte.AppGhn.repository.ParcelaSeguroVidaRepository;

public class ResourceRepositories {
    private final FreteRepository freteRepository;
    private final CustoDeAbastecimentoRepository abastecimentoRepository;
    private final CustoDePercursoRepository percursoRepository;
    private final MotoristaRepository motoristaRepository;
    private final CavaloRepository cavaloRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final ImpostoRepository impostoRepository;
    private final CertificadoRepository certificadoRepository;
    private final ParcelaSeguroFrotaRepository parcelaFrotaRepository;
    private final ParcelaSeguroVidaRepository parcelaVidaRepository;
    private final DespesaAdmRepository despesaAdmRepository;

    public ResourceRepositories(
            FreteRepository freteRepository,
            CustoDeAbastecimentoRepository abastecimentoRepository,
            CustoDePercursoRepository percursoRepository,
            MotoristaRepository motoristaRepository,
            CavaloRepository cavaloRepository,
            ManutencaoRepository manutencaoRepository,
            ImpostoRepository impostoRepository,
            CertificadoRepository certificadoRepository,
            ParcelaSeguroFrotaRepository parcelaFrotaRepository,
            ParcelaSeguroVidaRepository parcelaVidaRepository,
            DespesaAdmRepository despesaAdmRepository
    ) {
        this.freteRepository = freteRepository;
        this.abastecimentoRepository = abastecimentoRepository;
        this.percursoRepository = percursoRepository;
        this.motoristaRepository = motoristaRepository;
        this.cavaloRepository = cavaloRepository;
        this.manutencaoRepository = manutencaoRepository;
        this.impostoRepository = impostoRepository;
        this.certificadoRepository = certificadoRepository;
        this.parcelaFrotaRepository = parcelaFrotaRepository;
        this.parcelaVidaRepository = parcelaVidaRepository;
        this.despesaAdmRepository = despesaAdmRepository;
    }

    public FreteRepository getFreteRepository() {
        return freteRepository;
    }

    public CustoDeAbastecimentoRepository getAbastecimentoRepository() {
        return abastecimentoRepository;
    }

    public CustoDePercursoRepository getPercursoRepository() {
        return percursoRepository;
    }

    public MotoristaRepository getMotoristaRepository() {
        return motoristaRepository;
    }

    public CavaloRepository getCavaloRepository() {
        return cavaloRepository;
    }

    public ManutencaoRepository getManutencaoRepository() {
        return manutencaoRepository;
    }

    public ImpostoRepository getImpostoRepository() {
        return impostoRepository;
    }

    public CertificadoRepository getCertificadoRepository() {
        return certificadoRepository;
    }

    public ParcelaSeguroFrotaRepository getParcelaFrotaRepository() {
        return parcelaFrotaRepository;
    }

    public ParcelaSeguroVidaRepository getParcelaVidaRepository() {
        return parcelaVidaRepository;
    }

    public DespesaAdmRepository getDespesaAdmRepository() {
        return despesaAdmRepository;
    }

    //----------------------------------------------------------------------------------------------

   /* public LiveData<ResourceData> buscaLucroLiquidoPorAnoEOuCavaloIdParaFragmentDesempenho(int ano, Long cavaloId){

        freteRepository.buscaFretesPorAnoEOuCavaloIdParaFragmentDesempenho(ano, cavaloId)


    }*/

}
