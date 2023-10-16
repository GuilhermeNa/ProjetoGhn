package br.com.transporte.AppGhn.model.abstracts;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public abstract class Frota implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String placa;
    private String documentoEmString;
    private String marcaModelo;
    private String ano;
    private String modelo;
    private String cor;
    private String renavam;
    private String chassi;


    public String getDocumentoEmString() {
        return documentoEmString;
    }

    public void setDocumentoEmString(String documentoEmString) {
        this.documentoEmString = documentoEmString;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarcaModelo() {
        return marcaModelo;
    }

    public void setMarcaModelo(String marcaModelo) {
        this.marcaModelo = marcaModelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return getPlaca();
    }

    public boolean temIdValido() {
        return id > 0;
    }
}
