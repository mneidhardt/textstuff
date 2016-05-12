package dk.meem.text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    		System.err.println("Exception: " + e.getMessage());
    		System.err.println("Args: filename [lowerbound [encodingcharset]]");
    		System.err.println("lowerbound: All codepoints equal to or greater than this, will be reported. Default is 127.");
    		System.err.println("encodingcharset: E.g 'UTF-8'. Defaults to the JVM default charset.");
    		if (args.length < 3) {
    			System.err.println("Default charset: " + charset);
    		}
    	}
    }
    
    public void run(String filename, int lowerbound, String charset) throws IOException, FileNotFoundException, UnsupportedEncodingException {
    	System.out.println("#Reporting all codepoints above " + lowerbound + ".");
    	System.out.println("#Using charset " + charset + ".");

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset));

		String line;
		int linecount = 0;

		while ((line = br.readLine()) != null) {
			++linecount;
			
			String hits = containsCharsAbove(line, lowerbound);
			int spacecount = containsSpaces(line);

			if (!hits.isEmpty() || spacecount > 0) {
				System.out.println(linecount + " [s" + spacecount + "] :" + hits);
			}
			
		}

		br.close();
    }

    public String containsCharsAbove(String line, int lowerbound) {
		String n = Normalizer.normalize(line.trim(), Normalizer.Form.NFC);

		char[] chars = n.toCharArray();
		String hits = "";

		for (int i = 0; i < chars.length; i++) {
			int cp = Character.codePointAt(chars, i);
			if (cp > lowerbound) {
				hits += "[" + chars[i] + "=" + cp + " @" + (i + 1) + "] ";
			}
		}
		
		return hits;
    }

    public int containsSpaces(String line) {
    	Pattern pattern = Pattern.compile("\\s{2}");
        Matcher matcher = pattern.matcher(line);
        int found = 0;
        while (matcher.find()) {
        	++found;
        }
        
        return found;
    }
}
