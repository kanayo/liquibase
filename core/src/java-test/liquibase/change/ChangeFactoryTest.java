package liquibase.change;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ChangeFactory}
 */
public class ChangeFactoryTest
{

  private ChangeFactory factory;

  @Before
  public void setUp()
  {
    factory = ChangeFactory.getInstance();
  }

  @Test
  public void createTable()
  {
    Change createTableChange = factory.create("createTable");
    assertEquals(CreateTableChange.class, createTableChange.getClass());
  }

  @Test(expected = RuntimeException.class)
  public void createInvalidChange()
  {
    factory.create("invalidChange");
  }
}
