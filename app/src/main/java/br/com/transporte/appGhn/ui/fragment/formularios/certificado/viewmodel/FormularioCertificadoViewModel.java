package br.com.transporte.appGhn.ui.fragment.formularios.certificado.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.model.enums.TipoFormulario;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CertificadoRepository;
import br.com.transporte.appGhn.repository.Resource;

public class FormularioCertificadoViewModel extends ViewModel {
    private final CertificadoRepository repository;
    private final CavaloRepository cavaloRepository;
    public FormularioCertificadoViewModel(CertificadoRepository repository, CavaloRepository repositoryCavalo) {
        this.repository = repository;
        this.cavaloRepository = repositoryCavalo;
    }

    //----------------------------------------------------------------------------------------------
    //public DespesaCertificado certificadoArmazenado;
    private Long idCertificadoCarregado;
    private List<String> listaDePlacas;
    private List<String> listaDeCertificados;
    private Cavalo cavaloArmazenado;
    private DespesaCertificado certificadoArmazenado;
    private DespesaCertificado certificadoRenovado;
    private TipoDespesa tipoDespesa;
    private TipoFormulario tipoFormulario;

    //----------------------------------------------------------------------------------------------
    //                                 Métodos para carregar dados                                ||
    //----------------------------------------------------------------------------------------------

    public LiveData<DespesaCertificado> localizaCertificado(final Long id) {
        return repository.localizaCertificado(id);
    }

    public LiveData<Long> salva(@NonNull final DespesaCertificado certificado){
        if(certificado.getId() == null){
            return repository.adicionaCertificado(certificado);
        } else {
            return repository.editaCertificado(certificado);
        }
    }

    public LiveData<String> deleta(){
        if(certificadoArmazenado != null){
            return repository.deletaCertificado(certificadoArmazenado);
        } else {
            return new MutableLiveData<>("Não foi possivel localizar Certificado");
        }
    }
    public LiveData<List<String>> buscaPlacas(){
        return cavaloRepository.buscaPlacas();
    }

    public LiveData<Cavalo> localizaCavalo(final Long id){
        return cavaloRepository.localizaCavaloPeloId(id);
    }

    public LiveData<Cavalo> localizaPelaPlaca(final String placa){
        return cavaloRepository.localizaPelaPlaca(placa);
    }

    public LiveData<Resource<List<DespesaCertificado>>> buscaCertificados() {
        return repository.buscaCertificados();
    }

    public LiveData<List<DespesaCertificado>> buscaCertificadoPorTipo(final TipoDespesa tipo){
        return repository.buscaPorTipo(tipo);
    }

    public LiveData<List<DespesaCertificado>> buscaCertificadosPorCavaloId(final Long id){
        return repository.buscaPorCavaloId(id);
    }

    public LiveData<Long> renova(
            final DespesaCertificado certificadoCriado,
            final DespesaCertificado certificadoRenovado
    ) {
        repository.editaCertificado(certificadoRenovado);
        return repository.adicionaCertificado(certificadoCriado);
    }



    //----------------------------------------------------------------------------------------------
    //                                      Getters & Setters                                     ||
    //----------------------------------------------------------------------------------------------

    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public TipoFormulario getTipoFormulario() {
        return tipoFormulario;
    }

    public void setTipoFormulario(TipoFormulario tipoFormulario) {
        this.tipoFormulario = tipoFormulario;
    }

    public Long getIdCertificadoCarregado() {
        return idCertificadoCarregado;
    }

    public void setIdCertificadoCarregado(Long idCertificadoCarregado) {
        this.idCertificadoCarregado = idCertificadoCarregado;
    }

    public DespesaCertificado getCertificadoArmazenado() {
        return certificadoArmazenado;
    }

    public void setCertificadoArmazenado(DespesaCertificado certificadoArmazenado) {
        this.certificadoArmazenado = certificadoArmazenado;
    }

    public List<String> getListaDePlacas() {
        return listaDePlacas;
    }

    public void setListaDePlacas(List<String> listaDePlacas) {
        this.listaDePlacas = listaDePlacas;
    }

    public DespesaCertificado getCertificadoRenovado() {
        return certificadoRenovado;
    }

    public void setCertificadoRenovado(DespesaCertificado certificadoRenovado) {
        this.certificadoRenovado = certificadoRenovado;
    }

    public Cavalo getCavaloArmazenado() {
        return cavaloArmazenado;
    }

    public void setCavaloArmazenado(Cavalo cavaloArmazenado) {
        this.cavaloArmazenado = cavaloArmazenado;
    }

    public List<String> getListaDeCertificados() {
        return listaDeCertificados;
    }

    public void setListaDeCertificados(List<String> listaDeCertificados) {
        this.listaDeCertificados = listaDeCertificados;
    }

}
