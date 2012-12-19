package univ_angers.virtuose.utils;

import java.io.*;

import org.apache.log4j.Logger;

import univ_angers.virtuose.search.Search;

public class HandleFiles {
	private static Logger log = Logger.getLogger(HandleFiles.class);

	String type;
	String filePath;
	
	public HandleFiles(String t, String fP){
		this.type = (t.equalsIgnoreCase("f")?"File":"Directory");
		this.filePath = fP;
	}
	
	
	public void handle(){
		try{
			File f = new File(filePath);
			boolean success = f.exists();
			if (this.type.equals("File")){
				success = success && f.isFile(); 
				if(!success){
					success = f.createNewFile();
				}
			}else{
				success = success && f.isDirectory();
				if(!success){
					success = f.mkdir();
				}
			}
			
			if (success) {
				System.out.println(this.type+" : " 
					+ this.filePath + " created");
			}  
		}catch (Exception e){//Catch exception if any
			System.err.println("Error at "+this.filePath+"("+this.type+") creation : " + e.getMessage());
		}
	}
	
	public void addText(String text){
		//System.out.println("attempt to write in "+this.filePath);
		if (this.type.equals("File")){
			FileWriter fw;
			try {
				fw = new FileWriter(this.filePath, true);
				BufferedWriter out = new BufferedWriter(fw);
				out.write(text);
				out.write("\n");
				out.close();
			} catch (Exception e) {
				System.err.println("Error : " + e.getMessage());
			}

		} else {
			System.out.println("Can not write in "+this.filePath);
		}
	}
	
	
}