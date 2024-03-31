package tasks;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Main  extends JFrame {
    private final JTextField textFieldA;
    private JTextField textFieldFun;
    private final JTextField textFieldStart;
    private final JTextField textFieldStop;
    private final JTextField textFieldStep;
    private final JTextField derivativeLabel;
    private FFunction function;
    private XYSeries series;
    private XYSeries derivative;
    private double start = 9;
    private double stop = -9;
    private double step = 0.01;
    private double a = 1;
    private Thread calculation;
    private boolean isCalculation = false;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main frame = new Main();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {

        setResizable(false);
        setTitle("fFreeChart Test Plot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panelButtons = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panelButtons.getLayout();
        flowLayout.setHgap(15);

        contentPane.add(panelButtons, BorderLayout.SOUTH);
        JButton btnNewButtonPlot = new JButton("Plot");

        btnNewButtonPlot.addActionListener(e -> {
            isCalculation = false;
            if(!calculation.isAlive()) {
                initVal();

                function.setA(a);
                function.setFun(textFieldFun.getText());

                calculationPlot();
            }
        });

        panelButtons.add(btnNewButtonPlot);

        JButton btnNewButtonExit = new JButton("Exit");
        btnNewButtonExit.addActionListener(e -> System.exit(0));

        panelButtons.add(btnNewButtonExit);

        panelButtons.add(new JLabel("Start"));
        panelButtons.add((textFieldStart = new JTextField("-9.0",5)));
        panelButtons.add(new JLabel("Stop"));
        panelButtons.add((textFieldStop = new JTextField("9.0",5)));
        panelButtons.add(new JLabel("Step"));
        panelButtons.add((textFieldStep = new JTextField("0.01",5)));

        JPanel panelData = new JPanel();
        contentPane.add(panelData, BorderLayout.NORTH);
        JLabel lblNewLabel = new JLabel("a:");
        panelData.add(lblNewLabel);

        textFieldA = new JTextField("1.0");
        textFieldFun = new JTextField("cos(x)");

        panelData.add(textFieldA);

        JLabel lblNewLabelFun = new JLabel("Fun:");
        panelData.add(lblNewLabelFun);
        panelData.add(textFieldFun);

        derivativeLabel = new JTextField(20);
        panelData.add(new JLabel("Del:"));
        panelData.add(derivativeLabel);

        JButton interpolatorBt = new JButton("Interpolator");

        interpolatorBt.addActionListener(e ->{

            FileListInterpolation fun = new FileListInterpolation();
//            FileTreeMapInterpolation fun = new FileTreeMapInterpolation();
//            FileTreeSetInterpolation fun = new FileTreeSetInterpolation();
            fun.clear();
            try {
                fun.readFromFile("data.dat");
            }
            catch (IOException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
            fun.sort();
            interpolation(fun);
        });

        panelButtons.add(interpolatorBt);

        textFieldA.setColumns(10);
        textFieldFun.setColumns(10);

        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        contentPane.add(chartPanel, BorderLayout.CENTER);
    }

    private void interpolation(Interpolator fun) {

        isCalculation = false;
        if(!calculation.isAlive()) {
            initVal();
            calculation = new Thread(() -> {
                isCalculation = true;
                series.clear();
                derivative.clear();
                for (double x = start; x < stop && isCalculation; x += step) {
                    series.add(x, fun.evalf(x));
                }
                isCalculation = false;
            });
            calculation.start();
        }
    }

    private JFreeChart createChart() {
        FileListInterpolation fun = new FileListInterpolation();

        double x;
        for (int i = -9; i < 10; i+=1) {
            x = Math.random()*20 - 10;
            System.out.println(Math.sin(x));
            fun.addPoint(new Point2D(x,Math.sin(x)));
        }

        try {
            fun.writeToFile("data.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }

        series = new XYSeries("Function");
        derivative = new XYSeries("Derivative");

        initVal();

        function = new FFunction(textFieldFun.getText(),a);

        calculationPlot();

        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series);
        dataset.addSeries(derivative);

        return ChartFactory.createXYLineChart(null,
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private void initVal() {
        try {
            start = Double.parseDouble(textFieldStart.getText());
            stop = Double.parseDouble(textFieldStop.getText());
            step = Double.parseDouble(textFieldStep.getText());
            a = Double.parseDouble(textFieldA.getText());
        } catch (NumberFormatException ignored) {
        }
    }

    private void calculationPlot() {
        derivativeLabel.setText(function.getDer());
        calculation = new Thread(() -> {
            isCalculation = true;
            series.clear();
            derivative.clear();
            for (double x = start; x < stop && isCalculation; x += step) {
                derivative.add(x, function.derivative(x));
                series.add(x, function.evalf(x));
            }
            isCalculation = false;
        });
        calculation.start();
    }
}