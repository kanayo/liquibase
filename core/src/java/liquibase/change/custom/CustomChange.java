package liquibase.change.custom;

import liquibase.FileOpener;
import liquibase.database.Database;
import liquibase.exception.SetupException;
import liquibase.exception.InvalidChangeDefinitionException;

/**
 * Interface to implement when creating a custom change.  Actual custom changes implementations need to
 * implement CustomSqlChange or CustomTaskChange.
 * <br><br>
 * See http://www.liquibase.org/manual/custom_refactoring_class for more information.
 */
interface CustomChange {


    /**
     * Confirmation message to be displayed after the change is executed
     *
     * @return a {@link String} containing the message after the change is executed
     */
    public String getConfirmationMessage();

    /**
     * This method will be called after the no arg constructor and all of the
     * properties have been set to allow the task to do any heavy tasks or
     * more importantly generate any exceptions to report to the user about
     * the settings provided.
     *
     */
    public void setUp() throws SetupException;

    /**
     * Sets the fileOpener that should be used for any file loading and resource
     * finding for files that are provided by the user.
     */
    public void setFileOpener(FileOpener fileOpener);

    /**
     * Tests that the change is configured correctly before attempting to execute it.
     * @param database The database the change will be ran against
     */
    public void validate(Database database) throws InvalidChangeDefinitionException;

}
