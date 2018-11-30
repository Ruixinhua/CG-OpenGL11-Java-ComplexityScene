package objects3D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import GraphicsObjects.Utils;

public class Human {

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

    static float joint_color[] = {11f / 255, 151f / 255, 217f / 255, 1f};

    // other colours
    static float orange[] = {1.0f, 0.5f, 0.0f, 1.0f, 1.0f};
    static float brown[] = {0.5f, 0.25f, 0.0f, 1.0f, 1.0f};
    static float dkgreen[] = {0.0f, 0.5f, 0.0f, 1.0f, 1.0f};
    static float pink[] = {1.0f, 0.6f, 0.6f, 1.0f, 1.0f};

    // here is the texture of human:head, body, and joint
    public Texture head_texture;
    public Texture upperbody_texture;
    public Texture lowerbody_texture;
    public Texture joint_texture;
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

    // Implement using notes in Animation lecture
    public void DrawHuman(float speed, boolean isLauncher) {
        float theta = (float) (speed * 2 * Math.PI);
        float LimbRotation;
        float raftRotate;
        if (isLauncher) {
            LimbRotation = (float) Math.cos(theta) * 45;
            // the speed of raft rotate can be set here
            raftRotate = (speed * 2000) % 360;
        } else {
            LimbRotation = 0f;
            raftRotate = 0f;
        }


        GL11.glPushMatrix();
        {
            // lower body
            GL11.glTranslatef(0.0f, 0.5f, 0.0f);
            GL11.glPushMatrix();
            {
                GL11.glRotated(90, 1f, 0f, 0f);
                GL11.glRotated(-90, 0f, 0f, 1f);
                myTexture.DrawTexSphere(0.5f, 32, 32, lowerbody_texture);
                // draw a tail using line and sphere
                GL11.glPushMatrix();
                {
                    GL11.glColor3f(black[0], black[1], black[2]);
                    GL11.glBegin(GL11.GL_LINES);
                    GL11.glVertex3f(0, 0, 0);
                    GL11.glVertex3f(-0.6f, 0f, 0.6f);
                    GL11.glEnd();
                    GL11.glTranslatef(-0.6f, 0f, 0.6f);
                    // the end red sphere of tail
                    GL11.glPushMatrix();
                    {
                        GL11.glColor3f(red[0], red[1], red[2]);
                        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(red));
                        sphere.DrawSphere(0.15f, 32, 32f);
                    }
                    GL11.glPopMatrix();

                }
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();

            // chest: draw upper body first than neck and head
            GL11.glPushMatrix();
            {
                // here is the upper body with upper body texture
                GL11.glTranslatef(0.0f, 0.5f, 0.0f);
                GL11.glPushMatrix();
                {
                    GL11.glRotated(90, 1f, 0f, 0f);
                    GL11.glRotated(-90, 0f, 0f, 1f);
                    // draw upper body texture
                    myTexture.DrawTexSphere(0.5f, 32, 32, upperbody_texture);
                }
                GL11.glPopMatrix();

                // neck
                GL11.glPushMatrix();
                {
                    GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                    GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                    GL11.glColor3f(joint_color[0], joint_color[1], joint_color[2]);
                    GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(joint_color));
                    cylinder.DrawCylinder(0.15f, 0.7f, 32);

                    // head
                    GL11.glPushMatrix();
                    {

                        GL11.glTranslatef(0.0f, 0.0f, 1.0f);
                        GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
                        // draw head using head texture
                        myTexture.DrawTexSphere(0.5f, 32, 32, head_texture);
                        // The Bamboo raft
                        GL11.glColor3f(yellow[0], yellow[1], yellow[2]);
                        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(yellow));
                        GL11.glPushMatrix();
                        {
                            // draw the rod
                            GL11.glTranslatef(0f, 0f, -0.5f);
                            sphere.DrawSphere(0.1f, 32, 32);
                            GL11.glTranslatef(0f, 0f, -0.3f);
                            cylinder.DrawCylinder(0.05f, 0.4f, 32);
                            GL11.glPushMatrix();
                            {
                                // draw the raft
                                sphere.DrawSphere(0.1f, 32, 32);
                                // GL11.glTranslatef(0f, -0.2f, 0f);
                                GL11.glPushMatrix();
                                {
                                    // rotate the raft
                                    GL11.glRotatef(90, 1f, 0f, 0f);
                                    GL11.glRotatef(raftRotate, 0f, 1f, 0f);
                                    cylinder.DrawCylinder(0.05f, 0.4f, 32);

                                }
                                GL11.glPopMatrix();
                                GL11.glPushMatrix();
                                {
                                    // rotate the raft
                                    GL11.glRotatef(-90, 1f, 0f, 0f);
                                    GL11.glRotatef(-raftRotate, 0f, 1f, 0f);
                                    cylinder.DrawCylinder(0.05f, 0.4f, 32);

                                }
                                GL11.glPopMatrix();
                            }
                            GL11.glPopMatrix();
                        }
                        GL11.glPopMatrix();

                    }
                    GL11.glPopMatrix();
                }
                GL11.glPopMatrix();
                // left shoulder with joint texture
                GL11.glPushMatrix();
                {
                    GL11.glTranslatef(0.5f, 0.4f, 0.0f);
                    myTexture.DrawTexSphere(0.25f, 32, 32, joint_texture);

                    // left arm with joint_color color
                    GL11.glColor3f(joint_color[0], joint_color[1], joint_color[2]);
                    GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(joint_color));
                    GL11.glPushMatrix();
                    {
                        GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                        // TODO:rotate left arm
                        if (beginPress) {
                            float armSpeed = speed * 40;
                            leftArmAngle += armSpeed;
                            if (leftArmAngle > leftArmAngleLimit) {
                                leftArmAngle = leftArmAngleLimit;
                            }
                            leftArmPutAngle += armSpeed;
                            if (leftArmPutAngle > leftArmPutAngleLimit) {
                                leftArmPutAngle = leftArmPutAngleLimit;

                            }
                            leftArmPressAngle += armSpeed;
                            if (leftArmPressAngle > 10) {
                                isTouched = true;
                            }
                            if (leftArmPressAngle > leftArmPressAngleLimit)
                                leftArmPressAngle = leftArmPressAngleLimit;
                        }
                        if (leftArmPressAngle == leftArmPressAngleLimit && leftArmPutAngle == leftArmPutAngleLimit && leftArmAngle == leftArmAngleLimit) {
                            isPressed = true;
                            GL11.glRotatef(30, 0, 1f, 0.0f);
                        }else{
                            GL11.glRotatef(leftArmAngle, 0f, 1.0f, 0.0f);
                            GL11.glRotatef(leftArmPutAngle, 1, 0, 0f);
                            GL11.glRotatef(-leftArmPressAngle, 0f, 1f, 0f);
                        }
                        cylinder.DrawCylinder(0.15f, 0.7f, 32);
                        normalArm();

                    }
                    GL11.glPopMatrix();
                }
                GL11.glPopMatrix();
                // to chest
                // right shoulder with joint_color color
                GL11.glPushMatrix();
                {
                    GL11.glColor3f(joint_color[0], joint_color[1], joint_color[2]);
                    GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(joint_color));
                    GL11.glPushMatrix();
                    {
                        GL11.glTranslatef(-0.5f, 0.4f, 0.0f);
                        myTexture.DrawTexSphere(0.25f, 32, 32, joint_texture);
                        // right arm with joint_color color
                        GL11.glColor3f(joint_color[0], joint_color[1], joint_color[2]);
                        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(joint_color));
                        GL11.glPushMatrix();
                        {
                            GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                            // TODO:rotate arm
                            GL11.glRotatef(-30, 0, 1f, 0.0f);
                            cylinder.DrawCylinder(0.15f, 0.7f, 32);

                            // right elbow with joint texture
                            normalArm();
                        }
                        GL11.glPopMatrix();
                    }
                    GL11.glPopMatrix();
                }
                GL11.glPopMatrix();
            }
            // chest
            GL11.glPopMatrix();

            // pelvis

            // left hip with joint texture
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(-0.5f, -0.2f, 0.0f);

                myTexture.DrawTexSphere(0.25f, 32, 32, joint_texture);

                // left high leg withe joint_color color
                GL11.glColor3f(joint_color[0], joint_color[1], joint_color[2]);
                GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(joint_color));
                GL11.glPushMatrix();
                {
                    GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                    GL11.glRotatef(30, 0.0f, 1.0f, 0.0f);

                    GL11.glRotatef(90, 1.0f, 0.0f, 0.0f);
                    cylinder.DrawCylinder(0.15f, 0.7f, 32);
                    keenAndLeg();
                }
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();

            // pelvis

            // right hip with joint texture
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(0.5f, -0.2f, 0.0f);

                myTexture.DrawTexSphere(0.25f, 32, 32, joint_texture);

                // right high leg with joint_color color
                GL11.glColor3f(joint_color[0], joint_color[1], joint_color[2]);
                GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(joint_color));
                GL11.glPushMatrix();
                {
                    GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                    GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);

                    GL11.glRotatef(+90, 1.0f, 0.0f, 0.0f);
                    cylinder.DrawCylinder(0.15f, 0.7f, 32);
                    keenAndLeg();
                }
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    private void keenAndLeg() {
        // right knee with joint texture
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.0f, 0.0f, 0.75f);
            GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);
            myTexture.DrawTexSphere(0.25f, 32, 32, joint_texture);

            // right low leg with joint_color color
            GL11.glColor3f(joint_color[0], joint_color[1], joint_color[2]);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(joint_color));
            GL11.glPushMatrix();
            {
                // rotate right leg
                GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                cylinder.DrawCylinder(0.15f, 0.7f, 32);

                // right foot using white color
                GL11.glColor3f(white[0], white[1], white[2]);
                GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(white));
                GL11.glPushMatrix();
                {
                    GL11.glTranslatef(0.0f, 0.0f, 0.75f);
                    sphere.DrawSphere(0.3f, 32, 32);
                }
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    public void normalArm() {
        GL11.glTranslatef(0.0f, 0.0f, 0.75f);
        myTexture.DrawTexSphere(0.2f, 32, 32, joint_texture);

        // right forearm
        GL11.glColor3f(joint_color[0], joint_color[1], joint_color[2]);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(joint_color));
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.0f, 0.0f, 0.0f);
//			GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            cylinder.DrawCylinder(0.1f, 0.7f, 32);

            // right hand with white color
            GL11.glColor3f(white[0], white[1], white[2]);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE,
                    Utils.ConvertForGL(white));
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(0.0f, 0.0f, 0.75f);
                sphere.DrawSphere(0.2f, 32, 32);
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }
}


/*
 *
 *
 * }
 *
 */
