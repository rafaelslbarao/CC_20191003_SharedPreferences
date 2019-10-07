package br.rafael.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TelaPrincipalActivity extends AppCompatActivity {

    private static final int WRITE_REQUEST_CODE = 1;
    //
    private RadioGroup rgTipoDiretorio;
    private RadioButton rbExterno;
    private RadioButton rbInterno;
    private RadioButton rbPublico;
    private AppCompatEditText etTexto;
    private AppCompatButton btSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        inicializaCompenentes();
        //
        pedePermissoes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        salvaArquivo();
    }

    private void pedePermissoes() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, WRITE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão deve ser aceita", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                break;
        }
    }

    private void inicializaCompenentes() {
        AppCompatButton btSair = findViewById(R.id.btSair);
        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaInformacoesLogin();
                startActivity(new Intent(TelaPrincipalActivity.this, LoginActivity.class));
                finish();
            }
        });
        //
        rgTipoDiretorio = findViewById(R.id.rgTipoDiretorio);
        rbExterno = findViewById(R.id.rbExterno);
        rbInterno = findViewById(R.id.rbInterno);
        rbPublico = findViewById(R.id.rbPublico);
        etTexto = findViewById(R.id.etTexto);
        btSalvar = findViewById(R.id.btSalvar);
        //
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaArquivo();
            }
        });
        //
        rgTipoDiretorio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                carregaInformacoesArquivo();
            }
        });
    }

    private void limpaInformacoesLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("NOME DO ARQUIVO", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USUARIO");
        editor.remove("SENHA");
        editor.apply();
    }

    private void salvaArquivo() {
        switch (rgTipoDiretorio.getCheckedRadioButtonId()) {
            case R.id.rbExterno:
                salvaArquivoExterno();
                break;
            case R.id.rbInterno:
                salvaArquivoInterno();
                break;
            case R.id.rbPublico:
                salvaArquivoPublico();
                break;
        }
    }

    private void carregaInformacoesArquivo() {
        switch (rgTipoDiretorio.getCheckedRadioButtonId()) {
            case R.id.rbExterno:
                carregaArquivoExterno();
                break;
            case R.id.rbInterno:
                carregaArquivoInterno();
                break;
            case R.id.rbPublico:
                carregaArquivoPublico();
                break;
        }
    }

    private void salvaArquivoPublico() {
        File pastaPublica;
        pastaPublica = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //
        if (!pastaPublica.exists())
            pastaPublica.mkdirs();
        //
        File arquivo = new File(pastaPublica, "NOME DO ARQUIVO");
        criaESalvaArquivo(arquivo);
    }

    private void salvaArquivoInterno() {
        File pastaInterna;
        pastaInterna = getFilesDir();
        pastaInterna = new File(pastaInterna, "MINHA PASTA INTERNA");
        //
        if (!pastaInterna.exists())
            pastaInterna.mkdirs();
        //
        File arquivo = new File(pastaInterna, "NOME DO ARQUIVO");
        criaESalvaArquivo(arquivo);
    }

    private void salvaArquivoExterno() {
        File pastaExterna;
        pastaExterna = getExternalFilesDir("MINHA PASTA EXTERNA");
        //
        if (!pastaExterna.exists())
            pastaExterna.mkdirs();
        //
        File arquivo = new File(pastaExterna, "NOME DO ARQUIVO");
        criaESalvaArquivo(arquivo);
    }

    private void criaESalvaArquivo(File arquivo) {
        if (arquivo.exists())
            arquivo.delete();

        try {
            arquivo.createNewFile();
            FileWriter fileWriter = new FileWriter(arquivo);
            fileWriter.write(etTexto.getText().toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void carregaArquivoExterno() {
        File pastaExterna;
        pastaExterna = getExternalFilesDir("MINHA PASTA EXTERNA");
        File arquivo = new File(pastaExterna, "NOME DO ARQUIVO");
        carregaInformacoesArquivo(arquivo);
    }

    private void carregaArquivoInterno() {
        File pastaInterna;
        pastaInterna = getFilesDir();
        pastaInterna = new File(pastaInterna, "MINHA PASTA INTERNA");
        File arquivo = new File(pastaInterna, "NOME DO ARQUIVO");
        carregaInformacoesArquivo(arquivo);
    }

    private void carregaArquivoPublico() {
        File pastaPublica;
        pastaPublica = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File arquivo = new File(pastaPublica, "NOME DO ARQUIVO");
        carregaInformacoesArquivo(arquivo);
    }

    private void carregaInformacoesArquivo(File arquivo)
    {
        if(arquivo.exists())
        {
            try {
                FileReader fileReader = new FileReader(arquivo);
                char[] informacoes = new char[(int) arquivo.length()];
                fileReader.read(informacoes);
                etTexto.setText(new String(informacoes));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {
            etTexto.setText("");
        }
    }
}
