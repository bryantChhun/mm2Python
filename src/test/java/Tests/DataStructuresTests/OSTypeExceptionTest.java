package Tests.DataStructuresTests;

import org.mm2python.DataStructures.Exceptions.OSTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OSTypeExceptionTest {

    @Test
    void testOSTypeException() {
        assertThrows(OSTypeException.class, () -> {throw new OSTypeException("testing custom exception"); });
    }

    @Test
    void testOSTypeExceptionWithCause() {
        assertThrows(OSTypeException.class, () -> {throw new OSTypeException("custom exception message",
                new Throwable("custom type cause")); });
    }
}
