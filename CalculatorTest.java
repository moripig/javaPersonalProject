package project;

import javax.swing.*;

import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;

public class CalculatorTest extends JFrame implements ActionListener, KeyListener {

	final int MAX_INPUT_LENGTH = 16;	// 입력 가능한 길이 제한
	
	int display_State;	// 결과창에 출력할 때의 각 상태에 숫자를 부여하여 구분
	final int State_Input = 0;
	final int State_Result = 1;
	final int State_Error = 2;
	
	boolean nextInputClear; 	// 화면에 표시될 숫자를 지울지 말지 결정
	
	double lastNumber;			// 마지막으로 입력됐던 수
	String lastOperator;		// 마지막에 누른 연산자
	
	private JLabel output;		// 현재 숫자 또는 결과가 출력되는 공간
	private JButton buttons[];	// 버튼 역할을 할 배열 생성
	private JPanel panelMaster, panelButton, panelRemove;	// 버튼과 레이블을 배치할 공간
	
	public CalculatorTest(){
		
		this.getContentPane().setBackground(Color.lightGray);   //프레임의 배경은 회색
		
		panelMaster = new JPanel();
		
		output = new JLabel("0", JLabel.RIGHT);	// 레이블의 초기 값은 0
		output.setBackground(Color.lightGray);
		output.setOpaque(false);
		output.setFont(new Font("", Font.BOLD, 30));
		
		buttons = new JButton[23];
		
		panelButton = new JPanel();
		
		// 계산기 0~9 숫자버튼 생성
		for (int i=0 ; i<=9 ; i++){
			buttons[i] = new JButton(String.valueOf(i));			
		}
		
		// 연산에 필요한 버튼 생성
		buttons[10] = new JButton("±");
		buttons[11] = new JButton(".");
		buttons[12] = new JButton("=");
		buttons[13] = new JButton("÷");
		buttons[14] = new JButton("×");
		buttons[15] = new JButton("-");
		buttons[16] = new JButton("+");
		buttons[17] = new JButton("√");
		buttons[18] = new JButton("1/x");
		buttons[19] = new JButton("%");
		
		// 초기화 또는 입력 취소에 필요한 버튼 생성
		buttons[20] = new JButton("<--");
		buttons[21] = new JButton("CE");
		buttons[22] = new JButton("C");
		
		// 각 버튼의 디자인 결정
		for(int i=0 ; i<buttons.length ; i++){
			if(i<10){
				buttons[i].setForeground(Color.white);	// 숫자 
				buttons[i].setBackground(Color.gray);
				buttons[i].setFont(new Font("", Font.BOLD, 30));
			}else if(i>=10 && i<21){
				buttons[i].setForeground(Color.gray);	// 연산자
				buttons[i].setBackground(Color.orange);
				buttons[i].setFont(new Font("", Font.BOLD, 20));
			}else{
				buttons[i].setForeground(Color.white);	// 컨트롤 
				buttons[i].setBackground(Color.black);
				buttons[i].setFont(new Font("", Font.BOLD, 15));
			}
			buttons[12].setForeground(Color.white);	// = 버튼만 따로 
			buttons[12].setBackground(Color.blue);
			buttons[12].setFont(new Font("", Font.BOLD, 30));
		}
		
		// 숫자와 연산자 배치하기
		panelButton.setLayout(new GridLayout(4,5,5,5));
		
		for(int i=7; i<=9 ; i++){
			panelButton.add(buttons[i]);	// 7에서 9까지
		}
		
		panelButton.add(buttons[13]);	// 나누기
		panelButton.add(buttons[17]);	// 루트
		
		for(int i=4; i<=6 ; i++){
			panelButton.add(buttons[i]); 	// 4에서 6까지
		}
		
		panelButton.add(buttons[14]);	// 곱하기
		panelButton.add(buttons[18]);	// 1/x
		
		for(int i=1; i<=3 ; i++){
			panelButton.add(buttons[i]);	// 1에서 3까지
		}
		
		panelButton.add(buttons[15]);	// 빼기
		panelButton.add(buttons[19]);	// 퍼센트
		
		panelButton.add(buttons[10]);	// 0
		panelButton.add(buttons[0]);	// ±
		panelButton.add(buttons[11]);	// 소수점
		panelButton.add(buttons[16]);	// 더하기
		panelButton.add(buttons[12]);	// =
		
		// 백스페이스 및 초기화 버튼 배치
		panelRemove = new JPanel();
		panelRemove.setLayout(new GridLayout(1,2,2,2));
		
		panelRemove.add(buttons[20]);	// 백스페이스
		panelRemove.add(buttons[21]);	// CE
		panelRemove.add(buttons[22]);	// C
		
		
		// 삭제 버튼들과 숫자 및 연산자 버튼 배치함
		panelMaster.setLayout(new BorderLayout());
		panelMaster.add(panelRemove, BorderLayout.NORTH);	// 백스페이스와 초기화를 버튼 상단에 배치
		panelMaster.add(panelButton, BorderLayout.SOUTH);	// 숫자와 연산자를 그 아래에 배치
		
		getContentPane().add(output, BorderLayout.NORTH);
		getContentPane().add(panelMaster,BorderLayout.SOUTH);
		requestFocus();
		
		// ActionListener, KeyListener 가 맨 처음에 설정했던 최대 입력제한까지 숫자를 입력받도록 함
		for(int i=0 ; i<buttons.length ; i++){
			buttons[i].addActionListener(this);
			buttons[i].addKeyListener(this);
		}
				
		clearAll(); // 초기화
		
		addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e){ // 계산기 종료시 프로세스 종료
					System.exit(0);
				}
			}
		);		
	}
	
	//마우스 입력 관련 메소드
	@Override
	public void actionPerformed(ActionEvent e){
		
		double result = 0;
		
		for(int i=0 ; i<buttons.length ; i++){
			if(e.getSource()==buttons[i]){
				if(i<10){
					addToDisplay(i);
					break;
				}else{
					switch(i){
						case 10: // ±
							processSingChange();
							break;
						case 11: // .
							addPoint();
							break;
						case 12: // =
							processEquals();
							break;
						case 13: // /
							processOperator("/");
							break;
						case 14: // *
							processOperator("*");
							break;
						case 15: // -
							processOperator("-");
							break;
						case 16: // +
							processOperator("+");
							break;
						case 17: // √
							if (display_State != State_Error)
							{
							   try
								{
									if (getDisplayString().indexOf("-") == 0)
									    displayError("Invalid input for function!");

									result = Math.sqrt(getNumberInDisplay());
									displayResult(result);
								}

								catch(Exception ex)
								{
									displayError("Invalid input for function!");
									display_State = State_Error;
								}
							}
							break;
						case 18: // 1/x
							if (display_State != State_Error){
								try
								{
									if (getNumberInDisplay() == 0)
										displayError("0으로 나눌 수 없습니다.");
		
									result = 1 / getNumberInDisplay();
									displayResult(result);
								}
								
								catch(Exception ex)	{
									displayError("잘못된 입력입니다!");
									display_State = State_Error;
								}
							}
							break;
						case 19: // %
							if (display_State != State_Error){
								try	{
									result = getNumberInDisplay() / 100;
									displayResult(result);
								}
		
								catch(Exception ex)	{
									displayError("잘못된 입력입니다!");
									display_State = State_Error;
								}
							}
							break;
						case 20:	// 백스페이스
							backspace();
							break;
						case 21: // 현재 숫자 지움
							setDisplayString("0");
							nextInputClear = true;
							display_State = State_Input;
							break;
						case 22: // 완전 초기화
							clearAll();
							break;							
					}
				}	
			}
		}
	}
	
	
	// 키보드 숫자패드 입력
	public void keyPressed(KeyEvent e){

		int keycode = e.getKeyChar();

		switch(keycode){
			case KeyEvent.VK_0:
				addToDisplay(0);
				break;
			case KeyEvent.VK_1:
				addToDisplay(1);
				break;
			case KeyEvent.VK_2:
				addToDisplay(2);
				break;
			case KeyEvent.VK_3:
				addToDisplay(3);
				break;
			case KeyEvent.VK_4:
				addToDisplay(4);
				break;
			case KeyEvent.VK_5:
				addToDisplay(5);
				break;
			case KeyEvent.VK_6:
				addToDisplay(6);
				break;
			case KeyEvent.VK_7:
				addToDisplay(7);
				break;
			case KeyEvent.VK_8:
				addToDisplay(8);
				break;
			case KeyEvent.VK_9:
				addToDisplay(9);
				break;
			case 46: // .
				addPoint();				
				break;
			case 10: // =
				processEquals();
				break;
			case 47: // /
				processOperator("/");
				break;
			case 42: // *
				processOperator("*");
				break;
			case 43: // +
				processOperator("+");
				break;
			case 45: // -
				processOperator("-");
				break;
			case 8: // 백스페이스
				backspace();
				break;
			case 27: // ESC 입력시
				clearAll();
				break;				
		}
	}

	private void clearAll() {	// 완전 초기화 메소드 CE
		setDisplayString("0");
		lastOperator = "0";
		lastNumber = 0;
		display_State = State_Input;
		nextInputClear = true;
	}
	
	private void backspace(){	// 백스페이스 메소드, 쭉쭉 지우다가 마지막 숫자에서 백스페이스 눌리면 0이 됨
		if (display_State != State_Error){
			setDisplayString(getDisplayString().substring(0,
					  getDisplayString().length() - 1));
			
			if (getDisplayString().length() < 1)
				setDisplayString("0");
		}
	}

	private void processOperator(String string) {
		if (display_State != State_Error){
			double numberInDisplay = getNumberInDisplay();

			if (!lastOperator.equals("0")){
				try	{
					double result = processLastOperator();
					displayResult(result);
					lastNumber = result;
				}catch(Exception e){}
			
			}else{
				lastNumber = numberInDisplay;
			}
			
			nextInputClear = true;
			lastOperator = string;
		}
	}

	// 마지막 연산자를 포함한 동작에 대한 메소드
	private double processLastOperator() throws Exception{
		double result = 0;
		double numberInDisplay = getNumberInDisplay();

		if (lastOperator.equals("/")){
			if (numberInDisplay == 0)
				throw (new Exception());

			result = lastNumber / numberInDisplay;
		}
			
		if (lastOperator.equals("*")){
			result = lastNumber * numberInDisplay;
		}
		if (lastOperator.equals("-")){
			result = lastNumber - numberInDisplay;
		}
		if (lastOperator.equals("+")){
			result = lastNumber + numberInDisplay;
		}
		
		return result;
	}

	// 결과 출력 동작에 대한 메소드
	private void processEquals() {
		double result = 0;

		if (display_State != State_Error){
			try{
				result = processLastOperator();
				displayResult(result);
			}catch (Exception e){
				displayError("0으로 나눌 수 없습니다.");
			}
			lastOperator = "0";
		}
	}

	// 소수점 추가에 대한 메소드
	private void addPoint() {
		display_State = State_Input;

		if (nextInputClear)
			setDisplayString("");

		String inputString = getDisplayString();
	
		// 이미 점이 찍혀 있으면 안 찍음.
		if (inputString.indexOf(".") < 0)
			setDisplayString(new String(inputString + "."));
	}

	// +- 양수 음수 동작 대한 메소드
	private void processSingChange() {
		if (display_State == State_Input){
			String input = getDisplayString();
			if (input.length() > 0 && !input.equals("0")){
				if (input.indexOf("-") == 0)
					setDisplayString(input.substring(1));
				else
					setDisplayString("-" + input);
			}
		}else if (display_State == State_Result){
			double numberInDisplay = getNumberInDisplay();
		
			if (numberInDisplay != 0)
				displayResult(-numberInDisplay);
		}
	}

	private void displayResult(double result) {
		setDisplayString(Double.toString(result));
		lastNumber = result;
		display_State = State_Result;
		nextInputClear = true;
	}

	private void displayError(String error) {
		setDisplayString(error);
		lastNumber = 0;
		display_State = State_Error;
		nextInputClear = true;
	}

	private double getNumberInDisplay() {
		String input = output.getText();
		return Double.parseDouble(input);
	}
	
	// 연산자 입력 전까지 입력받은 숫자가 이어지는 메소드
	private void addToDisplay(int i) {
		if (nextInputClear)
			setDisplayString("");
		
		String inputString = getDisplayString();
		
		if(inputString.indexOf("0") == 0){
			inputString = inputString.substring(1);
		}
		if(( !inputString.equals("0") || i>0 ) && inputString.length() < MAX_INPUT_LENGTH){
			setDisplayString(inputString + i);
		}
		
		display_State = State_Input;
		nextInputClear = false;
	}


	private void setDisplayString(String string) {
		output.setText(string);
	}


	private String getDisplayString() {
		return output.getText();
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
}