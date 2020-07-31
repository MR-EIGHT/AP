package com.eight.calculator;

import com.eight.num.BigNum;

import java.util.Scanner;

public class calculator {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StringBuilder statement = new StringBuilder();
        String s = input.nextLine();
        s = s.replaceAll(" ", "");
        statement.append(s);


        int plus = 0, minus = 0, devide = 0, exponent = 0, multiply = 0;


        for (int i = 0; i < statement.length(); i++) {
            switch (statement.charAt(i)) {
                case '^':
                    exponent++;
                    break;
                case '*':
                    multiply++;
                    break;
                case '/':
                    devide++;
                    break;
                case '+':
                    plus++;
                    break;
                case '-':
                    minus++;
                    break;
                default:
                    //throw new IllegalArgumentException("Bad input");
            }
        }
        if (exponent > 0) exponent(statement, exponent);
        if (multiply > 0) operations(statement, multiply, '*');
        if (devide > 0) operations(statement, devide, '/');
        if (plus > 0) operations(statement, plus, '+');
        if (minus > 0) operations(statement, minus, '-');
        System.out.println(statement.toString());

    }

    public static String gui(String x) {
        StringBuilder statement = new StringBuilder();
        statement.append(x);


        int plus = 0, minus = 0, devide = 0, exponent = 0, multiply = 0;


        for (int i = 0; i < statement.length(); i++) {
            switch (statement.charAt(i)) {
                case '^':
                    exponent++;
                    break;
                case '*':
                    multiply++;
                    break;
                case '/':
                    devide++;
                    break;
                case '+':
                    plus++;
                    break;
                case '-':
                    minus++;
                    break;
                default:
                    //throw new IllegalArgumentException("Bad input");
            }
        }
        if (exponent > 0) exponent(statement, exponent);
        if (multiply > 0) operations(statement, multiply, '*');
        if (devide > 0) operations(statement, devide, '/');
        if (plus > 0) operations(statement, plus, '+');
        if (minus > 0) operations(statement, minus, '-');

        return statement.toString();
    }


    private static void operations(StringBuilder statement, int T, char operator) {
        StringBuilder num1 = new StringBuilder();
        StringBuilder num2 = new StringBuilder();
        int first = 0;
        int last = 0;
        while (T-- > 0) {

            for (int i = 0; i < statement.length() - 1; i++) {
                if (statement.charAt(i) == operator) {
                    for (int j = i - 1; j >= 0 && statement.charAt(j) >= '0' && statement.charAt(j) <= '9'; j--) {
                        num1.append(statement.charAt(j));
                        first = j;
                        if (j == 0) break;
                    }
                    if (statement.charAt(0) == '-' && first == 1) {
                        num1.append('-');
                        statement.deleteCharAt(0);
                        first = 0;
                        i--;

                    }

                    for (int j = i + 1; statement.charAt(j) >= '0' && statement.charAt(j) <= '9'; j++) {
                        num2.append(statement.charAt(j));
                        last = j;
                        if (j >= statement.length() - 1) break;
                    }
                    statement.setCharAt(i, ' ');
                    switch (operator) {

                        case '*':
                            statement.replace(first, last + 1, BigNum.fromString(num1.reverse().toString()).multiply(BigNum.fromString(num2.toString())).toString());

                            break;
                        case '/':
                            statement.replace(first, last + 1, BigNum.fromString(num1.reverse().toString()).divideBy(BigNum.fromString(num2.toString())).toString());

                            break;
                        case '+':
                            statement.replace(first, last + 1, BigNum.fromString(num1.reverse().toString()).add(BigNum.fromString(num2.toString())).toString());

                            break;
                        case '-':
                            statement.replace(first, last + 1, BigNum.fromString(num1.reverse().toString()).subtract(BigNum.fromString(num2.toString())).toString());

                            break;

//                        case '^':
//                            statement.replace(first, last + 1, BigNum.fromString(num1.reverse().toString()).Pow(BigNum.fromString(num2.toString())).toString());
//                            break;

                        default:
                            //throw new IllegalArgumentException("Bad input");
                    }

                    num1 = new StringBuilder();
                    num2 = new StringBuilder();

                }

            }

        }

    }


    private static void exponent(StringBuilder statement, int exponent) {
        StringBuilder num1 = new StringBuilder();
        StringBuilder num2 = new StringBuilder();
        int first = 0;
        int last = 0;
        while (exponent-- > 0) {
            for (int i = statement.length() - 1; i > 0; i--) {
                if (statement.charAt(i) == '^') {
                    for (int j = i - 1; statement.charAt(j) >= '0' && statement.charAt(j) <= '9'; j--) {
                        num1.append(statement.charAt(j));
                        first = j;
                        if (j == 0) break;
                    }
                    for (int j = i + 1; statement.charAt(j) >= '0' && statement.charAt(j) <= '9'; j++) {
                        num2.append(statement.charAt(j));
                        last = j;
                        if (j >= statement.length() - 1) break;
                    }
                    statement.setCharAt(i, ' ');
                    statement.replace(first, last + 1, BigNum.fromString(num1.reverse().toString()).Pow(BigNum.fromString(num2.toString())).toString());
                    num1 = new StringBuilder();
                    num2 = new StringBuilder();

                }

            }

        }

    }


}
