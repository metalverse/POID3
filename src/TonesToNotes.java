import jm.music.data.*;
import jm.music.data.Phrase;

import java.awt.List;
import java.util.ArrayList;

import jm.JMC;
import jm.util.*;

public class TonesToNotes implements JMC {
	
	 String filename;
	 ArrayList<String> toneList = new ArrayList<String>();
	 ArrayList<Integer> timeList = new ArrayList<Integer>();
	 
	 String tunesTab[] = {"C2","Cis2","D2","Dis2","E2","F2","Fis2","G2","Gis2","A2","Ais2","H2",
				"C1","Cis1","D1","Dis1","E1","F1","Fis1","G1","Gis1","A1","Ais1","H1",
				"C","Cis","D","Dis","E","F","Fis","G","Gis","A","Ais","H",
				"c","cis","d","dis","e","f","fis","g","gis","a","ais","h",
				"c1","cis1","d1","dis1","e1","f1","fis1","g1","gis1","a1","ais1","h1",
				"c2","cis2","d2","dis2","e2","f2","fis2","g2","gis2","a2","ais2","h2",
				"c3","cis3","d3","dis3","e3","f3","fis3","g3","gis3","a3","ais3","h3",
				"c4","cis4","d4","dis4","e4","f4","fis4","g4","gis4","a4","aisw5","h5",
				"c6","cis6","d6","dis6","e6","f6","fis6","g6","gis6","a6","ais6","h6"};
	 
	 public TonesToNotes(){

	 }
	 
	 public TonesToNotes(String filename, ArrayList<String> toneList, ArrayList<Integer> timeList){
		 this.filename = filename;
		 this.toneList = toneList;
		 this.timeList = timeList;
	 }
	 //12
	 public void GenerateScore(){
		 Score score = new Score("Row Your Boat");
		 //Note n = new Note(72, 1);  // Middle C (quarter note)
		// Note n2 = new Note(G4, CROTCHET);
		 Phrase phr = new Phrase();
		 System.out.println(toneList.size());
		 for(int i=0; i<toneList.size(); i++){
			 for(int j=0; j<tunesTab.length; j++){
				 if(toneList.get(i) == tunesTab[j]){
					 phr.addNote(j+12,  Math.round(timeList.get(i)/8.0)/2.0);
				 }
			 }
		 }
		 Part p = new Part();
		 p.add(phr);
		 phr.setTempo(100);
		 View.notate(phr);
	 }
	 
	 

}
