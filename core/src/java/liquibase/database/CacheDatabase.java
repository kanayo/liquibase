package liquibase.database;

import liquibase.exception.JDBCException;
import liquibase.database.structure.DatabaseSnapshot;
import liquibase.database.structure.CacheDatabaseSnapshot;
import liquibase.diff.DiffStatusListener;

import java.sql.Connection;
import java.text.ParseException;
import java.util.Set;

public class CacheDatabase extends AbstractDatabase {
    public static final String PRODUCT_NAME = "cache";
    private static final DataType BLOB_TYPE = new DataType("LONGVARBINARY", true);
    private static final DataType BOOLEAN_TYPE = new DataType("INTEGER", true);
    private static final DataType CLOB_TYPE = new DataType("LONGVARCHAR", true);
    private static final DataType CURRENCY_TYPE = new DataType("MONEY", true);
    private static final DataType DATETIME_TYPE = new DataType("DATETIME", false);
    private static final DataType UUID_TYPE = new DataType("CHAR(36)", false);

    public DataType getBlobType() {
        return BLOB_TYPE;
    }

    public DataType getBooleanType() {
        return BOOLEAN_TYPE;
    }

    public DataType getClobType() {
        return CLOB_TYPE;
    }

    public DataType getCurrencyType() {
        return CURRENCY_TYPE;
    }

    public DataType getDateTimeType() {
        return DATETIME_TYPE;
    }

    public DataType getUUIDType() {
        return UUID_TYPE;
    }

    public String getCurrentDateTimeFunction() {
        return "SYSDATE";
    }

    public String getDefaultDriver(String url) {
        if (url.startsWith("jdbc:Cache")) {
            return "com.intersys.jdbc.CacheDriver";
        }
        return null;
    }

    public String getProductName() {
        return "Cache";
    }

    public String getTypeName() {
        return "cache";
    }

    public boolean isCorrectDatabaseImplementation(Connection conn)
            throws JDBCException {
        return PRODUCT_NAME.equalsIgnoreCase(getDatabaseProductName(conn));
    }

    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    @Override
    public String getLineComment() {
        return "--";
    }

    public String getFalseBooleanValue() {
        return "0";
    }

    public String getTrueBooleanValue() {
        return "1";
    }

    @Override
    protected String getDefaultDatabaseSchemaName() throws JDBCException {
        return "";
    }

    public boolean supportsSequences() {
        return false;
    }

    public boolean supportsTablespaces() {
        return false;
    }

    public boolean supportsAutoIncrement() {
        return false;
    }

    public Object convertDatabaseValueToJavaObject(Object defaultValue, int dataType, int columnSize, int decimalDigits) throws ParseException {
        if (defaultValue != null) {
            if (defaultValue instanceof String) {
                String stringDefaultValue = (String) defaultValue;
                if (stringDefaultValue.charAt(0) == '"' && stringDefaultValue.charAt(stringDefaultValue.length() - 1) == '"') {
                    defaultValue = stringDefaultValue.substring(1, stringDefaultValue.length() - 1);
                } else if (stringDefaultValue.startsWith("$")) {
                    defaultValue = "OBJECTSCRIPT '" + defaultValue + "'";
                }
            }
        }
        return super.convertDatabaseValueToJavaObject(defaultValue, dataType, columnSize, decimalDigits);

    }

    public String getViewDefinition(String schemaName, String viewName) throws JDBCException {
        return null;
    }

    public DatabaseSnapshot createDatabaseSnapshot(String schema, Set<DiffStatusListener> statusListeners) throws JDBCException {
        return new CacheDatabaseSnapshot(this, statusListeners, schema);
    }
}
