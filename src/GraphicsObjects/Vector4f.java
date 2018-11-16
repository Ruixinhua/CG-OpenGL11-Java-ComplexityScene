package GraphicsObjects;



public class Vector4f {

	public float x=0;
	public float y=0;
	public float z=0;
	public float a=0;
	
	public Vector4f() 
	{  
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
		a = 0.0f;
	}
	 
	public Vector4f(float x, float y, float z,float a) 
	{ 
		this.x = x;
		this.y = y;
		this.z = z;
		this.a = a;
	}
	
	 //implement Vector plus a Vector  and comment what the method does  
	public Vector4f PlusVector(Vector4f Additonal) 
	{ 
		//return a vector after plus a vector
		return new Vector4f(this.x+Additonal.x,this.y+Additonal.y,this.z+Additonal.z,this.a+Additonal.a);
	} 
	
	 //implement Vector minus a Vector  and comment what the method does  
	public Vector4f MinusVector(Vector4f Minus) 
	{ 
		//return a vector after minus a vector
		return new Vector4f(this.x-Minus.x,this.y-Minus.y,this.z-Minus.z,this.a-Minus.a);
	}
	
	//implement Vector plus a Point and comment what the method does  
	public Point4f PlusPoint(Point4f Additonal) 
	{ 
		//return a point after plus a point
		return new Point4f(this.x+Additonal.x,this.y+Additonal.y,this.z+Additonal.z,this.a+Additonal.a);
	} 
	//Do not implement Vector minus a Point as it is undefined 
	
	//Implement a Vector * Scalar  and comment what the method does    ( we wont create Scalar * Vector due to expediency ) 
	public Vector4f byScalar(float scale )
	{
		//return a vector after multiply a scale
		return new Vector4f(this.x*scale,this.y*scale,this.z*scale,this.a*scale);
	}
	
	//implement returning the negative of a Vector  and comment what the method does  
	public Vector4f NegateVector()
	{
		//return the negative vector of this vector
		return new Vector4f(-this.x,-this.y,-this.z,-this.a);
	}
	
	//implement getting the length of a Vector    and comment what the method does
	public float length()
	{
		//return length of the vector
		return (float) Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);
	}
	
	//implement getting the Normal  of a Vector and comment what the method does
	public Vector4f Normal()
	{
		float l = length();
		//return the normal of vector which divided by its length.
		return new Vector4f(this.x/l, this.y/l, this.z/l, this.a/l);
	} 
	
	//implement getting the dot product of Vector.Vector  

	public float dot(Vector4f v)
	{ 
		return ( this.x*v.x + this.y*v.y + this.z*v.z+ this.a*v.a);
	}
	
	// Implemented this for you to avoid confusion 
	// as we will not normally  be using 4 float vector  
	public Vector4f cross(Vector4f v)  
	{ 
    float u0 = (this.y*v.z - z*v.y);
    float u1 = (z*v.x - x*v.z);
    float u2 = (x*v.y - y*v.x);
    float u4 = 0; //ignoring this for now  
    return new Vector4f(u0,u1,u2,u4);
	}
 
}
	 
	   

/*

										MMMM                                        
										MMMMMM                                      
 										MM MMMM                                    
 										MMI  MMMM                                  
 										MMM    MMMM                                
 										MMM      MMMM                              
  										MM        MMMMM                           
  										MMM         MMMMM                         
  										MMM           OMMMM                       
   										MM             .MMMM                     
MMMMMMMMMMMMMMM                        MMM              .MMMM                   
MM   IMMMMMMMMMMMMMMMMMMMMMMMM         MMM                 MMMM                 
MM                  ~MMMMMMMMMMMMMMMMMMMMM                   MMMM               
MM                                  OMMMMM                     MMMMM            
MM                                                               MMMMM          
MM                                                                 MMMMM        
MM                                                                   ~MMMM      
MM                                                                     =MMMM    
MM                                                                        MMMM  
MM                                4 D                                      MMMMMM 
MM                                                                     MMMMMMMM 
MM                                                                  :MMMMMMMM   
MM                                                                MMMMMMMMM     
MM                                                              MMMMMMMMM       
MM                             ,MMMMMMMMMM                    MMMMMMMMM         
MM              IMMMMMMMMMMMMMMMMMMMMMMMMM                  MMMMMMMM            
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM               ZMMMMMMMM              
MMMMMMMMMMMMMMMMMMMMMMMMMMMMM          MM$             MMMMMMMMM                
MMMMMMMMMMMMMM                       MMM            MMMMMMMMM                  
  									MMM          MMMMMMMM                     
  									MM~       IMMMMMMMM                       
  									MM      DMMMMMMMM                         
 								MMM    MMMMMMMMM                           
 								MMD  MMMMMMMM                              
								MMM MMMMMMMM                                
								MMMMMMMMMM                                  
								MMMMMMMM                                    
  								MMMM                                      
  								MM                                        
                             GlassGiant.com */