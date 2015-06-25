package com.bubelich;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

public class jBase64Test extends Assert{

    private static byte [] input_t1 = SecureRandom.getSeed(3*1024 + 0);
    private static byte [] input_t2 = SecureRandom.getSeed(3*1024 + 1);
    private static byte [] input_t3 = SecureRandom.getSeed(3*1024 + 2);

    @Test
    public void testEncode() throws Exception {

        // For one byte //
        assertEquals("Pw==",jBase64.encode(new byte[]{0x3f},jBase64.ALPHABET.BASE));

        // For two byte //
        assertEquals("P08=",jBase64.encode(new byte[]{0x3f,0x4f}, jBase64.ALPHABET.BASE));

        // For three bytes //
        assertEquals("P09f", jBase64.encode(new byte[]{0x3f, 0x4f, 0x5f}, jBase64.ALPHABET.BASE));

        // Text without padding //
        assertEquals("Encode input T1", DatatypeConverter.printBase64Binary(input_t1), jBase64.encode(input_t1, jBase64.ALPHABET.BASE));

        // Text with 1th padding //
        assertEquals("Encode input T2", DatatypeConverter.printBase64Binary(input_t2), jBase64.encode(input_t2, jBase64.ALPHABET.BASE));

        // Text with 2th padding //
        assertEquals("Encode input T3", DatatypeConverter.printBase64Binary(input_t3), jBase64.encode(input_t3, jBase64.ALPHABET.BASE));

        // For null //
        try { jBase64.encode(null, jBase64.ALPHABET.BASE); }
        catch (NullPointerException ex){ /* OK */}
        catch (Exception ex){ throw ex; }

        // Empty byte //
        try { jBase64.encode(new byte[]{}, jBase64.ALPHABET.BASE); }
        catch (RuntimeException ex){ /* OK */}
        catch (Exception ex){ throw ex; }

    }

    @Test
    public void testDecode() throws Exception {

        // With padding //
        assertEquals("any carnal pleasur",new String(jBase64.decode("YW55IGNhcm5hbCBwbGVhc3Vy", jBase64.ALPHABET.BASE,true)));
        assertEquals("any carnal pleasu",new String(jBase64.decode("YW55IGNhcm5hbCBwbGVhc3U=", jBase64.ALPHABET.BASE,true)));
        assertEquals("any carnal pleas",new String(jBase64.decode("YW55IGNhcm5hbCBwbGVhcw==", jBase64.ALPHABET.BASE,true)));


        // Without padding //
        assertEquals("any carnal pleasur",new String(jBase64.decode("YW55IGNhcm5hbCBwbGVhc3Vy", jBase64.ALPHABET.BASE,true)));
        assertEquals("any carnal pleasu",new String(jBase64.decode("YW55IGNhcm5hbCBwbGVhc3U", jBase64.ALPHABET.BASE,true)));
        assertEquals("any carnal pleas",new String(jBase64.decode("YW55IGNhcm5hbCBwbGVhcw", jBase64.ALPHABET.BASE,true)));

        // Normal //
        assertArrayEquals(input_t1,jBase64.decode(jBase64.encode(input_t1, jBase64.ALPHABET.BASE), jBase64.ALPHABET.BASE,true));
        assertArrayEquals(input_t2,jBase64.decode(jBase64.encode(input_t2, jBase64.ALPHABET.BASE), jBase64.ALPHABET.BASE,true));
        assertArrayEquals(input_t2,jBase64.decode(jBase64.encode(input_t2, jBase64.ALPHABET.BASE), jBase64.ALPHABET.BASE,true));

        // Safe //
        assertArrayEquals(input_t1,jBase64.decode(jBase64.encode(input_t1, jBase64.ALPHABET.SAFE), jBase64.ALPHABET.SAFE,true));
        assertArrayEquals(input_t2,jBase64.decode(jBase64.encode(input_t2, jBase64.ALPHABET.SAFE), jBase64.ALPHABET.SAFE,true));
        assertArrayEquals(input_t2,jBase64.decode(jBase64.encode(input_t2, jBase64.ALPHABET.SAFE), jBase64.ALPHABET.SAFE,true));

        // Exception expect //
        try { jBase64.decode("", jBase64.ALPHABET.BASE,true); }
        catch (IllegalArgumentException ex){ /* OK */}
        catch (Exception ex){ fail("Something wrong"); }

        try { jBase64.decode(null, jBase64.ALPHABET.BASE,true); }
        catch (NullPointerException ex){ /* OK */}
        catch (Exception ex){ throw ex; }

        // Wrong padding //
        try { jBase64.decode("YW55IGNhcm5hbCBwbGVhcw=x", jBase64.ALPHABET.BASE, true); }
        catch (IllegalArgumentException ex){ /* OK */}
        catch (Exception ex){ fail("Something wrong"); }

        try { jBase64.decode("YW55IGNhcm5hbCBwbGVhcw=x", jBase64.ALPHABET.BASE,true); }
        catch (IllegalArgumentException ex){ /* OK */}
        catch (Exception ex){ fail("Something wrong"); }

    }
}