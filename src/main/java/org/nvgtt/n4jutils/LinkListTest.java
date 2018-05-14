package org.nvgtt.n4jutils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LinkListTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Initiated.");
		
		//FileReader fr = new FileReader("links_list_test.txt");
		
		LineReader lr = new LineReader("links_list_test.txt");
	
		
		for(String line : lr) {
			if(line == "")
				continue;
			
			String[] links = line.split("\t\t");
			
			
			System.out.println(links[0] + " - " + links[1]);
		}
		
		//for(int i = 0;i< 10;i++)
			//System.out.println(readLine(fr));
		
		
		//int c = 0;
		//do {
			//c = fr.read();
			//System.out.println(c);
		//} while(c != -1);
		
		System.out.println("Finished.");
	}
	
	public static String readLine(FileReader fr) throws IOException {
		String line = "";
		
		while(true) {
			int c = fr.read();
			
			//Check end of stream or new character
			if(c == -1 || c == 13)
				break;
			
			line += (char) c;			
		}
		
		return line;
	}

}
