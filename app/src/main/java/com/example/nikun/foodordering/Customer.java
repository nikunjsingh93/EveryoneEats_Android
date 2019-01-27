package com.example.nikun.foodordering;

public class Customer {
    private String customerID;
    private String customerName;
    private String password;
    private Order[] orders;
    /**
     * constructor
     */
    public Customer(){
        this(null,null,null);
    }

    public Customer(String customerID,String password,String customerName){
        this.customerID=customerID;
        this.customerName=customerName;
        this.password=password;
    }
    /**
     * getter: get the value of the variable
     * */
    public String getCustomerID(){return customerID;}
    public String getPassword(){return password;}
    public String getCustomerName(){return customerName;}

    /**
     * setter: set the value of the variable
     */
    public void setCustomerID(String customerID){this.customerID=customerID;}
    public void setPassword(String password){this.password=password;}
    public void setCustomerName(String customerName){this.customerName=customerName;}

}
