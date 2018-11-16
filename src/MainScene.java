
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import GraphicsObjects.Arcball;
import GraphicsObjects.Utils;
import objects3D.TexCube;
import objects3D.TexSphere;
import objects3D.Grid;
import objects3D.Human;

public class MainScene {

	private boolean BadAnimation = true;
	private boolean Earth = false;
	/** position of pointer */
	float x = 400, y = 300;
	/** angle of rotation */
	float rotation = 0;
	/** time at last frame */
	long lastFrame;
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;

	long myDelta = 0; // to use for animation
	float Alpha = 0; // to use for animation
	long StartTime; // beginAnimiation

	Arcball MyArcball = new Arcball();

	boolean DRAWGRID = false;

	float pullX = 0.0f; // arc ball X cord.
	float pullY = 0.0f; // arc ball Y cord.

	int OrthoNumber = 1200; // using this for screen size, making a window of 1200 x 800 so aspect ratio 3:2
							// // do not change this for assignment 3 but you can change everything for your
							// project

	// basic colours
	static float black[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	static float white[] = { 1.0f, 1.0f, 1.0f, 1.0f };

	static float grey[] = { 0.5f, 0.5f, 0.5f, 1.0f };
	static float spot[] = { 0.1f, 0.1f, 0.1f, 0.5f };

	// primary colours
	static float red[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	static float green[] = { 0.0f, 1.0f, 0.0f, 1.0f };
	static float blue[] = { 0.0f, 0.0f, 1.0f, 1.0f };

	// secondary colours
	static float yellow[] = { 1.0f, 1.0f, 0.0f, 1.0f };
	static float magenta[] = { 1.0f, 0.0f, 1.0f, 1.0f };
	static float cyan[] = { 0.0f, 1.0f, 1.0f, 1.0f };

	// other colours
	static float orange[] = { 1.0f, 0.5f, 0.0f, 1.0f, 1.0f };
	static float brown[] = { 0.5f, 0.25f, 0.0f, 1.0f, 1.0f };
	static float dkgreen[] = { 0.0f, 0.5f, 0.0f, 1.0f, 1.0f };
	static float pink[] = { 1.0f, 0.6f, 0.6f, 1.0f, 1.0f };

	// static GLfloat light_position[] = {0.0, 100.0, 100.0, 0.0};

	// support method to aid in converting a java float array into a Floatbuffer
	// which is faster for the opengl layer to process

	public void start() {

		StartTime = getTime();
		try {
			Display.setDisplayMode(new DisplayMode(1200, 800));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL(); // init OpenGL
		getDelta(); // call once before loop to initialize lastFrame
		lastFPS = getTime(); // call before loop to initialize fps timer
		
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			update(delta);
			renderGL();
			Display.update();
			Display.sync(120); // cap fps to 120fps
		}

		Display.destroy();
	}

	public void update(int delta) {
		// rotate quad
		// rotation += 0.01f * delta;

		// keep quad on the screen
		if (x < 0)
			x = 0;
		if (x > 1200)
			x = 1200;
		if (y < 0)
			y = 0;
		if (y > 800)
			y = 800;

		updateFPS(); // update FPS Counter

	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @return milliseconds passed since last frame
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("Main Scene FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		changeOrth();
		MyArcball.startBall(0, 0, 1200, 800);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
		lightPos.put(10000f).put(1000f).put(1000).put(0).flip();

		FloatBuffer lightPos2 = BufferUtils.createFloatBuffer(4);
		lightPos2.put(0f).put(1000f).put(0).put(-1000f).flip();

		FloatBuffer lightPos3 = BufferUtils.createFloatBuffer(4);
		lightPos3.put(-10000f).put(1000f).put(1000).put(0).flip();

		FloatBuffer lightPos4 = BufferUtils.createFloatBuffer(4);
		lightPos4.put(1000f).put(1000f).put(1000f).put(0).flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPos); // specify the
																	// position
																	// of the
																	// light
		// GL11.glEnable(GL11.GL_LIGHT0); // switch light #0 on // I've setup specific
		// materials so in real light it will look a bit strange

		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPos); // specify the
																	// position
																	// of the
																	// light
		GL11.glEnable(GL11.GL_LIGHT1); // switch light #0 on
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, Utils.ConvertForGL(spot));

		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_POSITION, lightPos3); // specify
																	// the
																	// position
																	// of the
																	// light
		GL11.glEnable(GL11.GL_LIGHT2); // switch light #0 on
		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_DIFFUSE, Utils.ConvertForGL(grey));

		/*
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_POSITION, lightPos4); // specify
																	// the
																	// position
																	// of the
																	// light
		GL11.glEnable(GL11.GL_LIGHT3); // switch light #0 on
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_DIFFUSE, Utils.ConvertForGL(grey));

		GL11.glEnable(GL11.GL_LIGHTING); // switch lighting on
		 */
		GL11.glEnable(GL11.GL_DEPTH_TEST); // make sure depth buffer is switched
											// on
		GL11.glEnable(GL11.GL_NORMALIZE); // normalize normal vectors for safety
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_BLEND);
	
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		} // load in texture

	}

	public void changeOrth() {

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(1200 - OrthoNumber, OrthoNumber, (800 - (OrthoNumber * 0.66f)), (OrthoNumber * 0.66f), 100000,
				-100000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		FloatBuffer CurrentMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, CurrentMatrix);


		MyArcball.getMatrix(CurrentMatrix);

		GL11.glLoadMatrix(CurrentMatrix);

	}
	
	public void test(){
		// TODO test the code3
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor3f(red[0], red[1], red[2]);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(500, 0, 0);
		GL11.glColor3f(blue[0], blue[1], blue[2]);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 500, 0);
		GL11.glColor3f(green[0], green[1], green[2]);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 0, 500);
		GL11.glEnd();
	}
	/*
	 * You can edit this method to add in your own objects / remember to load in
	 * textures in the INIT method as they take time to load
	 * 
	 */
	public void renderGL() {
		changeOrth();

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glColor3f(0.5f, 0.5f, 1.0f);

		myDelta = getTime() - StartTime;
		float delta = ((float) myDelta) / 10000;

		// code to aid in animation
		float theta = (float) (delta * 2 * Math.PI);
		float thetaDeg = delta * 360;
		float posn_x = (float) Math.cos(theta); // same as your circle code in your notes
		float posn_y = (float) Math.sin(theta);

		/*
		 * This code draws a grid to help you view the human models movement You may
		 * change this code to move the grid around and change its starting angle as you
		 * please
		 */
		if (DRAWGRID) {
			GL11.glPushMatrix();
			Grid MyGrid = new Grid();
			GL11.glTranslatef(600, 400, 0);
			GL11.glScalef(200f, 200f, 200f);
			MyGrid.DrawGrid();
			GL11.glPopMatrix();
		}

		GL11.glPushMatrix();
		Human MyHuman = new Human();
		//I add texture for human here
		MyHuman.head_texture = head_texture;
		MyHuman.upperbody_texture = upperbody_texture;
		MyHuman.lowerbody_texture = lowerbody_texture;
		MyHuman.joint_texture = joint_texture;
		GL11.glTranslatef(300, 400, 0);
		GL11.glScalef(90f, 90f, 90f);
		if (!BadAnimation) {
			// insert your animation code to correct the position for the human rotating
			GL11.glTranslatef(-posn_x * 3f, 0.0f, -posn_y * 3f);
			GL11.glRotatef(-thetaDeg, 0.0f, 1.0f, 0.0f);
			
			
		} else {

			// bad animation version
			GL11.glTranslatef(posn_x * 3.0f, 0.0f, posn_y * 3.0f);
		}
		MyHuman.DrawHuman(delta*6, !BadAnimation); // give a delta for the Human object ot be animated
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		//here is my sign
		TexCube sign = new TexCube();
		
		GL11.glTranslatef(300f, 400f, 0f);
		//I write this function to test
		///test();
		GL11.glScalef(30f, 30f, 30f);
		// TODO sign texture
		sign.DrawTexCube(sign_teture);
		GL11.glPopMatrix();
		/*
		 * This code puts the earth code in which is larger than the human so it appears
		 * to change the scene
		 */
		if (Earth) {
			// Globe in the centre of the scene
			GL11.glPushMatrix();
			TexSphere MyGlobe = new TexSphere();
			GL11.glTranslatef(500, 500, 500);
			GL11.glScalef(140f, 140f, 140f);
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			MyGlobe.DrawTexSphere(8f, 100, 100, texture);
			GL11.glPopMatrix();
		}

	}

	public static void main(String[] argv) {
		MainScene mainScene = new MainScene();
		mainScene.start();
	}
	//here is all of my texture
	Texture texture;
	Texture sign_teture;
	Texture head_texture;
	Texture upperbody_texture;
	Texture lowerbody_texture;
	Texture joint_texture;
	/*
	 * Any additional textures for your assignment should be written in here. Make a
	 * new texture variable for each one so they can be loaded in at the beginning
	 */
	public void init() throws IOException {
		// load all textures here
		texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/earthspace.png"));
		sign_teture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/sign.png"));
		head_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/head.jpg"));
		upperbody_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/upperbody.jpg"));
		lowerbody_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/lowerbody.jpg"));
		joint_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/joint.jpg"));
		System.out.println("Texture loaded okay!!!");
	}
}
