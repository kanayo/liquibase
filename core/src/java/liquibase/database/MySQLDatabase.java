package liquibase.database;

import liquibase.database.sql.RawSqlStatement;
import liquibase.database.sql.SqlStatement;
import liquibase.database.structure.DatabaseSnapshot;
import liquibase.database.structure.MySqlDatabaseSnapshot;
import liquibase.diff.DiffStatusListener;
import liquibase.exception.JDBCException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * Encapsulates MySQL database support.
 */
public class MySQLDatabase extends AbstractDatabase {
    public static final String PRODUCT_NAME = "MySQL";
    private static final DataType BOOLEAN_TYPE = new DataType("TINYINT(1)", false);
    private static final DataType CURRENCY_TYPE = new DataType("DECIMAL", true);
    private static final DataType UUID_TYPE = new DataType("CHAR(36)", false);
    private static final DataType CLOB_TYPE = new DataType("TEXT", true);
    private static final DataType BLOB_TYPE = new DataType("BLOB", true);
    private static final DataType DATETIME_TYPE = new DataType("DATETIME", false);

    public String getProductName() {
        return "MySQL";
    }

    public String getTypeName() {
        return "mysql";
    }


    public String getConnectionUsername() throws JDBCException {
        return super.getConnectionUsername().replaceAll("\\@.*", "");
    }

    public boolean isCorrectDatabaseImplementation(Connection conn) throws JDBCException {
        return PRODUCT_NAME.equalsIgnoreCase(getDatabaseProductName(conn));
    }

    public String getDefaultDriver(String url) {
        if (url.startsWith("jdbc:mysql")) {
            return "com.mysql.jdbc.Driver";
        }
        return null;
    }


    public DataType getBooleanType() {
        return BOOLEAN_TYPE;
    }

    public DataType getCurrencyType() {
        return CURRENCY_TYPE;
    }

    public DataType getUUIDType() {
        return UUID_TYPE;
    }

    public DataType getClobType() {
        return CLOB_TYPE;
    }

    public DataType getBlobType() {
        return BLOB_TYPE;
    }

    public DataType getDateTimeType() {
        return DATETIME_TYPE;
    }

    public boolean supportsSequences() {
        return false;
    }

    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    public String getCurrentDateTimeFunction() {
        return "NOW()";
    }

    public String getLineComment() {
        return "--";
    }

    public String getFalseBooleanValue() {
        return "0";
    }

    public String getTrueBooleanValue() {
        return "1";
    }

    public String getConcatSql(String ... values) {
        StringBuffer returnString = new StringBuffer();
        returnString.append("CONCAT_WS(");
        for (String value : values) {
            returnString.append(value).append(", ");
        }

        return returnString.toString().replaceFirst(", $", ")");
    }

    public boolean supportsTablespaces() {
        return false;
    }


    protected String getDefaultDatabaseSchemaName() throws JDBCException {
//        return super.getDefaultDatabaseSchemaName().replaceFirst("\\@.*","");
        try {
            return getConnection().getCatalog();
        } catch (SQLException e) {
            throw new JDBCException(e);
        }
    }

    public String convertRequestedSchemaToSchema(String requestedSchema) throws JDBCException {
        if (requestedSchema == null) {
            return getDefaultDatabaseSchemaName();
        }
        return requestedSchema;
    }

    public String convertRequestedSchemaToCatalog(String requestedSchema) throws JDBCException {
        return requestedSchema;
    }

    public SqlStatement getViewDefinitionSql(String schemaName, String viewName) throws JDBCException {
        return new RawSqlStatement("select view_definition from information_schema.views where table_name='" + viewName + "' AND table_schema='" + schemaName + "'");
    }

    public String escapeTableName(String schemaName, String tableName) {
        if(schemaName != null) {
            return "`" + schemaName + "`.`" + tableName + "`";
        }
        return "`" + tableName + "`";
    }

    public String escapeConstraintName(String constraintName) {
        if (constraintName == null) {
            return null;
        }
        return "`" + constraintName + "`";
    }


    public String escapeColumnName(String schemaName, String tableName, String columnName) {
        return "`" + columnName + "`";
    }

    public String escapeColumnNameList(String columnNames) {
        StringBuffer sb = new StringBuffer();
        for(String columnName : columnNames.split(",")) {
            if(sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("`").append(columnName.trim()).append("`");
        }
        return sb.toString();
    }
    
    public DatabaseSnapshot createDatabaseSnapshot(String schema, Set<DiffStatusListener> statusListeners) throws JDBCException {
        return new MySqlDatabaseSnapshot(this, statusListeners, schema);
    }
}
