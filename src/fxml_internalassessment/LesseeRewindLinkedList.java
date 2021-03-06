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
 * This class is a carbon copy of RewindLinkedList, customized for ArrayLists with a generic type of Lessee
 */
public class LesseeRewindLinkedList {
    
    private final ArrayList<Lessee> value;
    private LesseeRewindLinkedList prev, next;

    public LesseeRewindLinkedList(List<Lessee> value, LesseeRewindLinkedList prev, LesseeRewindLinkedList next) {
        ArrayList<Lessee> newValue = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            newValue.add(new Lessee(value.get(i)));
        }
        this.value = newValue;
        this.prev = prev;
        this.next = next;
    }
    public LesseeRewindLinkedList(LesseeRewindLinkedList node) {
        ArrayList<Lessee> newValue = new ArrayList<>();
        for (int i = 0; i < node.value.size(); i++) {
            newValue.add(new Lessee(node.value.get(i)));
        }
        this.value = newValue;
        this.prev = node.prev;
        this.next = node.next;
    }
    public LesseeRewindLinkedList() {
        this.value = null;
        this.prev = null;
        this.next = null;
    }
    
    public void setPrev(LesseeRewindLinkedList prev) {
        prev.setNext(this);
        this.prev = prev;
    }
    
    public void setPrev(List<Lessee> value) {
        this.prev =new LesseeRewindLinkedList(value, null, this);
    }

    public void setNext(LesseeRewindLinkedList next) {
        next.setPrev(this);
        this.next = next;
    }
    
    public void setNext(List<Lessee> value) {
        this.next = new LesseeRewindLinkedList(value, this, null);
    }

    public ArrayList<Lessee> getValueDeep() {
        ArrayList<Lessee> newValue = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            newValue.add(new Lessee(value.get(i)));
        }
        return newValue;
    }

    public LesseeRewindLinkedList getPrev() {
        return prev;
    }
    
    public LesseeRewindLinkedList getNext() {
        return next;
    }
    
    //Deletes the element at the beginning of the list
    public void removeFirst(){
        LesseeRewindLinkedList cur=new LesseeRewindLinkedList(this.value, prev, next);
        while (cur.getPrev().getPrev()!=null){
            cur=cur.getPrev();
        }
        cur.prev=null;
    }
    /**
    *Returns the number of nodes in the linked list by starting out from the current block and counting all previous and following blocks
    */
    public int getListSize(){
        LesseeRewindLinkedList cur=new LesseeRewindLinkedList(value, prev, next), cur1=cur.getPrev(), cur2=cur.getNext();
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
