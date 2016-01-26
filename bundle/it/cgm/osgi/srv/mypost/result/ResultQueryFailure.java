package it.cgm.osgi.srv.mypost.result;


/**
 * Risultato d'eccezzione, viene lanciato al posto del "return result" di
 * RepositoryManager quando lo switch raggiunge il default viene istanziato new
 * {@link ResultQueryException}
 * 
 * @author CGM2
 * @since 1.0.0
 * @version 1.0.0
 */
public class ResultQueryFailure extends Result {
	/**
	 * Costruttore default della classe
	 */
	public ResultQueryFailure() {
		setStatus(-1);
		setMessage("Can't proceed with operation");
	}
}
