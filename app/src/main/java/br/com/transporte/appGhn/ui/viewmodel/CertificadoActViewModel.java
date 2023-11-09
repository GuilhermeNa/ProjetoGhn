package br.com.transporte.appGhn.ui.viewmodel;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CertificadoRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.repository.Resource;
import br.com.transporte.appGhn.ui.fragment.certificados.helpers.TipoDeRequisicao_dataSet;

public class CertificadoActViewModel extends ViewModel {
    private final CertificadoRepository certificadoRepository;
    private final CavaloRepository cavaloRepository;
    private final MotoristaRepository motoristaRepository;
    private final CertificadoIndiretoResource resourceCertificadoIndireto;
    private final CertificadoDiretoResource resourceCertificadoDireto;

    public CertificadoActViewModel(CertificadoRepository certificadoRepository, CavaloRepository cavaloRepository, MotoristaRepository motoristaRepository) {
        this.certificadoRepository = certificadoRepository;
        this.cavaloRepository = cavaloRepository;
        this.motoristaRepository = motoristaRepository;
        resourceCertificadoDireto = new CertificadoDiretoResource();
        resourceCertificadoIndireto = new CertificadoIndiretoResource();
    }

    public List<DespesaCertificado> getDataCertificadoDireto() {
        if (resourceCertificadoDireto != null) {
            return resourceCertificadoDireto.getDataCertificadoDireto(certificadosArmazenados);
        } else {
            return null;
        }
    }

    public List<DespesaCertificado> getDataCertificadoIndireto() {
        if (resourceCertificadoIndireto != null) {
            return resourceCertificadoIndireto.getDataCertificadoIndireto(certificadosArmazenados);
        } else {
            return null;
        }
    }

    public void defineParametrosDaRequisicaoCertificadoDireto(
            final TipoDeRequisicao_dataSet tipo,
            final int ano,
            final Long cavaloId
    ) {
        if (tipo != null) {
            resourceCertificadoDireto.setTipoDeRequisicao(tipo);
        }
        if (ano != 0) {
            resourceCertificadoDireto.setAnoRequisitado(ano);
        }
        if (cavaloId != null) {
            resourceCertificadoDireto.setCavaloId(cavaloId);
        }
    }

    public void defineParametrosDaRequisicaoCertificadoIndireto(
            final TipoDeRequisicao_dataSet tipo,
            final int ano
    ) {
        if (tipo != null) {
            resourceCertificadoIndireto.setTipoDeRequisicao(tipo);
        }
        if (ano != 0) {
            resourceCertificadoIndireto.setAnoRequisitado(ano);
        }
    }

    //----------------------------------------------------------------------------------------------
    public List<DespesaCertificado> certificadosArmazenados;
    public List<Cavalo> cavalosArmazenados;
    public List<Motorista> motoristasArmazenados;


    public LiveData<Resource<List<Cavalo>>> buscaCavalos() {
        return cavaloRepository.buscaCavalos();
    }

    public LiveData<Resource<List<DespesaCertificado>>> buscaCertificados() {
        return certificadoRepository.buscaCertificados();
    }

    public LiveData<Resource<List<Motorista>>> buscaMotoristas() {
        return motoristaRepository.buscaMotoristas();
    }

}

class CertificadoDiretoResource {
    private TipoDeRequisicao_dataSet tipoDeRequisicao;
    private int anoRequisitado;
    private Long cavaloId;

    public void setTipoDeRequisicao(TipoDeRequisicao_dataSet tipoDeRequisicao) {
        this.tipoDeRequisicao = tipoDeRequisicao;
    }

    public void setAnoRequisitado(int anoRequisitado) {
        this.anoRequisitado = anoRequisitado;
    }

    public void setCavaloId(Long cavaloId) {
        this.cavaloId = cavaloId;
    }

    //----------------------------------------------------------------------------------------------

    public List<DespesaCertificado> getDataCertificadoDireto(final List<DespesaCertificado> dataSet) {
        List<DespesaCertificado> lista = FiltraDespesasCertificado.listaPorCavaloId(dataSet, cavaloId);
        switch (tipoDeRequisicao) {
            case ATIVOS:
                lista = FiltraDespesasCertificado.listaPorStatus(lista, true);
                break;

            case TODOS:
                lista = FiltraDespesasCertificado.listaPorTipoDespesa(lista, DIRETA);
                break;

            case TODOS_E_ANO:
                lista = FiltraDespesasCertificado.listaPorAno(lista, anoRequisitado);
                break;
        }
        return lista;
    }

}

class CertificadoIndiretoResource {
    private TipoDeRequisicao_dataSet tipoDeRequisicao;
    private int anoRequisitado;

    public void setTipoDeRequisicao(TipoDeRequisicao_dataSet tipoDeRequisicao) {
        this.tipoDeRequisicao = tipoDeRequisicao;
    }

    public void setAnoRequisitado(int anoRequisitado) {
        this.anoRequisitado = anoRequisitado;
    }

    //----------------------------------------------------------------------------------------------

    public List<DespesaCertificado> getDataCertificadoIndireto(final List<DespesaCertificado> dataSet) {
        List<DespesaCertificado> lista = FiltraDespesasCertificado.listaPorTipoDespesa(dataSet, INDIRETA);
        switch (tipoDeRequisicao) {
            case ATIVOS:
                lista = FiltraDespesasCertificado.listaPorStatus(lista, true);
                break;

            case TODOS:
                //Lista já vem com todos por padrão.
                break;

            case TODOS_E_ANO:
                lista = FiltraDespesasCertificado.listaPorAno(lista, anoRequisitado);
                break;
        }
        return lista;
    }
}