package co.com.ivancho.business;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.com.ivancho.jdbc.JDBCManager;

/**
 * Ejecuta consultas, nada mas simple :|
 * 
 * @author IPinilla
 */
public class QueryProcessor {

	/**
	 * Ejecuta la consulta ingresada, pero primero reemplaza los símbolos de
	 * interrogación dentro de la consulta por los valores ingresados en el arreglo
	 * de entrada.
	 * 
	 * @param conn   Objeto de conexión
	 * @param query  Consulta a ejecutar
	 * @param params Lista de parámetros a usar en la consulta
	 * @return Devuelve una cadena de datos separados por comas
	 * 
	 */
	public static String exec(JDBCManager conn, String query, ArrayList<Object> params) {
		StringBuilder queryResult = new StringBuilder();

		if (conn != null) {
			try {
				PreparedStatement statement = conn.getConn().prepareStatement(transformParameters(query, params));
				// statement.setString(1, userName);
				ResultSet rs = statement.executeQuery();

				while (rs.next()) {
					queryResult.append(getResultsetDataByType(rs));
				}
				rs.close();
				statement.close();
				conn.getConn().close();

			} catch (SQLException e) {
				System.out.println("Hubo un error consultando la información.");
				e.printStackTrace();
				return null;
			}
		}
		return queryResult.toString();
	}

	/**
	 * Extrae el valor de cada columna entregada en el resultset y convierte su tipo
	 * de dato a String. Convierte toda una fila de datos en una cadena separada por
	 * comas.
	 * 
	 * @param rs Resultset objeto que contiene los datos encontrados con la
	 *           consulta.
	 * @return Devuelve el valor de todos los campos de una fila, separados por
	 *         comas.
	 */
	private static String getResultsetDataByType(ResultSet rs) {
		StringBuilder result = new StringBuilder();
		ResultSetMetaData metadata = null;
		try {
			metadata = rs.getMetaData();

			for (int i = 1; i <= metadata.getColumnCount(); i++) {

				switch (metadata.getColumnType(i)) {
				case Types.TIMESTAMP:
					if (null != rs.getTimestamp(i))
						result.append(rs.getTimestamp(i).toString()).append(", ");
					else
						result.append("[null]");
					break;
				case Types.DATE:
					if (null != rs.getDate(i))
						result.append(rs.getDate(i).toString()).append(", ");
					else
						result.append("[null]");
					break;
				case Types.INTEGER:
					Integer integer = rs.getInt(i);
					if (null != integer)
						result.append(String.valueOf(rs.getInt(i))).append(", ");
					break;
				case Types.VARCHAR:
					if (null != rs.getString(i))
						result.append(rs.getString(i).toString()).append(", ");
					else
						result.append("[null]");
					break;
				// Agregar mas tipos acá
				default:
					System.out.println("[Le faltó el tipo " + metadata.getColumnType(i) + "]");
					break;
				}
			}
		} catch (SQLException e) {
			System.out.println("Eror al analizar los tipos de datos devueltos.");
			e.printStackTrace();
		}
		result.append("\n");
		return result.toString();
	}

	/**
	 * Cambia cada símbolo de interrogante por el valor correspondiente en el
	 * listado de parámetros de entrada. Verifica que tipo de parámetro es ingresado
	 * apra asi determinar como se inserta en el query de la consulta.
	 * 
	 * @param query  Consulta a modificar.
	 * @param params Parámetros de entrada a usar para modificar el parámetro
	 *               <code>query</code>.
	 * @return Devuelve la consulta preparada para ejecutar.
	 */
	private static String transformParameters(String query, ArrayList<Object> params) {
		String queryTemp = query;
		String s = "\\?";
		String s2 = "(?<=%)(.*)(?=%)";
		Pattern p = Pattern.compile(s);

		for (Object object : params) {
			Matcher m = p.matcher(queryTemp);
			if (object.getClass() == Integer.class) {
				queryTemp = m.replaceFirst(object.toString());
			} else if (object.getClass() == String.class) {
				if(!(query.contains("'%") && query.contains("%'"))) {
					queryTemp = m.replaceFirst("'" + object.toString() + "'");
				} else {
					queryTemp = m.replaceFirst(object.toString());
				}
			}
		}
		return queryTemp;
	}
}
