package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes;

import static android.app.Activity.RESULT_CANCELED;
import static android.view.View.GONE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ADIANTAMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_ADIANTAMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DEFAUT;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentComissoesDetalhesBinding;
import br.com.transporte.AppGhn.exception.ValorInvalidoException;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.Salario;
import br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.DetalhesAdiantamentoAdapter;
import br.com.transporte.AppGhn.ui.adapter.DetalhesFreteAdapter;
import br.com.transporte.AppGhn.ui.adapter.DetalhesReembolsoAdapter;
import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.SalarioDAO;
import br.com.transporte.AppGhn.ui.dialog.AlteraComissao;
import br.com.transporte.AppGhn.ui.dialog.DescontaAdiantamento;
import br.com.transporte.AppGhn.util.CentralSalariosEComissoes;
import br.com.transporte.AppGhn.util.DatePickerUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ComissoesDetalhesFragment extends Fragment implements MenuProvider {
    private FragmentComissoesDetalhesBinding binding;
    private TextView comissaoTxt, liquidoTxt, reembolsoTxt, adiantamentoDescontarTxt, placaTxt, nomeTxt;
    private BigDecimal comissaoAcumulada, reembolsoAcumulado, descontoAcumulado, liquidoAFechar;
    private TipoDeAdapterPressionado tipoDeAdapterPressionado;
    private List<Adiantamento> listaAdiantamentosRecycler;
    private List<CustosDePercurso> listaReembolsoRecycler;
    private List<Frete> listaFreteRecycler;
    private Map<Integer, BigDecimal> map;
    private AdiantamentoDAO adiantamentoDao;
    private CustosDePercursoDAO custosDao;
    private FreteDAO freteDao;
    private DetalhesAdiantamentoAdapter adapterAdiantamento;
    private DetalhesReembolsoAdapter adapterReembolso;
    private DetalhesFreteAdapter adapterFrete;
    private Cavalo cavalo;
    private Button btn;
    private final ActivityResultLauncher<Intent> activityResultLauncherAdiantamento = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                Intent dataResultado = result.getData();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        Adiantamento adiantamentoRecebido = (Adiantamento) dataResultado.getSerializableExtra(CHAVE_ADIANTAMENTO);
                        substituiValorNoMap(adiantamentoRecebido.getId(), adiantamentoRecebido.restaReembolsar());
                        atualizaAdiantamentoAposRetornoDeResult("Registro editado");
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        int key = dataResultado.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        map.remove(key);
                        atualizaAdiantamentoAposRetornoDeResult("Registro apagado");
                        break;
                }
            });
    private final ActivityResultLauncher<Intent> activityResultLauncherCustosReembolsaveis = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                Intent dataResultado = result.getData();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        atualizaCustosReembolsaveisAposRetornoDeResult("Registro editado");
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        atualizaCustosReembolsaveisAposRetornoDeResult("Registro apagado");
                        break;
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void atualizaAdiantamentoAposRetornoDeResult(String msg) {
        listaAdiantamentosRecycler = getListaAdiantamentoEmAbertoPorPlaca();
        adapterAdiantamento.atualiza(listaAdiantamentosRecycler);
        uiMutavel_atualizaAdiantamento();
        uiMutavel_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void atualizaCustosReembolsaveisAposRetornoDeResult(String msg) {
        listaReembolsoRecycler = getListaReembolsoRecycler();
        adapterReembolso.atualiza(listaReembolsoRecycler);
        uiMutavel_atualizaReembolso();
        uiMutavel_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adiantamentoDao = new AdiantamentoDAO();
        custosDao = new CustosDePercursoDAO();
        freteDao = new FreteDAO();
        map = new HashMap<>();
        cavalo = recebeIdArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentComissoesDetalhesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();

        configuraToolbar();
        configuraRecyclerAdiantamento();
        configuraRecyclerReembolso();
        configuraRecyclerFrete();

        configuraUi();
        configuraBtn(view);

    }

    private Map<Integer, BigDecimal> criaMapAdiantamento() {
        for (Adiantamento a : listaAdiantamentosRecycler) {
            map.put(a.getId(), a.restaReembolsar());
        }
        return map;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Detalhes para pagamento");
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraBtn(@NonNull View view) {
        int compare = comissaoAcumulada.compareTo(BigDecimal.ZERO);
        if (compare == 0) {
            btn.setVisibility(GONE);
        }


        btn.setOnClickListener(v ->
                new AlertDialog.Builder(getContext())
                        .setTitle("Fechamento")
                        .setMessage(FormataNumerosUtil.formataMoedaPadraoBr(liquidoAFechar))
                        .setPositiveButton("Confirmar", (dialog, which) -> {
                            Salario salario = new Salario();

                            for (Frete f : listaFreteRecycler) {
                                f.getAdmFrete().setComissaoJaFoiPaga(true);
                                f.setApenasAdmEdita(true);
                                freteDao.edita(f);
                                salario.listaFretesAdiciona(f.getId());
                            }

                            for (Adiantamento a : listaAdiantamentosRecycler) {
                                if (map.containsKey(a.getId())) {
                                    BigDecimal bd = map.get(a.getId());
                                    try {
                                        a.restituirValorPagoComoAdiantamento(bd);
                                        a.setUltimoValorAbatido(bd);
                                        salario.listaAdiantamentosAdiciona(a.getId());
                                    } catch (ValorInvalidoException e) {
                                        e.printStackTrace();
                                        e.getMessage();
                                    }
                                }

                                adiantamentoDao.edita(a);
                            }

                            for (CustosDePercurso c : listaReembolsoRecycler) {
                                c.setTipo(TipoCustoDePercurso.REEMBOLSAVEL_JA_PAGO);
                                custosDao.edita(c);
                                salario.listaReembolsosAdiciona(c.getId());
                            }

                            LocalDate dataDoPagamento = DatePickerUtil.capturaDataDeHojeParaConfiguracaoinicial();
                            salario.setData(dataDoPagamento);
                            salario.setRefCavalo(cavalo.getId());
                            salario.setValorCusto(liquidoAFechar);
                            salario.setRefMotorista(cavalo.getMotorista().getId());
                            SalarioDAO salarioDao = new SalarioDAO();
                            salarioDao.adiciona(salario);

                            adapterFrete.atualiza(listaFreteRecycler);
                            adapterAdiantamento.atualiza(listaAdiantamentosRecycler);
                            adapterReembolso.atualiza(listaReembolsoRecycler);

                            NavController controlador = Navigation.findNavController(view);
                            controlador.popBackStack();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraUi() {
        try {
            nomeTxt.setText(cavalo.getMotorista().getNome());
        } catch (NullPointerException e) {
            e.printStackTrace();
            nomeTxt.setText("S/M");
        }
        placaTxt.setText(cavalo.getPlaca());

        configuraUiMutavel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraUiMutavel() {
        comissaoAcumulada = uiMutavel_atualizaComissao();
        reembolsoAcumulado = uiMutavel_atualizaReembolso();
        descontoAcumulado = uiMutavel_atualizaAdiantamento();
        uiMutavel_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
    }

    private void uiMutavel_atualizaLiquidoAFechar(BigDecimal comissaoAcumulada, BigDecimal reembolsoAcumulado, BigDecimal descontoAcumulado) {
        liquidoAFechar = comissaoAcumulada.add(reembolsoAcumulado).subtract(descontoAcumulado);
        liquidoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(liquidoAFechar));
    }

    private BigDecimal uiMutavel_atualizaComissao() {
        List<Frete> fretesRefCavaloQueEstaSendoVisualizado = freteDao.listaFiltradaPorCavalo(cavalo.getId());
        comissaoAcumulada = CentralSalariosEComissoes.getComissaoEmAberto(fretesRefCavaloQueEstaSendoVisualizado);
        comissaoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoAcumulada));
        return comissaoAcumulada;
    }

    private BigDecimal uiMutavel_atualizaReembolso() {
        reembolsoAcumulado = listaReembolsoRecycler.stream().map(CustosDePercurso::getValorCusto).reduce(BigDecimal.ZERO, BigDecimal::add);
        reembolsoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(reembolsoAcumulado));

        LinearLayout layout = binding.reembolsoLayout;
        defineSeRecyclerSeraExibida(reembolsoAcumulado, layout);
        return reembolsoAcumulado;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private BigDecimal uiMutavel_atualizaAdiantamento() {
        descontoAcumulado = map.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        adiantamentoDescontarTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(descontoAcumulado));

        LinearLayout layout = binding.adiantamentoLayout;
        defineSeRecyclerSeraExibida(descontoAcumulado, layout);
        return descontoAcumulado;
    }

    private void defineSeRecyclerSeraExibida(BigDecimal bD, LinearLayout layout) {
        int compare = bD.compareTo(BigDecimal.ZERO);
        if (compare == 0) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
    }

    private List<Adiantamento> getListaAdiantamentoEmAbertoPorPlaca() {
        return adiantamentoDao.listaPorCavaloEAberto(cavalo.getId());
    }

    private void inicializaCampos() {
        btn = binding.btn;
        placaTxt = binding.placa;
        nomeTxt = binding.motorista;
        liquidoTxt = binding.liquidoValor;
        adiantamentoDescontarTxt = binding.descontoValor;
        comissaoTxt = binding.comissaoValor;
        reembolsoTxt = binding.reembolsoValor;
    }

    private Cavalo recebeIdArguments() {
        CavaloDAO dao = new CavaloDAO();
        int cavaloId = (int) ComissoesDetalhesFragmentArgs.fromBundle(getArguments()).getCavaloId();
        cavalo = dao.localizaPeloId(cavaloId);
        return cavalo;
    }

    private void configuraRecyclerFrete() {
        RecyclerView recyclerView = binding.comissaoRecycler;
        listaFreteRecycler = freteDao.listaPorPlacaEComissaoAberta(cavalo.getId());

        adapterFrete = new DetalhesFreteAdapter(this, listaFreteRecycler);
        recyclerView.setAdapter(adapterFrete);

        LinearLayoutManager layoutManagerHorizontal1 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerHorizontal1);

        adapterFrete.setOnLongClickListener(t -> tipoDeAdapterPressionado = t);
    }

    private void configuraRecyclerReembolso() {
        RecyclerView recyclerView = binding.reembolsoRecycler;
        listaReembolsoRecycler = getListaReembolsoRecycler();

        adapterReembolso = new DetalhesReembolsoAdapter(this, listaReembolsoRecycler);
        recyclerView.setAdapter(adapterReembolso);

        LinearLayoutManager layoutManagerHorizontal1 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerHorizontal1);

        adapterReembolso.setOnItemClickListener(custosReembolsaveis -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
            intent.putExtra(CHAVE_ID, ((CustosDePercurso) custosReembolsaveis).getId());
            activityResultLauncherCustosReembolsaveis.launch(intent);
        });
    }

    private List<CustosDePercurso> getListaReembolsoRecycler() {
        return custosDao.listaPorCavaloEAberto(cavalo.getId());
    }

    private void configuraRecyclerAdiantamento() {
        RecyclerView recyclerView = binding.adiantamentoRecycler;
        listaAdiantamentosRecycler = getListaAdiantamentoEmAbertoPorPlaca();
        map = criaMapAdiantamento();

        adapterAdiantamento = new DetalhesAdiantamentoAdapter(listaAdiantamentosRecycler, this, map);
        recyclerView.setAdapter(adapterAdiantamento);

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerHorizontal);

        adapterAdiantamento.setOnItemClickListener(adiantamento -> {
            Intent intent = new Intent(requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_ADIANTAMENTO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            intent.putExtra(CHAVE_ID, ((Adiantamento) adiantamento).getId());
            activityResultLauncherAdiantamento.launch(intent);
        });

        adapterAdiantamento.setOnLongClickListener(t -> tipoDeAdapterPressionado = t);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;

        switch (tipoDeAdapterPressionado) {
            case ADIANTAMENTO:
                posicao = adapterAdiantamento.getPosicao();
                Adiantamento adiantamento = listaAdiantamentosRecycler.get(posicao);
                if (item.getItemId() == R.id.editarDesconto) {
                    DescontaAdiantamento descontaAdiantamento = new DescontaAdiantamento(this.getContext(), adiantamento);
                    descontaAdiantamento.dialogDescontaAdiantamento();
                    descontaAdiantamento.setCallback(new DescontaAdiantamento.Callback() {
                        @Override
                        public void quandoFunciona(int id, BigDecimal novoValor, String msg) {
                            substituiValorNoMap(id, novoValor);
                            adapterAdiantamento.atualizaMap();
                            uiMutavel_atualizaAdiantamento();
                            uiMutavel_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void quandoFalha(String msg) {
                            MensagemUtil.snackBar(getView(), msg);
                        }

                        @Override
                        public void quandoCancela(String msg) {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

            case FRETE:
                posicao = adapterFrete.getPosicao();
                Frete frete = listaFreteRecycler.get(posicao);
                if (item.getItemId() == R.id.editarComissao) {
                    AlteraComissao alteraComissao = new AlteraComissao(this.getContext(), frete);
                    alteraComissao.dialogAlteraComissao();
                    int posicaoTemporaria = posicao;
                    alteraComissao.setCallback(new AlteraComissao.Callback() {
                        @Override
                        public void quandoFunciona(String msg) {
                            adapterFrete.atualizaItem(posicaoTemporaria);
                            uiMutavel_atualizaComissao();
                            uiMutavel_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void quandoFalha(String msg) {
                            MensagemUtil.snackBar(requireView(), msg);
                        }

                        @Override
                        public void quandoCancela(String msg) {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);
        menu.removeItem(R.id.menu_padrao_search);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                Toast.makeText(this.requireContext(), "Logout", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                NavController controlador = Navigation.findNavController(requireView());
                controlador.popBackStack();
                break;
        }

        return false;
    }

    private void substituiValorNoMap(int id, BigDecimal novoValor) {
        if (map.containsKey(id)) {
            map.replace(id, novoValor);
        }
    }

    public enum TipoDeAdapterPressionado {
        ADIANTAMENTO, REEMBOLSO, FRETE
    }

}