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
 * @author Martin
 * This class is an implementation of a LinkedList with the purpose of making it
 * possible for the user to use the rewind/forward funcions in the main table.
 * Whenever the variable value is retrieved, the method getValueDeep returns a deep copy
 * in order to avoid passing pointers instead of copies of the actual values.
 */
public class RewindLinkedList {
    
    private final ArrayList<Entry> value;
    private RewindLinkedList prev, next;

    public RewindLinkedList(List<Entry> value, RewindLinkedList prev, RewindLinkedList next) {
        ArrayList<Entry> newValue = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            newValue.add(new Entry(value.get(i)));
        }
        this.value = newValue;
        this.prev = prev;
        this.next = next;
    }
    public RewindLinkedList(RewindLinkedList node) {
        ArrayList<Entry> newValue = new ArrayList<>();
        for (int i = 0; i < node.value.size(); i++) {
            newValue.add(new Entry(node.value.get(i)));
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
    
    public void setPrev(List<Entry> value) {
        this.prev =new RewindLinkedList(value, null, this);
    }

    public void setNext(RewindLinkedList next) {
        next.setPrev(this);
        this.next = next;
    }
    
    public void setNext(List<Entry> value) {
        this.next = new RewindLinkedList(value, this, null);
    }
    //Returns a deep copy of value
    public ArrayList<Entry> getValueDeep() {
        ArrayList<Entry> newValue = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            newValue.add(new Entry(value.get(i)));
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
        
        if (cur1!=null){
             otherNodes++;
            while(cur1.getPrev()!=null){
                otherNodes++;
                cur1=cur1.getPrev();
            }
        }
        if (cur2!=null) {
            otherNodes++;
            while(cur2.getNext()!=null){
                otherNodes++;
                cur2=cur2.getNext();
            }
        }
        return otherNodes+1;
    }
    @Override
    public String toString(){
        return "Array: "+value.toString()+" \nNext element: "+next+" \nPrevious element: "+prev;
    }
}
