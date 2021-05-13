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
            case R.id.exportarPDFBt:
                break;
            case R.id.salvarBt:
                save(contato);
                break;
        }
    }




    private void save(Contato contato){
        Intent retornoIntent = new Intent();
        retornoIntent.putExtra(Intent.EXTRA_USER, contato);
        setResult(RESULT_OK, retornoIntent);
        finish();
    }

}