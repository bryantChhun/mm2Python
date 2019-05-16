package Tests;

import mm2python.DataStructures.Builders.MDSParamObject;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MDSParamObjectTest {
    private MDSParamObject mdso1;
    private MDSParamObject mdso2;
    private MDSParamObject mdso3;

    private void clearMDSO() {
        mdso1 = null;
        mdso2 = null;
        mdso3 = null;
    }

    @Test
    void testIntAssignment() {
        try {
            mdso1 = new MDSParamObject(0);
            mdso2 = new MDSParamObject(1);
            mdso3 = new MDSParamObject(2);
        } catch(Exception ex) {
            fail(ex);
        }

        assertEquals(0, mdso1.getInt().intValue());
        assertEquals(1, mdso2.getInt().intValue());
        assertEquals(2, mdso3.getInt().intValue());

        clearMDSO();
    }

    @Test
    void testStrAssignment() {
        try {
            mdso1 = new MDSParamObject("str1");
            mdso2 = new MDSParamObject("str2");
            mdso3 = new MDSParamObject("str3");
        } catch(Exception ex) {
            fail(ex);
        }
        assertEquals("str1", mdso1.getStr());
        assertEquals("str2", mdso2.getStr());
        assertEquals("str3", mdso3.getStr());
        clearMDSO();
    }

    @Test
    void testLabelAssignment() {
        try {
            mdso1 = new MDSParamObject("label",0);
            mdso2 = new MDSParamObject("label1", "str");
            mdso3 = new MDSParamObject("label2", "str2");
        } catch(Exception ex) {
            fail(ex);
        }
        assertEquals("label", mdso1.getLabel());
        assertEquals("label1", mdso2.getLabel());
        assertEquals("label2", mdso3.getLabel());
        clearMDSO();
    }

    @Test
    void testIntThrows() {
        assertThrows(InvalidParameterException.class, () -> new MDSParamObject(0).getStr());
        assertThrows(InvalidParameterException.class, () -> new MDSParamObject(0).getLabel());
        clearMDSO();
    }

    @Test
    void testStrThrows() {
        assertThrows(InvalidParameterException.class, () -> new MDSParamObject("str").getInt());
        assertThrows(InvalidParameterException.class, () -> new MDSParamObject("str").getLabel());
        clearMDSO();
    }

    @Test
    void testArrayList() {
        List<MDSParamObject> testlist = new ArrayList<>();
        try {
            mdso1 = new MDSParamObject("label",0);
            mdso2 = new MDSParamObject("label1", "str");
            mdso3 = new MDSParamObject("label2", "str2");
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            testlist.add(mdso1);
            testlist.add(mdso2);
            testlist.add(mdso3);
        } catch(Exception ex) {
            fail(ex);
        }

        assertEquals(3, testlist.size());
    }

    @Test
    void testSet() {
        Set<MDSParamObject> testSet = new HashSet<>();
        try {
            mdso1 = new MDSParamObject("label",0);
            mdso2 = new MDSParamObject("label", 1);
            mdso3 = new MDSParamObject("label2", "str2");
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            testSet.add(mdso1);
            testSet.add(mdso2);
            testSet.add(mdso3);
        } catch(Exception ex) {
            fail(ex);
        }

        assertEquals(3, testSet.size());
        assertTrue(testSet.contains(mdso1));
        assertTrue(testSet.contains(mdso2));
        assertTrue(testSet.contains(mdso3));
    }

    @Test
    void testMap() {
        Set<MDSParamObject> testSet = new HashSet<>();
        try {
            mdso1 = new MDSParamObject("label",0);
            mdso2 = new MDSParamObject("label1", "str");
            mdso3 = new MDSParamObject("label2", "str2");
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            testSet.add(mdso1);
            testSet.add(mdso2);
            testSet.add(mdso3);
        } catch(Exception ex) {
            fail(ex);
        }

        assertEquals(3, testSet.size());
        assertTrue(testSet.contains(mdso1));
        assertTrue(testSet.contains(mdso2));
        assertTrue(testSet.contains(mdso3));
    }

}
