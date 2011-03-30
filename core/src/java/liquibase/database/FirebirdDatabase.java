package liquibase.database;

import liquibase.database.sql.RawSqlStatement;
import liquibase.database.sql.SqlStatement;
import liquibase.database.structure.DatabaseSnapshot;
import liquibase.database.structure.FirebirdDatabaseSnapshot;
import liquibase.exception.JDBCException;
import liquibase.diff.DiffStatusListener;

import java.sql.Connection;
import java.util.Set;

/**
 * Firebird database implementation.
 * SQL Syntax ref: http://www.ibphoenix.com/main.nfs?a=ibphoenix&page=ibp_60_sqlref
 */
public class FirebirdDatabase extends AbstractDatabase {
    private static final DataType BOOLEAN_TYPE = new DataType("SMALLINT", false);
    private static final DataType CURRENCY_TYPE = new DataType("DECIMAL(18, 4)", false);
    private static final DataType UUID_TYPE = new DataType("CHAR(36)", false);
    private static final DataType CLOB_TYPE = new DataType("BLOB SUB_TYPE TEXT", false);
    private static final DataType BLOB_TYPE = new DataType("BLOB", false);
    private static final DataType DATETIME_TYPE = new DataType("TIMESTAMP", false);

    public boolean isCorrectDatabaseImplementation(Connection conn) throws JDBCException {
        return getDatabaseProductName(conn).startsWith("Firebird");
    }

    public String getDefaultDriver(String url) {
        if (url.startsWith("jdbc:firebirdsql")) {
            return "org.firebirdsql.jdbc.FBDriver";
        }
        return null;
    }

    public String getProductName() {
        return "Firebird";
    }

    public String getTypeName() {
        return "firebird";
    }


    public boolean supportsSequences() {
        return true;
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

    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    public String getCurrentDateTimeFunction() {
        return "CURRENT_TIMESTAMP";
    }

    public boolean supportsTablespaces() {
        return false;
    }


    public SqlStatement createFindSequencesSQL(String schema) throws JDBCException {
        return new RawSqlStatement("SELECT RDB$GENERATOR_NAME FROM RDB$GENERATORS WHERE RDB$SYSTEM_FLAG IS NULL OR RDB$SYSTEM_FLAG = 0");
    }

    public SqlStatement getViewDefinitionSql(String schemaName, String viewName) throws JDBCException {
        String sql = "select rdb$view_source from rdb$relations where upper(rdb$relation_name)='" + viewName + "'";
//        if (schemaName != null) {
//            sql += " and rdb$owner_name='" + schemaName.toUpperCase() + "'";
//        }
//        if (getDefaultCatalogName() != null) {
//            sql += " and table_catalog='" + getDefaultCatalogName() + "'";
//
//        }
        return new RawSqlStatement(sql);
    }


    public boolean supportsDDLInTransaction() {
        return false;
    }


    public String getTrueBooleanValue() {
        return "1";
    }

    public String getFalseBooleanValue() {
        return "0";
    }


    public boolean isSystemTable(String catalogName, String schemaName, String tableName) {
        return tableName.startsWith("RDB$") || super.isSystemTable(catalogName, schemaName, tableName);
    }


    public boolean supportsAutoIncrement() {
        return false;
    }

    public boolean supportsSchemas() {
        return false;
    }

    public String convertRequestedSchemaToSchema(String requestedSchema) throws JDBCException {
        if (requestedSchema == null) {
            return getDefaultDatabaseSchemaName();
        } else {
            return requestedSchema.toUpperCase();
        }
    }

    public String getColumnType(String columnType, Boolean autoIncrement) {
        String type = super.getColumnType(columnType, autoIncrement);
        if (type.startsWith("BLOB SUB_TYPE <0")) {
            return getBlobType().getDataTypeName();
        } else {
            return type;
        }
    }

    public DatabaseSnapshot createDatabaseSnapshot(String schema, Set<DiffStatusListener> statusListeners) throws JDBCException {
        return new FirebirdDatabaseSnapshot(this, statusListeners, schema);
    }
}
