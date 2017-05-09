package utn.frd.lsi.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import utn.frd.lsi.data.ApiCaller;
import utn.frd.lsi.domain.Numeracion;

public class App {
	
	private ApiCaller api = new ApiCaller();
	
	public static void main(String[] args) throws Exception {
	    
		App app = new App();
		//http://enacom.gob.ar/numeracion_p136
		String[] ejemplos = new String[]{"*658","+549 (3287) 564-546", "01138077998", "02304355796", "2304355796", "5492304355796", "+5492304355796"};
		for(String ej : ejemplos){
			app.checkear(ej);
		}
	}
	
	public void checkear(String numeroDeTelefono) throws Exception{
		String result = "Analizando: "+numeroDeTelefono; 
		
		String numero = limpiarNumero(numeroDeTelefono);
		
		// detectar regla 10 digitos
		//http://enacom.gob.ar/numeracion_p136
		if(numero.length()>=10){
			numero = numero.substring(numero.length()-10, numero.length());
			while(numero.startsWith("0")) numero = numero.substring(1); //elimino los 0s al principio
			
			if(numero.startsWith("8") || numero.startsWith("6")){
				//es un numero NO GEOGRAFICO 6 y 8: Utilizados como primer dígito de Numeración no Geográfica. Ej.: 800.333.3344
				result += "0"+numero.substring(0,2)+"-"+numero.substring(3,5)+"-"+numero.substring(6,9);
			}else if(numero.startsWith("1") || numero.startsWith("2") || numero.startsWith("3") || numero.startsWith("54")){
				//1, 2 y 3: Utilizados como primer dígito en los Indicativos Interurbanos de Numeración  Geográfica. Ej.: 11.4347.9443
				
				//intentar obtener el prefijo
				String prefijo = obtenerPrefijo(numero);
				if(prefijo==null) 
					result += " - ERROR: el numero no tiene un prefijo valido";
				else{
					Numeracion numeracion = obtenerBloque(prefijo, numero);
					if(numeracion==null){
						result += " - ERROR: no tiene un bloque valido";
					}else{
						result += " - RESULTADO: (0"+prefijo+") "+numeracion.getBloque()+"-"+numero.substring(prefijo.length()+numeracion.getBloque().length());
					}
				}

			}else{
				//4,5,7,9: Reservados
				result += " ERROR: no es un numero de telefono válido ya que empieza con un numero reservado";
			}
		}else{
			if(numero.startsWith("*")){
				result += " - RESULTADO: "+numero;
			}else
				result += " - ERROR: "+numero+" no es un numero de telefono válido" ;
		}
			
		
/*		
		List<Numeracion> numeraciones = apiCaller.get("230","435");
		
		System.out.println("Se encontraron "+numeraciones.size()+" coincidentes:");
		for(Numeracion n : numeraciones){
			System.out.println("Localidad: "+n.getLocalidad()+" - estandarización: +54"+n.getPrefijo()+" "+n.getBloque());
		}
*/	    
		System.out.println(result);
		
	  }

	private Numeracion obtenerBloque(String prefijo, String numero) throws JSONException, IOException {
		//el prefijo puede tener 2,3 o 4 digitos
		//el bloque puede tener 3 o 4 digitos, independientemente de la longitud del prefijo
		boolean encontro = false;
		List<Numeracion> nums = api.get(prefijo, numero.substring(prefijo.length(), prefijo.length()+3)); //bloque de 3 digitos
		if(nums.size()==0) nums = api.get(prefijo, numero.substring(prefijo.length(), prefijo.length()+4)); //bloque de 4 digitos
		if(nums.size()==0) return null;
		
		return nums.get(0);
	}

	private String obtenerPrefijo(String numero) throws JSONException, IOException {
		long cantGrupos = 0;
		for(int longPrefijo = 2; longPrefijo<4; longPrefijo++){
			String prefijoAProbar = numero.substring(0, longPrefijo);
			cantGrupos = api.cantidadDeBloques( prefijoAProbar );
			if(cantGrupos>0)  //ya encontré el prefijo
				return prefijoAProbar;
		}
		return null;
	}

	//extrae solo los números de la expresión
	//si empieza con asterisco, lo mantiene
	private String limpiarNumero(String numeroDeTelefono) {
		// remove not number characters
		Pattern p = Pattern.compile("\\*?\\d+");
		Matcher m = p.matcher(numeroDeTelefono);
		StringBuffer numeroBf = new StringBuffer();
		while (m.find()) {
			numeroBf.append(m.group());
		}

		return numeroBf.toString();
	}
}
