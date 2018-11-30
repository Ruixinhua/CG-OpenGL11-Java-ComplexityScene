
import java.io.IOException;
import java.nio.FloatBuffer;

import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;
import com.sun.tools.corba.se.idl.constExpr.Or;
import objects3D.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.lwjgl.opengl.GL41;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import GraphicsObjects.Arcball;
import GraphicsObjects.Utils;

public class MainWindow {

	private boolean MouseOnepressed = true;
	private boolean dragMode = false;
	private boolean isLaunched = false;
	private boolean beginPress = false;
	private boolean isTouched = false;
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
	boolean waitForKeyrelease = true;
	/** Mouse movement */
	int LastMouseX = -1;
	int LastMouseY = -1;

	float pullX = 0.0f; // arc ball X cord.
	float pullY = 0.0f; // arc ball Y cord.

	//int OrthoNumber = 3000; // using this for screen size, making a window of 1200 x 800 so aspect ratio 3:2
	int OrthoNumber = 1500;						// // do not change this for assignment 3 but you can change everything for your
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
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer

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

		int MouseX = Mouse.getX();
		int MouseY = Mouse.getY();
		int WheelPosition = Mouse.getDWheel();

		boolean MouseButtonPressed = Mouse.isButtonDown(0);

		if (MouseButtonPressed && !MouseOnepressed) {
			MouseOnepressed = true;
			// Debug print
			// System.out.println("Mouse drag mode");
			MyArcball.startBall(MouseX, MouseY, 1200, 800);
			dragMode = true;

		} else if (!MouseButtonPressed) {
			// Debug print
			// System.out.println("Mouse drag mode end ");
			MouseOnepressed = false;
			dragMode = false;
		}

		if (dragMode) {
		    // TODO close the arc ball
			//MyArcball.updateBall(MouseX, MouseY, 1200, 800);
		}

		if (WheelPosition > 0) {
			OrthoNumber += 10;
            System.out.println(OrthoNumber);
		}

		if (WheelPosition < 0) {
			OrthoNumber -= 10;
			if (OrthoNumber < 610) {
				OrthoNumber = 610;
			}


		}

		/** rest key is R */
		if (Keyboard.isKeyDown(Keyboard.KEY_R))
			MyArcball.reset();

		/* bad animation can be turn on or off using A key) */

		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			isLaunched = !isLaunched;
			// TODO modify time
			StartTime = getTime();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_B)){
			beginPress = !beginPress;
			// TODO modify time
			StartTime = getTime();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			x += 0.35f * delta;

		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			y += 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			y -= 0.35f * delta;

		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
			rotation += 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			Earth = !Earth;
		}

		if (waitForKeyrelease) // check done to see if key is released
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_G)) {

				DRAWGRID = !DRAWGRID;
				Keyboard.next();
				if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
					waitForKeyrelease = true;
				} else {
					waitForKeyrelease = false;

				}
			}
		}

		/** to check if key is released */
		if (!Keyboard.isKeyDown(Keyboard.KEY_G)) {
			waitForKeyrelease = true;
		} else {
			waitForKeyrelease = false;

		}

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

		LastMouseX = MouseX;
		LastMouseY = MouseY;
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
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void initGL() {
		changeOrtho();
		MyArcball.startBall(0, 0, 1200, 800);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
//		FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
//		lightPos.put(0f).put(1000f).put(1000).put(0).flip();
//
//		FloatBuffer lightPos2 = BufferUtils.createFloatBuffer(4);
//		lightPos2.put(0f).put(1000f).put(0).put(-1000f).flip();
//
//		FloatBuffer lightPos3 = BufferUtils.createFloatBuffer(4);
//		lightPos3.put(-10000f).put(1000f).put(1000).put(0).flip();
//
//		FloatBuffer lightPos4 = BufferUtils.createFloatBuffer(4);
//		lightPos4.put(1000f).put(1000f).put(1000f).put(0).flip();
		FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
		lightPos.put(0f).put(1000f).put(1000).put(0).flip();

		FloatBuffer lightPos2 = BufferUtils.createFloatBuffer(4);
		lightPos2.put(0f).put(1000f).put(0).put(-1000f).flip();

		FloatBuffer lightPos3 = BufferUtils.createFloatBuffer(4);
		lightPos3.put(-10000f).put(1000f).put(1000).put(0).flip();

		FloatBuffer lightPos4 = BufferUtils.createFloatBuffer(4);
		lightPos4.put(1000f).put(1000f).put(1000f).put(0).flip();
		// TODO:test lighting
//		FloatBuffer lightPos5 = BufferUtils.createFloatBuffer(4);
//		lightPos.put(0f).put(0f).put(0f).put(1000f).flip();
//		GL11.glEnable(GL11.GL_LIGHT4);
//		GL11.glLight(GL11.GL_LIGHT4, GL11.GL_POSITION,lightPos5);

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightPos); // specify the
																	// position
																	// of the
																	// light
		//GL11.glEnable(GL11.GL_LIGHT0); // switch light #0 on // I've setup specific
		// materials so in real light it will look abit strange

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
//
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_POSITION, lightPos4); // specify
//																	// the
//																	// position
//																	// of the
//																	// light
		GL11.glEnable(GL11.GL_LIGHT3); // switch light #0 on
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_DIFFUSE, Utils.ConvertForGL(grey));
		GL11.glEnable(GL11.GL_LIGHTING); // switch lighting on
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
	public void changeOrthoTemp() {

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(1200 - OrthoNumber - 100, OrthoNumber, (800 - (OrthoNumber * 0.66f)), (OrthoNumber * 0.66f) + 200, 100000,
				-100000);
		loadMatrix();
	}
	public void changeOrtho(float speed) {
		System.out.println("launch"+speed);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(1200 - OrthoNumber - 1000, OrthoNumber, (800 - (OrthoNumber * 0.66f)) + 900 + speed, (OrthoNumber * 0.66f)+ 1100 + speed, 100000,
				-100000);
		loadMatrix();
	}
	public void changeOrthoToRocket(float speed){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		System.out.println(speed);
		float expendSpeed = speed * 300;
		float scaleSpeed = speed * 10;
		if(expendSpeed > 900) expendSpeed = 900;
		OrthoNumber += scaleSpeed;
		if(OrthoNumber > 3000) OrthoNumber = 3000;
		GL11.glOrtho(1200 - OrthoNumber-expendSpeed - 100, OrthoNumber, (800 - (OrthoNumber * 0.66f))+ expendSpeed , (OrthoNumber * 0.66f)+200+expendSpeed, 100000,
				-100000);
		loadMatrix();
	}

	public void loadMatrix(){
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		FloatBuffer CurrentMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, CurrentMatrix);
		MyArcball.getMatrix(CurrentMatrix);

		GL11.glLoadMatrix(CurrentMatrix);
	}
	public void changeOrtho(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		loadMatrix();
	}

	public void test(){
		// TODO test the code
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor3f(red[0], red[1], red[2]);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(500, 0, 0);
		GL11.glColor3f(cyan[0], cyan[1], cyan[2]);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 500, 0);
		GL11.glColor3f(green[0], green[1], green[2]);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 0, 500);
		GL11.glEnd();
	}

    public void drawQuadsWithTexture(Texture texture, Point4f[] points){
		Color.white.bind();
		texture.bind();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBegin(GL11.GL_QUADS);

		Vector4f v = points[1].MinusPoint(points[0]);
		Vector4f w = points[3].MinusPoint(points[0]);
		Vector4f normal = v.cross(w).Normal();
		GL11.glNormal3f(normal.x, normal.y, normal.z);
		GL11.glTexCoord2f(0f,0f);
		GL11.glVertex3f(points[0].x,points[0].y,points[0].z);
		GL11.glTexCoord2f(1f,0f);
		GL11.glVertex3f(points[1].x,points[1].y,points[1].z);
		GL11.glTexCoord2f(1f,1f);
		GL11.glVertex3f(points[2].x,points[2].y,points[2].z);
		GL11.glTexCoord2f(0f,1f);
		GL11.glVertex3f(points[3].x,points[3].y,points[3].z);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	/*
	 * You can edit this method to add in your own objects / remember to load in
	 * textures in the INIT method as they take time to load
	 *
	 */
	public void renderGL() {
		myDelta = getTime() - StartTime;
		// delta increase by 0.1/second
		float delta = ((float) myDelta) / 10000;
		startLauncher(delta);
	}

	private void startLauncher(float delta) {

		float beginTime = 0.5f;

		// TODO:change ortho
		if(delta > beginTime){
			beginPress = true;
			delta = delta - beginTime;
		}

		if(delta > 1.85){
            float temp = 1.85f - 0.3477f;
            changeOrtho(temp * temp * 1000);

            GL11.glRotatef(-5, 1, 0, 0);

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glColor3f(0.5f, 0.5f, 1.0f);


            // TODO rotate human
            drawRocket(temp * 4);
            drawBackground();
            drawController();
            drawGround();
		    return;
        }

		if(!isLaunched && !beginPress){
			changeOrthoTemp();
		}else if (!isLaunched && beginPress){
			changeOrthoToRocket(delta*10);
		}else{
			float temp = delta - 0.3477f;
			System.out.println("now:"+temp);
			//delta = (delta * 10 - 4.5f) / 10;
			changeOrtho(temp * temp * 1000);
		}
		//changeOrtho(delta*delta * 1000);

		// change perspective
		//if(isLaunched) OrthoNumber += delta*2;
		// Rotate the perspective
		//GL11.glRotatef(-5,1,0,0);
		GL11.glRotatef(-5,1,0,0);

		//GL11.glRotatef(180,0,0,1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glColor3f(0.5f, 0.5f, 1.0f);
		drawEarth();

		// TODO  look at
//		GLU.gluPerspective(100,1.5f,1f,1000f);
//		GLU.gluLookAt(0f,1000f,-1200f,0f,-1f,1f,0f,1f,1f);
		// code to add in animation
//        test();
		// TODO rotate human
		float temp = delta - 0.3477f;
		drawHuman(delta);
		drawRocket(temp*4);
		drawBackground();
		drawController();
		drawGround();
	}

	private void drawController() {
		GL11.glPushMatrix();
		//here is my sign
		Cube controller = new Cube();
		Cylinder button = new Cylinder();
		float size = 25;
		GL11.glColor3f(cyan[0], cyan[1], cyan[2]);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(cyan));
		GL11.glTranslatef(1000f,size / 2f, 0f);
		//I write this function to test
		///test();
		GL11.glScalef(size, size, size);
		// TODO sign texture

		controller.DrawCube();
		GL11.glTranslatef(0,1f,0);
		controller.DrawCube();
		GL11.glTranslatef(0,1f,0);
		controller.DrawCube();
		GL11.glTranslatef(0,1f,0);
		GL11.glColor3f(red[0], red[1], red[2]);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(red));
		GL11.glRotatef(-90,1,0,0);
		if(isTouched || isLaunched){
			GL11.glTranslatef(0,0f,-0.2f);
		}
		button.DrawCylinder(0.5f,0.5f,32);
		GL11.glPopMatrix();
	}

	public void drawEarth() {
		/*
		 * This code puts the earth code in which is larger than the human so it appears
		 * to change the scene
		 */
		if (Earth) {
			// Globe in the centre of the scene
			GL11.glPushMatrix();
			TexSphere MyGlobe = new TexSphere();
			GL11.glTranslatef(0, 0, 0);
			GL11.glScalef(500f, 500f, 500f);
			//GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			MyGlobe.DrawTexSphere(8f, 100, 100, texture);
			GL11.glPopMatrix();
		}
	}

	public void drawBackground() {
		GL11.glPushMatrix();
		float size = 5000;
		GL11.glScalef(size,size,size);
		GL11.glTranslatef(0,0,1);
		Point4f background_points[] = {new Point4f(-1,0,0,0.5f), new Point4f(1,0,0,0.5f),
				new Point4f(1,1,0,0.5f), new Point4f(-1, 1, 0 ,0.5f)};

		// TODO:draw background
		drawQuadsWithTexture(background_sky,background_points);
		GL11.glPopMatrix();
	}

	public void drawGround(){
		GL11.glPushMatrix();
		float size = 5000;
		GL11.glScalef(size,size,size);
		// move back
		Point4f ground_points[] = {new Point4f(-1,0,-1,1),new Point4f(1,0,-1,1),
				new Point4f(1,0,1,1),new Point4f(-1,0,1,1)};
		drawQuadsWithTexture(ground_texture,ground_points);
		GL11.glPopMatrix();
	}

	public void drawRocket(float speed) {
		GL11.glPushMatrix();
		GL11.glScalef(200,200,200);
		Rocket rocket = new Rocket();
		// TODO:draw rocket
		rocket.DrawRocket(speed, isLaunched);
		GL11.glPopMatrix();
	}

	public void drawHuman(float speed) {
		System.out.println("human"+speed);
		GL11.glPushMatrix();
		Human MyHuman = new Human();
		float sizeHuman = 40f;
		//I add texture for human here
		MyHuman.head_texture = head_texture;
		MyHuman.upperbody_texture = upperbody_texture;
		MyHuman.lowerbody_texture = lowerbody_texture;
		MyHuman.joint_texture = joint_texture;
		float theta = (float) (speed * 2 * Math.PI);
		float thetaDeg = speed * 360;
		float posn_x = (float) Math.cos(theta); // same as your circle code in your notes
		float posn_y = (float) Math.sin(theta);
		// TODO: the position of human
		GL11.glTranslatef(1053, sizeHuman*1.5f, -10);
		GL11.glScalef(sizeHuman, sizeHuman, sizeHuman);

		if (isLaunched) {
			// TODO  look at animation
			// insert your animation code to correct the position for the human rotating
			//GL11.glTranslatef(speed, 0.0f, -10f);
			//System.out.println(delta);
			//GL11.glTranslatef(posn_x * 16 -25, 0, posn_y * 16);
			///GL11.glRotatef(-thetaDeg , 0.0f, 1.0f, 0.0f);
		} else {
//			GL11.glTranslatef(posn_x * 16 -25, speed * 10, posn_y * 16);
//			GL11.glRotatef(-thetaDeg , 0.0f, 1.0f, 0.0f);
			// bad animation version
			//GL11.glTranslatef(posn_x * 3.0f, 0.0f, posn_y * 3.0f);
		}
		// TODO rotate human to left
		GL11.glRotatef(90,0,1,0);

		MyHuman.beginPress = beginPress;
		isTouched = MyHuman.isTouched;

		MyHuman.DrawHuman(speed * 8, isLaunched); // give a delta for the Human object ot be animated
		isLaunched = MyHuman.isPressed;
		GL11.glPopMatrix();
	}

	public static void main(String[] argv) {
		MainWindow hello = new MainWindow();
		hello.start();
	}
	//here is all of my texture
	Texture texture;
	Texture sign_texture;
	Texture head_texture;
	Texture upperbody_texture;
	Texture lowerbody_texture;
	Texture joint_texture;
	Texture background_sky;
	Texture ground_texture;
	/*
	 * Any additional textures for your assignment should be written in here. Make a
	 * new texture variable for each one so they can be loaded in at the beginning
	 */
	public void init() throws IOException {
		// load all textures here
		// TODO:add texture here
		texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/earthspace.png"));
		sign_texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/sign.png"));
		head_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/head.jpg"));
		upperbody_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/upperbody.jpg"));
		lowerbody_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/lowerbody.jpg"));
		joint_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/joint.jpg"));
		background_sky = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/background_sky.jpg"));
		ground_texture = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/ground.jpg"));
		System.out.println("Texture loaded okay ");
	}

}
