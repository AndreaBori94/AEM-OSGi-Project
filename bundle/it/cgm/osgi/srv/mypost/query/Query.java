package it.cgm.osgi.srv.mypost.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Gestisce la "request" in format JSON leggendolo e restituendo le rispettive
 * informazioni necessarie all'OSGi per elaborare le richieste. tutte le
 * "chiavi" del json possono anche essere "vuote" non vengono contate come
 * errore nel caso di un INS non e' necessario specificare parametri per cui si
 * possono "omettere" se vengono assegnati come "null" allora verra' registrato
 * un errore
 * 
 * @author CGM2
 * @version 1.0.0
 * @since 1.0.0
 *
 */
public class Query {

	/**
	 * Contiene la definizione di tutte le azioni disponibili dal servizio
	 */
	public static final String[] ACTIONS = { "INS", "DEL", "SET", "GET" };
	/**
	 * L'action quale INS, DEL, UPD, SET, GET
	 */
	private String action;

	/**
	 * Il target di riferimento del "nodo" un percorso formato da slash come
	 * separatore i.e: percorso/nuovo/nodo
	 */
	private String target;

	/**
	 * I parametri ( le chiavi ) da assegnare o da reperire.
	 */
	private String[][] param;

	/**
	 * La richiesta passata dal servizio per ottenere il json ed elaborarlo
	 */
	private SlingHttpServletRequest request;

	/**
	 * Costrutto principale della classe
	 * 
	 * @param request
	 *            la richiesta passata dal servizio utilizzato per ottenere il
	 *            json
	 */
	public Query(SlingHttpServletRequest request) {
		setRequest(request);
		setAction();
		setTarget();
		setParams();
	}

	/**
	 * Restituisce l'oggetto request
	 * 
	 * @return SlingHttpServletRequest l'oggetto passato con doPost o doGet del
	 *         servizio
	 */
	public SlingHttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * Ritorna il tipo di "azione" richiesta dall'utente, quale INS, DEL, UPD
	 * etc
	 * 
	 * @return String il tipo di azione ( non più di 3 char )
	 */
	public String getAction() {
		return this.action;
	}

	/**
	 * Restituisce un array di Stringhe con formattazione N-Chiave-Valore
	 * 
	 * @return String[][] array di parametri con chiave e valore per ogni N
	 */
	public String[][] getParams() {
		return this.param;
	}

	/**
	 * Restituisce il "target", il percorso del nodo alla quale eseguire
	 * l'azione richiesta
	 * 
	 * @return String il percorso del nodo
	 */
	public String getTarget() {
		return this.target;
	}

	/**
	 * Imposta l'oggetto di richiesta
	 * 
	 * @param r
	 *            l'oggetto della richiesta
	 * @return boolean true se ha assegnato correttamente l'oggetto altrimenti
	 *         false
	 */
	private boolean setRequest(SlingHttpServletRequest r) {
		this.request = r;
		return (this.request != null);
	}

	/**
	 * Imposta il percorso del nodo che verra' affetto dalle modifiche
	 * 
	 * @return boolean true se ha assegnato correttamente l'oggetto altrimenti
	 *         false
	 */
	private boolean setTarget() {
		String target = toObject("json").getString("target");
		boolean valid = !target.contains(" ");
		if (!valid) {
			target.replace(" ", "_");
		}
		this.target = target;
		return (this.target != null);
	}

	/**
	 * Imposta il tipo di azione da eseguire
	 * 
	 * @return boolean true se ha assegnato correttamente l'oggetto altrimenti
	 *         false
	 */
	private boolean setAction() {
		String act = toObject("json").getString("query").toUpperCase();
		boolean match = false;
		for (int i = 0; i != ACTIONS.length; i++) {
			if (ACTIONS[i].equals(act))
				match = true;
		}
		this.action = act;
		return (this.action != null && match);
	}

	/**
	 * Restituisce un oggetto JSONObject utilizzando la request.getParameter per
	 * ottenere il json ( passato come argomento ) e interpretarlo
	 * 
	 * @param obj
	 *            il JSON in formato Stringa
	 * @return JSONObject oggetto java contenente il JSON
	 */
	public JSONObject toObject(String obj) {
		return new JSONObject(getRequest().getParameter("json"));
	}

	/**
	 * Imposta l'array dei parametri leggendo il JSON ottenuto con toObject
	 * ("json")
	 * 
	 * @return boolean true se ha assegnato correttamente l'oggetto altrimenti
	 *         false
	 */
	private boolean setParams() {
		List<String[]> result = new ArrayList<String[]>();

		JSONObject object = toObject("json");
		JSONArray params = object.getJSONArray("param");
		for (int i = 0; i != params.length(); i++) {
			JSONArray param = (JSONArray) params.get(i);
			String[] temp = { param.getString(0), param.getString(1) };
			result.add(temp);
		}
        if ( result.size() != 0 ) {
		  this.param = result.toArray(new String[0][0]);
        } else this.param = null;
		return (this.param != null);
	}

	/**
	 * Restituisce una stringa contenente informazioni riassuntive del "parsing"
	 * 
	 * @deprecated SOLO PER DEBUG
	 * 
	 * @return String una stringa di debug dell'oggetto Query(this)
	 */
	@Deprecated
	public String toDebugString() {
		return "DEBUG STRING : \n" + "Azione : " + getAction() + "\n"
				+ "Target : " + getTarget() + "\n" + "Field : "
				+ getParams().toString() + "\n";
	}

	public int getActionId() {
		for (int i = 0; i != ACTIONS.length; i++) {
			if (ACTIONS[i].equals(getAction()))
				return i;
		}
		return -1;
	}

}
