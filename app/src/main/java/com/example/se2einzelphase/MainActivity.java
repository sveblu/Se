package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMATNR(View view) throws IOException, InterruptedException {
        EditText matNR = findViewById(R.id.editTextMATNR);
        TextView tv = findViewById(R.id.answerServer);
        String mtrklNR = String.valueOf(matNR.getEditableText());

        TCPClient tcpClient = new TCPClient(mtrklNR);

        tcpClient.start();
        tcpClient.join();

        String result = tcpClient.getResult();

        tv.setText(result);

    }

}

class TCPClient extends Thread {
    private String matNR;
    private String result;
    private Socket clientSocket;

    TCPClient(String input) {
        this.setMatNR(input);
    }

    public void run() {

        try {
            setClientSocket(new Socket("se2-isys.aau.at", 53212 ));
            DataOutputStream outToServer = new DataOutputStream(getClientSocket().getOutputStream());
            outToServer.writeBytes(getMatNR() + "\n");
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));

            this.setResult(inFromServer.readLine());
            getClientSocket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getResult (){
        return this.result;
    }
    public String getMatNR () {
        return this.matNR;
    }

    public void setMatNR(String matNR) {
        this.matNR = matNR;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
