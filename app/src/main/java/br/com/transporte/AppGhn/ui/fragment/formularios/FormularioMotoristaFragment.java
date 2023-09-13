package br.com.transporte.AppGhn.ui.fragment.formularios;

import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.util.MascaraMonetariaUtil.formatPriceSave;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.databinding.FragmentFormularioMotoristaBinding;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;
import br.com.transporte.AppGhn.dao.MotoristaDAO;
import br.com.transporte.AppGhn.ui.fragment.extensions.BitmapImagem;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.MascaraDataUtil;
import br.com.transporte.AppGhn.util.MascaraMonetariaUtil;

public class FormularioMotoristaFragment extends FormularioBaseFragment {
    public static final String GALERIA = "Galeria";
    public static final String CAMERA = "Câmera";
    public static final String SELECIONE_A_ORIGEM_DA_FOTO = "Selecione a origem da foto";
    public static final String ORIGEM_DA_FOTO = "Origem da foto";
    public static final String DATA = "data";
    private FragmentFormularioMotoristaBinding binding;
    private static final String SUB_TITULO_APP_BAR_EDITANDO = "Você está editando um registro de motorista que já existe.";
    private EditText dataEdit, nomeEdit, cpfEdit, cnhEdit, cnhValidadeEdit, dataContratacaoEdit, salarioBaseEdit;
    private TextInputLayout dataLayout, cnhValidadeLayout, contratacaoLayout;
    private ImageView motoristaImg;
    private Motorista motorista;
    private RoomMotoristaDao motoristaDao;
    private Bitmap imgRecebidaEmBitmap;
    private String imgEmString;
    private boolean recebeuImagem;
    private final ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                Intent dataResultado = result.getData();

                if (codigoResultado == RESULT_OK) {
                    try {
                        imgRecebidaEmBitmap = (Bitmap) dataResultado.getExtras().get(DATA);
                        motoristaImg.setImageBitmap(imgRecebidaEmBitmap);
                        motoristaImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        imgEmString = BitmapImagem.codificaBitmapEmString(imgRecebidaEmBitmap);
                        recebeuImagem = true;
                    } catch (Exception e) {

                    }
                } else {
                    if (!recebeuImagem) {
                        motoristaImg.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                        motoristaImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imgEmString = null;
                    }
                }
            });
    private final ActivityResultLauncher<Intent> activityResultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                Intent dataResultado = result.getData();

                if (codigoResultado == RESULT_OK) {
                    try {
                        Uri imageUri = dataResultado.getData();
                        imgRecebidaEmBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        motoristaImg.setImageBitmap(imgRecebidaEmBitmap);
                        motoristaImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        imgEmString = BitmapImagem.codificaBitmapEmString(imgRecebidaEmBitmap);
                        recebeuImagem = true;
                    } catch (Exception e) {

                    }
                } else {
                    if (!recebeuImagem) {
                        motoristaImg.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                        motoristaImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imgEmString = null;
                    }
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        motoristaDao = GhnDataBase.getInstance(this.requireContext()).getRoomMotoristaDao();
        long motoristaId = verificaSeRecebeDadosExternos(CHAVE_ID);
        defineTipoEditandoOuCriando(motoristaId);
        motorista = (Motorista) criaOuRecuperaObjeto(motoristaId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFormularioMotoristaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        Toolbar toolbar = binding.toolbar;
        configuraUi(toolbar);
        configuraCLickSelecaoDeImagemDoFuncionario();
    }

    private void configuraCLickSelecaoDeImagemDoFuncionario() {
        motoristaImg.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                .setTitle(ORIGEM_DA_FOTO)
                .setMessage(SELECIONE_A_ORIGEM_DA_FOTO)
                .setPositiveButton(CAMERA, (dialog, which) -> {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncherCamera.launch(intent);
                })
                .setNegativeButton(GALERIA, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    activityResultLauncherGaleria.launch(intent);
                }).show());
    }

    @Override
    public void inicializaCamposDaView() {
        dataEdit = binding.fragFormularioMotoristaData;
        nomeEdit = binding.fragFormularioMotoristaNome;
        cpfEdit = binding.fragFormularioMotoristaCpf;
        cnhEdit = binding.fragFormularioMotoristaCnh;
        cnhValidadeEdit = binding.editValidade;
        dataContratacaoEdit = binding.fragFormularioMotoristaDataContratacao;
        salarioBaseEdit = binding.fragFormularioSalarioBase;

        dataLayout = binding.fragFormularioMotoristaLayoutData;
        cnhValidadeLayout = binding.layoutValidade;
        contratacaoLayout = binding.fragFormularioMotoristaLayoutDataContratacao;

        motoristaImg = binding.fragFormularioMotoristaFoto;

    }

    @Override
    public Object criaOuRecuperaObjeto(Object id) {
        Long motoristaId = (Long)id;
        if (getTipoFormulario() == TipoFormulario.EDITANDO) {
            motorista = motoristaDao.localizaPeloId(motoristaId);
            if(motorista.getImg() != null) recebeuImagem = true;

        } else {
            motorista = new Motorista();
            recebeuImagem = false;
        }
        return motorista;
    }

    @Override
    public void alteraUiParaModoEdicao() {
        TextView subTxt = binding.fragCadastraMotoristaCadastraFormularioSub;
        subTxt.setText(SUB_TITULO_APP_BAR_EDITANDO);
    }

    @Override
    public void alteraUiParaModoCriacao() {

    }

    @Override
    public void exibeObjetoEmCasoDeEdicao() {
        dataEdit.setText(ConverteDataUtil.dataParaString(motorista.getDataNascimento()));
        nomeEdit.setText(motorista.getNome());
        cpfEdit.setText(motorista.getCpf());
        cnhEdit.setText(motorista.getCnh());
        cnhValidadeEdit.setText(ConverteDataUtil.dataParaString(motorista.getCnhValidade()));
        dataContratacaoEdit.setText(ConverteDataUtil.dataParaString(motorista.getDataContratacao()));
        salarioBaseEdit.setText(motorista.getSalarioBase().toPlainString());

        try {
            imgRecebidaEmBitmap = BitmapImagem.decodificaBitmapEmString(motorista);
            motoristaImg.setImageBitmap(imgRecebidaEmBitmap);
            motoristaImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void aplicaMascarasAosEditTexts() {
        MascaraDataUtil.MascaraData(dataEdit);
        MascaraDataUtil.MascaraData(cnhValidadeEdit);
        MascaraDataUtil.MascaraData(dataContratacaoEdit);
        salarioBaseEdit.addTextChangedListener(new MascaraMonetariaUtil(salarioBaseEdit));
        MascaraDataUtil.MascaraSimples(cpfEdit, "NNN.NNN.NNN-NN");

        configuraDataCalendario(dataLayout, dataEdit);
        configuraDataCalendario(cnhValidadeLayout, cnhValidadeEdit);
        configuraDataCalendario(contratacaoLayout, dataContratacaoEdit);

    }

    @Override
    public void verificaSeCamposEstaoPreenchidos(View view) {
        verificaCampoComData(dataEdit, view);
        verificaCampoComData(cnhValidadeEdit, view);
        verificaCampoComData(dataContratacaoEdit, view);
        verificaCampo(nomeEdit);
        verificaCampo(cpfEdit);
        verificaCampo(cnhEdit);
        verificaCampo(salarioBaseEdit);
    }

    @Override
    public void vinculaDadosAoObjeto() {
        motorista.setDataNascimento(ConverteDataUtil.stringParaData(dataEdit.getText().toString()));
        motorista.setNome(nomeEdit.getText().toString());
        motorista.setCpf(cpfEdit.getText().toString());
        motorista.setCnh(cnhEdit.getText().toString());
        motorista.setCnhValidade(ConverteDataUtil.stringParaData(cnhValidadeEdit.getText().toString()));
        motorista.setImg(imgEmString);
        motorista.setDataContratacao(ConverteDataUtil.stringParaData(dataContratacaoEdit.getText().toString()));
        motorista.setSalarioBase(new BigDecimal(formatPriceSave(salarioBaseEdit.getText().toString())));
    }

    @Override
    public void editaObjetoNoBancoDeDados() {
        if(recebeuImagem){
            imgEmString = BitmapImagem.codificaBitmapEmString(imgRecebidaEmBitmap);
            motorista.setImg(imgEmString);
        }
        motoristaDao.substitui(motorista);
    }

    @Override
    public void adicionaObjetoNoBancoDeDados() {
        motoristaDao.adiciona(motorista);
    }

    @Override
    public Long configuraObjetoNaCriacao() {
        return null;
    }

    @Override
    public void deletaObjetoNoBancoDeDados() {
        motoristaDao.deleta(motorista);
    }
}
