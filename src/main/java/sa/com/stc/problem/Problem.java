package sa.com.stc.problem;

public class Problem {
	public static String produceString(String input) {
		
		StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < input.length(); i++) {
			
			char current = input.charAt(i);
			result.append(current);
			if (current == '(') {
				i++;
				StringBuilder temp = new StringBuilder();
				while (i < input.length() && input.charAt(i) != ')') {
					 
					temp.append(input.charAt(i));
					i++;
					
				}
				
				StringBuilder reversed = temp.reverse();
				result.append(reversed);
				result.append(input.charAt(i));
				
			}
			
		}
		
		return result.toString();
	
	
		
		
	
	}
	public static void main(String[] args) {
		
		

		System.out.println(produceString("abd(jnb)"));
		
	}
}
