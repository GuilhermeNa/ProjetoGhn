package br.com.transporte.AppGhn.ui.fragment.freteReceber;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentCarregandoDadosFreteReceberBinding;
import br.com.transporte.AppGhn.util.AnimationUtil;

public class CarregandoDadosFreteReceberFragment extends Fragment {
    public static final String MSG_DADOS_CARREGADOS = "Tudo Pronto!";
    private FragmentCarregandoDadosFreteReceberBinding binding;
    private boolean activityNotificouQueDadosEstaoProntos = false;
    private boolean animacaoDeEntradaTerminou = false;
    private ImageView imagem;
    private TextView texto;

    private interface DadosCarregadosCallback {
        void finaliza();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCarregandoDadosFreteReceberBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        animacaoDeEntrada(() -> {
            animacaoDeEntradaTerminou = true;
            verificaSeActCarregouOsDados();
        });

    }

    private void inicializaCamposDaView() {
        imagem = binding.carregandoDados.carregaDadosImg;
        texto = binding.carregandoDados.carregaDadosTxt;
    }

    private void animacaoDeEntrada(final DadosCarregadosCallback callback) {
        final Handler handler = new Handler();
        final Runnable r = () -> {
            texto.setVisibility(VISIBLE);
            AnimationUtil.defineAnimacao(requireContext(), android.R.anim.fade_in, texto);
        };

        final Runnable r2 = () -> {
            imagem.setVisibility(VISIBLE);
            AnimationUtil.defineAnimacao(requireContext(), android.R.anim.slide_in_left, imagem);
            callback.finaliza();
        };

        handler.postDelayed(r, 500);
        handler.postDelayed(r2, 1000);
    }

    private void verificaSeActCarregouOsDados() {
        if (activityNotificouQueDadosEstaoProntos) {
            finaliza();
        }
    }

    public void finaliza() {
        if (animacaoDeEntradaTerminou) {
            animacaoDeSaida(() -> {
                NavController controlador = Navigation.findNavController(requireView());
                NavDirections direction = CarregandoDadosFreteReceberFragmentDirections.actionCarregandoDadosFragmentToNavFreteReceber();
                controlador.navigate(direction);
            });
        } else {
            activityNotificouQueDadosEstaoProntos = true;
        }
    }

    private void animacaoDeSaida(@NonNull final DadosCarregadosCallback callback) {
        final Handler handler = new Handler();

        final Runnable r = () -> {
            texto.setVisibility(INVISIBLE);
            AnimationUtil.defineAnimacao(requireContext(), R.anim.slide_out_bottom, texto);
        };

        final Runnable r1 = () -> {
            texto.setVisibility(VISIBLE);
            texto.setText(MSG_DADOS_CARREGADOS);
            AnimationUtil.defineAnimacao(requireContext(), R.anim.slide_in_bottom, texto);
        };

        final Runnable r2 = () -> {
            imagem.setVisibility(INVISIBLE);
            AnimationUtil.defineAnimacao(requireContext(), android.R.anim.slide_out_right, imagem);
        };

        final Runnable r3 = () -> {
            texto.setVisibility(INVISIBLE);
            AnimationUtil.defineAnimacao(requireContext(), R.anim.slide_out_bottom, texto);
        };

        final Runnable r4 = callback::finaliza;

        handler.postDelayed(r, 1000);
        handler.postDelayed(r1, 1500);
        handler.postDelayed(r2, 2500);
        handler.postDelayed(r3, 3000);
        handler.postDelayed(r4, 3500);
    }

}