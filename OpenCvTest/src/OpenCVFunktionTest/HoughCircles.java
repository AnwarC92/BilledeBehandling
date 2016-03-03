package OpenCVFunktionTest;

import javax.print.attribute.Size2DSyntax;
import javax.swing.JFrame;

import OpenCVFunktionTest.PanelVid;
import org.opencv.core.Core;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class HoughCircles {
	private JFrame frameCamera;
	private PanelVid panelCamera = new PanelVid();
	private JFrame cameraFilter;
	private PanelVid panelFilter = new PanelVid();

	public void CaptureWebcam() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		VideoCapture frame = new VideoCapture(0);
		Mat image = new Mat();
		Mat imageFilter = new Mat();
		Size fff = new Size();
		frameCamera = createFrame("Kamera", panelCamera);

		cameraFilter = createFrame("filtreret Kamera", panelFilter);

		try {
			frame.read(image);
		} catch (Exception e) {
			throw new Exception("Kamera virker ikke.");
		}

		setFramesSizes(image);

		Mat fundetCirkler = new Mat(); // matrix til at indsætte de cirkler der bliver fundet.
		
		
		if (frame.isOpened()) {
			/*
			 * Parameter definitioner til Hough alghoritmen
			 * 
			 */
			fff.height = 9;
			fff.width = 9;
			int radius;
			double j1 = 1;
			double j2 = 20;
			double j3 = 10;
			double j4 = 20;

			Point centrum;
			while (true) {
				frame.read(image);
				// vis kamereat fungere, hopper den ind i denne funktion.
				if (!image.empty()) {
					Imgproc.cvtColor(image, imageFilter, Imgproc.COLOR_BGR2GRAY); // gør billedet til grådt og indsætter det i den filtreret JFrame
					Imgproc.GaussianBlur(imageFilter, imageFilter, fff, 9, 9); // et filter det gør det mere "blurred" det hjælper åbenbart programmet med at finde boldene hurtigere og mere præcist
					
					Imgproc.HoughCircles(imageFilter, fundetCirkler, Imgproc.CV_HOUGH_GRADIENT, j1, j2, j3, j4, 22, 25); // alghoritmen der finder cirklerne, de sidste 2 parameter er min radius størrelse den skal finde og max.

					/*
					 * Tegner cirkel rundt om de bolde den finder igennem videon. den udregner også centrum og radius
					 * 
					 */

					for (int i = 0; i < fundetCirkler.cols(); i++) { // tjekker om den har fundet nogle cirkler og hvor mange, og går igennem alle de cirkler den finder.
						double vfundetCirkler[] = fundetCirkler.get(0, i);

						if (vfundetCirkler == null) 
							// vis den ikke finder nogle cirkler, hopper den ud af for loop.
							break;
						centrum = new Point(Math.round(vfundetCirkler[0]), Math.round(vfundetCirkler[1])); // udregner centrum af fra de punkter den har fundet på billedet
						radius = (int) Math.round(vfundetCirkler[2]); // udregner radius.
						Core.circle(image, centrum, radius, new Scalar(255, 255, 255), 1); // tegner cirklen 
						System.out.println(
								centrum + " og radius er : " + radius + " omkredsen er : " + radius * radius * Math.PI); // bare test, skal fjernes.
					}
					updateFrames(image, imageFilter); 

				} else {
					throw new Exception("Kunne ikke læse kamer"); // smider exceptions	
				}

			}

		} else {
			throw new Exception("Kunne ikke få billeder fra kamera."); // smider exceptions																		// exception																	// vis																		// kamera																		// ikke																		// virker
		}

	}

	/*
	 * Updater frames, livefeed.
	 */

	private void updateFrames(Mat image, Mat thresholdedImage) {
		setPanelsImages(image, thresholdedImage);
		repaintFrames();
	}

	/*
	 * Tilføjer nyt billede
	 */

	private void repaintFrames() {
		frameCamera.repaint();
		cameraFilter.repaint();

	}

	/*
	 * >størrelse af selve video frames.
	 * 
	 */

	private void setFramesSizes(Mat image) {
		frameCamera.setSize(image.width() + 20, image.height() + 60);
		cameraFilter.setSize(image.width() + 20, image.height() + 60);

	}

	/*
	 * bygger JFrame til livefeed. både til almendlig og til filtreret.
	 * 
	 */

	private JFrame createFrame(String Navn, PanelVid cameraFilter2) {
		JFrame frame = new JFrame(Navn);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 480);
		frame.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		frame.setContentPane(cameraFilter2);
		frame.setVisible(true);

		return frame;
	}

	/*
	 * Tilslutter selve billede med JFrame.
	 * 
	 */
	private void setPanelsImages(Mat image, Mat imageFilter) {
		panelCamera.setImageWithMat(image);
		panelFilter.setImageWithMat(imageFilter);
	}

}
