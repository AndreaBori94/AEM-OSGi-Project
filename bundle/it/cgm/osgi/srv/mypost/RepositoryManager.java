package it.cgm.osgi.srv.mypost;

import it.cgm.osgi.srv.mypost.query.Query;
import it.cgm.osgi.srv.mypost.result.Result;
import it.cgm.osgi.srv.mypost.result.ResultQueryException;
import it.cgm.osgi.srv.mypost.result.ResultQueryFailure;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

/**
 * Classe per la gestione della repository
 * 
 * @author CGM2
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class RepositoryManager {

	/**
	 * L'oggetto Query, serve per ottenere informazioni relative alla richiesta
	 */
	private Query queryObject;

	/**
	 * serve per la repository
	 */
	private ResourceResolverFactory resolverFactory;
	/**
	 * serve per la repository
	 */
	private Session session;
	/**
	 * serve per la repository
	 */
	private ResourceResolver resourceResolver;
	/**
	 * serve per la repository
	 */
	private Node root;

	/**
	 * Costrutto default della classe
	 * 
	 * @param q
	 *            l'oggetto query per ottenere informazioni relative alla
	 *            richiesta
	 * @param resolverFactory
	 *            serve per la repository
	 * @param session
	 *            serve per la repository
	 * @param resourceResolver
	 *            serve per la repository
	 */
	public RepositoryManager(Query q, ResourceResolverFactory resolverFactory,
			Session session, ResourceResolver resourceResolver) {
		setQueryObject(q);

		this.resolverFactory = resolverFactory;
		this.session = session;
		this.resourceResolver = resourceResolver;

	}

	/**
	 * Inizializza la repository e permette di scrivere all'interno del JCR
	 * bisogna chiamare closeJCR() per far si che i cambiamenti siano
	 * persistenti ( effettivi )
	 * 
	 * @throws Exception
	 *             generalizzata l'eccezione
	 */
	private void initJCR() throws Exception {
		this.resourceResolver = resolverFactory
				.getAdministrativeResourceResolver(null);
		this.session = resourceResolver.adaptTo(Session.class);
		this.root = session.getRootNode();
	}

	/**
	 * Salva i cambiamenti e termina la connessione con il JCR
	 * 
	 * @throws Exception
	 *             generalizzata l'eccezione
	 */
	public void closeJCR() throws Exception {
		this.session.save();
		this.session.logout();
	}

	/**
	 * Imposta l'oggetto Query che serve per ottenere informazioni per
	 * l'esecuzione della richiesta da parte dell'utente
	 * 
	 * @param q
	 *            l'oggetto query
	 */
	private void setQueryObject(Query q) {
		this.queryObject = q;
	}

	/**
	 * Ritorna l'oggetto query
	 * 
	 * @return Query class
	 */
	public Query getQueryObject() {
		return this.queryObject;
	}

	/**
	 * Esegue un inserimento ricorsivo all'interno del JCR qual'ora il percorso
	 * s'interrompe ma non ha finito, crea un nodo e riprende.
     * se è stato specificato anche una lista di parametri nel JSON allora prova anche
     * ad inserire quei parametri se sono nulli crea il nodo e basta, la conferma di
     * inserimento delle chiavi all'interno del nodo non è controllabile
     * la condizione di ritorno fa SOLO riferimento alla creazione riuscita o meno, l'inserimento di attributi non è controllabile
     * in tutti i casi se la creazione va a buon fine anche l'inserimento delle chiavi va a buon fine se fallisce uno fallisce l'altro.
	 * 
	 * @param path
	 *            il percorso del nodo ( deve contenere caratteri slash
	 *            (SHIFT+7) )
	 * @return Node il nodo ala fine del percorso oppure NULL se il nodo
	 *         esisteva gia'
	 * @throws Exception
	 *             generalizzazione dell'eccezione
	 */
	public Node doRecursiveIns(String path) throws Exception {
		Node last = null;
		String[] node_path_slip = path.split("/");
		if (node_path_slip.length > 0) {
			if (!root.hasNode(node_path_slip[0])) {
				root.addNode(node_path_slip[0]);
			}
			Node tmp = root.getNode(node_path_slip[0]);
			for (int i = 1; i != node_path_slip.length; i++) {
				if (!tmp.hasNode(node_path_slip[i])) {
					tmp = tmp.addNode(node_path_slip[i]);
				} else {
					if (i == node_path_slip.length) {
						if (tmp.hasNode(node_path_slip[i])) {
							return null;
						}
					}
					tmp = tmp.getNode(node_path_slip[i]);
				}
			}
			last = tmp;
		} else {
			if (!root.hasNode(path)) {
				last = root.addNode(path);
                String[][] prop_list = getQueryObject().getParams();
                if ( prop_list != null ) {
                    for (int i = 0; i != prop_list.length; i++) {
                        String k = prop_list[i][0];
                        String v = prop_list[i][1];
                        // se una delle due non è valida esce ( null or empty )
                        if (k != null && v != null) {
                            if (!k.isEmpty() && !v.isEmpty()) {
                                last.setProperty(k, v);
                            }
                        }
                    }
                }
			} else
				return null;
		}
		return last;
	}

	/*
	 * public Node doRecursiveGet(String path) throws Exception { Node last =
	 * null; String[] node_path_slip = path.split("/"); if
	 * (node_path_slip.length > 0) { if (!root.hasNode(node_path_slip[0])) {
	 * root.addNode(node_path_slip[0]); } Node tmp =
	 * root.getNode(node_path_slip[0]); for (int i = 1; i !=
	 * node_path_slip.length; i++) { if (!tmp.hasNode(node_path_slip[i])) { last
	 * = null; break; } else tmp = tmp.getNode(node_path_slip[i]); } last = tmp;
	 * } else { if (root.hasNode(path)) { last = root.getNode(path); } else last
	 * = null; } return last; }
	 */

	/**
	 * Esegue una rimozione ricorsiva all'interno del JCR qual'ora il percorso
	 * s'interrompe termina
	 * 
	 * @param path
	 *            il percorso del nodo ( deve contenere caratteri slash
	 *            (SHIFT+7) )
	 * @return boolean true se ha eliminato altrimenti false
	 * @throws Exception
	 *             generalizzazione dell'eccezione
	 */
	public boolean doRecursiveRem(String path) throws Exception {
		Node last = null;
		String[] node_path_slip = path.split("/");
		if (node_path_slip.length > 0) {
			if (!root.hasNode(node_path_slip[0])) {
				root.addNode(node_path_slip[0]);
			}
			Node tmp = root.getNode(node_path_slip[0]);
			for (int i = 1; i != node_path_slip.length; i++) {
				if (!tmp.hasNode(node_path_slip[i])) {
					last = null;
					return false;
				} else
					tmp = tmp.getNode(node_path_slip[i]);
			}
			last = tmp;
		} else {
			if (root.hasNode(path)) {
				last = root.getNode(path);
			} else
				last = null;
		}
		last.remove();
		return true;
	}

	/**
	 * Esegue un aggiornamento ricorsivo all'interno del JCR qual'ora il
	 * percorso s'interrompe ma non ha finito, interrompe e ritorna il
	 * fallimento
	 * 
	 * @param path
	 *            il percorso del nodo ( deve contenere caratteri slash
	 *            (SHIFT+7) )
	 * @return boolean condizione se il processo e' riuscito o meno
	 * @throws Exception
	 *             generalizzazione dell'eccezione
	 */
	public boolean doRecursiveSet(String path) throws Exception {
		Node last = null;
		String[] node_path_slip = path.split("/");
		if (node_path_slip.length > 0) {
			if (!root.hasNode(node_path_slip[0])) {
				root.addNode(node_path_slip[0]);
			}
			Node tmp = root.getNode(node_path_slip[0]);
			for (int i = 1; i != node_path_slip.length; i++) {
				if (!tmp.hasNode(node_path_slip[i])) {
					last = null;
					return false;
				} else
					tmp = tmp.getNode(node_path_slip[i]);
			}
			last = tmp;
		} else {
			if (root.hasNode(path)) {
				last = root.getNode(path);
			} else
				last = null;
		}
		if (last != null) {
			String[][] prop_list = getQueryObject().getParams();
			if ( prop_list == null ) return false;
            for (int i = 0; i != prop_list.length; i++) {
				String k = prop_list[i][0];
				String v = prop_list[i][1];
				if (k != null && v != null) {
					if (!k.isEmpty() && !v.isEmpty()) {
						last.setProperty(k, v);
					}
				}
			}
			return true;
		} else
			return false;
	}

	/**
	 * Esegue un GET ricorsivo all'interno del JCR qual'ora il percorso
	 * s'interrompe ma non ha finito, interrompe e ritorna nullo Utilizza il
	 * getQueryObject().getParams() per sapere quali chiavi deve cercare le
	 * inserisce in un array ogni volta che trova una corrispondenza e in fine
	 * restituisce l'array
	 * 
	 * @param path
	 *            il percorso del nodo ( deve contenere caratteri slash
	 *            (SHIFT+7) )
	 * @return String[][] null se non ha trovato il nodo altrimenti un array
	 *         bidimensionale di stringhe con struttura M-Chiave=Valore
	 * @throws Exception
	 *             generalizzazione dell'eccezione
	 */
	public String[][] doRecursiveGet(String path) throws Exception {
		Node last = null;
		String[] node_path_slip = path.split("/");
		if (node_path_slip.length > 0) {
			if (!root.hasNode(node_path_slip[0])) {
				root.addNode(node_path_slip[0]);
			}
			Node tmp = root.getNode(node_path_slip[0]);
			for (int i = 1; i != node_path_slip.length; i++) {
				if (!tmp.hasNode(node_path_slip[i])) {
					last = null;
					break;
				} else
					tmp = tmp.getNode(node_path_slip[i]);
			}
			last = tmp;
		} else {
			if (root.hasNode(path)) {
				last = root.getNode(path);
			} else
				last = null;
		}
		if (last != null) {
			int pointer = 0;
			String[][] required = getQueryObject().getParams();

			List<String[]> list = new ArrayList<String[]>();
			PropertyIterator it = last.getProperties();
			while (it.hasNext()) {
				Property p = it.nextProperty();
				String k = p.getName().toString();
				String v = p.getValue().toString();
				if (required[pointer][0].equals(k)) {
					list.add(new String[] { k, v });
					pointer++;
				}
			}

			return list.toArray(new String[0][0]);
		} else
			return null;
	}
    
    /** 
     * Esegue un SEL ricorsivo all'interno del JCR qual'ora il percorso
	 * s'interrompe ma non ha finito, interrompe e ritorna nullo, altrimenti prosegue.
     * Cerca tutti i nodi dal percorso specificato in poi e li
	 * inserisce in un array di stringhe con struttura (nome-percorso) in fine
	 * restituisce l'array con tutti i nomi e i percorsi dentro ad un array
	 * 
	 * @param path
	 *            il percorso del nodo ( deve contenere caratteri slash
	 *            (SHIFT+7) )
	 * @return String[][] null se non ha trovato il nodo altrimenti un array
	 *         bidimensionale di stringhe con struttura M-Chiave=Valore
	 * @throws Exception
	 *             generalizzazione dell'eccezione
	 */
    public String[][] doRecursiveSel(String path) throws Exception {
        Node last = null;
		String[] node_path_slip = path.split("/");
		if (node_path_slip.length > 0) {
			if (!root.hasNode(node_path_slip[0])) {
				root.addNode(node_path_slip[0]);
			}
			Node tmp = root.getNode(node_path_slip[0]);
			for (int i = 1; i != node_path_slip.length; i++) {
				if (!tmp.hasNode(node_path_slip[i])) {
					last = null;
					break;
				} else
					tmp = tmp.getNode(node_path_slip[i]);
			}
			last = tmp;
		} else {
			if (root.hasNode(path)) {
				last = root.getNode(path);
			} else
				last = null;
		}
		if (last != null) {
			//iteriamo cerchiamo tutti i nodi e prendiamo i loro nomi       
            List<String[]> allNode = new ArrayList<String>();
            NodeIterator list = last.getNodes();
            while ( list.hasNext() ) {
                Node tmp = list.next();
                String[] tmp_array = {
                    tmp.getName(),
                    tmp.getPath()
                }
                allNode.add(tmp_array);
            }            
            //Transforma l'elenco di nomi in un array di stringhe
            return allNode.toArray(new String[0][0]);            
		} else
			return null;   
    }

	/**
	 * Esegue il servizio unendo tutte le informazioni e restituisce un oggetto
	 * Result. L'oggetto deve poi chiamare l'istruzione render() che genera il
	 * JSON che a sua volta passera' per il response.getWriter().write()
	 * 
	 * @return Result oggetto con il json da restituire
	 */
	public Result doAction() {
		Result result = new Result();
		switch (getQueryObject().getActionId()) {
            case 0: {
                // INSERT
                try {
                    initJCR();
                    if (doRecursiveIns(getQueryObject().getTarget()) != null) {
                        result.setStatus(0);
                        result.setMessage("success");
                    } else {
                        return new ResultQueryFailure();
                    }
                    closeJCR();
                } catch (Exception e) {
                    result.setException(e);
                }
                return result;
            }
            case 1: {
                // REMOVE
                try {
                    initJCR();
                    if (doRecursiveRem(getQueryObject().getTarget())) {
                        result.setStatus(0);
                        result.setMessage("success");
                    } else {
                        return new ResultQueryFailure();
                    }
                    closeJCR();
                } catch (Exception e) {
                    result.setException(e);
                }
                return result;
            }
            case 2: {
                // SETTER
                try {
                    initJCR();
                    if (doRecursiveSet(getQueryObject().getTarget())) {
                        result.setStatus(0);
                        result.setMessage("success");
                    } else {
                        return new ResultQueryFailure();
                    }
                    closeJCR();
                } catch (Exception e) {
                    result.setException(e);
                }
                return result;
            }
            case 3: {
                // GETTER
                try {
                    initJCR();
                    String[][] append = doRecursiveGet(getQueryObject().getTarget());
                    result.setStatus(0);
                    result.setMessage("success");
                    result.setKeySet(append);
                    closeJCR();
                } catch (Exception e) {
                    result.setException(e);
                }
                return result;
            }
            case 4: {
                // SELECT
                try {
                    initJCR();
                    String[][] append = doRecursiveSel(getQueryObject().getTarget());
                    result.setStatus(0);
                    result.setMessage("success");
                    result.setKeySet(append);
                    closeJCR();
                } catch (Exception e) {
                    result.setException(e);
                }
                return result;
            }

            default: {
                return new ResultQueryException();
            }
		}
	}

}
