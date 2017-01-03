package Doubly;
// implementing SparseM using SinglyLinkedList
public class LLSparseM<E> implements SparseM<E> {
	//----- nested ElemNode class ------
	private static class ElemNode<E> {
		private int ridx;
		private int cidx;
		private int element;
		private ElemNode<E> nextInTheRow;
		private ElemNode<E> nextInTheCol;
		public ElemNode(int rID, int cID, int e, ElemNode<E> NR, ElemNode<E> NC){
			ridx = rID;
			cidx = cID;
			element = e;
			nextInTheRow = NR;
			nextInTheCol = NC;
		}
		public int getElement() { return element; }
		public int getRidx() { return ridx; }
		public int getCidx() { return cidx; }
		public ElemNode<E> getNextInRow() { return nextInTheRow; }
		public ElemNode<E> getNextInCol() { return nextInTheCol; }
		public void setElement(int e) { element = e; }
		public void setNextInRow(ElemNode<E> nr) { nextInTheRow = nr;}
		public void setNextInCol(ElemNode<E> nc) { nextInTheCol = nc;}
	} 
	// -------- end of nested ElemNode class
	
	//----- nested RowHead class ------
	private static class RowHead<E> {
		private int ridx;
		private RowHead<E> nextRhead;
		private ElemNode<E> firstElem; // first non zero element 
		public RowHead(int rid, RowHead<E> nextRH, ElemNode<E> fe) {
			ridx = rid;
			nextRhead = nextRH;
			firstElem = fe;
		}
		public RowHead<E> getNextRowHead() { return nextRhead;}	
		public void setNextRowHead(RowHead<E> rh) { nextRhead = rh;}
		public ElemNode<E> getFirstElem() { return firstElem;}
		public void setFirstElem(ElemNode<E> first) { firstElem = first;}
		public int getRidx() { return ridx;}
		
	}
	// -------- end of nested RowHead class
	
	//----- nested ColHead class ------
	private static class ColHead<E> {
		private int cidx;
		private ColHead<E> nextChead;
		private ElemNode<E> firstElem;
		public ColHead(int cid, ColHead<E> nextCH, ElemNode<E> fe) {
			cidx = cid;
			nextChead = nextCH;
			firstElem = fe;
		}
		public ColHead<E> getNextColHead() { return nextChead;}
		public void setNextColHead(ColHead<E> ch) {nextChead = ch;}
		public ElemNode<E> getFirstElem() { return firstElem; }
		public int getCidx() { return cidx;}
		public void setFirstElem(ElemNode<E> first) { firstElem = first;}
	}
	// -------- end of nested ColHead class
	
	private RowHead<E> Rhead = null;
	private ColHead<E> Chead = null;
	private int nrows = 0;
	private int ncols = 0;
	private int size = 0;
	private int nelems = 0;
	private int nrowHeads = 0;
	private int ncolHeads = 0;
	
	// constructor initialize a sparse matrix with nr rows and nc columns
	public LLSparseM(int nr, int nc) {
		Rhead = new RowHead<>(-1,null,null);
		Chead = new ColHead<>(-1,null,null);
		nrows = nr;
		ncols = nc;
		nelems = 0;
	}
	// get number of rows
	public int nrows() {
		return nrows;
	}
	// get number of columns
	public int ncols() {
		return ncols;
	}
	// get number of nonzero entries
	public int numElements() {
		return nelems;
	}
	
	// get element at (ridx,cidx).  If out of bounds, return -1;
	public int getElement(int ridx, int cidx) {
		if ( (ridx < 0) || (ridx>= nrows) || (cidx < 0) || (cidx>=ncols))
			return -1;
		RowHead<E> Rcurr = Rhead;
		while ( (Rcurr != null) && (Rcurr.getRidx() != ridx)  ) {
			Rcurr = Rcurr.nextRhead;
		}
		if (Rcurr == null) {
			return 0;
		}
		else {
			ElemNode<E> currElement = Rcurr.getFirstElem();
			while ( (currElement != null) && (currElement.getCidx() != cidx) ) {
					currElement = currElement.nextInTheCol;
			}
			if (currElement == null)
				return 0;
			return currElement.getElement();
		}
	}

	public void clearElement(int ridx, int cidx) {
		RowHead<E> Rcurr = Rhead;
		while ( (Rcurr != null) && (Rcurr.getRidx() != ridx) ) {
			Rcurr = Rcurr.nextRhead;
		}
		if (Rcurr == null)
			return;
		ElemNode<E> currElement = Rcurr.getFirstElem();
		ElemNode<E> prevElement = null;
		while ( (currElement != null) && (currElement.getCidx() != cidx) ) {
			prevElement = currElement;
			currElement = currElement.nextInTheRow;
		}
		if (currElement == null)
			return;
		// adjust pointer of row to remove the element
		if (prevElement == null) // this means currElement is first element in the row
			Rcurr.setFirstElem(currElement.nextInTheRow);
		else 
			prevElement.setNextInRow(currElement.nextInTheRow);
		
		// adjust pointer of col to remove the element
		ColHead<E> Ccurr = Chead;
		while (Ccurr.getCidx() != cidx) {  // we know the element exists because of previous check
			Ccurr = Ccurr.nextChead;
		}
		ElemNode<E> currElem = Ccurr.getFirstElem();
		ElemNode<E> prevElem = null;
		while (currElem.getRidx() != ridx) {
			prevElem = currElem;
			currElem = currElem.nextInTheCol;
		}
		// fix pointers 
		if (prevElem == null)
			Ccurr.setFirstElem(currElem.nextInTheCol);
		else
			prevElem.setNextInCol(currElem.nextInTheCol);
		nelems--;
	}
	// set the element at (ridx,cidx) to val. if ridx or cidx is out of bounds, just return.
	public void setElement(int ridx, int cidx, int val) {
		if ((ridx<0)||(ridx>=nrows)||(cidx<0)||(cidx>=ncols))
			return;
		if (val == 0) {
			clearElement(ridx, cidx);
			return;
		}
		ElemNode<E> newElement = new ElemNode<>(ridx,cidx, val, null,null);
		if (nelems == 0) {
			Rhead = new RowHead<E>(ridx, null, newElement);
			nrowHeads++;
			Chead = new ColHead<E>(cidx, null, newElement);
			ncolHeads++;
			nelems++;
			return;
		}
		nelems++;
		// find the row
		RowHead<E> Rcurr = Rhead;
		RowHead<E> Rprev = null;
		while ( (Rcurr!=null) && (Rcurr.getRidx()!=ridx) && (Rcurr.getRidx()< ridx) ) {
			Rprev = Rcurr;
			Rcurr = Rcurr.nextRhead;
		}
		// you get to the end of the rowHeads and it does not exist
		if (Rcurr == null) {
			RowHead<E> newRowHead = new RowHead<>(ridx,null,newElement);
			nrowHeads++;
			Rprev.setNextRowHead(newRowHead);
		}
		// the row exists already
		else if (Rcurr.getRidx() == ridx) {
			ElemNode<E> currElem = Rcurr.getFirstElem();
			ElemNode<E> prevElem = null;
			while (currElem!= null && currElem.getCidx()!=cidx && currElem.getCidx()< cidx) {
				prevElem = currElem;
				currElem = currElem.nextInTheRow;
			}
			if (currElem == null) {
				prevElem.setNextInRow(newElement);
			}
			else if (currElem.getCidx()==cidx) 
				currElem.setElement(val);
			else {
				newElement.setNextInRow(currElem);
				// check to see if need to change "firstElemNode" pointer
				if (Rcurr.getFirstElem()==currElem)
					Rcurr.setFirstElem(newElement);
				else 
					prevElem.setNextInRow(newElement);
			}
		}
		// row head does not exist and the next row head is greater than the new one 
		else {
			RowHead<E> newRowHead = new RowHead<>(ridx,Rcurr,newElement );
			nrowHeads++;
			if (Rhead == Rcurr) //this means the newest RowHead is the first (new Rhead)
				Rhead = newRowHead;
			else 
				Rprev.setNextRowHead(newRowHead);
		}
		// Now need to correct the column pointers:
		ColHead<E> Ccurr = Chead;
		ColHead<E> Cprev = null;
		while ( (Ccurr!=null) && (Ccurr.getCidx()!=cidx) && (Ccurr.getCidx()< cidx) ) {
			Cprev = Ccurr;
			Ccurr = Ccurr.nextChead;
		}
		if (Ccurr==null) {
			ColHead<E> newColHead = new ColHead<>(cidx,null,newElement);
			ncolHeads++;
			Cprev.setNextColHead(newColHead);
		}
		else if (Ccurr.getCidx()==cidx) {
			ElemNode<E> currElem = Ccurr.getFirstElem();
			ElemNode<E> prevElem = null;
			while (currElem!=null && currElem.getRidx() < ridx) {
				prevElem = currElem;
				currElem = currElem.nextInTheCol;
			}
			if (currElem == null) {
				prevElem.setNextInCol(newElement);
			}
			else {
				newElement.setNextInCol(currElem);
				if (Ccurr.getFirstElem()==currElem)
					Ccurr.setFirstElem(newElement);
				else prevElem.setNextInCol(newElement);
			}
		}
//		Ccurr has col index greater than cidx
		else {
			ColHead<E> newColHead = new ColHead<>(cidx,Ccurr,newElement);
			ncolHeads++;
			if (Chead == Ccurr)
				Chead = newColHead;
			else
				Cprev.setNextColHead(newColHead);
		}
	}

	public int[] getRowIndices() {
		RowHead<E> curr = Rhead;
		int i = 0;
		int [] RowInd = new int[nrowHeads];
		if (nrowHeads == 0) {
			return RowInd;
		}
		while (curr != null) {
			RowInd[i] = curr.getRidx();
			curr = curr.getNextRowHead();
			i++;
		}
		return RowInd;
	}

	public int[] getColIndices() {
		ColHead<E> curr = Chead;
		int i = 0;
		int [] ColHeadInd = new int[ncolHeads];
		if (ncolHeads == 0) {
			return ColHeadInd;
		}
		while (curr != null) {
			ColHeadInd[i] = curr.getCidx();
			curr = curr.getNextColHead();
			i++;
		}
		return ColHeadInd;
	}
	
	public int[] getOneRowColIndices(int ridx) {
		RowHead<E> curr = Rhead;
		int i = 0;
		int lengthOfRow = 0;
		if (ncolHeads == 0) {
			int[] EmptyOneRowColInd = new int[ncolHeads];
			return EmptyOneRowColInd;
		}
		while (curr.getRidx() != ridx) {
			curr = curr.getNextRowHead();
		}
		ElemNode<E> newCurr = curr.getFirstElem();
		while (newCurr != null) {
			lengthOfRow++;
			newCurr = newCurr.nextInTheRow;
		}
		int[] OneRowColInd = new int[lengthOfRow];
		newCurr = curr.getFirstElem();
		while (newCurr != null) {
			OneRowColInd[i] = newCurr.getCidx();
			newCurr = newCurr.nextInTheRow;
			i++;
		}
		return OneRowColInd;
	}

	public int[] getOneRowValues(int ridx) {
			RowHead<E> curr = Rhead;
			int i = 0;
			int[] OneRowVal = new int[ncolHeads];
			if (nelems == 0) {
				return OneRowVal;
			}
			while (curr.getRidx() != ridx) {
				curr = curr.getNextRowHead();
			}
			ElemNode<E> newCurr = curr.getFirstElem();
			while (newCurr != null) {
				OneRowVal[i] = newCurr.getElement();
				newCurr = newCurr.nextInTheRow;
				i++;
			}
			return OneRowVal;
	}

	public int[] getOneColValues(int cidx) {
			ColHead<E> curr = Chead;
			int i = 0;
			int[] OneColVal = new int[nrowHeads];
			if (nelems == 0) {
				return OneColVal;
			}
			while (curr.getCidx() != cidx) {
				curr = curr.getNextColHead();
			}
			ElemNode<E> newCurr = curr.getFirstElem();
			while (newCurr != null) {
				OneColVal[i] = newCurr.getElement();
				newCurr = newCurr.nextInTheCol;
				i++;
			}
			return OneColVal;
	}
	public int[] getOneColRowIndices(int cidx) {
		ColHead<E> curr = Chead;
		int i = 0;
		int lengthOfCol = 0;
		if (nrowHeads == 0) {
			int[] EmptyOneColRowInd = new int[nrowHeads];
			return EmptyOneColRowInd;
		}
		while (curr.getCidx() != cidx) {
			curr = curr.getNextColHead();
		}
		ElemNode<E> newCurr = curr.getFirstElem();
		while (newCurr != null) {
			lengthOfCol++;
			newCurr = newCurr.nextInTheCol;
		}
		int[] OneColRowInd = new int[lengthOfCol];
		newCurr = curr.getFirstElem();
		while (newCurr != null) {
			OneColRowInd[i] = newCurr.getRidx();
			newCurr = newCurr.nextInTheCol;
			i++;
		}
		return OneColRowInd;
}
	///////////////////////////////////

	@Override
	public SparseM addition(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SparseM substraction(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SparseM multiplication(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}

}

