package tools.jackson.core.unittest.read;

import org.junit.jupiter.api.Test;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.exc.StreamReadException;

import static org.junit.jupiter.api.Assertions.fail;

// Tests for [core#105] ("eager number parsing misses errors")
class ParserErrorHandlingDataInput105Test
    extends tools.jackson.core.unittest.JacksonCoreTestBase
{
    @Test
    void mangledIntsDataInput() throws Exception {
        _testMangledNonRootInts(MODE_DATA_INPUT);
    }

    @Test
    void mangledFloatsDataInput() throws Exception {
        _testMangledNonRootFloats(MODE_DATA_INPUT);
    }

    /*
    /**********************************************************
    /* Helper methods
    /**********************************************************
     */

    private void _testMangledNonRootInts(int mode)
    {
        try (JsonParser p = createParser(mode, "[ 123true ]")) {
            assertToken(JsonToken.START_ARRAY, p.nextToken());
            JsonToken t = p.nextToken();
            fail("Should have gotten an exception; instead got token: "+t);
        } catch (StreamReadException e) {
            verifyException(e, "expected space");
        }
    }

    private void _testMangledNonRootFloats(int mode)
    {
        try (JsonParser p = createParser(mode, "[ 1.5false ]")) {
            assertToken(JsonToken.START_ARRAY, p.nextToken());
            JsonToken t = p.nextToken();
            fail("Should have gotten an exception; instead got token: "+t);
        } catch (StreamReadException e) {
            verifyException(e, "expected space");
        }
    }
}
