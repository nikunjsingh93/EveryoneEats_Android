package com.example.nikun.foodordering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderList implements Iterable{
    private static int DEFAULT_SIZE=10;
    private Order orders[];
    private int front,rear;
    private int size;

    /**
     * constructor:
     */
    public OrderList(){
        orders=new Order[DEFAULT_SIZE];
        front=rear=0;
        size=0;
    }

    public OrderList(int capacity){
        orders=new Order[capacity];
        front=rear=0;
        size=0;
    }

    /**
     * @return the size of the queue;
     */
    public int getSize(){
        return size;
    }

    /**
     * @return the boolean type wheather the queue is empty;
     */
    public boolean isEmpty(){
        return front==rear;
    }

    /**
     * Update this OrderList by adding an item on the newest end;
     * @param order the order to add
     */
    public void addOrder(Order order){
        if(this.front==(this.rear+1)%this.orders.length){
            ensureCapacity(this.orders.length*2+1);
        }
        orders[rear]=order;
        this.rear=(this.rear+1)%this.orders.length;
        size++;
    }

    /**
     * Update this Queue by taking the oldest item off the queue
     * @return the order or null if there is no order;
     */
    public Order deleteOldestOrder(){
        Order temp=this.orders[front];
        this.front=(this.front+1)%this.orders.length;
        size--;
        return temp;
    }

    /**
     * increas the capacity of the array;
     * @param capacity the size increasing to ;
     */

    private void ensureCapacity(int capacity){
        Order[] old=this.orders;
        this.orders=new Order[capacity];
        int j=0;
        for(int i=this.front;i!=this.rear;i=(i+1)%old.length){
            orders[j++]=old[i];
        }
        front=0;
        rear=j;
    }

    public Order getOldestOrder(){
        return orders[front];
    }

    public Order getLatestOne(){
        return orders[rear-1];
    }

    public Iterator iterator() {
        List list=new ArrayList<>(size);
        for(int i=0;i<getSize();i++){
            list.add(orders[i]);
        }
        return list.iterator();
    }
}
