package org.liquibase.ide.common.action;

import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import org.liquibase.ide.common.IdeFacade;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class StatusAction extends MigratorAction {

    public StatusAction() {
        super("Change Log Status");
    }

    public void actionPerform(Database database, IdeFacade ideFacade) throws LiquibaseException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String changeLogFile = ideFacade.promptForChangeLogFile();
        if (changeLogFile == null) {
            return;
        }

        ideFacade.getLiquibase(changeLogFile, database).reportStatus(true, null, new PrintWriter(new PrintStream(byteArrayOutputStream)));

        ideFacade.showOutput("Check Change Log Status", byteArrayOutputStream.toString());

    }

    public boolean needsRefresh() {
        return false;
    }


}
