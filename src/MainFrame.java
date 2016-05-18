import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener{
	
	private JButton loadingButton;
	
	private static final long serialVersionUID = 1L;

	public MainFrame() throws IOException{
		
		JPanel panel = new JPanel();
		JFrame f = new JFrame("POID_3");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setSize(400, 200);
		setLocationRelativeTo(null);
				
		loadingButton = new JButton("Wczytaj plik .wav");
		loadingButton.addActionListener(this);
		loadingButton.setFont(new Font("Calibri",Font.PLAIN,25));
		
	
		f.setLocationRelativeTo(null);	
		panel.add(loadingButton);
		f.add(panel);	
	    pack();

	    f.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		
		if(source == loadingButton)
		{		
				new Audio();			
		}	
		
	}

}
