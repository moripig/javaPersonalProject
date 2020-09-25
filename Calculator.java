package project;

import javax.swing.*;

import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;

public class Calculator {

	public static void main(String args[]){
		
		CalculatorTest myCalculator = new CalculatorTest();
		
		myCalculator.pack();	// CalculatorTest 설정대로 표현
		myCalculator.setVisible(true);	// 프로그램 창 띄우기
		
		myCalculator.setTitle("계산기");	// 프로그램 이름(상단표시)
		myCalculator.setResizable(false);	// 프로그램 창 크기 조절 가능 여부
	}
}
