package com.example.nikun.foodordering;

import java.util.Iterator;

public class MsgEncoding {

    public String EncodingMsg1(String flag,Customer customer){
        String Msg1="";
        if(flag.equals("R")){
            Msg1+=flag+","+customer.getCustomerID()+"," +
                    customer.getPassword()+"," + customer.getCustomerName();
        }
        else if(flag.equals("S")){
            Msg1+=flag+","+customer.getCustomerID()+"," +
                    customer.getPassword();
        }else Msg1="error flag";
        return Msg1+"\n";
    }

    public String EncodingMsg3(Order order){
        String Msg3="";
        Msg3+=order.getOrderID()+","+order.getOrderCost()+"," +
                order.getCustomerID()+"," +(int)(order.getOrderTime().getMonth()+1)+"/"
                +order.getOrderTime().getDate()+"/"+(int)(order.getOrderTime().getYear()+1900);
        Iterator it=order.getOrderItems().iterator();
        while(it.hasNext()){
            OrderItem p=(OrderItem)it.next();
            Msg3+=","+p.getName()+","+p.getQuantiy();
        }
        return Msg3+"\n";
    }
}
