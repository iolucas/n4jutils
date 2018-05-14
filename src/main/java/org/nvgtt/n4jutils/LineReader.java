package org.nvgtt.n4jutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;


public class LineReader extends FileReader implements Iterable<String>, Iterator<String> {
	
	public LineReader(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	private String nextLine = null;

	public boolean hasNext() {
		
		this.nextLine = "";
		
		boolean hasNextFlag = false;
				
		while(true) {
			int c;
			try {
				c = this.read();
			} catch (IOException e) {
				break;
			}
			
			//If the stream has ended
			if(c == -1) {
				try {
					this.reset();
				} catch (IOException e) {} //Reset stream
				break; //Exit loop with flag false
			}
				
			hasNextFlag = true;
			
			//If we got some line termination char
			if(c == 13 || c == 10)
				break;
			
			this.nextLine += (char)c;
		}
		
		return hasNextFlag;
	}

	public String next() {
		// TODO Auto-generated method stub
		String nLine = this.nextLine;
		this.nextLine = null;
		return nLine;
	}

	public Iterator<String> iterator() {
		return this;
	}

}
