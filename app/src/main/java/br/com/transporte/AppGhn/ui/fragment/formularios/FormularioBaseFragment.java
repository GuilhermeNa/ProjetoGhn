package br.com.transporte.AppGhn.ui.fragment.formularios;

import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.AppGhn.util.MensagemUtil.snackBar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioBaseViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.FormularioBaseViewModelFactory;
import br.com.transporte.AppGhn.util.ConverteDataUtil;

public abstract class FormularioBaseFragment extends Fragment implements FormulariosInterface, MenuProvider {
    protected static final String TITULO_APP_BAR_EDITANDO = "Editando";
    protected static final String TITULO_APP_BAR_CRIANDO = "Criando";
    protected static final String TITULO_APP_BAR_RENOVANDO = "Renovando";
    protected static final String APAGA_REGISTRO_TITULO = "Apagando Registro";
    protected static final String APAGA_REGISTRO_TXT = "Todas as informações abaixo serão perdidas. Realmente deseja apagar permanentemente este registro?";
    protected static final String CANCELAR_REGISTRO_TITULO = "Cancelando Registro";
    protected static final String CANCELA_REGISTRO_TXT = "Todas as informações abaixo serão perdidas. Realmente deseja cancelar este cadastro?";
    protected static final String SIM = "sim";
    protected static final String NAO = "Não";
    public static final String ADICIONANDO_REGISTRO_TITULO = "Adicionando Registro";
    public static final String ADICIONA_REGISTRO_TXT = "Confirma a criação de um novo registro?";
    public static final String EDITANDO_REGISTRO_TITULO = "Editando Registro";
    public static final String EDITANDO_REGISTRO_TXT = "As informações previas serão perdidas. Confirma a edição deste registro?";
    public static final String SELECIONE_UMA_DATA = "Selecione uma data";
    public static final String PREENCHER = "Preencher";
    public static final String INCOMPLETO = "Incompleto";
    public static final String PREENCHA_A_DATA_CORRETAMENTE = "Preencha a data corretamente";
    private boolean completoParaSalvar = true;
    private TipoFormulario tipoFormulario;
    protected Cavalo cavaloRecebido;
    protected ExecutorService executor;
    protected Handler handler;
    protected GhnApplication application = new GhnApplication();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected Long getReferenciaDeCavalo(String chave) {
        return getArguments().getLong(chave);
    }

    protected void recebeReferenciaExternaDeCavalo(String chave) {
        CavaloRepository repository = new CavaloRepository(requireContext());
        FormularioBaseViewModelFactory factory = new FormularioBaseViewModelFactory(repository);
        ViewModelProvider provedor = new ViewModelProvider(this, factory);
        FormularioBaseViewModel viewModel = provedor.get(FormularioBaseViewModel.class);

        final Bundle bundle = getArguments();
        final long cavaloId = bundle.getLong(chave);

        viewModel.localizaCavalo(cavaloId).observe(this,
                cavalo -> {
                    if(cavalo != null) {
                        cavaloRecebido = cavalo;
                    }
                });
    }

    protected long verificaSeRecebeDadosExternos(String chave) {
        Bundle bundle = getArguments();
        long id = 0;

        if (bundle != null && bundle.containsKey(chave)) {
            id = bundle.getLong(chave);
        }

        return id;
    }

    protected void defineTipoEditandoOuCriando(long id) {
        if (id > 0) {
            setTipoFormulario(TipoFormulario.EDITANDO);
        } else {
            setTipoFormulario(TipoFormulario.ADICIONANDO);
        }
    }

    protected void configuraUi(Toolbar toolbar) {
        configuraToolbar(toolbar);
        aplicaMascarasAosEditTexts();
    }

    protected void bind() {
        if (tipoFormulario == EDITANDO) {
            alteraUiParaModoEdicao();
            exibeObjetoEmCasoDeEdicao();
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(TITULO_APP_BAR_EDITANDO);
        } else {
            alteraUiParaModoCriacao();
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(TITULO_APP_BAR_CRIANDO);
        }
    }

    protected void configuraToolbar(Toolbar toolbar) {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    protected void desmarcaBox(@NonNull CheckBox box) {
        if (box.isChecked()) {
            box.setChecked(false);
        }
    }

    protected void verificaCampo(@NonNull EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(PREENCHER);
            if (completoParaSalvar) completoParaSalvar = false;
        }
    }

    protected void verificaCampoComData(@NonNull EditText editText, View view) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(PREENCHER);
            if (completoParaSalvar) completoParaSalvar = false;
        } else if (editText.getText().toString().length() < 8) {
            editText.setError(INCOMPLETO);
            snackBar(view, PREENCHA_A_DATA_CORRETAMENTE);
            if (completoParaSalvar) completoParaSalvar = false;
        }
    }

    protected void configuraDataCalendario(@NonNull TextInputLayout layout, EditText editTxt) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText(SELECIONE_UMA_DATA)
                .build();
        layout.setStartIconOnClickListener(v -> {
            datePicker.show(getParentFragmentManager(), "DataFrete");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                LocalDate data = Instant.ofEpochMilli(selection).atZone(ZoneId.of("America/Sao_Paulo"))
                        .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                        .toLocalDate();
                editTxt.setText(ConverteDataUtil.dataParaString(data));
            });
            datePicker.addOnDismissListener(dialog -> {
                datePicker.clearOnPositiveButtonClickListeners();
                datePicker.clearOnDismissListeners();
            });
        });
    }

    protected TipoFormulario getTipoFormulario() {
        return tipoFormulario;
    }

    protected void setTipoFormulario(TipoFormulario tipoFormulario) {
        this.tipoFormulario = tipoFormulario;
    }

    protected boolean isCompletoParaSalvar() {
        return completoParaSalvar;
    }

    protected void setCompletoParaSalvar(boolean completoParaSalvar) {
        this.completoParaSalvar = completoParaSalvar;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_formularios, menu);
        if (tipoFormulario == EDITANDO) {
            menu.removeItem(R.id.menu_formulario_salvar);
        } else {
            menu.removeItem(R.id.menu_formulario_apagar);
            menu.removeItem(R.id.menu_formulario_editar);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                requireActivity().finish();
                break;

            case R.id.menu_formulario_salvar:
                verificaSeCamposEstaoPreenchidos(this.getView());

                if (isCompletoParaSalvar()) {
                    new AlertDialog.Builder(this.getContext()).
                            setTitle(ADICIONANDO_REGISTRO_TITULO).
                            setMessage(ADICIONA_REGISTRO_TXT).
                            setPositiveButton(SIM, (dialog, which) -> {
                                vinculaDadosAoObjeto();
                                adicionaObjetoNoBancoDeDados();

                                //TODO colocar esses comportamentos nos formularios
                                //  requireActivity().setResult(RESULT_OK);
                                //  requireActivity().finish();

                            }).
                            setNegativeButton(NAO, null).
                            show();
                } else {
                    setCompletoParaSalvar(true);
                }
                break;

            case R.id.menu_formulario_editar:
                verificaSeCamposEstaoPreenchidos(this.getView());

                if (isCompletoParaSalvar()) {
                    new AlertDialog.Builder(this.getContext()).
                            setTitle(EDITANDO_REGISTRO_TITULO).
                            setMessage(EDITANDO_REGISTRO_TXT).
                            setPositiveButton(SIM, (dialog, which) -> {
                                vinculaDadosAoObjeto();
                                editaObjetoNoBancoDeDados();

                                //TODO colocar esses comportamentos nos formularios
                                //  requireActivity().setResult(RESULT_EDIT);
                                //  requireActivity().finish();
                            }).
                            setNegativeButton(NAO, null).
                            show();
                } else {
                    setCompletoParaSalvar(true);
                }
                break;

            case R.id.menu_formulario_apagar:
                new AlertDialog.Builder(this.getContext()).
                        setTitle(APAGA_REGISTRO_TITULO).
                        setMessage(APAGA_REGISTRO_TXT).
                        setPositiveButton(SIM, (dialog, which) -> {
                            deletaObjetoNoBancoDeDados();

                            //TODO colocar esses comportamentos nos formularios
                            // requireActivity().setResult(RESULT_DELETE);
                            // requireActivity().finish();
                        }).
                        setNegativeButton(NAO, null).show();
                break;
        }
        return false;
    }

}
