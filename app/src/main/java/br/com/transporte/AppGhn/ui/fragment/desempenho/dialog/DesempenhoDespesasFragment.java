package br.com.transporte.AppGhn.ui.fragment.desempenho.dialog;

import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_ADM;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_IMPOSTOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_CERTIFICADOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_SEGUROS_DIRETOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_SEGUROS_INDIRETOS;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.Extensions.setResult;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.transporte.AppGhn.databinding.FragmentDesempenhoDespesasBinding;

public class DesempenhoDespesasFragment extends Fragment {
    private FragmentDesempenhoDespesasBinding binding;
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
        binding = FragmentDesempenhoDespesasBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.administrativoLayout.setOnClickListener(v -> setResult(DESPESAS_ADM, bundle, parentFragmentManager));
        binding.certificadoLayout.setOnClickListener(v -> setResult(DESPESA_CERTIFICADOS, bundle, parentFragmentManager));
        binding.frotaLayout.setOnClickListener(v -> setResult(DESPESA_SEGUROS_DIRETOS, bundle, parentFragmentManager));
        binding.segurosLayout.setOnClickListener(v -> setResult(DESPESA_SEGUROS_INDIRETOS, bundle, parentFragmentManager));
        binding.impostosLayout.setOnClickListener(v -> setResult(DESPESAS_IMPOSTOS, bundle, parentFragmentManager));

    }
}