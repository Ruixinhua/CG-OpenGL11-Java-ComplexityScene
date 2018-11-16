package objects3D;

import org.lwjgl.opengl.GL11;

import GraphicsObjects.Point4f;

public class Sphere {

	
	public Sphere() {

	}
	
	//get the point in specific phi and theta with radius
	public Point4f getPoint(float radius,float phi, float theta) {
		float r = (float)(radius * Math.cos(phi));
		float x = (float)(r * Math.cos(theta));
		float y = (float)(r * Math.sin(theta));
		float z = (float)(radius * Math.sin(phi));
		return new Point4f(x,y,z,0f);
	}
	// Implement using notes and examine Tetrahedron to aid in the coding  look at lecture  7 , 7b and 8 
	// 7b should be your primary source, we will cover more about circles in later lectures to understand why the code works 
	public void DrawSphere(float radius,float nSlices,float nSegments) {
		float inctheta = (float)(2.0f*Math.PI)/(nSlices);
		float incphi = (float)Math.PI/(nSegments);
		GL11.glBegin(GL11.GL_QUADS);
		for(float theta=-(float)Math.PI; theta < Math.PI; theta+=inctheta) {
			for(float phi=-(float)(Math.PI/2.0f); phi<(Math.PI/2.0f); phi+=incphi) {
				//the points that consist of a quad
				Point4f points[] = {getPoint(radius, phi, theta),getPoint(radius, phi+incphi, theta),
						getPoint(radius, phi+incphi, theta+inctheta),getPoint(radius, phi, theta+inctheta)};
				//draw a quad by using these points
				for (int i = 0; i < points.length; i++) {
					GL11.glNormal3f(points[i].x, points[i].y, points[i].z);
					GL11.glVertex3f(points[i].x, points[i].y, points[i].z);
				}
			}
		}
		GL11.glEnd();
	}
}

 
 