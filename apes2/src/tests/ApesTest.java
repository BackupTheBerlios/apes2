

import abbot.Log;
import abbot.tester.ComponentTester;
import java.awt.*;
import java.awt.event.*;
import junit.framework.*;
import junit.extensions.abbot.*;

/** JUnit test case for the FontChooser GUI component.  Provides a setup
 * method to instantate a gui for scripts to test.
 */
public class ApesTest extends ScriptTestCase {

    /** Construct a test case with the given name. */
    public ApesTest(String name) {
        super(name);
    }

    /** Provide a default test suite for this test case. */
    public static Test suite() { 
        return new ScriptTestSuite(ApesTest.class,
                                   "scripts/");
    }

    public static void main(String[] args) {
        args = Log.init(args);
        String[] names = { ApesTest.class.getName() };
        if (args.length == 1 && args[0].equals("--gui"))
            junit.swingui.TestRunner.main(names);
        else
            junit.textui.TestRunner.main(names);
    }
}
