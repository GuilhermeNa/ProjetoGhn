package br.com.transporte.AppGhn.ui.fragment.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentHomeBinding;
import br.com.transporte.AppGhn.model.enums.TipoSelecionaCavalo;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private NavDirections direction;
    private boolean animacao = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(animacao){
            configuraMovimentacaoDeAbertura();
        }
        configuraNavegacao(view);

    }

    private void configuraNavegacao(View view) {
        NavController controler = Navigation.findNavController(view);

        binding.cardAreaDoMotorista.setOnClickListener(v -> {
            direction = HomeFragmentDirections.actionNavHomeToNavSelecionaCavalo(TipoSelecionaCavalo.AREA_MOTORISTA);
            controler.navigate(direction);
        });

        binding.cardCadastroMotoristas.setOnClickListener(v -> {
            direction = HomeFragmentDirections.homeActionMotoristas();
            controler.navigate(direction);
        });

        binding.cardFrota.setOnClickListener(v -> {
            direction = HomeFragmentDirections.homeActionFrota();
            controler.navigate(direction);

        });

        binding.cardGerenciamento.setOnClickListener(v -> {
            direction = HomeFragmentDirections.homeActionGerenciamento();
            controler.navigate(direction);
        });


    }

    private void configuraMovimentacaoDeAbertura() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager result = requireActivity().getWindowManager();
        int mWidth;
        int mHeight;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mWidth = result.getCurrentWindowMetrics().getBounds().width();
            mHeight = result.getCurrentWindowMetrics().getBounds().height();
        } else {
            result.getDefaultDisplay().getMetrics(metrics);
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }

        TextView categoriastxt = binding.categoriasTitulo;
        ImageView logoutImg = binding.fragMotoristasLogout;
        categoriastxt.setVisibility(View.INVISIBLE);
        logoutImg.setVisibility(View.INVISIBLE);
        Animation animationLogout = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_logout);

        ImageView bannerCategorias = binding.categoriasBanner;
        LinearLayout containerCategorias = binding.categoriasContainer;

        containerCategorias.setY((float) (mHeight * 0.56));
        bannerCategorias.setY((float) (mHeight * 0.25));

        bannerCategorias.setScaleX((float) (mHeight * 0.0006));
        bannerCategorias.setScaleY((float) (mHeight * 0.0006));

        Handler handler = new Handler();
        Runnable r = () -> {
            containerCategorias.animate().setDuration(500);
            containerCategorias.animate().yBy(-(float) (mHeight * 0.56));

            bannerCategorias.animate().setDuration(500);
            bannerCategorias.animate().yBy(-(float) (mHeight * 0.25));
            bannerCategorias.animate().scaleX((float) (mHeight * 0.00060));
            bannerCategorias.animate().scaleY((float) (mHeight * 0.00060));

            logoutImg.setVisibility(View.VISIBLE);
            categoriastxt.setVisibility(View.VISIBLE);
            logoutImg.setAnimation(animationLogout);
            categoriastxt.setAnimation(animationLogout);
        };
        handler.postDelayed(r, 1500);
        animacao = false;
    }
}