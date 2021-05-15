package br.edu.ifsp.scl.ads.pdm.contatos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.transition.Hold;

import java.util.ArrayList;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ViewContatoBinding;

public class ContatosAdapter extends ArrayAdapter<Contato> {

    public ContatosAdapter(Context contexto, int layout, ArrayList<Contato> contatosList){
        super(contexto, layout, contatosList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        ViewContatoBinding viewContatoBinding;
        ContatoViewHolder contatoViewHolder;

        // verificar se é necessario criar uma nova célula
        if(convertView == null){
            // infla a nova célula
            viewContatoBinding = ViewContatoBinding.inflate(LayoutInflater.from(getContext()));

            // Atribui a nova célula a View que será devolvida preenchida para a ListView
            convertView = viewContatoBinding.getRoot();

            contatoViewHolder = new ContatoViewHolder();
            contatoViewHolder.nomeContatoTv = viewContatoBinding.nomeContatoTv;
            contatoViewHolder.emailContatotv = viewContatoBinding.emailContatoTv;
            contatoViewHolder.telefoneContatoTv = viewContatoBinding.telefoneContatoTv;

            convertView.setTag(contatoViewHolder);
        }
        contatoViewHolder = (ContatoViewHolder) convertView.getTag();

        // Atualizar os valores dos textViews

        Contato contato = getItem(position);
        contatoViewHolder.nomeContatoTv.setText(contato.getNome());
        contatoViewHolder.emailContatotv.setText(contato.getEmail());
        contatoViewHolder.telefoneContatoTv.setText(contato.getTelefone());

        return convertView;
    }

    private class ContatoViewHolder {
        public TextView nomeContatoTv;
        public TextView emailContatotv;
        public TextView telefoneContatoTv;
    }


}
