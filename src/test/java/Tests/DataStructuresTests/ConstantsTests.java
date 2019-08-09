package Tests.DataStructuresTests;

import mm2python.DataStructures.Constants;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConstantsTests {

    @Test
    void testTempFile() {
        Constants c = new Constants();
        Constants.tempFilePath = "testpath";
        assertEquals("testpath", Constants.tempFilePath);

        new Constants();
        assertEquals("testpath", Constants.tempFilePath);
    }

    @Test
    void testbitDepth() {
        Constants c = new Constants();
        Constants.bitDepth = 16;
        assertEquals(16, Constants.bitDepth);

        new Constants();
        assertEquals(16, Constants.bitDepth);
    }

    @Test
    void testHeight() {
        Constants c = new Constants();
        Constants.height = 2048;
        assertEquals(2048, Constants.height);

        new Constants();
        assertEquals(2048, Constants.height);
    }

    @Test
    void testWidth() {
        Constants c = new Constants();
        Constants.width = 1024;
        assertEquals(1024, Constants.width);

        new Constants();
        assertEquals(1024, Constants.width);
    }

    @Test
    void testPorts() {
        Constants c = new Constants();
        Constants.ports.add(1001);
        assertEquals(1001, Constants.ports.get(0).intValue());

        new Constants();
        assertEquals(1001, Constants.ports.get(0).intValue());
    }

    @Test
    void testfixedMemMap() {
        Constants c = new Constants();
        Constants.setFixedMemMap(true);
        assertTrue(Constants.getFixedMemMap());

        Constants p = new Constants();
        assertTrue(Constants.getFixedMemMap());
    }

    @Test
    void testpy4JRadioButton() {
        Constants c = new Constants();
        Constants.setPy4JRadioButton(true);
        assertTrue(Constants.getPy4JRadioButton());

        Constants p = new Constants();
        assertTrue(Constants.getPy4JRadioButton());
    }

    @Test
    void testOS() {
        Constants c = new Constants();

        assertEquals("mac", Constants.getOS());

        new Constants();
        assertEquals("mac", Constants.getOS());
    }


}
