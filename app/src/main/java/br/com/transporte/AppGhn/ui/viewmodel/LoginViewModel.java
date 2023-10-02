package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import br.com.transporte.AppGhn.repository.LoginRepository;

public class LoginViewModel extends ViewModel {
    private final LoginRepository repository;

    public LoginViewModel(LoginRepository repository) {
        this.repository = repository;
    }

//--------------------------------------------------------------------------------------------------

    public void login() {
        repository.login();
    }

    public boolean estaLogado() {
        return repository.estaLogado();
    }


}
