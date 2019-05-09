package mm2python.UI;

// intelliJ libraries
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

// mm2python libraries
import mm2python.DataStructures.Constants;
import mm2python.DataStructures.Exceptions.OSTypeException;
import mm2python.messenger.Py4J.Py4J;
import mm2python.mmDataHandler.ramDisk.ramDiskConstructor;
import mm2python.mmDataHandler.ramDisk.ramDiskDestructor;
import mm2python.mmDataHandler.ramDisk.tempPathFlush;
import mm2python.mmEventHandler.globalEvents;

// mm libraries
import org.micromanager.Studio;

// java libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;


public class pythonBridgeUI_dialog extends JFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JButton create_python_bridge;
    private JButton shutdown_python_bridge;
    private JButton start_monitor_global_events;
    private JButton stop_monitor_global_events;
    private JTextArea UI_logger_textArea;
    private JRadioButton py4JRadioButton;
    private JRadioButton kafkaRadioButton;
    private JRadioButton gRPCRadioButton;
    private JRadioButton openMQRadioButton;
    private JRadioButton arrowRadioButton;
    private JButton clear_ramdisk;
    private JButton create_ramdisk;
    private JTextField temp_file_path;
    private JButton destroy_ramdisk;
    private JScrollPane UI_logger;
    private JLabel selectMessengerInterfaceLabel;
    private JPanel DiskManagement;
    private JPanel Configuration;
    private JPanel Console;
    private JRadioButton fixedRadioButton;
    private JRadioButton dynamicRadioButton;
    private JTextPane fixedWriteToATextPane;
    private JTextPane dynamicWriteToATextPane;

    private static Studio mm;
    private Py4J gate;
    private globalEvents gevents;
    private final tempPathFlush ramDisk = new tempPathFlush(mm);
    private static final JFileChooser fc = new JFileChooser();
    private static File defaultTempPath;

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
        fixedRadioButton.addActionListener(e -> fixedRadioButtonActionPerformed(e));
        dynamicRadioButton.addActionListener(e -> dynamicRadioButtonActionPerformed(e));

        temp_file_path.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                temp_file_path_MousePerformed(me);
            }
        });

        nonUIInit(mm_);

        try {
            if (Constants.getOS().equals("win")) {
                temp_file_path.setText("C:/mmtemp");
                defaultTempPath = new File(temp_file_path.getText());
                fc.setCurrentDirectory(defaultTempPath);
            } else if (Constants.getOS().equals("mac")) {
                temp_file_path.setText("/Volumes/Q/mmtemp/");
                defaultTempPath = new File(temp_file_path.getText());
                fc.setCurrentDirectory(defaultTempPath);
            }
        } catch (OSTypeException ex) {
            reporter.set_report_area(false, false, ex.toString());
        }

    }

    private void nonUIInit(Studio mm_) {
        // initialize static values
        mm = mm_;
        new reporter(UI_logger_textArea, mm);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        ramDisk = new tempPathFlush(mm);

        // initialize Constants
        new Constants();
        if (py4JRadioButton.isSelected()) {
            Constants.py4JRadioButton = true;
        }
        Constants.tempFilePath = temp_file_path.getText();
        reporter.set_report_area(true, false, "mm2python.UI INITIALIZATION filename = " + Constants.tempFilePath);
    }

    private void temp_file_path_MousePerformed(MouseEvent evt) {
        int returnVal = fc.showOpenDialog(pythonBridgeUI_dialog.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            temp_file_path.setText(file.toString());
            Constants.tempFilePath = file.toString();
            defaultTempPath = file;
            reporter.set_report_area(false, false, "Temp file path changed to: " + Constants.tempFilePath);
        } else {
            reporter.set_report_area(false, false, "unable to set new path for temp file");
        }
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
//        Constants.resetAll();
        gevents.unRegisterGlobalEvents();

    }

    private void create_ramdiskActionPerformed(ActionEvent evt) {
        new ramDiskConstructor(mm);
    }

    private void clear_ramdiskActionPerformed(ActionEvent evt) {
        ramDisk.clearTempPathContents();
    }

    private void destroy_ramdiskActionPerformed(ActionEvent evt) {
        new ramDiskDestructor();
    }

    private void py4jRadioButtonActionPerformed(ActionEvent evt) {
        if (py4JRadioButton.isSelected()) {
            Constants.py4JRadioButton = true;
        }
    }

    private void fixedRadioButtonActionPerformed(ActionEvent evt) {
        Constants.fixedMemMap = true;
    }

    private void dynamicRadioButtonActionPerformed(ActionEvent evt) {
        Constants.fixedMemMap = false;
    }

    public void setData(pythonBridgeUI_dialog data) {
    }

    public void getData(pythonBridgeUI_dialog data) {
    }

    public boolean isModified(pythonBridgeUI_dialog data) {
        return false;
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
        contentPane.add(tabbedPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(800, 600), null, 0, false));
        Console = new JPanel();
        Console.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Main Pane", Console);
        UI_logger = new JScrollPane();
        Console.add(UI_logger, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        UI_logger_textArea = new JTextArea();
        Font UI_logger_textAreaFont = this.$$$getFont$$$(null, -1, 8, UI_logger_textArea.getFont());
        if (UI_logger_textAreaFont != null) UI_logger_textArea.setFont(UI_logger_textAreaFont);
        UI_logger.setViewportView(UI_logger_textArea);
        create_python_bridge = new JButton();
        create_python_bridge.setText("Create Python Bridge");
        Console.add(create_python_bridge, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shutdown_python_bridge = new JButton();
        shutdown_python_bridge.setText("Shutdown Python Bridge");
        Console.add(shutdown_python_bridge, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        start_monitor_global_events = new JButton();
        start_monitor_global_events.setText("START monitor");
        Console.add(start_monitor_global_events, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stop_monitor_global_events = new JButton();
        stop_monitor_global_events.setText("STOP monitor");
        Console.add(stop_monitor_global_events, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Configuration = new JPanel();
        Configuration.setLayout(new GridLayoutManager(11, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Configuration", Configuration);
        selectMessengerInterfaceLabel = new JLabel();
        selectMessengerInterfaceLabel.setText("Select Messenger Interface");
        Configuration.add(selectMessengerInterfaceLabel, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        py4JRadioButton = new JRadioButton();
        py4JRadioButton.setSelected(true);
        py4JRadioButton.setText("Py4J");
        Configuration.add(py4JRadioButton, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        kafkaRadioButton = new JRadioButton();
        kafkaRadioButton.setEnabled(false);
        kafkaRadioButton.setText("Kafka");
        Configuration.add(kafkaRadioButton, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openMQRadioButton = new JRadioButton();
        openMQRadioButton.setEnabled(false);
        openMQRadioButton.setText("openMQ");
        Configuration.add(openMQRadioButton, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(406, 23), null, 0, false));
        gRPCRadioButton = new JRadioButton();
        gRPCRadioButton.setEnabled(false);
        gRPCRadioButton.setText("gRPC");
        Configuration.add(gRPCRadioButton, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Select memmap method");
        Configuration.add(label1, new GridConstraints(7, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        Configuration.add(spacer1, new GridConstraints(6, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        arrowRadioButton = new JRadioButton();
        arrowRadioButton.setEnabled(false);
        arrowRadioButton.setText("Arrow");
        Configuration.add(arrowRadioButton, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fixedRadioButton = new JRadioButton();
        fixedRadioButton.setSelected(true);
        fixedRadioButton.setText("Fixed");
        Configuration.add(fixedRadioButton, new GridConstraints(8, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dynamicRadioButton = new JRadioButton();
        dynamicRadioButton.setText("Dynamic");
        Configuration.add(dynamicRadioButton, new GridConstraints(9, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fixedWriteToATextPane = new JTextPane();
        fixedWriteToATextPane.setText("Fixed: \nWrite to a fixed number of memory-mapped files (default 50).  Preserves disk space and has faster input-output speeds, but holds only the most recent 50 images");
        Configuration.add(fixedWriteToATextPane, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        dynamicWriteToATextPane = new JTextPane();
        dynamicWriteToATextPane.setText("Dynamic: \nWrite to a growing number of memory-mapped files.  Every new image is mapped to its own file until cleared.  Occupies disk space but has slower input-output speeds.");
        Configuration.add(dynamicWriteToATextPane, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        DiskManagement = new JPanel();
        DiskManagement.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Disk Management", DiskManagement);
        clear_ramdisk = new JButton();
        clear_ramdisk.setText("Clear Temp Folder");
        DiskManagement.add(clear_ramdisk, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("RAM Disk");
        DiskManagement.add(label2, new GridConstraints(3, 0, 3, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(58, 25), null, 0, false));
        destroy_ramdisk = new JButton();
        destroy_ramdisk.setText("Destroy RAM disk");
        DiskManagement.add(destroy_ramdisk, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create_ramdisk = new JButton();
        create_ramdisk.setText("Create RAM disk");
        DiskManagement.add(create_ramdisk, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        temp_file_path = new JTextField();
        temp_file_path.setText("/Volumes/RAM_Disk/JavaPlugin_temp_folder/");
        DiskManagement.add(temp_file_path, new GridConstraints(1, 1, 3, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Tempfile Path");
        DiskManagement.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
