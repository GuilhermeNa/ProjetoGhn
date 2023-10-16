package br.com.transporte.AppGhn.model.parcelas.factory;

public class ParcelasFactory {
    public static final int LIMITE_DE_PARCELAS = 12;

    public static int configQuantidadeDeParcelas(int parcelas) {
        if (parcelas > 0 && parcelas <= LIMITE_DE_PARCELAS) {
            return parcelas;
        }
        return LIMITE_DE_PARCELAS;
    }

}
