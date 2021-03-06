package org.generallib.possiblity.utils;

import java.util.Random;

public class PossibilityCalculator {
/*	public static void main(String[] ar){
		double possibility = getReciprocalPossibility(0, 1);
		System.out.println("getReciprocalPossibility(0, "+3+") : "+possibility);
		
		for(int x = 0; x < 5; x++){
			int win = 0,lose = 0;
			for(int i = 0; i < 1000; i++){
				if(isWin(possibility, 4))
					win++;
				else
					lose++;
			}
			
			System.out.println("win: "+win);
			System.out.println("lose: "+lose);
		}

	}*/
	
	/**
	 * 1/(2(level - vert))
	 * 
	 * @param level
	 * @return 1.0 if x - vert <= 0; 1/(2^(level - vert)) otherwise
	 */
	public static double getReciprocalPossibility(int vertAsymtote, int level){
		int base = level - vertAsymtote;
		
		if(base > 0){
			return 1.0D/(pow(2, base));
		}else{
			return 1.0D;
		}
	}
	
	private static final Random rand = new Random();
	/**
	 * 
	 * @param possiblity possibility to test 0 ~ 1
	 * @param decimals limit of decimals (for example, decimals 2 means (int)(possibility * 10^2))
	 * @return true if win; false if not
	 */
	public static boolean isWin(double possiblity, int decimals){
		int multiplier = pow(10, decimals);
		double range = (int)Math.round(possiblity * multiplier);
		
		int numTest = rand.nextInt(multiplier);
		
		//check if the random num is 0 out of possible numbers
		return numTest < range;
	}
	
	/**
	 * 
	 * @param num
	 * @param n
	 * @return num ^ n
	 */
	private static int pow(int num, int n){
		if(n == 0)
			return 1;
		
		int sum = num;
		for(int i = 1; i < n; i++){
			sum *= num;
		}
		return sum;
	}
}
