package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contato;
import com.example.fivecontacts.main.model.MyItemClickListener;
import com.example.fivecontacts.main.model.RVAdapter;
import com.example.fivecontacts.main.model.User;
import com.example.fivecontacts.main.utils.UIEducacionalPermissao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class ListaDeContatos_Activity extends AppCompatActivity implements UIEducacionalPermissao.NoticeDialogListener, BottomNavigationView.OnNavigationItemSelectedListener, MyItemClickListener {

    RecyclerView rv;
    BottomNavigationView bnv;
    User user;

    String numeroCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contatos);

        bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);
        bnv.setSelectedItemId(R.id.anvLigar);

        rv = findViewById(R.id.recyclerList);

        //setando um LinearLayoutManager para a RecyclerView
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        //Dados da Intent Anterior
        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                //Recuperando o Usuario
                user = (User) params.getSerializable("usuario");
                if (user != null) {
                    setTitle("Contatos de Emergência de "+user.getNome());
                  //  preencherListView(user); //Montagem do ListView
                    preencherLista(user);
                }
            }
        }

    }

    protected void atualizarListaDeContatos(User user){
        SharedPreferences recuperarContatos = getSharedPreferences("contatos", Activity.MODE_PRIVATE);

        int num = recuperarContatos.getInt("numContatos", 0);
        ArrayList<Contato> contatos = new ArrayList<Contato>();

        Contato contato;


        for (int i = 1; i <= num; i++) {
            String objSel = recuperarContatos.getString("contato" + i, "");
            if (objSel.compareTo("") != 0) {
                try {
                    ByteArrayInputStream bis =
                            new ByteArrayInputStream(objSel.getBytes(StandardCharsets.ISO_8859_1.name()));
                    ObjectInputStream oos = new ObjectInputStream(bis);
                    contato = (Contato) oos.readObject();

                    if (contato != null) {
                        contatos.add(contato);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        Log.v("PDM3","contatos:"+contatos.size());
        user.setContatos(contatos);
    }
    protected void preencherLista(User user){

        final ArrayList<Contato> contatos = user.getContatos();
        Collections.sort(contatos);

        RVAdapter adapter = new RVAdapter(contatos);
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
    }

    protected boolean checarPermissaoPhone_SMD(String numero){

        numeroCall=numero;
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
      == PackageManager.PERMISSION_GRANTED){

          Log.v ("SMD","Tenho permissão");

          return true;

      } else {

            if ( shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){

                Log.v ("SMD","Primeira Vez");


                String mensagem = "Nossa aplicação precisa acessar o telefone para discagem automática. Uma janela de permissão será solicitada";
                String titulo = "Permissão de acesso a chamadas";
                int codigo =1;
                UIEducacionalPermissao mensagemPermissao = new UIEducacionalPermissao(mensagem,titulo, codigo);

                mensagemPermissao.onAttach ((Context)this);
                mensagemPermissao.show(getSupportFragmentManager(), "primeiravez2");

            }else{
                String mensagem = "Nossa aplicação precisa acessar o telefone para discagem automática. Uma janela de permissão será solicitada";
                String titulo = "Permissão de acesso a chamadas II";
                int codigo =1;

                UIEducacionalPermissao mensagemPermissao = new UIEducacionalPermissao(mensagem,titulo, codigo);
                mensagemPermissao.onAttach ((Context)this);
                mensagemPermissao.show(getSupportFragmentManager(), "segundavez2");
                Log.v ("SMD","Outra Vez");

            }
      }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            int[] grantResults) {
        switch (requestCode) {
            case 2222:
               if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                   Toast.makeText(this, "VALEU", Toast.LENGTH_LONG).show();
                   Uri uri = Uri.parse(numeroCall);
                   //   Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                   Intent itLigar = new Intent(Intent.ACTION_CALL, uri);
                   startActivity(itLigar);

               }else{
                   Toast.makeText(this, "SEU FELA!", Toast.LENGTH_LONG).show();

                   String mensagem= "Seu aplicativo pode ligar diretamente, mas sem permissão não funciona. Se você marcou não perguntar mais, você deve ir na tela de configurações para mudar a instalação ou reinstalar o aplicativo  ";
                   String titulo= "Porque precisamos telefonar?";
                   UIEducacionalPermissao mensagemPermisso = new UIEducacionalPermissao(mensagem,titulo,2);
                   mensagemPermisso.onAttach((Context)this);
                   mensagemPermisso.show(getSupportFragmentManager(), "segundavez");
               }
                break;
        }
    }
            @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Checagem de o Item selecionado é o do perfil
        if (item.getItemId() == R.id.anvPerfil) {
            //Abertura da Tela MudarDadosUsario
            Intent intent = new Intent(this, PerfilUsuario_Activity.class);
            intent.putExtra("usuario", user);
            startActivityForResult(intent, 1111);

        }
        // Checagem de o Item selecionado é o do perfil
        if (item.getItemId() == R.id.anvMudar) {
            //Abertura da Tela Mudar COntatos
            Intent intent = new Intent(this, AlterarContatos_Activity.class);
            intent.putExtra("usuario", user);
            startActivityForResult(intent, 1112);

        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Caso seja um Voltar ou Sucesso selecionar o item Ligar

        if (requestCode == 1111) {//Retorno de Mudar Perfil
            bnv.setSelectedItemId(R.id.anvLigar);
            user=atualizarUser();
            setTitle("Contatos de Emergência de "+ user.getNome());
            atualizarListaDeContatos(user);
            preencherLista(user); //Montagem da lista
        }

        if (requestCode == 1112) {//Retorno de Mudar Contatos
            bnv.setSelectedItemId(R.id.anvLigar);
            atualizarListaDeContatos(user);
            preencherLista(user); //Montagem da lista
        }



    }

    private User atualizarUser() {
        User user = null;
        SharedPreferences temUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        String loginSalvo = temUser.getString("login","");
        String senhaSalva = temUser.getString("senha","");
        String nomeSalvo = temUser.getString("nome","");
        boolean manterLogado=temUser.getBoolean("manterLogado",false);

        user=new User(nomeSalvo,loginSalvo,senhaSalva,manterLogado);
        return user;
    }

    @Override
    public void onDialogPositiveClick(int codigo) {

        if (codigo==1){
          String[] permissions ={Manifest.permission.CALL_PHONE};
          requestPermissions(permissions, 2222);

        }


    }

    @Override
    public void onItemClick(View view, int position) {

        final ArrayList<Contato> contatos = user.getContatos();
        Collections.sort(contatos);

        if (checarPermissaoPhone_SMD(contatos.get(position).getNumero())) {

            Uri uri = Uri.parse(contatos.get(position).getNumero());
            Intent itLigar = new Intent(Intent.ACTION_CALL, uri);
            startActivity(itLigar);
        }

    }
}


