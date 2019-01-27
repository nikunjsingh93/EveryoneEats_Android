package com.example.nikun.foodordering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {

    public Socket socket;
    public MsgEncoding msgEncoding =new MsgEncoding();
    public MsgDecoding msgDecoding=new MsgDecoding();
    public Customer customer = new Customer();
    public String response="";
    public boolean isSuccess;
    EditText etUserId,etPin;
    final Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserId = (EditText) findViewById(R.id.usernameTextField);
        etPin = (EditText) findViewById(R.id.pinTextField);

        Button bLogin = (Button) findViewById(R.id.signInButton);
        Button bRegistr = (Button) findViewById(R.id.registerButton);





        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                int hr = cal.get((Calendar.HOUR_OF_DAY));
                if (hr < 11 || hr > 21) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                    adb.setTitle("Notice");
                    adb.setMessage("Restaurant is Closed At this time try from 11 AM to 9 PM!");
                    final AlertDialog ad = adb.create();
                    ad.show();
                } else {
                    customer.setCustomerID(etUserId.getText().toString());
                    customer.setPassword(etPin.getText().toString());
                    if (customer.getCustomerID().substring(0, 4).equals("Chef")) {
                        try {
                            InputStream stream = getResources().openRawResource(R.raw.cheflist);
                            InputStreamReader in = new InputStreamReader(stream);
                            BufferedReader br = new BufferedReader(in);
                            String thisLine;
                            while ((thisLine = br.readLine()) != null) {
                                Scanner scanner = new Scanner(thisLine);
                                scanner.useDelimiter(",");
                                if (customer.getCustomerID().equals(scanner.next())
                                        && customer.getPassword().equals(scanner.next())) {
                                    Intent intent = new Intent(LoginActivity.this, ChefActivity.class);
                                    startActivity(intent);
                                    break;
                                } else {
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                                            adb.setTitle("Notice");
                                            adb.setMessage("Invalid Chef ID or Password!");
                                            final AlertDialog ad = adb.create();
                                            ad.show();
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        SocketClientSendMsg1Thread myThread
                                = new SocketClientSendMsg1Thread("10.0.2.2",
                                Integer.parseInt("5000"), msgEncoding.EncodingMsg1("S", customer));
                        myThread.start();
                    }

                }
            }

        });

        bRegistr.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent registrIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registrIntent);
            }
        });

    }
    private class SocketClientSendMsg1Thread extends Thread{
        String dstAddress;
        int dstPort;

        private String Msg1;

        SocketClientSendMsg1Thread(String addr,int port, String msg1){
            dstAddress=addr;
            dstPort=port;
            this.Msg1 = msg1;
        }

        @Override
        public void run(){

            try {
                socket = MyClientSocket.startClientSocket(dstAddress,dstPort);
                OutputStream outputStream;
                outputStream = socket.getOutputStream();
                outputStream.write(Msg1.getBytes("UTF-8"));
                outputStream.flush();
                response += "send message: " + Msg1 + "\n";


                SocketClientRecvMsg2Thread socketClientRecvMsg2Thread
                        =new SocketClientRecvMsg2Thread();
                socketClientRecvMsg2Thread.start();



            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
//                response += "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
//                response += "IOException: " + e.toString();
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                        adb.setTitle("Notice");
                        adb.setMessage("Restaurant is Closed!");
                        final AlertDialog ad = adb.create();
                        ad.show();

                    }
                });
            }
        }
    }
    private class SocketClientRecvMsg2Thread extends Thread{
        private String Msg2;
        private String flag;
        SocketClientRecvMsg2Thread(){
            Msg2="";
        }

        public void run(){
            try{
                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(inputStream);
                BufferedReader br=new BufferedReader(isr);
                Msg2=br.readLine();
//                response+= Msg2+"\n";
                isSuccess=msgDecoding.DecodingMsg2(Msg2);
                customer.setCustomerName(msgDecoding.Msg2Name);

                flag=msgDecoding.flag;
                if(isSuccess&&flag.equals("R")){
                    response+="\n register success,dear "+customer.getCustomerName();
                    //TODO implement the register success

                }
                else if(isSuccess&&flag.equals("S")){
                    customer.setCustomerName(msgDecoding.Msg2Name);
                    response+="\n sign up success,dear "+customer.getCustomerName();
                    //TODO implement the sign up success

                }
                else if(!isSuccess&&flag.equals("R")){
                    response+="\n register failed, dear "+customer.getCustomerName();
                    //TODO implement the register failed;

                }
                else {
                    customer.setCustomerName(msgDecoding.Msg2Name);
                    response+="\n sign up failed, dear"+customer.getCustomerName();
                    //TODO implement the sign up success;

                }


                if(isSuccess){
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    intent.putExtra("ID",customer.getCustomerID());
                    intent.putExtra("Name",customer.getCustomerName());
                    intent.putExtra("Yes","1");
                    startActivity(intent);
                }
                else {
                    //Toast.makeText();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                            adb.setTitle("Notice");
                            adb.setMessage("Invalid UserID or Password!");
                            final AlertDialog ad = adb.create();
                            ad.show();
                        }
                    });

                }

            }catch(Exception e){
                e.printStackTrace();
                response+="UnkonwnHostException: "+e.toString();

            }
        }

    }


}
