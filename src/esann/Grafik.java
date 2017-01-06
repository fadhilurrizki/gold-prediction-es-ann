/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esann;
import java.awt.Color;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
/**
 *
 * @author Fadhil
 */
public class Grafik  extends ApplicationFrame {

    public Grafik(String title, double[] data, int count) {
        super(title);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel(data, count);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
    }
    private static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Grafik Fitness Terhadap Generasi", // Judul
                "Generasi", // x
                "Fitness", // y
                dataset, // data
                true, // legend
                true, // gtooltips
                false // URLs
        );
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(0.1, 0.1, 0.1, 0.1));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }
        NumberAxis axis = new NumberAxis("Generasi");
        axis.setAutoRangeIncludesZero(true);
        plot.setDomainAxis(axis);
        return chart;
    }
    private static XYDataset createDataset(double[] data) {
        XYSeries s1 = new XYSeries("Fitness"); 
        for(int i =0; i<data.length; i++) {
            s1.add(i+1,data[i]);
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);
        return dataset;
    }
    public static JPanel createDemoPanel(double[] data,int count) {
        JFreeChart chart = createChart(createDataset(data));
        ChartFrame frame = new ChartFrame(String.valueOf(count)+".png", chart);
        try {
            final ChartRenderingInfo info = new ChartRenderingInfo();
            final File f = new File(String.valueOf(count)+".png");
            ChartUtilities.saveChartAsPNG(f, chart, 800, 600);
            
        } catch(Exception e) {
        
        }
        return new ChartPanel(chart);
    }
    
}
