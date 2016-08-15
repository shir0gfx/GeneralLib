package org.generallib.math.expression;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.PatternSyntaxException;

public class ParsableCalculator {
	public static void main(String[] ar){
		ParsableCalculator calc = new ParsableCalculator();
		
		Scanner sc = new Scanner(System.in);
		while(true){
			try{
				System.out.print("Calc? >> ");
				String input = sc.nextLine();
				System.out.println("Result: "+calc.parse(input));
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
	
	private static final Map<Character, Operation> operations = new HashMap<Character, Operation>(){{
		put('+', new Operation() {@Override public double calc(double num1, double num2) {return num1 + num2;}});
		put('-', new Operation() {@Override public double calc(double num1, double num2) {return num1 - num2;}});
		put('*', new Operation() {@Override public double calc(double num1, double num2) {return num1 * num2;}});
		put('/', new Operation() {@Override public double calc(double num1, double num2) {return num1 / num2;}});
		put('^', new Operation() {@Override public double calc(double num1, double num2) {return Math.pow(num1, num2);}});
		put('%', new Operation() {@Override public double calc(double num1, double num2) {return num1 % num2;}});
	}};
	private static final Map<Character, Integer> precedence = new HashMap<Character, Integer>(){{
		put('+', 2);
		put('-', 2);
		put('*', 3);
		put('/', 3);
		put('^', 4);
		put('%', 3);
	}};
	
	private final Map<String, DecimalReplacer> replaces = new HashMap<String, DecimalReplacer>();
	public void registerPlaceHolder(String name, DecimalReplacer rep){
		replaces.put(name, rep);
	}

	private static Stack<String> stack = new Stack<String>();
	/**
	 * 
	 * @param input String to parse
	 * @return result
	 * @throws PatternSyntaxException throw if syntax error found.
	 * @throws NumberFormatException throw if invalid number in the string.
	 */
	public double parse(String input){
		Queue<String> output = eval(input);

		while(!output.isEmpty()){
			String out = output.poll();
			
			if(out.length() == 1 && operations.containsKey(out.charAt(0))){
				char op = out.charAt(0);
				double num2 = Double.parseDouble(stack.pop());
				double num1 = Double.parseDouble(stack.pop());
				stack.push(String.valueOf(operations.get(op).calc(num1, num2)));
			}else{
				stack.push(out);
			}
		}
		
		return Double.parseDouble(stack.pop());
	}
	
	private Stack<Character> operators = new Stack<Character>();
	private Queue<String> output = new LinkedList<String>();
	private Queue<String> eval(String input){
		//clean up
		operators.clear();
		output.clear();		
		//remove white spaces
		input = input.toLowerCase().replaceAll("[ \\t]", "");
		
		//replace place holders
		for(Entry<String, DecimalReplacer> entry : replaces.entrySet()){
			if(entry.getValue().replace() >= 0.0D){
				input = input.replaceAll(entry.getKey(), String.valueOf(entry.getValue().replace()));
			}else{
				input = input.replaceAll(entry.getKey(), "(0"+String.valueOf(entry.getValue().replace()+")"));
			}
		}
		//char array
		char[] chars = input.toCharArray();
		
		String numStr = "";
		for(int index = 0; index < chars.length; index++){
			char c = chars[index];
			
			//digit or num
			if(Character.isDigit(c) || c == '.'){
				numStr += c;
				continue;
			}
			
			//push previously built number
			if(!numStr.equals("")){
				output.add(numStr);
				numStr = "";
			}
			
			if(c == '('){
				operators.push(c);
			}else if(c == ')'){
				char operator;
				while((operator = operators.pop()) != '('){
					output.add(String.valueOf(operator));
				}
			}else if(operations.containsKey(c)){
				if(output.isEmpty())
					throw new PatternSyntaxException("Cannot use sign infront of number. Use parenthesis ex) (0 - 5)", input, index);
				
				if(operators.isEmpty()){
					operators.push(c);
				}else if(operators.peek() == '('){
					operators.push(c);
				}else if(operators.peek() == '^'){
					//right to left
					operators.push(c);
				}else{
					int precBefore = precedence.get(operators.peek());
					int precAfter = precedence.get(c);
					
					//pop operator and push to output
					if(precAfter <= precBefore){
						output.add(String.valueOf(operators.pop()));
					}
					
					operators.push(c);
				}
			}else{
				//syntax
				throw new PatternSyntaxException("Unrecognized character", input, index);
			}
		}
		
		//push previously built number
		if(!numStr.equals("")){
			output.add(numStr);
			numStr = "";
		}
		
		while(!operators.isEmpty()){
			output.add(String.valueOf(operators.pop()));
		}
		
		return output;
	}
	
	public interface Operation{
		double calc(double num1, double num2);
	}
	
	public interface DecimalReplacer{
		double replace();
	}
}
