package org.liquibase.ide.common.action;

import liquibase.DatabaseChangeLogLock;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import org.liquibase.ide.common.IdeFacade;

import java.io.IOException;
import java.text.DateFormat;

public class ListLocksAction extends MigratorAction {

    public ListLocksAction() {
        super("List LiquiBase Locks");
    }

    public void actionPerform(Database database, IdeFacade ideFacade) throws LiquibaseException {

        try {
            String output = "";
            DatabaseChangeLogLock[] locks = ideFacade.getLiquibase(null, database).listLocks();
            for (DatabaseChangeLogLock lock : locks) {
                output += "Locked by "+lock.getLockedBy()+" since "+ DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(lock.getLockGranted())+"\n";
            }

            ideFacade.showMessage("LiquiBase Logs", output);
        } catch (IOException e) {
            throw new LiquibaseException(e);
        }
    }

    public boolean needsRefresh() {
        return false;
    }


}
