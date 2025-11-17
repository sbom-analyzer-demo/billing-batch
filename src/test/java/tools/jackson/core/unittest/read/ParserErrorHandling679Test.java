package tools.jackson.core.unittest.read;

import org.junit.jupiter.api.Test;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.core.unittest.testutil.failure.JacksonTestFailureExpected;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Tests for [core#679] - partially fixed by #105 fix, but "/" case still fails
class ParserErrorHandling679Test
    extends tools.jackson.core.unittest.JacksonCoreTestBase
{
    @Test
    void nonRootMangledFloats679Bytes() throws Exception {
        _testNonRootMangledFloats679(MODE_INPUT_STREAM);
        _testNonRootMangledFloats679(MODE_INPUT_STREAM_THROTTLED);
    }

    @Test
    void nonRootMangledFloats679DataInput() throws Exception {
        _testNonRootMangledFloats679(MODE_DATA_INPUT);
    }

    @Test
    void nonRootMangledFloats679Chars() throws Exception {
        _testNonRootMangledFloats679(MODE_READER);
    }

    // "/" tests still fail - need more work to distinguish "/" from "//" and "/*"
    @JacksonTestFailureExpected
    @Test
    void nonRootMangledInts679Bytes() throws Exception {
        _testNonRootMangledInts(MODE_INPUT_STREAM);
        _testNonRootMangledInts(MODE_INPUT_STREAM_THROTTLED);
    }

    @JacksonTestFailureExpected
    @Test
    void nonRootMangledInts679DataInput() throws Exception {
        _testNonRootMangledInts(MODE_DATA_INPUT);
    }

    @JacksonTestFailureExpected
    @Test
    void nonRootMangledInts679Chars() throws Exception {
        _testNonRootMangledInts(MODE_READER);
    }

    /*
    /**********************************************************************
    /* Helper methods
    /**********************************************************************
     */

    private void _testNonRootMangledFloats679(int mode) throws Exception {
        _testNonRootMangledFloats679(mode, "1.5x");
        _testNonRootMangledFloats679(mode, "1.5.00");
    }

    private void _testNonRootMangledFloats679(int mode, String value) throws Exception
    {
        // Also test with floats
        try (JsonParser p = createParser(mode, "[ "+value+" ]")) {
            assertEquals(JsonToken.START_ARRAY, p.nextToken());
            JsonToken t = p.nextToken();
            Double v = p.getDoubleValue();
            fail("Should have gotten an exception for '"+value+"'; instead got ("+t+") number: "+v);
        } catch (StreamReadException e) {
            verifyException(e, "expected ");
        }
    }

    private void _testNonRootMangledInts(int mode) throws Exception {
        _testNonRootMangledInts(mode, "100k");
        _testNonRootMangledInts(mode, "100/");
    }

    private void _testNonRootMangledInts(int mode, String value) throws Exception
    {
        // Also test with floats
        try (JsonParser p = createParser(mode, "[ "+value+" ]")) {
            assertEquals(JsonToken.START_ARRAY, p.nextToken());
            try {
                JsonToken t = p.nextToken();
                int v = p.getIntValue();
                fail("Should have gotten an exception for '" + value + "'; instead got (" + t + ") number: " + v);
            } catch (StreamReadException e) {
                verifyException(e, "expected ");
            }
        }
    }
}
