package inputOutput;

import com.google.gson.Gson;
import controller.AutoTrackController;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransformationMatrix {
    public float[][] rotation3d = new float[][]{{1.0f,0.0f,0.0f},
            {0.0f,1.0f,0.0f},
            {0.0f, 0.0f, 1.0f}};
    public float[] translationVector3d = new float[]{0, 0, 0};
    public float[] scaleVector3d = new float[]{1, 1, 1};
    public int[][] flip2d = new int[][]{{1, 0},{0, 1}};

    public float[][] imagePoints;
    public float[][] trackingPoints;

    public int ignoreDimension = 2;

    public static TransformationMatrix loadFromJSON(String file) throws FileNotFoundException {
        return new Gson().fromJson(new FileReader(file), TransformationMatrix.class);
    }

    public void saveToJSON(File file) throws IOException {
        try (FileWriter fw = new FileWriter(file)) {
            new Gson().toJson(this, fw);
        }
    }

    public MatOfPoint2f getImagePoints(){
        if(imagePoints == null){return new MatOfPoint2f();}
        var array = new Point[4];
        for(int i = 0;i<imagePoints.length;i++){
            array[i] = new Point(imagePoints[i][0], imagePoints[i][1]);
        }
        return new MatOfPoint2f(array);
    }

    public MatOfPoint2f getTrackingPoints(){
        if(trackingPoints == null){return new MatOfPoint2f();}
        var array = new Point[4];
        for(int i = 0;i<trackingPoints.length;i++){
            array[i] = new Point(trackingPoints[i][0]+ AutoTrackController.TRACKING_SHIFT, trackingPoints[i][1]+ AutoTrackController.TRACKING_SHIFT);
        }
        return new MatOfPoint2f(array);
    }

    public Mat getRotationMat(){
        var mat = Mat.eye(2,3, CvType.CV_64F);
        int row = 0;
        for(int i = 0; i < rotation3d.length; i++){
            int col = 0;
            if(i == ignoreDimension){
                continue;
            }
            for(int j = 0; j < rotation3d[i].length; j++){
                if(j == ignoreDimension){
                    continue;
                }
                mat.put(row, col, rotation3d[i][j]);
                col++;
            }
            row++;
        }
        return mat;
    }

    public Mat getTranslationMat(){
        var mat = Mat.eye(2, 3, CvType.CV_64F);
        int row = 0;
        for(int i = 0;i < translationVector3d.length; i++){
            if(i == ignoreDimension){
                continue;
            }
            mat.put(row,2,translationVector3d[i]);
            row++;
        }
        return mat;
    }

    public Mat getScaleMat(){
        var mat = new Mat(2,3, CvType.CV_64F);
        for(int i = 0;i<mat.rows();i++){
            for(int j = 0;j<mat.cols();j++){
                float value = 0f;
                if(i == j){
                    value = scaleVector3d[i]*flip2d[i][j];
                }
                mat.put(i,j,value);
            }
        }
        return mat;
    }
}
