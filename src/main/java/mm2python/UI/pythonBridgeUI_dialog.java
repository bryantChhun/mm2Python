package mm2python.UI;

// intelliJ libraries
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

// mm2python libraries
import mm2python.DataStructures.*;
import mm2python.DataStructures.Exceptions.OSTypeException;
import mm2python.DataStructures.Maps.MDSMap;
import mm2python.DataStructures.Queues.FixedMemMapReferenceQueue;
import mm2python.DataStructures.Queues.MDSQueue;
import mm2python.DataStructures.Queues.DynamicMemMapReferenceQueue;
import mm2python.MPIMethod.Py4J.Py4J;
import mm2python.mmDataHandler.ramDisk.ramDiskConstructor;
import mm2python.mmDataHandler.ramDisk.ramDiskDestructor;
import mm2python.mmEventHandler.Executor.main_executor;
import mm2python.mmEventHandler.globalEvents;

// mm libraries
import mmcorej.CMMCore;
import org.micromanager.Studio;

// java libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class pythonBridgeUI_dialog extends JFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JButton create_python_bridge;
    private JButton shutdown_python_bridge;
    private JButton start_monitor_global_events;
    private JButton stop_monitor_global_events;
    private JTextArea UI_logger_textArea;
    private JRadioButton py4JRadioButton;
    private JRadioButton openMQRadioButton;
    private JRadioButton arrowRadioButton;
    private JButton clear_temp_folder;
    private JButton create_ramdisk;
    private JTextField guiTempFilePath;
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
    private JRadioButton consoleRadioButton;
    private JRadioButton MMCoreLogsRadioButton;
    private JRadioButton systemOutRadioButton;
    private JTextField maxNumberOfFilesTextField;

    private static Studio mm;
    private static CMMCore mmc;
    private Py4J gate;
    private globalEvents gevents;
    private final tempPathFlush clearTempPath = new tempPathFlush(mm);
    private static final JFileChooser fc = new JFileChooser();
    private static File defaultTempPath;

    public pythonBridgeUI_dialog(Studio mm_, CMMCore mmc_) {
        // mm2python.UI components created in the static constructor below
        setContentPane(contentPane);
        create_python_bridge.addActionListener(e -> create_python_bridgeActionPerformed(e));
        shutdown_python_bridge.addActionListener(e -> shutdown_python_bridgeActionPerformed(e));
        start_monitor_global_events.addActionListener(e -> start_monitor_global_eventsActionPerformed(e));
        stop_monitor_global_events.addActionListener(e -> stop_monitor_global_eventsActionPerformed(e));
        create_ramdisk.addActionListener(e -> create_ramdiskActionPerformed(e));
        clear_temp_folder.addActionListener(e -> clear_ramdiskActionPerformed(e));
        destroy_ramdisk.addActionListener(e -> destroy_ramdiskActionPerformed(e));
        py4JRadioButton.addActionListener(e -> py4jRadioButtonActionPerformed(e));
        fixedRadioButton.addActionListener(e -> fixedRadioButtonActionPerformed(e));
        dynamicRadioButton.addActionListener(e -> dynamicRadioButtonActionPerformed(e));
        consoleRadioButton.addActionListener(e -> consoleRadioButtonActionPerformed(e));
        MMCoreLogsRadioButton.addActionListener(e -> MMCoreLogsRadioButtonActionPerformed(e));
        systemOutRadioButton.addActionListener(e -> systemOutRadioButtonActionPerformed(e));

        guiTempFilePath.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                temp_file_path_MousePerformed(me);
            }
        });

        nonUIInit(mm_, mmc_);

        initTempPath();

        // shutdownhook is broken right now
        // causes application freeze upon exit
//        createShutdownHook();

    }

    private void initTempPath() {
        if (Constants.getOS().equals("win")) {
            guiTempFilePath.setText("C:/mmtemp");
            defaultTempPath = new File(guiTempFilePath.getText());
            Constants.tempFilePath = defaultTempPath.toString();
            fc.setCurrentDirectory(defaultTempPath);
        } else if (Constants.getOS().equals("mac")) {
            String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "mmtemp";
            guiTempFilePath.setText(path);
            defaultTempPath = new File(guiTempFilePath.getText());
            Constants.tempFilePath = defaultTempPath.toString();
            fc.setCurrentDirectory(defaultTempPath);
        }
    }

    private void nonUIInit(Studio mm_, CMMCore mmc_) {
        // initialize static values
        mm = mm_;
        mmc = mmc_;

        new reporter(UI_logger_textArea, mm);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // initialize Constants
        new Constants();
        if (py4JRadioButton.isSelected()) {
            Constants.setPy4JRadioButton(true);
        }
        Constants.tempFilePath = guiTempFilePath.getText();
        Constants.bitDepth = mm.getCMMCore().getImageBitDepth();
        Constants.height = mm.getCMMCore().getImageHeight();
        Constants.width = mm.getCMMCore().getImageWidth();
        Constants.setFixedMemMap(true);
        reporter.set_report_area("mm2python.UI INITIALIZATION filename = " + Constants.tempFilePath);

        // initialize MetaDataStore Map
        new MDSMap();

        // initialize Queues
        new MDSQueue();
        new FixedMemMapReferenceQueue();

        // set reporting
        reporter.console = true;
        reporter.systemout = true;
    }

    private void createShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            reporter.set_report_area("Shutdown Hook is running !");
            gate.stopConnection(ABORT);
            if (!Constants.getFixedMemMap()) {
                clearTempPath.clearTempPathContents();
            }
            gevents.unRegisterGlobalEvents();
        }
        ));
    }

    private void temp_file_path_MousePerformed(MouseEvent evt) {
        int returnVal = fc.showOpenDialog(pythonBridgeUI_dialog.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            guiTempFilePath.setText(file.toString());
            Constants.tempFilePath = file.toString();
            defaultTempPath = file;
            reporter.set_report_area("Temp file path changed to: " + Constants.tempFilePath);
        } else {
            reporter.set_report_area("unable to set new path for temp file");
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
        if (defaultTempPath.exists() || defaultTempPath.mkdirs()) {
            reporter.set_report_area("tempPath created or already exists");
        } else {
            reporter.set_report_area("WARNING: invalid temp path, no MMap files will be made");
        }

        // CREATE FIXED CIRCULAR REFERENCE
        int num = Integer.parseInt(maxNumberOfFilesTextField.getText());
        create_circular_map_reference(num);

        // CREATE DYNAMIC REFERENCE
        create_dynamic_map_reference();

        if (gevents == null) {
            gevents = new globalEvents(mm);
        }
    }

    private void stop_monitor_global_eventsActionPerformed(ActionEvent evt) {
        if (!Constants.getFixedMemMap()) {
            clearTempPath.clearTempPathContents();
        }
        gevents.unRegisterGlobalEvents();
        gevents = null;

        shutdownAndAwaitTermination();

        UI_logger_textArea.setText("");
        reporter.set_report_area("STOP monitoring global events, clearing data store references");

    }

    /**
     * shutdown procedure taken from:
     * https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
     */
    private void shutdownAndAwaitTermination() {
        ExecutorService mmExecutor = new main_executor().getExecutor();
        mmExecutor.shutdown();
        try {
            if(!mmExecutor.awaitTermination(5, TimeUnit.SECONDS)){
                mmExecutor.shutdownNow();
                if(!mmExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Executor thread pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            mmExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void create_circular_map_reference(int num_) {
        if (Constants.getFixedMemMap()) {
            try {
                FixedMemMapReferenceQueue.createFileNames(num_);
            } catch (FileNotFoundException fex) {
                reporter.set_report_area("exception creating circular memmaps: " + fex.toString());
            }
        }
    }

    private void create_dynamic_map_reference() {
        if (!Constants.getFixedMemMap()) {
            try {
                int num_channels = mm.acquisitions().getAcquisitionSettings().channels.size();
                int num_z = mm.acquisitions().getAcquisitionSettings().slices.size();
                new DynamicMemMapReferenceQueue(4, 30);
            } catch (Exception ex) {
                reporter.set_report_area("\t\tEXCEPTION RETRIEVING CHANNELS AND Z FOR DYNAMIC MMMAP");
                int num_channels = 4;
                int num_z = 30;
                new DynamicMemMapReferenceQueue(num_channels, num_z);
            }
        }
    }

    private void create_ramdiskActionPerformed(ActionEvent evt) {
        new ramDiskConstructor(mm);
    }

    private void clear_ramdiskActionPerformed(ActionEvent evt) {
        clearTempPath.clearTempPathContents();
    }

    private void destroy_ramdiskActionPerformed(ActionEvent evt) {
        new ramDiskDestructor();
    }

    private void py4jRadioButtonActionPerformed(ActionEvent evt) {
        if (py4JRadioButton.isSelected()) {
            Constants.setPy4JRadioButton(true);
        }
    }

    private void fixedRadioButtonActionPerformed(ActionEvent evt) {
        if (fixedRadioButton.isSelected()) {
            Constants.setFixedMemMap(true);
            dynamicRadioButton.setSelected(false);
        } else {
            Constants.setFixedMemMap(false);
            dynamicRadioButton.setSelected(true);
        }
    }

    private void dynamicRadioButtonActionPerformed(ActionEvent evt) {
        if (dynamicRadioButton.isSelected()) {
            Constants.setFixedMemMap(false);
            fixedRadioButton.setSelected(false);
        } else {
            Constants.setFixedMemMap(true);
            fixedRadioButton.setSelected(true);
        }
    }

    private void consoleRadioButtonActionPerformed(ActionEvent evt) {
        if (consoleRadioButton.isSelected()) {
            reporter.console = true;
        } else {
            reporter.console = false;
        }
    }

    private void MMCoreLogsRadioButtonActionPerformed(ActionEvent evt) {
        if (MMCoreLogsRadioButton.isSelected()) {
            reporter.mmlogs = true;
        } else {
            reporter.mmlogs = false;
        }
    }

    private void systemOutRadioButtonActionPerformed(ActionEvent evt) {
        if (systemOutRadioButton.isSelected()) {
            reporter.systemout = true;
        } else {
            reporter.systemout = false;
        }
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
        UI_logger_textArea.setEditable(false);
        Font UI_logger_textAreaFont = this.$$$getFont$$$(null, -1, 14, UI_logger_textArea.getFont());
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
        Configuration.setLayout(new GridLayoutManager(14, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Configuration", Configuration);
        selectMessengerInterfaceLabel = new JLabel();
        selectMessengerInterfaceLabel.setText("Select Messenger Interface");
        Configuration.add(selectMessengerInterfaceLabel, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        py4JRadioButton = new JRadioButton();
        py4JRadioButton.setSelected(true);
        py4JRadioButton.setText("Py4J");
        Configuration.add(py4JRadioButton, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openMQRadioButton = new JRadioButton();
        openMQRadioButton.setEnabled(false);
        openMQRadioButton.setText("openMQ");
        Configuration.add(openMQRadioButton, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(406, 23), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Select memmap method");
        Configuration.add(label1, new GridConstraints(10, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        Configuration.add(spacer1, new GridConstraints(9, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        arrowRadioButton = new JRadioButton();
        arrowRadioButton.setEnabled(false);
        arrowRadioButton.setText("Arrow");
        Configuration.add(arrowRadioButton, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fixedRadioButton = new JRadioButton();
        fixedRadioButton.setSelected(true);
        fixedRadioButton.setText("Fixed");
        Configuration.add(fixedRadioButton, new GridConstraints(11, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dynamicRadioButton = new JRadioButton();
        dynamicRadioButton.setText("Dynamic");
        Configuration.add(dynamicRadioButton, new GridConstraints(12, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fixedWriteToATextPane = new JTextPane();
        fixedWriteToATextPane.setSelectionColor(new Color(-8529665));
        fixedWriteToATextPane.setText("Fixed: \nWrite to a fixed number of memory-mapped files (default 100).  Preserves disk space and has faster input-output speeds, but holds only the most recent 100 images");
        Configuration.add(fixedWriteToATextPane, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        dynamicWriteToATextPane = new JTextPane();
        dynamicWriteToATextPane.setSelectionColor(new Color(-365));
        dynamicWriteToATextPane.setText("Dynamic: \nWrite to a growing number of memory-mapped files.  Every new image is mapped to its own file until cleared.  Occupies disk space and has slower input-output speeds, but allows data access of the whole acquisition.");
        Configuration.add(dynamicWriteToATextPane, new GridConstraints(13, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Logging");
        Configuration.add(label2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        consoleRadioButton = new JRadioButton();
        consoleRadioButton.setSelected(true);
        consoleRadioButton.setText("Console");
        Configuration.add(consoleRadioButton, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MMCoreLogsRadioButton = new JRadioButton();
        MMCoreLogsRadioButton.setSelected(false);
        MMCoreLogsRadioButton.setText("MM Core Logs");
        Configuration.add(MMCoreLogsRadioButton, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        systemOutRadioButton = new JRadioButton();
        systemOutRadioButton.setText("System Out");
        Configuration.add(systemOutRadioButton, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        Configuration.add(spacer2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        maxNumberOfFilesTextField = new JTextField();
        maxNumberOfFilesTextField.setText("100");
        Configuration.add(maxNumberOfFilesTextField, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        DiskManagement = new JPanel();
        DiskManagement.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Disk Management", DiskManagement);
        clear_temp_folder = new JButton();
        clear_temp_folder.setText("Clear Temp Folder");
        DiskManagement.add(clear_temp_folder, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("RAM Disk");
        DiskManagement.add(label3, new GridConstraints(3, 0, 3, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(58, 25), null, 0, false));
        destroy_ramdisk = new JButton();
        destroy_ramdisk.setEnabled(false);
        destroy_ramdisk.setText("Destroy RAM disk");
        DiskManagement.add(destroy_ramdisk, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create_ramdisk = new JButton();
        create_ramdisk.setEnabled(false);
        create_ramdisk.setText("Create RAM disk");
        DiskManagement.add(create_ramdisk, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guiTempFilePath = new JTextField();
        guiTempFilePath.setText("/");
        DiskManagement.add(guiTempFilePath, new GridConstraints(1, 1, 3, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Tempfile Path");
        DiskManagement.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

    //    public void setData(pythonBridgeUI_dialog data) {
//    }
//
//    public void getData(pythonBridgeUI_dialog data) {
//    }

//    public boolean isModified(pythonBridgeUI_dialog data) {
//        return false;
//    }

}
