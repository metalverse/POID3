import java.awt.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class FrequencyToTone {

	private Map<Double, String> soundsMap = new HashMap<Double, String>();
	private ArrayList<String> tunesList = new ArrayList<String>();
	private ArrayList<Integer> timeList = new ArrayList<Integer>();
	
	String tunesTab[] = {"C2","Cis2","D2","Dis2","E2","F2","Fis2","G2","Gis2","A2","Ais2","H2",
			"C1","Cis1","D1","Dis1","E1","F1","Fis1","G1","Gis1","A1","Ais1","H1",
			"C","Cis","D","Dis","E","F","Fis","G","Gis","A","Ais","H",
			"c","cis","d","dis","e","f","fis","g","gis","a","ais","h",
			"c1","cis1","d1","dis1","e1","f1","fis1","g1","gis1","a1","ais1","h1",
			"c2","cis2","d2","dis2","e2","f2","fis2","g2","gis2","a2","ais2","h2",
			"c3","cis3","d3","dis3","e3","f3","fis3","g3","gis3","a3","ais3","h3",
			"c4","cis4","d4","dis4","e4","f4","fis4","g4","gis4","a4","aisw5","h5",
			"c6","cis6","d6","dis6","e6","f6","fis6","g6","gis6","a6","ais6","h6"};
	
	double[] freqTab = new double[tunesTab.length];
	int[] values;
	
	public FrequencyToTone(){
		double freq = 16.351598;	
		
		
		int iterator = 0;
		double tmp = Math.pow(2,(1.0/12));
		System.out.println(tmp);
		while(iterator < tunesTab.length){
			soundsMap.put(freq, tunesTab[iterator]);
			freqTab[iterator] = freq;
			iterator++;
			freq = (Math.pow(tmp,(16/15))) * freq;
		}
		
		/*for (int i=0; i<tunesTab.length; i++){
			System.out.println(tunesTab[i] + " : " + freqTab[i] + "Hz");
		}*/
	}
	
	public String getTone(double freq){
		int iterator = 0;
		while(freq > freqTab[iterator]){
			iterator++;
		}
		if(iterator<tunesTab.length-1){
			if(freq-freqTab[iterator] > freq-freqTab[iterator+1] ){
				addToList(tunesTab[iterator+1]);
				return (tunesTab[iterator+1] );
			}
			else{
				addToList(tunesTab[iterator]);
				return (tunesTab[iterator]);
			}
		}else{
			addToList(tunesTab[iterator]);
			return tunesTab[iterator];
		}
	}
	
	//Sprawdzanie liczby wyst¹pieñ ostatnich dzwiêków a nie dzwieków bo zawsze ró¿ne
	
	public void addToList(String tone){
		if(tunesList.size()>1){
			String lastTone = tunesList.get(tunesList.size()-1);
			String befLastTone = tunesList.get(tunesList.size()-2);
			int lastToneOccurence = timeList.get(timeList.size()-1);
			if(tone.compareTo(lastTone) == 0){
				//System.out.println("Ten sam dzwiek " + tone);
				timeList.set((timeList.size()-1), timeList.get(timeList.size()-1) + 1);
			}else if(tone.compareTo(lastTone) != 0 && lastToneOccurence == 1 && tone.compareTo(befLastTone) == 0){
				//System.out.println("Dzwiek taki jak przedostatni, usuwam poprzedni i przedluzam: " + tone);
				tunesList.remove(tunesList.size()-1);
				timeList.remove(timeList.size()-1);
				timeList.set((timeList.size()-1), timeList.get(timeList.size()-1) + 2);
			}else if(tone.compareTo(lastTone) != 0 && lastToneOccurence <= 1){
				//System.out.println("Nowy dzwiek " + tone + ", usuwam poprzedni " + lastTone);
				tunesList.remove(tunesList.size()-1);
				timeList.remove(timeList.size()-1);
				tunesList.add(tone);
				timeList.add(1);
			}else if(tone.compareTo(lastTone) != 0 && lastToneOccurence > 1){
				//System.out.println("Nowy dzwiek " + tone);
				tunesList.add(tone);
				timeList.add(1);
			}
		}
		else{
			String lastTone = "null";
			if(tunesList.size() == 1) lastTone = tunesList.get(tunesList.size()-1);
			if(tone.compareTo(lastTone) == 0){
				//System.out.println("Ten sam dzwiek " + tone);
				timeList.set((timeList.size()-1), timeList.get(timeList.size()-1) + 1);
			}else{
				//System.out.println("Dodaje nowy dzwiek " + tone);
				tunesList.add(tone);
				timeList.add(1);
			}
		}
	}
	
	public void convertToValuesTab(){
		int size = 0;
		for(int i=0; i<timeList.size(); i++){
			size += timeList.get(i);
		}
		values = new int[size];
		int j = 0;
		int k = 0;
		for(int i=0; i<timeList.size(); i++){
			j = timeList.get(i);
			do{
				for(int l=0; l<tunesTab.length; l++){
					if(tunesList.get(i).compareTo(tunesTab[l]) == 0){
						values[k] = (int)freqTab[l]*10;
						break;
					}
				}
				j--;
				k++;
			}while(j>0);
		}
		/*System.out.println("\n\n\n*************");
		for(int i=0; i<values.length; i++){
			System.out.println(i + " " + values[i]);
		}*/
	}
	
	
	public void displayTones(){
		System.out.println("Dlugosc tablic:" + tunesList.size() + "|" + timeList.size());
		for(int i = 0; i < tunesList.size(); i++) {
            System.out.println("Dzwiek " + tunesList.get(i) + ", ilosc ramek:" + timeList.get(i));
        }
	}
}
