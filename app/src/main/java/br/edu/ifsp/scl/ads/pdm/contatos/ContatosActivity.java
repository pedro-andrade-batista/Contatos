package br.edu.ifsp.scl.ads.pdm.contatos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatosRvBinding;

public class ContatosActivity extends AppCompatActivity implements OnContatoClickListener {

    //private ActivityContatosBinding activityContatosBinding;
    private ActivityContatosRvBinding activityContatosRvBinding;

    private ArrayList<Contato> contatosList;

    //private ContatosAdapter contatosAdapter;
    private ContatosRvAdapter contatosRvAdapter;

    private final int NOVO_CONTATO_REQUEST_CODE = 0;
    private final int EDITAR_CONTATO_REQUEST_CODE = 1;
    private Contato contato;

    private LinearLayoutManager contatosLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activityContatosBinding = ActivityContatosBinding.inflate(getLayoutInflater());
//        setContentView(activityContatosBinding.getRoot());

        activityContatosRvBinding = ActivityContatosRvBinding.inflate(getLayoutInflater());
        setContentView(activityContatosRvBinding.getRoot());

        contatosList = new ArrayList<>();
        popularContatosList();

        contatosLinearLayoutManager = new LinearLayoutManager(this);

//        contatosAdapter = new ContatosAdapter(this, R.layout.view_contato, contatosList);
        contatosRvAdapter = new ContatosRvAdapter(contatosList, this, getMenuInflater());

//      activityContatosBinding.contatosLv.setAdapter(contatosAdapter);
        activityContatosRvBinding.contatosRv.setAdapter(contatosRvAdapter);
        activityContatosRvBinding.contatosRv.setLayoutManager(contatosLinearLayoutManager);


        //registerForContextMenu(activityContatosBinding.contatosLv);


//        activityContatosBinding.contatosLv.setOnItemClickListener(((parent, view, position, id) -> {
//            contato = contatosList.get(position);
//            Intent detalhesIntent = new Intent(this, ContatoActivity.class);
//            detalhesIntent.putExtra(Intent.EXTRA_USER, contato);
//            startActivity(detalhesIntent);
//
//        }));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterForContextMenu(activityContatosBinding.contatosLv);
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
//                contatosAdapter.notifyDataSetChanged(); // notifica o adapter a alteracao no conjunto de dados
                contatosRvAdapter.notifyDataSetChanged();
            }
        }
        else{
            if(requestCode == EDITAR_CONTATO_REQUEST_CODE && resultCode == RESULT_OK){
                Contato contato = (Contato) data.getSerializableExtra(Intent.EXTRA_USER); // modo sem criar constante
                int posicao = data.getIntExtra(Intent.EXTRA_INDEX, -1);
                if (contato != null && posicao != -1) {
                    contatosList.remove(posicao);
                    contatosList.add(posicao, contato);
//                    contatosAdapter.notifyDataSetChanged(); // notifica o adapter a alteracao no conjunto de dados
                    contatosRvAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_contato, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        contato = contatosList.get(menuInfo.position);

        contato = contatosList.get(contatosRvAdapter.getPosicao());

        int posicao = contatosRvAdapter.getPosicao();


        switch (item.getItemId()){
            case R.id.enviarEmailMi:
                sendEmail(contato);
                return true;
            case R.id.ligarMi:
                callPhone(contato);
                return true;
            case R.id.acessarSiteMi:
                openBrowser(contato);
                return true;
            case R.id.editarContatoMi:
                Intent editarContatoIntent = new Intent(this, ContatoActivity.class);
                editarContatoIntent.putExtra(Intent.EXTRA_USER, contato);
//                editarContatoIntent.putExtra(Intent.EXTRA_INDEX, menuInfo.position);
                editarContatoIntent.putExtra(Intent.EXTRA_INDEX, posicao);
                startActivityForResult(editarContatoIntent, EDITAR_CONTATO_REQUEST_CODE);
                return true;
            case R.id.removerContatoMi:
                removeContato(contato);
                return true;
            default:
                return false;
        }
    }

    private void popularContatosList() {
        for (int i = 0; i < 20; i++) {
            contatosList.add(
                    new Contato(
                            "Nome " + i,
                            "E-mail " + i,
                            "Telefone " + i,
                            ( i % 2 == 0 ) ? false : true,
                            "Celular " + i,
                            "www.site" + i + ".com.br"
                    )
            );
        }
    }

    private void callPhone(Contato contato){
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + contato.getTelefone()));
        startActivity(ligarIntent);
    }

    private void openBrowser(Contato contato){
        Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
        abrirNavegadorIntent.setData(Uri.parse("https://" + contato.getSite()));
        startActivity(abrirNavegadorIntent);
    }

    private void sendEmail(Contato contato){
        Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO);
        enviarEmailIntent.setData(Uri.parse("mailto:"));
        enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{ contato.getEmail() });
        enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, contato.getNome());
        enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, contato.toString());
        startActivity(enviarEmailIntent);
    }

    private void removeContato(Contato contato){
        contatosList.remove(contato);
//        contatosAdapter.notifyDataSetChanged();
        contatosRvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContatoClick(int posicao) {
        contato = contatosList.get(posicao);
        Intent detalhesIntent = new Intent(this, ContatoActivity.class);
        detalhesIntent.putExtra(Intent.EXTRA_USER, contato);
        startActivity(detalhesIntent);
    }
}