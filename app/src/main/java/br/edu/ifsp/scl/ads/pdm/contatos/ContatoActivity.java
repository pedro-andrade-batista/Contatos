package br.edu.ifsp.scl.ads.pdm.contatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatoBinding;

public class ContatoActivity extends AppCompatActivity {

    private ActivityContatoBinding activityContatoBinding;

    private final int CALL_PHONE_PERMISSION_REQUEST = 1;
    Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        activityContatoBinding = activityContatoBinding.inflate(getLayoutInflater());
        setContentView(activityContatoBinding.getRoot());


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
            case R.id.ligarBt:
                callPhone();
                break;
            case R.id.enviarEmailBt:
                sendEmail();
                break;
            case R.id.exportarPDFBt:
                break;
            case R.id.sitePessoalBt:
                openBrowser();
                break;
            case R.id.salvarBt:
                save(contato);
                break;
        }
    }


    private void callPhone(){
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + activityContatoBinding.telefoneEt.getText().toString()));
        startActivity(ligarIntent);
    }

    private void openBrowser(){
        Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
        abrirNavegadorIntent.setData(Uri.parse("https://" + activityContatoBinding.sitePessoalEt.getText().toString()));
        startActivity(abrirNavegadorIntent);
    }

    private void sendEmail(){
        Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO);
        enviarEmailIntent.setData(Uri.parse("mailto:"));
        enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{ activityContatoBinding.emailEt.getText().toString() });
        enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contato");
        enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, "Teste");
        startActivity(enviarEmailIntent);
    }

    private void save(Contato contato){
        Intent retornoIntent = new Intent();
        retornoIntent.putExtra(Intent.EXTRA_USER, contato);
        setResult(RESULT_OK, retornoIntent);
        finish();
    }

}