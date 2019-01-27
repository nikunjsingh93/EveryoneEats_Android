package com.example.nikun.foodordering;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class MsgDecoding {
    String flag;
    String Msg2Name;
    public MsgDecoding(){
        flag="";
    }



    public Customer DecodingMsg1(String Msg1){
        Customer customer=new Customer();
        Scanner scan = new Scanner(Msg1);
        scan.useDelimiter(",");
        if((flag=scan.next()).equals("R")){
            customer.setCustomerID(scan.next());
            customer.setPassword(scan.next());
            customer.setCustomerName(scan.next());
        }
        else if(flag.equals("S")){
            customer.setCustomerID(scan.next());
            customer.setPassword(scan.next());
        }
        else ;
        return customer;
    }



    public boolean DecodingMsg2(String msg2){
        boolean isSuccess;
        Scanner scan=new Scanner(msg2);
        scan.useDelimiter(",");
        flag=scan.next();
        Msg2Name=scan.next();
        if(scan.next().equals("success"))isSuccess=true;
        else isSuccess=false;
        return isSuccess;
    }



    public Order DecodingMsg3(String Msg3){
        Order order=new Order();
        Scanner scan=new Scanner(Msg3);
        scan.useDelimiter(",");
        SimpleDateFormat format=new SimpleDateFormat("MM/DD/yyyy");
        order.setOrderID(scan.next());
        order.setOrderCost(scan.nextDouble());
        order.setCustomerID(scan.next());
        try{
            order.setOrderTime(format.parse(scan.next()));}catch(Exception e){e.printStackTrace();}
        while(scan.hasNext()){
            OrderItem item=new OrderItem();
            item.setName(scan.next());
            item.setQuantiy(scan.nextInt());
            order.addItem(item);
        }
        return order;
    }



    public Order DecodingMsg4(String msg4,Order order){
        String flag;
        OrderItemList items=new OrderItemList();
        Scanner scan=new Scanner(msg4);
        scan.useDelimiter(",");
        if((flag=scan.next()).equals("AA")){
            order.setOrderAvailable("All Available");
        }else if(flag.equals("NA")){
            order.setOrderAvailable("Not Available");
        }else if(flag.equals("PA")){
            order.setOrderAvailable("Partially Available");
            //TODO need PA msg4 structure.

            while(scan.hasNext()){
                String name=scan.next();
                int Quantity=scan.nextInt();
                OrderItem p=new OrderItem();
                p.setQuantiy(Quantity);
                p.setName(name);
                items.add(p);
            }
            order.setOrderItems(items);
        }
        return order;

    }

    public Order DecodingItemAvailable(String availableItem, Order order){
        OrderItemList itemList=new OrderItemList();
        Scanner scanner=new Scanner(availableItem);
        scanner.useDelimiter(",");
        if((flag=scanner.next()).equals("PA")){
            while(scanner.hasNext()){
                OrderItem item=new OrderItem();
                item.setName(scanner.next());
                item.setQuantiy(scanner.nextInt());
                itemList.add(item);
            }
            order.setOrderItems(itemList);
            order.setOrderAvailable("Partially Available");
        } else if(flag.equals("AA")){
            order.setOrderAvailable("All Available");
        } else if(flag.equals("NA"))
            order.setOrderAvailable("Not Available");
        else ;

        return order;
    }


}
