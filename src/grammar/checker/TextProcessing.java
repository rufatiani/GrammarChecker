/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker;

import grammar.checker.service.CorpusService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ru'fatiani
 */
public class TextProcessing {
    
    public ArrayList<String> readFile(String filename) throws FileNotFoundException{
        ArrayList<String> words = new ArrayList();
        
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            StringBuilder sb = new StringBuilder();
            String currentLine;
            
            while((currentLine = br.readLine()) != null){
                words.add(currentLine);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        
        return words;
    }
    
    public boolean readFileCorpus(String filename) throws FileNotFoundException, IOException, SQLException{
        CorpusService cc = new CorpusService();
        boolean status = false;
        
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            StringBuilder sb = new StringBuilder();
            String currentLine;
            
            ArrayList<String> words = new ArrayList();
            while((currentLine = br.readLine()) != null){
                words.add(currentLine);
                
                if(currentLine.equals("."+ "\t" + "Z")){
                    cc.improveCorpus(words);
                    words = new ArrayList();
                }
            }
            
            status = true;
        } catch(IOException e){
            e.printStackTrace();
        }
        
        return status;
    }
    
    public void writeFile(File file, ArrayList<String> content) throws IOException{
	BufferedWriter bw = null;
	FileWriter fw = null;

	try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            
            for(int i=0;i<content.size();i++){
                bw.write(content.get(i));
            }        
            //System.out.println("Done");
	} catch (IOException e) {
            e.printStackTrace();
	} finally {
            try {
    		if (bw != null)
                    bw.close();

		if (fw != null)
                    fw.close();
            } catch (IOException ex) {
		ex.printStackTrace();
            }
	}
    }
}
