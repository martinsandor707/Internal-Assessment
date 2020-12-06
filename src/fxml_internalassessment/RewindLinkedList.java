/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

import java.util.List;

/**
 *
 * @author nando
 */
public class RewindLinkedList {
    
    private List<Person> value;
    private RewindLinkedList prev, next;

    public RewindLinkedList(List<Person> value, RewindLinkedList prev, RewindLinkedList next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }
    public RewindLinkedList(RewindLinkedList node) {
        this.value = node.value;
        this.prev = node.prev;
        this.next = node.next;
    }
    public RewindLinkedList() {
        this.value = null;
        this.prev = null;
        this.next = null;
    }

    public void setNode(RewindLinkedList node) {
        this.value = node.value;
        this.prev = node.prev;
        this.next = node.next;
    }
    
    public void setNode(List<Person> value, RewindLinkedList prev, RewindLinkedList next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }
    
    public void setValue(List<Person> value) {
        this.value = value;
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

    public List<Person> getValue() {
        return value;
    }

    public RewindLinkedList getPrev() {
        return prev;
    }

    public RewindLinkedList getNext() {
        return next;
    }
    
    //returns the first element in the list
    public RewindLinkedList getFirst(){
        RewindLinkedList cur=new RewindLinkedList(value, prev, next);
        while (cur.getPrev()!=null){
            cur=cur.getPrev();
        }
        return cur;
    }
    
    //sets the value of the first Node to the specified parameter and sets the previous pointer to null
    public void setFirst(List<Person> value){
        RewindLinkedList cur=new RewindLinkedList(this.value, prev, next);
        while (cur.getPrev()!=null){
            cur=cur.getPrev();
        }
        cur.setValue(value);
    }
    
    public void addFirst(List<Person> value){
        RewindLinkedList cur=new RewindLinkedList(this.value, prev, next);
        while (cur.getPrev()!=null){
            cur=cur.getPrev();
        }
        RewindLinkedList newFirst=new RewindLinkedList(value, null, cur);
        cur.setPrev(newFirst);
    }
    
    public void removeFirst(){
        RewindLinkedList cur=new RewindLinkedList(this.value, prev, next);
        while (cur.getPrev().getPrev()!=null){
            cur=cur.getPrev();
        }
        cur.prev=null;
    }
    
    //returns the last element of the list
    public RewindLinkedList getLast(){
        RewindLinkedList cur=new RewindLinkedList(value, prev, next);
        while (cur.getNext()!=null){
            cur=cur.getNext();
        }
        return cur;
    }
    
    //sets the value of the last Node to the specified parameter and sets the next pointer to null
    public void setLast(RewindLinkedList node){
        RewindLinkedList cur=new RewindLinkedList(value, prev, next);
        while (cur.getNext()!=null){
            cur=cur.getNext();
        }
         cur.setNode(node);
         cur.next=null;
    }
    
    public void addLast(List<Person> value){
        RewindLinkedList cur=new RewindLinkedList(this.value, prev, next);
        while (cur.getNext()!=null){
            cur=cur.getNext();
        }
        RewindLinkedList newNode=new RewindLinkedList(value, cur, null);
        cur.setNext(newNode);
    }
    
    public void removeLast(){
        RewindLinkedList cur=new RewindLinkedList(value, prev, next);
        while (cur.getNext().getNext()!=null){
            cur=cur.getNext();
        }
         cur.next=null;
    }
    
    //Returns the number of nodes in the linked list by starting out from the current block and counting all previous and following blocks
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
    
}
