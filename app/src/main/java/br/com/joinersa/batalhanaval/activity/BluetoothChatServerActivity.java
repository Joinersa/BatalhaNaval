package br.com.joinersa.batalhanaval.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import br.com.joinersa.batalhanaval.BluetoothUtil;
import br.com.joinersa.batalhanaval.ChatController;

public class BluetoothChatServerActivity extends CombateActivity implements ChatController.ChatListener {

    private static final UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private boolean running;
    private BluetoothServerSocket serverSocket;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle icicle) {

        // antes do onCreate da classe pai. :)
        // Pega a lista da activity anterior e passa para activity de combate ----------
        nodoBarcosList = getIntent().getParcelableArrayListExtra("minha_matriz");
        // -----------------------------------------------------------------------------

        super.onCreate(icicle);

        // -------- mod Joiner ----
        dialog = new ProgressDialog(BluetoothChatServerActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Servidor aguardando conexão...");
        dialog.show();
        // ------------------------


        // Deixa o servidor disponível para busca
        if (Build.VERSION.SDK_INT < 21) {
            BluetoothUtil.makeVisible(this, 300);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Inicia thread do chat para não travar a UI
        new ChatThread().start();
    }

    class ChatThread extends Thread {

        @Override
        public void run() {
            try {
                // Abre o socket servidor (o cliente precisa utilizar o mesmo UUID)
                serverSocket = btfAdapter.listenUsingRfcommWithServiceRecord("LivroAndroid", uuid);
                Log.d(TAG, "Servidor aguardando conexão...");

                // Aguardando até alguém conectar (esta chamada é bloqueante)
                BluetoothSocket socket = serverSocket.accept();
                if (socket != null) {
                    // Mostra o device nome nome do device que conectou
                    final BluetoothDevice device = socket.getRemoteDevice();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss(); // mod joiner
                            Toast.makeText(BluetoothChatServerActivity.this, "Conectou: " + device.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    // Alguém conectou
                    chat = new ChatController(socket, BluetoothChatServerActivity.this);
                    chat.start();
                }
            } catch (IOException e) {
                Log.e(TAG, "Erro no servidor: " + e.getMessage(), e);
                running = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
        }
    }
}
