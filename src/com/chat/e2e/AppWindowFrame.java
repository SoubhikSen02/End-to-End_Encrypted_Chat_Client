/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.chat.e2e;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Soubhik
 */
public class AppWindowFrame extends javax.swing.JFrame {

    /**
     * Creates new form appWindowFrame
     */
    public AppWindowFrame() {
        initComponents();
        appWindowPanel.setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        WindowManager.start(this);

        String[] previousWindowState = ConfigManager.getPreviousWindowState().split(",");
        if(previousWindowState[0].equals("windowed"))
        {
            setSize(Integer.parseInt(previousWindowState[1]), Integer.parseInt(previousWindowState[2]));
        }
        else if(previousWindowState[0].equals("fullscreen"))
        {
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }

        addComponentListener(new ComponentAdapter() {
            Robot robot;

            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    if (robot == null)
                        robot = new Robot();
                    Dimension currentSize = getSize();
                    Dimension minimumSize = getMinimumSize();
                    if (currentSize.width < minimumSize.width || currentSize.height < minimumSize.height) {
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        int newWidth = Math.max(minimumSize.width, currentSize.width);
                        int newHeight = Math.max(minimumSize.height, currentSize.height);
                        setSize(newWidth, newHeight);
                    }
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }

                if(getExtendedState() == JFrame.MAXIMIZED_BOTH)
                {
                    ConfigManager.setPreviousWindowState("fullscreen,800,600");
                }
                else
                {
                    Dimension size = getSize();
                    ConfigManager.setPreviousWindowState("windowed," + size.width + "," + size.height);
                }
            }
        });

        setVisible(true);

//        try {
//            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("D:\\download\\Noto_Emoji (1)\\NotoEmoji-VariableFont_wght.ttf"));
//            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            genv.registerFont(font);
//            font = font.deriveFont(12f);
//            UIManager.getLookAndFeelDefaults().put("defaultFont", font);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        //UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("", Font.PLAIN, 12));
        //System.out.println(getSize());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        appWindowPanel = new javax.swing.JPanel();
        topMenuBar = new javax.swing.JMenuBar();
        fileMenuItem = new javax.swing.JMenu();
        settingsSubMenuItem = new javax.swing.JMenuItem();
        exitSubMenuItem = new javax.swing.JMenuItem();
        editMenuItem = new javax.swing.JMenu();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                WindowManager.closeAppWindow();
            }
        });
        setTitle("E2E Chat");

        javax.swing.GroupLayout appWindowPanelLayout = new javax.swing.GroupLayout(appWindowPanel);
        appWindowPanel.setLayout(appWindowPanelLayout);
        appWindowPanelLayout.setHorizontalGroup(
                appWindowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 745, Short.MAX_VALUE)
        );
        appWindowPanelLayout.setVerticalGroup(
                appWindowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 559, Short.MAX_VALUE)
        );

        fileMenuItem.setText("File");
        fileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuItemActionPerformed(evt);
            }
        });

        exitSubMenuItem.setText("Exit");
        exitSubMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitSubMenuItemActionPerformed(evt);
            }
        });
        fileMenuItem.add(exitSubMenuItem);

        topMenuBar.add(fileMenuItem);

        editMenuItem.setText("Edit");
        editMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMenuItemActionPerformed(evt);
            }
        });

        settingsSubMenuItem.setText("Settings");
        settingsSubMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsSubMenuItemActionPerformed(evt);
            }
        });
        editMenuItem.add(settingsSubMenuItem);

        topMenuBar.add(editMenuItem);

        setJMenuBar(topMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(appWindowPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(appWindowPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    private void fileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void editMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void settingsSubMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        SettingsWindowDialog settings = new SettingsWindowDialog(this, true);
    }

    private void exitSubMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        WindowManager.closeAppWindow();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppWindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppWindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppWindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppWindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppWindowFrame().setVisible(true);
            }
        });
    }

    public void addToMainPanel(JComponent newComponent, boolean resizable)
    {
        GridBagConstraints addConstraints = new GridBagConstraints();
        if(resizable)
        {
            addConstraints.fill = GridBagConstraints.BOTH;
            addConstraints.weightx = 1;
            addConstraints.weighty = 1;
        }
        appWindowPanel.add(newComponent, addConstraints);
        appWindowPanel.repaint();
        appWindowPanel.revalidate();
    }

    public JPanel getAppWindowPanel() {
        return appWindowPanel;
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel appWindowPanel;
    private javax.swing.JMenu editMenuItem;
    private javax.swing.JMenuItem exitSubMenuItem;
    private javax.swing.JMenu fileMenuItem;
    private javax.swing.JMenuItem settingsSubMenuItem;
    private javax.swing.JMenuBar topMenuBar;
    // End of variables declaration
}