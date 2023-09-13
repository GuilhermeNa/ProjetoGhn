package br.com.transporte.AppGhn.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.com.transporte.AppGhn.database.conversor.ConversorBigDecimal;
import br.com.transporte.AppGhn.database.conversor.ConversorIntegerList;
import br.com.transporte.AppGhn.database.conversor.ConversorLocalDate;
import br.com.transporte.AppGhn.database.conversor.ConversorLongList;
import br.com.transporte.AppGhn.database.conversor.ConversorTipoAbastecimento;
import br.com.transporte.AppGhn.database.conversor.ConversorTipoCertificado;
import br.com.transporte.AppGhn.database.conversor.ConversorTipoCustoManutencao;
import br.com.transporte.AppGhn.database.conversor.ConversorTipoCustoPercurso;
import br.com.transporte.AppGhn.database.conversor.ConversorTipoDespesa;
import br.com.transporte.AppGhn.database.conversor.ConversorTipoImposto;
import br.com.transporte.AppGhn.database.conversor.ConversorTipoRecebimento;
import br.com.transporte.AppGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaComSeguroFrotaDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.AppGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.model.abstracts.Custos;
import br.com.transporte.AppGhn.model.abstracts.CustosEDespesas;
import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.abstracts.Despesas;
import br.com.transporte.AppGhn.model.abstracts.Frota;
import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;

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
