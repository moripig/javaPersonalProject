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
		
		myCalculator.pack();	// CalculatorTest ������� ǥ��
		myCalculator.setVisible(true);	// ���α׷� â ����
		
		myCalculator.setTitle("����");	// ���α׷� �̸�(���ǥ��)
		myCalculator.setResizable(false);	// ���α׷� â ũ�� ���� ���� ����
	}
}
