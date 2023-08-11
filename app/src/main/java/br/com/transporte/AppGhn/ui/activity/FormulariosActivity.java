package br.com.transporte.AppGhn.ui.activity;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_RECEBIMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_ABASTECIMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_ADIANTAMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CERTIFICADOS;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DEFAUT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_IMPOSTOS;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_MANUTENCAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_MOTORISTA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_RECEBIMENTO_FRETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEMIREBOQUE;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityFormulariosBinding;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.ui.fragment.formularios.abastecimento.FormularioAbastecimentoFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioAdiantamentoFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioCavaloFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioCertificadosFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioCustosDeManutencaoFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioCustosDePercursoFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioDespesaAdmFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioFreteFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioImpostosFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioMotoristaFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioRecebimentoFreteFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioSeguroFrotaFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioSeguroVidaFragment;
import br.com.transporte.AppGhn.ui.fragment.formularios.FormularioSemiReboqueFragment;

public class FormulariosActivity extends AppCompatActivity {
    private ActivityFormulariosBinding binding;
    private Fragment fragment;
    private TipoFormulario tipoFormulario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormulariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBarColor();

        Intent dados = getIntent();
        Bundle bundle = new Bundle();
        int chave_formulario = dados.getIntExtra(CHAVE_FORMULARIO, VALOR_DEFAUT);
        int cavaloId;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState == null) {
            switch (chave_formulario) {

                //----------------------------------------------------------------------------------------------
                //                                          FRETE                                             ||
                //----------------------------------------------------------------------------------------------
                case VALOR_FRETE:
                    fragment = new FormularioFreteFragment();

                    cavaloId = dados.getIntExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
                    bundle.putInt(CHAVE_ID_CAVALO, cavaloId);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int freteId = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID, freteId);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          ABASTECIMENTO                                     ||
                //----------------------------------------------------------------------------------------------

                case VALOR_ABASTECIMENTO:
                    fragment = new FormularioAbastecimentoFragment();

                    cavaloId = dados.getIntExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
                    bundle.putInt(CHAVE_ID_CAVALO, cavaloId);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int idAbastecimento = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID, idAbastecimento);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          CUSTOS DE PERCURSO                                ||
                //----------------------------------------------------------------------------------------------

                case VALOR_CUSTO_PERCURSO:
                    fragment = new FormularioCustosDePercursoFragment();

                    cavaloId = dados.getIntExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
                    bundle.putInt(CHAVE_ID_CAVALO, cavaloId);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int despesaId = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID, despesaId);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          MOTORISTA                                         ||
                //----------------------------------------------------------------------------------------------

                case VALOR_MOTORISTA:
                    fragment = new FormularioMotoristaFragment();
                    if (dados.hasExtra(CHAVE_ID)) {
                        int idMotorista = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle = new Bundle();
                        bundle.putInt(CHAVE_ID, idMotorista);
                        fragment.setArguments(bundle);
                    }
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          CAVALO                                            ||
                //----------------------------------------------------------------------------------------------

                case VALOR_CAVALO:
                    fragment = new FormularioCavaloFragment();
                    if (dados.hasExtra(CHAVE_ID)) {
                        int idCavalo = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle = new Bundle();
                        bundle.putInt(CHAVE_ID_CAVALO, idCavalo);
                        fragment.setArguments(bundle);
                    }
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;


                //----------------------------------------------------------------------------------------------
                //                                          SEMIREBOQUE                                       ||
                //----------------------------------------------------------------------------------------------

                case VALOR_SEMIREBOQUE:
                    fragment = new FormularioSemiReboqueFragment();

                    bundle = new Bundle();
                    cavaloId = dados.getIntExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
                    bundle.putInt(CHAVE_ID_CAVALO, cavaloId);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int idSr = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID, idSr);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          CERTIFICADOS                                      ||
                //----------------------------------------------------------------------------------------------

                case VALOR_CERTIFICADOS:
                    fragment = new FormularioCertificadosFragment();
                    bundle = new Bundle();

                    tipoFormulario = (TipoFormulario) dados.getSerializableExtra(CHAVE_REQUISICAO);
                    TipoDespesa tipoDespesa = (TipoDespesa) dados.getSerializableExtra(CHAVE_DESPESA);

                    bundle.putSerializable(CHAVE_REQUISICAO, tipoFormulario);
                    bundle.putSerializable(CHAVE_DESPESA, tipoDespesa);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int idCertificado = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID, idCertificado);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          IMPOSTOS                                          ||
                //----------------------------------------------------------------------------------------------

                case VALOR_IMPOSTOS:
                    fragment = new FormularioImpostosFragment();

                    if (dados.hasExtra(CHAVE_ID)) {
                        int idImposto = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle = new Bundle();
                        bundle.putInt(CHAVE_ID, idImposto);
                        fragment.setArguments(bundle);
                    }

                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          MANUTENCAO                                        ||
                //----------------------------------------------------------------------------------------------

                case VALOR_MANUTENCAO:
                    fragment = new FormularioCustosDeManutencaoFragment();

                    cavaloId = dados.getIntExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
                    bundle = new Bundle();
                    bundle.putInt(CHAVE_ID_CAVALO, cavaloId);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int idManutencao = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle = new Bundle();
                        bundle.putInt(CHAVE_ID, idManutencao);
                    }
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          DESPESA ADM                                       ||
                //----------------------------------------------------------------------------------------------

                case VALOR_DESPESA_ADM:
                    fragment = new FormularioDespesaAdmFragment();

                    if (dados.hasExtra(CHAVE_ID)) {
                        int despesaId = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle = new Bundle();
                        bundle.putInt(CHAVE_ID, despesaId);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          SEGURO AUTO                                       ||
                //----------------------------------------------------------------------------------------------

                case VALOR_SEGURO_FROTA:
                    fragment = new FormularioSeguroFrotaFragment();
                    bundle = new Bundle();

                    tipoFormulario = (TipoFormulario) dados.getSerializableExtra(CHAVE_REQUISICAO);
                    bundle.putSerializable(CHAVE_REQUISICAO, tipoFormulario);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int seguroId = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID, seguroId);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          SEGURO VIDA                                       ||
                //----------------------------------------------------------------------------------------------

                case VALOR_SEGURO_VIDA:
                    fragment = new FormularioSeguroVidaFragment();
                    bundle = new Bundle();

                    tipoFormulario = (TipoFormulario) dados.getSerializableExtra(CHAVE_REQUISICAO);
                    bundle.putSerializable(CHAVE_REQUISICAO, tipoFormulario);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int seguroId = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID, seguroId);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          RECEBIMENTO FRETE                                 ||
                //----------------------------------------------------------------------------------------------

                case VALOR_RECEBIMENTO_FRETE:
                    fragment = new FormularioRecebimentoFreteFragment();

                    int freteId = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                    bundle = new Bundle();
                    bundle.putInt(CHAVE_ID, freteId);

                    if (dados.hasExtra(CHAVE_ID_RECEBIMENTO)) {
                        int recebimentoId = dados.getIntExtra(CHAVE_ID_RECEBIMENTO, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID_RECEBIMENTO, recebimentoId);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

                //----------------------------------------------------------------------------------------------
                //                                          ADIANTAMENTO                                      ||
                //----------------------------------------------------------------------------------------------

                case VALOR_ADIANTAMENTO:
                    fragment = new FormularioAdiantamentoFragment();

                    cavaloId = dados.getIntExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
                    bundle = new Bundle();
                    bundle.putInt(CHAVE_ID_CAVALO, cavaloId);

                    if (dados.hasExtra(CHAVE_ID)) {
                        int adiantamentoId = dados.getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        bundle.putInt(CHAVE_ID, adiantamentoId);
                    }

                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.activity_formularios_frame, fragment).commit();
                    break;

            }
        }

    }

    private void setStatusBarColor() {
        int color = ContextCompat.getColor(this, R.color.midnightblue);
        getWindow().setStatusBarColor(color);
    }

}