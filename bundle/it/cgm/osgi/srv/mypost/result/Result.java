package it.cgm.osgi.srv.mypost.result;

import org.json.JSONObject;

/**
 * Classe per la gestione dei risultati in formato JSON con il metodo render()
 * 
 * @author CGM2
 * @version 1.0.0
 * @since 1.0.0
 *
 */
public class Result {

	/**
	 * Lo status dell'errore -1 vuol dire che non e' stato inizializzato
	 */
	private int status;

	/**
	 * Il messaggio ( opzionale ) che viene allegato all'errore, puo' anche
	 * essere presente in caso di successo con valore di "success"
	 */
	private String msg;

	/**
	 * Il "key set" del result, il risultato vero e proprio, contiene un array
	 * di array con struttura chiave - valore
	 */
	private String[][] keySet;

	/**
	 * Costrutto base della classe inizializza i valori di default per la classe
	 * Result ( così da ritornare sempre un oggetto anche in caso di errore )
	 */
	public Result() {
		setStatus(-1);
		setMessage("Invalid Result Recivied");
		setKeySet(null);
	}

	public void setException(Exception e) {
		setStatus(-1);
		setMessage("MSG: " + e.getMessage() + "\nCSE: " + e.getCause());
	}

	/**
	 * Renderizza il JSON e lo trasforma in strina, questo funziona va chiamata
	 * come parametro di: response.getWriter().write(result.render());
	 * 
	 * @return String il json in formato stringa
	 */
	public String render() {
		JSONObject object = new JSONObject();
		object.put("status", getStatus());
		object.put("msg", getMessage());
		object.put("keyset", getKeySet());
		return object.toString();
	}

	/**
	 * Imposta il key set ( contiene le variabili da ritornare in formato json )
	 * 
	 * @param keySet
	 *            un array bidimensionale contenente i risultati che verranno
	 *            poi parsificati
	 */
	public void setKeySet(String[][] keySet) {
		if (keySet == null || keySet.length < 1) {
			this.keySet = new String[][] { { "UNDEFINED_KEY", "UNDEFINED_VAL" } };
		} else
			this.keySet = keySet;
	}

	/**
	 * Ritorna il keySet con le variabili ancora da parsificare
	 * 
	 * @return String[][] il keyset di variabili
	 */
	public String[][] getKeySet() {
		return this.keySet;
	}

	/**
	 * Imposta il messaggio che accompagna il result, per dare un informazione
	 * aggiuntiva in caso di errore e/o di successo
	 * 
	 * @param msg
	 *            String il messaggio da allegare
	 */
	public void setMessage(String msg) {
		this.msg = msg;
	}

	/**
	 * Ritorna il messaggio che accompagna il Result
	 * 
	 * @return String il messaggio allegato
	 */
	public String getMessage() {
		return this.msg;
	}

	/**
	 * Imposta il valore di status, rappresenta un informazione quale per
	 * esempio -1=null 0=success 1=fail
	 * 
	 * @param status
	 *            il valore dello status del Result di default -1
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Ritorna il valore dello status, rappresenta un informazione quale per
	 * esempio -1=null 0=success 1=fail
	 * 
	 * @return int il valore dello status del result di default -1
	 */
	public int getStatus() {
		return this.status;
	}
}
