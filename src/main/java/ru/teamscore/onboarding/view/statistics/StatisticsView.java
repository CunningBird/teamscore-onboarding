package ru.teamscore.onboarding.view.statistics;


import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.teamscore.onboarding.entity.Contract;
import ru.teamscore.onboarding.view.main.MainView;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Route(value = "StatisticsView", layout = MainView.class)
@ViewController("StatisticsView")
@ViewDescriptor("statistics-view.xml")
public class StatisticsView extends StandardView {

    @ViewComponent
    protected JmixTabSheet tabSheet;

    @Autowired
    private DataManager dataManager;

    @Subscribe
    protected void onInit(InitEvent event) {
        Component lineChart = createLineChart();
        tabSheet.add("Contracts cost diagram", lineChart);
    }

    protected Component createLineChart() {
        SOChart rootChart = new SOChart();
        rootChart.setWidthFull();
        rootChart.setMinHeight("30em");

        Data xValues = new Data();
        Data yValues = new Data();
        AtomicInteger x = new AtomicInteger();

        dataManager.load(Contract.class).all().list().forEach(contract -> {
            xValues.add(x.getAndIncrement());
            yValues.add(contract.getAmount());
        });

        xValues.setName("Contract number");
        yValues.setName("Contract cost");

        LineChart lineChart = new LineChart(xValues, yValues);
        lineChart.setName("Count of contracts");

        XAxis xAxis = new XAxis(DataType.NUMBER);
        YAxis yAxis = new YAxis(DataType.NUMBER);

        RectangularCoordinate rc = new RectangularCoordinate(xAxis, yAxis);
        lineChart.plotOn(rc);

        rootChart.add(lineChart, new Title("Contracts cost statistics"));

        return rootChart;
    }
}