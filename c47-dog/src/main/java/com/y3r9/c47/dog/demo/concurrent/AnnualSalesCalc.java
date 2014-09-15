package com.y3r9.c47.dog.demo.concurrent;

import java.text.DateFormatSymbols;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class AnnualSalesCalc {
	private static int NUMBER_OF_CUSTOMERS = 100;
	private static int NUMBER_OF_MONTHS = 12;
	private static int salesMatix[][];

	private static class Summer implements Callable {
		private int companyID;

		public Summer(int companyID) {
			super();
			this.companyID = companyID;
		}

		@Override
		public Object call() throws Exception {
			int sum = 0;
			for (int col = 0; col < NUMBER_OF_MONTHS; col++) {
				sum += salesMatix[companyID][col];
			}
			System.out.printf("Totaling for client 1%02d completed%n",
					companyID);
			return sum;
		}
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		generateMatix();
		printMatrix();
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Set<Future<Integer>> set = new HashSet<>();
		for (int row=0; row<NUMBER_OF_CUSTOMERS; row++) {
			Callable<Integer> callable = new Summer(row);
			//Future<Integer> future = executor.submit(callable);
			FutureTask<Integer> future = new FutureTask<Integer>(callable);
			//future.run();
			executor.submit(future);
			set.add(future);
		}
		int sum = 0;
		for (Future<Integer> future : set) {
			sum += future.get();
		}
		System.out.printf("%nThe annual turnover (bags): %s%n%n", sum);
		executor.shutdown();
	}

	private static void generateMatix() {
		salesMatix = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_MONTHS];
		for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
			for (int j = 0; j < NUMBER_OF_MONTHS; j++) {
				salesMatix[i][j] = (int) (Math.random() * 100);
			}
		}
	}

	private static void printMatrix() {
		System.out.print("\t\t");
		String[] monthDisplayNames = (new DateFormatSymbols()).getShortMonths();
		for (String strName : monthDisplayNames) {
			System.out.printf("%8s", strName);
		}
		System.out.printf("%n%n");
		for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
			System.out.printf("Client ID: 1%02d", i);
			for (int j = 0; j < NUMBER_OF_MONTHS; j++) {
				System.out.printf("%8d", salesMatix[i][j]);
			}
			System.out.println();
		}
		System.out.printf("%n%n");

	}

}
