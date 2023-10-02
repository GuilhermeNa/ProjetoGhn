package br.com.transporte.AppGhn.ui.fragment.home;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentGerenciamentoBinding;
import br.com.transporte.AppGhn.model.enums.TipoSelecionaCavalo;
import br.com.transporte.AppGhn.ui.activity.CertificadosActivity;
import br.com.transporte.AppGhn.ui.activity.comissao.ComissoesActivity;
import br.com.transporte.AppGhn.ui.activity.DespesasAdmActivity;
import br.com.transporte.AppGhn.ui.activity.freteAReceberActivity.FreteAReceberActivity;
import br.com.transporte.AppGhn.ui.activity.SegurosActivity;

public class GerenciamentoFragment extends Fragment implements MenuProvider {
    public static final String GERENCIAMENTO = "Gerenciamento";
    private FragmentGerenciamentoBinding binding;
    private CardView cardFrete, cardComissoes, cardImposto, cardCertificado, cardSeguros, cardManutencao, cardDespesa, cardDesempenho;
    private NavDirections direction;
    private NavController controlador;
    private Intent intent;
    private CardView cardMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGerenciamentoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        controlador = Navigation.findNavController(view);
        configuraToolbar();

        cardFrete.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), FreteAReceberActivity.class);
            startActivity(intent);
        });

        cardComissoes.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), ComissoesActivity.class);
            startActivity(intent);
        });

        cardImposto.setOnClickListener(v -> {
            direction = GerenciamentoFragmentDirections.actionNavGerenciamentoToNavImpostosFragment();
            controlador.navigate(direction);
        });

        cardCertificado.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), CertificadosActivity.class);
            startActivity(intent);
        });

        cardSeguros.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), SegurosActivity.class);
            startActivity(intent);
        });

        cardManutencao.setOnClickListener(v -> {
            direction = GerenciamentoFragmentDirections.actionNavGerenciamentoToNavSelecionaCavaloFragment(TipoSelecionaCavalo.MANUTENCAO);
            controlador.navigate(direction);
        });

        cardDespesa.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), DespesasAdmActivity.class);
            startActivity(intent);
        });

        cardDesempenho.setOnClickListener(v -> {
            direction = GerenciamentoFragmentDirections.actionNavGerenciamentoToNavDesempenho();
            controlador.navigate(direction);
        });

        cardMedia.setOnClickListener(v ->{
            direction = GerenciamentoFragmentDirections.actionNavGerenciamentoToNavMedia();
            controlador.navigate(direction);
        });
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(GERENCIAMENTO);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

    }

    private void inicializaCampos() {
        cardMedia = binding.fragGerenciamentoMedia;
        cardFrete = binding.fragGerenciamentoFreteReceber;
        cardComissoes = binding.fragGerenciamentoSalarios;
        cardImposto = binding.fragGerenciamentoImpostos;
        cardCertificado = binding.fragGerenciamentoCertificados;
        cardSeguros = binding.fragGerenciamentoProvisionamento;
        cardManutencao = binding.fragGerenciamentoManutencao;
        cardDespesa = binding.fragGerenciamentoDespesa;
        cardDesempenho = binding.fragGerenciamentoDesempenho;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);
        menu.removeItem(R.id.menu_padrao_search);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_padrao_logout:
                Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                controlador.popBackStack();
                break;
        }
        return false;
    }
}