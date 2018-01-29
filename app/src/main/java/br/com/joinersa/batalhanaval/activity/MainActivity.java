package br.com.joinersa.batalhanaval.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import br.com.joinersa.batalhanaval.R;

public class MainActivity extends AppCompatActivity {

    ImageView ivSobre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btnIniciarPartida = (ImageView) findViewById(R.id.btn_iniciar_partida);
        ivSobre = (ImageView) findViewById(R.id.iv_sobre);

        btnIniciarPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MeuTabuleiroActivity.class));
            }
        });

        ivSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SobreActivity.class));
            }
        });

    }
}
