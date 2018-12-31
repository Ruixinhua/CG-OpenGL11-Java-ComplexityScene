package objects3D;

import org.lwjgl.opengl.GL11;

public class RocketB extends Rocket {

    public void DrawRocket(float delta, boolean isLaunch) {
        speed = delta;

        if(isLaunch){
            GL11.glTranslatef(0, speed*speed / 2, 0);
        }

        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0f, groundUp, 0f);
            GL11.glRotatef(-90, 1, 0, 0);
            GL11.glPushMatrix();
            {
                drawRocketBody(radius, height,bodys[0]);
                GL11.glTranslatef(radiusTemp+radius,0,0);
                drawRocketBody(radiusTemp,rocketBody,bodys[1]);
                GL11.glTranslatef(-radiusTemp-radius,radiusTemp+radius,0);
//                drawRocketBody(radiusTemp,rocketBody,bodys[1]);
                GL11.glTranslatef(-radiusTemp-radius,-radius-radiusTemp,0);
                drawRocketBody(radiusTemp,rocketBody,bodys[2]);
                GL11.glTranslatef(radiusTemp+radius-0.02f,-radius-radiusTemp,0);
//                drawRocketBody(radiusTemp,rocketBody,bodys[3]);
            }
            GL11.glPopMatrix();
            drawRocketWing();

        }
        GL11.glPopMatrix();
    }
}
