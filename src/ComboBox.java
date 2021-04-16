import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ComboBox extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ComboBox frame = new ComboBox();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ComboBox() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JComboBox url_name = new JComboBox();
		
		url_name.setEditable(true);
		contentPane.add(url_name, BorderLayout.NORTH);
		url_name.setBounds(58,63,305,32);
		
		url_name.addItem("Select URL");
		url_name.addItem("url1");
		url_name.addItem("url2");
		url_name.addItem("url3");
		url_name.setSelectedItem("Select URL");

		
		/*JButton btnGetUrl = new JButton("Get URL");
		//btnGetUrl.addActionListener(new ActionListener() {
		//	public void actionPerformed(ActionEvent arg0) {
				System.out.println(url_name.getSelectedItem().toString());
			}
		});
		
		btnGetUrl.setBounds(143, 140, 127. 23);
		contentPane.add(btnGetUrl);
		*/
	}

}

