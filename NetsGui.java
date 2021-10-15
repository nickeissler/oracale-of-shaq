import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

@SuppressWarnings("serial")
public class NetsGui extends JFrame {
	private ArrayList<Integer> path;
	private JPanel panel;
	private RosterParser parser;


	public NetsGui(ArrayList<Integer> path) {
		super("Degrees of Shaq");
		this.setSize(800, 300);
		this.path = path;
		this.setLayout(new BorderLayout());
		this.panel = new JPanel();
		this.add(this.panel, BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.parser = new RosterParser();
	}

	public JPanel createPanel(int id, TreeMap<Integer, String> map, boolean first, boolean last) {
		JPanel player = new JPanel(new BorderLayout());
		String name = map.get(id);
		String url = parser.getProfilePic(name);
		
		JButton playerButton = this.playerButton(url);
		
		String bio = toParagraph(parser.getPlayerDescripiton(name));
		playerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, bio);
			}
		});
		playerButton.setSize(20, 20);
		player.add(playerButton, BorderLayout.CENTER);
		if (first) {
			JLabel label = new JLabel(name.toUpperCase() + " played with:");
			player.add(label, BorderLayout.NORTH);
			return player;
		}
		if (last) {
			JLabel label = new JLabel(name.toUpperCase());
			player.add(label, BorderLayout.NORTH);
			return player;
		}
		JLabel label = new JLabel(name.toUpperCase() + ", who played with:");
		player.add(label, BorderLayout.NORTH);
		return player;
	}

	public void drawPath(TreeMap<Integer, String> map) {
		for (int i = 0; i < this.path.size(); i++) {
			if (i == 0) {
				JPanel player = createPanel(path.get(i), map, true, false);
				this.panel.add(player);
				this.panel.validate();
				this.validate();
			} else if (i == path.size() - 1) {
				JPanel player = createPanel(path.get(i), map, false, true);
				this.panel.add(player);
				this.panel.validate();
				this.validate();
			} else {
				JPanel player = createPanel(path.get(i), map, false, false);
				this.panel.add(player);
				this.panel.validate();
				this.validate();
			}
		}
		this.repaint();
	}

	public static String toParagraph(String s) {
		int counter = 0;
		s = s.replaceAll("More bio, uniform, draft, salary info", "");
		StringBuilder string = new StringBuilder(s);
		for (int i = 0; i < s.length(); i++) {
			if (counter < 45) {
				++counter;
			} else if (string.charAt(i) == ' ') {
				string.setCharAt(i, '\n');
				counter = 0;
			} else {
				continue;
			}
		}
		return string.toString();
	}
	
	public JButton playerButton(String pictureURL) {
			try {
				URL url = new URL(pictureURL);
				ImageIcon icon = new ImageIcon(url);
				return new JButton(icon);
			} catch (MalformedURLException e) {
				System.out.println("Empty pButton");
		}
			System.out.println("Error: URL Exception");
			return new JButton();
	}
	

	

}

