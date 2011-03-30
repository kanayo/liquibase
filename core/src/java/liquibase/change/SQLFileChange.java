package liquibase.change;

import java.io.*;
import java.util.Set;
import java.util.logging.Logger;
import liquibase.FileOpener;
import liquibase.database.structure.DatabaseObject;
import liquibase.exception.SetupException;
import liquibase.log.LogFactory;
import liquibase.util.MD5Util;
import liquibase.util.StreamUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents a Change for custom SQL stored in a File.
 * 
 * To create an instance call the constructor as normal and then call
 * @link{#setFileOpener(FileOpener)} before calling setPath otherwise the
 * file will likely not be found.
 * 
 * 
 * @author <a href="mailto:csuml@yahoo.co.uk">Paul Keeble</a>
 * 
 */
public class SQLFileChange extends AbstractSQLChange {
    private static final Logger log = LogFactory.getLogger();
    private String file;
    private String encoding = null;
    
    
    public SQLFileChange() {
        super("sqlFile", "SQL From File");
    }

    public String getPath() {
        return file;
    }

    /**
     * Sets the file name but setUp must be called for the change to have impact.
     * 
     * @param fileName The file to use
     */
    public void setPath(String fileName) {
        file = fileName;
    }
    
    /**
     * The encoding of the file containing SQL statements
		 * @return the encoding
		 */
		public String getEncoding()
		{
			return encoding;
		}

		/**
		 * @param encoding the encoding to set
		 */
		public void setEncoding(String encoding)
		{
			this.encoding = encoding;
		}

		public void setUp() throws SetupException {
        if (file == null) {
            throw new SetupException("<sqlfile> - No path specified");
        }
        log.fine("SQLFile file:" + file);
        boolean loaded = loadFromClasspath(file);
        if(!loaded) {
            loaded = loadFromFileSystem(file);
        }
        
        if (!loaded) {
            throw new SetupException("<sqlfile path="+file+"> - Could not find file");
        }
        log.finer("SQLFile file contents is:" + getSql());
    }

    /**
     * Tries to load the file from the file system.
     * 
     * @param file The name of the file to search for
     * @return True if the file was found, false otherwise.
     */
    private boolean loadFromFileSystem(String file) throws SetupException {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            setSql( StreamUtil.getStreamContents(fis, encoding) );
            return true;
        } catch (FileNotFoundException fnfe) {
            return false;
        } catch (IOException e) {
            throw new SetupException("<sqlfile path="+file+"> -Unable to read file", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {//NOPMD
                    // safe to ignore
                }
            }
        }

    }

    /**
     * Tries to load a file using the FileOpener.
     * 
     * If the fileOpener can not be found then the attempt to load from the
     * classpath the return is false.
     * 
     * @param file The file name to try and find.
     * @return True if the file was found and loaded, false otherwise.
     */
    private boolean loadFromClasspath(String file) throws SetupException {
        InputStream in = null;
        try {
            FileOpener fo = getFileOpener();
            if(fo== null) {
                return false;
            }
            
            in = fo.getResourceAsStream(file);
            if (in == null) {
                return false;
            }
            setSql( StreamUtil.getStreamContents(in, encoding));
            return true;
        } catch (IOException ioe) {
            throw new SetupException("<sqlfile path="+file+"> -Unable to read file", ioe);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {//NOPMD
                    // safe to ignore
                }
            }
        }
    }
    
    /**
     * Calculates an MD5 from the contents of the file.
     * 
     * @see liquibase.change.AbstractChange#getMD5Sum()
     */
    public String getMD5Sum() {
        return MD5Util.computeMD5(getSql());
    }

    public Element createNode(Document currentChangeLogDOM) {
        Element sqlElement = currentChangeLogDOM.createElement("sqlFile");
        sqlElement.setAttribute("path", file);
        if (encoding != null) {
        	sqlElement.setAttribute("encoding", encoding);
        }
        return sqlElement;
    }

    public String getConfirmationMessage() {
        return "SQL in file " + file + " executed";
    }


    public Set<DatabaseObject> getAffectedDatabaseObjects() {
        return null;
    }
}
