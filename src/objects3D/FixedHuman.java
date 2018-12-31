package objects3D;

import GraphicsObjects.Utils;
import org.lwjgl.opengl.GL11;

public class FixedHuman extends Human {
    public float rotateAngle = 0;
    protected void drawRightShoulder(){
        // right shoulder with joint texture
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.5f, 0.4f, 0.0f);
            drawJoints();
            // right arm with boneColor color
            GL11.glPushMatrix();
            {
                GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                int temp = (int)(rotateAngle / 20) % 2;
                if(temp == 0){
                    GL11.glRotatef(140-(rotateAngle % 20), 0, 1f, 0.0f);
                }else{
                    GL11.glRotatef((rotateAngle % 20)+120, 0, 1f, 0.0f);
                }
                drawBones();
                GL11.glTranslatef(0.0f, 0.0f, 0.75f);
                drawJoints();
                // right forearm
                GL11.glPushMatrix();
                {
//			        GL11.glRotatef(30.0f, 0.0f, 1.0f, 0.0f);
                    temp = (int)(rotateAngle / 20) % 2;
                    if(temp == 0){
                        GL11.glRotatef(40-(rotateAngle % 20)*2, 0, 1f, 0.0f);
                    }else{
                        GL11.glRotatef((rotateAngle % 20)*2, 0, 1f, 0.0f);
                    }
                    drawHand();
                }
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

}
