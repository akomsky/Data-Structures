import java.util.*;

public class HW2MainClass {

			
	public static void main(String[] args) {
		try{
			int n = args.length;
			if(n < 2){
				System.out.println("java HW2MainClass INPUT_P1/INPUT_P2/INPUT_P3/TEST_P1/TEST_P2/TEST_P3 FNAME");
				return;
			}
			
			String opt = args[0];	// option
			String fname = args[1];	// input file (prefix or full, depends on the options)
			
			MyRandGeneratorClass myrnd = new MyRandGeneratorClass(100);
			// use the constructor without argument to generate complete random input files 
			// MyRandGeneratorClass myrnd = new MyRandGeneratorClass();

			if(opt.equals("INPUT_P1")){
				int num_files = (n == 3) ? Integer.parseInt(args[2]) : 10;
				
				for(int i = 1; i <= num_files; ++i){
					String fname_new = fname + "-" + Integer.toString(i) + ".txt";
					
					// generating random positive int array (allow repeating) as an input file for P1
					int len = myrnd.GenerateRandInt(5, 10);
					ArrayList<Integer> input_arr = myrnd.GenerateRandIntSequence(len, 1, 4);
					IOUtilsClass.printV(input_arr, fname_new);
				}
			}else if(opt.equals("INPUT_P2")){
				int num_files = (n == 3) ? Integer.parseInt(args[2]) : 10;

				for(int i = 1; i <= num_files; ++i){
					String fname_new = fname + "-" + Integer.toString(i) + ".txt";
					
					// generating random positive int array (no repeating) as an input file for P2
					int len = myrnd.GenerateRandInt(5, 20);
					ArrayList<Integer> input_arr = myrnd.GenerateRandDistinctIntSequence(len, -100, 100);
					IOUtilsClass.printV(input_arr, fname_new);
				}
			}else if(opt.equals("INPUT_P3")){
				int num_files = (n == 3) ? Integer.parseInt(args[2]) : 10;

				for(int i = 1; i <= num_files; ++i){
					String fname_new = fname + "-" + Integer.toString(i) + ".txt";
					
					// generating random positive int array (no repeating) as an input file for P3
					int len = myrnd.GenerateRandInt(5, 15);
					ArrayList<Integer> input_arr = myrnd.GenerateRandDistinctIntSequence(len, -100, 100);
					IOUtilsClass.printV(input_arr, fname_new);
					int tol = myrnd.GenerateRandInt(0, 3);
					IOUtilsClass.addLine("TOL " + Integer.toString(tol), fname_new);
				}
			}else if(opt.equals("TEST_P1")){
				ArrayList<Integer> input_arr = IOUtilsClass.ParseP1P2(fname);
				IOUtilsClass.printV_with_prefix(input_arr, "INPUT:");
				LinkedList< ArrayList<Integer> > ret = Prob1Prob2Class.Problem1(input_arr);
				
			    for (ArrayList<Integer> one_output : ret) 
			        IOUtilsClass.printV_with_prefix(one_output, "OUTPUT:");

			}else if(opt.equals("TEST_P2")){
				ArrayList<Integer> input_arr = IOUtilsClass.ParseP1P2(fname);
				IOUtilsClass.printV_with_prefix(input_arr, "INPUT:");
				LinkedList< ArrayList<Integer> > ret = Prob1Prob2Class.Problem2(input_arr);
				
			    for (ArrayList<Integer> one_output : ret) 
			        IOUtilsClass.printV_with_prefix(one_output, "OUTPUT:");

			}else if(opt.equals("TEST_P3")){
				int[] tol = {Integer.MIN_VALUE};
				ArrayList<Integer> input_arr = IOUtilsClass.ParseP3(fname, tol);
				IOUtilsClass.printV_with_prefix(input_arr, "INPUT:");
				System.out.println("INPUT: Tolerance = " + Integer.toString(tol[0]));
				
				BinaryTreeClass tree = new BinaryTreeClass();
				LinkedList< ArrayList<Integer> > ret = tree.Problem3(input_arr, tol[0]);
			    for (ArrayList<Integer> one_output : ret) 
			        IOUtilsClass.printV_with_prefix(one_output, "OUTPUT:");
			    
			}else{
				System.out.println("The first argument has to be INPUT_P* or TEST_P*");
				return;
			}
	    } catch (Exception e) {
	    	System.out.println("OUTPUT: NULL Something is wrong");
	        return;
	    }		
		
/*								
		System.out.println("HW 2");

		int [] tmparray = {1, 2, 3, 4};
////		LinkedList< int[] > ret = Problem1(tmparray);
//		LinkedList< int[] > ret = Problem2_Easy(tmparray);
////		ListIterator< int[] > myiter = ret.listIterator();
////		while( myiter.hasNext() ){
//		for(int [] tmpV : ret ){
//			printV(tmpV);
////			int [] tmpV = myiter.next();
//		}
		
//        WebTreeClass tree = new WebTreeClass();
//        Vector<Node> trees = tree.getTrees(tmparray, 0, tmparray.length-1);
//        System.out.println("Preorder traversal of different "+
//                           " binary trees are:");
//        for (int i = 0; i < trees.size(); i++) {
//            tree.preOrder(trees.get(i));
//            System.out.println("");
//        }		
        
        BinaryTreeClass mytree = new BinaryTreeClass();
        ArrayList<ArrayList<Integer> > output_list = 
        		mytree.ConstructFromInorderAll(tmparray, 2);
        for (ArrayList<Integer> one_output : output_list) {
        	printV(one_output);
        }		        
*/	
	}

}
