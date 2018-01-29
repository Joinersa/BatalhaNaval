package br.com.joinersa.batalhanaval.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.joinersa.batalhanaval.R;
import br.com.joinersa.batalhanaval.entity.NodoBarco;

public class ListaDevicesActivity extends BluetoothCheckActivity implements AdapterView.OnItemClickListener {

    private ProgressDialog dialog;
    protected List<BluetoothDevice> lista;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_devices);

        listView = (ListView) findViewById(R.id.list_view);
        //iniciar lista com devices pareados
        lista = new ArrayList<>(btfAdapter.getBondedDevices());
        // registra o receiver para receber as mensagens de dispositivos pareados
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        // registra o receiver para receber a mensagem do final da busca
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (btfAdapter.isDiscovering()) {
            btfAdapter.cancelDiscovery();
        }
        // Dispara a busca
        btfAdapter.startDiscovery();
        dialog = ProgressDialog.show(ListaDevicesActivity.this, "Aguarde...", "Buscando dispositivos adversários...", false, true);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        private int count;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Se um device foi encontrado
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Recupera o device da intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Apenas insere na lista os devices que ainda não estão pareados
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    lista.add(device);
                    Toast.makeText(context, "Encontrou: " + device.getName() + ":" + device.getAddress(), Toast.LENGTH_SHORT).show();
                    count++;
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //Iniciou a busca
                count = 0;
                Toast.makeText(context, "Busca iniciada.", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // Terminou a busca
                Toast.makeText(context, "Busca finalizada.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                //Atualiza o listView. Agora vai ter todos os devices pareados, mais os novos que foram encontrados
                updateLista();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Garante que a busca é cancelada ao sair.
        if (btfAdapter != null) {
            btfAdapter.cancelDiscovery();
        }
        // Cancela o registro do receiver
        this.unregisterReceiver(mReceiver);
    }

    private void updateLista() {
        // Cria o array com o nome de cada device
        List<String> nomes = new ArrayList<>();
        for (BluetoothDevice device : lista) {
            //Neste exemplo, esta variável boolean sempre será true, pois esta lista é somente dos pareados
            boolean pareado = device.getBondState() == BluetoothDevice.BOND_BONDED;
            nomes.add(device.getName() + " - " + device.getAddress() + (pareado ? " *pareado" : ""));
        }
        // Cria um adapter para popular o ListView
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout, nomes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int idx, long id) {
        // recupera o device selecionado
        BluetoothDevice device = lista.get(idx);
        // Vai para a tela para enviar a mensagem
        Intent intent = new Intent(ListaDevicesActivity.this, CombateActivity.class);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);

        ArrayList<NodoBarco> nodoBarcosList = getIntent().getParcelableArrayListExtra("minha_matriz");
        intent.putParcelableArrayListExtra("minha_matriz", nodoBarcosList);
        intent.putExtra("activity", "cliente");

        startActivity(intent);
    }

}
