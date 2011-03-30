package liquibase.database.structure;

import liquibase.database.Database;

import java.util.ArrayList;
import java.util.List;

public class Table implements DatabaseObject, Comparable<Table> {

    private Database database;
    private String name;
    private String remarks;
    private String schema;
    private List<Column> columns = new ArrayList<Column>();

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Column> getColumns() {
        return columns;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table that = (Table) o;

        return name.equalsIgnoreCase(that.name);

    }

    public int hashCode() {
        return name.toUpperCase().hashCode();
    }


    public int compareTo(Table o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }


    public String toString() {
    	return getName();
    }

    /**
     * Returns the column object for the given columnName.  If the column does not exist in this table,
     * return null.
     */
    public Column getColumn(String columnName) {
        for (Column column : getColumns()) {
            if (column.getName().equalsIgnoreCase(columnName)) {
                return column;
            }
        }
        return null;
    }

	/**
	 * @return Returns the schema.
	 */
	public String getSchema () {
		return schema;
	}

	/**
	 * @param schema The schema to set.
	 */
	public void setSchema (String schema) {
		this.schema = schema;
	}
}
