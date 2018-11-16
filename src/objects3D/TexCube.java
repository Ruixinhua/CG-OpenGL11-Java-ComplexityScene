package objects3D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;

public class TexCube {

	public TexCube() {

	}

	// Implement using notes and looking at TexSphere
	public void DrawTexCube(Texture texture) {
		
		Point4f vertices[] = { new Point4f(-1.0f, -1.0f, -1.0f, 0.0f), new Point4f(-1.0f, -1.0f, 1.0f, 0.0f),
				new Point4f(-1.0f, 1.0f, -1.0f, 0.0f), new Point4f(-1.0f, 1.0f, 1.0f, 0.0f),
				new Point4f(1.0f, -1.0f, -1.0f, 0.0f), new Point4f(1.0f, -1.0f, 1.0f, 0.0f),
				new Point4f(1.0f, 1.0f, -1.0f, 0.0f), new Point4f(1.0f, 1.0f, 1.0f, 0.0f) };

		int faces[][] = { { 0, 4, 5, 1 }, { 0, 2, 6, 4 }, { 0, 1, 3, 2 }, { 4, 6, 7, 5 }, { 1, 5, 7, 3 },
				{ 2, 3, 7, 6 } };
		//bind texture
		Color.white.bind();
		texture.bind();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBegin(GL11.GL_QUADS);
		for (int face = 0; face < 6; face++) { // per face
			Vector4f v = vertices[faces[face][1]].MinusPoint(vertices[faces[face][0]]);
			Vector4f w = vertices[faces[face][3]].MinusPoint(vertices[faces[face][0]]);
			Vector4f normal = v.cross(w).Normal();
			GL11.glNormal3f(normal.x, normal.y, normal.z);
			//convert to the texture coordinates
			GL11.glTexCoord2f(0f, 0f);// corner left
			GL11.glVertex3f(vertices[faces[face][0]].x, vertices[faces[face][0]].y, vertices[faces[face][0]].z);
			GL11.glTexCoord2f(1f, 0f);// corner right 
			GL11.glVertex3f(vertices[faces[face][1]].x, vertices[faces[face][1]].y, vertices[faces[face][1]].z);
			GL11.glTexCoord2f(1f, 1f);// top right
			GL11.glVertex3f(vertices[faces[face][2]].x, vertices[faces[face][2]].y, vertices[faces[face][2]].z);
			GL11.glTexCoord2f(0f, 1f);// top left
			GL11.glVertex3f(vertices[faces[face][3]].x, vertices[faces[face][3]].y, vertices[faces[face][3]].z);
		} // per face

		GL11.glEnd();
		//disable texture, so it can work correct
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

}

/*
 * 
 * 
 * }
 * 
 */
