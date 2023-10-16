package br.com.transporte.AppGhn.ui.fragment.home;

import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

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

import br.com.transporte.AppGhn.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    public static final String PREENCHA_O_LOGIN = "Preencha o Login.";
    public static final String PREENCHA_A_SENHA = "Preencha a Senha.";
    public static final String A_SENHA_DEVE_TER_NO_MINIMO_6_CARACTERES = "A senha deve ter no minimo 6 caracteres.";
    private FragmentLoginBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configuraLogin();
        navegaParaHome(requireView());
    }

    private void configuraLogin() {
        binding.loginBtLogin.setOnClickListener(view -> {
            String nome = binding.loginEditaNome.getText().toString();
            String senha = binding.loginEditaSenha.getText().toString();
            if (nome.isEmpty()) {
                snackBar(view, PREENCHA_O_LOGIN);
            } else if (senha.isEmpty()) {
                snackBar(view, PREENCHA_A_SENHA);
            } else if (senha.length() <= 5) {
                snackBar(view, A_SENHA_DEVE_TER_NO_MINIMO_6_CARACTERES);
            } else {
                navegaParaHome(view);
            }
        });
    }

    private void navegaParaHome(View view) {
        NavController controler = Navigation.findNavController(view);
        NavDirections direction = LoginFragmentDirections.loginActionHome();
        controler.navigate(direction);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}