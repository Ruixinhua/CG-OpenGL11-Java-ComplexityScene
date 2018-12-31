
import java.awt.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;
import objects3D.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import GraphicsObjects.Arcball;
import GraphicsObjects.Utils;

public class MainWindow {

	private boolean isLaunched = false;
	/** position of pointer */
	float x = 400, y = 300;
	/** time at last frame */
	long lastFrame;
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;

	long myDelta = 0; // to use for animation
	long StartTime; // beginAnimiation

	Arcball MyArcball = new Arcball();

	/** Mouse movement */

	// basic colours

	// primary colours
	static float red[] = { 1.0f, 0.0f, 0.0f, 1.0f };

    float cameraAngle = 90f;
    float cameraZ = -115f;
    float cameraY = 160;
    float directionZ = 100;
    float humanPositionY = 0;
    float racketRotate = 0;
    Point4f cameraPosition = new Point4f(0, cameraY, cameraZ,1);
    Point4f cameraDirection = new Point4f(0,cameraY,directionZ,1);
    Point4f cameraUp = new Point4f(0, 1, 0,1);
    Point4f humanPosition = new Point4f(0,humanPositionY,cameraZ+225,1);
    float raftForce = 0f;
    float raftSpeed = 0;
    float raftRotate = 180;
    float handRotate  = 0;
    float humanRotation = 180;
    int raftMaxSpeed = 3;
    int sizeHuman = 30;
    int humanSpeed = 5;
    int maxSpeed = 30;
	RobotCat myRobotCat;
	FixedHuman npc;
	NormalHuman daXiong;
	Rocket rockets[];
    List<Rectangle> collisions = new ArrayList<>();

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

        initObject();
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			update(delta);
			renderGL();
			Display.update();
			Display.sync(120); // cap fps to 120fps
		}

		Display.destroy();
	}

    private void initObject() {
	    myRobotCat = new RobotCat();
	    npc = new FixedHuman();
        daXiong = new NormalHuman();
        rockets = new Rocket[]{new RocketA(),new RocketB(),new RocketC()};
        int width = rocketSize * 8;
        int height = rocketSize * 5;
        Rectangle npc1 = new Rectangle(-width / 2,museumSize-2*height/3,width,height);
        Rectangle npc2 = new Rectangle(museumSize-2*height/3,-width/2,height,width);
        Rectangle npc3 = new Rectangle(-museumSize-height/3,-width/2,height,width);
        myRobotCat.head_texture = head_texture;
        myRobotCat.upperbody_texture = upperbody_texture;
        myRobotCat.lowerbody_texture = lowerbody_texture;
//        myRobotCat.joint_texture = joint_texture;
        npc.head_texture = head_texture;
        npc.upperbody_texture = upperbody_texture;
        npc.lowerbody_texture = lowerbody_texture;
//        npc.joint_texture = joint_texture;
        daXiong.head_texture = head_texture;
        daXiong.upperbody_texture = upperbody_texture;
        daXiong.lowerbody_texture = lowerbody_texture;
        rockets[0].bodys = new Texture[]{wall,background_sky,child,background_sky};
        rockets[1].bodys = new Texture[]{joint_texture,background_sky,child,background_sky};
        rockets[2].bodys = new Texture[]{background_sky,joint_texture,joint_texture,joint_texture};
//        daXiong.joint_texture = joint_texture;
//	    collisions.add(myRobotCat);
//	    collisions.add(npc);
//	    collisions.add(daXiong);
        collisions.add(npc1);
        collisions.add(npc2);
        collisions.add(npc3);
    }

    public void drawRectangle(Rectangle r){
	    GL11.glColor3f(red[0],red[1],red[2]);
        GL11.glBegin(GL11.GL_QUADS);
        Point4f points[] = {new Point4f(r.x,0,r.y,0),new Point4f(r.x+r.width,0,r.y,0),
                new Point4f(r.x+r.width,0,r.y+r.height,0), new Point4f(r.x,0,r.y+r.height,0)};
        Vector4f v = points[1].MinusPoint(points[0]);
        Vector4f w = points[3].MinusPoint(points[0]);
        Vector4f normal = v.cross(w).Normal();
        GL11.glNormal3f(normal.x, normal.y, normal.z);

        GL11.glVertex3f(points[0].x,points[0].y,points[0].z);
        GL11.glVertex3f(points[1].x,points[1].y,points[1].z);
        GL11.glVertex3f(points[2].x,points[2].y,points[2].z);
        GL11.glVertex3f(points[3].x,points[3].y,points[3].z);
        GL11.glEnd();
    }

	public void update(int delta) {
        // TODO key listener
        float rotateAngle = 1.0f;
        raftRotate++;
        handRotate++;
        racketRotate++;
        npc.rotateAngle = handRotate;
        daXiong.rotateAngle = handRotate;
        Point currentHumanPosition = new Point(Math.round(humanPosition.x), Math.round(humanPosition.z));
        if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            if(!checkCollision(currentHumanPosition)){
                cameraAngle-=rotateAngle;
                humanRotation+=rotateAngle;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            if(!checkCollision(currentHumanPosition)) {
                cameraAngle += rotateAngle;
                humanRotation -= rotateAngle;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_J)){
            humanSpeed+=1;
            if(humanSpeed > maxSpeed) humanSpeed = maxSpeed;
            System.out.println("humanSpeed "+humanSpeed);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_K)){
            humanSpeed-=1;
            if(humanSpeed < 5) humanSpeed = 5;
            System.out.println("humanSpeed "+humanSpeed);
        }
        float rad =(float) Math.PI*cameraAngle/180.0f;

        Point currentHumanPositionFront = new Point(Math.round(humanPosition.x+sizeHuman*(float) Math.cos(rad)),
                Math.round(humanPosition.z+sizeHuman*(float) Math.sin(rad)));
        Point currentHumanPositionBack = new Point(Math.round(humanPosition.x-sizeHuman*(float) Math.cos(rad)),
                Math.round(humanPosition.z-sizeHuman*(float) Math.sin(rad)));
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            if(!checkCollision(currentHumanPositionFront)){
                cameraPosition.z+=(float)Math.sin(rad) * humanSpeed;
                cameraPosition.x+=(float)Math.cos(rad) * humanSpeed;
            }
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            if(!checkCollision(currentHumanPositionBack)) {
                cameraPosition.z -= (float) Math.sin(rad) * humanSpeed;
                cameraPosition.x -= (float) Math.cos(rad) * humanSpeed;
            }
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_R)){
            cameraPosition.z = cameraZ;
            cameraPosition.x = 0;
            cameraPosition.y = cameraY;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_R)){
            cameraPosition.z = cameraZ;
            cameraPosition.x = 0;
            cameraPosition.y = cameraY;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)){
            raftForce += 0.05;
            if(raftForce > raftMaxSpeed) raftForce = raftMaxSpeed;
            System.out.println("raftForce "+raftForce);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            raftForce -= 0.05;
            if(raftForce < 0) raftForce = 0;
        }
        float deltaTime = delta * 0.02f;
        if(raftForce > 1){

            if(raftSpeed > 5){
                raftSpeed = raftSpeed + (raftForce-1) * deltaTime * 0.05f;
            }else{
                raftSpeed += (raftForce-1) * deltaTime * 0.1;
            }
            cameraPosition.y += raftSpeed * deltaTime;
            humanPosition.y += raftSpeed * deltaTime;
        }else if(raftForce < 1 && humanPosition.y > 0){
            raftSpeed -= (1-raftForce) * deltaTime * 0.4f;
            cameraPosition.y += raftSpeed * deltaTime;
            humanPosition.y += raftSpeed * deltaTime;
        }
        if(humanPosition.y < 0){
            raftSpeed = 0;
            raftForce = 0f;
            humanPosition.y = 0;
            cameraPosition.y = cameraY;
        }
        float r = -cameraZ;
        checkBound();
        cameraDirection.x = (float)Math.cos(rad) * r + cameraPosition.x;
        cameraDirection.z = (float)Math.sin(rad) * r + cameraPosition.z;
        humanPosition.x = (float)Math.cos(rad)*r+cameraPosition.x;
        humanPosition.z = (float)Math.sin(rad)*r+cameraPosition.z;
        humanPosition.y = cameraPosition.y - cameraY;
        cameraDirection.y = cameraPosition.y;
		updateFPS(); // update FPS Counter
	}

    private boolean checkCollision(Point position) {
        for (Rectangle r: collisions) {
            if(r.contains(position)) return true;
        }
        return false;
    }

    private void checkBound() {
	    if(cameraPosition.y > museumSize*2 - sizeHuman){
            cameraPosition.y = museumSize*2 - sizeHuman - 10;
            raftForce = 0f;
            raftSpeed = 0;
        }
        if(cameraPosition.y < 0){
            cameraPosition.y = 0;
        }
        if(cameraPosition.z > museumSize - sizeHuman * 4){
            cameraPosition.z = museumSize - sizeHuman * 4;
            raftForce = 0f;
            raftSpeed = 0;
        }
        if(cameraPosition.z < -museumSize + sizeHuman * 4){
            cameraPosition.z = -museumSize + sizeHuman * 4;
            raftForce = 0f;
            raftSpeed = 0;
        }
        if(cameraPosition.x > museumSize - sizeHuman * 4){
            cameraPosition.x = museumSize - sizeHuman * 4;
            raftForce = 0f;
            raftSpeed = 0;
        }
        if(cameraPosition.x < -museumSize + sizeHuman * 4){
            cameraPosition.x = -museumSize + sizeHuman * 4;
            raftForce = 0f;
            raftSpeed = 0;
        }
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
	float lightIntensity = 0.1f;
    public void drawTestShere(Point4f position){
	    GL11.glPushMatrix();
        Sphere sphere = new Sphere();
        GL11.glTranslatef(position.x,position.y,position.z);
//        GL11.glTranslatef(0,300,0);
//        GL11.glColor3f(white[0],white[1],white[2]);
//        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, Utils.ConvertForGL(white));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, Utils.ConvertForGL(new float[]{0.0f,0.0f,0.0f,1}));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, Utils.ConvertForGL(new float[]{0.0f,0.0f,0.0f,1}));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, Utils.ConvertForGL(new float[]{0.0f,0.0f,0.0f,1}));
//        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, Utils.ConvertForGL(new float[]{lightIntensity,lightIntensity,lightIntensity,1}));
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 0f);
        sphere.DrawSphere(10f,16,16);
//        GL11.glRotatef(90,0,0,1);
        GL11.glPopMatrix();
    }
	public void initGL() {
		changeOrtho();
//		MyArcball.startBall(0, 0, 1200, 800);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
		lightPos.put(0f).put(-1000).put(0).put(1).flip();
		FloatBuffer lightPos1 = BufferUtils.createFloatBuffer(4);
		lightPos1.put(1000).put(0).put(0).put(1).flip();
        FloatBuffer lightPos2 = BufferUtils.createFloatBuffer(4);
        lightPos2.put(-1000).put(0).put(0).put(1).flip();
		FloatBuffer lightPos3 = BufferUtils.createFloatBuffer(4);
		lightPos3.put(0).put(1000f).put(0).put(1).flip();

		FloatBuffer lightPos4 = BufferUtils.createFloatBuffer(4);
		lightPos4.put(1000f).put(1000f).put(1000f).put(0).flip();
		// TODO test lighting
//		FloatBuffer lightPos5 = BufferUtils.createFloatBuffer(4);
//		lightPos.put(0f).put(0f).put(0f).put(1000f).flip();
//		GL11.glEnable(GL11.GL_LIGHT4);
//		GL11.glLight(GL11.GL_LIGHT4, GL11.GL_POSITION,lightPos5);


//        GL11.glColor3f(white[0],white[1],white[2]);
//        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPos); // specify the
																	// position
																	// of the
																	// light
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, Utils.ConvertForGL(new float[]{0.0f,0.0f,0.0f,1}));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, Utils.ConvertForGL(new float[]{1.0f,1.0f,1.0f,1}));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR , Utils.ConvertForGL(new float[]{1.0f,1.0f,1.0f,1}));
		// materials so in real light it will look a bit strange
//        GL11.glEnable(GL11.GL_LIGHT0); // switch light #0 on // I've setup specific

		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPos1); // specify the
																	// position
																	// of the
																	// light
        // position
        // of the
        // light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, Utils.ConvertForGL(new float[]{0.0f,0.0f,0.0f,1}));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, Utils.ConvertForGL(new float[]{1.0f,1.0f,1.0f,1}));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR , Utils.ConvertForGL(new float[]{1.0f,1.0f,1.0f,1}));
        // materials so in real light it will look a bit strange
        GL11.glEnable(GL11.GL_LIGHT1); // switch light #0 on // I've setup specific

        GL11.glLight(GL11.GL_LIGHT2, GL11.GL_POSITION, lightPos2); // specify the
        // position
        // of the
        // light
        // position
        // of the
        // light
        GL11.glLight(GL11.GL_LIGHT2, GL11.GL_AMBIENT, Utils.ConvertForGL(new float[]{0.0f,0.0f,0.0f,1}));
        GL11.glLight(GL11.GL_LIGHT2, GL11.GL_DIFFUSE, Utils.ConvertForGL(new float[]{1.0f,1.0f,1.0f,1}));
        GL11.glLight(GL11.GL_LIGHT2, GL11.GL_SPECULAR , Utils.ConvertForGL(new float[]{1.0f,1.0f,1.0f,1}));
        // materials so in real light it will look a bit strange
        GL11.glEnable(GL11.GL_LIGHT2); // switch light #0 on // I've setup specific

        GL11.glLight(GL11.GL_LIGHT3, GL11.GL_POSITION, lightPos3); // specify the
        // position
        // of the
        // light
        // position
        // of the
        // light
        GL11.glLight(GL11.GL_LIGHT3, GL11.GL_AMBIENT, Utils.ConvertForGL(new float[]{0.0f,0.0f,0.0f,1}));
        GL11.glLight(GL11.GL_LIGHT3, GL11.GL_DIFFUSE, Utils.ConvertForGL(new float[]{1.0f,1.0f,1.0f,1}));
        GL11.glLight(GL11.GL_LIGHT3, GL11.GL_SPECULAR , Utils.ConvertForGL(new float[]{1.0f,1.0f,1.0f,1}));
        // materials so in real light it will look a bit strange
        GL11.glEnable(GL11.GL_LIGHT3); // switch light #0 on // I've setup specific
//
//		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_POSITION, lightPosBack); // specify
//		GL11.glEnable(GL11.GL_LIGHT2); // switch light #0 on
//		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_DIFFUSE, Utils.ConvertForGL(grey));
//
//		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_POSITION, lightPos4); // specify
////																	// the
////																	// position
////																	// of the
////																	// light
////		GL11.glEnable(GL11.GL_LIGHT3); // switch light #0 on
//		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_DIFFUSE, Utils.ConvertForGL(grey));



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
	public void changeOrtho(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		//loadMatrix();
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
		//startLauncher(delta);
        // TODO test lootAt
        lookAtScene();
	}

	int npcBack = 100;
	int rocketSize = 100;
    private void lookAtScene() {
	    changeOrtho();
        GLU.gluPerspective(60f,1,0.1f,-1);
        GLU.gluLookAt( cameraPosition.x, cameraPosition.y,cameraPosition.z,cameraDirection.x, cameraDirection.y,
                cameraDirection.z, cameraUp.x,cameraUp.y,cameraUp.z );
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glColor3f(0.5f, 0.5f, 1.0f);
//        test(humanPosition);
//        drawTestShere(cameraPosition);
        drawTestShere(new Point4f(0,300,0,0));
        drawTestShere(new Point4f(300,0,0,0));
        drawTestShere(new Point4f(-300,0,0,0));
//        drawTestShere(new Point4f(0,0,300,0));
//        drawTestShere(new Point4f(0,0,-300,0));
        raftRotate = raftForce * 2 + raftRotate;
	    drawHuman(myRobotCat,raftRotate, humanPosition, humanRotation);
        drawHuman(npc,handRotate, new Point4f(museumSize - npcBack,0,npcBack*2,1), 90);
        drawHuman(npc,handRotate, new Point4f(-museumSize + npcBack,0,-npcBack*2,1), -90);
        drawHuman(daXiong,handRotate, new Point4f(npcBack,0,museumSize - npcBack,1), 0);
        drawRocket(new Point(-rocketSize,museumSize-rocketSize), rocketSize,rockets[0],racketRotate);
        drawRocket(new Point(museumSize-rocketSize,-rocketSize ), Math.round(rocketSize*1.2f),rockets[1],90);
        drawRocket(new Point(-museumSize+rocketSize,rocketSize ), Math.round(rocketSize*1.5f),rockets[2],0);
        for(Rectangle r : collisions){
            drawRectangle(r);
        }
//	    drawBackground(new Point4f(0,100,0,1));
        // ground
	    drawMuseum(new Point4f(0,0,0,1),ground_texture);
	    // TODO sky box
        drawMuseum(new Point4f(0,museumSize*2,0,1),sky);
        GL11.glPushMatrix();
        // TODO the front wall
	    GL11.glRotatef(90,1,0,0);
        drawMuseum(new Point4f(0,museumSize,-museumSize,1), wallFront);
        drawMuseum(new Point4f(0,-museumSize,-museumSize,1), door);
	    GL11.glPopMatrix();
        GL11.glPushMatrix();
        // TODO the side wall
        GL11.glRotatef(90,0,0,1);
        drawMuseum(new Point4f(museumSize,museumSize,0,1), wallSide);
        drawMuseum(new Point4f(museumSize,-museumSize,0,1), wallSide);
        GL11.glPopMatrix();
    }

    int museumSize = 1000;
	public void drawMuseum(Point4f position, Texture texture){
        GL11.glPushMatrix();

        GL11.glTranslatef(position.x,position.y,position.z);
        GL11.glScalef(museumSize,museumSize,museumSize);
        // move back
        Point4f ground_points[] = {new Point4f(-1,0,-1,1),new Point4f(1,0,-1,1),
                new Point4f(1,0,1,1),new Point4f(-1,0,1,1)};
        drawQuadsWithTexture(texture,ground_points);
        GL11.glPopMatrix();
    }

	public void drawRocket(Point position, int rocketSize, Rocket rocket, float rotation) {
		GL11.glPushMatrix();
		GL11.glTranslatef(position.x,0,position.y);
		GL11.glRotatef(rotation,0,1,0);
		GL11.glScalef(rocketSize,rocketSize,rocketSize);
//		rocket.body = background_sky;
		// TODO:draw rocket
		rocket.DrawRocket(0, isLaunched);
		GL11.glPopMatrix();
	}

	public void drawHuman(Human human, float speed, Point4f position, float angle) {
		GL11.glPushMatrix();
		// TODO: the position of human
		GL11.glTranslatef(position.x, position.y + sizeHuman * 1.4f, position.z);
		GL11.glScalef(sizeHuman, sizeHuman, sizeHuman);
		// TODO rotate human to left
		GL11.glRotatef(angle,0,1,0);
        human.DrawHuman(speed * 8, isLaunched); // give a delta for the RobotCat object ot be animated
//		myRobotCat.beginPress = beginPress;
//		isTouched = myRobotCat.isTouched;
//		isLaunched = myRobotCat.isPressed;
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
	Texture target;
	Texture sky;
	Texture wall;
	Texture wallFront;
	Texture door;
	Texture wallSide;
	Texture child;
//	Texture rocketBody;
	/*
	 * Any additional textures for your assignment should be written in here. Make a
	 * new texture variable for each one so they can be loaded in at the beginning
	 */
	public void init() throws IOException {
		// load all textures here
		// TODO:add texture here
		texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/earthspace.png"));
		sign_texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/sign.png"));
		head_texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/head.png"));
		upperbody_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/upperbody.jpg"));
		lowerbody_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/lowerbody.jpg"));
		joint_texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/joint.jpg"));
		background_sky = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/background_sky.jpg"));
		target = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("res/2018.png"));
		ground_texture = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/ground.jpg"));
		sky = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/sky.jpg"));
		wall = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/wall.jpg"));
        wallFront = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/wall1.jpg"));
        wallSide = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/wall2.jpg"));
        door = TextureLoader.getTexture("JPG",ResourceLoader.getResourceAsStream("res/door.jpg"));
        child = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("res/2018.png"));
//        rocketBody = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("res/rocketBodyS.png"));
		System.out.println("Texture loaded okay ");

	}

}
