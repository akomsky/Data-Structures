import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Prob1Prob2Class {
	/**
	 * 
	 * @param num_val_list
	 * @return
	 * output_list- the final list of all possible binary numbers
	 * answer- each number that will be passed back. length is the size of num_val_list
	 * baseRule- take given num_bal_list and convert it into an array of type Integer
	 */
	// Problem 1:
	public static LinkedList< ArrayList<Integer> > Problem1(ArrayList<Integer> num_val_list){
		LinkedList< ArrayList<Integer> > output_list = new LinkedList<>();
		Integer[] answer = new Integer[num_val_list.size()];
		// fill the answer array to 0 to start it from the beginning, indicating the number zero
		Arrays.fill(answer, 0);
		Integer [] baseRules = num_val_list.toArray(new Integer[num_val_list.size()]);
		// call the baseConverter method to find all possible numbers.
		bNum(num_val_list.size(),baseRules,answer,output_list);	
		// return the completed list
		return output_list;
		
	}
	// method used to find all possible numbers with given bases
	/**
	 * 
	 * @param k - the size of the number
	 * @param b - the array of the base rules per number space
	 * @param answer - the number being passed through (The previous number)
	 * @param output_list - the final list of all numbers
	 */
	public static void bNum(int k, Integer[] b, Integer[] answer,LinkedList< ArrayList<Integer> > output_list) {
		// convert the answer given in an array to an ArrayList
		ArrayList<Integer> newAnswer = new ArrayList<Integer>(Arrays.asList(answer));
		// add the ArrayList to the final output_list
		output_list.add(newAnswer);
		// if the array has reached the maximum number, return (This is the base case)
		if (isFull(answer,k,b))
			return;
		else {
			// digitPlace is the digit place.  Start at the last digit place (which is the length (k) minus 1)
			int digitPlace = k-1;
			answer[digitPlace] = (answer[digitPlace] + 1) % b[digitPlace];
			// go to the next digit by subtracting digitPlace
			digitPlace--;
			// check all the digit places
			while (digitPlace >= 0) {
				if (carryOver(answer,k,digitPlace))
					answer[digitPlace] = (answer[digitPlace] + 1) % b[digitPlace];
			digitPlace--;
			}
		}
		// find the next number
		bNum(k,b,answer,output_list);
	}
	/**
	 * This method checks if the the digit place has reached its limit and needs
	 * to go over into the next digit place.
	 * @param array - the number
	 * @param k - the length of the number
	 * @param place - the digit place
	 * @return
	 */
	public static boolean carryOver(Integer[] array,int k,int place) {
		boolean fact = true;
		for (int i=k-1; i>place;i--){
			if (array[i]!=0)
				fact = false;
		}
		return fact;
	}
	/**
	 * This method checks if the method bNum should end by seeing if the number has reached its
	 * maximum possible number according to the base rules.
	 * @param array - the array of the number being checked
	 * @param k - the size of the number
	 * @param b - the base rule array
	 * @return
	 */
	public static boolean isFull(Integer[] array, int k, Integer[]b) {
		boolean fact = true;
		for (int i=k-1; i>=0;i--)
			if (array[i] != b[i]-1)
				fact = false;
		return fact;
	}
	/**
	 * 
	 * @param input_arr - ArrayList of the official list of numbers to check for max sub arrays
	 * @return
	 */
	public static LinkedList< ArrayList<Integer> > Problem2( ArrayList<Integer> input_arr ){
		// the official list of all maximum increasing subarrays
		LinkedList< ArrayList<Integer> > output_list = new LinkedList<>();
		// convert the ArrayList into an array of Integers
		Integer [] array = input_arr.toArray(new Integer[input_arr.size()]);
		// this will be passed through to check
		ArrayList<Integer>partialResult = new ArrayList<Integer>();
		// ArrayList of all possible numbers the subarrays can begin with
		ArrayList<Integer>allMins = findMinIndices(array);
		// for each of these minimum beginnings, find all of the maximal subarrays
		for (int i=0;i<allMins.size();i++) {
			maxsub(array,allMins.get(i),partialResult,output_list);
		}
		// return the final list
		return output_list;
	}
	/**
	 * 
	 * @param array  - the original array given
	 * @param firstIndex - where to begin
	 * @param partialResult - the passed in subarray
	 * @param output_list - the official list
	 */
	public static void maxsub(Integer[]array,int firstIndex,ArrayList<Integer>partialResult,LinkedList< ArrayList<Integer> > output_list){
		// add the number to the partial array
		partialResult.add(array[firstIndex]);
		// go through the array 
		for(int i=firstIndex;i<array.length;i++) {
			// see if the numbers are bigger and valid, if so, call the maxsub array again starting from that index
			if (array[i] > partialResult.get(partialResult.size()-1) && !thereIsNumberInbetween(array,firstIndex,i)) {
				maxsub(array,i,partialResult,output_list);
			}
			// if at the end of the array
			if ( array[i] == array[array.length-1] )  {
				// check if this is a valid maximum subarray
				// if it is NOT, then take of the last index from the subarray and continue
				if (thereIsLargerInList(array,firstIndex)){
						int lastOfPartialResult = partialResult.remove(partialResult.size()-1);
				}
				// if it IS valid, the add it to the offical list of all subarrays and then remove the last index
				else {
					output_list.add((ArrayList<Integer>) partialResult.clone());
					int lastOfPartialResult = partialResult.remove(partialResult.size()-1);
				}	
			}
		}
		//return finalList;
	}
	/**
	 * This method checks if there is a number that can go into the array between.  If there is
	 * then the subarray tested is not good.
	 * @param array
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static boolean thereIsNumberInbetween(Integer[]array,int startIndex,int endIndex) {
		for (int i=startIndex; i<endIndex; i++)
			if (array[i] > array[startIndex] && array[i] < array[endIndex])
				return true;
		return false;
	}
	/**
	 * This method checks if there is are possible numbers in the array after the given index that can
	 * fit in the array, which would be the subarray being checked NOT maximal.
	 * @param array
	 * @param valueIndex
	 * @return
	 */
	public static boolean thereIsLargerInList(Integer[] array,int valueIndex) {
		for (int i=valueIndex;i<array.length;i++)
			if (array[i] > array[valueIndex])
				return true;;
		return false;
	}
	/**
	 * This method will find all possible numbers that the subarray can begin with
	 * @param array
	 * @return
	 */
	public static ArrayList<Integer> findMinIndices(Integer[] array) {
		ArrayList<Integer> mins = new ArrayList<Integer>();
		int min = array[0];
		mins.add(0);
		for (int i=0; i<array.length;i++){
			if (array[i] < min){
				min = array[i];
				mins.add(i);
			}
		}
		return mins;
	}
}
