package br.com.transporte.AppGhn.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.databinding.ActivityMainBinding;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.ui.activity.utilActivity.StatusBarUtil;
import br.com.transporte.AppGhn.ui.viewmodel.MainActViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.MainActViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.setStatusBarColor(this, getWindow());
        inicializaViewModel();

      /*  GhnApplication application = new GhnApplication();
        RxDataStore<Preferences> dataStore = application.getDataStore();

        Preferences.Key<Boolean> teste = PreferencesKeys.booleanKey("bolean");
        Flowable<Boolean> exemploTeste = dataStore.data().map(preferences -> preferences.get(teste));

        Single<Preferences> updateResult = dataStore.updateDataAsync(
                preferences -> {
                    MutablePreferences mutablePreferences = preferences.toMutablePreferences();
                    Boolean currentBoolean = preferences.get(teste);
                    mutablePreferences.set(teste, currentBoolean != null ? currentBoolean????? );
                    return Single.just(mutablePreferences);
                });


        LoginRepository repository = new LoginRepository(this);*/


    }

    private void inicializaViewModel() {
        CavaloRepository cavaloRepository = new CavaloRepository(this);
        MotoristaRepository motoristaRepository = new MotoristaRepository(this);
        MainActViewModelFactory factory = new MainActViewModelFactory(cavaloRepository, motoristaRepository);
        ViewModelProvider provedor = new ViewModelProvider(this, factory);
        MainActViewModel viewModel =  provedor.get(MainActViewModel.class);

    }

}