# Really fast Java implementation of Base64 Encoding
[![Build Status](https://travis-ci.org/thesimj/jBase64.svg?branch=master)](https://travis-ci.org/thesimj/jBase64)

Base64 encoder/decoder written in Java.

Base64 is a group of similar binary-to-text encoding schemes that represent binary data in an ASCII string format
by translating it into a radix-64 representation.

Base64 encoding schemes are commonly used when there is a need to encode binary data that needs to be stored and
transferred over media that is designed to deal with textual data. This is to ensure that the data remains intact
without modification during transport. Base64 is commonly used in a number of applications, including email via MIME,
and storing complex data in XML.

This library use simple and fast implementation. It's build on Java SE 1.7.

## Speed test result
```
Iteration: 1000000
Data lenght: 2048 byte
```
```
Lib: Base64Lib (Java 1.8)
	Encode: 4.459955 sec.
	Decode: 5.883765 sec.
	Equals? true

Lib: DatatypeConverter (Java 1.8)
	Encode: 5.182778 sec.
	Decode: 6.290830 sec.
	Equals? true

Lib: JBase64(this) (Java 1.8)
	Encode: 4.105961 sec.
	Decode: 3.054953 sec.
	Equals? true
```

## Installation
```java
Import src file /src/main/java/com/bubelich/jBase64.java for main class.
Import src file /src/test/java/com/bubelich/jBase64Testt.java for JUnit 4 tests.
```

## Usage

### Encoding:

```java
import com.bubelich.jBase64;
....
byte [] data = "Hello world!".getBytes();
String encdata = jBase64.encode(data, jBase64.ALPHABET.SAFE);
System.out.println("BaseZ85 encoded data: " + encdata); // Base64 encoded data: SGVsbG8gd29ybGQh
```

### Decoding:

```java
import com.bubelich.jBase64;
...
String encdata = "SGVsbG8gd29ybGQh";
byte [] data = jBase64.decode(encdata, jBase64.ALPHABET.SAFE,true);
System.out.println("BaseZ85 decoded data: " + new String(data)); // Base64 decoded data: Hello World!
```

### More info
Wiki - https://en.wikipedia.org/wiki/Base64