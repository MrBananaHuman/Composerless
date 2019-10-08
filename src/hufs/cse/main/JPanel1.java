package hufs.cse.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

class JPanel1 extends JFrame implements ActionListener {
	private JLabel wkdfmLabel, qkrwkLabel;
	private JLabel rhythmLabel, songnameLabel;
	private JLabel copyRightLabel;
	private JTextField songNameField, songRhythmField;
	private JPanel textPanel = null;
	private JButton playButton = null;
	private JButton pauseButton = null;
	private JButton createButton = null;
	private JButton loadButton = null;
	private File selectedFile = null;
	private JFileChooser jFileChooser;
	private MidiPlayer midiPlayer = null;
	private JList jlist;
	private DefaultListModel model;
	private JScrollPane jlistScroller;
	private JComboBox qkrwkComboBox, wkdfmComboBox;
	private String qkrwkElement, wkdfmElement;

	// ����
	void qkrwk() {
		
		// ���� label
		qkrwkLabel = new JLabel("Rhythm ");
		
		qkrwkLabel.setLocation(190,10);
		qkrwkLabel.setSize(50,50);

		int size = 3;
		String[] qkrwk = new String[size];

		qkrwk[0] = "2/4";
		qkrwk[1] = "3/4";
		qkrwk[2] = "4/4";

		qkrwkComboBox = new JComboBox();
		qkrwkComboBox.setModel(new DefaultComboBoxModel(qkrwk));
		
		qkrwkComboBox.setLocation(250,20);
		qkrwkComboBox.setSize(60, 25); // x, y ��ǥ ���α���, ���α���
		// qkrwkComboBox.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// // TODO Auto-generated method stub
		// qkrwkElement = qkrwkComboBox.getSelectedItem().toString();
		// System.out.println(qkrwkComboBox.getSelectedItem());
		//20
		// });
	}

	// �帣
	void wkdfm() {
		wkdfmLabel = new JLabel("Genre ");
		wkdfmLabel.setLocation(15,10);
		wkdfmLabel.setSize(50,50);

		int size = 3;
		String[] wkdfm = new String[size];

		wkdfm[0] = "czerny";
		wkdfm[1] = "bluse";
		wkdfm[2] = "jazz";

		wkdfmComboBox = new JComboBox();
		wkdfmComboBox.setModel(new DefaultComboBoxModel(wkdfm));
		
		wkdfmComboBox.setLocation(63,20);
		wkdfmComboBox.setSize(100, 25); // x, y ��ǥ ���α���, ���α���
		// wkdfmComboBox.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// // TODO Auto-generated method stub
		// wkdfmElement = wkdfmComboBox.getSelectedItem().toString();
		// System.out.println(wkdfmComboBox.getSelectedItem());
		// }
		// });
	}

	public JPanel1() {
		super("Gui");

		// open dialog box
		jFileChooser = new JFileChooser("C:\\");

		final JFrame frame1 = new JFrame();

		model = new DefaultListModel();

		jlist = new JList(model);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.setBounds(5, 220, 225, 200);
		// scroller �߰�
		jlistScroller = new JScrollPane(jlist);
		jlistScroller.setBounds(15, 220, 320, 200);
		
		frame1.add(jlistScroller);
		frame1.setSize(370, 500);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setLayout(null);

		// >>>>>> JComboBox <<<<<<<
		qkrwk();
		wkdfm();

		frame1.add(qkrwkLabel);
		frame1.add(wkdfmLabel);
		frame1.add(qkrwkComboBox);
		frame1.add(wkdfmComboBox);

		// >>>>>> JComboBox <<<<<<<
		playButton = new JButton(new ImageIcon("play.png"));
		playButton.setBackground(null);
		playButton.setBorder(null);

		pauseButton = new JButton(new ImageIcon("stop.png"));
		pauseButton.setBackground(null);
		pauseButton.setBorder(null);

		createButton = new JButton(new ImageIcon("save.png"));
		createButton.setBackground(null);
		createButton.setBorder(null);

		loadButton = new JButton(new ImageIcon("load.png"));
		loadButton.setBackground(null);
		loadButton.setBorder(null);

		playButton.addActionListener(this); // �̺�Ʈ�����ʸ� ����մϴ�.
		pauseButton.addActionListener(this);
		createButton.addActionListener(this);
		loadButton.addActionListener(this);
		jlist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int index = jlist.locationToIndex(e.getPoint());
				ListModel md = jlist.getModel();
				Object o = md.getElementAt(index);
				System.out.println("moustClicked" + o);
				midiPlayer.loaded(o.toString());
			}
		});

		// filtering
		FileNameExtensionFilter filter = new FileNameExtensionFilter("midi ����",
				"mid", "midi");

		// ���� ���� ���̾�α׸� ���
		jFileChooser.setFileFilter(filter);

		playButton.setBounds(210, 156, 50, 50);
		pauseButton.setBounds(250, 170, 50, 20);
		createButton.setBounds(105, 157, 50, 50);
		loadButton.setBounds(160, 165, 50, 30);

		// >>>>>>>>>>>> JText, JLabel <<<<<<<<<<<<<
		
		songNameField = new JTextField();
		songRhythmField = new JTextField();

		rhythmLabel = new JLabel("Rhythm num ");
		songnameLabel = new JLabel("Save File Name ");
		
		rhythmLabel.setBounds(15, 60, 100, 50);
		songRhythmField.setBounds(100, 70, 80, 27);
		
		songnameLabel.setBounds(15, 100, 180, 50);
		songNameField.setBounds(180, 115, 150, 27);
		
		copyRightLabel = new JLabel("copyright @ 2014 Hufs.Cse All Rights Reserved");
		copyRightLabel.setBounds(5, 390, 360, 100);
		// >>>>>>>>>>> Add Frame <<<<<<<<<<<<<<< 
		
		frame1.add(rhythmLabel);
		frame1.add(songRhythmField);
		frame1.add(songnameLabel);
		frame1.add(songNameField);
		frame1.add(copyRightLabel);
		
		frame1.add(playButton);
		frame1.add(pauseButton);
		frame1.add(createButton);
		frame1.add(loadButton);
		frame1.setTitle("You are also Composer");
//		frame1.add(jlist);

		midiPlayer = new MidiPlayer(); // �̵� �÷��̾� ��

		frame1.getContentPane().setBackground(Color.white);
		frame1.setVisible(true);
	}

	public String getListElement(String path) {
		return path;
	}

	public void addListElement(String path) {
		model.addElement(path);
	}

	// �̺�Ʈ�� �߻� ��
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource(); // �̺�Ʈ ��ü�κ��� �̺�Ʈ�� �߻��� ��ü�� ��� �´�.
		if (o == playButton) {
			System.out.println(selectedFile.getAbsolutePath());
			midiPlayer.start();
		} else if (o == pauseButton) {
			midiPlayer.pause();
		} else if (o == createButton) {
			String qkrwkName = qkrwkComboBox.getSelectedItem().toString();
			String wkdfmName = wkdfmComboBox.getSelectedItem().toString();
			String songName = songNameField.getText();
			String rhythm = songRhythmField.getText();
			System.out.println(songName);
			System.out.println(rhythm);
			System.out.println(qkrwkName);
			System.out.println(wkdfmName);
			
			int TimeSigTop = 0;
			int TimeSigBottom = 0;
			if(qkrwkName.equals("4/4")){
				TimeSigTop = 4;
				TimeSigBottom = 4;
			}
			songName += ".mid";
//			/(int TimeSigTop, int TimeSigBottom, int RhythmNum, String Genre, String FileName){
			ComposerlessMain main = new ComposerlessMain(TimeSigTop,TimeSigBottom,Integer.parseInt(rhythm),wkdfmName,songName);
			// 1. �� ��

			// 1) ����
			// qkrwkElement

			// 2) �帣
			// wkdfmElement

			// 2. �� ����
			/*
			 * int result = jFileChooser.showSaveDialog(this);
			 * if(jFileChooser.showSaveDialog(this) ==
			 * JFileChooser.APPROVE_OPTION){ File file =
			 * jFileChooser.getSelectedFile(); model.saveGrimPanData(file); //
			 * �׸��� ��� }
			 */
		} else if (o == loadButton) { // load
			// filtering Ȯ���� �߰�
			int result = jFileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				// ������ ������ ��� ��ȯ
				selectedFile = jFileChooser.getSelectedFile();
				model.addElement(selectedFile.getAbsoluteFile());
			}
		}
	}
}