package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.crypto.SealedObject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import entities.DataPackage;
import entities.Process;
import services.Client;
import services.HelperService;

public class Draw extends JPanel {
    private ArrayList<entities.Process> processes = new ArrayList<>();
    private JPanel mainPanel;
    private CustomPanel chartPanel;
    private JScrollPane chartPane;
    private JComboBox option;
    private JButton computeBtn;
    private JTable table;
    private JScrollPane tablePane;
    private DefaultTableModel model;
    private JLabel waitingtimeJLabel;
    private JLabel aroundtimeJLabel;
    private JLabel waitingtimeResultJLabel;
    private JLabel aroundtimeResultJLabel;
    private JLabel h1JLabel;
    private JTextField jTextField;
    private JButton jButton1;
    private static Client client = null;

    public Draw() {
        this.setSize(1090, 750);
//        try {
//            client = new Client("localhost", 1234);
//            client.startClient();
//        } catch (Exception ex) {
//            Logger.getLogger(Manage.class.getName()).log(Level.SEVERE, null, ex);
//        }


        jTextField = new JTextField();
        jTextField.setBounds(25, 150, 190, 25);

        jButton1 = new JButton("ReadFile");
        jButton1.setBounds(225, 150, 80, 25);
        jButton1.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        option = new JComboBox(new String[]{"FCFS", "SJF", "priority", "RR"});
        option.setBounds(650, 150, 250, 25);
        computeBtn = new JButton("Compute");
        computeBtn.setBounds(930, 150, 80, 25);
        computeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        table = new JTable();
        model = new DefaultTableModel(new Object[][]{}, new String[]{"PROCESS", "AT", "BT", "WT", "CT", "PRIORITY", "TURNAROUND TIME"});
        table.setModel(model);
        tablePane = new JScrollPane(table);
        tablePane.setBounds(25, 400, 1000, 150);
        chartPanel = new CustomPanel();
        chartPanel.setBackground(Color.WHITE);
        chartPane = new JScrollPane(chartPanel);
        chartPane.setBounds(25, 250, 1000, 100);

        waitingtimeJLabel = new JLabel("average waiting time: ");
        waitingtimeJLabel.setBounds(30, 560, 138, 30);

        waitingtimeResultJLabel = new JLabel();
        waitingtimeResultJLabel.setBounds(170, 560, 100, 30);

        aroundtimeJLabel = new JLabel("average around time: ");
        aroundtimeJLabel.setBounds(30, 590, 138, 30);

        aroundtimeResultJLabel = new JLabel();
        aroundtimeResultJLabel.setBounds(170, 590, 100, 30);

        h1JLabel = new JLabel("Lập lịch CPU");
        h1JLabel.setFont(new Font("Segoe UI", Font.CENTER_BASELINE, 40));
        h1JLabel.setBounds(350, 30, 400, 60);

        mainPanel = new JPanel(null);
        mainPanel.setPreferredSize(new Dimension(1090, 750));
        // Action read file
        jButton1.addActionListener(e -> {
            if (jTextField.getText() == null) {
                JOptionPane.showMessageDialog(this, "No file",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    processes = HelperService.readProcess(jTextField.getText());
                    setTableProcess(processes, model);
                } catch (Exception exception) {
                    System.out.println(exception);
                    JOptionPane.showMessageDialog(this, "error ocurred!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action send file to service
        computeBtn.addActionListener(e -> {
            try {
                client = new Client();
                client.startClient();
                if (processes.size() == 0) {
                    throw new Exception("null");
                }
                client.setProcesses(processes);
                String selected = (String) option.getSelectedItem();
                DataPackage dataPackage;
                SealedObject sealedObject;
                String str;
                switch (selected) {
                    case "FCFS":
                        sealedObject = HelperService.encryptObject(client.getProcesses(), client.getSecretKey());
                        str = HelperService.encryptInput("fcfs", client.getSecretKey());
                        break;
                    case "SJF":
                        sealedObject = HelperService.encryptObject(client.getProcesses(), client.getSecretKey());
                        str = HelperService.encryptInput("sjf", client.getSecretKey());
                        break;
                    case "priority":
                        sealedObject = HelperService.encryptObject(client.getProcesses(), client.getSecretKey());
                        str = HelperService.encryptInput("priority", client.getSecretKey());
                        break;
                    case "RR":
                        sealedObject = HelperService.encryptObject(client.getProcesses(), client.getSecretKey());
                        str = HelperService.encryptInput("rrb", client.getSecretKey());
                        break;
                    default:
                        return;
                }
                dataPackage = new DataPackage(sealedObject, str);
                client.send(dataPackage);
                dataPackage = client.receive();
                processes = (ArrayList<Process>) dataPackage.getSealedObject().getObject(client.getSecretKey());
                System.out.println(processes);
                System.out.println(client.getProcesses());
                chartPanel.setProcesses(processes);
                waitingtimeResultJLabel.setText(String.valueOf(wt(processes)));
                aroundtimeResultJLabel.setText(String.valueOf(at(processes)));
                setTableProcess(processes,model);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "unread file or incorrect data!",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(option);
        mainPanel.add(jTextField);
        mainPanel.add(jButton1);
        mainPanel.add(computeBtn);
        mainPanel.add(tablePane);
        mainPanel.add(waitingtimeJLabel);
        mainPanel.add(aroundtimeJLabel);
        mainPanel.add(waitingtimeResultJLabel);
        mainPanel.add(aroundtimeResultJLabel);
        mainPanel.add(h1JLabel);
        mainPanel.add(chartPane);
        this.add(mainPanel);
    }

    private void setTableProcess(ArrayList<entities.Process> processes, DefaultTableModel model) {
        model.setRowCount(0);
        Object[] row = new Object[7];
        for (int i = 0; i < processes.size(); i++) {
            row[0] = processes.get(i).getNameProcess();
            row[1] = processes.get(i).getArrivalTime();
            row[2] = processes.get(i).getBurstTime();
            row[3] = processes.get(i).getWaitingTime();
            row[4] = processes.get(i).getCompletionTime();
            row[5] = processes.get(i).getPriority();
            row[6] = processes.get(i).getTurnaroundTime();
            model.addRow(row);
        }
    }

    private float wt(ArrayList<entities.Process> processes) {
        float sum = 0;
        for (int i = 0; i < processes.size(); i++) {
            sum = sum + this.processes.get(i).getWaitingTime();
        }
        return sum / processes.size();

    }

    private float at(ArrayList<entities.Process> processes) {
        float sum = 0;
        for (int i = 0; i < processes.size(); i++) {
            sum = sum + this.processes.get(i).getTurnaroundTime();
        }
        return sum / processes.size();

    }

    public void sort(ArrayList<entities.Process> processes) {
        Collections.sort(processes, Comparator.comparingInt(Process::getCompletionTime));
    }

    class CustomPanel extends JPanel {
        private ArrayList<Process> processes;

        @Override
        public void paintComponent(Graphics g) {
//            sort(processes);
            super.paintComponent(g);
            if (processes != null) {
                if (processes.get(0).getCompletionTime() != 0) {
                    int x = 100;
                    int y = 30;
                    g.drawString("0", x - 5, y + 45);
                    for (int i = 0; i < processes.size(); i++) {
                        entities.Process p = processes.get(i);
                        x = 100 * (i + 1);
                        y = 30;
                        g.drawRect(x, y, 100, 30);
                        g.setFont(new Font("Segoe UI", Font.BOLD, 13));
                        g.drawString(processes.get(i).getNameProcess(), x + 45, y + 20);

                        g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        g.drawString(Integer.toString(processes.get(i).getCompletionTime()), x + 95, y + 45);
                    }
                }
            }
        }

        public void setProcesses(ArrayList<Process> processes) {
            this.processes = processes;
            repaint();
        }
    }
}




