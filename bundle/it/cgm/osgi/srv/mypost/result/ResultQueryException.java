package it.cgm.osgi.srv.mypost.result;

import it.cgm.osgi.srv.mypost.query.Query;

/**
 * Risultato d'eccezzione, viene lanciato al posto del "return result" di
 * RepositoryManager quando lo switch raggiunge il default viene istanziato new
 * {@link ResultQueryException}
 * 
 * @author CGM2
 * @since 1.0.0
 * @version 1.0.0
 */
public class ResultQueryException extends Result {
	/**
	 * Costruttore default della classe
	 */
	public ResultQueryException() {
		setStatus(-1);
		String msg = "";
		for (int i = 0; i != Query.ACTIONS.length; i++) {
			msg += "[" + Query.ACTIONS[i] + "]";
		}
		setMessage("Invalid Query, only " + msg);
	}
}
