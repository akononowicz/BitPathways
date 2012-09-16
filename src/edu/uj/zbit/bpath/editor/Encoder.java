/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class Encoder {

	static public String encrypt(String str) {
		// zamiana stringu na postc w kodzie base64

		try {
			str = Base64.encode(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// szyfrowanie kodu base64 szyfrem Cezara (root13)
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'A' && c <= 'Z') c -= 13;
            sb.insert(i, c);
        }
		str = sb.toString();
		
		
		return str;
	}

	static public String decrypt(String str) throws Exception {
		// dekodujemy otrzymany napis przy pomocy szyfru Cezara
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'A' && c <= 'Z') c -= 13;
            sb.insert(i, c);
        }
		str = sb.toString();
		
		// dekodujemy otrzymany napis przy pomocy algorytmu BASE64
    	byte[] orig = new sun.misc.BASE64Decoder().decodeBuffer(str);
		
    	// zapis tablicy bajtów jak string przy użyciu kodowania 'UTF-8'
		String st = new String(orig, "UTF-8");
        return st;
    }
	
}
