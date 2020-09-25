package project;

import javax.swing.*;

import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;

public class CalculatorTest extends JFrame implements ActionListener, KeyListener {

	final int MAX_INPUT_LENGTH = 16;	// �Է� ������ ���� ����
	
	int display_State;	// ���â�� ����� ���� �� ���¿� ���ڸ� �ο��Ͽ� ����
	final int State_Input = 0;
	final int State_Result = 1;
	final int State_Error = 2;
	
	boolean nextInputClear; 	// ȭ�鿡 ǥ�õ� ���ڸ� ������ ���� ����
	
	double lastNumber;			// ���������� �Էµƴ� ��
	String lastOperator;		// �������� ���� ������
	
	private JLabel output;		// ���� ���� �Ǵ� ����� ��µǴ� ����
	private JButton buttons[];	// ��ư ������ �� �迭 ����
	private JPanel panelMaster, panelButton, panelRemove;	// ��ư�� ���̺��� ��ġ�� ����
	
	public CalculatorTest(){
		
		this.getContentPane().setBackground(Color.lightGray);   //�������� ����� ȸ��
		
		panelMaster = new JPanel();
		
		output = new JLabel("0", JLabel.RIGHT);	// ���̺��� �ʱ� ���� 0
		output.setBackground(Color.lightGray);
		output.setOpaque(false);
		output.setFont(new Font("", Font.BOLD, 30));
		
		buttons = new JButton[23];
		
		panelButton = new JPanel();
		
		// ���� 0~9 ���ڹ�ư ����
		for (int i=0 ; i<=9 ; i++){
			buttons[i] = new JButton(String.valueOf(i));			
		}
		
		// ���꿡 �ʿ��� ��ư ����
		buttons[10] = new JButton("��");
		buttons[11] = new JButton(".");
		buttons[12] = new JButton("=");
		buttons[13] = new JButton("��");
		buttons[14] = new JButton("��");
		buttons[15] = new JButton("-");
		buttons[16] = new JButton("+");
		buttons[17] = new JButton("��");
		buttons[18] = new JButton("1/x");
		buttons[19] = new JButton("%");
		
		// �ʱ�ȭ �Ǵ� �Է� ��ҿ� �ʿ��� ��ư ����
		buttons[20] = new JButton("<--");
		buttons[21] = new JButton("CE");
		buttons[22] = new JButton("C");
		
		// �� ��ư�� ������ ����
		for(int i=0 ; i<buttons.length ; i++){
			if(i<10){
				buttons[i].setForeground(Color.white);	// ���� 
				buttons[i].setBackground(Color.gray);
				buttons[i].setFont(new Font("", Font.BOLD, 30));
			}else if(i>=10 && i<21){
				buttons[i].setForeground(Color.gray);	// ������
				buttons[i].setBackground(Color.orange);
				buttons[i].setFont(new Font("", Font.BOLD, 20));
			}else{
				buttons[i].setForeground(Color.white);	// ��Ʈ�� 
				buttons[i].setBackground(Color.black);
				buttons[i].setFont(new Font("", Font.BOLD, 15));
			}
			buttons[12].setForeground(Color.white);	// = ��ư�� ���� 
			buttons[12].setBackground(Color.blue);
			buttons[12].setFont(new Font("", Font.BOLD, 30));
		}
		
		// ���ڿ� ������ ��ġ�ϱ�
		panelButton.setLayout(new GridLayout(4,5,5,5));
		
		for(int i=7; i<=9 ; i++){
			panelButton.add(buttons[i]);	// 7���� 9����
		}
		
		panelButton.add(buttons[13]);	// ������
		panelButton.add(buttons[17]);	// ��Ʈ
		
		for(int i=4; i<=6 ; i++){
			panelButton.add(buttons[i]); 	// 4���� 6����
		}
		
		panelButton.add(buttons[14]);	// ���ϱ�
		panelButton.add(buttons[18]);	// 1/x
		
		for(int i=1; i<=3 ; i++){
			panelButton.add(buttons[i]);	// 1���� 3����
		}
		
		panelButton.add(buttons[15]);	// ����
		panelButton.add(buttons[19]);	// �ۼ�Ʈ
		
		panelButton.add(buttons[10]);	// 0
		panelButton.add(buttons[0]);	// ��
		panelButton.add(buttons[11]);	// �Ҽ���
		panelButton.add(buttons[16]);	// ���ϱ�
		panelButton.add(buttons[12]);	// =
		
		// �齺���̽� �� �ʱ�ȭ ��ư ��ġ
		panelRemove = new JPanel();
		panelRemove.setLayout(new GridLayout(1,2,2,2));
		
		panelRemove.add(buttons[20]);	// �齺���̽�
		panelRemove.add(buttons[21]);	// CE
		panelRemove.add(buttons[22]);	// C
		
		
		// ���� ��ư��� ���� �� ������ ��ư ��ġ��
		panelMaster.setLayout(new BorderLayout());
		panelMaster.add(panelRemove, BorderLayout.NORTH);	// �齺���̽��� �ʱ�ȭ�� ��ư ��ܿ� ��ġ
		panelMaster.add(panelButton, BorderLayout.SOUTH);	// ���ڿ� �����ڸ� �� �Ʒ��� ��ġ
		
		getContentPane().add(output, BorderLayout.NORTH);
		getContentPane().add(panelMaster,BorderLayout.SOUTH);
		requestFocus();
		
		// ActionListener, KeyListener �� �� ó���� �����ߴ� �ִ� �Է����ѱ��� ���ڸ� �Է¹޵��� ��
		for(int i=0 ; i<buttons.length ; i++){
			buttons[i].addActionListener(this);
			buttons[i].addKeyListener(this);
		}
				
		clearAll(); // �ʱ�ȭ
		
		addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e){ // ���� ����� ���μ��� ����
					System.exit(0);
				}
			}
		);		
	}
	
	//���콺 �Է� ���� �޼ҵ�
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
						case 10: // ��
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
						case 17: // ��
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
										displayError("0���� ���� �� �����ϴ�.");
		
									result = 1 / getNumberInDisplay();
									displayResult(result);
								}
								
								catch(Exception ex)	{
									displayError("�߸��� �Է��Դϴ�!");
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
									displayError("�߸��� �Է��Դϴ�!");
									display_State = State_Error;
								}
							}
							break;
						case 20:	// �齺���̽�
							backspace();
							break;
						case 21: // ���� ���� ����
							setDisplayString("0");
							nextInputClear = true;
							display_State = State_Input;
							break;
						case 22: // ���� �ʱ�ȭ
							clearAll();
							break;							
					}
				}	
			}
		}
	}
	
	
	// Ű���� �����е� �Է�
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
			case 8: // �齺���̽�
				backspace();
				break;
			case 27: // ESC �Է½�
				clearAll();
				break;				
		}
	}

	private void clearAll() {	// ���� �ʱ�ȭ �޼ҵ� CE
		setDisplayString("0");
		lastOperator = "0";
		lastNumber = 0;
		display_State = State_Input;
		nextInputClear = true;
	}
	
	private void backspace(){	// �齺���̽� �޼ҵ�, ���� ����ٰ� ������ ���ڿ��� �齺���̽� ������ 0�� ��
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

	// ������ �����ڸ� ������ ���ۿ� ���� �޼ҵ�
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

	// ��� ��� ���ۿ� ���� �޼ҵ�
	private void processEquals() {
		double result = 0;

		if (display_State != State_Error){
			try{
				result = processLastOperator();
				displayResult(result);
			}catch (Exception e){
				displayError("0���� ���� �� �����ϴ�.");
			}
			lastOperator = "0";
		}
	}

	// �Ҽ��� �߰��� ���� �޼ҵ�
	private void addPoint() {
		display_State = State_Input;

		if (nextInputClear)
			setDisplayString("");

		String inputString = getDisplayString();
	
		// �̹� ���� ���� ������ �� ����.
		if (inputString.indexOf(".") < 0)
			setDisplayString(new String(inputString + "."));
	}

	// +- ��� ���� ���� ���� �޼ҵ�
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
	
	// ������ �Է� ������ �Է¹��� ���ڰ� �̾����� �޼ҵ�
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