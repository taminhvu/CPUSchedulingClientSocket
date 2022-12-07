
package view;

import entities.DataPackage;
import entities.Ddnode;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SealedObject;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import services.Client;
import services.HelperService;

public class ShortWay extends javax.swing.JPanel {

    JPanel jPanel1 = new javax.swing.JPanel();
    JPanel jPanel_draw = new javax.swing.JPanel();
    JLabel jLabel2 = new javax.swing.JLabel();
    JTextField jTextField1 = new JTextField();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    private static Client client = null;


    public class MyGraphics extends JComponent {

        public static int kc = 100;
        private static final long serialVersionUID = 1L;
        private String url;


        MyGraphics(String url) {
            this.url = url;
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.yellow);
        }

        MyGraphics() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.yellow);
        }

        @Override
        public void paintComponent(Graphics g) {
            try {
                int x[] = new int[50];
                int y[] = new int[50];
                BufferedReader bufferedReader = null;

                // File node
                File file = new File(this.url);
                try {
                    bufferedReader = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException ex) {
                    throw ex;
                }
                String line;
                // duyệt từng hàng, kiếm cặp node (source-destination)
                line = bufferedReader.readLine();
                ArrayList<String> aline = new ArrayList<>();
                do {
                    StringTokenizer st = null;
                    try {
                        st = new StringTokenizer(line, ";");
                    } catch (Exception ex) {
                        throw ex;
                    }
                    while (st.hasMoreTokens()) {
                        aline.add(st.nextToken());
                    }
                    line = bufferedReader.readLine();
                } while (line != null);

                // Node đầu
                x[1] = 100;
                y[1] = 50;
                g.drawOval(x[1], y[1], 25, 25);
                g.drawString("1", x[1] + 10, y[1] + 15);

                // Node kế tiếp (add ngang)
                x[2] = x[1] + kc;
                y[2] = 50;
                g.drawOval(x[2], y[2], 25, 25);
                g.drawString("2", x[2] + 10, y[2] + 15);
                int trongso = Integer.parseInt(aline.get(2));
                String trongsoString = Integer.toString(trongso);
                int xtrongso = x[1] + 65;
                int ytrongso = y[1] + 10;
                g.drawString(trongsoString, xtrongso, ytrongso);
                g.drawLine(x[1] + 25, y[1] + 12, x[2], y[2] + 12);

//            for (int i=4;i<aline.size(); i+=3) {
//                for (int j=1; j<i; j+=3) {
//                    if (aline.get(j).equals(aline.get(i))) {
//                        JOptionPane.showMessageDialog(jPanel1, "Không thể vẽ vì có node bị trùng!!!");
//                        initComponents();
//                        break;
//                    }
//                }
//            }
                for (int i = 4; i < aline.size(); i += 3) {
                    // node đầu add ngang rồi nên temp = 1 => add dọc
                    int j = i - 1;
                    int sttj = Integer.parseInt(aline.get(j));
                    String sttjchu = Integer.toString(sttj);

                    int stt = Integer.parseInt(aline.get(i));
                    String sttchu = Integer.toString(stt);

                    if (x[stt] == 0) {
                        int temp = 1;
                        // node i được trỏ đến bởi node j

                        trongso = Integer.parseInt(aline.get(i + 1));
                        trongsoString = Integer.toString(trongso);
                        // Xet node hien tai voi node source (2nd)
                        // h1 xét node đứng trước (source)
                        for (int h1 = 0; h1 < j; h1 += 3) {
                            // so sánh node j (trỏ đến node i) với các node đã từng trỏ
                            if (aline.get(j).equals(aline.get(h1))) {
                                // Nếu temp = 1 thì  add dọc
                                if (h1 >= 6 && h1 <= j - 6) {
                                    int h1sau = h1 + 3;
                                    if (aline.get(h1sau).equals(aline.get(h1))) {
                                        temp = 2;
                                    }
                                }

                                if (temp == 1) {
                                    int stth1 = Integer.parseInt(aline.get(h1));
                                    x[stt] = x[stth1];
                                    y[stt] = y[stth1] + kc;
                                    g.drawOval(x[stt], y[stt], 25, 25);
                                    g.drawString(sttchu, x[stt] + 10, y[stt] + 15);
                                    g.drawLine(x[stth1] + 12, y[stth1] + 25, x[stt] + 12, y[stt]);
                                    xtrongso = x[stth1] + 5;
                                    ytrongso = y[stth1] + 65;

                                    g.drawString(trongsoString, xtrongso, ytrongso);
                                    temp++;
                                } // đã add dọc và add ngang thì add ở giữa
                                else if (temp == 2) {
                                    int stth1 = Integer.parseInt(aline.get(h1));
                                    x[stt] = x[stth1] + kc / 2;
                                    y[stt] = y[stth1] + kc / 2;
                                    g.drawOval(x[stt], y[stt], 25, 25);
                                    g.drawString(sttchu, x[stt] + 10, y[stt] + 15);
                                    g.drawLine(x[stth1] + 19, y[stth1] + 22, x[stt] + 5, y[stt] + 2);
                                    g.drawString(trongsoString, x[stth1] + 40, y[stth1] + 38);
                                    temp++;
                                }
                            }
                        }

                        // Xet node hien tai voi node destination (1st)
                        for (int h2 = 1; h2 < j; h2 += 3) {
                            // add ngang trước, lần đầu xuất hiện của node
                            if (aline.get(j).equals(aline.get(h2)) && temp < 2) {
                                int stth2 = Integer.parseInt(aline.get(h2));
                                x[stt] = x[stth2] + kc;
                                y[stt] = y[stth2];
                                // Nếu trùng thì tách 2 node
                                xtrongso = x[stth2] + 65;
                                ytrongso = y[stth2] + 10;
                                for (int l = 4; l < stt; l++) {
                                    if (x[stt] == x[l]) {
                                        x[stt] -= 10;
                                        y[stt] += 25;
                                        xtrongso = x[stth2] + 58;
                                        ytrongso = y[stth2] + 23;
                                        break;
                                    }
                                }
                                g.drawOval(x[stt], y[stt], 25, 25);
                                g.drawString(sttchu, x[stt] + 10, y[stt] + 15);
                                g.drawLine(x[stth2] + 25, y[stth2] + 12, x[stt], y[stt] + 12);
                                g.drawString(trongsoString, xtrongso, ytrongso);
                            }
                        }
                    }
                }

                // Vẽ đường đi
                // File đường đi
                String line_way;
                //kiem tra message server gui ve xem co loi hay khong
                System.out.println("Server sent message: " + client.getMessage());
                System.out.println("Shortest path length is: " + client.getDdnode().getChiphi());

                jLabel2.setText("Shortest path length is: " + client.getDdnode().getChiphi());
                if (client.getMessage().equals("ok")) {
                    ArrayList<String> aline_way = new ArrayList<>();
                    System.out.println(client.getDdnode());
                    for (int i = 0; i < client.getDdnode().getNodes().size(); i++) {
                        line_way = client.getDdnode().getNodes().get(i).toLowerCase();
                        StringTokenizer st = new StringTokenizer(line_way, ";");
                        while (st.hasMoreTokens()) {
                            aline_way.add(st.nextToken());
                        }
                    }

                    g.setColor(Color.red);
                    g.drawOval(x[1], y[1], 25, 25);
                    g.drawString("1", x[1] + 10, y[1] + 15);

                    for (int i = 1; i < aline_way.size(); i += 2) {
                        int stt = Integer.parseInt(aline_way.get(i));
                        String sttchu = Integer.toString(stt);

                        int sttj = Integer.parseInt(aline_way.get(i - 1));
//                g.drawOval(x[stt], y[stt], 25, 25);
//                g.drawString(sttchu, x[stt] + 10, y[stt] + 15);

                        if (x[stt] == x[sttj]) {
                            g.drawOval(x[stt], y[stt], 25, 25);
                            g.drawString(sttchu, x[stt] + 10, y[stt] + 15);
                            g.drawLine(x[sttj] + 12, y[sttj] + 25, x[stt] + 12, y[stt]);
                        } else if (y[stt] == y[sttj]) {
                            g.drawOval(x[stt], y[stt], 25, 25);
                            g.drawString(sttchu, x[stt] + 10, y[stt] + 15);
                            g.drawLine(x[sttj] + 25, y[sttj] + 12, x[stt], y[stt] + 12);
                        } else if (x[stt] == x[sttj] + kc / 2) {
                            g.drawOval(x[stt], y[stt], 25, 25);
                            g.drawString(sttchu, x[stt] + 10, y[stt] + 15);
                            g.drawLine(x[sttj] + 19, y[sttj] + 22, x[stt] + 5, y[stt] + 2);
                        } else if (y[stt] == y[sttj] + 25) {
                            g.drawOval(x[stt], y[stt], 25, 25);
                            g.drawString(sttchu, x[stt] + 10, y[stt] + 15);
                            g.drawLine(x[sttj] + 25, y[sttj] + 12, x[stt], y[stt] + 12);
                        }
                    }
                } else {
                    throw new Exception("Loi khong the tinh toan va ve duong di ngan nhat!!");
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public ShortWay() {
        this.setSize(1090, 750);
        initComponents();
    }

    public static BufferedImage getScreenShot(
            Component component) {

        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        component.paint(image.getGraphics()); // alternately use .printAll(..)
        return image;
    }


    private void initComponents() {
        jButton1.setText("Attach");
        jButton2.setText("Draw Graph");
        jButton3.setText("Screen Graph");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);

        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)
                                .addContainerGap(549, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addContainerGap(80, Short.MAX_VALUE))
        );


        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ĐƯỜNG ĐI NGẮN NHẤT");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jButton1.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File f = chooser.getSelectedFile();
            if (f != null) {
                String filenameString = f.getAbsolutePath();
                jTextField1.setText(filenameString);
            }

        });

        jButton2.addActionListener(e -> {
            try {
                if (jTextField1.getText() != null) {
                    client = new Client();
                    client.startClient();
                    client.setDdnode(HelperService.readDDnode(jTextField1.getText()));
                    SealedObject sealedObject = HelperService.encryptObject(client.getDdnode(), client.getSecretKey());
                    String str = HelperService.encryptInput("ddnn", client.getSecretKey());
                    DataPackage dataPackage = new DataPackage(sealedObject, str);
                    client.send(dataPackage);

                    dataPackage = client.receive();
                    client.setDdnode((Ddnode) dataPackage.getSealedObject().getObject(client.getSecretKey()));
                    client.setMessage(HelperService.decryptInput(dataPackage.getMessage(), client.getSecretKey()));
                    System.out.println(client.getMessage());

                } else {
                    JOptionPane.showMessageDialog(this, "no file!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                if (client.getMessage().equals("ok")) {
                    MyGraphics graphics = new MyGraphics(jTextField1.getText());
                    jPanel_draw.add(graphics);
                    layout.setHorizontalGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jPanel_draw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addContainerGap())
                    );
                    layout.setVerticalGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(jPanel_draw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                } else {
                    JOptionPane.showMessageDialog(this, "loi xay ra khong the ve!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(this, "unread file or incorrect data!",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        jButton3.addActionListener(e -> {
            try {
                if (client.getMessage().equals("ok")) {
                    BufferedImage img = getScreenShot(jPanel_draw);
                    JOptionPane.showMessageDialog(
                            this,
                            new JLabel(
                                    new ImageIcon(img.getScaledInstance(
                                            img.getWidth(null) / 2,
                                            img.getHeight(null) / 2,
                                            Image.SCALE_SMOOTH)
                                    )));

                    // write the image as a PNG
                    try {
                        ImageIO.write(img, "png", new File("./screenshot.png"));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "loi luu hinh!",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "loi chup hinh!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(this, "chưa có dữ liệu mới trả về!",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}