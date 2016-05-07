package dk.meem.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Application {

    public static void main( String[] args ) {
    	int lowerbound = 127;	// All code points above this will be reported. 
    	try {
    		if (args.length == 0) {
    			throw new Exception("Filename missing.");
    		}
    		if (args.length > 1) {
    			lowerbound = Integer.parseInt(args[1]);
    		}
        	new Application().run(args[0], lowerbound);
    	} catch (Exception e) {
    		System.err.println("Args: filename [lowerbound]");
    		System.err.println("lowerbound: All codepoints equal to or greater than this, will be reported. Default is 127");
    	}
    }
    
    public void run(String filename, int lowerbound) {
    	List<String> charsfound = new ArrayList<String>();
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

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
						hits += "[" + chars[i] + "=" + cp + "] ";
					}
				}
				
				if ( ! hits.isEmpty()) {
					charsfound.add(linecount + ":" + hits);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	for (String entry : charsfound) {
    		System.out.println(entry);
    	}
    }
    
}
