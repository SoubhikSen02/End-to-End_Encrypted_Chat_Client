/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.chat.e2e;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

/**
 *
 * @author Soubhik
 */
public class ChatMessagesListInfoTextComponent extends javax.swing.JPanel {

    private String messageID;

    public String getMessageID()
    {
        return messageID;
    }

    /**
     * Creates new form ChatMessagesListInfoTextComponent
     */
    public ChatMessagesListInfoTextComponent(String ID) {
        initComponents();
        infoTextLabel.setContentType("text/html");
        messageID = ID;
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        infoTextLabel = new javax.swing.JTextPane();

        infoTextLabel.setEditable(false);
        infoTextLabel.setFocusable(false);
        infoTextLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        infoTextLabel.setAutoscrolls(false);
        infoTextLabel.setText("placeholderInfoText");
        infoTextLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        infoTextLabel.setMargin(new java.awt.Insets(2, 2, 2, 2));
        infoTextLabel.setOpaque(false);
        infoTextLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                infoTextLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                infoTextLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                infoTextLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                infoTextLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                infoTextLabelMouseReleased(evt);
            }
        });

        infoTextLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e))
                {
                    showMoreOptionsPopup(e);
                }
            }
        });
        infoTextLabel.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try
                    {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    }
                    catch(Exception ex)
                    {
                        //System.out.println(ex);
                    }
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(infoTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(infoTextLabel)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>

    private void showMoreOptionsPopup(MouseEvent e)
    {
        moreOptionsPopup = new JPopupMenu();

        JMenuItem copyOption = new JMenuItem("Copy");
        copyOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(infoTextLabel.getText().replaceAll("<.*?>", "")), null);

                ChatMainPanel parent = WindowManager.getChatPanelReference();
                if(parent != null && parent.getActiveChatComponentReference() != null)
                {
                    String chatID = parent.getActiveChatComponentReference().getChatID();
                    String[][] message = DatabaseManager.makeQuery("select message_content from chat" + chatID + " where message_id = " + messageID + ";", null);
                    if(message != null && message.length > 0)
                    {
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(message[0][0]), null);
                    }
                }
            }
        });
        moreOptionsPopup.add(copyOption);

        JMenuItem deleteOption = new JMenuItem("Delete");
        deleteOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(Main.getMainFrame(), "Are you sure you want to delete this message?\nIt will only be deleted for you.\nOthers will still be able to see this message.", "Delete message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(response != JOptionPane.YES_OPTION)
                    return;
                ChatMainPanel chatPanelReference = WindowManager.getChatPanelReference();
                if(chatPanelReference != null)
                {
                    chatPanelReference.deleteParticularMessageInActiveChat(messageID);
                }
            }
        });
        moreOptionsPopup.add(deleteOption);

        moreOptionsPopup.show(infoTextLabel, e.getX(), e.getY());
    }

    private void infoTextLabelMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void infoTextLabelMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void infoTextLabelMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void infoTextLabelMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void infoTextLabelMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private String processAndHighlightLinks(String originalMessage)
    {
        String messageTextOnlyWithoutHtml = originalMessage.replaceAll("<.*?>", "");
        //System.out.println(originalMessage + "\n" + messageTextOnlyWithoutHtml);
        String[] messageParts = messageTextOnlyWithoutHtml.split(" ");
        //System.out.println(Arrays.toString(messageParts));
        boolean[] isLink = new boolean[messageParts.length];
        for(int i = 0; i < messageParts.length; i++)
        {
            try
            {
                URL url = new URL(messageParts[i]);
                isLink[i] = true;
            }
            catch(Exception e)
            {
                isLink[i] = false;
            }
        }
        //System.out.println(Arrays.toString(isLink));
        StringBuilder newMessage = new StringBuilder(originalMessage);
        int currentLastEditedIndex = 0;
        for(int i = 0; i < messageParts.length; i++)
        {
            if(isLink[i])
            {
                int initialStartingIndex = newMessage.indexOf(messageParts[i], currentLastEditedIndex);
                int finalEndingIndex = initialStartingIndex + messageParts[i].length();
                String startingHtmlTag = "<a href=\"" + messageParts[i] + "\">";
                String endingHtmlTag = "</a>";
                newMessage.insert(finalEndingIndex, endingHtmlTag);
                newMessage.insert(initialStartingIndex, startingHtmlTag);
                currentLastEditedIndex = initialStartingIndex + messageParts[i].length() + startingHtmlTag.length() + endingHtmlTag.length();
            }
        }
        return newMessage.toString();
    }

    private String processIntoMultipleLines(String originalMessage)
    {
        StringBuilder originalMessageWithHtmlTags = new StringBuilder(originalMessage);
        originalMessageWithHtmlTags.insert(0, "<html><center><b>");
        originalMessageWithHtmlTags.insert(originalMessageWithHtmlTags.length(), "</b></center></html>");
        originalMessage = originalMessageWithHtmlTags.toString();


        // precalculate approximate width of message bubble based on the following data -
        // ratio of message bubble width          message scroll list width          message bubble width          size of window(approx)
        // to message scroll list width
        //           0.95                                      504                           478                         800*600
        //           0.96                                      662                           636
        //           0.97                                      890                           864
        //           0.98                                      1085                          1059
        //           0.98                                      1385                          1359
        //           0.98                                      1640                          1614               fullscreen-1920xless than 1080
        // max possible difference between window width and scroll list width = 300 (approx safe distance)
        // safe ratio for message bubble width from scroll list width = 0.95 (at most)
        // results -
        // actual message scroll list width                  estimated message bubble width                 size of window(approx)
        //             504                                                475                                      800*600
        //             668                                                630
        //             891                                                842
        //             1081                                               1023
        //             1384                                               1311
        //             1640                                               1554                          fullscreen-1920xless than 1080
        // estimation gets closer to actual width as scroll list width increases
        // I could consider querying the scroll list width and then setting the width instead of using 0.95 everytime
        // but I think it is unnecessary as it looks decent in the UI and does what it needs to do
        // the main point of precalculating the width is to set the text as the message list is being created, and not after it is created and rendered
        // to get the actual width, rendering is required first, so pre-calculation is needed here to do the text setting before rendering
        // actual ratio is set to 0.9 instead as 0.95 is too close and sometimes doesn't work properly and looks bad in general
        int defaultWidthOfMessageField = (int)((Main.getMainFrame().getWidth() - 300) * 0.9); //infoTextLabel.getWidth();
        //System.out.println(defaultWidthOfMessageField);

        FontMetrics fontMetrics = getFontMetrics(getFont());
        String messageTextOnlyWithoutHtml = originalMessage.replaceAll("<.*?>", "");
        int messageWidth = fontMetrics.stringWidth(messageTextOnlyWithoutHtml);

        if(messageWidth <= defaultWidthOfMessageField)
        {
            return originalMessage;
        }
        else {
            StringBuilder newMessage = new StringBuilder();
            StringBuilder currentLineText = new StringBuilder();
            //System.out.println(originalMessage.length());
            for (int i = 0; i < originalMessage.length(); i++) {
                //System.out.println(i);
                if(originalMessage.charAt(i) == '<')
                {
                    while(originalMessage.charAt(i) != '>')
                    {
                        newMessage.append(originalMessage.charAt(i));
                        i++;
                    }
                    newMessage.append(originalMessage.charAt(i));
                    if(newMessage.length() >= 4 && newMessage.lastIndexOf("<br>") != -1 && newMessage.lastIndexOf("<br>") == newMessage.length() - 4)
                    {
                        currentLineText = new StringBuilder();
                    }
                }
                else
                {
                    currentLineText.append(originalMessage.charAt(i));
                    if (fontMetrics.stringWidth(currentLineText.toString()) > defaultWidthOfMessageField) {
                        i--;
                        newMessage.append("<br>");
                        currentLineText = new StringBuilder();
                    }
                    else {
                        newMessage.append(originalMessage.charAt(i));
                    }
                }
            }
            return newMessage.toString();
        }
    }

    public void setMessage(String message)
    {
        infoTextLabel.setText(processIntoMultipleLines(processAndHighlightLinks(message)));
        //System.out.println(infoTextLabel.getText());
    }

    // Variables declaration - do not modify
    private javax.swing.JTextPane infoTextLabel;
    private JPopupMenu moreOptionsPopup;
    // End of variables declaration
}
