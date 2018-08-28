package ec.edu.espol.mdleiton.saludlaboral;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

    ListView listview;
    ArrayList<Integer> listID = new ArrayList<Integer>(100);
    List<Aviso> rowItems= new ArrayList<Aviso>();
    CustomListViewAdapter adapter;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast toast = Toast.makeText(getApplicationContext(),"Item " + (position + 1) + ": " + rowItems.get(position), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            TextView myTextView = (TextView)findViewById(R.id.myTextView);
            myTextView.setText(string);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (listview == null) {
            listview = (ListView) findViewById(R.id.listaEstados);
        }

        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy",Locale.US);
        String dateString = dateformat.format(new Date());
        Aviso item = new Aviso(R.mipmap.ic_launcher_round,"Condicionales normales" ,"sdw",dateString);
        rowItems.add(item);
        adapter = new CustomListViewAdapter(this, R.layout.list_item, rowItems);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

        Runnable runnable = new Runnable() {
                public void run() {
                    while(true){
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                        Message msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy",Locale.US);
                        String dateString = dateformat.format(new Date());
                        bundle.putString("myKey", dateString);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        solicitud();
                    }
                }
            };
            Thread mythread = new Thread(runnable);
            mythread.start();
        }

    public void solicitud(){
        new HttpRequestTask(
                new HttpRequest("http://192.168.0.100:8080/", HttpRequest.GET ),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {

                        if (response.code == 200) {
                            String[] parts = response.body.split("-");
                            if (! listID.contains(Integer.parseInt(parts[0]))){
                                listID.add(Integer.parseInt(parts[0]));
                                if (! parts[1].contains("Sin estado")){
                                    String[] mensaje = parts[1].split("Sugerencias:");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy",Locale.US);
                                    String dateString = dateformat.format(new Date());
                                    String[] sug1 = mensaje[1].split("1.");
                                    String[] sug2 = mensaje[1].split("2.");
                                    sug2 = sug2[1].split("3.");
                                    String[] sug3 = mensaje[1].split("3.");

                                    Aviso item = new Aviso(R.mipmap.ic_launcher_round,mensaje[0] ,"Sugerencias:\n 1. " + sug1[1] + "\n2. " + sug2[0] + "\n3. " + sug3[1] + "\n",dateString);
                                    rowItems.add(item);
                                    adapter.notifyDataSetChanged();

                                Notification builder = new Notification.Builder(getBaseContext())
                                        .setSmallIcon(R.mipmap.ic_launcher_round)
                                        .setContentTitle("Aviso:"+mensaje[0])
                                        .setAutoCancel(true)    //swipe for delete
                                        .setContentText(mensaje[0]) //content
                                        .setStyle(new Notification.BigTextStyle()
                                                .bigText("Sugerencias:\n1. "+ sug1[1] + "\n2. " + sug2[0] + "\n3. " + sug3[1] + "\n"))
                                        .build();
                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.notify(1, builder);
                                }
                            }
                        }
                    }
                }).execute();
    }

}