package OpenCVFunktionTest;
import OpenCVFunktionTest.HoughCircles;


public class testHough {

	public static void main(String[] args){
		
		HoughCircles test = new HoughCircles();
		
		
		try {
			test.CaptureWebcam();
		} catch(Exception e){
			System.out.println("der er en fejl");
		}
		
		
		
	}
	
	
}
