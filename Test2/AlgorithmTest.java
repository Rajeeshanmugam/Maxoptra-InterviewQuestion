package com.BankDetails.BankDetails;

import java.util.Scanner;

/* Eg.17 means n%10 == 7 output will be 7 and divisible n = n/10= 17/10 = 1 then 
 * return n value as 1 and do summation of modulo then 7+1=8 and multiple 
 * with n/10= 1/10 =0
 */
public class AlgorithmTest {
	public static void main(String[] args) {
		 Scanner input = new Scanner(System.in);
	     System.out.print("Input an integer: ");
	     long n = input.nextLong();
	     System.out.println("The sum of the digits is: " + sumDigits(n));
	}
	public static int sumDigits(long n) {
        int sum = 0;
        while (n != 0) {
            sum += n % 10;
            n = n/10;
        }
        return sum;
    }
}
