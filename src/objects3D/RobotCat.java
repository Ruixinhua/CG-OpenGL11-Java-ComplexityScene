package objects3D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import GraphicsObjects.Utils;

public class RobotCat extends Human{
    protected void drawRaft(float speed){
        // draw the rod
        // The Bamboo raft
        GL11.glColor3f(yellow[0], yellow[1], yellow[2]);
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


/*
 *
 *
 * }
 *
 */
