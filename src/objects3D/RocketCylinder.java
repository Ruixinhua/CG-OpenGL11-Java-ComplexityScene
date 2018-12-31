package objects3D;

import GraphicsObjects.Point4f;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class RocketCylinder {
    public void DrawTexCylinder(float radius, float height, int nSegments, Texture texture)
    {
        Color.white.bind();
        texture.bind();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glBegin(GL11.GL_QUADS);

        for (float i = 0f; i < nSegments; i += 1.0)
        { /* a loop around circumference of a tube */
            double angle = Math.PI * i * 2f / nSegments ;
            double nextAngle = Math.PI * (i + 1.0) * 2f / nSegments;

            /* compute sin & cosine */
            float x1 = (float)Math.sin(angle)*radius, y1 =(float) Math.cos(angle)*radius;
            float x2 = (float)Math.sin(nextAngle)*radius, y2 = (float)Math.cos(nextAngle)*radius;
            //this is the vertices that draw the cylinder side
            //this is the points that draw the round surface
            Point4f points[] = {new Point4f(x1, 0, y1, 0),new Point4f(x1, height,y1,  0),
                    new Point4f(x2,height, y2,  0), new Point4f(x2, 0, y2, 0)};
            Point4f textures[] = {new Point4f(i/nSegments,0,0,0),new Point4f(i/nSegments,height,0,0),
                    new Point4f((i+1)/nSegments,height,0,0),new Point4f((i+1)/nSegments,0,0,0)};
            //draw the cylinder
            for (int j = 0; j < points.length; j++) {
                GL11.glTexCoord2f(textures[j].x,textures[j].y);
                GL11.glNormal3f(points[j].x, 0, points[j].y);
                GL11.glVertex3f(points[j].x, points[j].y, points[j].z);
            }


        } /* a loop around circumference of a tube */
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public void DrawCylinder(float radius, float height, int nSegments)
    {
        GL11.glBegin(GL11.GL_QUADS);

        for (float i = 0f; i < nSegments; i += 1.0)
        { /* a loop around circumference of a tube */
            double angle = Math.PI * i * 2f / nSegments ;
            double nextAngle = Math.PI * (i + 1.0) * 2f / nSegments;

            /* compute sin & cosine */
            float x1 = (float)Math.sin(angle)*radius, y1 =(float) Math.cos(angle)*radius;
            float x2 = (float)Math.sin(nextAngle)*radius, y2 = (float)Math.cos(nextAngle)*radius;
            //this is the vertices that draw the cylinder side
            //this is the points that draw the round surface
            Point4f points[] = {new Point4f(x1, 0, y1, 0),new Point4f(x1, height,y1,  0),
                    new Point4f(x2,height, y2,  0), new Point4f(x2, 0, y2, 0)};
            //draw the cylinder
            for (int j = 0; j < points.length; j++) {
                GL11.glNormal3f(points[j].x, 0, points[j].y);
                GL11.glVertex3f(points[j].x, points[j].y, points[j].z);
            }


        } /* a loop around circumference of a tube */
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }
}
