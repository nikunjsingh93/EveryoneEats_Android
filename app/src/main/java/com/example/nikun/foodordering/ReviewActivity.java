package com.example.nikun.foodordering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewActivity extends Activity {
    String s;
    String ring;

    private TextView tvB = null;
    private TextView tvC = null;
    private TextView tvF = null;
    private TextView tvO = null;
    private TextView tvBN = null;
    private TextView tvCN = null;
    private TextView tvFN = null;
    private TextView tvON = null;
    private TextView tvCus = null;
    private TextView tvName = null;
    private TextView tvTotal = null;
    private TextView tvStatus = null;
    private TextView tvTax = null;

    private String s1 = null;
    private String s2 = null;
    private String s3 = null;
    private String s4 = null;
    private String s5 = null;
    private String s6 = null;
    private String s7 = null;
    private String s8 = null;
    private double sum;

    private Button button = null;
    private Button buttonCancel = null;

    private Date date;
    private String msg = null;
    private Socket myClientSocket;

    private String response="";
    private Order order = new Order();
    public MsgDecoding MD=new MsgDecoding();

    SocketClientRecvMsg4Thread socketClientRecvMsg4Thread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_main);
        Intent intent = getIntent();
        s1 = intent.getStringExtra("Burgers");
        s2 = intent.getStringExtra("Chickens");
        s3 = intent.getStringExtra("FrenchFries");
        s4 = intent.getStringExtra("OnionRings");
        s5 = intent.getStringExtra("CustomerID");
        s6 = intent.getStringExtra("CustomerName");
        s7 = intent.getStringExtra("orderDate");
        s8 = intent.getStringExtra("orderID");
        SimpleDateFormat format=new SimpleDateFormat("MM/DD/yyyy");
        order.setCustomerID(s5);
        tvB = (TextView) findViewById(R.id.lblBurger);
        tvB.setText("Burgers:");
        tvBN = (TextView) findViewById(R.id.lblBurgerNum);
        tvC = (TextView) findViewById(R.id.lblChicken);
        tvC.setText("Chickens:");
        tvCN = (TextView) findViewById(R.id.lblChickenNum);
        tvF = (TextView) findViewById(R.id.lblFries);
        tvF.setText("French Fries:");
        tvFN = (TextView) findViewById(R.id.lblFriesNum);
        tvO = (TextView) findViewById(R.id.lblOnion);
        tvO.setText("Onion Rings: ");
        tvON = (TextView) findViewById(R.id.lblOnionNum);
        tvCus = (TextView) findViewById(R.id.lblIDNum);
        tvCus.setText(s5);

        order.setStatus("Submitted");
        tvStatus = (TextView) findViewById(R.id.lblStatus);
        tvStatus.setText(order.getStatus());

        order.setOrderID(s8);

        tvCus = (TextView) findViewById(R.id.lblIDNum);
        tvCus.setText(s5);

        tvTotal = (TextView) findViewById(R.id.lblTotalNum);
        tvTax = (TextView) findViewById(R.id.lblTaxNum);


        tvName = (TextView) findViewById(R.id.lblName);
        tvName.setText(s6);

        socketClientRecvMsg4Thread
                =new SocketClientRecvMsg4Thread();
        socketClientRecvMsg4Thread.start();

        addListenerOnButton();
        addListenerOnButton2();
        addListenerOnButton3();

    }

    public void populateText(){



        if(order.getOrderAvailable().equals("Partially Available")){
            if(order.getOrderItems().getItemByName("burger")!=null) {
                if (s1.equals(String.valueOf(order.getOrderItems().getItemByName("burger").getQuantiy()))) {
                    tvBN.setText(s1);
                } else {
                    tvBN.setText(order.getOrderItems().getItemByName("burger").getQuantiy()
                            + "/" + s1 + " PA");
                }
            }
            if(order.getOrderItems().getItemByName("chicken")!=null) {
                if (s2.equals(String.valueOf(order.getOrderItems().getItemByName("chicken").getQuantiy()))){
                    tvCN.setText(s2);
                }else{
                    tvCN.setText(order.getOrderItems().getItemByName("chicken").getQuantiy()
                            + "/" + s2 + " PA");
                }
            }

            if(order.getOrderItems().getItemByName("frenchfries")!=null) {
                if (s3.equals(String.valueOf(order.getOrderItems().getItemByName("frenchfries").getQuantiy()))) {
                    tvFN.setText(s3);
                } else {
                    tvFN.setText(order.getOrderItems().getItemByName("frenchfries").getQuantiy()
                            + "/" + s3 + " PA");
                }
            }

            if(order.getOrderItems().getItemByName("onionring")!=null) {
                if (s4.equals(String.valueOf(order.getOrderItems().getItemByName("onionring").getQuantiy()))) {
                    tvON.setText(s4);
                } else {
                    tvON.setText(order.getOrderItems().getItemByName("onionring").getQuantiy()
                            + "/" + s4 + " PA");
                }
            }
        }else{
            tvBN.setText(s1);
            tvCN.setText(s2);
            tvFN.setText(s3);
            tvON.setText(s4);
        }



    }

    public void addListenerOnButton(){
        button = (Button) findViewById(R.id.btnCheck);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // send Msg5 -- yes
                order.setStatus("Receving");
                tvStatus.setText(order.getStatus());
                SocketClientSendMsg5Thread socketClientSendMsg5Thread
                        =new SocketClientSendMsg5Thread("T");
                socketClientSendMsg5Thread.start();


                button.setClickable(false);
                buttonCancel.setClickable(false);

                AlertDialog.Builder adb = new AlertDialog.Builder(ReviewActivity.this);
                adb.setTitle("Notice");
                adb.setMessage("Checkout Successfully!");
                final AlertDialog ad = adb.create();
                ad.show();

                if(order.getOrderItems().getItemByName("burger") != null)
                    tvBN.setText(String.valueOf(order.getOrderItems().getItemByName("burger").getQuantiy()));
                if(order.getOrderItems().getItemByName("chicken") != null)
                    tvCN.setText(String.valueOf(order.getOrderItems().getItemByName("chicken").getQuantiy()));
                if(order.getOrderItems().getItemByName("frenchfries") != null)
                    tvFN.setText(String.valueOf(order.getOrderItems().getItemByName("frenchfries").getQuantiy()));
                if(order.getOrderItems().getItemByName("onionring") != null)
                    tvON.setText(String.valueOf(order.getOrderItems().getItemByName("onionring").getQuantiy()));
            }
        });

    }

    public void addListenerOnButton2(){
        buttonCancel = (Button) findViewById(R.id.btnCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // send Msg5 -- no
                order = new Order();

                SocketClientSendMsg5Thread socketClientSendMsg5Thread
                        =new SocketClientSendMsg5Thread("F");
                socketClientSendMsg5Thread.start();


                button.setClickable(false);
                buttonCancel.setClickable(false);

                Intent intent = new Intent(ReviewActivity.this, MenuActivity.class);
                intent.putExtra("ID",s5);
                intent.putExtra("Name",s6);
                intent.putExtra("Yes","0");
                startActivity(intent);
            }
        });

    }

    public void addListenerOnButton3(){
        Button button = (Button) findViewById(R.id.btnLogout);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(ReviewActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private class SocketClientRecvMsg4Thread  extends Thread{

        private String Msg4;

        SocketClientRecvMsg4Thread(){
            Msg4="";
            try{
                myClientSocket=MyClientSocket.getClientSocket();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        public void run() {
            try{
                InputStream inputStream = myClientSocket.getInputStream();
                InputStreamReader isr=new InputStreamReader(inputStream);
                BufferedReader br=new BufferedReader(isr);
                Msg4=br.readLine();
                order=MD.DecodingMsg4(Msg4,order);

                response+=order.getOrderAvailable()+"\n";


                int burger;
                int chicken;
                int fries;
                int onion;

                DecimalFormat df = new DecimalFormat("0.00");

                if(order.getOrderAvailable().equals("Partially Available")){
                    if(order.getOrderItems().getItemByName("burger") != null)
                        burger = order.getOrderItems().getItemByName("burger").getQuantiy();
                    else burger = 0;
                    if(order.getOrderItems().getItemByName("chicken") != null)
                        chicken = order.getOrderItems().getItemByName("chicken").getQuantiy();
                    else chicken = 0;
                    if(order.getOrderItems().getItemByName("frenchfries") != null)
                        fries = order.getOrderItems().getItemByName("frenchfries").getQuantiy();
                    else fries = 0;
                    if(order.getOrderItems().getItemByName("onionring") != null)
                        onion = order.getOrderItems().getItemByName("onionring").getQuantiy();
                    else onion = 0;
                }
                else{
                    burger = Integer.parseInt(s1);
                    chicken = Integer.parseInt(s2);
                    fries = Integer.parseInt(s3);
                    onion = Integer.parseInt(s4);
                }

                sum = (burger * 3.5) + (chicken * 4.0) + (fries * 1.5) + (onion * 2.0);

                s = df.format(sum);



                double tax;
                tax = 0.06 * sum;


                ring = df.format(tax);



                ReviewActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        populateText();
                        tvTotal.setText(s);
                        tvTax.setText(ring);
                    }
                });

            }catch(UnknownHostException e){
                e.printStackTrace();
                response+="UnkonwnHostException: "+e.toString();
            }catch(IOException e){
                e.printStackTrace();
                response+="IOException: "+e.toString();
            }
        }
    }

    private class SocketClientSendMsg5Thread extends Thread{
        private String Msg5="";

        SocketClientSendMsg5Thread(String feedback){
            Msg5+=feedback+"\n";
        }

        @Override
        public void run(){
            try{
                OutputStream outputStream=myClientSocket.getOutputStream();
                outputStream.write(Msg5.getBytes("UTF-8"));
                outputStream.flush();

                response+= "\nreply:"+Msg5+"\n";


                if(Msg5.equals("T\n")){
                    SocketClientRecvMsg6Thread socketClientRecvMsg6Thread
                            =new SocketClientRecvMsg6Thread();
                    socketClientRecvMsg6Thread.start();
                }
            }catch(UnknownHostException e){
                e.printStackTrace();
                response+="UnkonwnHostException: "+e.toString();
            }catch(IOException e){
                e.printStackTrace();
                response+="IOException: "+e.toString();
            }
        }
    }

    private class SocketClientRecvMsg6Thread extends Thread{
        private String Msg6="";

        SocketClientRecvMsg6Thread(){;}

        @Override
        public void run(){
            try{
                InputStream inputStream = myClientSocket.getInputStream();
                InputStreamReader isr=new InputStreamReader(inputStream);
                BufferedReader br=new BufferedReader(isr);
                Msg6=br.readLine();
//                response+="\nMsg6 "+Msg6;
                ReviewActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //button.setText(Msg6);
                        order.setStatus("Packaging");
                        tvStatus.setText(order.getStatus());
                        AlertDialog.Builder adb = new AlertDialog.Builder(ReviewActivity.this);
                        adb.setTitle("Notice");
                        adb.setMessage("The Food is Packaging.");
                        final AlertDialog ad = adb.create();
                        ad.show();
                    }
                });

                SocketClientRecvMsg7Thread socketClientRecvMsg7Thread
                        =new SocketClientRecvMsg7Thread();
                socketClientRecvMsg7Thread.start();
            }catch(UnknownHostException e){
                e.printStackTrace();
                response+="UnkonwnHostException: "+e.toString();
            }catch(IOException e){
                e.printStackTrace();
                response+="IOException: "+e.toString();
            }
        }
    }

    private class SocketClientRecvMsg7Thread extends Thread{
        private String Msg7="";

        SocketClientRecvMsg7Thread(){;}

        @Override
        public void run(){
            try{
                InputStream inputStream = myClientSocket.getInputStream();
                InputStreamReader isr=new InputStreamReader(inputStream);
                BufferedReader br=new BufferedReader(isr);
                Msg7=br.readLine();
//                response+="\nMsg7 "+Msg7;
                ReviewActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        order.setStatus("Ready");
                        tvStatus.setText(order.getStatus());
                        AlertDialog.Builder adb = new AlertDialog.Builder(ReviewActivity.this);
                        adb.setTitle("Notice");
                        adb.setMessage("The Food is Ready to Pick Up.");
                        final AlertDialog ad = adb.create();
                        ad.show();
                    }
                });
            }catch(UnknownHostException e){
                e.printStackTrace();
                response+="UnkonwnHostException: "+e.toString();
            }catch(IOException e){
                e.printStackTrace();
                response+="IOException: "+e.toString();
            }
        }
    }


}
