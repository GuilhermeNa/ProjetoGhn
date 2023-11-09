package br.com.transporte.appGhn.ui.fragment.seguros.seguroHome;

import static android.view.View.GONE;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import br.com.transporte.AppGhn.databinding.FragmentSegurosHomeBinding;

public class SegurosHomeFragment extends Fragment {
    private FragmentSegurosHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSegurosHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ProgressBar progressBar = binding.progressBar;

        final Handler handler = new Handler();
        handler.postDelayed(
                () -> {
                    progressBar.setVisibility(GONE);

                    final NavController controlador =
                            Navigation
                                    .findNavController(requireView());

                    final NavDirections direction =
                            SegurosHomeFragmentDirections
                                    .actionNavSeguroHomeToNavSeguroFrota();

                    controlador.navigate(direction);
                }, 1000);
    }

}