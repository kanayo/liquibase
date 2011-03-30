package liquibase.change;

import liquibase.database.MockDatabase;
import liquibase.database.sql.DropDefaultValueStatement;
import liquibase.database.sql.SqlStatement;
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

public class DropDefaultValueChangeTest extends AbstractChangeTest {

    @Test
    public void getRefactoringName() throws Exception {
        assertEquals("Drop Default Value", new DropDefaultValueChange().getChangeName());
    }

    @Test
    public void generateStatement() throws Exception {
        DropDefaultValueChange change = new DropDefaultValueChange();
        change.setSchemaName("SCHEMA_NAME");
        change.setTableName("TABLE_NAME");
        change.setColumnName("COL_HERE");

        SqlStatement[] sqlStatements = change.generateStatements(new MockDatabase());
        assertEquals(1, sqlStatements.length);
        assertTrue(sqlStatements[0] instanceof DropDefaultValueStatement);
        assertEquals("SCHEMA_NAME", ((DropDefaultValueStatement) sqlStatements[0]).getSchemaName());
        assertEquals("TABLE_NAME", ((DropDefaultValueStatement) sqlStatements[0]).getTableName());
        assertEquals("COL_HERE", ((DropDefaultValueStatement) sqlStatements[0]).getColumnName());
    }

    @Test
    public void getConfirmationMessage() throws Exception {
        DropDefaultValueChange change = new DropDefaultValueChange();
        change.setTableName("TABLE_NAME");
        change.setColumnName("COL_HERE");

        assertEquals("Default value dropped from TABLE_NAME.COL_HERE", change.getConfirmationMessage());
    }

    @Test
    public void createNode() throws Exception {
        DropDefaultValueChange change = new DropDefaultValueChange();
        change.setTableName("TABLE_NAME");
        change.setColumnName("COL_NAME");

        Element node = change.createNode(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        assertEquals("dropDefaultValue", node.getTagName());
        assertFalse(node.hasAttribute("schemaName"));
        assertEquals("TABLE_NAME", node.getAttribute("tableName"));
        assertEquals("COL_NAME", node.getAttribute("columnName"));
    }

    @Test
    public void createNode_withSchema() throws Exception {
        DropDefaultValueChange change = new DropDefaultValueChange();
        change.setSchemaName("SCHEMA_NAME");
        change.setTableName("TABLE_NAME");
        change.setColumnName("COL_NAME");

        Element node = change.createNode(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        assertEquals("dropDefaultValue", node.getTagName());
        assertEquals("SCHEMA_NAME", node.getAttribute("schemaName"));
        assertEquals("TABLE_NAME", node.getAttribute("tableName"));
        assertEquals("COL_NAME", node.getAttribute("columnName"));
    }

}
