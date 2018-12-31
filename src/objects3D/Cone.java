package objects3D;

import org.lwjgl.opengl.GL11;
import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;
import java.math.*;

public class Cone {


    public Cone() {
    }

    // remember to use Math.PI instead PI
    // Implement using notes and examine Tetrahedron to aid in the coding  look at lecture  7 , 7b and 8
    public void DrawCone(float radius, float height, int nSegments, float alpha)
    {
        GL11.glBegin(GL11.GL_TRIANGLES);
        for (float i = 0f; i < nSegments; i += 1.0)
        { /* a loop around circumference of a tube */
            double angle = Math.PI * i * 2f / nSegments ;
            double nextAngle = Math.PI * (i + 1.0) * 2f / nSegments;

            /* compute sin & cosine */
            float x1 = (float)Math.sin(angle)*radius, y1 =(float) Math.cos(angle)*radius;
            float x2 = (float)Math.sin(nextAngle)*radius, y2 = (float)Math.cos(nextAngle)*radius;
            //this is the vertices that draw the cylinder side
            Point4f vertices[] = new Point4f[]{
                    new Point4f(x1, y1, 0, alpha),new Point4f(x2, y2, 0, alpha),new Point4f(0, 0, height, alpha)
            };
            Vector4f v = vertices[1].MinusPoint(vertices[0]);
            Vector4f w = vertices[2].MinusPoint(vertices[0]);
            Vector4f normal = v.cross(w).Normal();
            GL11.glNormal3f(normal.x, normal.y, normal.z);
            for (int j = 0; j < 3; j++) {
                GL11.glNormal3f(vertices[j].x, vertices[j].y, vertices[j].z);
                GL11.glVertex3f(vertices[j].x, vertices[j].y, vertices[j].z);
            }

        } /* a loop around circumference of a tube */
        GL11.glEnd();
    }
}

