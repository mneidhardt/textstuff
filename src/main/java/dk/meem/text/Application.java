package dk.meem.text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;

public class Application {

    public static void main( String[] args ) {
    	int lowerbound = 127;	// All code points above this will be reported.
    	String charset = Charset.defaultCharset().name();

    	try {
    		if (args.length == 0) {
    			throw new Exception("Filename missing.");
    		}
    		if (args.length > 1) {
    			lowerbound = Integer.parseInt(args[1]);
    			if (args.length > 2) {
    				charset = args[2];
    			}
    		}
        	new Application().run(args[0], lowerbound, charset);
    	} catch (Exception e) {
    		System.err.println("Args: filename [lowerbound [encodingcharset]]");
    		System.err.println("lowerbound: All codepoints equal to or greater than this, will be reported. Default is 127");
    		System.err.println("encodingcharset: E.g 'UTF8'. Default is ??");
    		
    	}
    }
    
    public void run(String filename, int lowerbound, String charset) throws FileNotFoundException, UnsupportedEncodingException {
    	System.out.println("Using charset " + charset);
    	//try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        try {
        	BufferedReader br = new BufferedReader(
     			   new InputStreamReader(
     	                      new FileInputStream(filename), charset));
        	
			String line;
			int linecount=0;
			
			while ((line = br.readLine()) != null) {
				++linecount;
				String n = Normalizer.normalize(line.trim(), Normalizer.Form.NFC);

				char[] chars = n.toCharArray();
				String hits = "";

				for (int i=0; i<chars.length; i++) {
					int cp = Character.codePointAt(chars, i);
					if (cp > lowerbound) {
						hits += "[" + chars[i] + "=" + cp + " @" + (i+1) + "] ";
					}
				}
				
				if ( ! hits.isEmpty()) {
					System.out.println(linecount + ":" + hits);
				}
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
