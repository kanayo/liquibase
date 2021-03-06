package liquibase.database;

import liquibase.database.sql.RawSqlStatement;
import liquibase.database.sql.SqlStatement;
import liquibase.database.structure.DatabaseSnapshot;
import liquibase.database.structure.DB2DatabaseSnapshot;
import liquibase.exception.DateParseException;
import liquibase.exception.JDBCException;
import liquibase.diff.DiffStatusListener;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

public class DB2Database extends AbstractDatabase {
    private static final DataType BOOLEAN_TYPE = new DataType("SMALLINT", true);
    private static final DataType CURRENCY_TYPE = new DataType("DECIMAL(19,4)", false);
    private static final DataType UUID_TYPE = new DataType("VARCHAR(36)", false);
    private static final DataType CLOB_TYPE = new DataType("CLOB", true);
    private static final DataType BLOB_TYPE = new DataType("BLOB", true);
    private static final DataType DATETIME_TYPE = new DataType("TIMESTAMP", false);

    public boolean isCorrectDatabaseImplementation(Connection conn) throws JDBCException {
        return getDatabaseProductName(conn).startsWith("DB2/");
    }

    public String getDefaultDriver(String url) {
        if (url.startsWith("jdbc:db2")) {
            return "com.ibm.db2.jcc.DB2Driver";
        }
        return null;
    }

    public String getProductName() {
        return "DB2";
    }

    public String getTypeName() {
        return "db2";
    }

    protected String getDefaultDatabaseSchemaName() throws JDBCException {//NOPMD
        return super.getDefaultDatabaseSchemaName().toUpperCase();
    }

    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    public String getAutoIncrementClause() {
        return "GENERATED BY DEFAULT AS IDENTITY";
    }

    public String getCurrentDateTimeFunction() {
        return "CURRENT TIMESTAMP";
    }

    public DataType getBooleanType() {
        return BOOLEAN_TYPE;
    }

    public String getTrueBooleanValue() {
        return "1";
    }

    public String getFalseBooleanValue() {
        return "0";
    }

    /**
     * Return an DB2 date literal with the same value as a string formatted using ISO 8601.
     * <p/>
     * Convert an ISO8601 date string to one of the following results:
     * to_date('1995-05-23', 'YYYY-MM-DD')
     * to_date('1995-05-23 09:23:59', 'YYYY-MM-DD HH24:MI:SS')
     * <p/>
     * Implementation restriction:
     * Currently, only the following subsets of ISO8601 are supported:
     * YYYY-MM-DD
     * hh:mm:ss
     * YYYY-MM-DDThh:mm:ss
     */
    public String getDateLiteral(String isoDate) {
        String normalLiteral = super.getDateLiteral(isoDate);

        if (isDateOnly(isoDate)) {
            StringBuffer val = new StringBuffer();
            val.append("DATE(");
            val.append(normalLiteral);
            val.append(')');
            return val.toString();
        } else if (isTimeOnly(isoDate)) {
            StringBuffer val = new StringBuffer();
            val.append("TIME(");
            val.append(normalLiteral);
            val.append(')');
            return val.toString();
        } else if (isDateTime(isoDate)) {
            StringBuffer val = new StringBuffer();
            val.append("TIMESTAMP(");
            val.append(normalLiteral);
            val.append(')');
            return val.toString();
        } else {
            return "UNSUPPORTED:" + isoDate;
        }
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

    public SqlStatement createFindSequencesSQL(String schema) throws JDBCException {
        return new RawSqlStatement("SELECT SEQNAME AS SEQUENCE_NAME FROM SYSCAT.SEQUENCES WHERE SEQTYPE='S' AND SEQSCHEMA = '" + convertRequestedSchemaToSchema(schema) + "'");
    }


    public boolean shouldQuoteValue(String value) {
        return super.shouldQuoteValue(value)
                && !value.startsWith("\"SYSIBM\"");
    }


    public boolean supportsTablespaces() {
        return true;
    }

    public SqlStatement getViewDefinitionSql(String schemaName, String name) throws JDBCException {
        return new RawSqlStatement("select view_definition from SYSIBM.VIEWS where TABLE_NAME='" + name + "' and TABLE_SCHEMA='" + convertRequestedSchemaToSchema(schemaName) + "'");
    }

    public String getViewDefinition(String schemaName, String name) throws JDBCException {
        return super.getViewDefinition(schemaName, name).replaceFirst("CREATE VIEW \\w+ AS ", ""); //db2 returns "create view....as select
    }

    public Object convertDatabaseValueToJavaObject(Object defaultValue, int dataType, int columnSize, int decimalDigits) throws ParseException {
        if (defaultValue != null && defaultValue instanceof String) {
            if (dataType == Types.TIMESTAMP) {
                defaultValue = ((String) defaultValue).replaceFirst("^\"SYSIBM\".\"TIMESTAMP\"\\('", "").replaceFirst("'\\)", "");
            } else if (dataType == Types.TIME) {
                defaultValue = ((String) defaultValue).replaceFirst("^\"SYSIBM\".\"TIME\"\\('", "").replaceFirst("'\\)", "");
            } else if (dataType == Types.DATE) {
                defaultValue = ((String) defaultValue).replaceFirst("^\"SYSIBM\".\"DATE\"\\('", "").replaceFirst("'\\)", "");
            }
        }
        return super.convertDatabaseValueToJavaObject(defaultValue, dataType, columnSize, decimalDigits);
    }

    protected java.util.Date parseDate(String dateAsString) throws DateParseException {
        try {
            if (dateAsString.indexOf(' ') > 0) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAsString);
            } else if (dateAsString.indexOf('.') > 0 && dateAsString.indexOf('-') > 0) {
                return new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS").parse(dateAsString);

            } else {
                if (dateAsString.indexOf(':') > 0) {
                    return new SimpleDateFormat("HH:mm:ss").parse(dateAsString);
                } else if (dateAsString.indexOf('.') > 0) {
                    return new SimpleDateFormat("HH.mm.ss").parse(dateAsString);
                } else {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(dateAsString);
                }
            }
        } catch (ParseException e) {
            throw new DateParseException(dateAsString);
        }
    }

    public String convertRequestedSchemaToSchema(String requestedSchema) throws JDBCException {
        if (requestedSchema == null) {
            return getDefaultDatabaseSchemaName();
        } else {
            return requestedSchema.toUpperCase();
        }
    }

    public String convertRequestedSchemaToCatalog(String requestedSchema) throws JDBCException {
        return null;
    }

    public String generatePrimaryKeyName(String tableName) {
        if(tableName.equals(databaseChangeLogTableName))
            tableName = shortDatabaseChangeLogTableName;
        else if(tableName.equals(databaseChangeLogLockTableName))
            tableName = shortDatabaseChangeLogLockTableName;

        String pkName = super.generatePrimaryKeyName(tableName);
        if (pkName.length() > 18) {
            pkName = pkName.substring(0, 17);
        }
        return pkName;
    }

    public boolean isColumnAutoIncrement(String schemaName, String tableName, String columnName) throws SQLException, JDBCException {
        boolean autoIncrement = false;

        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement("SELECT IDENTITY FROM SYSCAT.COLUMNS WHERE TABSCHEMA = ? AND TABNAME = ? AND COLNAME = ? AND HIDDEN != 'S'");
            stmt.setString(1, convertRequestedSchemaToSchema(schemaName));
            stmt.setString(2, tableName);
            stmt.setString(3, columnName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String identity = rs.getString("IDENTITY");
                if (identity.equalsIgnoreCase("Y")) {
                    autoIncrement = true;
                }
            }
            rs.close();
        } finally {
            if (stmt != null)
            {
                stmt.close();
            }
        }

        return autoIncrement;
    }

    public DatabaseSnapshot createDatabaseSnapshot(String schema, Set<DiffStatusListener> statusListeners) throws JDBCException {
        return new DB2DatabaseSnapshot(this, statusListeners, schema);
    }
}
