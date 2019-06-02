/**
 * 
 */
package co.com.ivancho.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Usa parámetros para establecer una conexión con una base de datos.
 * 
 * @author IPinilla
 *
 */
public class JDBCManager {

	private String dbDriver = "jdbc:postgresql://";
	private Connection conn = null;

	/**
	 * Obtiene el driver adecuado, si no lo encuentra, genera una excepción.
	 * 
	 * @param driverName Nombre edl driver de conexión a base de datos.
	 * @return Devuelve <code><b>true</b></code> si encontró un driver adecuado.
	 */
	public boolean getDriver(String driverName) {
		try {
			Class.forName(driverName);
			System.out.println("Driver registrado.");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver no encontrado.");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Conecta a la base de datos con los parámetros ingresados.
	 * 
	 * @param host       Nombre del servidor.
	 * @param port       Puerto de conexión al servidor.
	 * @param database   Nombre de la base de datos a la que se va a conectar.
	 * @param dbUser     Nombre del usuario de conexión a la base de datos.
	 * @param dbPassword Clave del usuario de conexión a la base de datos.
	 * @return Devuelve <code><b>true</b></code> si pudo conectarse a la base de
	 *         datos.
	 */
	public boolean connect(String host, int port, String database, String dbUser, String dbPassword) {

		try {
			conn = DriverManager.getConnection(dbDriver + host + ":" + String.valueOf(port) + "/" + database, dbUser,
					dbPassword);
		} catch (SQLException e) {
			System.out.println("Conexión falló:");
			e.printStackTrace();
			return false;
		}

		if (conn != null) {
			System.out.println("Conexión establecida.");
			return true;
		} else {
			System.out.println("Coneción falló.");
		}

		return false;
	}

	/**
	 * Devuelve el objeto de conexión a la base ed datos
	 * 
	 * @return Objeto de conexión.
	 */
	public Connection getConn() {
		return conn;
	}
}
