package com.example.nikun.foodordering;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Order {
    private String orderID;     //i changed the datatype to String;
    private double orderCost;
    private String orderAvailable;
    private String feedback;    //the definition of feedback? or use isCancel;
    private String status;
    private String customerID;
    private Date orderTime;
    private boolean payment;
    private OrderItemList orderItems;
    private int cookTime;
    static private int counter = 1;
    private Socket socket;

    /**
     * constructor
     */
    public Order(){
        this(null,null);
    }

    public Order(String orderID,String customerID){
        this.orderID=orderID;
        this.customerID=customerID;
        orderCost=0;
        orderAvailable="";
        feedback=null;
        status="";
        payment=false;
        orderItems=new OrderItemList();
    }
    /**
     * getter: get the value of the variable
     * */
    public String getOrderID(){return orderID;}
    public double getOrderCost(){return orderCost;}
    public String getOrderAvailable(){return orderAvailable;}
    public String getFeedback(){return feedback;}
    public String getStatus(){return status;}
    public String getCustomerID(){return customerID;}
    public Date getOrderTime(){return orderTime;}
    public boolean isPaid(){return payment;}
    public OrderItemList getOrderItems(){return orderItems;}
    public Socket getSocket(){return socket;}
    public int getCookTime(){return cookTime;}

    /**
     * setter: set the value of the variable
     */

    public void setOrderID(String orderID){this.orderID=String.valueOf(counter);
    counter++;}
    public void setOrderCost(double orderCost){this.orderCost=orderCost;}
    public void setOrderAvailable(String orderAvailable){this.orderAvailable=orderAvailable;}
    public void setFeedback(String feedback){this.feedback=feedback;}
    public void setStatus(String status){this.status=status;}
    public void setCustomerID(String customerID){this.customerID=customerID;}
    public void setOrderTime(Date orderTime){this.orderTime=orderTime;}
    public void setPayment(boolean payment){this.payment=payment;}
    public void setOrderItems(OrderItemList orderItems){this.orderItems=orderItems;}
    public void setSocket(Socket socket){this.socket=socket;}
    public void setCookTime(int cookTime){this.cookTime=cookTime;}

    /**
     * addItem(OrderItem): add item to the order;
     * @param item
     */

    public void addItem(OrderItem item){
        orderItems.add(item);
        caculateCost();
    }

    /**
     *
     */
    public void deleteAll(){
        orderItems.deleteAll();
    }

    /**
     *
     * @param name
     * @param quantity
     */

    public void deleteItem(String name,int quantity){
        orderItems.delete(name,quantity);
        caculateCost();
    }


    public int caculateCookTime(){
        int time=0;
        for(int i=0;i<getOrderItems().size();i++){
            time = time + (int)(Math.random()*120)+180;
        }
        return time;
    }
    /**
     * caculateCost of the whole order;
     * @return the total value of the order;
     */
    public double caculateCost(){
        double totalCost=0;
        Iterator it=orderItems.iterator();
        while(it.hasNext()){
            OrderItem p=(OrderItem)it.next();
            totalCost+=p.getQuantiy()*p.getCost();
        }
        totalCost=totalCost*1.06*1.3;
        return totalCost;
    }


}

class OrderItemList implements Iterable{
    private Node head;
    private int N;

    /**
     * use linklist in the OrderItemList;
     */

    public OrderItemList(){
        head=null;
        N=0;
    }

    private class Node{
        Node(OrderItem item, Node next){
            this.item=item;
            this.next=next;
        }
        OrderItem item;
        Node next;
    }

    /**
     *
     * @return the size of the orderItemList
     */
    public int size(){
        return N;
    }

    /**
     * add():add item to the list;
     * if the item is already existed, add the quantity to the itemQuantity
     * consider what should be return;???
     * @param item the item added to the OrderItemList
     */
    public void add(OrderItem item){
        if(head==null){
            head=new Node(item,null);
            N++;
            return;
        }
        for(Node p=head;p!=null;p=p.next){
            if(p.item.getName().equals(item.getName())){
                p.item.setQuantiy(p.item.getQuantiy()+item.getQuantiy());
                return;
            }
        }
        head=new Node(item,head);
        N++;
    }

    public OrderItem getItemByName(String name){
        if(head==null){
            return null;
        }
        for(Node p=head;p!=null;p=p.next){
            if(p.item.getName().equals(name)){
                return p.item;
            }
        }
        return null;
    }

    /**
     * delete the item or deduct the quantity of the item in the order
     * if the deduct number is bigger than the orderItem quantity,delete the item
     * if the deduct number is less than the orderItem quantity, deduct the number;
     * consider what should be return;???
     * @param itemName
     * @param itemQuantity
     */
    public void delete(String itemName,int itemQuantity){
        if(head==null)return;
        if(head.item.getName()==itemName){
            if(head.item.getQuantiy()<=itemQuantity){
                head=head.next;
            }else{
                head.item.setQuantiy(head.item.getQuantiy()-itemQuantity);
            }
            return;
        }
        for(Node p=head;p.next!=null;p=p.next){
            if (p.next.item.getName()==itemName){
                if(p.next.item.getQuantiy()<=itemQuantity){
                    p.next=p.next.next;
                }
                else {
                    p.next.item.setQuantiy(p.next.item.getQuantiy()-itemQuantity);
                }
            }
        }
        return ;
    }

    public void deleteAll(){
        head=null;
        N=0;
    }

    @Override
    public Iterator iterator() {
        List list=new ArrayList<>(N);
        for(Node p=head;p!=null;p=p.next){
            list.add(p.item);
        }
        return list.iterator();
    }
}
