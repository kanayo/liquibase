package liquibase.change;

import liquibase.database.MockDatabase;
import liquibase.database.sql.AlterSequenceStatement;
import liquibase.database.sql.SqlStatement;
import liquibase.database.sql.InsertStatement;
import liquibase.test.JUnitFileOpener;
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Tests for {@link liquibase.change.AlterSequenceChange}
 */
public class LoadDataChangeTest extends AbstractChangeTest {

    @Test
    public void getRefactoringName() throws Exception {
        assertEquals("Load Data", new LoadDataChange().getChangeName());
    }

    @Test
    public void generateStatement() throws Exception {
        LoadDataChange refactoring = new LoadDataChange();
        refactoring.setSchemaName("SCHEMA_NAME");
        refactoring.setTableName("TABLE_NAME");
        refactoring.setFile("changelogs/sample.data1.csv");
        refactoring.setFileOpener(new JUnitFileOpener());

        SqlStatement[] sqlStatements = refactoring.generateStatements(new MockDatabase());

        assertEquals(2, sqlStatements.length);
        assertTrue(sqlStatements[0] instanceof InsertStatement);
        assertTrue(sqlStatements[1] instanceof InsertStatement);

        assertEquals("SCHEMA_NAME", ((InsertStatement) sqlStatements[0]).getSchemaName());
        assertEquals("TABLE_NAME", ((InsertStatement) sqlStatements[0]).getTableName());
        assertEquals("Bob Johnson", ((InsertStatement) sqlStatements[0]).getColumnValue("name"));
        assertEquals("bjohnson", ((InsertStatement) sqlStatements[0]).getColumnValue("username"));

        assertEquals("SCHEMA_NAME", ((InsertStatement) sqlStatements[1]).getSchemaName());
        assertEquals("TABLE_NAME", ((InsertStatement) sqlStatements[1]).getTableName());
        assertEquals("John Doe", ((InsertStatement) sqlStatements[1]).getColumnValue("name"));
        assertEquals("jdoe", ((InsertStatement) sqlStatements[1]).getColumnValue("username"));
    }

    @Test
    public void generateStatement_excel() throws Exception {
        LoadDataChange refactoring = new LoadDataChange();
        refactoring.setSchemaName("SCHEMA_NAME");
        refactoring.setTableName("TABLE_NAME");
        refactoring.setFile("changelogs/sample.data1-excel.csv");
        refactoring.setFileOpener(new JUnitFileOpener());

        LoadDataColumnConfig ageConfig = new LoadDataColumnConfig();
        ageConfig.setHeader("age");
        ageConfig.setType("NUMERIC");
        refactoring.addColumn(ageConfig);

        LoadDataColumnConfig activeConfig = new LoadDataColumnConfig();
        activeConfig.setHeader("active");
        activeConfig.setType("BOOLEAN");
        refactoring.addColumn(activeConfig);

        SqlStatement[] sqlStatements = refactoring.generateStatements(new MockDatabase());

        assertEquals(2, sqlStatements.length);
        assertTrue(sqlStatements[0] instanceof InsertStatement);
        assertTrue(sqlStatements[1] instanceof InsertStatement);

        assertEquals("SCHEMA_NAME", ((InsertStatement) sqlStatements[0]).getSchemaName());
        assertEquals("TABLE_NAME", ((InsertStatement) sqlStatements[0]).getTableName());
        assertEquals("Bob Johnson", ((InsertStatement) sqlStatements[0]).getColumnValue("name"));
        assertEquals("bjohnson", ((InsertStatement) sqlStatements[0]).getColumnValue("username"));
        assertEquals("15", ((InsertStatement) sqlStatements[0]).getColumnValue("age").toString());
        assertEquals(Boolean.TRUE, ((InsertStatement) sqlStatements[0]).getColumnValue("active"));

        assertEquals("SCHEMA_NAME", ((InsertStatement) sqlStatements[1]).getSchemaName());
        assertEquals("TABLE_NAME", ((InsertStatement) sqlStatements[1]).getTableName());
        assertEquals("John Doe", ((InsertStatement) sqlStatements[1]).getColumnValue("name"));
        assertEquals("jdoe", ((InsertStatement) sqlStatements[1]).getColumnValue("username"));
        assertEquals("21", ((InsertStatement) sqlStatements[1]).getColumnValue("age").toString());
        assertEquals(Boolean.FALSE, ((InsertStatement) sqlStatements[1]).getColumnValue("active"));
    }

    @Test
    public void getConfirmationMessage() throws Exception {
        LoadDataChange refactoring = new LoadDataChange();
        refactoring.setTableName("TABLE_NAME");
        refactoring.setFile("FILE_NAME");

        assertEquals("Data loaded from FILE_NAME into TABLE_NAME", refactoring.getConfirmationMessage());
    }

    @Test
    public void createNode() throws Exception {
        LoadDataChange refactoring = new LoadDataChange();
        refactoring.setSchemaName("SCHEMA_NAME");
        refactoring.setTableName("TABLE_NAME");
        refactoring.setFile("FILE_NAME");
        refactoring.setEncoding("UTF-8");

        Element node = refactoring.createNode(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        assertEquals("loadData", node.getNodeName());
        assertEquals("SCHEMA_NAME", node.getAttribute("schemaName"));
        assertEquals("TABLE_NAME", node.getAttribute("tableName"));
        assertEquals("FILE_NAME", node.getAttribute("file"));
        assertEquals("UTF-8", node.getAttribute("encoding"));
    }
    
    @Test
    public void getMd5Sum() throws Exception {
        LoadDataChange refactoring = new LoadDataChange();
        refactoring.setSchemaName("SCHEMA_NAME");
        refactoring.setTableName("TABLE_NAME");
        refactoring.setFile("changelogs/sample.data1.csv");
        refactoring.setFileOpener(new JUnitFileOpener());

        String md5sum1 = refactoring.getMD5Sum();

        refactoring.setFile("changelogs/sample.data2.csv");
        String md5sum2 = refactoring.getMD5Sum();

        assertTrue(!md5sum1.equals(md5sum2));
        assertTrue(md5sum2.equals(refactoring.getMD5Sum()));
    }

}