package liquibase.change.custom;

import liquibase.database.Database;
import liquibase.database.sql.SqlStatement;
import liquibase.exception.CustomChangeException;
import liquibase.exception.UnsupportedChangeException;

/**
 * Interface to implement when creating a custom change that generates SQL.  When updating a database,
 * implementing this interface is preferred over CustomTaskChange because the SQL can either be executed
 * directly or saved to a text file for later use depending on the migration mode used.  To allow
 * the change to be rolled back, also implement the CustomSqlRollback interface.  If your change requires sql-based
 * logic and non-sql-based logic, it is best to create a change set that contains a mix of CustomSqlChange and CustomTaskChange calls.
 *
 * @see liquibase.change.custom.CustomSqlRollback
 * @see liquibase.change.custom.CustomTaskChange
  */
public interface CustomSqlChange extends CustomChange {
    /**
     * Generates the SQL statements required to run the change
     *
     * @param database the target {@link liquibase.database.Database} associated to this change's statements
     * @return an array of {@link SqlStatement}s with the statements
     * @throws liquibase.exception.CustomChangeException if an exception occurs while processing this change
     * @throws liquibase.exception.UnsupportedChangeException if this change is not supported by the {@link liquibase.database.Database} passed as argument
     */
    public SqlStatement[] generateStatements(Database database) throws UnsupportedChangeException, CustomChangeException;

}
