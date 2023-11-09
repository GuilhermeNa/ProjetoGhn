package br.com.transporte.appGhn.ui.fragment.desempenho.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.FragmentDesempenhoRepository;

public class DesempenhoFragmentViewModelFactory implements ViewModelProvider.Factory {
    //private final ResourceRepositories repositoryResource;
    private final FragmentDesempenhoRepository repository;

    public DesempenhoFragmentViewModelFactory(
         //   ResourceRepositories repositoryResource,
            FragmentDesempenhoRepository repository
    ) {
        this.repository = repository;
        //this.repositoryResource = repositoryResource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DesempenhoFragmentViewModel(repository);
        //return (T) new DesempenhoFragmentViewModel(repositoryResource);
    }

}