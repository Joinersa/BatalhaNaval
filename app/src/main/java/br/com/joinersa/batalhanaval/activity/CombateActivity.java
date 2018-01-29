package br.com.joinersa.batalhanaval.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.joinersa.batalhanaval.ChatController;
import br.com.joinersa.batalhanaval.R;
import br.com.joinersa.batalhanaval.entity.NodoBarco;

public class CombateActivity extends BluetoothCheckActivity implements ChatController.ChatListener {

    protected static final int TAM_MATRIZ = 7, VITORIA = 6, VERTICAl = 0, HORIZONTAL = 1;
    protected static final int NAVIO_1 = 1, NAVIO_2 = 2, NAVIO_3 = 3;

    private int mCountNavio1 = 0;
    private int mCountNavio2 = 0;
    private int mCountNavio3 = 0;

    private int countStatusAdversarioNavio1 = 0;
    private int countStatusAdversarioNavio2 = 0;
    private int countStatusAdversarioNavio3 = 0;


    protected static final String TAG = "livroandroid";
    // Precisa utilizar o mesmo UUID que o servidor utilizou para abrir o socket servidor
    protected static final UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    protected BluetoothDevice device;
    protected String msgEnviar, msgReceber;
    protected ChatController chat;

    private boolean[][] matrizMarcados = new boolean[TAM_MATRIZ][TAM_MATRIZ];

    protected ArrayList<NodoBarco> nodoBarcosList;

    private NodoBarco[][] minhaMatriz = new NodoBarco[TAM_MATRIZ][TAM_MATRIZ];


    private boolean primeiraVez = true;
    private String jogador;

    private TextView tvInfoCombate, tvStatusAdversario;
    private ImageView ivStatusAdversario;

    private boolean minhaVez;

    private int linhaGlobal, colunaGlobal;

    private GridLayout glTabuleiro;

    private ImageView a0, a1, a2, a3, a4, a5, a6,
            b0, b1, b2, b3, b4, b5, b6,
            c0, c1, c2, c3, c4, c5, c6,
            d0, d1, d2, d3, d4, d5, d6,
            e0, e1, e2, e3, e4, e5, e6,
            f0, f1, f2, f3, f4, f5, f6,
            g0, g1, g2, g3, g4, g5, g6;


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_combate);
        carregarViews();

        // ----- Inicializar matriz de posições marcadas ------ *obs.: pode não ser necessário
        for (int i = 0; i < TAM_MATRIZ; i++) {
            for (int j = 0; j < TAM_MATRIZ; j++) {
                matrizMarcados[i][j] = false;
            }
        }
        // ----------------------------------------------------


        // ---- SERVIDOR -----
        // Mod Joiner - Verifica se está no modo servidor. Se a lista estiver preenchida, ja foi preenchida por herança.
        if (nodoBarcosList != null) {
            System.out.println("---------- Minha matriz (Servidor) ------------");
            // Inserir os dados na estrutura de dados adequada (matriz)
            int aux = 0;
            for (int i = 0; i < TAM_MATRIZ; i++) {
                for (int j = 0; j < TAM_MATRIZ; j++) {
                    minhaMatriz[i][j] = nodoBarcosList.get(aux);
                    aux++;
                    System.out.print(minhaMatriz[i][j].getTipo() + " "); // TESTE
                }
                System.out.println(); // TESTE
            }

            // se for o servidor, começa jogando:
            minhaVez = true;

        }
        // -------------------

        // ---- CLIENTE ------
        // Device selecionado na lista
        device = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        try {
            // FAZ A CONEXÃO SE ABRIU NO MODO CHAT CLIENTE ***********
            if (device != null) {

                //Toast.makeText(this, "Conectado: " + device.getName(), Toast.LENGTH_SHORT).show();

                // Pegar dados da outra activity em um ArrayList parcelable
                nodoBarcosList = getIntent().getParcelableArrayListExtra("minha_matriz");
                System.out.println("---------- Minha matriz (Cliente) ------------");
                // Inserir os dados na estrutura de dados adequada (matriz)
                int aux = 0;
                for (int i = 0; i < TAM_MATRIZ; i++) {
                    for (int j = 0; j < TAM_MATRIZ; j++) {
                        minhaMatriz[i][j] = nodoBarcosList.get(aux);
                        aux++;
                        System.out.print(minhaMatriz[i][j].getTipo() + " "); // TESTE
                    }
                    System.out.println(); // TESTE
                }

                // Faz a conexão utilizando o mesmo UUID que o servidor utilizou
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                // Inicia o controlador chat
                chat = new ChatController(socket, this);
                chat.start();

                // Se for cliente, espera pra jogar (na primeira vez):
                //se for o servidor, começa jogando:
                minhaVez = false;
            }
        } catch (IOException e) {
            error("Erro ao conectar: " + e.getMessage(), e);
        }
        // -------------------

        ativarViewsDoJogador(minhaVez); // ativa ou desativa as views de acordo com a vez do jogador. O servidor sempre começará jogando.
    }


    private void ativarViewsDoJogador(boolean minhaVez) {


        if (minhaVez) {

            for (int linha = 0; linha < TAM_MATRIZ; linha++) {
                for (int coluna = 0; coluna < TAM_MATRIZ; coluna++) {
                    getImageView(linha, coluna).setClickable(!matrizMarcados[linha][coluna]);
                }
            }

            tvInfoCombate.setText("Sua vez");
            tvInfoCombate.setBackgroundColor(Color.GREEN);

        } else {

            for (int linha = 0; linha < TAM_MATRIZ; linha++) {
                for (int coluna = 0; coluna < TAM_MATRIZ; coluna++) {
                    getImageView(linha, coluna).setClickable(false);
                }
            }

            tvInfoCombate.setText("Vez do adversário...");
            tvInfoCombate.setBackgroundColor(Color.RED);
        }
    }

    private void error(final String msg, final IOException e) {
        Log.e(TAG, "Erro no cliente: " + e.getMessage(), e);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CombateActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void vibrate(long milliseconds)
    {
//        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        rr.vibrate(milliseconds);

        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(milliseconds);
        }
    }

    private void carregarViews() {

        glTabuleiro = (GridLayout) findViewById(R.id.gl_matriz_combate);
        tvInfoCombate = (TextView) findViewById(R.id.tv_informacao_combate);
        tvStatusAdversario = (TextView) findViewById(R.id.tv_status_adversario);
        ivStatusAdversario = (ImageView) findViewById(R.id.iv_status_adversario);

        a0 = (ImageView) findViewById(R.id.iv_a0);
        a1 = (ImageView) findViewById(R.id.iv_a1);
        a2 = (ImageView) findViewById(R.id.iv_a2);
        a3 = (ImageView) findViewById(R.id.iv_a3);
        a4 = (ImageView) findViewById(R.id.iv_a4);
        a5 = (ImageView) findViewById(R.id.iv_a5);
        a6 = (ImageView) findViewById(R.id.iv_a6);

        b0 = (ImageView) findViewById(R.id.iv_b0);
        b1 = (ImageView) findViewById(R.id.iv_b1);
        b2 = (ImageView) findViewById(R.id.iv_b2);
        b3 = (ImageView) findViewById(R.id.iv_b3);
        b4 = (ImageView) findViewById(R.id.iv_b4);
        b5 = (ImageView) findViewById(R.id.iv_b5);
        b6 = (ImageView) findViewById(R.id.iv_b6);

        c0 = (ImageView) findViewById(R.id.iv_c0);
        c1 = (ImageView) findViewById(R.id.iv_c1);
        c2 = (ImageView) findViewById(R.id.iv_c2);
        c3 = (ImageView) findViewById(R.id.iv_c3);
        c4 = (ImageView) findViewById(R.id.iv_c4);
        c5 = (ImageView) findViewById(R.id.iv_c5);
        c6 = (ImageView) findViewById(R.id.iv_c6);

        d0 = (ImageView) findViewById(R.id.iv_d0);
        d1 = (ImageView) findViewById(R.id.iv_d1);
        d2 = (ImageView) findViewById(R.id.iv_d2);
        d3 = (ImageView) findViewById(R.id.iv_d3);
        d4 = (ImageView) findViewById(R.id.iv_d4);
        d5 = (ImageView) findViewById(R.id.iv_d5);
        d6 = (ImageView) findViewById(R.id.iv_d6);

        e0 = (ImageView) findViewById(R.id.iv_e0);
        e1 = (ImageView) findViewById(R.id.iv_e1);
        e2 = (ImageView) findViewById(R.id.iv_e2);
        e3 = (ImageView) findViewById(R.id.iv_e3);
        e4 = (ImageView) findViewById(R.id.iv_e4);
        e5 = (ImageView) findViewById(R.id.iv_e5);
        e6 = (ImageView) findViewById(R.id.iv_e6);

        f0 = (ImageView) findViewById(R.id.iv_f0);
        f1 = (ImageView) findViewById(R.id.iv_f1);
        f2 = (ImageView) findViewById(R.id.iv_f2);
        f3 = (ImageView) findViewById(R.id.iv_f3);
        f4 = (ImageView) findViewById(R.id.iv_f4);
        f5 = (ImageView) findViewById(R.id.iv_f5);
        f6 = (ImageView) findViewById(R.id.iv_f6);

        g0 = (ImageView) findViewById(R.id.iv_g0);
        g1 = (ImageView) findViewById(R.id.iv_g1);
        g2 = (ImageView) findViewById(R.id.iv_g2);
        g3 = (ImageView) findViewById(R.id.iv_g3);
        g4 = (ImageView) findViewById(R.id.iv_g4);
        g5 = (ImageView) findViewById(R.id.iv_g5);
        g6 = (ImageView) findViewById(R.id.iv_g6);

        // ----------------------------------------
        a0.setOnClickListener(new MyOnClickListener(0, 0));
        a1.setOnClickListener(new MyOnClickListener(0, 1));
        a2.setOnClickListener(new MyOnClickListener(0, 2));
        a3.setOnClickListener(new MyOnClickListener(0, 3));
        a4.setOnClickListener(new MyOnClickListener(0, 4));
        a5.setOnClickListener(new MyOnClickListener(0, 5));
        a6.setOnClickListener(new MyOnClickListener(0, 6));

        b0.setOnClickListener(new MyOnClickListener(1, 0));
        b1.setOnClickListener(new MyOnClickListener(1, 1));
        b2.setOnClickListener(new MyOnClickListener(1, 2));
        b3.setOnClickListener(new MyOnClickListener(1, 3));
        b4.setOnClickListener(new MyOnClickListener(1, 4));
        b5.setOnClickListener(new MyOnClickListener(1, 5));
        b6.setOnClickListener(new MyOnClickListener(1, 6));

        c0.setOnClickListener(new MyOnClickListener(2, 0));
        c1.setOnClickListener(new MyOnClickListener(2, 1));
        c2.setOnClickListener(new MyOnClickListener(2, 2));
        c3.setOnClickListener(new MyOnClickListener(2, 3));
        c4.setOnClickListener(new MyOnClickListener(2, 4));
        c5.setOnClickListener(new MyOnClickListener(2, 5));
        c6.setOnClickListener(new MyOnClickListener(2, 6));

        d0.setOnClickListener(new MyOnClickListener(3, 0));
        d1.setOnClickListener(new MyOnClickListener(3, 1));
        d2.setOnClickListener(new MyOnClickListener(3, 2));
        d3.setOnClickListener(new MyOnClickListener(3, 3));
        d4.setOnClickListener(new MyOnClickListener(3, 4));
        d5.setOnClickListener(new MyOnClickListener(3, 5));
        d6.setOnClickListener(new MyOnClickListener(3, 6));

        e0.setOnClickListener(new MyOnClickListener(4, 0));
        e1.setOnClickListener(new MyOnClickListener(4, 1));
        e2.setOnClickListener(new MyOnClickListener(4, 2));
        e3.setOnClickListener(new MyOnClickListener(4, 3));
        e4.setOnClickListener(new MyOnClickListener(4, 4));
        e5.setOnClickListener(new MyOnClickListener(4, 5));
        e6.setOnClickListener(new MyOnClickListener(4, 6));

        f0.setOnClickListener(new MyOnClickListener(5, 0));
        f1.setOnClickListener(new MyOnClickListener(5, 1));
        f2.setOnClickListener(new MyOnClickListener(5, 2));
        f3.setOnClickListener(new MyOnClickListener(5, 3));
        f4.setOnClickListener(new MyOnClickListener(5, 4));
        f5.setOnClickListener(new MyOnClickListener(5, 5));
        f6.setOnClickListener(new MyOnClickListener(5, 6));

        g0.setOnClickListener(new MyOnClickListener(6, 0));
        g1.setOnClickListener(new MyOnClickListener(6, 1));
        g2.setOnClickListener(new MyOnClickListener(6, 2));
        g3.setOnClickListener(new MyOnClickListener(6, 3));
        g4.setOnClickListener(new MyOnClickListener(6, 4));
        g5.setOnClickListener(new MyOnClickListener(6, 5));
        g6.setOnClickListener(new MyOnClickListener(6, 6));

    }

    private ImageView getImageView(int linha, int coluna) {
        switch (linha) {
            case 0:
                switch (coluna) {
                    case 0:
                        return a0;
                    case 1:
                        return a1;
                    case 2:
                        return a2;
                    case 3:
                        return a3;
                    case 4:
                        return a4;
                    case 5:
                        return a5;
                    case 6:
                        return a6;
                }
            case 1:
                switch (coluna) {
                    case 0:
                        return b0;
                    case 1:
                        return b1;
                    case 2:
                        return b2;
                    case 3:
                        return b3;
                    case 4:
                        return b4;
                    case 5:
                        return b5;
                    case 6:
                        return b6;
                }
            case 2:
                switch (coluna) {
                    case 0:
                        return c0;
                    case 1:
                        return c1;
                    case 2:
                        return c2;
                    case 3:
                        return c3;
                    case 4:
                        return c4;
                    case 5:
                        return c5;
                    case 6:
                        return c6;
                }
            case 3:
                switch (coluna) {
                    case 0:
                        return d0;
                    case 1:
                        return d1;
                    case 2:
                        return d2;
                    case 3:
                        return d3;
                    case 4:
                        return d4;
                    case 5:
                        return d5;
                    case 6:
                        return d6;
                }
            case 4:
                switch (coluna) {
                    case 0:
                        return e0;
                    case 1:
                        return e1;
                    case 2:
                        return e2;
                    case 3:
                        return e3;
                    case 4:
                        return e4;
                    case 5:
                        return e5;
                    case 6:
                        return e6;
                }
            case 5:
                switch (coluna) {
                    case 0:
                        return f0;
                    case 1:
                        return f1;
                    case 2:
                        return f2;
                    case 3:
                        return f3;
                    case 4:
                        return f4;
                    case 5:
                        return f5;
                    case 6:
                        return f6;
                }
            case 6:
                switch (coluna) {
                    case 0:
                        return g0;
                    case 1:
                        return g1;
                    case 2:
                        return g2;
                    case 3:
                        return g3;
                    case 4:
                        return g4;
                    case 5:
                        return g5;
                    case 6:
                        return g6;
                }
        }
        return null;
    }

    private class MyOnClickListener implements View.OnClickListener {

        private int linha, coluna;

        public MyOnClickListener(int linha, int coluna) {
            this.linha = linha;
            this.coluna = coluna;
            linhaGlobal = linha;
            colunaGlobal = coluna;
        }

        @Override
        public void onClick(View view) {

            view.setClickable(false); // desabilitar o click

            linhaGlobal = linha;
            colunaGlobal = coluna;

            matrizMarcados[linha][coluna] = true;

            // ---------- ENVIAR MENSAGEM PARA O OUTRO DEVICE -------------

            try {
                chat.sendMessage(linha + "," + coluna);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // ------------------------------------------------------------

        }
    }

    @Override
    public void onMessageReceived(final String msg) {
        Log.i(TAG, "onMessageReceived (recebeu uma mensagem): " + msg);
        // É chamado numa thread, portanto use o runOnUiThread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // RECEBENDO MENSAGEM DO ADVERSÁRIO

                //Toast.makeText(CombateActivity.this, msg, Toast.LENGTH_SHORT).show();


                if (minhaVez) {
                    //formato de string: "tipo,orientacao,parte", "int,int,int". Ex.: "1,2,3"

                    int tipo = Integer.parseInt(msg.substring(0, 1));
                    int orientacao = Integer.parseInt(msg.substring(2, 3));
                    int parte = Integer.parseInt(msg.substring(4, 5));

                    setarImagem(tipo, orientacao, parte);

                    // Se Eu errar o alvo
                    if (tipo == 0) {
                        minhaVez = false;
                        ativarViewsDoJogador(false);
                    } else {
                        if (mCountNavio1 + mCountNavio2 + mCountNavio3 == VITORIA) {
                            // Minha vitória...
                            vibrate(500);
                            startActivity(new Intent(CombateActivity.this, VitoriaActivity.class));
                            finish();
                        } else {
                            vibrate(400);
                        }
                    }

                } else {
                    // VEZ DO ADVERSÁRIO

                    // formato: "linha, coluna". Ex.: "1,2"
                    int linha = Integer.parseInt(msg.substring(0, 1));
                    int coluna = Integer.parseInt(msg.substring(2, 3));

                    //formato de string: "tipo,orientacao,parte". Ex.: "1,2,3"

                    int mTipo = minhaMatriz[linha][coluna].getTipo();
                    int mOrientacao = minhaMatriz[linha][coluna].getOrientacao();
                    int mParte = minhaMatriz[linha][coluna].getParte();

                    try {
                        chat.sendMessage(mTipo + "," + mOrientacao + "," + mParte);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if (mTipo == 1) {
                        countStatusAdversarioNavio1++;
                    }
                    if (mTipo == 2) {
                        countStatusAdversarioNavio2++;
                    }
                    if (mTipo == 3) {
                        countStatusAdversarioNavio3++;
                    }
                    // AQUI SERÁ MOSTRADO O STATUS DO ADVERSÁRIO.. BARCOS EXPLODINDO, BOMBAS CAINDO NA AGUA, ETC.
                    setarStatusAdversario(mTipo);

                    // Se o adversário errar, serei habilitado para jogar.
                    if (mTipo == 0) {
                        minhaVez = true;
                        ativarViewsDoJogador(true);
                    } else {
                        // Adversário Vendeu...
                        if (countStatusAdversarioNavio1 + countStatusAdversarioNavio2 + countStatusAdversarioNavio3 == VITORIA) {
                            startActivity(new Intent(CombateActivity.this, DerrotaActivity.class));
                            finish();
                        } else {
                            vibrate(400);
                        }
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chat != null) {
            chat.stop();
        }
    }


    private void setarImagem(int tipo, int orientacao, int parte) {

        ImageView iv = getImageView(linhaGlobal, colunaGlobal);

        if (tipo > 0) {

            if (tipo == NAVIO_1) {
                // BARCO 1:
                mCountNavio1++;

                if (orientacao == VERTICAl) {
                    iv.setImageResource(R.mipmap.navio_vertical_1_parte_1);
                } else {
                    iv.setImageResource(R.mipmap.navio_horizontal_1_parte_1);
                }

            } else if (tipo == NAVIO_2) {
                // BARCO 2:
                mCountNavio2++;

                if (orientacao == VERTICAl) {
                    // SÓ PARA BARCOS VERTICAIS
                    if (mCountNavio2 == 2) {
                        if (parte == 1) {
                            iv.setImageResource(R.mipmap.navio_vertical_2_parte_1);
                            getImageView(linhaGlobal + 1, colunaGlobal).setImageResource(R.mipmap.navio_vertical_2_parte_2);
                        } else {
                            getImageView(linhaGlobal - 1, colunaGlobal).setImageResource(R.mipmap.navio_vertical_2_parte_1);
                            iv.setImageResource(R.mipmap.navio_vertical_2_parte_2);
                        }

                    } else {
                        iv.setImageResource(R.mipmap.ic_ok);
                    }

                } else {
                    // SÓ PARA BARCOS HORIZONTAIS
                    if (mCountNavio2 == 2) {
                        if (parte == 1) {
                            iv.setImageResource(R.mipmap.navio_horizontal_2_parte_1);
                            getImageView(linhaGlobal, colunaGlobal + 1).setImageResource(R.mipmap.navio_horizontal_2_parte_2);
                        } else {
                            getImageView(linhaGlobal, colunaGlobal - 1).setImageResource(R.mipmap.navio_horizontal_2_parte_1);
                            iv.setImageResource(R.mipmap.navio_horizontal_2_parte_2);
                        }

                    } else {
                        iv.setImageResource(R.mipmap.ic_ok);
                    }

                }

            } else {
                // BARCO 3:
                mCountNavio3++;

                if (orientacao == VERTICAl) {

                    if (mCountNavio3 == 3) {
                        if (parte == 1) {
                            iv.setImageResource(R.mipmap.navio_vertical_3_parte_1);
                            getImageView(linhaGlobal + 1, colunaGlobal).setImageResource(R.mipmap.navio_vertical_3_parte_2);
                            getImageView(linhaGlobal + 2, colunaGlobal).setImageResource(R.mipmap.navio_vertical_3_parte_3);
                        } else if (parte == 2) {
                            getImageView(linhaGlobal - 1, colunaGlobal).setImageResource(R.mipmap.navio_vertical_3_parte_1);
                            iv.setImageResource(R.mipmap.navio_vertical_3_parte_2);
                            getImageView(linhaGlobal + 1, colunaGlobal).setImageResource(R.mipmap.navio_vertical_3_parte_3);
                        } else {
                            getImageView(linhaGlobal - 2, colunaGlobal).setImageResource(R.mipmap.navio_vertical_3_parte_1);
                            getImageView(linhaGlobal - 1, colunaGlobal).setImageResource(R.mipmap.navio_vertical_3_parte_2);
                            iv.setImageResource(R.mipmap.navio_vertical_3_parte_3);
                        }

                    } else {
                        iv.setImageResource(R.mipmap.ic_ok);
                    }

                } else {
                    // HORIZONTAL
                    if (mCountNavio3 == 3) {
                        if (parte == 1) {
                            iv.setImageResource(R.mipmap.navio_horizontal_3_parte_1);
                            getImageView(linhaGlobal, colunaGlobal + 1).setImageResource(R.mipmap.navio_horizontal_3_parte_2);
                            getImageView(linhaGlobal, colunaGlobal + 2).setImageResource(R.mipmap.navio_horizontal_3_parte_3);
                        } else if (parte == 2) {
                            getImageView(linhaGlobal, colunaGlobal - 1).setImageResource(R.mipmap.navio_horizontal_3_parte_1);
                            iv.setImageResource(R.mipmap.navio_horizontal_3_parte_2);
                            getImageView(linhaGlobal, colunaGlobal + 1).setImageResource(R.mipmap.navio_horizontal_3_parte_3);
                        } else {
                            getImageView(linhaGlobal, colunaGlobal - 2).setImageResource(R.mipmap.navio_horizontal_3_parte_1);
                            getImageView(linhaGlobal, colunaGlobal - 1).setImageResource(R.mipmap.navio_horizontal_3_parte_2);
                            iv.setImageResource(R.mipmap.navio_horizontal_3_parte_3);
                        }

                    } else {
                        iv.setImageResource(R.mipmap.ic_ok);
                    }

                }

            }

        } else {
            // Jogador errou:
            iv.setImageResource(R.mipmap.ic_erro_na_jogada);
        }
    }

    private void setarStatusAdversario(int tipo) {

        if (tipo == NAVIO_1) {
            tvStatusAdversario.setText("Adversário explodiu seu navio 1");
            ivStatusAdversario.setImageResource(R.drawable.navio_explodido_1);

        } else if (tipo == NAVIO_2) {
            if (countStatusAdversarioNavio2 == 1) {
                tvStatusAdversario.setText("Adversário atingiu uma parte de seu navio 2");
                ivStatusAdversario.setImageResource(R.drawable.navio_explodido_2_1);
            } else if (countStatusAdversarioNavio2 == 2) {
                tvStatusAdversario.setText("Adversário explodiu seu navio 2");
                ivStatusAdversario.setImageResource(R.drawable.navio_explodido_2_2);
            }

        } else if (tipo == NAVIO_3){
            if (countStatusAdversarioNavio3 == 1) {
                tvStatusAdversario.setText("Adversário atingiu uma parte de seu navio 3");
                ivStatusAdversario.setImageResource(R.drawable.navio_explodido_3_1);
            } else if (countStatusAdversarioNavio3 == 2) {
                tvStatusAdversario.setText("Adversário atingiu duas partes de seu navio 3");
                ivStatusAdversario.setImageResource(R.drawable.navio_explodido_3_2);
            } else if (countStatusAdversarioNavio3 == 3){
                tvStatusAdversario.setText("Seu navio 3 foi detonado!");
                ivStatusAdversario.setImageResource(R.drawable.navio_explodido_3_3);
            }

        } else {
            tvStatusAdversario.setText("Seu adversário errou!");
            ivStatusAdversario.setImageResource(R.mipmap.ic_erro_na_jogada);
        }
    }

}
