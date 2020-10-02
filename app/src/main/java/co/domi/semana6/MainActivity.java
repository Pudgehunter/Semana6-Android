package co.domi.semana6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import co.domi.semana6.model.User;

public class MainActivity extends AppCompatActivity {

    EditText usuarioId, passwordId;
    Button ingresar;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean onActive;
    private String line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioId = findViewById(R.id.usuarioId);
        passwordId = findViewById(R.id.passwordId);
        ingresar = findViewById(R.id.Ingresar);

        onActive = true;

        initClient();

        ingresar.setOnClickListener(
                (v) -> {
                    Gson gson = new Gson();
                    String user = usuarioId.getText().toString();
                    String password = passwordId.getText().toString();
                    String description = "De celular se envia datos del usuario que es el nombre y el pasaporte";

                    User obj = new User(user,password,description);
                    String json = gson.toJson(obj);
                    sendUser(json);
                }
        );

    }

    public void initClient() {
        new Thread(
                () -> {
                    try {
                        //2. Servidor intentando conectar
                        socket = new Socket("192.168.1.2",5000);

                        //3. Conectados
                        System.out.println("Cliente conectado");

                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        reader = new BufferedReader(isr);

                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

                        while(onActive) {
                            System.out.println("Esperando...");
                            line = reader.readLine();
                            System.out.println("Recibido");
                            //Validación de si tienen el mismo usuario o contraseña.
                            if(line != null) {
                                runOnUiThread(() -> {
                                    if (line.equals("cambio de pantalla")) {
                                        Intent a = new Intent(this, Bienvenido.class);
                                        startActivity(a);
                                    }
                                    if (line.equals("Fail")) {
                                        Toast.makeText(this, "No existe", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }  catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    public void sendUser(String msg) {
        new Thread(
                () -> {
                    try {
                        writer.write(msg+"\n");
                        writer.flush();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        ).start();
    }

}