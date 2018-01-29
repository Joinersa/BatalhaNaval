package br.com.joinersa.batalhanaval.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.joinersa.batalhanaval.R;
import br.com.joinersa.batalhanaval.entity.NodoBarco;

public class MeuTabuleiroActivity extends AppCompatActivity {

    ImageView ivBtnConectar, ivBtnGirar;
    ImageView ivNavioVertical1, ivNavioVertical2, ivNavioVertical3;
    ImageView ivNavioHorizontal1, ivNavioHorizontal2, ivNavioHorizontal3;

//    ArrayList<Integer> listaTipo;
//    ArrayList<Integer> listaHorientacao;
//    ArrayList<Integer> listaParte;

    ImageView a0, a1, a2, a3, a4, a5, a6,
            b0, b1, b2, b3, b4, b5, b6,
            c0, c1, c2, c3, c4, c5, c6,
            d0, d1, d2, d3, d4, d5, d6,
            e0, e1, e2, e3, e4, e5, e6,
            f0, f1, f2, f3, f4, f5, f6,
            g0, g1, g2, g3, g4, g5, g6;

    GridLayout gridLayoutMatriz;

    final int TAM_MATRIZ = 7;
    final int HORIZONTAL = 1, VERTICAL = 0;
    final int BARCO_1 = 1, BARCO_2 = 2, BARCO_3 = 3;

    private NodoBarco[][] matriz = new NodoBarco[TAM_MATRIZ][TAM_MATRIZ];
    private ArrayList<NodoBarco> nodoBarcosList;

    int countVezNavio = 1;
    int horientacao = VERTICAL;
    boolean flagAux = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_tabuleiro);

        // INICIALIZA A MATRIZ
        for (int i = 0; i < TAM_MATRIZ; i++) {
            for (int j = 0; j < TAM_MATRIZ; j++) {
                matriz[i][j] = new NodoBarco(0, 0, 0);
            }
        }

        carregarViews();
        setViewsOnLongClickListener();
        setViewsOnDragListener();
    }

    private void setViewsOnLongClickListener() {
        ivNavioVertical1.setOnTouchListener(new MyOnTouchListener());
        ivNavioVertical2.setOnTouchListener(new MyOnTouchListener());
        ivNavioVertical3.setOnTouchListener(new MyOnTouchListener());
        ivNavioHorizontal1.setOnTouchListener(new MyOnTouchListener());
        ivNavioHorizontal2.setOnTouchListener(new MyOnTouchListener());
        ivNavioHorizontal3.setOnTouchListener(new MyOnTouchListener());
    }

    private void setViewsOnDragListener() {

        a0.setOnDragListener(new MyOnDragListener(0, 0));
        a1.setOnDragListener(new MyOnDragListener(0, 1));
        a2.setOnDragListener(new MyOnDragListener(0, 2));
        a3.setOnDragListener(new MyOnDragListener(0, 3));
        a4.setOnDragListener(new MyOnDragListener(0, 4));
        a5.setOnDragListener(new MyOnDragListener(0, 5));
        a6.setOnDragListener(new MyOnDragListener(0, 6));

        b0.setOnDragListener(new MyOnDragListener(1, 0));
        b1.setOnDragListener(new MyOnDragListener(1, 1));
        b2.setOnDragListener(new MyOnDragListener(1, 2));
        b3.setOnDragListener(new MyOnDragListener(1, 3));
        b4.setOnDragListener(new MyOnDragListener(1, 4));
        b5.setOnDragListener(new MyOnDragListener(1, 5));
        b6.setOnDragListener(new MyOnDragListener(1, 6));

        c0.setOnDragListener(new MyOnDragListener(2, 0));
        c1.setOnDragListener(new MyOnDragListener(2, 1));
        c2.setOnDragListener(new MyOnDragListener(2, 2));
        c3.setOnDragListener(new MyOnDragListener(2, 3));
        c4.setOnDragListener(new MyOnDragListener(2, 4));
        c5.setOnDragListener(new MyOnDragListener(2, 5));
        c6.setOnDragListener(new MyOnDragListener(2, 6));

        d0.setOnDragListener(new MyOnDragListener(3, 0));
        d1.setOnDragListener(new MyOnDragListener(3, 1));
        d2.setOnDragListener(new MyOnDragListener(3, 2));
        d3.setOnDragListener(new MyOnDragListener(3, 3));
        d4.setOnDragListener(new MyOnDragListener(3, 4));
        d5.setOnDragListener(new MyOnDragListener(3, 5));
        d6.setOnDragListener(new MyOnDragListener(3, 6));

        e0.setOnDragListener(new MyOnDragListener(4, 0));
        e1.setOnDragListener(new MyOnDragListener(4, 1));
        e2.setOnDragListener(new MyOnDragListener(4, 2));
        e3.setOnDragListener(new MyOnDragListener(4, 3));
        e4.setOnDragListener(new MyOnDragListener(4, 4));
        e5.setOnDragListener(new MyOnDragListener(4, 5));
        e6.setOnDragListener(new MyOnDragListener(4, 6));

        f0.setOnDragListener(new MyOnDragListener(5, 0));
        f1.setOnDragListener(new MyOnDragListener(5, 1));
        f2.setOnDragListener(new MyOnDragListener(5, 2));
        f3.setOnDragListener(new MyOnDragListener(5, 3));
        f4.setOnDragListener(new MyOnDragListener(5, 4));
        f5.setOnDragListener(new MyOnDragListener(5, 5));
        f6.setOnDragListener(new MyOnDragListener(5, 6));

        g0.setOnDragListener(new MyOnDragListener(6, 0));
        g1.setOnDragListener(new MyOnDragListener(6, 1));
        g2.setOnDragListener(new MyOnDragListener(6, 2));
        g3.setOnDragListener(new MyOnDragListener(6, 3));
        g4.setOnDragListener(new MyOnDragListener(6, 4));
        g5.setOnDragListener(new MyOnDragListener(6, 5));
        g6.setOnDragListener(new MyOnDragListener(6, 6));

    }

    private void carregarViews() {

        ivNavioVertical1 = (ImageView) findViewById(R.id.iv_navio_vertical_1);
        ivNavioVertical2 = (ImageView) findViewById(R.id.iv_navio_vertical_2);
        ivNavioVertical3 = (ImageView) findViewById(R.id.iv_navio_vertical_3);
        ivNavioHorizontal1 = (ImageView) findViewById(R.id.iv_navio_horizontal_1);
        ivNavioHorizontal2 = (ImageView) findViewById(R.id.iv_navio_horizontal_2);
        ivNavioHorizontal3 = (ImageView) findViewById(R.id.iv_navio_horizontal_3);

        ivNavioVertical1.setVisibility(View.VISIBLE); // primeiro vem visível
        ivNavioVertical2.setVisibility(View.INVISIBLE);
        ivNavioVertical3.setVisibility(View.INVISIBLE);
        ivNavioHorizontal1.setVisibility(View.INVISIBLE);
        ivNavioHorizontal2.setVisibility(View.INVISIBLE);
        ivNavioHorizontal3.setVisibility(View.INVISIBLE);

        ivBtnConectar = (ImageView) findViewById(R.id.iv_btn_conectar);
        ivBtnGirar = (ImageView) findViewById(R.id.iv_btn_girar);

        gridLayoutMatriz = (GridLayout) findViewById(R.id.grid_layout_matriz);

        a0 = (ImageView) findViewById(R.id.a0);
        a1 = (ImageView) findViewById(R.id.a1);
        a2 = (ImageView) findViewById(R.id.a2);
        a3 = (ImageView) findViewById(R.id.a3);
        a4 = (ImageView) findViewById(R.id.a4);
        a5 = (ImageView) findViewById(R.id.a5);
        a6 = (ImageView) findViewById(R.id.a6);

        b0 = (ImageView) findViewById(R.id.b0);
        b1 = (ImageView) findViewById(R.id.b1);
        b2 = (ImageView) findViewById(R.id.b2);
        b3 = (ImageView) findViewById(R.id.b3);
        b4 = (ImageView) findViewById(R.id.b4);
        b5 = (ImageView) findViewById(R.id.b5);
        b6 = (ImageView) findViewById(R.id.b6);

        c0 = (ImageView) findViewById(R.id.c0);
        c1 = (ImageView) findViewById(R.id.c1);
        c2 = (ImageView) findViewById(R.id.c2);
        c3 = (ImageView) findViewById(R.id.c3);
        c4 = (ImageView) findViewById(R.id.c4);
        c5 = (ImageView) findViewById(R.id.c5);
        c6 = (ImageView) findViewById(R.id.c6);

        d0 = (ImageView) findViewById(R.id.d0);
        d1 = (ImageView) findViewById(R.id.d1);
        d2 = (ImageView) findViewById(R.id.d2);
        d3 = (ImageView) findViewById(R.id.d3);
        d4 = (ImageView) findViewById(R.id.d4);
        d5 = (ImageView) findViewById(R.id.d5);
        d6 = (ImageView) findViewById(R.id.d6);

        e0 = (ImageView) findViewById(R.id.e0);
        e1 = (ImageView) findViewById(R.id.e1);
        e2 = (ImageView) findViewById(R.id.e2);
        e3 = (ImageView) findViewById(R.id.e3);
        e4 = (ImageView) findViewById(R.id.e4);
        e5 = (ImageView) findViewById(R.id.e5);
        e6 = (ImageView) findViewById(R.id.e6);

        f0 = (ImageView) findViewById(R.id.f0);
        f1 = (ImageView) findViewById(R.id.f1);
        f2 = (ImageView) findViewById(R.id.f2);
        f3 = (ImageView) findViewById(R.id.f3);
        f4 = (ImageView) findViewById(R.id.f4);
        f5 = (ImageView) findViewById(R.id.f5);
        f6 = (ImageView) findViewById(R.id.f6);

        g0 = (ImageView) findViewById(R.id.g0);
        g1 = (ImageView) findViewById(R.id.g1);
        g2 = (ImageView) findViewById(R.id.g2);
        g3 = (ImageView) findViewById(R.id.g3);
        g4 = (ImageView) findViewById(R.id.g4);
        g5 = (ImageView) findViewById(R.id.g5);
        g6 = (ImageView) findViewById(R.id.g6);


        ivBtnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MeuTabuleiroActivity.this, CombateActivity.class));
                runAlertConectar();
            }
        });

        ivBtnGirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // verifica em qual vez está, ou seja, qual barco está visivel na tela. Se é o 1, 2 ou 3. (verticais ou horizontais)
                switch (countVezNavio) {
                    case 1:
                        if (ivNavioVertical1.getVisibility() == View.VISIBLE) {
                            ivNavioVertical1.setVisibility(View.INVISIBLE);
                            ivNavioHorizontal1.setVisibility(View.VISIBLE);
                        } else {
                            ivNavioVertical1.setVisibility(View.VISIBLE);
                            ivNavioHorizontal1.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        if (ivNavioVertical2.getVisibility() == View.VISIBLE) {
                            ivNavioVertical2.setVisibility(View.INVISIBLE);
                            ivNavioHorizontal2.setVisibility(View.VISIBLE);
                            horientacao = HORIZONTAL;
                        } else {
                            ivNavioVertical2.setVisibility(View.VISIBLE);
                            ivNavioHorizontal2.setVisibility(View.INVISIBLE);
                            horientacao = VERTICAL;
                        }
                        break;
                    case 3:
                        if (ivNavioVertical3.getVisibility() == View.VISIBLE) {
                            ivNavioVertical3.setVisibility(View.INVISIBLE);
                            ivNavioHorizontal3.setVisibility(View.VISIBLE);
                            horientacao = HORIZONTAL;
                        } else {
                            ivNavioVertical3.setVisibility(View.VISIBLE);
                            ivNavioHorizontal3.setVisibility(View.INVISIBLE);
                            horientacao = VERTICAL;
                        }
                        break;
                }

                // ATIVA E DESATIVA QUADRADOS
                removeDragListener();
            }
        });
    }

    private class MyOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                ClipData clipData = ClipData.newPlainText("simple_text", "text");
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(clipData, dragShadowBuilder, view, 0);
                } else {
                    view.startDrag(clipData, dragShadowBuilder, view, 0);
                }

                view.setVisibility(View.INVISIBLE);
                return true;

            } else {
                return false;
            }

        }
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

    private void removeDragListener() {
        //FALTA TATAMENTO DE ERRO AINDA

        for (int i = 0; i < TAM_MATRIZ; i++) {
            for (int j = 0; j < TAM_MATRIZ; j++) {

                if (matriz[i][j].getTipo() != 0) {
                    getImageView(i, j).setOnDragListener(null);
                }

                if (countVezNavio == 2) {

                    //deixar o ultimo quadrado sempre desativado
                    getImageView(TAM_MATRIZ - 1, TAM_MATRIZ - 1).setOnDragListener(null);

                    if (ivNavioVertical2.getVisibility() == View.VISIBLE) {
                        // se forem todos os quadrados da ultima linha, menos o ultimo, desativa.
                        if (i == TAM_MATRIZ - 1 && j != TAM_MATRIZ - 1) {
                            getImageView(i, j).setOnDragListener(null);
                        }
                        // ativar da ultima coluna caso seja pressionado o botão de girar
                        if (j == TAM_MATRIZ - 1 && i != TAM_MATRIZ - 1 && matriz[i][j].getTipo() == 0) {
                            getImageView(i, j).setOnDragListener(new MyOnDragListener(i, j));
                        }

                    }

                    if (ivNavioHorizontal2.getVisibility() == View.VISIBLE) {

                        if (j == TAM_MATRIZ - 1 && i != TAM_MATRIZ - 1) {
                            getImageView(i, j).setOnDragListener(null);
                        }

                        if (i == TAM_MATRIZ - 1 && j != TAM_MATRIZ - 1 && matriz[i][j].getTipo() == 0) {
                            getImageView(i, j).setOnDragListener(new MyOnDragListener(i, j));
                        }

                    }

                } else if (countVezNavio == 3) {

                    getImageView(TAM_MATRIZ - 2, TAM_MATRIZ - 2).setOnDragListener(null);
                    getImageView(TAM_MATRIZ - 2, TAM_MATRIZ - 1).setOnDragListener(null);
                    getImageView(TAM_MATRIZ - 1, TAM_MATRIZ - 2).setOnDragListener(null);
                    getImageView(TAM_MATRIZ - 1, TAM_MATRIZ - 1).setOnDragListener(null);

                    if (ivNavioVertical3.getVisibility() == View.VISIBLE) {

                        if ((i == TAM_MATRIZ - 1 || i == TAM_MATRIZ - 2) && (j != TAM_MATRIZ - 1 && j != TAM_MATRIZ - 2)) {
                            getImageView(i, j).setOnDragListener(null);
                        }

                        if ((j == TAM_MATRIZ - 1 || j == TAM_MATRIZ - 2) && (i != TAM_MATRIZ - 1 && i != TAM_MATRIZ - 2) && matriz[i][j].getTipo() == 0) {
                            getImageView(i, j).setOnDragListener(new MyOnDragListener(i, j));
                        }
                    }

                    if (ivNavioHorizontal3.getVisibility() == View.VISIBLE) {

                        if ((j == TAM_MATRIZ - 1 || j == TAM_MATRIZ - 2) && (i != TAM_MATRIZ - 1 && i != TAM_MATRIZ - 2)) {
                            getImageView(i, j).setOnDragListener(null);
                        }

                        if ((i == TAM_MATRIZ - 1 || i == TAM_MATRIZ - 2) && (j != TAM_MATRIZ - 1 && j != TAM_MATRIZ - 2) && matriz[i][j].getTipo() == 0) {
                            getImageView(i, j).setOnDragListener(new MyOnDragListener(i, j));
                        }
                    }
                }

            }
        }

    }

    private void runAlertConectar() {
        // Copiando dodos da matriz para lista parcelable ---------

        nodoBarcosList = new ArrayList<>(); //lista com todos os objetos da matriz do jogador(Eu)

        for (int i = 0; i < TAM_MATRIZ; i++) {
            for (int j = 0; j < TAM_MATRIZ; j++) {
                if (matriz[i][j].getTipo() == -1) {
                    matriz[i][j].setTipo(0); // eliminar os -1.
                }
                nodoBarcosList.add(matriz[i][j]);
            }
        }

        // --------------------------------------------------------

        View viewAlert = getLayoutInflater().inflate(R.layout.layout_alert_conectar, null);
        Button btnIniciarSevidor = viewAlert.findViewById(R.id.btn_iniciar_servidor);
        Button btnConectarAUmSevidor = viewAlert.findViewById(R.id.btn_conectar_a_um_servidor);

        btnIniciarSevidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeuTabuleiroActivity.this, "Iniciar servidor", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MeuTabuleiroActivity.this, BluetoothChatServerActivity.class);
                intent.putParcelableArrayListExtra("minha_matriz", nodoBarcosList);
                intent.putExtra("activity", "servidor");

                startActivity(intent);
                finish();
            }
        });

        btnConectarAUmSevidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeuTabuleiroActivity.this, "Conectar a um servidor", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MeuTabuleiroActivity.this, ListaDevicesActivity.class);
                intent.putParcelableArrayListExtra("minha_matriz", nodoBarcosList);
                intent.putExtra("activity", "cliente");

                startActivity(intent);
                finish();
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(MeuTabuleiroActivity.this);
        alert.setView(viewAlert);
        alert.show();
    }

    private class MyOnDragListener implements View.OnDragListener {

        int linha, coluna;

        public MyOnDragListener(int linha, int coluna) {
            super();
            this.linha = linha;
            this.coluna = coluna;
        }

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            int action = dragEvent.getAction();

            View v = (View) dragEvent.getLocalState();


            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // PARA PASSAGEM TEMPORÁRIA DA IMAGEM POR CIMA DOS QUADRADOS
                    Log.i("Teste", "ACTION_DRAG_ENTERED");

                    view.setBackgroundColor(ContextCompat.getColor(MeuTabuleiroActivity.this, R.color.corInsercaoPossivel));

                    Log.i("Joiner", "Count = " + countVezNavio);

                    if (countVezNavio == 2) {

                        if (horientacao == VERTICAL) {
                            if (linha != TAM_MATRIZ - 1) {
                                getImageView(linha + 1, coluna).setBackgroundColor(ContextCompat.getColor(MeuTabuleiroActivity.this, R.color.corInsercaoPossivel));
                            }
                        } else {
                            if (coluna != TAM_MATRIZ - 1) {
                                getImageView(linha, coluna + 1).setBackgroundColor(ContextCompat.getColor(MeuTabuleiroActivity.this, R.color.corInsercaoPossivel));
                            }
                        }

                    } else if (countVezNavio == 3) {

                        if (horientacao == VERTICAL) {
                            if (linha < TAM_MATRIZ - 2) {
                                getImageView(linha + 1, coluna).setBackgroundColor(ContextCompat.getColor(MeuTabuleiroActivity.this, R.color.corInsercaoPossivel));
                                getImageView(linha + 2, coluna).setBackgroundColor(ContextCompat.getColor(MeuTabuleiroActivity.this, R.color.corInsercaoPossivel));
                            }
                        } else {
                            if (coluna < TAM_MATRIZ - 2) {
                                getImageView(linha, coluna + 1).setBackgroundColor(ContextCompat.getColor(MeuTabuleiroActivity.this, R.color.corInsercaoPossivel));
                                getImageView(linha, coluna + 2).setBackgroundColor(ContextCompat.getColor(MeuTabuleiroActivity.this, R.color.corInsercaoPossivel));
                            }
                        }
                    }

                    break;
                //case DragEvent.ACTION_DRAG_LOCATION:
                case DragEvent.ACTION_DRAG_EXITED:
                    // AÇÃO DE SAIDA DA IMAGEM DO QUADRADINHO
                    // deixa transparente, pois o toque foi para outro quadrado.
                    // ..... colocar tratamento para saber se o barco é horizontal ou vertical ...

                    Log.i("Teste", "ACTION_DRAG_EXITED");

                    view.setBackgroundColor(Color.TRANSPARENT); //primeiro quadradinho selecionado. (barco 1, ou primeira parte dos demais barcos)

                    if (countVezNavio == 2) {

                        if (horientacao == VERTICAL) {
                            if (linha != TAM_MATRIZ - 1) {
                                getImageView(linha + 1, coluna).setBackgroundColor(Color.TRANSPARENT);
                            }
                        } else {
                            if (coluna != TAM_MATRIZ - 1) {
                                getImageView(linha, coluna + 1).setBackgroundColor(Color.TRANSPARENT);
                            }
                        }

                    } else if (countVezNavio == 3) {

                        if (horientacao == VERTICAL) {
                            if (linha < TAM_MATRIZ - 2) {
                                getImageView(linha + 1, coluna).setBackgroundColor(Color.TRANSPARENT);
                                getImageView(linha + 2, coluna).setBackgroundColor(Color.TRANSPARENT);
                            }
                        } else {
                            if (coluna < TAM_MATRIZ - 2) {
                                getImageView(linha, coluna + 1).setBackgroundColor(Color.TRANSPARENT);
                                getImageView(linha, coluna + 2).setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                    }

                    break;

                case DragEvent.ACTION_DROP:
                    // QUANDO SOLTA A IMAGEM DENTRO DE UM QUADRADINHO

                    Log.i("Teste", "ACTION_DROP");

                    //v = (View) dragEvent.getLocalState();

                    ViewGroup owner = (ViewGroup) v.getParent();
                    owner.removeView(v);


                    switch (v.getId()) {

                        case R.id.iv_navio_vertical_1:
                            // seta no layout.
                            ((ImageView) view).setImageResource(R.mipmap.navio_vertical_1_parte_1);
                            view.setBackgroundColor(Color.TRANSPARENT);
                            // seta na estrutura de dados (matriz).
                            setarBarcoNaMatriz(linha, coluna, 1, VERTICAL, 1);
                            // tornar o próximo barco visível
                            ivNavioVertical2.setVisibility(View.VISIBLE);

                            //view.setOnDragListener(null); // "desabilitar"

                            countVezNavio++;

                            break;

                        case R.id.iv_navio_horizontal_1:
                            ((ImageView) view).setImageResource(R.mipmap.navio_horizontal_1_parte_1);
                            view.setBackgroundColor(Color.TRANSPARENT);

                            setarBarcoNaMatriz(linha, coluna, 1, HORIZONTAL, 1);

                            ivNavioVertical2.setVisibility(View.VISIBLE);

                            countVezNavio++;

                            break;

                        case R.id.iv_navio_vertical_2:

                            if (matriz[linha][coluna].getTipo() == 0 && linha != TAM_MATRIZ - 1 && matriz[linha + 1][coluna].getTipo() != -1) {

                                ((ImageView) view).setImageResource(R.mipmap.navio_vertical_2_parte_1);
                                getImageView(linha + 1, coluna).setImageResource(R.mipmap.navio_vertical_2_parte_2);

                                view.setBackgroundColor(Color.TRANSPARENT);
                                getImageView(linha + 1, coluna).setBackgroundColor(Color.TRANSPARENT);

                                setarBarcoNaMatriz(linha, coluna, 2, VERTICAL, 1);
                                setarBarcoNaMatriz(linha + 1, coluna, 2, VERTICAL, 2);

                                ivNavioVertical3.setVisibility(View.VISIBLE);


                                countVezNavio++;


                            } else {
                                Toast.makeText(MeuTabuleiroActivity.this, "Impossível colocar nessa posição! :/", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                            }
                            break;

                        case R.id.iv_navio_horizontal_2:

                            if (matriz[linha][coluna].getTipo() == 0 && coluna != TAM_MATRIZ - 1 && matriz[linha][coluna + 1].getTipo() != -1) {

                                ((ImageView) view).setImageResource(R.mipmap.navio_horizontal_2_parte_1);
                                getImageView(linha, coluna + 1).setImageResource(R.mipmap.navio_horizontal_2_parte_2);

                                view.setBackgroundColor(Color.TRANSPARENT);
                                getImageView(linha, coluna + 1).setBackgroundColor(Color.TRANSPARENT);

                                setarBarcoNaMatriz(linha, coluna, 2, HORIZONTAL, 1);
                                setarBarcoNaMatriz(linha, coluna + 1, 2, HORIZONTAL, 2);

                                ivNavioVertical3.setVisibility(View.VISIBLE);

                                // serve para desenhar corretamente a sombra do próximo barco(3).
                                horientacao = VERTICAL;

                                //v.setVisibility(View.VISIBLE);
                                countVezNavio++;

                            } else {
                                Toast.makeText(MeuTabuleiroActivity.this, "Impossível colocar nessa posição! :/", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                            }
                            break;

                        case R.id.iv_navio_vertical_3:

                            if (matriz[linha][coluna].getTipo() == 0 && linha < TAM_MATRIZ - 2 && matriz[linha + 2][coluna].getTipo() != -1) {

                                ((ImageView) view).setImageResource(R.mipmap.navio_vertical_3_parte_1);
                                getImageView(linha + 1, coluna).setImageResource(R.mipmap.navio_vertical_3_parte_2);
                                getImageView(linha + 2, coluna).setImageResource(R.mipmap.navio_vertical_3_parte_3);

                                view.setBackgroundColor(Color.TRANSPARENT);
                                getImageView(linha + 1, coluna).setBackgroundColor(Color.TRANSPARENT);
                                getImageView(linha + 2, coluna).setBackgroundColor(Color.TRANSPARENT);

                                setarBarcoNaMatriz(linha, coluna, 3, VERTICAL, 1);
                                setarBarcoNaMatriz(linha + 1, coluna, 3, VERTICAL, 2);
                                setarBarcoNaMatriz(linha + 2, coluna, 3, VERTICAL, 3);

                                //v.setVisibility(View.VISIBLE);
                                countVezNavio++;

                                runAlertConectar(); // abre a opção para conectar a outro jogador
                                ivBtnConectar.setVisibility(View.VISIBLE);

                            } else {
                                Toast.makeText(MeuTabuleiroActivity.this, "Impossível colocar nessa posição! :/", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case R.id.iv_navio_horizontal_3:

                            if (matriz[linha][coluna].getTipo() == 0 && coluna < TAM_MATRIZ - 2 && matriz[linha][coluna + 2].getTipo() != -1) {

                                ((ImageView) view).setImageResource(R.mipmap.navio_horizontal_3_parte_1);
                                getImageView(linha, coluna + 1).setImageResource(R.mipmap.navio_horizontal_3_parte_2);
                                getImageView(linha, coluna + 2).setImageResource(R.mipmap.navio_horizontal_3_parte_3);

                                view.setBackgroundColor(Color.TRANSPARENT);
                                getImageView(linha, coluna + 1).setBackgroundColor(Color.TRANSPARENT);
                                getImageView(linha, coluna + 2).setBackgroundColor(Color.TRANSPARENT);


                                setarBarcoNaMatriz(linha, coluna, 3, HORIZONTAL, 1);
                                setarBarcoNaMatriz(linha, coluna + 1, 3, HORIZONTAL, 2);
                                setarBarcoNaMatriz(linha, coluna + 2, 3, HORIZONTAL, 3);

                                //v.setVisibility(View.VISIBLE);
                                countVezNavio++;

                                runAlertConectar(); // abre a opção para conectar a outro jogador
                                ivBtnConectar.setVisibility(View.VISIBLE);

                            } else {
                                Toast.makeText(MeuTabuleiroActivity.this, "Impossível colocar nessa posição! :/", Toast.LENGTH_SHORT).show();
                            }

                            break;
                    }


                    // -----------------------------
                    for (int i = 0; i < TAM_MATRIZ; i++) {
                        for (int j = 0; j < TAM_MATRIZ; j++) {
                            System.out.print(matriz[i][j].getTipo() + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("--------------------------");


                    removeDragListener();
                    break;

                case DragEvent.ACTION_DRAG_ENDED:

                    Log.i("Teste", "ACTION_DRAG_ENDED");
                    v.setVisibility(View.VISIBLE);
                    //view.setBackgroundColor(Color.TRANSPARENT);

                    break;
            }

            return true;
        }
    }


    private void setarBarcoNaMatriz(int linha, int coluna, int tipoDoBarco, int horientacao, int parte) {

        int inicioLinha, fimLinha, inicioColuna, fimColuna;

        matriz[linha][coluna].setTipo(tipoDoBarco); // seta o 1, que é o barco 1
        matriz[linha][coluna].setOrientacao(horientacao);
        matriz[linha][coluna].setParte(parte);

        if (linha != 0 && linha != TAM_MATRIZ - 1 && coluna != 0 && coluna != TAM_MATRIZ - 1) {
            inicioLinha = linha - 1;
            inicioColuna = coluna - 1;
            fimLinha = inicioLinha + 3;
            fimColuna = inicioColuna + 3;
        } else if (linha == 0 && coluna == 0) {
            inicioLinha = linha;
            inicioColuna = coluna;
            fimLinha = inicioLinha + 2;
            fimColuna = inicioColuna + 2;
        } else if (linha == 0 && coluna == TAM_MATRIZ - 1) {
            inicioLinha = linha;
            inicioColuna = coluna - 1;
            fimLinha = inicioLinha + 2;
            fimColuna = inicioColuna + 2;
        } else if (linha == TAM_MATRIZ - 1 && coluna == 0) {
            inicioLinha = linha - 1;
            inicioColuna = coluna;
            fimLinha = inicioLinha + 2;
            fimColuna = inicioColuna + 2;
        } else if (linha == TAM_MATRIZ - 1 && coluna == TAM_MATRIZ - 1) {
            inicioLinha = linha - 1;
            inicioColuna = coluna - 1;
            fimLinha = inicioLinha + 2;
            fimColuna = inicioColuna + 2;
        } else if (linha == 0 && coluna != 0 && coluna != TAM_MATRIZ - 1) {
            inicioLinha = linha;
            inicioColuna = coluna - 1;
            fimLinha = inicioLinha + 2;
            fimColuna = inicioColuna + 3;
        } else if (linha == TAM_MATRIZ - 1 && coluna != 0 && coluna != TAM_MATRIZ - 1) {
            inicioLinha = linha - 1;
            inicioColuna = coluna - 1;
            fimLinha = inicioLinha + 2;
            fimColuna = inicioColuna + 3;
        } else if (coluna == 0 && linha != 0 && linha != TAM_MATRIZ - 1) {
            inicioLinha = linha - 1;
            inicioColuna = coluna;
            fimLinha = inicioLinha + 3;
            fimColuna = inicioColuna + 2;
        } else {// if (coluna == TAM_MATRIZ - 1 && linha != 0 && linha != TAM_MATRIZ - 1) {
            inicioLinha = linha - 1;
            inicioColuna = coluna - 1;
            fimLinha = inicioLinha + 3;
            fimColuna = inicioColuna + 2;
        }

        // setar nas redondezas com -1.
        for (int i = inicioLinha; i < fimLinha; i++) {
            for (int j = inicioColuna; j < fimColuna; j++) {
                if (matriz[i][j].getTipo() == 0) {
                    matriz[i][j].setTipo(-1);
                }
            }
        }

    }
}
