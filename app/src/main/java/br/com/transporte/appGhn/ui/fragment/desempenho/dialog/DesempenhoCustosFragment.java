package br.com.transporte.appGhn.ui.fragment.desempenho.dialog;

import static br.com.transporte.appGhn.model.enums.TipoDeRequisicao.COMISSAO;
import static br.com.transporte.appGhn.model.enums.TipoDeRequisicao.CUSTOS_ABASTECIMENTO;
import static br.com.transporte.appGhn.model.enums.TipoDeRequisicao.CUSTOS_MANUTENCAO;
import static br.com.transporte.appGhn.model.enums.TipoDeRequisicao.CUSTOS_PERCURSO;
import static br.com.transporte.appGhn.ui.fragment.desempenho.extensions.Extensions.setResult;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import br.com.transporte.AppGhn.databinding.FragmentDesempenhoCustosBinding;

public class DesempenhoCustosFragment extends Fragment {
    private FragmentDesempenhoCustosBinding binding;
    private Bundle bundle;
    private FragmentManager parentFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentFragmentManager = getParentFragmentManager();
        bundle = new Bundle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDesempenhoCustosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.percursoLayout.setOnClickListener(v -> setResult(CUSTOS_PERCURSO, bundle, parentFragmentManager));
        binding.salariosLayout.setOnClickListener(v -> setResult(COMISSAO, bundle, parentFragmentManager));
        binding.manutencaoLayout.setOnClickListener(v -> setResult(CUSTOS_MANUTENCAO, bundle, parentFragmentManager));
        binding.abastecimentoLayout.setOnClickListener(v -> setResult(CUSTOS_ABASTECIMENTO, bundle, parentFragmentManager));

    }
}