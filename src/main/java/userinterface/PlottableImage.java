package userinterface;

import javafx.beans.NamedArg;
import javafx.event.EventHandler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlottableImage extends ScatterChart<Number, Number> {

    private final ImageView imageView = new ImageView();
    private final List<PlottableImageCallback> clickHandler = new ArrayList<>();

    public PlottableImage(@NamedArg("xAxis") NumberAxis xAxis, @NamedArg("yAxis") NumberAxis yAxis) {
        super(xAxis, yAxis);
        getPlotChildren().add(imageView);
//        imageView.setOnMouseClicked(mouseEvent -> System.out.printf(Locale.ENGLISH,
//                "(%.2f, %.2f)%n",
//                xAxis.getValueForDisplay(mouseEvent.getX()),
//                imageView.getImage().getHeight()-yAxis.getValueForDisplay(mouseEvent.getY()).intValue()
//        ));
        imageView.setOnMouseClicked(mouseEvent -> clickHandler.forEach(handler -> handler.onMouseClicked(xAxis.getValueForDisplay(mouseEvent.getX()).doubleValue(),yAxis.getValueForDisplay(mouseEvent.getY()).doubleValue())));
    }

    public void setImage(Image image) {
        imageView.setImage(image);

//        ((NumberAxis) getXAxis()).setUpperBound(image.getWidth()/2);
//        ((NumberAxis) getXAxis()).setLowerBound(-image.getWidth()/2);
//        ((NumberAxis) getYAxis()).setUpperBound(image.getHeight()/2);
//        ((NumberAxis) getYAxis()).setLowerBound(-image.getHeight()/2);
//
        ((NumberAxis) getXAxis()).setUpperBound(image.getWidth());
        ((NumberAxis) getXAxis()).setLowerBound(0);
        ((NumberAxis) getYAxis()).setUpperBound(image.getHeight());
        ((NumberAxis) getYAxis()).setLowerBound(0);

        // TODO: This might conflict with the Transformation Matrix
//        prefWidthProperty().bind(image.widthProperty());
//        prefHeightProperty().bind(image.heightProperty());

        maxWidthProperty().bind(image.widthProperty());
        maxHeightProperty().bind(image.heightProperty());

        updateAxisRange();
    }

    public void registerImageClickedHandler(PlottableImageCallback handler){
        clickHandler.add(handler);
    }
}
