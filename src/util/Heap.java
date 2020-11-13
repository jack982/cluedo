package util;

import gamedata.Cell;

import java.util.ArrayList;
import java.util.Iterator;

public class Heap {
	
	private ArrayList elements;
	private int maxSize;
	private static final int NIL = -1;
	
		
	public Heap(int maxSize) {
		this.maxSize = maxSize;
        System.out.println("maxSize: "+this.maxSize);
		elements = new ArrayList(maxSize);
        for(int i=0;i<maxSize;i++){
            elements.add(i,null);
        }
        System.out.println("dim: "+elements.size());
	}
	
	
	
	public int getParent(int index) {
		if(index == 0) return NIL;
		else if(index % 4 == 0) return index/4 - 1;
		else return index/4;
	}
	
	
	public int getLeft(int index) {
		int ret = 4*index + 1;
		
		if(ret >= maxSize) return NIL;
		else return ret;
	}
	
	
	public int getMiddleLeft(int index) {
		int ret = 4*index + 2;
		
		if(ret >= maxSize) return NIL;
		else return ret;
	}
	

	public int getMiddleRight(int index) {
		int ret = 4*index + 3;
		
		if(ret >= maxSize) return NIL;
		else return ret;
	}
	
	public int getRight(int index) {
		int ret = 4*index + 4;
		
		if(ret >= maxSize) return NIL;
		else return ret;
	}
	
	
	public void insert(Object o, int index) {
		elements.set(index,o);
        System.out.println(elements.get(index));
	}
	
	
	public ArrayList getElements() {
		return this.elements;
	}
	
	public int getCurrentSize() {
		return elements.size();
	}
    
    
    public void printHeap(){
        Iterator it = elements.iterator();
        while(it.hasNext()){
            Object o = it.next();
            if(o != null){
                Cell c = (Cell)o;
                
            System.out.println("index: "+elements.indexOf(c)+" x: "+c.getX()+" y: "+c.getY());
            }
        }
    }
	
}


















