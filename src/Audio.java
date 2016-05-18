
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;



public class Audio extends JFrame implements ActionListener{
	
		private static final long serialVersionUID = 1L;
		double[] buffer;
		Random random = new Random();	 
		private JButton autokorelacjaButton, cepstrumButton, wykresAutokorelacjaButton, wykresCepstrumButton, saveSingleButton, saveSeqButton;
		private JLabel autokorelacjaLabel, cepstrumLabel, wynikLabel, wykresLabel;
		private JTextField autokorelacjaField, cepstrumField, wynikField, wykresField, cutCepstrumLabel, cutWykresCepstrumLabel;
		private FrequencyToTone ftt;
		
		JPanel panel = new JPanel();
		JFrame f = new JFrame("JPanel");
		
		
		int iloscRamek;
		public int[] values;
		int frames;
		int ramka;

		public double[] tmp;
		public double[] tmp2;
		public double[] dbR;
		public double[] dbI;
	 
	public Audio(){
		
		panel.setLayout(null);	
		f.setSize(370, 320);
		
		autokorelacjaLabel = new JLabel("Autokorelacja: Ramka?");
		autokorelacjaLabel.setBounds(10, 10, 150, 30);	
		panel.add(autokorelacjaLabel);			
		
		autokorelacjaField = new JTextField(2);
		autokorelacjaField.setBounds(150, 10, 40, 30);
		panel.add(autokorelacjaField);	
		
		autokorelacjaButton = new JButton("Wykonaj");
		autokorelacjaButton.setBounds(200, 10, 100, 30);	
		autokorelacjaButton.addActionListener(this);
		panel.add(autokorelacjaButton);
				
		cepstrumLabel = new JLabel("Cepstrum: Ramka?");
		cepstrumLabel.setBounds(10, 50, 150, 30);	
		panel.add(cepstrumLabel);			
		
		cepstrumField = new JTextField(2);
		cepstrumField.setBounds(150, 50, 40, 30);
		panel.add(cepstrumField);	
		
		cepstrumButton = new JButton("Wykonaj");
		cepstrumButton.setBounds(200, 50, 100, 30);	
		cepstrumButton.addActionListener(this);
		panel.add(cepstrumButton);		
		
		cutCepstrumLabel = new JTextField(2);
		cutCepstrumLabel.setBounds(305, 50, 40, 30);
		panel.add(cutCepstrumLabel);	
		
		int c0 = 25;
		cutCepstrumLabel.setText(Integer.toString(c0));
		
		wynikLabel = new JLabel("Wynik - ramek:");
		wynikLabel.setBounds(10, 100, 150, 30);	
		panel.add(wynikLabel);			
		
		wynikField = new JTextField(2);
		wynikField.setBounds(150, 100, 40, 30);
		wynikField.setEditable(false);
		panel.add(wynikField);			
		
		wykresLabel = new JLabel("Wykres z ramki:");
		wykresLabel.setBounds(10, 140, 150, 30);	
		panel.add(wykresLabel);			
		
		wykresField = new JTextField(2);
		wykresField.setBounds(150, 140, 40, 30);
		panel.add(wykresField);	
		
		wykresAutokorelacjaButton = new JButton("A");
		wykresAutokorelacjaButton.setBounds(200, 140, 50, 30);	
		wykresAutokorelacjaButton.addActionListener(this);
		panel.add(wykresAutokorelacjaButton);	
		
		wykresCepstrumButton = new JButton("C");
		wykresCepstrumButton.setBounds(250, 140, 50, 30);	
		wykresCepstrumButton.addActionListener(this);
		panel.add(wykresCepstrumButton);	
		
		cutWykresCepstrumLabel = new JTextField(2);
		cutWykresCepstrumLabel.setBounds(305, 140, 40, 30);
		panel.add(cutWykresCepstrumLabel);	
		
		int c1 = 26;
		cutWykresCepstrumLabel.setText(Integer.toString(c1));
		
		
		
		saveSingleButton = new JButton("Single .wav");
		saveSingleButton.setBounds(30, 200, 120, 60);	
		saveSingleButton.addActionListener(this);
		panel.add(saveSingleButton);	
		
		saveSeqButton = new JButton("Seq .wav");
		saveSeqButton.setBounds(170, 200, 120, 60);	
		saveSeqButton.addActionListener(this);
		panel.add(saveSeqButton);	
			
	    FileDialog fd = new FileDialog(f, "wczytaj audio", FileDialog.LOAD);
	    fd.setLocationRelativeTo(null);    
	    fd.setVisible(true);
	    String katalog = fd.getDirectory();
	    String plik = fd.getFile(); 
	    File audioFile = new File(katalog + plik);
    
	    loadFile(audioFile);		    

	    f.add(panel);   
	    pack();	    
	    f.setLocationRelativeTo(null);
	    f.setVisible(true);    
	    
	    ftt = new FrequencyToTone();
	}
	

	////////////////////////////////////////////////////
	//												  //
	//					Autokorelacja		  		  //
	//												  //
	////////////////////////////////////////////////////
	
	int autocorelation(int start, int ramka) {
		for (int i = 0; i < ramka; i++) {
			tmp2[i] = buffer[i + start];
		}
		boolean falling = true;
		double MAX = autokorelacja(tmp2, 0);
		double bottom = MAX;
		double best = 0;
		double best_m = 1;
		double[] vall = new double[ramka];
		for (int m = 0; m < ramka; m++) {
			double val = autokorelacja(tmp2, m);
			vall[m] = val;
			if (falling) {
				if (val < bottom) {
					bottom = val;
				} else if ((MAX - bottom) * 0.98 > MAX - val) {
					falling = false;
					best = val;
				}
			} else {
				if (val > best) {
					best = val;
					best_m = m;
				}
			}
		}
		int mnoznik = 1;
		if (frames > 44100) {
			mnoznik = frames / 44100;
		}
		ftt.setMultiplier(mnoznik);
		
		System.out.println(frames / (best_m * mnoznik) + " Hz | dzwiek:" + ftt.getTone(frames / (best_m * mnoznik)));
		//String b = ftt.getTone(frames / (best_m * mnoznik));
		return (int) (frames / best_m);
	}
	
	void autocorelationWykres(int start, int ramka) {
		for (int i = 0; i < ramka; i++) {
			tmp2[i] = buffer[i + start];
		}
		boolean falling = true;
		double MAX = autokorelacja(tmp2, 0);
		double bottom = MAX;
		double best = 0;
		double best_m = 1;
		double[] vall = new double[ramka];
		for (int m = 0; m < ramka; m++) {
			double val = autokorelacja(tmp2, m);
			vall[m] = val;
			if (falling) {
				if (val < bottom) {
					bottom = val;
				} else if ((MAX - bottom) * 0.9 > MAX - val) {
					falling = false;
					best = val;
				}
			}else {
				if (val > best) {
					best = val;
					best_m = m;
				}
			}
		}

		int mnoznik = 1;
		if (frames > 44100) {
			mnoznik = frames / 44100;
		}
		ftt.setMultiplier(mnoznik);
		System.out.println(frames / (best_m * mnoznik) + " Hz");

		new Chart(vall);
	}
	
	
	double autokorelacja(double[] buffer, int m) {
		
		double suma=0;	
//		for(int i = 0 ; i < buffer.length; i++){			
//			suma += buffer[i] * buffer[(i+m) % 44100];		
//		}
		for (int n = 0; n + m < buffer.length; n++) {
			suma += buffer[n] * buffer[(n + m)];
		}		
			//System.out.println(suma);
		return suma;	
	}
		
	
	
	////////////////////////////////////////////////////
	//												  //
	//					Cepstrum			  		  //
	//												  //
	////////////////////////////////////////////////////
	
	int cepstrum(int start, int ramka, int cut) {

		for (int i = 0; i < ramka; i++) {
			tmp[i] = buffer[i + start];
		}

		HammingWindow(tmp);
		initFFT(tmp);
		FFT(dbR, dbI);
		modul(dbR);
		for (int i = 0; i < dbR.length; i++) {
			dbR[i] = dbR[i] * 10000000;
		}
		logarytm(dbR);
		initFFT(dbR);
		FFT(dbR, dbI);
		modul(dbR);
		// printMax(dbR);
		// return(printMax(dbR));

		double max = 0;
		int index = 0;
		for (int i = cut; i < dbR.length / 2; i++) {
			if (dbR[i] > max) {
				max = dbR[i];
				index = i;
			}
		}

		int mnoznik = 1;
		if (frames > 44100) {
			mnoznik = frames / 44100;
		}
		ftt.setMultiplier(mnoznik);
		System.out.println(frames / (index * mnoznik) + " Hz | dzwiek:" + ftt.getTone(frames / (index * mnoznik)));
		//String b = ftt.getTone(frames / (index * mnoznik));
		return frames / index;
	}
	
	
	void cepstrumWykres(int start, int ramka, int cut) {

		for (int i = 0; i < ramka; i++) {
			tmp[i] = buffer[i + start];
		}

		HammingWindow(tmp);
		initFFT(tmp);
		FFT(dbR, dbI);
		modul(dbR);
		for (int i = 0; i < dbR.length; i++) {
			dbR[i] = dbR[i] * 10000000;
		}
		logarytm(dbR);
		initFFT(dbR);
		FFT(dbR, dbI);
		modul(dbR);
		// printMax(dbR);

		new Chart(dbR);

		double max = 0;
		int index = 0;
		for (int i = cut; i < dbR.length / 2; i++) {
			if (dbR[i] > max) {
				max = dbR[i];
				index = i;
			}
		}

		int mnoznik = 1;
		if (frames > 44100) {
			mnoznik = frames / 44100;
		}
		ftt.setMultiplier(mnoznik);
		System.out.println(frames / (index * mnoznik) + " Hz");
		//return frames / index;
	}

	
	////////////////////////////////////////////////////
	//												  //
	//					funkcje	cepstrum	  		  //
	//												  //
	////////////////////////////////////////////////////
	
	
	public void initFFT(double[] buff) {
		for (int i = 0; i < buff.length; i++) {
			dbR[i] = buff[i];
			dbI[i] = 0;
		}
	}
	
	public void printFFT(double[] dbR) {
		for (int i = 0; i < dbR.length; i++) {
			System.out.println(dbR[i]);
		}

	}
	
	public void HammingWindow(double[] buff) {
		for (int i = 0; i < buff.length; i++) {
			buff[i] = 0.53836 - 0.46163 * Math.cos(2 * Math.PI * buff[i] / buff.length - 1);
		}
	}
	
	public void modul(double[] dbR){			
		for (int i = 0; i < dbR.length; i++) {
			//dbR[i] = Math.abs(dbR[i]);
			dbR[i] = Math.sqrt(dbR[i]*dbR[i] + dbI[i]*dbI[i]);
		}
	}
	
	public void logarytm(double[] dbR){
		
		for (int i = 0; i < dbR.length; i++) {
			dbR[i] = Math.log10(dbR[i]);
		}
	}
	
		
	public void printBuffer(double[] buff) {
		for (int i = 0; i < buff.length; i++) {
			System.out.println(buff[i]);
		}
	}
	
	
    public void FFT(double[] dbR, double[] dbI) {
        
        int lenght = dbR.length;
        double sortR[] = new double[lenght];
        double sortI[] = new double[lenght];

        for (int i = 0; i < lenght; i++) {
                int j = findIndex(i, lenght);
                sortR[i] = dbR[j];
                sortI[i] = dbI[j];
        }

        int blockNr, bfNr, m;
        double firstR, firstI, secondR, secondI;

        for (int N = 2; N <= lenght; N = 2 * N) {
                blockNr = lenght / N;
                bfNr = N / 2;
                for (int i = 0; i < blockNr; i++) {
                        for (int j = 0; j < bfNr; j++) {
                                m = i * N + j;

                                // (R1 + R2*cos(2pim/N) + I2*sin(2pim/N)) + (I1 - R2*sin(2pim/N) + I2*cos(2pim/N))j
                                firstR = sortR[m] + sortR[m + (N / 2)] * Math.cos(2 * Math.PI * m / N)
                                                + sortI[m + (N / 2)] * Math.sin(2 * Math.PI * m / N);
                                firstI = sortI[m] - sortR[m + (N / 2)] * Math.sin(2 * Math.PI * m / N)
                                                + sortI[m + (N / 2)] * Math.cos(2 * Math.PI * m / N);

                                // (R1 - R2*cos(2pim/N) - I2*sin(2pim/N)) + (I1 + R2*sin(2pim/N) - I2*cos(2pim/N))j
                                secondR = sortR[m] - sortR[m + (N / 2)] * Math.cos(2 * Math.PI * m / N)
                                                - sortI[m + (N / 2)] * Math.sin(2 * Math.PI * m / N);
                                secondI = sortI[m] + sortR[m + (N / 2)] * Math.sin(2 * Math.PI * m / N)
                                                - sortI[m + (N / 2)] * Math.cos(2 * Math.PI * m / N);

                                sortR[m] = firstR;
                                sortI[m] = firstI;
                                sortR[m + (N / 2)] = secondR;
                                sortI[m + (N / 2)] = secondI;
                        }
                }
        }
        for (int i = 0; i < lenght; i++) {
                dbR[i] = (sortR[i] / lenght);
                dbI[i] = (sortI[i] / lenght);
        }
}
    
    
	private int findIndex(int index, int n) {
		int newIndex = 0;
		int bitsNr = (int) (Math.log(n) / Math.log(2));

		int bit;
		for (int i = 0; i < bitsNr; i++) {
			bit = index % 2;
			index = index >> 1;
			newIndex = newIndex << 1;
			newIndex += bit;
		}
		return newIndex;
	}
	
	
	////////////////////////////////////////////////////
	//												  //
	//						.wav				  	  //
	//												  //
	////////////////////////////////////////////////////
	
	void loadFile(File audioFile) {
		try
		{
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(audioFile);

			// Display information about the wav file
			//wavFile.display();
			frames = (int) wavFile.getNumFrames();
			//frames = 44100;
			System.out.println("Frames" + frames);
			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();

			// Create a buffer of 100 frames
			buffer = new double[frames * numChannels];
			//System.out.println("Buffer size: " + buffer.length);
			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			
			do{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, frames);
			}
			while (framesRead != 0);
			// Close the wavFile
			wavFile.close();

		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
	
	void saveFileSequence(int[] values) {	
		
		try{
	         int sampleRate = 44100;    // Samples per second
	         double duration = frames/sampleRate;    	         
	         int numFrames = (int)(duration * sampleRate);
	         double[] buffer2 = new double[frames];
	         WavFile wavFile = WavFile.newWavFile(new File("wyjsciowySeq1.wav"), 1, numFrames, 16, sampleRate);
	         for(int j = 0; j < values.length; j++) {
		         for(int i = 0; i < frames/iloscRamek; i++) {
		        	 int a = i + j*frames/iloscRamek;
		        	 buffer2[a] = Math.sin(2.0 * Math.PI * values[j] * a / frames);	
		         }
	         }

	         wavFile.writeFrames(buffer2, numFrames);
	         wavFile.close();
	      }
	      catch (Exception e){
	         System.err.println(e);
	      }
		
		ftt.displayTones();
	}   
	
void saveFileSequence2(int[] values) {	
		
		try{
	         int sampleRate = 44100;    // Samples per second
	         double duration = frames/sampleRate;    	         
	         int numFrames = (int)(duration * sampleRate);
	         double[] buffer2 = new double[frames];
	         WavFile wavFile = WavFile.newWavFile(new File("wyjsciowySeq2.wav"), 1, numFrames, 16, sampleRate);
	         for(int j = 0; j < values.length; j++) {
		         for(int i = 0; i < frames/iloscRamek; i++) {
		        	 int a = i + j*frames/iloscRamek;
		        	 buffer2[a] = Math.sin(2.0 * Math.PI * values[j] * a / frames);	
		         }
	         }

	         wavFile.writeFrames(buffer2, numFrames);
	         wavFile.close();
	      }
	      catch (Exception e){
	         System.err.println(e);
	      }
		
		ftt.displayTones();
	}   

	void saveFile(int[] values) {	
		
		int frequency, sum=0;
	
		for(int i=0; i<values.length; i++) {			
			sum = sum + values[i];
			//System.out.println(values[i] + " " + sum);
		}
		frequency = sum/values.length;
		//System.out.println(frequency +" = " + sum + " / " + values.length);
		
		try{
	         int sampleRate = 44100;    // Samples per second
	         double duration = frames/sampleRate;    	         
	         int numFrames = (int)(duration * sampleRate);
	         double[] buffer2 = new double[frames];
	         WavFile wavFile = WavFile.newWavFile(new File("wyjsciowy.wav"), 1, numFrames, 16, sampleRate);
		     for(int i = 0; i < frames; i++) {
		        	 buffer2[i] = Math.sin(2.0 * Math.PI * frequency * i / frames);	
	         }

	         wavFile.writeFrames(buffer2, numFrames);
	         wavFile.close();
	      }
	      catch (Exception e){
	         System.err.println(e);
	      }
		ftt.displayTones();
	} 
	


	////////////////////////////////////////////////////
	//												  //
	//						UI						  //
	//												  //
	////////////////////////////////////////////////////
	
	
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
		if (source == autokorelacjaButton) {
			ftt = new FrequencyToTone();
			ramka = Integer.parseInt(autokorelacjaField.getText());				
			iloscRamek = frames/ramka;
			tmp = new double[ramka];
			tmp2 = new double[ramka];
			dbR = new double[ramka];
			dbI = new double[ramka];
			values = new int[iloscRamek];
			
		    for(int j=0; j<iloscRamek; j++) {	    	
		    	System.out.print(j+" ");
		    	values[j] = autocorelation(j*ramka, ramka);		    	
	    	}
		    
			wynikField.setText(Integer.toString(iloscRamek));
			
		} 
		
		if (source == cepstrumButton) {
			ftt = new FrequencyToTone();
			ramka = Integer.parseInt(cepstrumField.getText());
			int cut = Integer.parseInt(cutCepstrumLabel.getText());
			
			iloscRamek = frames/ramka;
			tmp = new double[ramka];
			tmp2 = new double[ramka];
			dbR = new double[ramka];
			dbI = new double[ramka];
			values = new int[iloscRamek];
		    
			for(int j=0; j<iloscRamek; j++) {	
				//System.out.print(j+" ");
				values[j] = cepstrum(j*ramka, ramka, cut);	   
	    	}
			wynikField.setText(Integer.toString(iloscRamek));
		} 
		
		if (source == wykresCepstrumButton) {

			int i = Integer.parseInt(wykresField.getText());
			int cut = Integer.parseInt(cutWykresCepstrumLabel.getText());
			cepstrumWykres(ramka * i, ramka, cut);
		}

		if (source == wykresAutokorelacjaButton) {
			
			int i = Integer.parseInt(wykresField.getText());
			autocorelationWykres(ramka * i, ramka);
			
		}
		
		if (source == saveSingleButton) {
			
			saveFile(values);
			ftt.convertToValuesTab();

			
		} 
		if (source == saveSeqButton) {
			
			//for(int i=0; i<values.length; i++){
			//	System.out.println(values[i]);
			//}
			//saveFileSequence(values);
			ftt.convertToValuesTab();
			saveFileSequence(ftt.values);
			/*for(int i=0; i<ftt.values.length; i++){
				System.out.println(ftt.values[i] + " | " + values[i]);
			}
			System.out.println(values.length + " | " + ftt.values.length);*/
		} 

       
    } 
}
