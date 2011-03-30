package liquibase.change.custom;

import liquibase.FileOpener;
import liquibase.database.Database;
import liquibase.database.sql.SqlStatement;
import liquibase.database.sql.StoredProcedureStatement;
import liquibase.database.structure.DatabaseObject;
import liquibase.exception.SetupException;
import liquibase.exception.UnsupportedChangeException;
import liquibase.exception.InvalidChangeDefinitionException;

import java.util.Set;

public class ExampleCustomProcCallChange implements CustomSqlChange {

    private String procedureName;

    @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
    private FileOpener fileOpener;


    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public SqlStatement[] generateStatements(Database database) throws UnsupportedChangeException {
        StoredProcedureStatement procedureStatement = new StoredProcedureStatement("testHello");
        return new SqlStatement[]{
                procedureStatement,
        };
    }

    public String getConfirmationMessage() {
        return "Executed " + getProcedureName();
    }

    public void setUp() throws SetupException {
    }

    public Set<DatabaseObject> getAffectedDatabaseObjects() {
        return null;
    }


    public void setFileOpener(FileOpener fileOpener) {
        this.fileOpener = fileOpener;
    }

    public void validate(Database database) throws InvalidChangeDefinitionException {

    }
    
}
