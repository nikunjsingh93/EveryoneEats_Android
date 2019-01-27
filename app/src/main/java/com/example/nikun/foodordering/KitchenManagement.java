package com.example.nikun.foodordering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Scanner;

public class KitchenManagement {
    private InventoryItemList inventoryItems;
    private  OrderList orderList;
    private  CustomerList customerList;
    private Order processingOrder;
    private String notification;
    private static KitchenManagement kitchen=null;
    private String customerFile="/sdcard/input.txt";
    private String inventoryFile="/sdcard/InventoryList.txt";

    /**
     * constructor: in singleton it's private;
     */
    private  KitchenManagement(){
        this(null,null,null);
        loadCustomer();
        orderList=new OrderList();

    }

    private KitchenManagement(InventoryItemList inventoryItems,OrderList orderList,CustomerList customerList){
        this.inventoryItems=inventoryItems;
        this.orderList=orderList;
        this.customerList=customerList;
    }

    /**
     * create the instance
     * @return the single instance of kitchenmanagement
     */
    public static synchronized KitchenManagement getKitchen(){
        if(kitchen==null){
            kitchen=new KitchenManagement();
        }
        return kitchen;
    }

    public CustomerList getCustomerList(){
        return customerList;
    }

    public InventoryItemList getInventoryItems(){
        return inventoryItems;
    }

    public OrderList getOrderList(){
        return orderList;
    }

    public void finishedOldestOrder(){
        orderList.deleteOldestOrder();
    }

    /**
     * verify if the customer is in the customerList and wheather the pin is right
     * @param customer the
     * @return true if the customer exist and pin is right
     */
    public boolean verifyCustomer(Customer customer){
        Customer a;
        if((a=customerList.getCustomerByID(customer.getCustomerID()))==null)return false;
        if(!a.getPassword().equals(a.getPassword()))return false;

        return true;
    }

    /**
     * add the new customer to the file
     * @param customer the new customer registered
     * @return true if registration success,false if failed;
     */

    public boolean registerCustomer(Customer customer){
        String thisLine=null;
        Customer a;
        if((a=customerList.getCustomerByID(customer.getCustomerID()))==null){
            customerList.addCustomer(customer);
            try{
                File file=new File(customerFile);
                OutputStream stream=new FileOutputStream(file);
                OutputStreamWriter os=new OutputStreamWriter(stream);
                BufferedWriter ou=new BufferedWriter(os);
                Iterator it=customerList.iterator();
                while(it.hasNext()){
                    Customer p=(Customer)it.next();
                    ou.write(p.getCustomerID()+","+p.getPassword()+","+p.getCustomerName());
                    ou.newLine();
                }
                ou.flush();
                stream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }
        else return false;
    }
    /**
     * check the orderif the order is "Not Available","Partially Available" or "All Available"
     * @param order1 the order need to be checked
     * @return "AA" means All Available; "NA" means not available; "PA"means not available and
     *          also contain which items are available and its quantity;
     */

    public String CheckOrderItem(Order order1){
        String result="";
        int count=0;
        int countp=0;
        int counta=0;
        InventoryItem a;
        Iterator it=order1.getOrderItems().iterator();
        while(it.hasNext()){
            OrderItem p=(OrderItem) it.next();
            if((a=inventoryItems.getInventoryItemByName(p.getName()))!=null&&a.getQuantity()>=p.getQuantiy()) { //all available;
                if (count == 0) {
                    result = result + p.getName() + "," + p.getQuantiy();
                } else {
                    result = result + "," + p.getName() + "," + p.getQuantiy();
                }
                count++;
                counta++;
            }else if(a!=null&&a.getQuantity()==0){  //
                if (count == 0) {
                    result = result + p.getName() + "," + a.getQuantity();
                } else {
                    result = result + "," + p.getName() + "," + a.getQuantity();
                }
                count++;
            }
            else{
                if (count == 0) {
                    result = result + p.getName() + "," + a.getQuantity();
                } else {
                    result = result + "," + p.getName() + "," + a.getQuantity();
                }
                count++;
                countp++;
            }
        }
        if(counta==0&&countp==0)result="NA,"+result;
        else if(countp>0)result="PA,"+result;
        else if(count>=counta)result="AA,"+result;
        else result="ERROR in the quantity of the item";
        return result;
    }

    public void deleteOldestOrder(){
        orderList.deleteOldestOrder();
    }

    public void addOrder(Order order){
        orderList.addOrder(order);
    }

    /**
     * upload the customerList by reading the file;
     */
    public void loadCustomer(){
        String thisLine=null;
        customerList=new CustomerList();
        try{
            File file=new File(customerFile);
            if(!file.exists()){
                file.createNewFile();
                return ;
            }
            InputStream stream=new FileInputStream(customerFile);
            InputStreamReader is=new InputStreamReader(stream);
            BufferedReader in=new BufferedReader(is);
            while((thisLine=in.readLine())!=null){
                Scanner scanner=new Scanner(thisLine);
                scanner.useDelimiter(",");
                Customer cus=new Customer(scanner.next(),scanner.next(),scanner.next());
                customerList.addCustomer(cus);
            }
            in.close();
            stream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * upload the inventory list by reading the file
     *
     */

    public void uploadInventoryList(InputStream stream){
        String thisLine=null;
        if(inventoryItems==null)inventoryItems=new InventoryItemList();
        try{

            InputStreamReader is=new InputStreamReader(stream);
            BufferedReader in=new BufferedReader(is);
            while((thisLine=in.readLine())!=null){
                Scanner scanner=new Scanner(thisLine);
                scanner.useDelimiter(",");
                InventoryItem item=new InventoryItem(scanner.next(),scanner.nextInt());
                inventoryItems.addInventoryItem(item);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
