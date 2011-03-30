package liquibase.preconditions;

import liquibase.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.exception.PreconditionFailedException;
import liquibase.exception.PreconditionErrorException;

/**
 * Marker interface for preconditions.  May become an annotation in the future.
 */
public interface Precondition {
    public void check(Database database, DatabaseChangeLog changeLog) throws PreconditionFailedException, PreconditionErrorException;

    public String getTagName();
}
