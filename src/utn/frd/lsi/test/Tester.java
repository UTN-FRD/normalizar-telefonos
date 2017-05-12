package utn.frd.lsi.test;

import utn.frd.lsi.normalizer.Normalizador;

public class Tester {
	
	public static void main(String[] args) throws Exception {
		
		Normalizador app = new Normalizador("0## ####-####");

		String[] ejemplos = new String[]{"01168007101","023068007101","5491168007101","*658","+549 (3287) 564-546", "01138077998", "02304355796", "2304355796", "5492304355796", "+5492304355796"};

		
		for(String ej : ejemplos){
			System.out.println( app.formatear(ej) );
		}
	}
	
}
