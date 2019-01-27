package com.example.nikun.foodordering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerList implements Iterable{
    private int N;
    private static int DEFAULT_SIZE=10;
    private Customer[] customers;

    /**
     * constructor:
     */
    public CustomerList(){
        N=0;
        customers=new Customer[DEFAULT_SIZE];
    }

    public CustomerList(int capacity){
        N=0;
        customers=new Customer[capacity];
    }

    /**
     * upload the CustomerList by adding the customer
     * @param customer the customer to be added
     */
    public void addCustomer(Customer customer){
        if(N==customers.length){
            ensureCapacity(customers.length*2);
        }
        customers[N++]=customer;
    }

    /**
     * @return the number of customer;
     */
    public int size(){
        return N;
    }

    /**
     * get the customer by searching ID;
     * @param customerID the ID of the customer to be searched
     * @return searched customer
     */
    public Customer getCustomerByID(String customerID){
        for(int i=0;i<N;i++){
            if(customers[i].getCustomerID().equals(customerID))
                return customers[i];
        }
        return null;
    }
    /**
     * get the customer by searching name;
     * @param customerName the name of the customer to be searched
     * @return searched customer
     */
    public Customer getCustomerByName(String customerName){
        for (int i=0;i<N;i++){
            if(customers[i].getCustomerName().equals(customerName))
                return customers[i];
        }
        return null;
    }

    /**
     * update the CustomerList by increaseing the size of the array
     * @param capacity the size increased to
     */
    private void ensureCapacity(int capacity){
        Customer[] old=this.customers;
        customers=new Customer[capacity];
        for(int i=0;i<N;i++){
            customers[i]=old[i];
        }
    }

    @Override
    public Iterator iterator() {
        List list=new ArrayList<>(N);
        for(int i=0;i<size();i++){
            list.add(customers[i]);
        }
        return list.iterator();
    }
}

