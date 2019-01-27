package com.example.nikun.foodordering;

public class OrderItem {
    private String name;
    private double cost;
    private int quantiy;
    private boolean available;
    private int cooktime;
    /**
     * constructor
     */

    public OrderItem(){
        this(null,0,0,false,0);
    }

    public OrderItem(String name,double cost,int quantiy,boolean available,int cooktime){
        this.name=name;
        this.cost=cost;
        this.quantiy=quantiy;
        this.available=available;
        this.cooktime=cooktime;
    }

    /**
     * getter: get the value of the variable
     * */
    public String getName(){return name;}
    public double getCost(){return cost;}
    public int getQuantiy(){return quantiy;}
    public boolean isAvailable(){return available;}
    public int getCooktim(){return cooktime;}

    /**
     * setter: set the value of the variable
     */

    public void setName(String name){this.name=name;}
    public void setCost(double cost){this.cost=cost;}
    public void setQuantiy(int quantity){this.quantiy=quantity;}
    public void setAvailable(boolean available){this.available=available;}
    public void setCooktime(int cooktime){this.cooktime=cooktime;}

}

