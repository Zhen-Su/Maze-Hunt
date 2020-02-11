package gameNetworks;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import player.Player;

/**
 * 
 * @author kevin
 *
 */
public class GameClient extends Frame {

	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 600;
	public static final String GAME_NAME = "--GAMENAME--";
	private NetClient nc = new NetClient(this);
	private Player player;
	private List<Player> players = new ArrayList<>();
	private Image offScreenImage = null;
	private ConDialog dialog = new ConDialog();

	public NetClient getNc() {
		return nc;
	}

	public void setNc(NetClient nc) {
		this.nc = nc;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	

	@Override
	public void paint(Graphics g) {
		for (int i = 0; i < players.size(); i++) {
			Player t = players.get(i);
			t.draw(g);
		}
		player.draw(g);
	}

	//this method to fix screen	flicker problem
	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(800, 600);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

//	public void startClient() {
//
//		// In MVP, we need to enter Server IP manually
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Please enter Server IP: ");
//		String serverIP = sc.nextLine();
//		System.out.println("Please enter your UDP Port (Any Number): ");
//		int clientUDPPort = sc.nextInt();
//
//		nc.setClientUDPPort(clientUDPPort);
//		nc.connect(serverIP, GameServer.SERVER_TCP_PORT);
//
//		sc.close();
//	}

	public static void main(String[] args) {
		GameClient gc = new GameClient();
//		gc.startClient();
		gc.launch();
	}

	private void launch() {

		this.setLocation(400, 300);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setTitle(GAME_NAME);

		// This is a windows events, the purpose is to exit normally when we close the
		// window
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.setResizable(false);
		this.setBackground(Color.BLACK);
		this.addKeyListener(new KeyMonitor());
		this.setVisible(true);
		
		new Thread(new PaintThread()).start();

		dialog.setVisible(true);
	}

	class PaintThread implements Runnable {

		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * we can make this inner class as an outer class in gameEvents package
	 * 
	 * @author kevin
	 *
	 */
	class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
		}
	}

    class ConDialog extends Dialog{
        Button b = new Button("connect to server");
        TextField tfIP = new TextField("127.0.0.1", 15);
        TextField tfMyUDPPort = new TextField("5555", 4);

        public ConDialog() {
            super(GameClient.this, true);
            this.setLayout(new FlowLayout());
            this.add(new Label("IP:"));
            this.add(tfIP);
            this.add(new Label("My UDP Port:"));
            this.add(tfMyUDPPort);
            this.add(b);
            this.setLocation(400, 400);
            this.pack();
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    setVisible(false);
                }
            });
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String IP = tfIP.getText().trim();
                    int myUDPPort = Integer.parseInt(tfMyUDPPort.getText().trim());
                    nc.setClientUDPPort(myUDPPort);
                    nc.connect(IP, GameServer.SERVER_TCP_PORT);
                    setVisible(false);
                }
            });
        }
    }
}


