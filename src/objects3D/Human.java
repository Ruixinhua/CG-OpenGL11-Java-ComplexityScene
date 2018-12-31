package objects3D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import GraphicsObjects.Utils;

public class Human implements Collision{

    // basic colours
    static float black[] = {0.0f, 0.0f, 0.0f, 1.0f};
    static float white[] = {1.0f, 1.0f, 1.0f, 1.0f};

    static float grey[] = {0.5f, 0.5f, 0.5f, 1.0f};
    static float spot[] = {0.1f, 0.1f, 0.1f, 0.5f};

    // primary colours
    static float red[] = {1.0f, 0.0f, 0.0f, 1.0f};
    static float green[] = {0.0f, 1.0f, 0.0f, 1.0f};
    static float blue[] = {0.0f, 0.0f, 1.0f, 1.0f};

    // secondary colours
    static float yellow[] = {1.0f, 1.0f, 0.0f, 1.0f};
    static float magenta[] = {1.0f, 0.0f, 1.0f, 1.0f};
    static float cyan[] = {0.0f, 1.0f, 1.0f, 1.0f};

    static float boneColor[] = {11f / 255, 151f / 255, 217f / 255, 1f};

    // other colours
    static float orange[] = {1.0f, 0.5f, 0.0f, 1.0f, 1.0f};
    static float brown[] = {0.5f, 0.25f, 0.0f, 1.0f, 1.0f};
    static float dkgreen[] = {0.0f, 0.5f, 0.0f, 1.0f, 1.0f};
    static float pink[] = {1.0f, 0.6f, 0.6f, 1.0f, 1.0f};

    // here is the texture of human:head, body, and joint
    public Texture head_texture;
    public Texture upperbody_texture;
    public Texture lowerbody_texture;
    //public Texture joint_texture;
    public boolean isPressed = false;
    public boolean beginPress = false;
    public boolean isTouched = false;
    // TODO set left arm angle limit
    public float leftArmAngle = 30;
    public float leftArmAngleLimit = 70;
    public float leftArmPutAngleLimit = 110;
    public float leftArmPutAngle = 0;
    public float leftArmPressAngleLimit = 25;
    public float leftArmPressAngle = 0;
    // I use this texture sphere to draw texture
    TexSphere myTexture;
    Sphere sphere;
    Cylinder cylinder;

    public Human() {
        myTexture = new TexSphere();
        sphere = new Sphere();
        cylinder = new Cylinder();
    }
    protected void drawLowerBody(){
        // lower body
        GL11.glTranslatef(0.0f, 0.5f, 0.0f);
        GL11.glPushMatrix();
        {
            GL11.glRotated(90, 1f, 0f, 0f);
            GL11.glRotated(-90, 0f, 0f, 1f);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
            myTexture.DrawTexSphere(0.5f, 16, 16, lowerbody_texture);
            // draw a tail using line and sphere
            GL11.glColor3f(black[0], black[1], black[2]);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex3f(0, 0, 0);
            GL11.glVertex3f(-0.6f, 0f, 0.6f);
            GL11.glEnd();
            GL11.glTranslatef(-0.6f, 0f, 0.6f);
            // the end red sphere of tail
            GL11.glColor3f(red[0], red[1], red[2]);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(red));
            sphere.DrawSphere(0.15f, 16, 16f);
        }
        GL11.glPopMatrix();
    }
    protected void drawUpperBody(){
        // here is the upper body with upper body texture
        GL11.glTranslatef(0.0f, 0.5f, 0.0f);
        GL11.glPushMatrix();
        {
            GL11.glRotated(90, 1f, 0f, 0f);
            GL11.glRotated(-90, 0f, 0f, 1f);
            // draw upper body texture
            myTexture.DrawTexSphere(0.5f, 16, 16, upperbody_texture);
        }
        GL11.glPopMatrix();
    }
    protected void drawRaft(float speed){
        // draw the rod
        // The Bamboo raft
        GL11.glColor3f(red[0], red[1], red[2]);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
        GL11.glTranslatef(0f, 0f, -0.5f);
        sphere.DrawSphere(0.1f, 8, 8);
        GL11.glTranslatef(0f, 0f, -0.3f);
        cylinder.DrawCylinder(0.05f, 0.4f, 8);
        GL11.glPushMatrix();
        {
            // draw the raft
            sphere.DrawSphere(0.1f, 8, 8);
            GL11.glRotatef(speed, 0f, 0f, 1f);
            // GL11.glTranslatef(0f, -0.2f, 0f);
            drawRaftLeft();
            drawRaftRight();
        }
        GL11.glPopMatrix();
    }
    protected void drawRaftRight() {
        GL11.glPushMatrix();
        {
            // rotate the raft
            GL11.glRotatef(-90, 1f, 0f, 0f);
//            GL11.glRotatef(180, 0f, 0f, 1f);
            cylinder.DrawCylinder(0.05f, 0.4f, 8);

        }
        GL11.glPopMatrix();
    }
    protected void drawRaftLeft() {
        GL11.glPushMatrix();
        {
            // rotate the raft
            GL11.glRotatef(90, 1f, 0f, 0f);
            GL11.glRotatef(180, 0f, 0f, 1f);
            cylinder.DrawCylinder(0.05f, 0.4f, 8);

        }
        GL11.glPopMatrix();
    }
    protected void drawHead(float speed) {
        // head
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.0f, 0.0f, 1.0f);
            GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
            // draw head using head texture
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(black));
            myTexture.DrawTexSphere(0.5f, 16, 16, head_texture);
            drawRaft(speed);
        }
        GL11.glPopMatrix();
    }
    protected void drawNeck(float speed){
        // neck
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.0f, 0.0f, 0.0f);
            GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            drawBones();
            drawHead(speed);
        }
        GL11.glPopMatrix();
    }

    protected void drawBones() {
        GL11.glColor3f(boneColor[0], boneColor[1], boneColor[2]);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(black));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, Utils.ConvertForGL(black));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, Utils.ConvertForGL(black));
        cylinder.DrawCylinder(0.15f, 0.55f, 16);
    }

    protected void drawJoints(){
        GL11.glColor3f(13f / 255f, 152f / 255f,217f / 255f);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(black));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, Utils.ConvertForGL(black));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, Utils.ConvertForGL(black));
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 30f);
        sphere.DrawSphere(0.25f, 16, 16);
    }
    protected void drawLeftShoulder(){
        // left shoulder with boneColor color
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(-0.5f, 0.4f, 0.0f);
            drawJoints();
            // left arm with boneColor color
            GL11.glTranslatef(0.0f, 0.0f, 0.0f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            // TODO:rotate arm
            GL11.glRotatef(-30, 0, 1f, 0.0f);
//            GL11.glRotatef(90f, 0.0f, 0.0f, 1.0f);
           drawBones();

            // left elbow with joint texture
            normalArm();
        }
        GL11.glPopMatrix();
    }
    protected void drawRightShoulder() {
        // right shoulder with joint texture
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.5f, 0.4f, 0.0f);
            drawJoints();
            // right arm with boneColor color
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

            // TODO:rotate left arm
//                if (beginPress) {
//                    float armSpeed = speed * 40;
//                    leftArmAngle += armSpeed;
//                    if (leftArmAngle > leftArmAngleLimit) {
//                        leftArmAngle = leftArmAngleLimit;
//                    }
//                    leftArmPutAngle += armSpeed;
//                    if (leftArmPutAngle > leftArmPutAngleLimit) {
//                        leftArmPutAngle = leftArmPutAngleLimit;
//
//                    }
//                    leftArmPressAngle += armSpeed;
//                    if (leftArmPressAngle > 10) {
//                        isTouched = true;
//                    }
//                    if (leftArmPressAngle > leftArmPressAngleLimit)
//                        leftArmPressAngle = leftArmPressAngleLimit;
//                }
            GL11.glRotatef(30, 0, 1f, 0.0f);
//                if (leftArmPressAngle == leftArmPressAngleLimit && leftArmPutAngle == leftArmPutAngleLimit && leftArmAngle == leftArmAngleLimit) {
//                    isPressed = true;
//
//                }else{
//                    GL11.glRotatef(leftArmAngle, 0f, 1.0f, 0.0f);
//                    GL11.glRotatef(leftArmPutAngle, 1, 0, 0f);
//                    GL11.glRotatef(-leftArmPressAngle, 0f, 1f, 0f);
//                }
            drawBones();
            drawJoints();
            normalArm();
        }
        GL11.glPopMatrix();
    }
    protected void drawChest(float speed){
        // chest: draw upper body first than neck and head
        GL11.glPushMatrix();
        {
            drawUpperBody();
            drawNeck(speed);
            drawLeftShoulder();
            // to chest
            drawRightShoulder();
        }
        // chest
        GL11.glPopMatrix();
    }
    // Implement using notes in Animation lecture
    public void DrawHuman(float speed, boolean isLauncher) {
//        float theta = (float) (speed * 2 * Math.PI);
//        float LimbRotation;
//        float raftRotate;
//        if (isLauncher) {
//            LimbRotation = (float) Math.cos(theta) * 45;
//            // the speed of raft rotate can be set here
//            raftRotate = (speed * 2000) % 360;
//        } else {
//            LimbRotation = 0f;
//            raftRotate = 0f;
//        }
        // lower body
        drawLowerBody();
        // chest
        drawChest(speed);
        // pelvis
        drawPelvis();
    }
    protected void drawPelvis() {
        drawLeftLeg();
        // pelvis
        drawRightLeg();
    }
    protected void drawLeftLeg() {
        // left hip with joint texture
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(-0.5f, -0.2f, 0.0f);
            drawJoints();

            // left high leg withe boneColor color
            GL11.glTranslatef(0.0f, 0.0f, 0.0f);
            GL11.glRotatef(30, 0.0f, 1.0f, 0.0f);

            GL11.glRotatef(90, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
            drawBones();
            keenAndLeg();
        }
        GL11.glPopMatrix();

    }

    protected void drawRightLeg(){
        // right hip with joint texture
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.5f, -0.2f, 0.0f);
            drawJoints();

            // right high leg with boneColor color

            GL11.glTranslatef(0.0f, 0.0f, 0.0f);
            GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);

            GL11.glRotatef(+90, 1.0f, 0.0f, 0.0f);
            drawBones();

            keenAndLeg();
        }
        GL11.glPopMatrix();
    }

    protected void keenAndLeg() {
        // right knee with joint texture
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.0f, 0.0f, 0.75f);
            GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);
            drawJoints();
            // right low leg with boneColor color
            // rotate right leg
            GL11.glTranslatef(0.0f, 0.0f, 0.0f);
            drawBones();
            // right foot using white color
            GL11.glColor3f(white[0], white[1], white[2]);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
            GL11.glTranslatef(0.0f, 0.0f, 0.75f);
            sphere.DrawSphere(0.3f, 16, 16);
        }
        GL11.glPopMatrix();
    }

    protected void normalArm() {
        GL11.glTranslatef(0.0f, 0.0f, 0.75f);
        drawJoints();

        // right forearm
        GL11.glColor3f(boneColor[0], boneColor[1], boneColor[2]);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.0f, 0.0f, 0.0f);
//			GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            drawHand();
        }
        GL11.glPopMatrix();
    }

    protected void drawHand() {
        drawBones();
        // right hand with white color
        GL11.glColor3f(white[0], white[1], white[2]);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
        GL11.glTranslatef(0.0f, 0.0f, 0.75f);
        sphere.DrawSphere(0.3f, 16, 16);
    }

    @Override
    public void detectCollision(Collision object) {

    }
}


/*
 *
 *
 * }
 *
 */
