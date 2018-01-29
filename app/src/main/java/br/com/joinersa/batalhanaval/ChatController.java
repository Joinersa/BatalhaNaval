package br.com.joinersa.batalhanaval;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.joinersa.batalhanaval.entity.NodoBarco;

/**
 * Created by joine on 10/09/2017.
 */

public class ChatController {
    public static final String TAG = "chat";
    private BluetoothSocket socket;
    private InputStream in;
    private OutputStream out;
    private ChatListener listener;
    private boolean running;

    public interface ChatListener {
        void onMessageReceived(String msg);
    }

    public ChatController(BluetoothSocket socket, ChatListener listener) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.listener = listener;
        this.running = true;
    }

    // Inicia a leitura da InputStream
    public void start() {
        new Thread() {
            @Override
            public void run() {
                running = true;
                // faz a leitura
                byte[] bytes = new byte[1024];
                int length;
                //fica em loop para receber as mensagens
                while (running) {
                    try {
                        Log.d(TAG, "Aguardando mensagens");
                        //Lê a mensagem (fica bloqueado até receber)
                        length = in.read(bytes);
                        String msg = new String(bytes, 0, length);
                        Log.d(TAG, "Mensagem: " + msg);

                        // Recebe a mensagem (informa o listener)
                        listener.onMessageReceived(msg);


                    } catch (Exception e) {
                        running = false;
                        Log.e(TAG, "Error: " + e.getMessage(), e);
                    }
                }
            }
        }.start();
    }


    public void sendMessage(String msg) throws IOException {
        if (out != null) {
            out.write(msg.getBytes());
        }
    }


    public void stop() {
        running = false;
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {

        }
    }

}
