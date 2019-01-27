package com.example.nikun.foodordering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InventoryItemList implements Iterable{
    private int N;
    private static int DEFAULT_SIZE=50;
    private InventoryItem[] inventoryItems;

    /**
     * constructor:
     */
    public InventoryItemList(){
        N=0;
        inventoryItems=new InventoryItem[DEFAULT_SIZE];
    }

    public InventoryItemList(int capacity){
        N=0;
        inventoryItems=new InventoryItem[capacity];
    }

    /**
     * add the inventory list to the list
     * @param item the inventoryItem to added
     */
    public void addInventoryItem(InventoryItem item){
        for(int i=0;i<N;i++){
            if(inventoryItems[i].getName().equals(item.getName())){
                inventoryItems[i].setQuantity(inventoryItems[i].getQuantity() + item.getQuantity());
                return ;
            }
        }
        if(N==inventoryItems.length){
            ensureCapacity(inventoryItems.length*2);
        }
        inventoryItems[N++]=item;
    }

    /**
     * @return the size of the inventorylist .
     */
    public int size(){
        return N;
    }

    public InventoryItem getInventoryItemByName(String name){
        for(int i=0;i<N;i++){
            if(inventoryItems[i].getName().equals(name))return inventoryItems[i];
        }
        return null;
    }

    private void ensureCapacity(int capacity){
        InventoryItem[] old=this.inventoryItems;
        inventoryItems=new InventoryItem[capacity];
        for(int i=0;i<N;i++){
            inventoryItems[i]=old[i];
        }
    }

    @Override
    public Iterator iterator() {
        List list=new ArrayList<>(N);
        for(int i=0;i<size();i++){
            list.add(inventoryItems[i]);
        }
        return list.iterator();
    }

}

