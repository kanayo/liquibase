package liquibase.database;

import static org.junit.Assert.*;
import org.junit.Test;

public class CacheDatabaseTest extends AbstractDatabaseTest{

	public CacheDatabaseTest() throws Exception {
        super(new CacheDatabase());
    }
	
	protected String getProductNameString() {
	      return "Cache";
	    }
	
	@Test
	public void supportsSequences() {
		assertFalse(database.supportsSequences());
	}

	@Test
	public void getFalseBooleanValue() {
		assertEquals("0", database.getFalseBooleanValue());
	}

	@Test
	public void getTrueBooleanValue() {
		assertEquals("1", database.getTrueBooleanValue());
	}

	@Test
	public void getLineComment() {
		assertEquals("--", database.getLineComment());
	}

	@Test
	public void getDefaultDriver() {
		assertEquals("com.intersys.jdbc.CacheDriver", 
				database.getDefaultDriver("jdbc:Cache://127.0.0.1:56773/TESTMIGRATE"));
	}

	@Test
	public void getProductName() {
		assertEquals(getProductNameString(), database.getProductName());
	}

	@Test
	public void getTypeName() {
		assertEquals("cache", database.getTypeName());
	}

	@Test
	public void getDateType() {
		assertEquals(new DataType("DATE", false), database.getDateType());
	}

	@Test
	public void getBlobType() {
		assertEquals(new DataType("LONGVARBINARY", true), database.getBlobType());
	}

	@Test
	public void getBooleanType() {
		assertEquals(new DataType("INTEGER", true), database.getBooleanType());
	}

	@Test
	public void getClobType() {
		assertEquals(new DataType("LONGVARCHAR", true), database.getClobType());
	}

	@Test
	public void getCurrencyType() {
		assertEquals(new DataType("MONEY", true), database.getCurrencyType());
	}

	@Test
	public void getCurrentDateTimeFunction() {
		assertEquals("SYSDATE", database.getCurrentDateTimeFunction());
	}

	@Test
	public void getDateTimeType() {
		assertEquals(new DataType("DATETIME", false), database.getDateTimeType());
	}

	@Test
	public void getUUIDType() {
		assertEquals(new DataType("CHAR(36)", false), database.getUUIDType());
	}

	@Test
	public void supportsInitiallyDeferrableColumns() {
		assertFalse(database.supportsInitiallyDeferrableColumns());
	}
}
