package br.com.transporte.AppGhn.ui.fragment.desempenho.dialog;

import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.FRETE_BRUTO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.FRETE_LIQUIDO;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.LUCRO_LIQUIDO;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.Extensions.setResult;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import br.com.transporte.AppGhn.databinding.FragmentDesempenhoReceitaBinding;

public class DesempenhoReceitaFragment extends Fragment {
    private FragmentDesempenhoReceitaBinding binding;
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
        binding = FragmentDesempenhoReceitaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.freteBrutoLayout.setOnClickListener(v -> setResult(FRETE_BRUTO, bundle, parentFragmentManager));
        binding.freteLiquidoLayout.setOnClickListener(v -> setResult(FRETE_LIQUIDO, bundle, parentFragmentManager));
        binding.lucroLayout.setOnClickListener(v -> setResult(LUCRO_LIQUIDO, bundle, parentFragmentManager));

        binding.liquidoImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
        binding.lucroImg.setColorFilter(Color.parseColor("#FFFFFFFF"));

    }
}