package objects3D;

import org.lwjgl.opengl.GL11;
import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;
import java.math.*;

public class Cylinder {

	
	public Cylinder() { 
	}
	
	// remember to use Math.PI instead PI 
	// Implement using notes and examine Tetrahedron to aid in the coding  look at lecture  7 , 7b and 8 
	public void DrawCylinder(float radius, float height, int nSegments )
	{
		GL11.glBegin(GL11.GL_QUADS);
		for (float i = 0f; i < nSegments; i += 1.0)
		{ /* a loop around circumference of a tube */
			double angle = Math.PI * i * 2f / nSegments ;
			double nextAngle = Math.PI * (i + 1.0) * 2f / nSegments;

			/* compute sin & cosine */
			float x1 = (float)Math.sin(angle)*radius, y1 =(float) Math.cos(angle)*radius;
			float x2 = (float)Math.sin(nextAngle)*radius, y2 = (float)Math.cos(nextAngle)*radius;
			//this is the points that draw the round surface
			Point4f points[] = {new Point4f(x1, y1, 0, 0),new Point4f(x1, y1, height, 0),
					new Point4f(x2, y2, height, 0), new Point4f(x2, y2, 0, 0)};

			//draw the cylinder
			for (int j = 0; j < points.length; j++) {
				GL11.glNormal3f(points[j].x, points[j].y, 0);
				GL11.glVertex3f(points[j].x, points[j].y, points[j].z);
			}

		} /* a loop around circumference of a tube */
		GL11.glEnd();
	}
}

