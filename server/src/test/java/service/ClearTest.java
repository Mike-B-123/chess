package service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.UniqueError500;
import services.ClearService;


public class ClearTest {
    String testClear = "{}" ;
    @Test
    public void positiveClear() throws UniqueError500 {
        Assertions.assertEquals(testClear, ClearService.clear().toString());
    }
}
