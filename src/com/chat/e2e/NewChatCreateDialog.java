/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.chat.e2e;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Soubhik
 */
public class NewChatCreateDialog extends javax.swing.JDialog {

    ChatMainPanel parentPanel;

    /**
     * Creates new form NewChatCreateDialog
     */
    public NewChatCreateDialog(java.awt.Frame parent, boolean modal, ChatMainPanel parentPanel) {
        super(parent, modal);
        initComponents();
        this.parentPanel = parentPanel;
        setTitle("Create chat");
        setLocationRelativeTo(parent);

        groupChatButton.setEnabled(false);

        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        chatTypeButtonGroup = new javax.swing.ButtonGroup();
        createChatLabel = new javax.swing.JLabel();
        selectTypeLabel = new javax.swing.JLabel();
        personalChatButton = new javax.swing.JRadioButton();
        groupChatButton = new javax.swing.JRadioButton();
        accountIdLabel = new javax.swing.JLabel();
        accountIdField = new javax.swing.JTextField();
        savedUsersButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        createChatButton = new javax.swing.JButton();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
            }
        });

        createChatLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        createChatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        createChatLabel.setText("Create New Chat");

        selectTypeLabel.setText("Select the type of chat:");

        personalChatButton.setSelected(true);
        personalChatButton.setText("Personal Chat");
        personalChatButton.setToolTipText("Chat between 2 users");
        personalChatButton.setFocusPainted(false);
        personalChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                personalChatButtonActionPerformed(evt);
            }
        });

        groupChatButton.setText("Group Chat (coming soon)");
        groupChatButton.setToolTipText("Chat between more than 2 users");
        groupChatButton.setFocusPainted(false);
        groupChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupChatButtonActionPerformed(evt);
            }
        });
        //groupChatButton.setEnabled(false);

        chatTypeButtonGroup.add(personalChatButton);
        chatTypeButtonGroup.add(groupChatButton);

        accountIdLabel.setText("Enter the account ID of other participant:");

        accountIdField.setToolTipText("Enter 16-digit account ID");
        accountIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                accountIdFieldActionPerformed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                accountIdFieldActionPerformed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                accountIdFieldActionPerformed();
            }
        });

        savedUsersButton.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(NewChatCreateDialog.class.getClassLoader().getResource("contact-book (1).png")))); // NOI18N
        savedUsersButton.setToolTipText("Choose from saved users");
        savedUsersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedUsersButtonActionPerformed(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        createChatButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        createChatButton.setText("Create Chat");
        createChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createChatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(createChatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(selectTypeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(accountIdLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                                        .addComponent(personalChatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(groupChatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(accountIdField)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(savedUsersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(createChatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(createChatLabel)
                                .addGap(18, 18, 18)
                                .addComponent(selectTypeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(personalChatButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(groupChatButton)
                                .addGap(18, 18, 18)
                                .addComponent(accountIdLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(accountIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(savedUsersButton))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(createChatButton))
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void personalChatButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if(personalChatButton.isSelected())
        {
            accountIdLabel.setText("Enter the account ID of other participant:");
            accountIdField.setToolTipText("Enter 16-digit account ID");
        }
    }

    private void groupChatButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if(groupChatButton.isSelected())
        {
            accountIdLabel.setText("Enter the comma-separated list of account IDs of other participants:");
            accountIdField.setToolTipText("Enter 16-digit account IDs separated by commas");
        }
    }

    private void accountIdFieldActionPerformed() {
        // TODO add your handling code here:
        accountIdField.setBackground((new JTextField()).getBackground());
    }

    private void savedUsersButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String[][] savedUsers = DatabaseManager.makeQuery("select display_name, account_id from savedUsers;");
        if(savedUsers != null && savedUsers.length > 0) {
            if(personalChatButton.isSelected())
                new SelectFromSavedUsersDialog(this, true, savedUsers, SelectFromSavedUsersDialog.SINGLE_SELECTION_MODE);
            else if(groupChatButton.isSelected())
                new SelectFromSavedUsersDialog(this, true, savedUsers, SelectFromSavedUsersDialog.MULTI_SELECTION_MODE);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "No saved users were found.", "No saved users", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        closeWindow();
    }

    private void createChatButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //  set busy before accessing network routine to create the new chat
        startCreateChatButtonLoadingAnimation();
        accountIdField.setBackground((new JTextField()).getBackground());

        SwingWorker<Void, Void> createChatWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if(personalChatButton.isSelected())
                {
                    boolean errorPresent = false;
                    String accountID = accountIdField.getText();
                    if(accountID.length() != 16)
                    {
                        errorPresent = true;
                    }
                    for(int i = 0; !errorPresent && i < accountID.length(); i++)
                    {
                        if(((int)accountID.charAt(i)) < 48 || ((int)accountID.charAt(i)) > 57)
                        {
                            errorPresent = true;
                        }
                    }
                    if(errorPresent)
                    {
                        accountIdField.setBackground(new Color(255, 94, 116, 131));
                        JOptionPane.showMessageDialog(getSelfReference(), "Please enter a single valid account ID of length 16\nand containing only digits.", "Invalid account ID", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                }
                else
                {
                    boolean errorPresent = false;
                    String[] accountIDs = accountIdField.getText().split(",");
                    if(accountIDs.length == 0)
                    {
                        errorPresent = true;
                    }
                    for(int i = 0; !errorPresent && i < accountIDs.length; i++)
                    {
                        String accountID = accountIDs[i];
                        if(accountID.length() != 16)
                        {
                            errorPresent = true;
                        }
                        for(int j = 0; !errorPresent && j < accountID.length(); j++)
                        {
                            if(((int)accountID.charAt(j)) < 48 || ((int)accountID.charAt(j)) > 57)
                            {
                                errorPresent = true;
                            }
                        }
                    }
                    if(errorPresent)
                    {
                        accountIdField.setBackground(new Color(255, 94, 116, 131));
                        JOptionPane.showMessageDialog(getSelfReference(), "Please enter a valid list of account IDs\neach of length 16 containing only digits\nand separated by commas.", "Invalid account ID list", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                }

                if(!NetworkManager.isConnected())
                {
                    JOptionPane.showMessageDialog(getSelfReference(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                            "Server unavailable", JOptionPane.ERROR_MESSAGE);
                    stopCreateChatButtonLoadingAnimation();
                    return null;
                }
                if(!NetworkManager.isLoggedIn())
                {
                    JOptionPane.showMessageDialog(getSelfReference(), "User is currently not logged in with the server.\nPlease try again a little later.",
                            "User not logged in", JOptionPane.ERROR_MESSAGE);
                    stopCreateChatButtonLoadingAnimation();
                    return null;
                }
                if(NetworkManager.isBusy())
                {
                    JOptionPane.showMessageDialog(getSelfReference(), "The server is currently busy.\nPlease try again later.",
                            "Server busy", JOptionPane.ERROR_MESSAGE);
                    stopCreateChatButtonLoadingAnimation();
                    return null;
                }

                if(personalChatButton.isSelected())
                {
                    NetworkManager.setBusy(true);
                    Boolean success = NetworkManager.checkAccountIdValid(accountIdField.getText());
                    if(success == null)
                    {
                        NetworkManager.setBusy(false);
                        JOptionPane.showMessageDialog(getSelfReference(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                                "Server unavailable", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                    if(!success)
                    {
                        NetworkManager.setBusy(false);
                        accountIdField.setBackground(new Color(255, 94, 116, 131));
                        JOptionPane.showMessageDialog(getSelfReference(), "The given account ID " + accountIdField.getText() + " is invalid.", "Invalid account ID", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                }
                else
                {
                    NetworkManager.setBusy(true);
                    String[] accountIDs = accountIdField.getText().split(",");
                    ArrayList<String> invalidAccountIDs = new ArrayList<>();
                    for(int i = 0; i < accountIDs.length; i++) {
                        Boolean success = NetworkManager.checkAccountIdValid(accountIDs[i]);
                        if (success == null) {
                            NetworkManager.setBusy(false);
                            JOptionPane.showMessageDialog(getSelfReference(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                                    "Server unavailable", JOptionPane.ERROR_MESSAGE);
                            stopCreateChatButtonLoadingAnimation();
                            return null;
                        }
                        if(!success)
                        {
                            invalidAccountIDs.add(accountIDs[i]);
                        }
                    }
                    if (!invalidAccountIDs.isEmpty())
                    {
                        NetworkManager.setBusy(false);
                        accountIdField.setBackground(new Color(255, 94, 116, 131));
                        String invalidAccountIDsList = "";
                        for(int i = 0; i < invalidAccountIDs.size(); i++)
                        {
                            invalidAccountIDsList = invalidAccountIDsList + "\n" + invalidAccountIDs.get(i);
                        }
                        JOptionPane.showMessageDialog(getSelfReference(), "The given account IDs below are invalid -" + invalidAccountIDsList, "Invalid account IDs", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                }

                if(personalChatButton.isSelected())
                {
                    Boolean success = NetworkManager.checkIfNewPersonalChatAllowed(accountIdField.getText());
                    if(success == null)
                    {
                        NetworkManager.setBusy(false);
                        JOptionPane.showMessageDialog(getSelfReference(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                                "Server unavailable", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                    if(!success)
                    {
                        NetworkManager.setBusy(false);
                        accountIdField.setBackground(new Color(255, 94, 116, 131));
                        JOptionPane.showMessageDialog(getSelfReference(), "The given account ID " + accountIdField.getText() + " has turned off new personal chat requests.\nNew personal chat cannot be created with this user.", "New chat not allowed", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                }
                else
                {
                    String[] accountIDs = accountIdField.getText().split(",");
                    ArrayList<String> groupChatDisabledAccountIDs = new ArrayList<>();
                    for(int i = 0; i < accountIDs.length; i++) {
                        Boolean success = NetworkManager.checkIfNewGroupChatAllowed(accountIDs[i]);
                        if (success == null) {
                            NetworkManager.setBusy(false);
                            JOptionPane.showMessageDialog(getSelfReference(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                                    "Server unavailable", JOptionPane.ERROR_MESSAGE);
                            stopCreateChatButtonLoadingAnimation();
                            return null;
                        }
                        if(!success)
                        {
                            groupChatDisabledAccountIDs.add(accountIDs[i]);
                        }
                    }
                    if (!groupChatDisabledAccountIDs.isEmpty())
                    {
                        NetworkManager.setBusy(false);
                        accountIdField.setBackground(new Color(255, 94, 116, 131));
                        String groupChatDisabledAccountIDsList = "";
                        for(int i = 0; i < groupChatDisabledAccountIDs.size(); i++)
                        {
                            groupChatDisabledAccountIDsList = groupChatDisabledAccountIDsList + "\n" + groupChatDisabledAccountIDs.get(i);
                        }
                        JOptionPane.showMessageDialog(getSelfReference(), "The given account IDs below have turned off new group chat requests -" + groupChatDisabledAccountIDsList + "\nNew group chat cannot be created with these users in them.", "New chat not allowed", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                }

                if(personalChatButton.isSelected())
                {
                    Boolean success = NetworkManager.createNewPersonalChat("PERSONAL", accountIdField.getText());
                    NetworkManager.setBusy(false);
                    if(success == null)
                    {
                        JOptionPane.showMessageDialog(getSelfReference(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                                "Server unavailable", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }
                    if(!success)
                    {
                        JOptionPane.showMessageDialog(getSelfReference(), "The server faced an internal error.\nPlease try again later.",
                                "Server error", JOptionPane.ERROR_MESSAGE);
                        stopCreateChatButtonLoadingAnimation();
                        return null;
                    }

                    parentPanel.updateFromDB();
                    JOptionPane.showMessageDialog(getSelfReference(), "The chat has been successfully created.", "Chat created", JOptionPane.INFORMATION_MESSAGE);
                    getSelfReference().dispose();
                    return null;
                }
                else
                {
                    //TODO: Add code for creating new group chat by calling network manager routine
                    NetworkManager.setBusy(false);
                    JOptionPane.showMessageDialog(getSelfReference(), "group chat create test message", "gc test", JOptionPane.INFORMATION_MESSAGE);
                    stopCreateChatButtonLoadingAnimation();
                    return null;
                }


                //return null;
            }
        };
        createChatWorker.execute();
    }

    private NewChatCreateDialog getSelfReference()
    {
        return this;
    }

    private void startCreateChatButtonLoadingAnimation()
    {
        personalChatButton.setEnabled(false);
        //groupChatButton.setEnabled(false);
        accountIdField.setEnabled(false);
        savedUsersButton.setEnabled(false);
        cancelButton.setEnabled(false);
        ImageIcon createChatButtonIcon = new ImageIcon(Objects.requireNonNull(NewChatCreateDialog.class.getClassLoader().getResource("Spinner-1s-20px (1).gif")));
        createChatButton.setIcon(createChatButtonIcon);
        createChatButtonIcon.setImageObserver(createChatButton);
        createChatButton.setEnabled(false);
    }

    private void stopCreateChatButtonLoadingAnimation()
    {
        createChatButton.setIcon(null);
        createChatButton.setEnabled(true);
        personalChatButton.setEnabled(true);
        //groupChatButton.setEnabled(true);
        accountIdField.setEnabled(true);
        savedUsersButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(NewChatCreateDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(NewChatCreateDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(NewChatCreateDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(NewChatCreateDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                NewChatCreateDialog dialog = new NewChatCreateDialog(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    public void setAccountIdFieldText(String newText)
    {
        accountIdField.setText(newText);
    }

    private void closeWindow()
    {
        if(createChatButton.isEnabled())
            dispose();
    }

    // Variables declaration - do not modify
    private javax.swing.JTextField accountIdField;
    private javax.swing.JLabel accountIdLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.ButtonGroup chatTypeButtonGroup;
    private javax.swing.JButton createChatButton;
    private javax.swing.JLabel createChatLabel;
    private javax.swing.JRadioButton groupChatButton;
    private javax.swing.JRadioButton personalChatButton;
    private javax.swing.JButton savedUsersButton;
    private javax.swing.JLabel selectTypeLabel;
    // End of variables declaration
}