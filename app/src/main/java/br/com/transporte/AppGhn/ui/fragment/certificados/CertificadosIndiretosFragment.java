package br.com.transporte.AppGhn.ui.fragment.certificados;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.EDITANDO;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.RENOVANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_UPDATE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CERTIFICADOS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentCertificadosIndiretosBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.CertificadoIndiretoAdapter;
import br.com.transporte.AppGhn.dao.DespesasCertificadoDAO;
import br.com.transporte.AppGhn.util.DataUtil;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CertificadosIndiretosFragment extends Fragment {
    private FragmentCertificadosIndiretosBinding binding;
    private List<DespesaCertificado> listaDeCertificadosIndiretos_Todos, listaDeCertificadosIndiretos;
    private CertificadoIndiretoAdapter adapter;
    private DespesasCertificadoDAO certificadoDao;
    private LinearLayout buscaVazia;
    private int ano;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        atualizaAdapter("Registro editado");
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        atualizaAdapter("Registro apagado");
                        break;

                    case RESULT_UPDATE:
                        atualizaAdapter("Registro renovado");
                        break;
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<DespesaCertificado> getListaDeTodosOsIndiretos() {
        return certificadoDao.listaTodosOsCertificadosIndiretos();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private List<DespesaCertificado> getListaDeCertificadosIndiretos_Ativos() {
        return listaDeCertificadosIndiretos_Todos.stream()
                .filter(DespesaCertificado::isValido)
                .collect(Collectors.toList());
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void atualizaAdapter(String msg) {
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show();
        listaDeCertificadosIndiretos_Todos = getListaDeTodosOsIndiretos();
        listaDeCertificadosIndiretos = getListaDeCertificadosIndiretos_Ativos();
        atualizaUiRecycler();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        certificadoDao = new DespesasCertificadoDAO();
        listaDeCertificadosIndiretos_Todos = getListaDeTodosOsIndiretos();
        listaDeCertificadosIndiretos = getListaDeCertificadosIndiretos_Ativos();

    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCertificadosIndiretosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraToolbar();
        verificaSeListaEstaVazia(listaDeCertificadosIndiretos, buscaVazia);
    }

    private void inicializaCamposDaView() {
        buscaVazia = binding.buscaVazia;
    }

    private void configuraRecycler() {
        RecyclerView recyclerView = binding.recycler;

        adapter = new CertificadoIndiretoAdapter(this, listaDeCertificadosIndiretos);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.requireContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(certificado -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CERTIFICADOS);
            intent.putExtra(CHAVE_ID, ((DespesaCertificado) certificado).getId());
            intent.putExtra(CHAVE_REQUISICAO, EDITANDO);
            intent.putExtra(CHAVE_DESPESA, INDIRETA);
            activityResultLauncher.launch(intent);
        });
    }

    private void verificaSeListaEstaVazia(List<DespesaCertificado> lista, LinearLayout layout) {
        if (lista.isEmpty()) {
            layout.setVisibility(View.VISIBLE);
        } else if (!lista.isEmpty() && layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        DespesaCertificado certificado = listaDeCertificadosIndiretos.get(posicao);

        if (item.getItemId() == R.id.renovarCertificado && !certificado.isValido()) {
            Toast.makeText(this.requireContext(), "Certificado já renovado", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.renovarCertificado) {
            Intent intent = new Intent(this.getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CERTIFICADOS);
            intent.putExtra(CHAVE_ID, certificado.getId());
            intent.putExtra(CHAVE_REQUISICAO, RENOVANDO);
            intent.putExtra(CHAVE_DESPESA, INDIRETA);
            activityResultLauncher.launch(intent);
        }

        return super.onContextItemSelected(item);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Certificados");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_certificados, menu);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.menu_certificados_visibilidade:
                        showBottomDialog();
                        break;

                    case R.id.menu_certificados_logout:
                        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this.requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_layout_certificado);

        SwitchMaterial switchExibicao = dialog.findViewById(R.id.switch_btn);
        SwitchMaterial switchData = dialog.findViewById(R.id.switch_btn2);
        TextView data = dialog.findViewById(R.id.ano);
        ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);
        ImageView setaEsquerda = dialog.findViewById(R.id.esquerda);
        ImageView setaDireita = dialog.findViewById(R.id.direita);
        Button btnfiltrar = dialog.findViewById(R.id.btn_filtrar);
        ConstraintLayout layoutSelecionaAno = dialog.findViewById(R.id.dialog_layout_data);
        Animation animacima = AnimationUtils.loadAnimation(this.requireContext(), R.anim.slide_in);
        Animation animabaixo = AnimationUtils.loadAnimation(this.requireContext(), R.anim.slide_out);

        switchExibicao.setChecked(true);
        String dataEmString = data.getText().toString();
        ano = Integer.parseInt(dataEmString);
        int anoAtual = DataUtil.capturaDataDeHojeParaConfiguracaoinicial().getYear();

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        switchData.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutSelecionaAno.setVisibility(View.VISIBLE);
                layoutSelecionaAno.startAnimation(animacima);
            } else {
                layoutSelecionaAno.setVisibility(View.INVISIBLE);
                layoutSelecionaAno.startAnimation(animabaixo);
            }
        });

        setaEsquerda.setOnClickListener(v -> {
            ano = ano - 1;
            data.setText(String.valueOf(ano));
        });

        setaDireita.setOnClickListener(v -> {
            if (ano < anoAtual) {
                ano = ano + 1;
                data.setText(String.valueOf(ano));
            } else {
                Toast.makeText(this.requireContext(), "Data limite para busca", Toast.LENGTH_SHORT).show();
            }

        });

        btnfiltrar.setOnClickListener(v -> {
            if (switchExibicao.isChecked() && switchData.isChecked()) {
                listaDeCertificadosIndiretos = listaDeCertificadosIndiretos_Todos.stream()
                        .filter(c -> c.getDataDeEmissao().getYear() == ano)
                        .collect(Collectors.toList());
                atualizaUiRecycler();

            } else if (switchExibicao.isChecked() && !switchData.isChecked()) {
                listaDeCertificadosIndiretos = getListaDeTodosOsIndiretos();
                Collections.sort(listaDeCertificadosIndiretos, Comparator.comparing(DespesaCertificado::getDataDeEmissao));
                atualizaUiRecycler();

            } else {
                listaDeCertificadosIndiretos = getListaDeCertificadosIndiretos_Ativos();
                atualizaUiRecycler();

            }

            dialog.dismiss();

        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void atualizaUiRecycler() {
        adapter.atualiza(listaDeCertificadosIndiretos);
        verificaSeListaEstaVazia(listaDeCertificadosIndiretos, buscaVazia);
    }

}