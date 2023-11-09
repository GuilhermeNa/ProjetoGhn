package br.com.transporte.appGhn.ui.fragment.home;

import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.RESULT_LOGOUT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentGerenciamentoBinding;
import br.com.transporte.appGhn.model.enums.TipoSelecionaCavalo;
import br.com.transporte.appGhn.ui.activity.certificado.CertificadosActivity;
import br.com.transporte.appGhn.ui.activity.comissao.ComissoesActivity;
import br.com.transporte.appGhn.ui.activity.despesaAdm.DespesasAdmActivity;
import br.com.transporte.appGhn.ui.activity.freteAReceberActivity.FreteAReceberActivity;
import br.com.transporte.appGhn.ui.activity.seguro.SegurosActivity;

public class GerenciamentoFragment extends Fragment implements MenuProvider {
    public static final String GERENCIAMENTO = "Gerenciamento";
    private FragmentGerenciamentoBinding binding;
    private CardView cardFrete, cardComissoes, cardImposto, cardCertificado, cardSeguros, cardManutencao, cardDespesa, cardDesempenho;
    private NavDirections direction;
    private NavController controlador;
    private Intent intent;
    private CardView cardMedia;
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if(result.getResultCode() == RESULT_LOGOUT){
                            final NavDirections directions = GerenciamentoFragmentDirections.actionGlobalNavLogin();
                            controlador.navigate(directions);
                        }
                    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGerenciamentoBinding.inflate(getLayoutInflater());
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
            activityResultLauncher.launch(intent);
        });

        cardComissoes.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), ComissoesActivity.class);
            activityResultLauncher.launch(intent);
        });

        cardImposto.setOnClickListener(v -> {
            direction = GerenciamentoFragmentDirections.actionNavGerenciamentoToNavImpostosFragment();
            controlador.navigate(direction);
        });

        cardCertificado.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), CertificadosActivity.class);
            activityResultLauncher.launch(intent);
        });

        cardSeguros.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), SegurosActivity.class);
            activityResultLauncher.launch(intent);
        });

        cardManutencao.setOnClickListener(v -> {
            direction = GerenciamentoFragmentDirections.actionNavGerenciamentoToNavSelecionaCavaloFragment(TipoSelecionaCavalo.MANUTENCAO);
            controlador.navigate(direction);
        });

        cardDespesa.setOnClickListener(v -> {
            intent = new Intent(this.requireActivity(), DespesasAdmActivity.class);
            activityResultLauncher.launch(intent);
        });

        cardDesempenho.setOnClickListener(v -> {
            direction = GerenciamentoFragmentDirections.actionNavGerenciamentoToNavDesempenho();
            controlador.navigate(direction);
        });

        cardMedia.setOnClickListener(v -> {
            direction = GerenciamentoFragmentDirections.actionNavGerenciamentoToNavMedia();
            controlador.navigate(direction);
        });
    }

    private void configuraToolbar() {
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
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                final NavDirections direction =
                        GerenciamentoFragmentDirections.actionGlobalNavLogin();

                controlador.navigate(direction);
                break;

            case android.R.id.home:
                controlador.popBackStack();
                break;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}