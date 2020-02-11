package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gameNetworks.GameClient;
import messages.MoveMessage;
import messages.NewMessage;

public class Player {

	public int id;

	public static final int X_SPEED = 5;
	public static final int Y_SPEED = 5;

	private int x;
	private int y;

	private GameClient gameClient;

	private boolean bL, bU, bR, bD;
	private Dir dir = Dir.STOP;

	public enum Dir {
		L, LU, U, RU, R, RD, D, LD, STOP
	}

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Player(int x, int y, GameClient gameClient, Dir dir) {
		this(x, y);
		this.gameClient = gameClient;
		this.dir = dir;
	}

	public void draw(Graphics g) {

		g.fillOval(x, y, 50, 50);
		move();

	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A:
			bL = true;
			break;
		case KeyEvent.VK_W:
			bU = true;
			break;
		case KeyEvent.VK_D:
			bR = true;
			break;
		case KeyEvent.VK_S:
			bD = true;
			break;
		}
		locateDirection();
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A:
			bL = false;
			break;
		case KeyEvent.VK_W:
			bU = false;
			break;
		case KeyEvent.VK_D:
			bR = false;
			break;
		case KeyEvent.VK_S:
			bD = false;
			break;
		}
		locateDirection();
	}

	private void move() {
		switch (dir) {
		case L:
			x -= X_SPEED;
			break;
		case LU:
			x -= X_SPEED;
			y -= Y_SPEED;
			break;
		case U:
			y -= Y_SPEED;
			break;
		case RU:
			x += X_SPEED;
			y -= Y_SPEED;
			break;
		case R:
			x += X_SPEED;
			break;
		case RD:
			x += X_SPEED;
			y += Y_SPEED;
			break;
		case D:
			y += Y_SPEED;
			break;
		case LD:
			x -= X_SPEED;
			y += Y_SPEED;
			break;
		case STOP:
			break;
		}
		if (x < 0)
			x = 0;
		if (y < 30)
			y = 30;
		if (x + 30 > GameClient.FRAME_WIDTH)
			x = GameClient.FRAME_WIDTH - 30;
		if (y + 30 > gameClient.FRAME_HEIGHT)
			y = GameClient.FRAME_HEIGHT - 30;
	}

	private void locateDirection() {
		// TODO Auto-generated method stub
		Dir oldDir = this.dir;
		if (bL && !bU && !bR && !bD)
			dir = Dir.L;
		else if (bL && bU && !bR && !bD)
			dir = Dir.LU;
		else if (!bL && bU && !bR && !bD)
			dir = Dir.U;
		else if (!bL && bU && bR && !bD)
			dir = Dir.RU;
		else if (!bL && !bU && bR && !bD)
			dir = Dir.R;
		else if (!bL && !bU && bR && bD)
			dir = Dir.RD;
		else if (!bL && !bU && !bR && bD)
			dir = Dir.D;
		else if (bL && !bU && !bR && bD)
			dir = Dir.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Dir.STOP;

		if (dir != oldDir) {

			/**
			 * If the new direction is different from old direction, client will send a
			 * message to server, then server will broadcast this messsage to all clients.
			 **/

			MoveMessage message = new MoveMessage(id, x, y, dir);
			gameClient.getNc().send(message);
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public GameClient getGameClient() {
		return gameClient;
	}

	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	public Dir getDir() {
		return dir;
	}

	public void setDir(Dir dir) {
		this.dir = dir;
	}

}
