/*
* Copyright (c) 2015, Bubelich Mykola (bubelich.com)
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
*
* Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* Neither the name of the copyright holder nor the names of its contributors
* may be used to endorse or promote products derived from this software without
* specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/

package com.bubelich;

import java.util.Objects;

/**
 * Author: Bubelich Mykola <br>
 * Date: 2015-06-01<br>
 *
 * <p>Implementation of Base64 (Normal and Url-Safe) data encoding/decoding</p>
 * <p>A very fast and memory efficient class to encode and decode BASE64 scheme</p>
 *
 * <ul>
 * <li><a name="basic"><b>Basic</b></a>
 * <p> Uses "The Base64 Alphabet" as specified in Table 1 of
 *     RFC 4648 and RFC 2045 for encoding and decoding operation.
 *     The encoder does not add any line feed (line separator)
 *     character. The decoder rejects data that contains characters
 *     outside the base64 alphabet.</p></li>
 *
 * <li><a name="url"><b>URL and Filename safe</b></a>
 * <p> Uses the "URL and Filename safe Base64 Alphabet" as specified
 *     in Table 2 of RFC 4648 for encoding and decoding. The
 *     encoder does not add any line feed (line separator) character.
 *     The decoder rejects data that contains characters outside the
 *     base64 alphabet.</p></li>
 *</ul>
 *
 * <p> Unless otherwise noted, passing a {@code null} argument to a
 * method of this class will cause a {@link java.lang.NullPointerException
 * NullPointerException} to be thrown.</p>
 *
 * @author Bubelich Mykola (bubelich.com)
 * @link https://github.com/thesimj/jBase64 (github)
 *
 */
public final class jBase64 {

    /**
     * Enumerate for alphabet type
     */
    public enum ALPHABET {
        SAFE {

            @Override
            public final char[] get(){
                return _ASAFE;
            }

            @Override
            public String regex() {
                return "^[0-9a-zA-Z-_=]{4,}[=]{0,2}$";
            }

        },
        BASE {

            @Override
            public final char[] get(){
                return _ABASE;
            }

            @Override
            public String regex() {
                return "^[0-9a-zA-Z+/]{4,}[=]{0,2}$";
            }
        };

        public abstract char[] get();
        public abstract String regex();
    }


    /**
     *  Alphabet base mode char array
     */
    private final static char[] _ABASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

    /**
     *  Alphabet safe mode char array
     */
    private final static char[] _ASAFE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_=".toCharArray();

    /**
     * Alphabet reverse index array
     */
    private final static int [] _ARINX = new int[] {    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,62,-1,62,-1,63,52,53,54,
            55,56,57,58,59,60,61,-1,-1,-1,0,-1,-1,-1,0,1,2,3,4,
            5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,
            24,25,-1,-1,-1,-1,63,-1,26,27,28,29,30,31,32,33,34,
            35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51};

    /** Private constructor **/
    private jBase64(){}

    /**
     * Encode input byte array as Base64 string.
     *
     * @param input byte[] Array of byte to encode.
     * @param alphas ALPHABET enumerate that use SAFE and NORMAL Base64 Encoding chars.
     *
     * @return String Encoded string that represent byte input as string in Base64 encoding.
     * @throws RuntimeException
     *
     **/
    public static String encode(byte[] input, ALPHABET alphas) throws RuntimeException{

        // Require Not Null //
        Objects.requireNonNull(input);
        Objects.requireNonNull(alphas);

        if(input.length == 0)
            throw new RuntimeException("Input is empty!");

        int buf = 0;

        int indx = 0;
        int len = input.length;

        int oindx = 0;
        int olen = (len % 3) == 0 ?
                len * 4 / 3 :
                4 - (len % 3) + len * 4 / 3;

        // Create buffer for output char //
        char[] out = new char[olen];
        char[] ALPHA = alphas.get();

        // Fill last two char by symbol "=" //
        out[olen - 1] = ALPHA[ALPHA.length - 1];
        out[olen - 2] = ALPHA[ALPHA.length - 1];

        // Starting cycle for parsing 3 byte to base64 //
        while (len >= 3) {

            // Copy next three bytes into lower 24 bits //
            buf = (input[indx++] & 0xFF) << 16 |
                    (input[indx++] & 0xFF) << 8 |
                    (input[indx++] & 0xFF);

            // Copy encode to output using alphabets index //
            out[oindx++] = ALPHA[(buf >> 18)  & 0x3F];
            out[oindx++] = ALPHA[(buf >> 12)  & 0x3F];
            out[oindx++] = ALPHA[(buf >> 6)   & 0x3F];
            out[oindx++] = ALPHA[buf          & 0x3F];

            len -= 3;
        }

        // Padding stage //
        switch (len) {
            case 2: {
                buf = (input[indx++] & 0xFF) << 16 |
                        (input[indx] & 0xFF) << 8;

                out[oindx++] = ALPHA[buf >> 18 & 0x3F];
                out[oindx++] = ALPHA[buf >> 12 & 0x3F];
                out[oindx] =  ALPHA[buf >> 6 & 0x3F];

                break;
            }
            case 1: {
                buf = (input[indx] & 0xFF) << 16;

                out[oindx++] = ALPHA[buf >> 18 & 0x3F];
                out[oindx] = ALPHA[buf >> 12 & 0x3F];

                break;
            }
        }

        // Construct new string from char array //
        return new String(out);
    }

    /**
     * Decode input string in Base64 to array of byte.
     *
     * @param input String
     * @param alphas ALPHABET enumerate that use SAFE and NORMAL Base64 Encoding chars.
     * @param validate boolean Use to validate input string, if true slow down compute speed to perform regex check.
     *
     * @return byte[] Return array of byte.
     * @throws RuntimeException
     *
     */
    public static byte [] decode(String input, ALPHABET alphas, boolean validate) throws RuntimeException{

        // Require Not Null //
        Objects.requireNonNull(input);
        Objects.requireNonNull(alphas);

        /* Try validate input string */
        if(validate && !input.matches(alphas.regex()))
            throw new IllegalArgumentException("Input string validation error!");

        int buf = 0;
        int indx = 0;
        int len = input.length();
        int oindex = 0;
        int olen = len * 3 / 4;

        // Calculate out length //
        // mod 2 used to determinate usage of padding scheme //
        if(len % 2 == 0)
            // When padding '=' & '==' used //
            if (input.endsWith("==")) {
                olen -= 2;
                len -= 2;
            }
            else if (input.endsWith("=")) {
                olen -= 1;
                len -= 1;
            }

        // Create buffer for output byte //
        byte[] out = new byte[olen];

        while (len >= 4){

            buf =   _ARINX[input.charAt(indx++)] << 18 |
                    _ARINX[input.charAt(indx++)] << 12 |
                    _ARINX[input.charAt(indx++)] << 6 |
                    _ARINX[input.charAt(indx++)];

            out[oindex++] = (byte)(buf >> 16);
            out[oindex++] = (byte)(buf >> 8);
            out[oindex++] = (byte)(buf);

            len -= 4;
        }

        // Padding stage //
        switch (olen - oindex){

            // 1 byte padding //
            case 2 : {
                buf =   _ARINX[input.charAt(indx++)] << 18 |
                        _ARINX[input.charAt(indx++)] << 12 |
                        _ARINX[input.charAt(indx)] << 6;

                out[oindex++] = (byte)(buf >> 16);
                out[oindex] = (byte)(buf >> 8);

                break;
            }

            // 2 byte padding //
            case 1 : {
                buf =   _ARINX[input.charAt(indx++)] << 18 |
                        _ARINX[input.charAt(indx)] << 12;

                out[oindex] = (byte)(buf >> 16);

                break;
            }

        }

        // Return output array of bytes //
        return out;
    }

    public static boolean validate(String input, ALPHABET alphabet){
        return input.matches(alphabet.regex());
    }

}