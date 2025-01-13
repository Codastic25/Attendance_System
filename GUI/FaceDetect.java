package GUI;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;


import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.MatOfRect;

public class FaceDetect {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.load("/home/cytech/opencv/java/lib/libopencv_java4100.so");

        String faceCascadePath = "/home/cytech/id3/java/projets/attendance_system/attendance_projet/src/haarcascade_frontalface_default.xml";
        CascadeClassifier faceCascade = new CascadeClassifier(faceCascadePath);

        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Impossible d'ouvrir la caméra");
            return;
        }

        Mat frame = new Mat();

        while (true) {
            camera.read(frame);
            if (frame.empty()) {
                System.out.println("La capture d'image a échoué");
                break;
            }

            Mat grayFrame = new Mat();
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

            MatOfRect faces = new MatOfRect();
            faceCascade.detectMultiScale(grayFrame, faces);

            for (Rect face : faces.toArray()) {
                Imgproc.rectangle(frame, face.tl(), face.br(), new Scalar(0, 0, 255), 2);
            }

            HighGui.imshow("Face Recognition", frame);
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }

        camera.release();
        HighGui.destroyAllWindows();
    }
}


/*
Workflow typique sans photos :

    Enregistrement d'un employé :
        Prendre une photo initiale de l'employé.
        Générer son encodage facial à partir de cette photo.
        Stocker l'encodage dans une base de données avec le nom de l'employé.

    Reconnaissance faciale :
        Capturer une nouvelle image ou vidéo.
        Détecter les visages et générer leurs encodages.
        Comparer ces encodages avec ceux stockés dans la base de données.
        Identifier l'employé si la distance est inférieure au seuil (par exemple, 0.6).
 */