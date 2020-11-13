/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author mciasco
 *
 */
public class MyPriorityQueue {
	
	private class QueueElement {
		
		private int priority;
		private Object obj;
		
		public QueueElement(Object obj, int priority) {
			this.obj = obj;
			this.priority = priority;
		}
		
		
		public Object getObj() {
			return obj;
		}
		public void setObj(Object obj) {
			this.obj = obj;
		}
		public int getPriority() {
			return priority;
		}
		public void setPriority(int priority) {
			this.priority = priority;
		}
		
		
		
		
	}
	
		
	ArrayList elements;

	
		
	
	public MyPriorityQueue() {
		elements = new ArrayList();
	}


	private ArrayList getElements() {
		return elements;
	}


	private void setElements(ArrayList elements) {
		this.elements = elements;
	}
	
	public synchronized void push(Object obj, int priority) {
		QueueElement qelem = new QueueElement(obj,priority);
		
		Iterator i = this.elements.iterator();
		boolean found = false;
		int index = -1;
		
		while(i.hasNext() && !found) {
			QueueElement qe = (QueueElement)i.next();
			if(qelem.getPriority() < qe.getPriority()) {
				index = elements.indexOf(qe);
				found = true;
			}
		}
		if(found) {
			elements.add(index,qelem);
		}
		else {
			elements.add(qelem);
		}
	}
	
	public synchronized Object pop() {
		Object returnObj = null;
		
		if(elements.size() > 0) {
			returnObj = ((QueueElement)elements.get(0)).getObj();
			elements.remove(0);
		}
		return returnObj;
	}
	
	
}
