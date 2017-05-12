package utn.frd.lsi.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import utn.frd.lsi.domain.Numeracion;

public class ApiCaller {

	private String url = "http://enacom.cloudapi.junar.com/api/v2/datastreams/NUMER-GEOGR/data.pjson/?auth_key=aa1fa323db16cc5dd566736c5f2a2ece2d2ce676";

	private String filtroPrefijo = "&filter0=column4[==]"; // &filter0=column4[==]230
	private String filtroBloque = "&filter1=column5[==]"; //&filter1=column5[==]4492
	private String filtroAnd = "&where=(filter0%20and%20filter1)";
	
	private HashMap<String,Object> cache = new HashMap<String,Object>();
	
	//http://enacom.cloudapi.junar.com/api/v2/datastreams/NUMER-GEOGR/data.pjson/?auth_key=aa1fa323db16cc5dd566736c5f2a2ece2d2ce676&filter0=column4[==]230&pGroupBy0=column4&pFunction0=COUNT[column5]
	private String groupByBloque = "&pGroupBy0=column4&pFunction0=COUNT[column5]"; //responde a cuantos bloques hay por un prefijo
	
	public long cantidadDeBloques(String prefijo) throws JSONException, IOException {
		String cacheKey = "CANT"+prefijo;
		if(cache.containsKey(cacheKey)){
			return (long) cache.get(cacheKey);
		}
		
		JSONObject json = readJsonFromUrl(url + filtroPrefijo + prefijo + groupByBloque);
		long result = 0;
		
		for(Object object : json.getJSONArray("result")){
			JSONObject jsonObject = (JSONObject)object;
			if( !jsonObject.keySet().isEmpty() && jsonObject.has("INDICATIVO") ){
				result = jsonObject.getLong("0-00");
				break;
			}
		}
		cache.put(cacheKey, result);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Numeracion> get(String prefijo) throws JSONException, IOException {
		String cacheKey = "GET"+prefijo;
		if(cache.containsKey(cacheKey)){
			return (List<Numeracion>) cache.get(cacheKey);
		}
		
		List<Numeracion> result = new ArrayList<Numeracion>();
		JSONObject json = readJsonFromUrl(url + filtroPrefijo + prefijo);
		for(Object object : json.getJSONArray("result")){
			JSONObject jsonObject = (JSONObject)object;
			if( !jsonObject.keySet().isEmpty() ){
				Numeracion n = new Numeracion(jsonObject);
				result.add(n);
			}
		}

		cache.put(cacheKey, result);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Numeracion> get(String prefijo, String bloque) throws JSONException, IOException {
		String cacheKey = "GET"+prefijo+bloque;
		if(cache.containsKey(cacheKey)){
			return (List<Numeracion>) cache.get(cacheKey);
		}

		List<Numeracion> result = new ArrayList<Numeracion>();
		JSONObject json = readJsonFromUrl(url + filtroPrefijo + prefijo + filtroBloque + bloque + filtroAnd);
		for(Object object : json.getJSONArray("result")){
			JSONObject jsonObject = (JSONObject)object;
			if( !jsonObject.keySet().isEmpty() && !jsonObject.has("length") ){
				Numeracion n = new Numeracion(jsonObject);
				result.add(n);
			}
		}

		cache.put(cacheKey, result);
		return result;
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		URL apiUrl = new URL(url);
		apiUrl.openConnection().setConnectTimeout(15000);
		apiUrl.openConnection().setReadTimeout(15000);
		InputStream is = apiUrl.openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

}
