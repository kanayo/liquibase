package liquibase.database;

import liquibase.ChangeSet;
import liquibase.DatabaseChangeLogLock;
import liquibase.RanChangeSet;
import liquibase.Liquibase;
import liquibase.diff.DiffStatusListener;
import liquibase.database.sql.SqlStatement;
import liquibase.database.template.JdbcTemplate;
import liquibase.database.structure.DatabaseSnapshot;
import liquibase.exception.DatabaseHistoryException;
import liquibase.exception.JDBCException;
import liquibase.exception.LockException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class MockDatabase implements Database {

    public boolean isCorrectDatabaseImplementation(Connection conn) throws JDBCException {
        return false;
    }

    public String getDefaultDriver(String url) {
        return null;
    }

    public DatabaseConnection getConnection() {
        return null;
    }

    public void setConnection(Connection conn) {
    }

    public void setConnection(DatabaseConnection conn) {
    }

    public boolean getAutoCommitMode() {
        return false;
    }

    public boolean isAutoCommit() throws JDBCException {
        return false;
    }

    public void setAutoCommit(boolean b) throws JDBCException {

    }

    public boolean supportsDDLInTransaction() {
        return false;
    }

    public String getDatabaseProductName() {
        return null;
    }

    public String getDatabaseProductVersion() throws JDBCException {
        return null;
    }


    public int getDatabaseMajorVersion() throws JDBCException {
        return 0;
    }

    public int getDatabaseMinorVersion() throws JDBCException {
        return 0;
    }

    public String getProductName() {
        return null;
    }

    public String getTypeName() {
        return null;
    }

    public String getDriverName() throws JDBCException {
        return null;
    }

    public String getConnectionURL() throws JDBCException {
        return null;
    }

    public String getConnectionUsername() throws JDBCException {
        return null;
    }

    public String getDefaultCatalogName() throws JDBCException {
        return null;
    }

    public String getDefaultSchemaName()  {
        return null;
    }

    public void setDefaultSchemaName(String schemaName) throws JDBCException {
        
    }

    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    public boolean supportsSequences() {
        return true;
    }

    public boolean supportsAutoIncrement() {
        return true;
    }

    public String getColumnType(String columnType, Boolean autoIncrement) {
        return columnType;
    }

    public String getFalseBooleanValue() {
        return "FALSE";
    }

    public String getTrueBooleanValue() {
        return "TRUE";
    }

    public String getDateLiteral(String isoDate) {
        return isoDate;
    }


    public String getDateLiteral(java.sql.Date date) {
        return date.toString();
    }

    public String getDateLiteral(Time time) {
        return time.toString();
    }

    public String getDateLiteral(Timestamp timeStamp) {
        return timeStamp.toString();
    }

    public String getCurrentDateTimeFunction() {
        return "DATETIME()";
    }

    public void setCurrentDateTimeFunction(String function) {
    }

    public String getLineComment() {
        return null;
    }

    public String getAutoIncrementClause() {
        return null;
    }

    public SqlStatement getCommitSQL() {
        return null;
    }

    /**
     * @see liquibase.database.Database#getDatabaseChangeLogTableName()
     */
    public String getDatabaseChangeLogTableName() {
        return "DATABASECHANGELOG";
    }

    /**
     * @see liquibase.database.Database#getDatabaseChangeLogLockTableName()
     */
    public String getDatabaseChangeLogLockTableName() {
        return "DATABASECHANGELOGLOCK";
    }
    
    /**
     * Does nothing
     * 
     * @see liquibase.database.Database#setDatabaseChangeLogLockTableName(java.lang.String)
     */
    public void setDatabaseChangeLogLockTableName(String tableName) {
    }

	/**
	 * Does nothing
	 * 
     * @see liquibase.database.Database#setDatabaseChangeLogTableName(java.lang.String)
     */
    public void setDatabaseChangeLogTableName(String tableName) {
    }

	public String getConcatSql(String... values) {
        return null;
    }

    public boolean acquireLock(Liquibase liquibase) throws LockException {
        return false;
    }

    public void releaseLock() throws LockException {
    }

    public DatabaseChangeLogLock[] listLocks() throws LockException {
        return new DatabaseChangeLogLock[0];
    }

    public boolean doesChangeLogTableExist() {
        return false;
    }

    public boolean doesChangeLogLockTableExist() {
        return false;
    }

    public void checkDatabaseChangeLogTable(Liquibase liquibase) throws JDBCException, IOException {
    }

    public void checkDatabaseChangeLogLockTable(Liquibase liquibase) throws JDBCException, IOException {
    }

    public void dropDatabaseObjects(String schema) throws JDBCException {
    }

    public void tag(String tagString) throws JDBCException {
    }

    public boolean doesTagExist(String tag) throws JDBCException {
        return false;
    }

    public boolean isSystemTable(String catalogName, String schemaName, String tableName) {
        return false;
    }


    public boolean isSystemView(String catalogName, String schemaName, String name) {
        return false;
    }

    public boolean isLiquibaseTable(String tableName) {
        return false;
    }

    public SqlStatement createFindSequencesSQL(String schema) throws JDBCException {
        return null;
    }

    public boolean shouldQuoteValue(String value) {
        return true;
    }

    public boolean supportsTablespaces() {
        return false;
    }

    public String getViewDefinition(String schemaName, String name) throws JDBCException {
        return null;
    }

    public int getDatabaseType(int type) {
        return type;
    }

    public String getDatabaseProductName(Connection conn) throws JDBCException {
        return "Mock Database";
    }

    public DataType getBooleanType() {
        return new DataType("BOOLEAN", false);
    }

    public DataType getCurrencyType() {
        return new DataType("CURRENCY", true);
    }

    public DataType getUUIDType() {
        return new DataType("UUID", false);
    }

    public DataType getClobType() {
        return new DataType("CLOB", true);
    }

    public DataType getCharType()
    {
        return new DataType("CHAR", true);
    }

    public DataType getVarcharType()
    {
        return new DataType("VARCHAR", true);
    }

    public DataType getFloatType()
    {
        return new DataType("FLOAT", true);
    }

    public DataType getDoubleType()
    {
        return new DataType("DOUBLE", true);
    }

    public DataType getIntType()
    {
        return new DataType("INT", true);
    }

    public DataType getTinyIntType()
    {
        return new DataType("TINYINT", true);
    }

    public DataType getBlobType() {
        return new DataType("BLOB", true);
    }

    public DataType getDateType() {
        return new DataType("DATE", false);
    }

    public DataType getDateTimeType() {
        return new DataType("DATETIME", false);
    }

    public DataType getTimeType() {
        return new DataType("TIME", false);
    }

    public DataType getBigIntType() {
        return new DataType("BIGINT", true);
    }

    public Object convertDatabaseValueToJavaObject(Object defaultValue, int dataType, int columnSize, int decimalDigits) {
        return defaultValue;
    }

    public String convertJavaObjectToString(Object value) {
        return value.toString();
    }

    public String getDateLiteral(Date defaultDateValue) {
        return defaultDateValue.toString();
    }

    public String escapeTableName(String schemaName, String tableName) {
        if (schemaName == null) {
            return tableName;
        } else {
            return schemaName+"."+tableName;
        }
    }

    public String escapeIndexName(String schemaName, String indexName) {
        return escapeTableName(schemaName, indexName);
    }

    public String escapeColumnName(String schemaName, String tableName, String columnName) {
        return columnName;
    }

    public String escapeColumnNameList(String columnNames) {
        return columnNames;
    }

    public String escapeSequenceName(String schemaName, String sequenceName) {
        if (sequenceName == null) {
            return sequenceName;
        } else {
            return schemaName+"."+sequenceName;
        }
    }

    public String convertRequestedSchemaToSchema(String requestedSchema) throws JDBCException {
        return requestedSchema;
    }

    public String convertRequestedSchemaToCatalog(String requestedSchema) throws JDBCException {
        return null;
    }

    public boolean supportsSchemas() {
        return true;
    }

    public String generatePrimaryKeyName(String tableName) {
        return "PK_"+tableName;
    }

    public String escapeViewName(String schemaName, String viewName) {
        return escapeTableName(schemaName, viewName);
    }

    public boolean isColumnAutoIncrement(String schemaName, String tableName, String columnName) throws SQLException {
        return false;
    }

    public boolean acquireLock() throws LockException {
        return false;
    }

    public void checkDatabaseChangeLogTable() throws JDBCException {
        ;
    }

    public void checkDatabaseChangeLogLockTable() throws JDBCException {
        ;
    }

    public ChangeSet.RunStatus getRunStatus(ChangeSet changeSet) throws JDBCException, DatabaseHistoryException {
        return null;
    }

    public RanChangeSet getRanChangeSet(ChangeSet changeSet) throws JDBCException, DatabaseHistoryException {
        return null;
    }

    public void markChangeSetAsRan(ChangeSet changeSet) throws JDBCException {
        ;
    }

    public void markChangeSetAsReRan(ChangeSet changeSet) throws JDBCException {
        ;
    }

    public List<RanChangeSet> getRanChangeSetList() throws JDBCException {
        return null;
    }

    public Date getRanDate(ChangeSet changeSet) throws JDBCException, DatabaseHistoryException {
        return null;
    }

    public void removeRanStatus(ChangeSet changeSet) throws JDBCException {
        ;
    }

    public void commit() {
        ;
    }

    public void rollback() {
        ;
    }

    public SqlStatement getSelectChangeLogLockSQL() throws JDBCException {
        return null;
    }

    public JdbcTemplate getJdbcTemplate() {
        return null;
    }

    public void setJdbcTemplate(JdbcTemplate template) {
        ;
    }

    public String escapeStringForDatabase(String string) {
        return string;
    }

    public void close() throws JDBCException {
        ;
    }

    public DatabaseSnapshot createDatabaseSnapshot(String schema, Set<DiffStatusListener> statusListeners) throws JDBCException {
        return null;
    }

    public boolean supportsRestrictForeignKeys() {
        return true;
    }

    public String escapeConstraintName(String constraintName) {
        return constraintName;
    }
    
    public boolean isLocalDatabase() throws JDBCException {
    	return true;
    }
}
