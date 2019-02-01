import java.util.Stack;
import java.util.StringTokenizer;

public class Calculator {	
	private final String tokens="+-*/";	
	private Calculator(){}
	
	public static Calculator instance = null;
	public static Calculator getInstance(){
		if(instance == null)
			instance = new Calculator();
		return instance;
	}
	
	//������ -> ���������� ��ȯ �� ���
	public double calculate(String infixData) throws Exception{		
		String[] postfixData = changeToPostFix(infixData);		
		double result=0;
		Stack<String> stack=new Stack<String>();
		for(int i=0;i<postfixData.length;i++){
			String nowStr=postfixData[i];
			if(tokens.contains(nowStr)){ //�����ڸ�
				double num1=Double.parseDouble(stack.pop());
				double num2=Double.parseDouble(stack.pop());
				double res=0;
				switch(nowStr){				
				case "*":
					res=num2*num1;
					break;
				case "/":
					res=num2/num1;
					break;					
				case "+":
					res=num2+num1;
					break;
				case "-":
					res=num2-num1;
					break;				
				}			
				stack.push(String.valueOf(res));				
			}else{ //�����̸�
				stack.push(nowStr);				
			}
		}
		result=Double.parseDouble(stack.pop());
		return result;
	}
	
	//�������� ���������� �ٲ���
	private String[] changeToPostFix(String infixData) throws Exception {		
		Stack<String> stack=new Stack<String>();		
		StringTokenizer st=new StringTokenizer(infixData,tokens,true);
		String[] postfixData=new String[st.countTokens()];				
		int idx=0;
		//���� ����
		while(st.hasMoreTokens()){
			String nowStr=st.nextToken();
			if(tokens.contains(nowStr)){ //������ �̸�
				if(stack.isEmpty()){ //stack�� ���������
					stack.push(nowStr);
				}else{ //stack�� �����Ͱ� �����ϸ�
					String compStr=stack.peek();
					if(getOpPrec(nowStr)>getOpPrec(compStr)){ //�켱 ������ ������
						stack.push(nowStr);
					}else{ //�켱 ������ ������
						while(!stack.isEmpty()){
							postfixData[idx++]=stack.pop();
						}
					}
				}				
			}else{ //�����̸�
				postfixData[idx++]=nowStr;
			}
		}		
		while(!stack.isEmpty()){
			postfixData[idx++]=stack.pop();
		}		
		return postfixData;		
	}	
	//������ �켱����
	private static int getOpPrec(String op){ 
		switch(op){
		case "*":
		case "/":
			return 3;
		case "+":
		case "-":
			return 1;
		}		
		return -1;		
	}	
}

