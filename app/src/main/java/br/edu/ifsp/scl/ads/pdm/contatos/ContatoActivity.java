package br.edu.ifsp.scl.ads.pdm.contatos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatoBinding;

public class ContatoActivity extends AppCompatActivity {

    private ActivityContatoBinding activityContatoBinding;

    private final int PERMISSAO_ESCRITA_AMAZENAMENTO_EXTERNO_REQUEST_CODE = 0;
    Contato contato;
    private int posicao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        activityContatoBinding = activityContatoBinding.inflate(getLayoutInflater());
        setContentView(activityContatoBinding.getRoot());

        contato = (Contato) getIntent().getSerializableExtra(Intent.EXTRA_USER);

        if(contato != null){
            posicao = getIntent().getIntExtra(Intent.EXTRA_INDEX, -1);

            boolean ativo = posicao != -1? true: false;
            alterarAtivacaoViews(ativo);

            if(ativo){
                getSupportActionBar().setSubtitle("Edição de contato");
                activityContatoBinding.exportarPDFBt.setVisibility(View.GONE);
                activityContatoBinding.salvarBt.setText("Editar");
            }

            else{
                getSupportActionBar().setSubtitle("Detalhes do contato");
                activityContatoBinding.salvarBt.setVisibility(View.GONE);
            }




            activityContatoBinding.nomeCompletoEt.setText(contato.getNome());
            activityContatoBinding.emailEt.setText(contato.getEmail());
            activityContatoBinding.telefoneEt.setText(contato.getTelefone());
            activityContatoBinding.addTelefoneCb.setChecked(contato.isTelefoneComercial());
            activityContatoBinding.celularEt.setText(contato.getTelefoneCelular());
            activityContatoBinding.sitePessoalEt.setText(contato.getSite());
        }
        else{
            getSupportActionBar().setSubtitle("Novo contato");
            activityContatoBinding.exportarPDFBt.setVisibility(View.GONE);
        }

    }

    private void alterarAtivacaoViews(boolean ativo){
        activityContatoBinding.nomeCompletoEt.setEnabled(ativo);
        activityContatoBinding.emailEt.setEnabled(ativo);
        activityContatoBinding.telefoneEt.setEnabled(ativo);
        activityContatoBinding.addTelefoneCb.setChecked(ativo);
        activityContatoBinding.celularEt.setEnabled(ativo);
        activityContatoBinding.sitePessoalEt.setEnabled(ativo);
    }

    public void isCheckedAddCellPhone(View view) {
        if(activityContatoBinding.addTelefoneCb.isChecked())
            activityContatoBinding.celularEt.setVisibility(View.VISIBLE);
        else{
            activityContatoBinding.celularEt.setVisibility(View.GONE);
            activityContatoBinding.celularEt.setText("");
        }
    }

    public void onClickButton(View view) {
        contato = new Contato(
                activityContatoBinding.nomeCompletoEt.getText().toString(),
                activityContatoBinding.emailEt.getText().toString(),
                activityContatoBinding.telefoneEt.getText().toString(),
                activityContatoBinding.tipoTelefoneRg.isSelected(),
                activityContatoBinding.celularEt.getText().toString(),
                activityContatoBinding.sitePessoalEt.getText().toString()
        );
        switch (view.getId()){
            case R.id.exportarPDFBt:
                verificarPermissaoEscritaArmazenamentoExterno();
                break;
            case R.id.salvarBt:
                save(contato);
                break;
        }
    }

    private void verificarPermissaoEscritaArmazenamentoExterno(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSAO_ESCRITA_AMAZENAMENTO_EXTERNO_REQUEST_CODE);
            }
            else
                gerarDocumentoPDF();
        }
        else
            gerarDocumentoPDF();
    }

    private void gerarDocumentoPDF(){
        View conteudo = activityContatoBinding.getRoot();
        int altura = conteudo.getHeight();
        int largura = conteudo.getWidth();

        PdfDocument documentoPdf = new PdfDocument();

        PdfDocument.PageInfo configuracao = new PdfDocument.PageInfo.Builder(largura, altura, 1).create();
        PdfDocument.Page pagina = documentoPdf.startPage(configuracao);

        conteudo.draw(pagina.getCanvas());

        documentoPdf.finishPage(pagina);

        File diretorioDocumentos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        try{

            File documento = new File(diretorioDocumentos, contato.getNome().replace(" ", "_") + ".pdf");
            documento.createNewFile();
            documentoPdf.writeTo(new FileOutputStream(documento));
            documentoPdf.close();
        }catch(IOException ioe){
            ioe.printStackTrace();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSAO_ESCRITA_AMAZENAMENTO_EXTERNO_REQUEST_CODE)
            verificarPermissaoEscritaArmazenamentoExterno();
    }

    private void save(Contato contato){
        Intent retornoIntent = new Intent();
        retornoIntent.putExtra(Intent.EXTRA_USER, contato);
        retornoIntent.putExtra(Intent.EXTRA_INDEX, posicao);
        setResult(RESULT_OK, retornoIntent);
        finish();
    }

}