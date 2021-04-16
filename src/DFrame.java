import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;

public class DialogFrame extends JFrame implements ActionListener {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DFrame(){
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLayout(new FlowLayout());
			
			JComboBox url_name = new JComboBox();
			
			
					this.add(cb);
					this.pack();
			        this.setVisible(true);
		}
		
		@Override
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==comboBox)
		}
	}

	private static void DFrame() {
		// TODO Auto-generated method stub
		
	}

}
