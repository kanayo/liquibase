package org.liquibase.ide.common.change.action;

import liquibase.change.Change;
import liquibase.change.DropViewChange;
import liquibase.database.structure.View;
import org.liquibase.ide.common.change.wizard.RefactorWizard;
import org.liquibase.ide.common.change.wizard.page.RefactorWizardPage;

public class DropViewAction extends BaseViewRefactorAction {

    public DropViewAction() {
        super("Drop View");
    }

    public RefactorWizard createRefactorWizard(View dbObject) {
        return new RefactorWizard("Drop view "+ dbObject);
    }

    protected Change[] createChanges(View view, RefactorWizardPage... pages) {
        DropViewChange change = new DropViewChange();
        change.setViewName(view.getName());

        return new Change[]{change};
    }
}
