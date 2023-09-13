package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesPagasFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.OnItemClickListenerNew;

public class SalariosAdapter extends RecyclerView.Adapter<SalariosAdapter.ViewHolder> {
    private final ComissoesPagasFragment context;
    private final List<CustosDeSalario> lista;
    private final RoomFreteDao freteDao;
    private final RoomAdiantamentoDao adiantamentoDao;
    private final RoomCustosPercursoDao custosDePercursoDao;
    private final RoomCavaloDao cavaloDao;
    private final RoomMotoristaDao motoristaDao;
    private OnItemClickListenerNew onItemClickListener;
    private BigDecimal totalAdiantamentos, totalReembolsos, totalFretes;

    public SalariosAdapter(@NonNull ComissoesPagasFragment context, List<CustosDeSalario> lista) {
        this.context = context;
        this.lista = lista;
        GhnDataBase dataBase = GhnDataBase.getInstance(context.requireContext());
        freteDao = dataBase.getRoomFreteDao();
        cavaloDao = dataBase.getRoomCavaloDao();
        adiantamentoDao = dataBase.getRoomAdiantamentoDao();
        custosDePercursoDao = dataBase.getRoomCustosPercursoDao();
        motoristaDao = dataBase.getRoomMotoristaDao();
    }

    public void setOnItemClickListener(OnItemClickListenerNew onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView placaTxtView, nomeTxtView, comissaoPagaTituloTxtView, totalPagoTxtView,
                adiantamentoTxtView, dataTxtView, reembolsoTxtView, comissaoTxtView, totalPagoTituloTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_salarios_data);
            placaTxtView = itemView.findViewById(R.id.rec_item_salarios_placa);
            nomeTxtView = itemView.findViewById(R.id.rec_item_salarios_nome);
            comissaoPagaTituloTxtView = itemView.findViewById(R.id.rec_item_salarios_comissao_devida_titulo);
            totalPagoTituloTxtView = itemView.findViewById(R.id.rec_item_salarios_pago_titulo);
            totalPagoTxtView = itemView.findViewById(R.id.rec_item_salarios_total);
            adiantamentoTxtView = itemView.findViewById(R.id.rec_item_salarios_descontos);
            reembolsoTxtView = itemView.findViewById(R.id.rec_item_salarios_reembolso);
            comissaoTxtView = itemView.findViewById(R.id.rec_item_salarios_pago);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public SalariosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_comissoes, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull SalariosAdapter.ViewHolder holder, int position) {
        CustosDeSalario salario = lista.get(position);
        configuraUi(holder);
        vincula(holder, salario);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(salario.getId()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    private void configuraUi(@NonNull ViewHolder holder) {
        holder.dataTxtView.setVisibility(View.VISIBLE);
        holder.totalPagoTituloTxtView.setText(R.string.total_pago);
        holder.comissaoPagaTituloTxtView.setText(R.string.comissao_paga);
    }

    private void vincula(@NonNull ViewHolder holder, CustosDeSalario salario) {
        calculaValoresParaExibir(salario);

        String placa = cavaloDao.localizaPeloId(salario.getRefCavaloId()).getPlaca();
        holder.placaTxtView.setText(placa);

        String nome = motoristaDao.localizaPeloId(salario.getRefMotoristaId()).getNome();
        holder.nomeTxtView.setText(nome);

        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(salario.getData()));
        holder.totalPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(salario.getValorCusto()));
        holder.adiantamentoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(totalAdiantamentos));
        holder.reembolsoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(totalReembolsos));
        holder.comissaoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(totalFretes));

    }

    private void calculaValoresParaExibir(@NonNull CustosDeSalario salario) {
        BigDecimal valorEncontrado;

        List<BigDecimal> listaAdiantamentos = new ArrayList<>();
        for (Long i : salario.getRefAdiantamentos()) {
            valorEncontrado = adiantamentoDao.localizaPeloId(i).getUltimoValorAbatido();
            listaAdiantamentos.add(valorEncontrado);
        }

        List<BigDecimal> listaReembolsos = new ArrayList<>();
        for (Long i : salario.getRefReembolsos()) {
            valorEncontrado = custosDePercursoDao.localizaPeloId(i).getValorCusto();
            listaReembolsos.add(valorEncontrado);
        }

        List<BigDecimal> listaFretes = new ArrayList<>();
        for (Long i : salario.getRefFretes()) {
            valorEncontrado = freteDao.localizaPeloId(i).getComissaoAoMotorista();
            listaFretes.add(valorEncontrado);
        }

        totalAdiantamentos = listaAdiantamentos.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalReembolsos = listaReembolsos.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFretes = listaFretes.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<CustosDeSalario> listaDeSalariosPagos) {
        this.lista.clear();
        this.lista.addAll(listaDeSalariosPagos);
        notifyDataSetChanged();
    }

}
