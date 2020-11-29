/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

/**
 *
 * @author nando
 */
public class RewindLinkedList {
    
    private Object value;
    private RewindLinkedList prev, next;

    public RewindLinkedList(Object value, RewindLinkedList prev, RewindLinkedList next) {
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
    
    public void setNode(Object value, RewindLinkedList prev, RewindLinkedList next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
    

    public void setPrev(RewindLinkedList prev) {
        prev.setNext(this);
        this.prev = prev;
    }
    
    public void setPrev(Object value) {
        this.prev =new RewindLinkedList(value, null, this);
    }

    public void setNext(RewindLinkedList next) {
        next.setPrev(this);
        this.next = next;
    }
    
    public void setNext(Object value) {
        this.next = new RewindLinkedList(value, this, null);
    }

    public Object getValue() {
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
    public void setFirst(Object value){
        RewindLinkedList cur=new RewindLinkedList(this.value, prev, next);
        while (cur.getPrev()!=null){
            cur=cur.getPrev();
        }
        cur.setValue(value);
    }
    
    public void addFirst(Object value){
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
        cur.setPrev(null);
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
         cur.setNext(null);
    }
    
    public void addLast(Object value){
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
         cur.setNext(null);
    }
    
    //Returns the number of nodes in the linked list by starting out from the current block and counting all previous and following blocks
    public int getListSize(){
        RewindLinkedList cur=new RewindLinkedList(value, prev, next), cur1=cur.getPrev(), cur2=cur.getNext();
        int otherNodes=0;
        while(cur1.getValue()!=null){
            otherNodes++;
            cur1=cur1.getPrev();
        }
        while(cur2.getValue()!=null){
            otherNodes++;
            cur2=cur2.getNext();
        }
        return otherNodes+1;
    }
    
}
