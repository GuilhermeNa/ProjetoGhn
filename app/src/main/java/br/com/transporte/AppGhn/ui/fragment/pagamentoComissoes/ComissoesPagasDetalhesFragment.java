package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_ADIANTAMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.SalarioDAO;
import br.com.transporte.AppGhn.databinding.FragmentComissoesPagasDetalhesBinding;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.AdiantamentoPagoAdapter;
import br.com.transporte.AppGhn.ui.adapter.FretePagoAdapter;
import br.com.transporte.AppGhn.ui.adapter.ReembolsoPagoAdapter;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class ComissoesPagasDetalhesFragment extends Fragment {
    public static final String VALOR_NEGATIVO = "(-) ";
    public static final String VALOR_POSITIVO = "(+) ";
    public static final String DETALHES_PAGAMENTO = "Detalhes Pagamento";
    private FragmentComissoesPagasDetalhesBinding binding;
    private TextView motoristaTxtView, placaTxtView, dataTxtView, valorAdiantamentosTxtView, valorReembolsosTxtView, valorFretesTxtView;
    private List<Adiantamento> listaDeAdiantamentos;
    private List<CustosDePercurso> listaDeReembolsos;
    private List<Frete> listaDeFretes;
    private CustosDeSalario salario;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdiantamentoDAO adiantamentoDao = new AdiantamentoDAO();
        CustosDePercursoDAO custosDao = new CustosDePercursoDAO();
        FreteDAO freteDao = new FreteDAO();
        SalarioDAO salarioDao = new SalarioDAO();

        int salarioId = (int) ComissoesPagasDetalhesFragmentArgs.fromBundle(getArguments()).getSalarioId();
        salario = salarioDao.localizaPeloId(salarioId);

        listaDeAdiantamentos = new ArrayList<>();
        for (Integer i : salario.getRefAdiantamentos()) {
            Adiantamento adiantamento = adiantamentoDao.localizaPeloId(i);
            listaDeAdiantamentos.add(adiantamento);
        }

        listaDeReembolsos = new ArrayList<>();
        for (Integer i : salario.getRefReembolsos()) {
            CustosDePercurso custo = custosDao.localizaPeloId(i);
            listaDeReembolsos.add(custo);
        }

        listaDeFretes = new ArrayList<>();
        for (Integer i : salario.getRefFretes()) {
            Frete frete = freteDao.localizaPeloId(i);
            listaDeFretes.add(frete);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentComissoesPagasDetalhesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecyclerAdiantamentos();
        configuraRecyclerReembolsos();
        configuraRecyclerFretes();
        configuraUi();
        configuraToolbar();
    }

    private void configuraUi() {
        CavaloDAO cavaloDao = new CavaloDAO();
        String placa = cavaloDao.localizaPeloId(salario.getRefCavalo()).getPlaca();
        String motorista = cavaloDao.localizaPeloId(salario.getRefCavalo()).getMotorista().getNome();

        BigDecimal somaAdiantamento = listaDeAdiantamentos.stream()
                .map(Adiantamento::getUltimoValorAbatido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal somaReembolso = listaDeReembolsos.stream()
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal somaFrete = listaDeFretes.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dataTxtView.setText(ConverteDataUtil.dataParaString(salario.getData()));
        placaTxtView.setText(placa);
        motoristaTxtView.setText(motorista);

        String valorAdiantamento = VALOR_NEGATIVO + FormataNumerosUtil.formataMoedaPadraoBr(somaAdiantamento);
        valorAdiantamentosTxtView.setText(valorAdiantamento);

        String valorReembolso = VALOR_POSITIVO + FormataNumerosUtil.formataMoedaPadraoBr(somaReembolso);
        valorReembolsosTxtView.setText(valorReembolso);

        valorFretesTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFrete));
    }

    private void configuraRecyclerFretes() {
        RecyclerView recycler = binding.recyclerFretes;
        FretePagoAdapter adapter = new FretePagoAdapter(this, listaDeFretes);
        recycler.setAdapter(adapter);
        recyclerLinhaVerticalDecoration(recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.requireContext());
        recycler.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(frete -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_FRETE);
            intent.putExtra(CHAVE_ID_CAVALO, ((Frete) frete).getRefCavaloId());
            intent.putExtra(CHAVE_ID, ((Frete) frete).getId());
            startActivity(intent);
        });

    }

    private void recyclerLinhaVerticalDecoration(@NonNull RecyclerView recycler) {
        Drawable divider = ContextCompat.getDrawable(requireContext(), R.drawable.divider);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(divider));
        recycler.addItemDecoration(itemDecoration);
    }

    private void configuraRecyclerReembolsos() {
        RecyclerView recycler = binding.recyclerReembolsos;
        ReembolsoPagoAdapter adapter = new ReembolsoPagoAdapter(this, listaDeReembolsos);
        recycler.setAdapter(adapter);
        recyclerLinhaVerticalDecoration(recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.requireContext());
        recycler.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(custo -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
            intent.putExtra(CHAVE_ID_CAVALO, ((CustosDePercurso) custo).getRefCavalo());
            intent .putExtra(CHAVE_ID, ((CustosDePercurso) custo).getId());
            startActivity(intent);
        });
    }

    private void configuraRecyclerAdiantamentos() {
        RecyclerView recycler = binding.recyclerAdiantamentos;
        AdiantamentoPagoAdapter adapter = new AdiantamentoPagoAdapter(this, listaDeAdiantamentos);
        recycler.setAdapter(adapter);
        recyclerLinhaVerticalDecoration(recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.requireContext());
        recycler.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(adiantamento -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_ADIANTAMENTO);
            intent.putExtra(CHAVE_ID_CAVALO, ((Adiantamento) adiantamento).getRefCavaloId());
            intent.putExtra(CHAVE_ID, ((Adiantamento) adiantamento).getId());
            startActivity(intent);
        });
    }

    private void inicializaCamposDaView() {
        motoristaTxtView = binding.motorista;
        placaTxtView = binding.placa;
        dataTxtView = binding.dataPagamento;
        valorAdiantamentosTxtView = binding.totalAdiantamento;
        valorReembolsosTxtView = binding.totalReembolso;
        valorFretesTxtView = binding.totalFrete;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(DETALHES_PAGAMENTO);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_padrao, menu);
                menu.removeItem(R.id.menu_padrao_search);
                menu.removeItem(R.id.menu_padrao_editar);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        NavController controlador = Navigation.findNavController(requireView());
                        controlador.popBackStack();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);


    }
}