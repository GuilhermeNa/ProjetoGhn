package br.com.transporte.AppGhn.database.conversor;

import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.COFINS;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.CSLL;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.FGTS;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.ICMS;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.INSS;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.IPI;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.IPVA;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.IRPJ;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.IRRF;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.ISS;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.PASEP;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.PIS;
import static br.com.transporte.AppGhn.model.enums.TipoDeImposto.SIMPLES_NAC;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.AppGhn.model.enums.TipoDeImposto;

public class ConversorTipoImposto {

    @TypeConverter
    public String paraString(TipoDeImposto tipo) {
        if (tipo != null)
            return tipo.getDescricao();
        else
            return null;
    }

    @TypeConverter
    public TipoDeImposto paraTipo(String tipo) {
        if (tipo != null) {
            switch (tipo) {
                case "IRRF":
                    return IRRF;
                case "INSS":
                    return INSS;
                case "FGTS":
                    return FGTS;
                case "SIMPLES_NAC":
                    return SIMPLES_NAC;
                case "IPVA":
                    return IPVA;
                case "ICMS":
                    return ICMS;
                case "ISS":
                    return ISS;
                case "PIS":
                    return PIS;
                case "PASEP":
                    return PASEP;
                case "CSLL":
                    return CSLL;
                case "COFINS":
                    return COFINS;
                case "IPI":
                    return IPI;
                case "IRPJ":
                    return IRPJ;
            }
        }
        return null;
    }

}
