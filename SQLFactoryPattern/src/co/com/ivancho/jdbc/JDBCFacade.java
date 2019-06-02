/**
 * 
 */
package co.com.ivancho.jdbc;

import java.util.ArrayList;

import co.com.ivancho.business.QueryProcessor;

/**
 * @author ASUS Fachada del programa, toma los parámetros de entrada, crea una
 *         conexión con la BD y finalmente trae los datos en forma de una cadena
 *         separada por comas.
 */
public class JDBCFacade {

	public String host = "localhost";
	public int port = 5432;
	public String database = "GameScores";
	public String databaseUser = "postgres";
	public String databasePassword = "";

	private JDBCManager connection = new JDBCManager();

	/**
	 * Selecciona el driver de base de datos que se va a usar para conectarse.
	 * 
	 * @param database Nombre de la base de datos.
	 */
	public JDBCFacade(String database) {

		try {
			if (null == database || database.isEmpty()) {
				System.out.println("El motor de base de datos es requerido");
			}
			if (database.toLowerCase().equals("postgresql")) {
				if (!connection.getDriver("org.postgresql.Driver")) {
					System.out.println("Driver no encontrado.");
				}
				if (!connection.connect(host, port, this.database, databaseUser, databasePassword)) {
					System.out.println("No se pudo conectar a la base de datos ");
				}
			} else {
				System.out.println("Y la BD!?");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ejecuta un select a la base de datos seleccionada.
	 * 
	 * @param query  Consulta a ejecutar.
	 * @param params lista de parametros a poner en la consulta.
	 */
	public void getSelect(String query, ArrayList<Object> params) {
		if (null != connection) {
			System.out.println(QueryProcessor.exec(connection, query, params));
		} else {
			System.out.println("Hubo algún error con la conexión...");
		}
	}

	/**
	 * Devuelve la conexión establecida como un objeto.
	 * 
	 * @return Devuelve el objeto de conexión.
	 */
	public JDBCManager getConnection() {
		return connection;
	}

}
