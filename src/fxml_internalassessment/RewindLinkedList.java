/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nando
 */
public class RewindLinkedList {
    
    private final ArrayList<Person> value;
    private RewindLinkedList prev, next;

    public RewindLinkedList(List<Person> value, RewindLinkedList prev, RewindLinkedList next) {
        ArrayList<Person> newValue = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            newValue.add(new Person(value.get(i)));
        }
        this.value = newValue;
        this.prev = prev;
        this.next = next;
    }
    public RewindLinkedList(RewindLinkedList node) {
        ArrayList<Person> newValue = new ArrayList<>();
        for (int i = 0; i < node.value.size(); i++) {
            newValue.add(new Person(node.value.get(i)));
        }
        this.value = newValue;
        this.prev = node.prev;
        this.next = node.next;
    }
    public RewindLinkedList() {
        this.value = null;
        this.prev = null;
        this.next = null;
    }
    
    public void setPrev(RewindLinkedList prev) {
        prev.setNext(this);
        this.prev = prev;
    }
    
    public void setPrev(List<Person> value) {
        this.prev =new RewindLinkedList(value, null, this);
    }

    public void setNext(RewindLinkedList next) {
        next.setPrev(this);
        this.next = next;
    }
    
    public void setNext(List<Person> value) {
        this.next = new RewindLinkedList(value, this, null);
    }

    public ArrayList<Person> getValueDeep() {
        ArrayList<Person> newValue = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            newValue.add(new Person(value.get(i)));
        }
        return newValue;
    }

    public RewindLinkedList getPrev() {
        return prev;
    }
    
    public RewindLinkedList getNext() {
        return next;
    }
    
    //Deletes the element at the beginning of the list
    public void removeFirst(){
        RewindLinkedList cur=new RewindLinkedList(this.value, prev, next);
        while (cur.getPrev().getPrev()!=null){
            cur=cur.getPrev();
        }
        cur.prev=null;
    }
    /**
    *Returns the number of nodes in the linked list by starting out from the current block and counting all previous and following blocks
    */
    public int getListSize(){
        RewindLinkedList cur=new RewindLinkedList(value, prev, next), cur1=cur.getPrev(), cur2=cur.getNext();
        int otherNodes=0;
//        boolean doesCurExist;
//        if (this==null) doesCurExist=false;
//        else doesCurExist=true;
//        System.out.println("Does the current Node exist?  " +doesCurExist);
        if (cur1!=null){
             otherNodes++;
//             System.out.println("Cur1= " +cur1);
            while(cur1.getPrev()!=null){
                otherNodes++;
                cur1=cur1.getPrev();
//                System.out.println("Cur1= " +cur1);
            }
        }
        if (cur2!=null) {
            otherNodes++;
//            System.out.println("Cur2= "+cur2);
            while(cur2.getNext()!=null){
                otherNodes++;
                cur2=cur2.getNext();
//                System.out.println("Cur2= " +cur2);
            }
        }
//        System.out.println("The number of nodes is: " +(otherNodes+1));
        return otherNodes+1;
    }
    @Override
    public String toString(){
        return "Array: "+value.toString()+" \nNext element: "+next+" \nPrevious element: "+prev;
    }
}
