package Doubly;
// implementing SparseVec using Doubly Linked List
public class LLSparseVec<E> implements SparseVec<E> {
    //----- nested Node class ------
    private static class Node<E> {
            private int element;
            private int index;
            private Node<E> prev;
            private Node<E> next;
            public Node(int idx, int e, Node<E> p, Node<E> n) {
                    index = idx;
                    element = e;
                    prev = p;
                    next = n;
            }
            public Node(int e, Node<E> p, Node<E> n) {
                    element = e;
                    prev = p;
                    next = n;
            }
            public int getElement() { return element;}
            public int getIndex() { return index;}
            public Node<E> getPrev() { return prev;}
            public Node<E> getNext() { return next;}
            public void setElement(int e) { element = e;}
            public void setPrev(Node<E> p) {prev = p;}
            public void setNext(Node<E> n) {next = n;}
    } // ------ end of nested Node class -----

    private Node<E> header;
    private Node<E> trailer;
    private int length = 0;
    private int nelems = 0;
    
    // constructor initialize a sparse vector with a given length len
    public LLSparseVec(int len) {
    	if (len <= 0)	
    		len = 1; 	// if zero or negative len, set len = 1;
    	header = new Node<>(0,0,null,null);
        trailer = new Node<>(1,0,header,null);
        header.setNext(trailer);
        length = len;
        nelems = 0;
    }
    // constructor initialize a sparse vector
    public LLSparseVec() {
        header = new Node<>(0,null,null);
        trailer = new Node<>(0,header,null);
        header.setNext(trailer);
    }

    public int getLength() {
        	return length;
    }

    public int numElements() {
        	return nelems;
    }
    // go through the list and find the element.
    // if element index is out of bounds, return -1;
    public int getElement(int idx) {
    		if ((idx<0)||(idx>=length))
    			return -1;
        	Node <E> pointer = header.getNext();
        	while ( (pointer.getIndex() != idx) && (pointer != trailer) ) {
                	pointer = pointer.getNext();
        	}
        	// this means that there is no element with that index
        	if(pointer == trailer) return 0;
        	else return pointer.getElement();
    }	

    public void clearElement(int idx) {
        	Node <E> current = header.getNext();
        	while ( (current.getIndex() != idx) && (current!=trailer)) {
                	current = current.getNext();
        	}
        	if (current == trailer) return;		// there is no element with that index
        	// correct pointers to remove the element
        	Node<E> predecessor = current.getPrev();
        	Node<E> sucessor = current.getNext();
        	predecessor.setNext(sucessor);
        	sucessor.setPrev(predecessor);
        	nelems--;
    }

    public void setElement(int idx, int val) {
    		// check if index is within bounds of the vector. if it isn't, just return;
    		if ((idx < 0) || (idx>= length))
    			return;
    		// clear the element if value is zero
    		if (val==0) {
    			clearElement(idx);
    		}
        	if (nelems == 0) {
        		addBetween(idx, val, header, trailer);
        		return;
        	}
        	Node <E> current = header.getNext();
        	// traverse the list until end or find the index or until the index is bigger than given index
        	while ( (current!=trailer) && (current.getIndex() != idx) && (current.getIndex() < idx)) {// || ( current.getIndex()< idx) ) {
                	current = current.getNext();
        	}
        	// at the end of the list
        	if (current == trailer) {
        		addBetween(idx, val, trailer.getPrev(), trailer);
        		return;
            }
        	// find the correct index
            else if(current.getIndex() == idx) {
                    current.setElement(val);
                    return;
            }
        	// the index is bigger than the given idx
            else {
                    addBetween(idx, val, current.getPrev(), current);
            }
    }
    // insert a new element into the list
    public void addElement(int idx, int val) {
    	if (nelems == 0) {
    		addBetween(idx,val,header,trailer);
    		return;
    	}
    	addBetween(idx,val, trailer.getPrev(), trailer);
    }
 
    public int[] getAllIndices() {
    	if (nelems == 0) {
            return null;
    	}
            Node <E> current = header.getNext();
            int i = 0;
            int [] allInd = new int[nelems];
            while (current != trailer){
                    allInd[i] = current.getIndex();
                    current = current.getNext();
                    i++;
            }
            return allInd;
    }
    
    public int[] getAllValues() {
    	if (nelems == 0) {
            return null;
    	}
        Node <E> current = header.getNext();
        int i = 0;
        int [] allVal = new int[nelems];
        while (current != trailer){
                allVal[i] = current.getElement();
                current = current.getNext();
                i++;
        }
        return allVal;
    }

    public SparseVec<E> addition(SparseVec<E> otherV) {
    	// check if vectors are the same size.
    	if (length != otherV.getLength()) {
    		System.out.println("Vectors must be the same length.");
    		return null;
    	}
    	int max = otherV.numElements();	// get number of elements in otherV
    	int sum = 0;
    	int Vind[] = otherV.getAllIndices(); // get the indices of otherV
    	int Vval[] = otherV.getAllValues();	 // get the values of otherV
    	SparseVec<E> newest = new LLSparseVec<E>(length); // create an empty sparseVec
    	// point to beginning of each vector being added
    	Node<E> curr = header.getNext();
    	int i = 0;
    	// while there are still elements in each vector, traverse through each list but only add the elements in order
    	while (curr != trailer && i<max) {	
    		// curr element is smaller than otherV so add curr element and go to the next curr
    		if (curr.getIndex() < Vind[i]) {
        		newest.addElement(curr.getIndex(), curr.getElement());
        		curr = curr.getNext();
        	}
    		// otherV is smaller than curr so add otherV and increment i to get the next index and value
        	else if (Vind[i] < curr.getIndex()) {
        		newest.addElement(Vind[i], Vval[i]);
        		i++;
        	}
    		// if none of the above conditions, this means they have the same index 
    		// and need to add them together to get new element value
        	else {
        		sum = curr.getElement() + Vval[i];
        		if (sum == 0) 
        			newest.clearElement(Vind[i]);
        		// add the new element to new vector
        		newest.addElement(Vind[i], sum);
        		// move both to next elements in vector
        		curr = curr.getNext();
        		i++;
        	}
    	}
    	// the curr is at the end but there are still elements with higher
    	// indices left in otherV so now just add the rest to the vector.
    	if ((curr == trailer) && (i != (max-1))) { 	//
			while (i < max) {
				newest.addElement(Vind[i], Vval[i]);
				i++;
			}
    	}
    	// otherV is at the end of the vector but there are still elements in
    	// the other vector with higher indices so add the rest to the vector
    	else if ((i == max) && curr != trailer) {
    		while (curr != trailer) {
    			newest.addElement(curr.getIndex(), curr.getElement());
    			curr = curr.getNext();
    		}
    	}
    	// return the new vector
    	return newest;
    }
    // same method as addition but instead subtract the elements when indices match up	 
	public SparseVec substraction(SparseVec otherV) {
		if (length != otherV.getLength()) {
    		System.out.println("Vectors must be the same length.");
    		return null;
    	}
    	int max = otherV.numElements();
    	int sum = 0;
    	int Vind[] = otherV.getAllIndices();
    	int Vval[] = otherV.getAllValues();
    	SparseVec<E> newest = new LLSparseVec<E>(length);
    	Node<E> curr = header.getNext();
    	int i = 0;
    	while (curr != trailer && i<max) {			
    		if (curr.getIndex() < Vind[i]) {
        		newest.addElement(curr.getIndex(), curr.getElement());
        		curr = curr.getNext();
        	}
        	else if (Vind[i] < curr.getIndex()) {
        		newest.addElement(Vind[i], Vval[i]);
        		i++;
        	}
        	else {
        		sum = curr.getElement() - Vval[i];
        		if (sum == 0) newest.clearElement(Vind[i]);
        		newest.addElement(Vind[i], sum);
        		curr = curr.getNext();
        		i++;
        	}
    	}
    	if ((curr == trailer) && (i != (max-1))) { 	//
			while (i < max) {
				newest.addElement(Vind[i], Vval[i]);
				i++;
			}
    	}
    	else if ((i == max) && curr != trailer) {
    		while (curr != trailer) {
    			newest.addElement(curr.getIndex(), curr.getElement());
    			curr = curr.getNext();
    		}
    	}
    	return newest;
    }
	// same method as addition but instead multiply the elements when indices match up
	public SparseVec multiplication(SparseVec otherV) {
		if (length != otherV.getLength()) {
    		System.out.println("Vectors must be the same length.");
    		return null;
    	}
    	int max = otherV.numElements();
    	int sum = 0;
    	int Vind[] = otherV.getAllIndices();
    	int Vval[] = otherV.getAllValues();
    	SparseVec<E> newest = new LLSparseVec<E>(length);
    	Node<E> curr = header.getNext();
    	int i = 0;
    	while (curr != trailer && i<max) {			
    		if (curr.getIndex() < Vind[i]) {
        		newest.addElement(curr.getIndex(), curr.getElement());
        		curr = curr.getNext();
        	}
        	else if (Vind[i] < curr.getIndex()) {
        		newest.addElement(Vind[i], Vval[i]);
        		i++;
        	}
        	else {
        		sum = curr.getElement() * Vval[i];
        		if (sum == 0) newest.clearElement(Vind[i]);
        		newest.addElement(Vind[i], sum);
        		curr = curr.getNext();
        		i++;
        	}
    	}
    	if ((curr == trailer) && (i != (max-1))) { 	//
			while (i < max) {
				newest.addElement(Vind[i], Vval[i]);
				i++;
			}
    	}
    	else if ((i == max) && curr != trailer) {
    		while (curr != trailer) {
    			newest.addElement(curr.getIndex(), curr.getElement());
    			curr = curr.getNext();
    		}
    	}
    	return newest;
    }
	
	// method that inserts elements in between
    private void addBetween(int id, int e, Node<E> predecessor, Node<E> sucessor) {
    		Node<E> newest = new Node<>(id,e, predecessor,sucessor);
    		predecessor.setNext(newest);
    		sucessor.setPrev(newest);
    		nelems++;
    }
}


    
    


