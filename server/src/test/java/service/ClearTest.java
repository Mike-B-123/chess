package service;


import dataaccess.MemoryGameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.UniqueError500;
import services.clearService;
import spark.utils.Assert;

import java.util.HashMap;


public class ClearTest {
    String testClear = "{}" ;
    @Test
    public void positiveClear() throws UniqueError500 {
        Assertions.assertEquals(testClear, clearService.clear().toString());
    }
}
