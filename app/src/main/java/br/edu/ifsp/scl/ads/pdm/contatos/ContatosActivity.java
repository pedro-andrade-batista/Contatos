package br.edu.ifsp.scl.ads.pdm.contatos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatosBinding;

public class ContatosActivity extends AppCompatActivity {

    private ActivityContatosBinding activityContatosBinding;
    private ArrayList<Contato> contatosList;
    private ContatosAdapter contatosAdapter;
    private final int NOVO_CONTATO_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContatosBinding = ActivityContatosBinding.inflate(getLayoutInflater());
        setContentView(activityContatosBinding.getRoot());

        contatosList = new ArrayList<>();
        //popularContatosList();

        contatosAdapter = new ContatosAdapter(this, R.layout.view_contato, contatosList);

        activityContatosBinding.contatosLv.setAdapter(contatosAdapter);

        registerForContextMenu(activityContatosBinding.contatosLv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(activityContatosBinding.contatosLv);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contatos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.novoContatoMi) {
            Intent novoContatoIntent = new Intent(this, ContatoActivity.class);
            startActivityForResult(novoContatoIntent, NOVO_CONTATO_REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NOVO_CONTATO_REQUEST_CODE && resultCode == RESULT_OK) {
            Contato contato = (Contato) data.getSerializableExtra(Intent.EXTRA_USER); // modo sem criar constante
            if (contato != null) {
                contatosList.add(contato);
                contatosAdapter.notifyDataSetChanged(); // notifica o adapter a alteracao no conjunto de dados
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_contato, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Contato contato = contatosList.get(menuInfo.position);

        switch (item.getItemId()){
            case R.id.enviarEmailMi:
                Intent enviarEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: "));
                enviarEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {contato.getEmail()});
                enviarEmail.putExtra(Intent.EXTRA_SUBJECT, contato.getNome());
                enviarEmail.putExtra(Intent.EXTRA_TEXT, contato.toString());
                startActivity(enviarEmail);
                return true;
            case R.id.ligarMi:
                return true;
            case R.id.acessarSiteMi:
                return true;
            case R.id.detalhesContatoMi:
                return true;
            case R.id.editarContatoMi:
                return true;
            case R.id.removerContatoMi:
                return true;
            default:
                return false;
        }
    }
}