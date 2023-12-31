package br.com.transporte.appGhn.ui.activity.formulario;

import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID_RECEBIMENTO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_TIPO_DESPESA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_ABASTECIMENTO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_ADIANTAMENTO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CAVALO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CERTIFICADOS;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_DEFAUT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_IMPOSTOS;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_MANUTENCAO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_MOTORISTA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_RECEBIMENTO_FRETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEMIREBOQUE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityFormulariosBinding;
import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.model.enums.TipoFormulario;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioAdiantamentoFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioCavaloFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioCustosDeManutencaoFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioCustosDePercursoFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioFreteFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioImpostosFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioMotoristaFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioRecebimentoFreteFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.FormularioSemiReboqueFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.abastecimento.FormularioAbastecimentoFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.certificado.FormularioCertificadosFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.despesaAdm.FormularioDespesaAdmFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.FormularioSeguroFrotaFragment;
import br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroVida.FormularioSeguroVidaFragment;

public class FormulariosActivity extends AppCompatActivity {
    public static final long DEFAULT_VALUE = 0;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityFormulariosBinding binding = ActivityFormulariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBarColor();
        Intent dados = getIntent();
        Bundle bundle = new Bundle();
        final TipoDespesa tipoDespesa;
        int chave_formulario = dados.getIntExtra(CHAVE_FORMULARIO, VALOR_DEFAUT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState == null) {
            switch (chave_formulario) {
                //----------------------------------------------------------------------------------------------
                //                                          FRETE                                             ||
                //----------------------------------------------------------------------------------------------
                case VALOR_FRETE:
                    fragment = new FormularioFreteFragment();
                    recebeCavaloId(dados, bundle);
                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          ABASTECIMENTO                                     ||
                //----------------------------------------------------------------------------------------------
                case VALOR_ABASTECIMENTO:
                    fragment = new FormularioAbastecimentoFragment();
                    recebeCavaloId(dados, bundle);
                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          CUSTOS DE PERCURSO                                ||
                //----------------------------------------------------------------------------------------------
                case VALOR_CUSTO_PERCURSO:
                    fragment = new FormularioCustosDePercursoFragment();
                    recebeCavaloId(dados, bundle);
                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          MOTORISTA                                         ||
                //----------------------------------------------------------------------------------------------
                case VALOR_MOTORISTA:
                    fragment = new FormularioMotoristaFragment();
                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          CAVALO                                            ||
                //----------------------------------------------------------------------------------------------
                case VALOR_CAVALO:
                    fragment = new FormularioCavaloFragment();
                    if (dados.hasExtra(CHAVE_ID)) {
                        long idCavalo = dados.getLongExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putLong(CHAVE_ID_CAVALO, idCavalo);
                    }
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          SEMIREBOQUE                                       ||
                //----------------------------------------------------------------------------------------------
                case VALOR_SEMIREBOQUE:
                    fragment = new FormularioSemiReboqueFragment();
                    recebeCavaloId(dados, bundle);
                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          CERTIFICADOS                                      ||
                //----------------------------------------------------------------------------------------------
                case VALOR_CERTIFICADOS:
                    fragment = new FormularioCertificadosFragment();

                    TipoFormulario tipoFormulario = (TipoFormulario) dados.getSerializableExtra(CHAVE_REQUISICAO);
                    tipoDespesa = (TipoDespesa) dados.getSerializableExtra(CHAVE_DESPESA);
                    bundle.putSerializable(CHAVE_REQUISICAO, tipoFormulario);
                    bundle.putSerializable(CHAVE_DESPESA, tipoDespesa);

                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          IMPOSTOS                                          ||
                //----------------------------------------------------------------------------------------------
                case VALOR_IMPOSTOS:
                    fragment = new FormularioImpostosFragment();
                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          MANUTENCAO_                                     ||
                //----------------------------------------------------------------------------------------------
                case VALOR_MANUTENCAO:
                    fragment = new FormularioCustosDeManutencaoFragment();
                    recebeCavaloId(dados, bundle);
                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          DESPESA ADM                                       ||
                //----------------------------------------------------------------------------------------------
                case VALOR_DESPESA_ADM:
                    fragment = new FormularioDespesaAdmFragment();
                    recebeChaveId_quandoLong(dados, bundle);
                    tipoDespesa = (TipoDespesa) dados.getSerializableExtra(CHAVE_TIPO_DESPESA);
                    bundle.putSerializable(CHAVE_TIPO_DESPESA, tipoDespesa);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          SEGURO AUTO                                       ||
                //----------------------------------------------------------------------------------------------
                case VALOR_SEGURO_FROTA:
                    fragment = new FormularioSeguroFrotaFragment();

                    tipoFormulario = (TipoFormulario) dados.getSerializableExtra(CHAVE_REQUISICAO);
                    bundle.putSerializable(CHAVE_REQUISICAO, tipoFormulario);

                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          SEGURO VIDA                                       ||
                //----------------------------------------------------------------------------------------------
                case VALOR_SEGURO_VIDA:
                    fragment = new FormularioSeguroVidaFragment();

                    tipoFormulario = (TipoFormulario) dados.getSerializableExtra(CHAVE_REQUISICAO);
                    bundle.putSerializable(CHAVE_REQUISICAO, tipoFormulario);

                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          RECEBIMENTO FRETE                                 ||
                //----------------------------------------------------------------------------------------------
                case VALOR_RECEBIMENTO_FRETE:
                    fragment = new FormularioRecebimentoFreteFragment();

                   recebeChaveId_quandoLong(dados, bundle);

                    if (dados.hasExtra(CHAVE_ID_RECEBIMENTO)) {
                        long objetoId = dados.getLongExtra(CHAVE_ID_RECEBIMENTO, VALOR_DEFAUT);
                        bundle.putLong(CHAVE_ID_RECEBIMENTO, objetoId);
                    }

                    inicializaFragment(bundle, fragmentTransaction);
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          ADIANTAMENTO                                      ||
                //----------------------------------------------------------------------------------------------
                case VALOR_ADIANTAMENTO:
                    fragment = new FormularioAdiantamentoFragment();
                    recebeCavaloId(dados, bundle);
                    recebeChaveId_quandoLong(dados, bundle);
                    inicializaFragment(bundle, fragmentTransaction);
                    break;
            }
        }
    }

    private void recebeCavaloId(@NonNull Intent dados, @NonNull Bundle bundle) {
        long cavaloId;
        cavaloId = dados.getLongExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
        bundle.putLong(CHAVE_ID_CAVALO, cavaloId);
    }

    private static void recebeChaveId_quandoLong(@NonNull Intent dados, Bundle bundle) {
        if (dados.hasExtra(CHAVE_ID)) {
            long objId = dados.getLongExtra(CHAVE_ID, DEFAULT_VALUE);
            bundle.putLong(CHAVE_ID, objId);
        }
    }

    private void inicializaFragment(Bundle bundle, @NonNull FragmentTransaction fragmentTransaction) {
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
    }
    
    private void setStatusBarColor() {
        int color = ContextCompat.getColor(this, R.color.midnightblue);
        getWindow().setStatusBarColor(color);
    }

}