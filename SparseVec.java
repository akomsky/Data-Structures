package Doubly;

public interface SparseVec<E> {
	// problem 1
		int getLength();				//return the length of the vector
		int numElements();				//return total number of nonzero elements in the vector
		int getElement(int idx);	   	//return the element at a given idx 
		void clearElement(int idx); 	//set the element at idx to zero
		void setElement(int idx, int val); 			//set the element at idx to val
		// get all nonzero indices
		int[] getAllIndices();
		// get all nonzero values
		int[] getAllValues();
		
		// methods for problem 2
		SparseVec<E> addition(SparseVec<E> otherV);		// this vector + otherV
													// return a new vector storing the result
		SparseVec<E> substraction(SparseVec<E> otherV);	// this vector - otherV
													// return a new vector storing the result
		SparseVec<E> multiplication(SparseVec<E> otherV);	// this matrix .* otherV
													// return a new vector storing the result
		void addElement(int index, int element);
		
}
