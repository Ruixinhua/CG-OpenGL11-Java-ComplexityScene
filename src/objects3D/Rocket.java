package objects3D;

import GraphicsObjects.Point4f;
import GraphicsObjects.Utils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import java.nio.FloatBuffer;

public class Rocket {
    static float bodyColor[] = {218f / 255f, 218f / 255f, 218f / 255f, 1.0f};
    static float blue[] = {11f / 255, 87f / 255, 168f / 255, 1.0f};
    static float red[] = {1.0f, 0.0f, 0.0f, 1.0f};
    static float fireColor[] = {243f / 255f, 163f / 255f,96f / 255f, 1f};
    float radius = 0.5f;
    float height = 6f;
    float groundUp = 0.2f; // the distance far from the ground
    float speed;
    float radiusTemp = radius / 2;
    float rocketBody = height/3;
    float fireRadius = radiusTemp*3/5;
    float fireHeight = height / 8;
    Cylinder cylinder = new Cylinder();;
    TexCylinder texCylinder = new TexCylinder();;
    Cone cone = new Cone();;
    Sphere sphere= new Sphere();;
    public Texture bodys[];
    public Rocket() {
    }

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

                drawRocketBody(radius, height,null);
                if(isLaunch) drawRocketFire((radius * 3) / 5,height / 6);
                GL11.glTranslatef(radiusTemp+radius-0.02f,0,0);
                drawRocketBody(radiusTemp,rocketBody,null);
                if(isLaunch) drawRocketFire(fireRadius,fireHeight);
                GL11.glTranslatef(-radiusTemp-radius+0.02f,radiusTemp+radius,0);
                drawRocketBody(radiusTemp,rocketBody,null);
                if(isLaunch) drawRocketFire(fireRadius,fireHeight);
                GL11.glTranslatef(-radiusTemp-radius+0.02f,-radius-radiusTemp,0);
                drawRocketBody(radiusTemp,rocketBody,null);
                if(isLaunch) drawRocketFire(fireRadius,fireHeight);
                GL11.glTranslatef(radiusTemp+radius-0.02f,-radius-radiusTemp,0);
                drawRocketBody(radiusTemp,rocketBody,null);
                if(isLaunch) drawRocketFire(fireRadius,fireHeight);
            }
            GL11.glPopMatrix();
            drawRocketWing();

        }
        GL11.glPopMatrix();
    }
    public void drawRocketFire(float radius, float height){
        GL11.glColor3f(fireColor[0],fireColor[1],fireColor[2]);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(fireColor));
        GL11.glPushMatrix();
        {
           // GL11.glTranslatef(0f, -groundUp, 0f);
            GL11.glRotatef(90, 1, 0, 0);
//            FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
//            lightPos.put(0).put(0).put(0).put(1000f).flip();
//            // TODO:set light
//            GL11.glLight(GL11.GL_LIGHT4, GL11.GL_AMBIENT, lightPos);
//            GL11.glEnable(GL11.GL_LIGHT4);
            sphere.DrawSphere(radius,16,16);
            GL11.glTranslatef(0,(0.2f - radius) / 2 + 0.15f,0);
            GL11.glRotatef(90,1,0,0);
            cone.DrawCone(radius,height,16, 0.5f);
        }
        GL11.glPopMatrix();
    }

    public void drawRocketBody(float radius, float height, Texture texture){
        GL11.glPushMatrix();
        {
//            GL11.glColor3f(red[0], red[1], red[2]);
//            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(red));
//            cylinder.DrawCylinder((radius * 3) / 5, height, 16);
            GL11.glRotatef(90,0,0,1);
            GL11.glColor3f(bodyColor[0], bodyColor[1], bodyColor[2]);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(bodyColor));
            if(texture != null){
                texCylinder.DrawCylinder(radius,height,16,texture);
            }else{
                cylinder.DrawCylinder(radius, height, 16);
            }
            GL11.glTranslatef(0, 0, height);
            GL11.glColor3f(blue[0], blue[1], blue[2]);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
            cone.DrawCone(radius, radius * 4, 16, 0f);
        }
        GL11.glPopMatrix();
    }

    public void drawRocketWing(){
        float halfRadius = radius / 2;
        float squre = (float) Math.sqrt(3d);
        float gap = halfRadius + height / 12;
        GL11.glPushMatrix();
        {
            GL11.glRotatef(90, 1, 0, 0);
            GL11.glColor3f(blue[0], blue[1], blue[2]);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
            GL11.glBegin(GL11.GL_QUADS);
            Point4f points[] = {
                    new Point4f(-radius, 0f, 0f, 0), new Point4f(-radius, height / 5, 0f, 0),
                    new Point4f(-radius - height / 6, height / 8, 0, 0), new Point4f(-radius - height / 6, -groundUp, 0, 0),
                    new Point4f(radius, 0f, 0f, 0), new Point4f(radius, height / 5, 0f, 0),
                    new Point4f(radius + height / 6, height / 8, 0, 0), new Point4f(radius + height / 6, -groundUp, 0, 0),

                    new Point4f(-halfRadius * squre, height / 2, -halfRadius, 0), new Point4f(-halfRadius * squre, height / 2 + height / 6, -halfRadius, 0),
                    new Point4f(-gap * squre, height / 2 + height / 6 - height / 20, -gap, 0), new Point4f(-gap * squre, height / 2 + height / 20, -gap, 0),
                    new Point4f(halfRadius * squre, height / 2, -halfRadius, 0), new Point4f(halfRadius * squre, height / 2 + height / 6, -halfRadius, 0),
                    new Point4f(gap * squre, height / 2 + height / 6 - height / 20, -gap, 0), new Point4f(gap * squre, height / 2 + height / 20, -gap, 0),
                    new Point4f(0f, height / 2, radius, 0), new Point4f(0f, height / 2 + height / 6, radius, 0),
                    new Point4f(0f, height / 2 + height / 6 - height / 20, gap * 2, 0), new Point4f(0f, height / 2 + height / 20, gap * 2, 0)
            };
            for (int i = 0; i < points.length; i++) {
                GL11.glNormal3f(points[i].x, points[i].y, points[i].z);
                GL11.glVertex3f(points[i].x, points[i].y, points[i].z);
            }
            GL11.glEnd();
        }
        GL11.glPopMatrix();
    }
}
