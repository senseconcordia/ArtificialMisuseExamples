package net.sf.jailer.ui.syntaxtextarea;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import net.sf.jailer.database.Session;
import net.sf.jailer.datamodel.Association;
import net.sf.jailer.datamodel.Column;
import net.sf.jailer.datamodel.DataModel;
import net.sf.jailer.datamodel.Table;
import net.sf.jailer.util.Quoting;

public class DataModelBasedSQLCompletionProvider extends SQLCompletionProvider<DataModel, String, Table> {

	private final Map<String, String> schemaPerUUCName = new HashMap<String, String>();
	private final Map<String, Table> schemaTablePerUUCName = new HashMap<String, Table>();
	private final Map<String, List<Table>> tablesPerSchema = new HashMap<String, List<Table>>();
	
	public DataModelBasedSQLCompletionProvider(Session session, DataModel metaDataSource) throws SQLException {
		super(session, metaDataSource);
		for (Table table: metaDataSource.getTables()) {
			String schema = table.getSchema("");
			String name = table.getUnqualifiedName();
			schemaPerUUCName.put(Quoting.normalizeIdentifier(schema), schema);
			schemaTablePerUUCName.put(Quoting.normalizeIdentifier(schema) + "." + Quoting.normalizeIdentifier(name), table);
			List<Table> tps = tablesPerSchema.get(schema);
			if (tps == null) {
				tps = new ArrayList<Table>();
				tablesPerSchema.put(schema, tps);
			}
			tps.add(table);
		}
	}

	@Override
	protected List<String> getColumns(Table table, long timeOut, JComponent waitCursorSubject) {
		ArrayList<String> columns = new ArrayList<String>();
		for (Column column: table.getColumns()) {
			columns.add(column.name);
		}
		return columns;
	}

	@Override
	protected String getDefaultSchema(DataModel metaDataSource) {
		return "";
	}

	@Override
	protected String findSchema(DataModel metaDataSource, String name) {
		return schemaPerUUCName.get(Quoting.normalizeIdentifier(name));
	}

	@Override
	protected Table findTable(String schema, String name) {
		return schemaTablePerUUCName.get(Quoting.normalizeIdentifier(schema) + "." + Quoting.normalizeIdentifier(name));
	}

	@Override
	protected String getTableName(Table table) {
		return table.getUnqualifiedName();
	}

	@Override
	protected List<Table> getTables(String schema) {
		return tablesPerSchema.get(schema);
	}

	@Override
	protected String getSchemaName(String schema) {
		return schema;
	}

	@Override
	protected List<String> getSchemas(DataModel metaDataSource) {
		return new ArrayList<String>(tablesPerSchema.keySet());
	}

	@Override
	protected List<Association> getAssociations(Table source, Table destination) {
		List<Association> result = new ArrayList<Association>();
		for (Association association: source.associations) {
			if (association.destination == destination || destination == null) {
				result.add(association);
			}
		}
		return result;
	}

}
