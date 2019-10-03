package br.rafael.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class TelaPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        inicializaCompenentes();
    }

    private void inicializaCompenentes()
    {
        AppCompatButton btSair =findViewById(R.id.btSair);
        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaInformacoesLogin();
                startActivity(new Intent(TelaPrincipalActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void limpaInformacoesLogin()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("NOME DO ARQUIVO", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USUARIO");
        editor.remove("SENHA");
        editor.apply();
    }
}
