package mm2python.UI;

// intelliJ libraries
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

// mm2python libraries
import mm2python.DataStructures.constants;
import mm2python.messenger.Py4J.Py4J;
import mm2python.mmDataHandler.ramDisk.ramDiskConstructor;
import mm2python.mmDataHandler.ramDisk.ramDiskFlush;
import mm2python.mmEventHandler.globalEvents;
import mm2python.UI.reporter;

// mm libraries
import org.micromanager.Studio;

// java libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class pythonBridgeUI_dialog extends JFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JButton create_python_bridge;
    private JButton shutdown_python_bridge;
    private JButton start_monitor_global_events;
    private JButton stop_monitor_global_events;
    private JTextArea UI_logger_textArea;
    private JRadioButton py4JRadioButton;
    private JRadioButton rayRadioButton;
    private JRadioButton kafkaRadioButton;
    private JRadioButton horovodRadioButton;
    private JRadioButton gRPCRadioButton;
    private JRadioButton openMQRadioButton;
    private JRadioButton arrowRadioButton;
    private JButton clear_ramdisk;
    private JButton create_ramdisk;
    private JTextField temp_file_path;
    private JButton destroy_ramdisk;
    private JScrollPane UI_logger;
    private JLabel selectMessengerInterfaceLabel;
    private JPanel tempfilePathLabel;

    private static Studio mm;
    private Py4J gate;
    private globalEvents gevents;
    private final mm2python.UI.reporter init_reports;
    private final ramDiskFlush flush;

    public pythonBridgeUI_dialog(Studio mm_) {
        // mm2python.UI components created in the static constructor below
        setContentPane(contentPane);
        create_python_bridge.addActionListener(e -> create_python_bridgeActionPerformed(e));
        shutdown_python_bridge.addActionListener(e -> shutdown_python_bridgeActionPerformed(e));
        start_monitor_global_events.addActionListener(e -> start_monitor_global_eventsActionPerformed(e));
        stop_monitor_global_events.addActionListener(e -> stop_monitor_global_eventsActionPerformed(e));
        create_ramdisk.addActionListener(e -> create_ramdiskActionPerformed(e));
        clear_ramdisk.addActionListener(e -> clear_ramdiskActionPerformed(e));
        destroy_ramdisk.addActionListener(e -> destroy_ramdiskActionPerformed(e));
        py4JRadioButton.addActionListener(e -> py4jRadioButtonActionPerformed(e));
        rayRadioButton.addActionListener(e -> rayRadioButtonActionPerformed(e));
        kafkaRadioButton.addActionListener(e -> kafkaRadioButtonActionPerformed(e));
        horovodRadioButton.addActionListener(e -> horovodRadioButtonActionPerformed(e));
        gRPCRadioButton.addActionListener(e -> gRPCRadioButtonActionPerformed(e));
        openMQRadioButton.addActionListener(e -> openMQRadioButtonActionPerformed(e));
        arrowRadioButton.addActionListener(e -> arrowRadioButtonActionPerformed(e));

        temp_file_path.addActionListener(e -> temp_file_pathActionPerformed(e));

        mm = mm_;
        init_reports = new reporter(UI_logger_textArea, mm);
        flush = new ramDiskFlush(mm);

        // initialize constants
        new constants(mm);
        if (py4JRadioButton.isSelected()) { constants.py4JRadioButton = true; }
        constants.RAMDiskName = temp_file_path.getText();;
        reporter.set_report_area(true, false, "mm2python.UI INITIALIZATION filename = " + constants.RAMDiskName);

    }

    private void temp_file_pathActionPerformed(ActionEvent evt){
        constants.RAMDiskName = temp_file_path.getText();
        reporter.set_report_area(false, false,"Temp file path changed to: "+constants.RAMDiskName);
    }

    private void create_python_bridgeActionPerformed(ActionEvent evt) {
        reporter.set_report_area("creating python bridge");
        gate = new Py4J(mm);
        gate.startConnection();
    }

    private void shutdown_python_bridgeActionPerformed(ActionEvent evt) {
        gate.stopConnection(ABORT);
    }

    private void start_monitor_global_eventsActionPerformed(ActionEvent evt) {
        reporter.set_report_area("monitoring global events");
        gevents = new globalEvents(mm, UI_logger_textArea);
        gevents.registerGlobalEvents();
    }

    private void stop_monitor_global_eventsActionPerformed(ActionEvent evt) {
        reporter.set_report_area("STOP monitoring global events, clearing data store references");
        constants.resetAll();
        gevents.unRegisterGlobalEvents();

    }

    private void create_ramdiskActionPerformed(ActionEvent evt) {
        new ramDiskConstructor(mm);
    }

    private void clear_ramdiskActionPerformed(ActionEvent evt) {
        flush.clearRamDiskContents();
    }

    private void destroy_ramdiskActionPerformed(ActionEvent evt) {
        //ToDO: write this
    }

    private void py4jRadioButtonActionPerformed(ActionEvent evt) {
        if (py4JRadioButton.isSelected()) {
            constants.py4JRadioButton = true;
        }
    }

    private void kafkaRadioButtonActionPerformed(ActionEvent evt) {
    }

    private void gRPCRadioButtonActionPerformed(ActionEvent evt) {
    }

    private void openMQRadioButtonActionPerformed(ActionEvent evt) {
    }

    private void arrowRadioButtonActionPerformed(ActionEvent evt) {
    }

    private void rayRadioButtonActionPerformed(ActionEvent evt) {
    }

    private void horovodRadioButtonActionPerformed(ActionEvent evt) {
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 2, new Insets(1, 1, 1, 1), -1, -1));
        tabbedPane1 = new JTabbedPane();
        contentPane.add(tabbedPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Main Pane", panel1);
        UI_logger = new JScrollPane();
        panel1.add(UI_logger, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        UI_logger_textArea = new JTextArea();
        UI_logger.setViewportView(UI_logger_textArea);
        create_python_bridge = new JButton();
        create_python_bridge.setText("Create Python Bridge");
        panel1.add(create_python_bridge, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shutdown_python_bridge = new JButton();
        shutdown_python_bridge.setText("Shutdown Python Bridge");
        panel1.add(shutdown_python_bridge, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        start_monitor_global_events = new JButton();
        start_monitor_global_events.setText("START monitor");
        panel1.add(start_monitor_global_events, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stop_monitor_global_events = new JButton();
        stop_monitor_global_events.setText("STOP monitor");
        panel1.add(stop_monitor_global_events, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(11, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Configuration", panel2);
        selectMessengerInterfaceLabel = new JLabel();
        selectMessengerInterfaceLabel.setText("Select Messenger Interface");
        panel2.add(selectMessengerInterfaceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        py4JRadioButton = new JRadioButton();
        py4JRadioButton.setSelected(true);
        py4JRadioButton.setText("Py4J");
        panel2.add(py4JRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        kafkaRadioButton = new JRadioButton();
        kafkaRadioButton.setEnabled(false);
        kafkaRadioButton.setText("Kafka");
        panel2.add(kafkaRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horovodRadioButton = new JRadioButton();
        horovodRadioButton.setEnabled(false);
        horovodRadioButton.setText("Horovod");
        panel2.add(horovodRadioButton, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openMQRadioButton = new JRadioButton();
        openMQRadioButton.setEnabled(false);
        openMQRadioButton.setText("openMQ");
        panel2.add(openMQRadioButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(406, 23), null, 0, false));
        rayRadioButton = new JRadioButton();
        rayRadioButton.setEnabled(false);
        rayRadioButton.setText("Ray");
        panel2.add(rayRadioButton, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(406, 23), null, 0, false));
        gRPCRadioButton = new JRadioButton();
        gRPCRadioButton.setEnabled(false);
        gRPCRadioButton.setText("gRPC");
        panel2.add(gRPCRadioButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Select Distributed Compute Interface");
        panel2.add(label1, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        arrowRadioButton = new JRadioButton();
        arrowRadioButton.setEnabled(false);
        arrowRadioButton.setText("Arrow");
        panel2.add(arrowRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tempfilePathLabel = new JPanel();
        tempfilePathLabel.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Disk Management", tempfilePathLabel);
        final JLabel label2 = new JLabel();
        label2.setText("RAM Disk");
        tempfilePathLabel.add(label2, new GridConstraints(0, 0, 4, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(58, 25), null, 0, false));
        create_ramdisk = new JButton();
        create_ramdisk.setText("Create RAM disk");
        tempfilePathLabel.add(create_ramdisk, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clear_ramdisk = new JButton();
        clear_ramdisk.setText("Clear RAM disk");
        tempfilePathLabel.add(clear_ramdisk, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        destroy_ramdisk = new JButton();
        destroy_ramdisk.setText("Destroy RAM disk");
        tempfilePathLabel.add(destroy_ramdisk, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        temp_file_path = new JTextField();
        temp_file_path.setText("/Volumes/RAM_Disk/JavaPlugin_temp_folder/");
        tempfilePathLabel.add(temp_file_path, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        tempfilePathLabel.add(spacer3, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        tempfilePathLabel.add(spacer4, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Tempfile Path");
        tempfilePathLabel.add(label3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
