package Doubly;

public interface SparseM<E> {
	// problem 3
	int nrows();							//return number of rows 
	int ncols();							//return number of columns
	int numElements();						//return total number of nonzero elements in the matrix
	int getElement(int ridx, int cidx);	   	//return the element at a given entry (ridx, cidx), 
	void clearElement(int ridx, int cidx); 	//set the element at (ridx,cidx) to zero
	void setElement(int ridx, int cidx, int val); 			//set the element at (ridx,cidx) to val
	// get indices of non-empty rows, sorted
	int[] getRowIndices();
	// get indices of non-empty cols, sorted
	int[] getColIndices();			
	// get col indices of a given row
	int[] getOneRowColIndices(int ridx);
	// get values of a given row
	int[] getOneRowValues(int ridx);
	// get col indices of a given col
	int[] getOneColRowIndices(int cidx);
	// get values of a given Col
	int[] getOneColValues(int cidx);


	// methods for problem 4
	SparseM addition(SparseM otherM);			// this matrix + otherM
												// return a new matrix storing the result
	SparseM substraction(SparseM otherM);		// this matrix - otherM
												// return a new matrix storing the result
	SparseM multiplication(SparseM otherM);		// this matrix .* with otherM
												// return a new matrix storing the result
}
