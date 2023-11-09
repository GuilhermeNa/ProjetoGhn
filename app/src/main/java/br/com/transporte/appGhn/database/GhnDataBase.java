package br.com.transporte.appGhn.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.com.transporte.appGhn.database.conversor.ConversorBigDecimal;
import br.com.transporte.appGhn.database.conversor.ConversorIntegerList;
import br.com.transporte.appGhn.database.conversor.ConversorLocalDate;
import br.com.transporte.appGhn.database.conversor.ConversorLongList;
import br.com.transporte.appGhn.database.conversor.ConversorTipoAbastecimento;
import br.com.transporte.appGhn.database.conversor.ConversorTipoCertificado;
import br.com.transporte.appGhn.database.conversor.ConversorTipoCustoManutencao;
import br.com.transporte.appGhn.database.conversor.ConversorTipoCustoPercurso;
import br.com.transporte.appGhn.database.conversor.ConversorTipoDespesa;
import br.com.transporte.appGhn.database.conversor.ConversorTipoImposto;
import br.com.transporte.appGhn.database.conversor.ConversorTipoRecebimento;
import br.com.transporte.appGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.appGhn.database.dao.RoomCavaloDao;
import br.com.transporte.appGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.appGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.appGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.appGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.appGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.appGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.appGhn.database.dao.RoomDespesaComSeguroFrotaDao;
import br.com.transporte.appGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.appGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.appGhn.database.dao.RoomFreteDao;
import br.com.transporte.appGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.appGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.appGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.appGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.appGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.appGhn.model.RecebimentoDeFrete;
import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.model.abstracts.Custos;
import br.com.transporte.appGhn.model.abstracts.CustosEDespesas;
import br.com.transporte.appGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.appGhn.model.abstracts.Despesas;
import br.com.transporte.appGhn.model.abstracts.Frota;
import br.com.transporte.appGhn.model.abstracts.Parcela;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.model.custos.CustosDeSalario;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;

@TypeConverters({
        ConversorBigDecimal.class,
        ConversorLocalDate.class,
        ConversorTipoCustoPercurso.class,
        ConversorTipoAbastecimento.class,
        ConversorTipoCustoManutencao.class,
        ConversorTipoDespesa.class,
        ConversorTipoRecebimento.class,
        ConversorTipoCertificado.class,
        ConversorTipoImposto.class,
        ConversorIntegerList.class,
        ConversorLongList.class
})
@Database(entities = {
        Frota.class,
        Cavalo.class,
        SemiReboque.class,
        Motorista.class,
        //------------
        Frete.class,
        Adiantamento.class,
        RecebimentoDeFrete.class,
        //------------
        CustosEDespesas.class,
        Custos.class,
        Despesas.class,
        DespesaComSeguro.class,
        Parcela.class,
        //------------
        CustosDePercurso.class,
        CustosDeAbastecimento.class,
        CustosDeManutencao.class,
        CustosDeSalario.class,
        //------------
        DespesaAdm.class,
        DespesaCertificado.class,
        DespesaComSeguroDeVida.class,
        DespesaComSeguroFrota.class,
        DespesasDeImposto.class,
        Parcela_seguroFrota.class,
        Parcela_seguroVida.class
},
        version = 12, exportSchema = false)
public abstract class GhnDataBase extends RoomDatabase {
    private static final String GHN_DB = "ghn.db";
    private static GhnDataBase instance = null;

    //----------------------------------------------------------------------------------------------

    @NonNull
    public static GhnDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, GhnDataBase.class, GHN_DB)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------------

    public abstract RoomCavaloDao getRoomCavaloDao();

    public abstract RoomMotoristaDao getRoomMotoristaDao();

    public abstract RoomSemiReboqueDao getRoomReboqueDao();

    public abstract RoomCustosPercursoDao getRoomCustosPercursoDao();

    public abstract RoomCustosAbastecimentoDao getRoomCustosAbastecimentoDao();

    public abstract RoomAdiantamentoDao getRoomAdiantamentoDao();

    public abstract RoomCustosDeManutencaoDao getRoomCustosDeManutencaoDao();

    public abstract RoomCustosDeSalarioDao getRoomCustosDeSalarioDao();

    public abstract RoomDespesaAdmDao getRoomDespesaAdmDao();

    public abstract RoomDespesaCertificadoDao getRoomDespesaCertificadoDao();

    public abstract RoomDespesaSeguroVidaDao getRoomDespesaSeguroVidaDao();

    public abstract RoomDespesaComSeguroFrotaDao getRoomDespesaComSeguroFrotaDao();

    public abstract RoomDespesaImpostoDao getRoomDespesaImpostoDao();

    public abstract RoomParcela_seguroFrotaDao getRoomParcela_seguroFrotaDao();

    public abstract RoomFreteDao getRoomFreteDao();

    public abstract RoomRecebimentoFreteDao getRoomRecebimentoFreteDao();

    public abstract RoomParcela_seguroVidaDao getRoomParcela_seguroVidaDao();

}
