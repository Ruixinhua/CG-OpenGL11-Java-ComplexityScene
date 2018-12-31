package objects3D;

import GraphicsObjects.Utils;
import org.lwjgl.opengl.GL11;

public class NormalHuman extends Human {
    public float rotateAngle = 0;

    @Override
    protected void drawLeftShoulder() {
        // left shoulder with boneColor color
        GL11.glPushMatrix();
        {
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(boneColor));
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(-0.5f, 0.4f, 0.0f);
                drawJoints();
                // left arm with boneColor color
                GL11.glPushMatrix();
                {
                    GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                    GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                    // TODO:rotate arm
                    int temp = (int)(rotateAngle / 20) % 2;
                    if(temp == 0){
                        GL11.glRotatef(-140+(rotateAngle % 20), 0, 1f, 0.0f);
                    }else{
                        GL11.glRotatef(-(rotateAngle % 20)-120, 0, 1f, 0.0f);
                    }
//                    GL11.glRotatef(90f, 0.0f, 0.0f, 1.0f);
                    drawBones();

                    // left elbow with joint texture
                    GL11.glTranslatef(0.0f, 0.0f, 0.75f);
                    drawJoints();
                    // right forearm
                    GL11.glPushMatrix();
                    {
//			        GL11.glRotatef(30.0f, 0.0f, 1.0f, 0.0f);
                        temp = (int)(rotateAngle / 20) % 2;
                        if(temp == 0){
                            GL11.glRotatef(-40+(rotateAngle % 20)*2, 0, 1f, 0.0f);
                        }else{
                            GL11.glRotatef(-(rotateAngle % 20)*2, 0, 1f, 0.0f);
                        }
                        drawHand();
                    }
                    GL11.glPopMatrix();
                }
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }
    protected void drawRaft(float speed){
        // draw the rod
        // The Bamboo raft

        GL11.glColor3f(orange[0], orange[1], orange[2]);
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
}
