package com.example.nikun.foodordering;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class MenuActivity extends Activity implements AdapterView.OnItemClickListener, ProductsAdapter.Callback{
    public static ArrayList<Products> productsArrayList = new ArrayList<>();
    String strburger = null;
    String strchichen = null;
    String strfries = null;
    String stronion = null;
    String customerID = null;
    String customerName = null;
    Order order=new Order();
    MyClientSocket myClientSocket;
    double sum;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_itemsarray);
        setContentView(R.layout.menuframe);

        Intent it = getIntent();
        String sr = it.getStringExtra("Yes");
        if(sr.equals("1")){
            initProducts();
        }

        ProductsAdapter adapter = new ProductsAdapter(this, productsArrayList, this);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.shopcar);
        TextView shoppingPrice = (TextView)findViewById(R.id.shoppingPrice);
        TextView settlement = (TextView)findViewById(R.id.settlement);
        TextView increase = (TextView)findViewById(R.id.increase);
        TextView welcom = findViewById(R.id.welcom);




        receive();
        addListener();
        addListenerOnButton2();

        welcom.setText("Welcome, dear " +  customerName + "!");


    }

    private void initProducts() {
        productsArrayList = new ArrayList<>();
        Resources res = this.getResources();


        Products products = new Products();
        products.setId(001);products.setNumber(0);
        products.setGoods("Burger");
        products.setPrice(3.50);
        products.setPicture(res.getDrawable(R.drawable.burger));
        productsArrayList.add(products);


        Products products1 = new Products();
        products1.setId(002); products1.setNumber(0);
        products1.setGoods("Chicken");
        products1.setPrice(4.00);
        products1.setPicture(res.getDrawable(R.drawable.chick));
        productsArrayList.add(products1);


        Products products2 = new Products();
        products2.setId(003);products2.setNumber(0);
        products2.setGoods("Fries");
        products2.setPrice(1.50);
        products2.setPicture(res.getDrawable(R.drawable.fry));
        productsArrayList.add(products2);


        Products products3 = new Products();
        products3.setId(004);products3.setNumber(0);
        products3.setGoods("Onion");
        products3.setPrice(2.00);
        products3.setPicture(res.getDrawable(R.drawable.onion));
        productsArrayList.add(products3);




    }

    //
    public void setPrice() {

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.shopcar);
       // TextView shoppingNum;
       // shoppingNum = (TextView)relativeLayout.findViewById(R.id.shopNum);

        TextView shoppingPrice = (TextView)relativeLayout.findViewById(R.id.shoppingPrice);
//        if (shoppingNum == null){
//            Log.d("debug","nonnull");
//        }

        sum = 0;
        int shopNum = 0;

        for (Products products : productsArrayList) {
            sum = sum + (products.getNumber() * products.getPrice());
            shopNum = shopNum + products.getNumber();

        }

//        if(shopNum>0){
//            shoppingNum.setVisibility(View.VISIBLE);
//        }else {
//            shoppingNum.setVisibility(View.GONE);
//        }
        if(sum>0){
            shoppingPrice.setVisibility(View.VISIBLE);
        }else {
            shoppingPrice.setVisibility(View.GONE);
        }
        shoppingPrice.setText("$" + " " + (new DecimalFormat("0.00")).format(sum));
       // shoppingNum.setText(String.valueOf(shopNum));

        String a = (String) shoppingPrice.getText();
        Log.d("debug",a);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        setPrice();
    }

    @Override
    public void click(View v) {
        setPrice();

    }



    public void addListener() {

        TextView settlement = (TextView) findViewById(R.id.settlement);

        settlement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                boolean f = false;
                for (Products products : productsArrayList) {
                    if(products.getNumber() != 0)
                        f = true;
                }

                if(f == true){
                    pass();
                    order.setCustomerID(customerID);
                    order.setOrderTime(new Date());
                    order.setOrderID("12345");
                    order.setOrderCost(sum);
                    int quantity[] = new int[4];
                    quantity[0] = Integer.parseInt(strburger);
                    quantity[1] = Integer.parseInt(strchichen);
                    quantity[2] = Integer.parseInt(strfries);
                    quantity[3] = Integer.parseInt(stronion);
                    if(!strburger.equals("0")){
                        OrderItem oi = new OrderItem("burger",3.5,quantity[0],false,0);
                        order.addItem(oi);
                    }
                    if(!strchichen.equals("0")){
                        OrderItem oi = new OrderItem("chicken",4,quantity[1],false,0);
                        order.addItem(oi);
                    }
                    if(!strfries.equals("0")){
                        OrderItem oi = new OrderItem("frenchfries",1.5,quantity[2],false,0);
                        order.addItem(oi);
                    }
                    if(!stronion.equals("0")){
                        OrderItem oi = new OrderItem("onionring",2.0,quantity[3],false,0);
                        order.addItem(oi);
                    }

                    MsgEncoding mec = new MsgEncoding();
//                order.setOrderCost(Integer.parseInt(tvTotal.getText().toString()));
                    String Msg3 = mec.EncodingMsg3(order);

                    SocketClientSendMsg3Thread socketClientSendMsg3Thread
                            =new SocketClientSendMsg3Thread(Msg3);
                    socketClientSendMsg3Thread.start();
                }
                else{
                    AlertDialog.Builder adb = new AlertDialog.Builder(MenuActivity.this);
                    adb.setTitle("Notice");
                    adb.setMessage("You haven't selected any Items!");
                    final AlertDialog ad = adb.create();
                    ad.show();
                }
            }
        });
    }

    public void pass(){
        int item[] = new int[10];
        int i = 0;
        for (Products products : productsArrayList) {
            item[i]=products.getNumber();
            i++;
        }
        strburger = String.valueOf(item[0]);
        strchichen = String.valueOf(item[1]);
        strfries = String.valueOf(item[2]);
        stronion = String.valueOf(item[3]);
    }

    public void receive(){
        Intent intent = getIntent();
        customerID = intent.getStringExtra("ID");
        customerName = intent.getStringExtra("Name");
    }

    private class SocketClientSendMsg3Thread extends Thread{

        private String Msg3;

        SocketClientSendMsg3Thread(String msg){
            this.Msg3=msg;
            try {
                myClientSocket = MyClientSocket.getClientSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            try{
                OutputStream outputStream = myClientSocket.getOutputStream();
                outputStream.write(Msg3.getBytes("UTF-8"));
                outputStream.flush();


                Intent intent = new Intent(MenuActivity.this, ReviewActivity.class);
                intent.putExtra("Burgers",strburger);
                intent.putExtra("Chickens",strchichen);
                intent.putExtra("FrenchFries",strfries);
                intent.putExtra("OnionRings",stronion);
                intent.putExtra("CustomerID",customerID);
                intent.putExtra("CustomerName",customerName);
                intent.putExtra("orderDate",""+(int)(order.getOrderTime().getMonth()+1)+"/"
                        +order.getOrderTime().getDate()+"/"+(int)(order.getOrderTime().getYear()+1900));
                intent.putExtra("orderID","1234");
                startActivity(intent);
            }catch(UnknownHostException e){
                e.printStackTrace();
                //response+="UnkonwnHostException: "+e.toString();
            }catch(IOException e){
                e.printStackTrace();
                //response+="IOException: "+e.toString();
            }

        }
    }

    public void addListenerOnButton2(){
        Button button = (Button) findViewById(R.id.btnLogout);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}

