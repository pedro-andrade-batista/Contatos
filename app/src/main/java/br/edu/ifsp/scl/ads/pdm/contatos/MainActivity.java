package br.edu.ifsp.scl.ads.pdm.contatos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    private final int CALL_PHONE_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());


    }

    public void isCheckedAddCellPhone(View view) {
        if(activityMainBinding.addTelefoneCb.isChecked())
            activityMainBinding.celularEt.setVisibility(View.VISIBLE);
        else{
            activityMainBinding.celularEt.setVisibility(View.GONE);
            activityMainBinding.celularEt.setText("");
        }
    }

    public void onClickButton(View view) {
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
                break;
        }
    }


    private void callPhone(){
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + activityMainBinding.telefoneEt.getText().toString()));
        startActivity(ligarIntent);
    }

    private void openBrowser(){
        Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
        abrirNavegadorIntent.setData(Uri.parse("https://" + activityMainBinding.sitePessoalEt.getText().toString()));
        startActivity(abrirNavegadorIntent);
    }

    private void sendEmail(){
        Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO);
        enviarEmailIntent.setData(Uri.parse("mailto:"));
        enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL,activityMainBinding.emailEt.getText().toString());
        startActivity(enviarEmailIntent);
    }

}