package br.com.transporte.appGhn.ui.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import br.com.transporte.AppGhn.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    public static final String PREENCHA_O_LOGIN = "Preencha o Login.";
    public static final String PREENCHA_A_SENHA = "Preencha a Senha.";
    public static final String A_SENHA_DEVE_TER_NO_MINIMO_6_CARACTERES = "A senha deve ter no minimo 6 caracteres.";
    public static final String SENHA = "123456";
    public static final String SENHA_E_OU_USUARIO_INCORRETOS = "Senha e/ou usuário incorretos";
    public static final String LOGIN = "Usuario";
    private FragmentLoginBinding binding;
    private TextInputEditText campoNome;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configuraCampoNome();
        configuraLogin();
    }

    private void configuraCampoNome() {
        campoNome = binding.loginEditaNome;
        campoNome.setText(LOGIN);
    }

    private void configuraLogin() {
        binding.loginBtLogin.setOnClickListener(view -> {
            final String nome =
                    Objects.requireNonNull(campoNome.getText()).toString();

            final String senha =
                    Objects.requireNonNull(binding.loginEditaSenha.getText()).toString();

            if (nome.isEmpty()) {
                snackBar(view, PREENCHA_O_LOGIN);
            } else if (senha.isEmpty()) {
                snackBar(view, PREENCHA_A_SENHA);
            } else if (senha.length() <= 5) {
                snackBar(view, A_SENHA_DEVE_TER_NO_MINIMO_6_CARACTERES);
            } else if (senha.equals(SENHA)) {
                navegaParaHome(view);
            } else {
                snackBar(view, SENHA_E_OU_USUARIO_INCORRETOS);
            }

        });
    }

    private void navegaParaHome(View view) {
        final NavController controler = Navigation.findNavController(view);
        final NavDirections direction = LoginFragmentDirections.loginActionHome();
        controler.navigate(direction);
    }

    public void snackBar(final View view, final String mensagem) {
        final Snackbar snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#FF0000"));
        snackbar.setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}