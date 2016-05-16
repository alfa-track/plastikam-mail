package ru.plastikam.mail.ui;

import org.primefaces.model.chart.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@Scope(scopeName = "view")
public class Charts implements Serializable {

    private BarChartModel barModel;

    {
        createBarModels();
    }

    public BarChartModel getBarModel() {
        return barModel;
    }


    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries cs1 = new ChartSeries();
        cs1.setLabel("Москва");
        cs1.set("2016-05-01", 520);
        cs1.set("2016-05-02", 500);
        cs1.set("2016-05-03", 544);
        cs1.set("2016-05-04", 650);
        cs1.set("2016-05-05", 505);

        ChartSeries cs2 = new ChartSeries();
        cs2.setLabel("Петербург");
        cs2.set("2016-05-01", 400);
        cs2.set("2016-05-02", 460);
        cs2.set("2016-05-03", 110);
        cs2.set("2016-05-04", 135);
        cs2.set("2016-05-05", 120);

        ChartSeries cs3 = new ChartSeries();
        cs3.setLabel("Нижний Новгород");
        cs3.set("2016-05-01", 52);
        cs3.set("2016-05-02", 60);
        cs3.set("2016-05-03", 110);
        cs3.set("2016-05-04", 135);
        cs3.set("2016-05-05", 120);

        ChartSeries cs4 = new ChartSeries();
        cs4.setLabel("Челябинск");
        cs4.set("2016-05-01", 52);
        cs4.set("2016-05-02", 60);
        cs4.set("2016-05-03", 110);
        cs4.set("2016-05-04", 35);
        cs4.set("2016-05-05", 120);

        ChartSeries cs5 = new ChartSeries();
        cs5.setLabel("Казань");
        cs5.set("2016-05-01", 52);
        cs5.set("2016-05-02", 60);
        cs5.set("2016-05-03", 40);
        cs5.set("2016-05-04", 65);
        cs5.set("2016-05-05", 120);

        ChartSeries cs6 = new ChartSeries();
        cs6.setLabel("Воронеж");
        cs6.set("2016-05-01", 52);
        cs6.set("2016-05-02", 60);
        cs6.set("2016-05-03", 110);
        cs6.set("2016-05-04", 135);
        cs6.set("2016-05-05", 60);

        model.addSeries(cs1);
        model.addSeries(cs2);
        model.addSeries(cs3);
        model.addSeries(cs4);
        model.addSeries(cs5);
        model.addSeries(cs6);

        return model;
    }

    private void createBarModels() {
        createBarModel();
    }

    private void createBarModel() {
        barModel = initBarModel();

        barModel.setTitle("Количество писем от регионов по дням");
        barModel.setLegendPosition("ne");

        barModel.setStacked(true);

        DateAxis axis = new DateAxis("");
        axis.setTickAngle(-50);
        //axis.setMax("2014-02-01");
        axis.setTickFormat("%#d %b %y");
        axis.setTickCount(4);
        barModel.getAxes().put(AxisType.X, axis);

        Axis yAxis = barModel.getAxis(AxisType.Y);
//        yAxis.setLabel("Births");
//        yAxis.setMin(0);
//        yAxis.setMax(200);
    }
}
