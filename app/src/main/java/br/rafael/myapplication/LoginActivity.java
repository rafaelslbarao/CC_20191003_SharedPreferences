package br.rafael.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private AppCompatEditText etUsuario;
    private AppCompatEditText etSenha;
    private AppCompatButton btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!verificaLoginRealizado()) {
            setContentView(R.layout.activity_login);
            inicializaComponentes();
        }
        else
        {
            startActivity(new Intent(this, TelaPrincipalActivity.class));
            finish();
        }
    }

    private void inicializaComponentes()
    {
        etUsuario = findViewById(R.id.etUsuario);
        etSenha = findViewById(R.id.etSenha);
        btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                efetuaLogin();
            }
        });
    }

    private void efetuaLogin()
    {
        String usuario = etUsuario.getText().toString().trim();
        String senha = etSenha.getText().toString();
        //
        if(usuario.isEmpty()) {
            Toast.makeText(this, "Informe o usuário", Toast.LENGTH_SHORT).show();
            return;
        }
        if(senha.isEmpty()) {
            Toast.makeText(this, "Informe o usuário", Toast.LENGTH_SHORT).show();
            return;
        }
        //
        salvaInformacoesLogin(usuario, senha);
        //
        startActivity(new Intent(this, TelaPrincipalActivity.class));
        finish();
    }

    private boolean verificaLoginRealizado()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("NOME DO ARQUIVO", MODE_PRIVATE);
        String usuario = sharedPreferences.getString("USUARIO", "");
        String senha = sharedPreferences.getString("SENHA", "");
        //
        if(usuario.isEmpty() || senha.isEmpty())
            return false;
        else
            return true;
    }

    private void salvaInformacoesLogin(String usuario, String senha)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("NOME DO ARQUIVO", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USUARIO", usuario);
        editor.putString("SENHA", senha);
        editor.apply();
    }
}
