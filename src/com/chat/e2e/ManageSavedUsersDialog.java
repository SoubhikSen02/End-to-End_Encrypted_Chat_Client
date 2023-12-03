/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.chat.e2e;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Soubhik
 */
public class ManageSavedUsersDialog extends javax.swing.JDialog {

    /**
     * Creates new form ManageSavedUsersDialog
     */
    public ManageSavedUsersDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();

        savedUsersListScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        savedUsersListScrollPanel.getVerticalScrollBar().setUnitIncrement(10);

        updateListFromDB();

        setTitle("Saved users");
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private JDialog getSelfReference()
    {
        return this;
    }

    JPanel insideContainer;
    JTextField newNameField;
    JTextField newAccountIdField;

    private void updateListFromDB()
    {
        String[][] savedUsersList = DatabaseManager.makeQuery("select display_name, account_id from savedUsers;");
        if(savedUsersList == null)
        {
            savedUsersList = new String[0][0];
        }

        JPanel outsideContainer = new JPanel(new BorderLayout(1, 1));
        insideContainer = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.RELATIVE;

        JLabel nameHeader = new JLabel("Name");
        nameHeader.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        nameHeader.setHorizontalAlignment(JLabel.CENTER);
        insideContainer.add(nameHeader, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;

        JLabel accIdHeader = new JLabel("Account ID");
        accIdHeader.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        accIdHeader.setHorizontalAlignment(JLabel.CENTER);
        insideContainer.add(accIdHeader, constraints);

        for(int i = 0; i < savedUsersList.length; i++)
        {
            constraints.gridwidth = GridBagConstraints.RELATIVE;

            JLabel nameField = new JLabel(savedUsersList[i][0]);
            nameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            nameField.setHorizontalAlignment(JLabel.LEFT);
            insideContainer.add(nameField, constraints);

            constraints.gridwidth = GridBagConstraints.REMAINDER;

            JLabel accIdField = new JLabel(savedUsersList[i][1]);
            accIdField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            accIdField.setHorizontalAlignment(JLabel.CENTER);
            insideContainer.add(accIdField, constraints);
        }

        outsideContainer.add(insideContainer, BorderLayout.NORTH);
        savedUsersListScrollPanel.getViewport().setView(outsideContainer);

        newNameField = new JTextField();
        newAccountIdField = new JTextField();

        newNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                newNameFieldChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newNameFieldChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                newNameFieldChanged();
            }
        });

        newAccountIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                newAccountIdFieldChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newAccountIdFieldChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                newAccountIdFieldChanged();
            }
        });
    }

    private void newNameFieldChanged()
    {
        newNameField.setBackground((new JTextField()).getBackground());
    }

    private void newAccountIdFieldChanged()
    {
        newAccountIdField.setBackground((new JTextField()).getBackground());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        savedUsersListScrollPanel = new javax.swing.JScrollPane();
        cancelButton = new javax.swing.JButton();
        addNewUserButton = new javax.swing.JButton();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(addNewUserButton.getText().equals("Add new user"))
                {
                    dispose();
                    //Added safe return after dispose for reasons given somewhere else in the code
                    return;
                }
                else if(addNewUserButton.getText().equals("Confirm and add"))
                {
                    if(newNameField.getText().isEmpty() && newAccountIdField.getText().isEmpty())
                    {
                        dispose();
                        //Added safe return after dispose for reasons given somewhere else in the code
                        return;
                    }
                    else
                    {
                        //System.out.println("Entered non empty discard of cancel");
                        int response = JOptionPane.showConfirmDialog(getSelfReference(), "Do you want to stop adding new user and close the window?", "Discard and close", JOptionPane.YES_NO_OPTION);
                        if(response == JOptionPane.NO_OPTION)
                        {
                            return;
                        }
                        else if(response == JOptionPane.YES_OPTION)
                        {
                            dispose();
                            //Added safe return after dispose for reasons given somewhere else in the code
                            return;
                        }
                    }
                }
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        addNewUserButton.setText("Add new user");
        addNewUserButton.setFocusPainted(false);
        addNewUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewUserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(savedUsersListScrollPanel)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addNewUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(savedUsersListScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(addNewUserButton)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(cancelButton)
                                                .addContainerGap())))
        );

        pack();
    }// </editor-fold>



    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if(addNewUserButton.getText().equals("Add new user"))
        {
            //System.out.println("Entered dispose of cancel");
            dispose();
            //An explicit return is needed here because for some weird reason, after this particular if block is executed and
            //the window is disposed, the else if block below also gets executed. Since the below else if is also executed,
            //an additional JOptionPane may show up after dispose depending on the condition of the JTextFields.
            //The reason why the else if is also executed after the if block is unknown, it absolutely shouldn't happen,
            //because only the first block matching the condition executes in an if else if block chain, but this particular
            //instance of code somehow bypasses the fundamental rules of the language.
            //It's honestly the most insane thing I have seen. Makes absolutely zero sense. Not even with the wildest theories.
            //Either I have gone dumb and making a huge mistake here and not even noticing it, or this is crazy for real.
            return;
        }
        else if(addNewUserButton.getText().equals("Confirm and add"));
        {
            //System.out.println("Entered discard of cancel");
            //TODO: Add code for discarding new input boxes
            if(newNameField.getText().isEmpty() && newAccountIdField.getText().isEmpty())
            {
                //System.out.println("Entered empty discard of cancel");
                insideContainer.remove(newNameField);
                insideContainer.remove(newAccountIdField);
                addNewUserButton.setText("Add new user");
            }
            else
            {
                //System.out.println("Entered non empty discard of cancel");
                int response = JOptionPane.showConfirmDialog(this, "Do you want to discard details and cancel adding user?", "Discard and cancel", JOptionPane.YES_NO_OPTION);
                if(response == JOptionPane.NO_OPTION)
                {
                    return;
                }
                else if(response == JOptionPane.YES_OPTION)
                {
                    insideContainer.remove(newNameField);
                    insideContainer.remove(newAccountIdField);
                    addNewUserButton.setText("Add new user");
                }
            }
        }
    }

    private void addNewUserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //System.out.println("test");
        // TODO add your handling code here:
        if(addNewUserButton.getText().equals("Add new user"))
        {
            //TODO: Add code for adding input boxes
            addNewUserButton.setText("Confirm and add");

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridwidth = GridBagConstraints.RELATIVE;

            newNameField.setText("");
            newNameField.setBackground((new JTextField()).getBackground());
            insideContainer.add(newNameField, constraints);

            constraints.gridwidth = GridBagConstraints.REMAINDER;

            newAccountIdField.setText("");
            newAccountIdField.setBackground((new JTextField()).getBackground());
            insideContainer.add(newAccountIdField, constraints);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int difference = insideContainer.getSize().height - savedUsersListScrollPanel.getViewport().getExtentSize().height;
                    if(difference > 0)
                    {
                        savedUsersListScrollPanel.getViewport().setViewPosition(new Point(0, difference));
                    }
                    newNameField.grabFocus();
                }
            });
        }
        else if(addNewUserButton.getText().equals("Confirm and add"))
        {
            //TODO: Add code for adding input to database
            newNameField.setBackground((new JTextField()).getBackground());
            newAccountIdField.setBackground((new JTextField()).getBackground());

            String errorMessages = "";
            String name = newNameField.getText();
            String accountID = newAccountIdField.getText();
            if(name.isEmpty())
            {
                errorMessages = errorMessages + "\nName cannot be empty.";
                newNameField.setBackground(new Color(255, 94, 116, 131));
            }
            if(accountID.isEmpty())
            {
                errorMessages = errorMessages + "\nAccount ID cannot be empty.";
                newAccountIdField.setBackground(new Color(255, 94, 116, 131));
            }
            if(accountID.length() != 16)
            {
                errorMessages = errorMessages + "\nAccount ID must be exactly 16 digits in length.";
                newAccountIdField.setBackground(new Color(255, 94, 116, 131));

            }
            for(int i = 0; i < accountID.length(); i++)
            {
                if(accountID.charAt(i) < 48 || accountID.charAt(i) > 57)
                {
                    errorMessages = errorMessages + "\nAccount ID must contain only digits.";
                    newAccountIdField.setBackground(new Color(255, 94, 116, 131));
                    break;
                }
            }
            if(!errorMessages.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please correct the following errors to proceed -" + errorMessages, "Invalid input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DatabaseManager.makeUpdate("insert into savedUsers values('" + accountID + "', '" + name + "');");
            insideContainer.remove(newNameField);
            insideContainer.remove(newAccountIdField);
            addNewUserButton.setText("Add new user");

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridwidth = GridBagConstraints.RELATIVE;

            JLabel newNameLabel = new JLabel(name);
            newNameLabel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            newNameLabel.setHorizontalAlignment(JLabel.LEFT);
            insideContainer.add(newNameLabel, constraints);

            constraints.gridwidth = GridBagConstraints.REMAINDER;

            JLabel newAccountIdLabel = new JLabel(accountID);
            newAccountIdLabel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            newAccountIdLabel.setHorizontalAlignment(JLabel.CENTER);
            insideContainer.add(newAccountIdLabel, constraints);

            JOptionPane.showMessageDialog(this, "New user has been successfully added.", "User added", JOptionPane.INFORMATION_MESSAGE);
        }
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
//            java.util.logging.Logger.getLogger(ManageSavedUsersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ManageSavedUsersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ManageSavedUsersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ManageSavedUsersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ManageSavedUsersDialog dialog = new ManageSavedUsersDialog(new javax.swing.JFrame(), true);
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

    // Variables declaration - do not modify
    private javax.swing.JButton addNewUserButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane savedUsersListScrollPanel;
    // End of variables declaration
}