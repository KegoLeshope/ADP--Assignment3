/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.adp_assignemnt3;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author User
 */
public class RunActivity{
   ArrayList <Customer> cust= new ArrayList<Customer>();
   ArrayList <Supplier> sup = new ArrayList<Supplier>();
   
ObjectInputStream input;
FileWriter fw;
BufferedWriter bw;
OutputStreamWriter output;
   
   public void openFileRead(){
        try{
            input = new ObjectInputStream( new FileInputStream( "stakeholder.ser" ) );
            System.out.println("*** ser file created and opened for reading  ***");
        }
        catch (IOException ioe){
            System.out.println("error opening ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    public void readFile(){
        try{
           while(true){
               Object line = input.readObject();
               String c ="Customer";
               String s = "Supplier";
               String name = line.getClass().getSimpleName();
               if ( name.equals(c)){
                   cust.add((Customer)line);
               } else if ( name.equals(s)){
                   sup.add((Supplier)line);
               } else {
                   System.out.println("It didn't work");
               }
           }
        }
        catch (EOFException eofe) {
            System.out.println("End of file reached");
        }
        catch (ClassNotFoundException ioe) {
            System.out.println("Class error reading ser file: "+ ioe);
        }
        catch (IOException ioe) {
            System.out.println("Error reading ser file: "+ ioe);
        }
    }
    public void readCloseFile(){
        try{
            input.close( ); 
        }
        catch (IOException ioe){
            System.out.println("error closing ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    public void sortCustomer(){
        String[] sortID = new String[cust.size()];
        ArrayList<Customer> sortA= new ArrayList<Customer>();
        int count = cust.size();
        for (int i = 0; i < count; i++) {
            sortID[i] = cust.get(i).getStHolderId();
        }
        Arrays.sort(sortID);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sortID[i].equals(cust.get(j).getStHolderId())){
                    sortA.add(cust.get(j));
                }
            }
        }
        cust.clear();
        cust = sortA;
    }
    public int getAge(String dob){
        String[] seperation = dob.split("-");
        
        LocalDate birth = LocalDate.of(Integer.parseInt(seperation[0]), Integer.parseInt(seperation[1]), Integer.parseInt(seperation[2]));
        LocalDate current = LocalDate.now();
        Period difference = Period.between(birth, current);
        int age = difference.getYears();
        return age;
    }
    public String formatDob(String dob){
        DateTimeFormatter changeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");       
        LocalDate birth = LocalDate.parse(dob);
        String formatted = birth.format(changeFormat);
        return formatted;
    }
    
    public void displayCustomersText(){
        try{
            fw = new FileWriter("customerOutFile.txt");
            bw = new BufferedWriter(fw);
            bw.write(String.format("%s \n","==============================Customers============================="));
            bw.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", "ID","Name","Surname","Date of Birth","Age"));
            bw.write(String.format("%s \n","===================================================================="));
            for (int i = 0; i < cust.size(); i++) {
                bw.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", cust.get(i).getStHolderId(), cust.get(i).getFirstName(), cust.get(i).getSurName(), formatDob(cust.get(i).getDateOfBirth()),getAge(cust.get(i).getDateOfBirth())));
            }
            bw.write(String.format("%s\n"," "));
            bw.write(String.format("%s",rent()));
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            bw.close( );
        }
        catch (IOException ioe){
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    public String rent(){
        int count = cust.size();
        int canRent = 0;
        int notRent = 0;
        for (int i = 0; i < count; i++) {
            if (cust.get(i).getCanRent()){
                canRent++;
            }else {
                notRent++;
            }
        }
        String line =String.format("Number of customers who can rent : %4s\nNumber of customers who cannot rent : %s\n", canRent, notRent);
        return line;
    }
    
    public void sortSuppliers(){
        String[] sortID = new String[sup.size()];
        ArrayList<Supplier> sortA= new ArrayList<Supplier>();
        int count = sup.size();
        for (int i = 0; i < count; i++) {
            sortID[i] = sup.get(i).getName();
        }
        Arrays.sort(sortID);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sortID[i].equals(sup.get(j).getName())){
                    sortA.add(sup.get(j));
                }
            }
        }
        sup.clear();
        sup = sortA;
    }
    public void displaySupplierText(){
        try{
            fw = new FileWriter("supplierOutFile.txt", true);
            bw = new BufferedWriter(fw);
            bw.write("===============================SUPPLIERS==============================\n");
            bw.write(String.format("%-15s %-20s %-15s %-15s \n", "ID","Name","Prod Type","Description"));
            bw.write("======================================================================\n");
            for (int i = 0; i < sup.size(); i++) {
                bw.write(String.format("%-15s %-20s %-15s %-15s \n", sup.get(i).getStHolderId(), sup.get(i).getName(), sup.get(i).getProductType(),sup.get(i).getProductDescription()));
            }
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            bw.close( );
        }
        catch (IOException ioe){
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }    
    
    public static void main(String args[])  {
        RunActivity object = new RunActivity();
        object.openFileRead();
        object.readFile();
        object.readCloseFile();
        object.sortCustomer();
        object.sortSuppliers();
        object.displayCustomersText();
        object.displaySupplierText();
        
    }
   
}
