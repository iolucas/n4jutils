package org.nvgtt.n4jutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;


public class DumpLoader {
	
	static void print(Object obj) {
		System.out.println(obj);
	}
	
	static JSONObject readJsonFile(String filePath) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object object = parser.parse(new FileReader(filePath));
		//convert Object to JSONObject
		JSONObject jsonObject = (JSONObject)object;
		return jsonObject;
	}
	
	static HashMap<String, Object> JSONToHashMap(JSONObject jsonObj) {
		
		HashMap<String, Object> hashMapObj = new HashMap<String, Object>();
		
		for(Object key : jsonObj.keySet()) {
			Object value = jsonObj.get(key);
			hashMapObj.put((String) key, value);
		}
		
		return hashMapObj;
	}

	public static void main(String[] args) {
		//C:\neo4j-enterprise-3.1.0\data\databases\test_java.db
		
		String dbFilePath = args[0];
		String nodesFilePath = args[1];
		String linksFilePath = args[2];
		
		HashMap<String, Long> inMemoryIndex = new HashMap<String, Long>();
		
		try {
			
			JSONArray nodes = (JSONArray) readJsonFile(nodesFilePath).get("nodes");
			JSONArray links = (JSONArray) readJsonFile(linksFilePath).get("links");
						
			BatchInserter inserter = BatchInserters.inserter(new File(dbFilePath));
			
			//Fill nodes
			for(Object nodeObj : nodes) {
				JSONObject node = (JSONObject) nodeObj;
				HashMap<String, Object> nodeProperties = JSONToHashMap(node); //Batch inserter accepts HashMaps as properties
				long nodeId = inserter.createNode(nodeProperties, Labels.Article);
				
				inMemoryIndex.put((String) node.get("id"), nodeId);
			}
			
			//Fill links
			for(Object linkObj : links) {
				try {
					
					JSONObject link = (JSONObject) linkObj;
					HashMap<String, Object> linkProperties = JSONToHashMap(link);
					
					long originId = inMemoryIndex.get(link.get("origin"));
					long targetId = inMemoryIndex.get(link.get("target"));
					
					inserter.createRelationship(originId, targetId, Relations.Needs, linkProperties);
				}
				catch(Exception e) {
					print(e.toString());
				}
			}
			
			inserter.shutdown();
		
			print("Done inserting.");            
        }
		catch(Exception e) {
			print(e.toString());
		}
	}

}
