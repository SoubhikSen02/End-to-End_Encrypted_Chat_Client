/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.chat.e2e;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Timer;


/**
 *
 * @author Soubhik
 */
public class ChatMainPanel extends javax.swing.JPanel {

    final public static int KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_LOADING_OLD_MESSAGES = 1;
    final public static int KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON = 2;
    //final public static int KEEP_CHAT_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING = 3;
    //final public static int


    private SwingWorker<Void, Void> networkStatusUpdater;
    public SwingWorker<Void, Void> getNetworkStatusUpdaterThread()
    {
        return networkStatusUpdater;
    }



    private SwingWorker<Void, Void> reloginThread;
    public SwingWorker<Void, Void> getReloginThread()
    {
        return reloginThread;
    }



    private SwingWorker<Void, Void> newChatAndMessageReceiverThread;
    public SwingWorker<Void,Void> getNewChatAndMessageReceiverThread()
    {
        return newChatAndMessageReceiverThread;
    }




    private SwingWorker<Void, Void> messageReadReceiptsUpdatorThread;
    public SwingWorker<Void, Void> getMessageReadReceiptsUpdatorThread()
    {
        return messageReadReceiptsUpdatorThread;
    }
    private ArrayList<String[]> readReceiptsToBeSent = new ArrayList<>();



    private SwingWorker<Void, Void> newMessageSenderThread;
    public SwingWorker<Void, Void> getNewMessageSenderThread()
    {
        return newMessageSenderThread;
    }
    private ArrayList<String[]> messagesToBeSentList = new ArrayList<>();



    private Timer periodicPanelUpdatorTimer;
    private TimerTask chatPanelUpdateTask;
    public Timer getPeriodicPanelUpdatorTimer()
    {
        return periodicPanelUpdatorTimer;
    }



    private Timer otherThreadsCrashHandler;
    private TimerTask otherThreadsRestartTask;
    public Timer getOtherThreadsCrashHandler()
    {
        return otherThreadsCrashHandler;
    }



    private Timer unknownInfoFinderTimer;
    private TimerTask unknownInfoCheckAndFindTask;
    public Timer getUnknownInfoFinderTimer()
    {
        return unknownInfoFinderTimer;
    }



    private HashMap<String, Color> currentChatParticipantsColorsMapping = new HashMap<>();



    //private boolean



    private ArrayList<ChatListComponentPanel> chatListComponents = new ArrayList<>();
    private ChatListComponentPanel activeChatComponentReference = null;
    private long numberOfLoadedMessagesInActiveChat = 0;
    private ChangeListener loadOlderMessagesEventListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            //System.out.println("Change listener called");
            JScrollBar chatMessagesListScrollBar = chatMessagesListScrollPanel.getVerticalScrollBar();
            if(activeChatComponentReference != null && chatMessagesListScrollBar.getMaximum() - chatMessagesListScrollBar.getModel().getExtent() > 0 && chatMessagesListScrollBar.getValue() == 0)
            {
                if(numberOfLoadedMessagesInActiveChat < Long.parseLong(ConfigManager.getInitialNumberOfMessagesToLoad()))
                {
                    return;
                }
                loadOlderMessages();
            }
        }
    };
    int messageListCurrentScrollBarPositionFromBottom;
    int messageListCurrentScrollBarPositionFromTop;
    boolean forceScrollDownMessageList;


    public ChatListComponentPanel getActiveChatComponentReference()
    {
        return activeChatComponentReference;
    }



    /**
     * Creates new form chatFullScreenPanel
     */
    public ChatMainPanel() {
        initComponents();

        networkStatusUpdater = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(5000);
                while(true)
                {
                    if(NetworkManager.isConnected() && NetworkManager.isLoggedIn())
                    {
                        appStatusTextLabel.setText("Online");
                        appStatusColorLabel.setBackground(new Color(0, 255, 0));
                    }
                    else if(NetworkManager.isConnected())
                    {
                        appStatusTextLabel.setText("Logging in");
                        appStatusColorLabel.setBackground(new Color(253, 212, 79));
                    }
                    else
                    {
                        appStatusTextLabel.setText("Offline");
                        appStatusColorLabel.setBackground(new Color(255, 0, 0));
                    }
                    Thread.sleep((Integer.parseInt(ConfigManager.getConnectionProbeTimePeriod()) * 1000L) > 5000? 5000:(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000));
                }
                //return null;
            }
        };
        networkStatusUpdater.execute();

        reloginThread = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try{
                    Thread.sleep(3000);
                    while(true)
                    {
                        if(NetworkManager.isConnected())
                        {
                            if(!NetworkManager.isLoggedIn())
                            {
                                if(!ConfigManager.getAccountSessionID().equals("null"))
                                {
                                    Boolean success = NetworkManager.loginToOldSession(ConfigManager.getAccountID(), ConfigManager.getAccountSessionID());
                                    if(success != null && !success)
                                    {
                                        JOptionPane.showMessageDialog(Main.getMainFrame(), "You have been force logged out due to invalid session ID.\nYou will be moved back to the main menu.\nPlease try logging in again.",
                                                "User logged out", JOptionPane.ERROR_MESSAGE);
                                        WindowManager.changeFromChatToSelection(true);
                                    }
                                }
                                else
                                {
                                    Boolean success = NetworkManager.reloginToTemporarySession(ConfigManager.getAccountID());
                                    if(success != null && !success)
                                    {
                                        JOptionPane.showMessageDialog(Main.getMainFrame(), "You have been force logged out due to invalid session ID.\nYou will be moved back to the main menu.\nPlease try logging in again.",
                                                "User logged out", JOptionPane.ERROR_MESSAGE);
                                        WindowManager.changeFromChatToSelection(true);
                                    }
                                }
                            }
                        }
                        Thread.sleep(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000L);
                    }
                }
                catch(Exception e)
                {

                }
                return null;
            }
        };
        reloginThread.execute();

        newChatAndMessageReceiverThread = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try{
                    Thread.sleep(3000);
                    while(true)
                    {
                        if(NetworkManager.isConnected())
                        {
                            if(NetworkManager.isLoggedIn())
                            {
                                Boolean success = NetworkManager.checkForNewChats();
                                if(success != null && success)
                                    updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);

                                success = NetworkManager.checkForChatInfoUpdates();
                                if(success != null && success)
                                    updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);

                                success = NetworkManager.checkForNewMessages();
                                if(success != null && success)
                                    updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);
                            }
                        }
                        Thread.sleep((Integer.parseInt(ConfigManager.getConnectionProbeTimePeriod()) * 1000L) > 3000? 3000:(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000));
                    }
                }
                catch(Exception e)
                {

                }
                return null;
            }
        };
        newChatAndMessageReceiverThread.execute();


        String[][] unsentReadReceipts = DatabaseManager.makeQuery("select chat_id, message_id from unsentReadReceipts;");
        if(unsentReadReceipts != null)
        {
            for(int i = 0; i < unsentReadReceipts.length; i++)
            {
                readReceiptsToBeSent.add(new String[]{unsentReadReceipts[i][0], unsentReadReceipts[i][1]});
            }
        }
        messageReadReceiptsUpdatorThread = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try
                {
                    Thread.sleep(5000);
                    while(true)
                    {
                        if(NetworkManager.isConnected())
                        {
                            if(NetworkManager.isLoggedIn())
                            {
                                Boolean success = NetworkManager.checkForNewReadReceipts();
                                if(success != null && success)
                                    updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);

                                while(!readReceiptsToBeSent.isEmpty())
                                {
                                    String chatID = readReceiptsToBeSent.get(0)[0];
                                    String messageID = readReceiptsToBeSent.get(0)[1];

                                    //disable read receipt 3 for group chats by not actually sending any such receipts to server
//                                    String[][] chatType = DatabaseManager.makeQuery("select chat_type from chat where chat_id = " + chatID + ";");
//                                    if(chatType == null || chatType.length == 0 || chatType[0][0].equals("GROUP"))
//                                    {
//                                        DatabaseManager.makeUpdate("delete from unsentReadReceipts where chat_id = " + chatID + " and message_id = " + messageID + ";");
//                                        readReceiptsToBeSent.remove(0);
//                                        continue;
//                                    }

                                    success = NetworkManager.sendNewReadReceipts(chatID, messageID);
                                    if(success == null || !success)
                                        break;
                                    else if(success)
                                    {
                                        DatabaseManager.makeUpdate("delete from unsentReadReceipts where chat_id = " + chatID + " and message_id = " + messageID + ";");
                                        readReceiptsToBeSent.remove(0);
                                    }
                                }
                            }
                        }
                        Thread.sleep((Integer.parseInt(ConfigManager.getConnectionProbeTimePeriod()) * 1000L) > 3000? 3000:(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000));
                    }
                }
                catch(Exception e)
                {

                }
                return null;
            }
        };
        messageReadReceiptsUpdatorThread.execute();

        periodicPanelUpdatorTimer = new Timer();
        LocalDate currentDate = LocalDate.now();
        LocalDate nextDayDate = currentDate.plusDays(1);
        chatPanelUpdateTask = new TimerTask() {
            @Override
            public void run() {
                updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);
            }
        };
        periodicPanelUpdatorTimer.scheduleAtFixedRate(chatPanelUpdateTask, Date.valueOf(nextDayDate), 86400000);


        unknownInfoFinderTimer = new Timer();
        unknownInfoCheckAndFindTask = new TimerTask() {
            @Override
            public void run() {
                String[][] unknownUsers = DatabaseManager.makeQuery("select * from unknownUsers;");
                if(unknownUsers == null || unknownUsers.length == 0)
                {
                    return;
                }
                boolean anyUpdates = false;
                for(int i = 0; i < unknownUsers.length; i++)
                {
                    Boolean success = NetworkManager.retrieveUnknownUserDisplayName(unknownUsers[i][0]);
                    if(success == null)
                        return;
                    if(!success)
                        continue;
                    anyUpdates = true;
                }
                if(anyUpdates)
                    updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);
            }
        };
        unknownInfoFinderTimer.schedule(unknownInfoCheckAndFindTask, 10000, 60000);

        String[][] chatList = DatabaseManager.makeQuery("select chat_id, chat_type, chat_name, chat_participants from chat order by last_message_timestamp desc;");
        if(chatList != null && chatList.length > 0)
        {
            for(int i = 0; i < chatList.length; i++)
            {
                ChatListComponentPanel chatListComponent = new ChatListComponentPanel(chatList[i][0], getSelfReference());
                chatListComponent.getChatNameLabel().setText(chatList[i][2]);
                chatListComponent.getChatNameLabel().setToolTipText(chatList[i][2]);
                String[][] chatInfo = DatabaseManager.makeQuery("select message_timestamp, message_content, from_account_id, sent_to_server from chat" + chatList[i][0] + " where message_timestamp = (select max(message_timestamp) from chat" + chatList[i][0] + ");");
                if(chatInfo != null && chatInfo.length == 1)
                {
                    //TODO: verify later how timestamp is stored and displayed and add required code to display them properly instead of the entire thing, most likely use Date.getTime()
                    Calendar calendarInstance = Calendar.getInstance();
                    calendarInstance.setTimeInMillis(Long.parseLong(chatInfo[0][0]));
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm a");
                    if(chatList[i][1].equals("PERSONAL"))
                    {
                        chatListComponent.getChatIconLabel().setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatListComponentPanel.class.getClassLoader().getResource("user.png"))));
                    }
                    else
                    {
                        chatListComponent.getChatIconLabel().setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatListComponentPanel.class.getClassLoader().getResource("group.png"))));
                    }
                    chatListComponent.getChatLastMessageTimestamp().setText(chatInfo[0][0]);
                    //chatListComponent.getChatLastMessageTimestamp().setToolTipText((formatter.format(calendarInstance.getTime())).toUpperCase());
                    //TODO: add later functionality to verify type of message and show message content accordingly
                    if(chatList[i][1].equals("PERSONAL")) {
                        chatListComponent.getChatLastMessageLabel().setText(chatInfo[0][1]);
                    }
                    else
                    {
                        String senderID = chatInfo[0][2];
                        if(senderID.equals("0000000000000000"))
                        {
                            chatListComponent.getChatLastMessageLabel().setText(chatInfo[0][1]);
                        }
                        else if(senderID.equals(ConfigManager.getAccountID()))
                        {
                            chatListComponent.getChatLastMessageLabel().setText("You: " + chatInfo[0][1]);
                        }
                        else
                        {
                            String[][] senderName = DatabaseManager.makeQuery("select display_name from savedUsers where account_id = '" + senderID + "';");
                            if(senderName == null || senderName.length == 0)
                            {
                                chatListComponent.getChatLastMessageLabel().setText(senderID + ": " + chatInfo[0][1]);
                                DatabaseManager.makeUpdate("insert into unknownUsers values('" + senderID + "');");
                            }
                            else
                            {
                                chatListComponent.getChatLastMessageLabel().setText(senderName[0][0] + ": " + chatInfo[0][1]);
                            }
                        }
                    }
                    if(chatInfo[0][2].equals(ConfigManager.getAccountID()))
                    {
                        if(Integer.parseInt(chatInfo[0][3]) == 3)
                        {
                            chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("double-check.png"))));
                        }
                        else if(Integer.parseInt(chatInfo[0][3]) == 2)
                        {
                            chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("sent.png"))));
                        }
                        else if(Integer.parseInt(chatInfo[0][3]) == 1)
                        {
                            chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("check-symbol.png"))));
                        }
                        else
                        {
                            chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("time-left.png"))));
                        }
                    }
                    chatListComponent.getChatLastMessageLabel().setToolTipText(chatInfo[0][1]);

                    String chatParticipantsCompareQueryString = "";
                    String[] participants = chatList[i][3].split(",");
                    for(int j = 0; j < participants.length; j++)
                    {
                        chatParticipantsCompareQueryString = chatParticipantsCompareQueryString + " or from_account_id = '" + participants[j] + "'";
                    }
                    String[][] unreadMessagesCount = DatabaseManager.makeQuery("select count(*) from chat" + chatList[i][0] + " where sent_to_server = 2 and (from_account_id = '0000000000000000'" + chatParticipantsCompareQueryString + ");");
                    if(unreadMessagesCount != null && Integer.parseInt(unreadMessagesCount[0][0]) > 0)
                    {
                        chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("reminder (2).png"))));
                        chatListComponent.getChatLastMessageLabel().setText(Integer.parseInt(unreadMessagesCount[0][0]) + " new message" + (Integer.parseInt(unreadMessagesCount[0][0]) > 1 ? "s" : ""));
                        chatListComponent.getChatLastMessageLabel().setToolTipText(chatListComponent.getChatLastMessageLabel().getText());
                    }

                    chatListComponents.add(chatListComponent);
                }
            }

            if(!chatListComponents.isEmpty()) {
                JPanel outsideContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
                JPanel insideContainer = new JPanel(new GridBagLayout());

                GridBagConstraints layoutConstraints = new GridBagConstraints();
                layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
                layoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
                layoutConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                for (int i = 0; i < chatListComponents.size(); i++) {
                    insideContainer.add(chatListComponents.get(i), layoutConstraints);
                }

                outsideContainer.add(insideContainer);
                chatListScrollPanel.getViewport().setView(outsideContainer);
            }
            else
            {
                JLabel noChatMessageLabel = new JLabel("No chats found");
                noChatMessageLabel.setHorizontalAlignment(SwingUtilities.CENTER);
                noChatMessageLabel.setToolTipText("Create a new chat yourself or share your account ID with others");
                noChatMessageLabel.setEnabled(false);
                chatListScrollPanel.getViewport().setView(noChatMessageLabel);
            }
        }
        else
        {
            JLabel noChatMessageLabel = new JLabel("No chats found");
            noChatMessageLabel.setHorizontalAlignment(SwingUtilities.CENTER);
            noChatMessageLabel.setToolTipText("Create a new chat yourself or share your account ID with others");
            noChatMessageLabel.setEnabled(false);
            chatListScrollPanel.getViewport().setView(noChatMessageLabel);
        }

        chatListScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //chatListScrollPanel.setHorizontalScrollBar(null);
        chatListScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //chatListScrollPanel.getHorizontalScrollBar().
        chatListScrollPanel.getVerticalScrollBar().setUnitIncrement(10);

        chatHeaderPanel.setVisible(false);
        chatSendMessagePanel.setVisible(false);
        JLabel noChatSelectedLabel = new JLabel("Select a chat to see the messages here");
        noChatSelectedLabel.setHorizontalAlignment(SwingUtilities.CENTER);
        noChatSelectedLabel.setEnabled(false);
        chatMessagesListScrollPanel.getViewport().setView(noChatSelectedLabel);

        String[][] allChats = DatabaseManager.makeQuery("select chat_id from chat;");
        if(allChats != null) {
            for (int i = 0; i < allChats.length; i++) {
                String chatID = allChats[i][0];
                String[][] unsentMessages = DatabaseManager.makeQuery("select message_id from chat" + chatID + " where sent_to_server = 0 and from_account_id = '" + ConfigManager.getAccountID() + "';");
                if(unsentMessages != null) {
                    for (int j = 0; j < unsentMessages.length; j++) {
                        messagesToBeSentList.add(new String[]{chatID, unsentMessages[j][0]});
                    }
                }
            }
        }

        //TODO: Implement message sending part of the thread by taking the chat id and message id and sending it and updating database with the results
        newMessageSenderThread = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try
                {
                    Thread.sleep(5000);
                    while(true)
                    {
                        if(NetworkManager.isConnected())
                        {
                            if(NetworkManager.isLoggedIn())
                            {
                                while(!messagesToBeSentList.isEmpty())
                                {
                                    String[] messageToBeSent = messagesToBeSentList.get(0);
                                    String chatID = messageToBeSent[0];
                                    String messageID = messageToBeSent[1];
                                    String[][] messageInfo = DatabaseManager.makeQuery("select message_type, message_content from chat" + chatID + " where message_id = " + messageID + ";");
                                    if(messageInfo != null && messageInfo.length == 1)
                                    {
                                        Boolean success = NetworkManager.sendNewMessage(chatID, messageID, messageInfo[0][0], messageInfo[0][1]);
                                        if(success == null)
                                            break;
                                        else if(!success)
                                            break;
                                        else if(success)
                                        {
                                            messagesToBeSentList.remove(0);
                                            updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);
                                        }
                                    }
                                    else
                                    {
                                        messagesToBeSentList.remove(0);
                                    }
                                }
                            }
                        }
                        Thread.sleep((Integer.parseInt(ConfigManager.getConnectionProbeTimePeriod()) * 1000L) > 3000? 3000:(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000));
                    }
                }
                catch (Exception e)
                {

                }
                return null;
            }
        };
        newMessageSenderThread.execute();

        chatMessagesListScrollPanel.getViewport().addChangeListener(loadOlderMessagesEventListener);



        //TODO: Update the swingworker code here too if any changes are made above
        otherThreadsCrashHandler = new Timer();
        otherThreadsRestartTask = new TimerTask() {
            @Override
            public void run() {
                if(networkStatusUpdater.isDone())
                {
                    //System.out.println("test");
                    networkStatusUpdater = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            Thread.sleep(5000);
                            while(true)
                            {
                                if(NetworkManager.isConnected() && NetworkManager.isLoggedIn())
                                {
                                    appStatusTextLabel.setText("Online");
                                    appStatusColorLabel.setBackground(new Color(0, 255, 0));
                                }
                                else if(NetworkManager.isConnected())
                                {
                                    appStatusTextLabel.setText("Logging in");
                                    appStatusColorLabel.setBackground(new Color(253, 212, 79));
                                }
                                else
                                {
                                    appStatusTextLabel.setText("Offline");
                                    appStatusColorLabel.setBackground(new Color(255, 0, 0));
                                }
                                Thread.sleep((Integer.parseInt(ConfigManager.getConnectionProbeTimePeriod()) * 1000L) > 5000? 5000:(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000));
                            }
                            //return null;
                        }
                    };
                    networkStatusUpdater.execute();
                }
                if(reloginThread.isDone())
                {
                    //System.out.println("test");
                    reloginThread = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            try{
                                Thread.sleep(3000);
                                while(true)
                                {
                                    if(NetworkManager.isConnected())
                                    {
                                        if(!NetworkManager.isLoggedIn())
                                        {
                                            if(!ConfigManager.getAccountSessionID().equals("null"))
                                            {
                                                Boolean success = NetworkManager.loginToOldSession(ConfigManager.getAccountID(), ConfigManager.getAccountSessionID());
                                                if(success != null && !success)
                                                {
                                                    JOptionPane.showMessageDialog(Main.getMainFrame(), "You have been force logged out due to invalid session ID.\nYou will be moved back to the main menu.\nPlease try logging in again.",
                                                            "User logged out", JOptionPane.ERROR_MESSAGE);
                                                    WindowManager.changeFromChatToSelection(true);
                                                }
                                            }
                                            else
                                            {
                                                Boolean success = NetworkManager.reloginToTemporarySession(ConfigManager.getAccountID());
                                                if(success != null && !success)
                                                {
                                                    JOptionPane.showMessageDialog(Main.getMainFrame(), "You have been force logged out due to invalid session ID.\nYou will be moved back to the main menu.\nPlease try logging in again.",
                                                            "User logged out", JOptionPane.ERROR_MESSAGE);
                                                    WindowManager.changeFromChatToSelection(true);
                                                }
                                            }
                                        }
                                    }
                                    Thread.sleep(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000L);
                                }
                            }
                            catch(Exception e)
                            {

                            }
                            return null;
                        }
                    };
                    reloginThread.execute();
                }
                if(newChatAndMessageReceiverThread.isDone())
                {
                    //System.out.println("test");
                    newChatAndMessageReceiverThread = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            try{
                                Thread.sleep(3000);
                                while(true)
                                {
                                    if(NetworkManager.isConnected())
                                    {
                                        if(NetworkManager.isLoggedIn())
                                        {
                                            Boolean success = NetworkManager.checkForNewChats();
                                            if(success != null && success)
                                                updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);

                                            success = NetworkManager.checkForNewMessages();
                                            if(success != null && success)
                                                updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);
                                        }
                                    }
                                    Thread.sleep((Integer.parseInt(ConfigManager.getConnectionProbeTimePeriod()) * 1000L) > 3000? 3000:(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000));
                                }
                            }
                            catch(Exception e)
                            {

                            }
                            return null;
                        }
                    };
                    newChatAndMessageReceiverThread.execute();
                }
                if(newMessageSenderThread.isDone())
                {
                    //System.out.println("test");
                    newMessageSenderThread = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            try
                            {
                                Thread.sleep(5000);
                                while(true)
                                {
                                    if(NetworkManager.isConnected())
                                    {
                                        if(NetworkManager.isLoggedIn())
                                        {
                                            while(!messagesToBeSentList.isEmpty())
                                            {
                                                String[] messageToBeSent = messagesToBeSentList.get(0);
                                                String chatID = messageToBeSent[0];
                                                String messageID = messageToBeSent[1];
                                                String[][] messageInfo = DatabaseManager.makeQuery("select message_type, message_content from chat" + chatID + " where message_id = " + messageID + ";");
                                                if(messageInfo != null && messageInfo.length == 1)
                                                {
                                                    Boolean success = NetworkManager.sendNewMessage(chatID, messageID, messageInfo[0][0], messageInfo[0][1]);
                                                    if(success == null)
                                                        break;
                                                    else if(!success)
                                                        break;
                                                    else if(success)
                                                    {
                                                        messagesToBeSentList.remove(0);
                                                        updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);
                                                    }
                                                }
                                                else
                                                {
                                                    messagesToBeSentList.remove(0);
                                                }
                                            }
                                        }
                                    }
                                    Thread.sleep((Integer.parseInt(ConfigManager.getConnectionProbeTimePeriod()) * 1000L) > 3000? 3000:(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000));
                                }
                            }
                            catch (Exception e)
                            {

                            }
                            return null;
                        }
                    };
                    newMessageSenderThread.execute();
                }
                if(messageReadReceiptsUpdatorThread.isDone())
                {
                    //System.out.println("test");
                    messageReadReceiptsUpdatorThread = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            try
                            {
                                Thread.sleep(5000);
                                while(true)
                                {
                                    if(NetworkManager.isConnected())
                                    {
                                        if(NetworkManager.isLoggedIn())
                                        {
                                            Boolean success = NetworkManager.checkForNewReadReceipts();
                                            if(success != null && success)
                                                updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);

                                            while(!readReceiptsToBeSent.isEmpty())
                                            {
                                                String chatID = readReceiptsToBeSent.get(0)[0];
                                                String messageID = readReceiptsToBeSent.get(0)[1];

                                                //disable read receipt 3 for group chats by not actually sending any such receipts to server
//                                                String[][] chatType = DatabaseManager.makeQuery("select chat_type from chat where chat_id = " + chatID + ";");
//                                                if(chatType == null || chatType.length == 0 || chatType[0][0].equals("GROUP"))
//                                                {
//                                                    DatabaseManager.makeUpdate("delete from unsentReadReceipts where chat_id = " + chatID + " and message_id = " + messageID + ";");
//                                                    readReceiptsToBeSent.remove(0);
//                                                    continue;
//                                                }

                                                success = NetworkManager.sendNewReadReceipts(chatID, messageID);
                                                if(success == null || !success)
                                                    break;
                                                else if(success)
                                                {
                                                    DatabaseManager.makeUpdate("delete from unsentReadReceipts where chat_id = " + chatID + " and message_id = " + messageID + ";");
                                                    readReceiptsToBeSent.remove(0);
                                                }
                                            }
                                        }
                                    }
                                    Thread.sleep((Integer.parseInt(ConfigManager.getConnectionProbeTimePeriod()) * 1000L) > 3000? 3000:(Long.parseLong(ConfigManager.getConnectionProbeTimePeriod()) * 1000));
                                }
                            }
                            catch(Exception e)
                            {

                            }
                            return null;
                        }
                    };
                    messageReadReceiptsUpdatorThread.execute();
                }
            }
        };
        otherThreadsCrashHandler.schedule(otherThreadsRestartTask, 30000, 30000);


        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER && chatSendMessageButton.isEnabled())
                {
                    chatSendMessageButton.doClick();
                }
            }
        };
        //chatSendMessageButton.addKeyListener(enterKeyListener);
        chatSendMessageField.addKeyListener(enterKeyListener);
    }

    private ChatMainPanel getSelfReference()
    {
        return this;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        chatListPanel = new javax.swing.JPanel();
        chatListScrollPanel = new javax.swing.JScrollPane();
        chatListControlsPanel = new javax.swing.JPanel();
        newChatButton = new javax.swing.JButton();
        deleteChatButton = new javax.swing.JToggleButton();
        appStatusPanel = new javax.swing.JPanel();
        profileButton = new javax.swing.JButton();
        appStatusColorLabel = new javax.swing.JLabel();
        appStatusTextLabel = new javax.swing.JLabel();
        chatMessagesPanel = new javax.swing.JPanel();
        chatHeaderPanel = new javax.swing.JPanel();
        chatHeaderIconLabel = new javax.swing.JLabel();
        chatHeaderNameLabel = new javax.swing.JLabel();
        chatHeaderParticipantOrOnlineLabel = new javax.swing.JLabel();
        chatHeaderMoreOptionsButton = new javax.swing.JButton();
        chatSendMessagePanel = new javax.swing.JPanel();
        chatSendMessageOtherTypesToggleButton = new javax.swing.JToggleButton();
        chatSendMessageField = new javax.swing.JTextField();
        chatSendMessageButton = new javax.swing.JButton();
        chatMessagesListScrollPanel = new javax.swing.JScrollPane();

        chatListScrollPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chatListControlsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        newChatButton.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("new-message (2).png")))); // NOI18N
        newChatButton.setToolTipText("New Chat");
        newChatButton.setFocusPainted(false);
        newChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newChatButtonActionPerformed(evt);
            }
        });

        deleteChatButton.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("delete-message.png")))); // NOI18N
        deleteChatButton.setToolTipText("Delete Chat");
        deleteChatButton.setFocusPainted(false);
        deleteChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteChatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chatListControlsPanelLayout = new javax.swing.GroupLayout(chatListControlsPanel);
        chatListControlsPanel.setLayout(chatListControlsPanelLayout);
        chatListControlsPanelLayout.setHorizontalGroup(
                chatListControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(chatListControlsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(newChatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteChatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(143, Short.MAX_VALUE))
        );
        chatListControlsPanelLayout.setVerticalGroup(
                chatListControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatListControlsPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(chatListControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(deleteChatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(newChatButton))
                                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout chatListPanelLayout = new javax.swing.GroupLayout(chatListPanel);
        chatListPanel.setLayout(chatListPanelLayout);
        chatListPanelLayout.setHorizontalGroup(
                chatListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(chatListPanelLayout.createSequentialGroup()
                                .addGroup(chatListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(chatListControlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chatListScrollPanel))
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        chatListPanelLayout.setVerticalGroup(
                chatListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(chatListPanelLayout.createSequentialGroup()
                                .addComponent(chatListScrollPanel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatListControlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        appStatusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        profileButton.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("user.png")))); // NOI18N
        profileButton.setToolTipText("Profile");
        profileButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                profileButtonActionPerformed(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        appStatusColorLabel.setBackground(new java.awt.Color(153, 153, 153));
        appStatusColorLabel.setOpaque(true);

        appStatusTextLabel.setBackground(new java.awt.Color(153, 153, 153));
        appStatusTextLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        appStatusTextLabel.setText("Unknown");
        appStatusTextLabel.setToolTipText("App network status:\nUnknown -> Network not yet initialised or internal error\nOnline -> Connected to server\nOffline -> Disconneted from or not able to connect to server");

        javax.swing.GroupLayout appStatusPanelLayout = new javax.swing.GroupLayout(appStatusPanel);
        appStatusPanel.setLayout(appStatusPanelLayout);
        appStatusPanelLayout.setHorizontalGroup(
                appStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(appStatusPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(profileButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(appStatusColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(appStatusTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        appStatusPanelLayout.setVerticalGroup(
                appStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(appStatusPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(appStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(appStatusPanelLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(appStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, appStatusPanelLayout.createSequentialGroup()
                                                                .addComponent(profileButton)
                                                                .addContainerGap())
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, appStatusPanelLayout.createSequentialGroup()
                                                                .addComponent(appStatusColorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(20, 20, 20))))
                                        .addGroup(appStatusPanelLayout.createSequentialGroup()
                                                .addComponent(appStatusTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        chatMessagesPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chatHeaderPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        chatHeaderPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chatHeaderPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chatHeaderPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                chatHeaderPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                chatHeaderPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                chatHeaderPanelMouseReleased(evt);
            }
        });

        chatHeaderIconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chatHeaderIconLabel.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("user.png")))); // NOI18N
        chatHeaderIconLabel.setToolTipText("Chat Information");
        chatHeaderIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chatHeaderIconLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chatHeaderIconLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                chatHeaderIconLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                chatHeaderIconLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                chatHeaderIconLabelMouseReleased(evt);
            }
        });

        chatHeaderNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        chatHeaderNameLabel.setText("placeholderChatName");
        chatHeaderNameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chatHeaderNameLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chatHeaderNameLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                chatHeaderNameLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                chatHeaderNameLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                chatHeaderNameLabelMouseReleased(evt);
            }
        });

        chatHeaderParticipantOrOnlineLabel.setText("placeholderOnlineStatusOrListOfParticipants");
        chatHeaderParticipantOrOnlineLabel.setToolTipText("placeholderOnlineStatusOrListOfParticipantsFull");
        chatHeaderParticipantOrOnlineLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ChatHeaderParticipantOrOnlineLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ChatHeaderParticipantOrOnlineLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ChatHeaderParticipantOrOnlineLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ChatHeaderParticipantOrOnlineLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ChatHeaderParticipantOrOnlineLabelMouseReleased(evt);
            }
        });

        chatHeaderMoreOptionsButton.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("more.png")))); // NOI18N
        chatHeaderMoreOptionsButton.setToolTipText("More options");
        chatHeaderMoreOptionsButton.setVisible(false);
        chatHeaderMoreOptionsButton.setFocusPainted(false);
        chatHeaderMoreOptionsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chatHeaderMoreOptionsButtonActionPerformed(e);
            }
        });

        javax.swing.GroupLayout chatHeaderPanelLayout = new javax.swing.GroupLayout(chatHeaderPanel);
        chatHeaderPanel.setLayout(chatHeaderPanelLayout);
        chatHeaderPanelLayout.setHorizontalGroup(
                chatHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(chatHeaderPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chatHeaderIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(chatHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(chatHeaderNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chatHeaderParticipantOrOnlineLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatHeaderMoreOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        chatHeaderPanelLayout.setVerticalGroup(
                chatHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(chatHeaderPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(chatHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatHeaderPanelLayout.createSequentialGroup()
                                                .addComponent(chatHeaderNameLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(chatHeaderParticipantOrOnlineLabel))
                                        .addGroup(chatHeaderPanelLayout.createSequentialGroup()
                                                .addGroup(chatHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(chatHeaderIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(chatHeaderMoreOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        chatSendMessagePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chatSendMessageOtherTypesToggleButton.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("add.png")))); // NOI18N
        chatSendMessageOtherTypesToggleButton.setToolTipText("Send document, image, audio, video, etc. (coming soon)");
        chatSendMessageOtherTypesToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatSendMessageOtherTypesToggleButtonActionPerformed(evt);
            }
        });
        chatSendMessageOtherTypesToggleButton.setEnabled(false);

        chatSendMessageField.setToolTipText("Text message field");
        chatSendMessageField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                chatSendMessageFieldActionPerformed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                chatSendMessageFieldActionPerformed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                chatSendMessageFieldActionPerformed();
            }
        });

        chatSendMessageButton.setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("send-message.png")))); // NOI18N
        chatSendMessageButton.setToolTipText("Send");
        chatSendMessageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatSendMessageButtonActionPerformed(evt);
            }
        });
        chatSendMessageButton.setEnabled(false);

        javax.swing.GroupLayout chatSendMessagePanelLayout = new javax.swing.GroupLayout(chatSendMessagePanel);
        chatSendMessagePanel.setLayout(chatSendMessagePanelLayout);
        chatSendMessagePanelLayout.setHorizontalGroup(
                chatSendMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(chatSendMessagePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chatSendMessageOtherTypesToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatSendMessageField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatSendMessageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        chatSendMessagePanelLayout.setVerticalGroup(
                chatSendMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(chatSendMessagePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(chatSendMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(chatSendMessageField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(chatSendMessageOtherTypesToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(chatSendMessageButton, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
        );

        chatMessagesListScrollPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout chatMessagesPanelLayout = new javax.swing.GroupLayout(chatMessagesPanel);
        chatMessagesPanel.setLayout(chatMessagesPanelLayout);
        chatMessagesPanelLayout.setHorizontalGroup(
                chatMessagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatMessagesPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(chatMessagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(chatMessagesListScrollPanel)
                                        .addComponent(chatHeaderPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chatSendMessagePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        chatMessagesPanelLayout.setVerticalGroup(
                chatMessagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(chatMessagesPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chatHeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatMessagesListScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatSendMessagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(chatListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(appStatusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatMessagesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(chatMessagesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(chatListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(appStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );






    }// </editor-fold>

    public void hideMessagesPanel()
    {
        if(activeChatComponentReference != null)
        {
            chatMessagesListScrollPanel.getViewport().setView(null);
            chatHeaderPanel.setVisible(false);
            chatSendMessagePanel.setVisible(false);

            JLabel noChatSelectedLabel = new JLabel("Select a chat to see the messages here");
            noChatSelectedLabel.setHorizontalAlignment(SwingUtilities.CENTER);
            noChatSelectedLabel.setEnabled(false);
            chatMessagesListScrollPanel.getViewport().setView(noChatSelectedLabel);

            activeChatComponentReference.setActiveStatus(false);
            activeChatComponentReference.dehighlightPanel();
            activeChatComponentReference = null;
        }
    }

    private void deleteChatButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if(deleteChatButton.isSelected())
        {
            deleteChatButton.setToolTipText("You are in delete mode, click on a chat above to delete it\nTo exit from delete mode, click this delete button again");
            if(activeChatComponentReference != null)
            {
                chatMessagesListScrollPanel.getViewport().setView(null);
                chatHeaderPanel.setVisible(false);
                chatSendMessagePanel.setVisible(false);

                JLabel noChatSelectedLabel = new JLabel("Select a chat to see the messages here");
                noChatSelectedLabel.setHorizontalAlignment(SwingUtilities.CENTER);
                noChatSelectedLabel.setEnabled(false);
                chatMessagesListScrollPanel.getViewport().setView(noChatSelectedLabel);

                activeChatComponentReference.setActiveStatus(false);
                activeChatComponentReference.dehighlightPanel();
                activeChatComponentReference = null;
            }
        }
        else
        {
            deleteChatButton.setToolTipText("Delete Chat");
        }
    }

    private void chatHeaderMoreOptionsButtonActionPerformed(MouseEvent e)
    {
        if(SwingUtilities.isLeftMouseButton(e) && activeChatComponentReference != null)
        {
            String[][] chatType = DatabaseManager.makeQuery("select chat_type from chat where chat_id = " + activeChatComponentReference.getChatID() + ";");
            if(chatType == null || chatType.length == 0)
            {
                return;
            }

            if(chatHeaderMoreOptionsMenu != null)
                remove(chatHeaderMoreOptionsMenu);

            chatHeaderMoreOptionsMenu = new JPopupMenu();

            if(chatType[0][0].equals("PERSONAL"))
            {
                // TODO: Add options for personal chat
                //  Currently, this button is disabled for personal chats
                //  So this case will never get executed
                //chatHeaderMoreOptionsMenu.removeAll();
            }
            else if(chatType[0][0].contains("GROUP"))
            {
                //chatHeaderMoreOptionsMenu.removeAll();

                JMenuItem addNewUserOption = new JMenuItem("Add new user");
                addNewUserOption.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addNewUserToGroupChatOptionSelected();
                    }
                });
                chatHeaderMoreOptionsMenu.add(addNewUserOption);

                JMenuItem leaveChatOption = new JMenuItem("Leave chat");
                leaveChatOption.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        leaveGroupChatOptionSelected();
                    }
                });
                chatHeaderMoreOptionsMenu.add(leaveChatOption);

                add(chatHeaderMoreOptionsMenu);

                chatHeaderMoreOptionsMenu.show(chatHeaderMoreOptionsButton, e.getX(), e.getY());
            }
        }
    }

    private void addNewUserToGroupChatOptionSelected()
    {
        SwingWorker<Void, Void> newUserAddDialogCreator = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if(!NetworkManager.isConnected())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                            "Server unavailable", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if(!NetworkManager.isLoggedIn())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "User is currently not logged in with the server.\nPlease try again a little later.",
                            "User not logged in", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if(NetworkManager.isBusy())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "The server is currently busy.\nPlease try again later.",
                            "Server busy", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                GroupChatAddUserDialog addUserDialog = new GroupChatAddUserDialog(Main.getMainFrame(), true, getSelfReference());

                return null;
            }
        };
        newUserAddDialogCreator.execute();
    }

    private void leaveGroupChatOptionSelected()
    {
        SwingWorker<Void, Void> leaveGroupChatHandler = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if(!NetworkManager.isConnected())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                            "Server unavailable", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if(!NetworkManager.isLoggedIn())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "User is currently not logged in with the server.\nPlease try again a little later.",
                            "User not logged in", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if(NetworkManager.isBusy())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "The server is currently busy.\nPlease try again later.",
                            "Server busy", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                int response = JOptionPane.showConfirmDialog(Main.getMainFrame(), "Are you sure you want to leave the group chat?", "Leave chat", JOptionPane.YES_NO_OPTION);
                if(response == JOptionPane.YES_OPTION)
                {
                    if(!NetworkManager.isConnected())
                    {
                        JOptionPane.showMessageDialog(Main.getMainFrame(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                                "Server unavailable", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    if(!NetworkManager.isLoggedIn())
                    {
                        JOptionPane.showMessageDialog(Main.getMainFrame(), "User is currently not logged in with the server.\nPlease try again a little later.",
                                "User not logged in", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    if(NetworkManager.isBusy())
                    {
                        JOptionPane.showMessageDialog(Main.getMainFrame(), "The server is currently busy.\nPlease try again later.",
                                "Server busy", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }

                    NetworkManager.setBusy(true);
                    Boolean success = NetworkManager.leaveFromGroupChat(activeChatComponentReference.getChatID());
                    NetworkManager.setBusy(false);
                    if(success == null)
                    {
                        JOptionPane.showMessageDialog(getSelfReference(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                                "Server unavailable", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    if(!success)
                    {
                        JOptionPane.showMessageDialog(getSelfReference(), "The server faced an internal error.\nPlease try again later.",
                                "Server error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }

                    updateFromDB(ChatMainPanel.KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON);
                    JOptionPane.showMessageDialog(getSelfReference(), "You have successfully left the chat.", "Successfully left", JOptionPane.INFORMATION_MESSAGE);
                    return null;
                }
                return null;
            }
        };
        leaveGroupChatHandler.execute();
    }

    private void profileButtonActionPerformed(MouseEvent e) {
        // TODO add your handling code here:
        if(SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e))
        {
            if(profileOptionsMenu != null)
                remove(profileOptionsMenu);

            profileOptionsMenu = new JPopupMenu("Options");

            JMenuItem profileOption = new JMenuItem("Profile (coming soon)");
            profileOption.setEnabled(false);
            profileOptionsMenu.add(profileOption);

            JMenuItem logoutOption = new JMenuItem("Logout");
            logoutOption.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logoutOptionClicked();
                }
            });
            profileOptionsMenu.add(logoutOption);

            add(profileOptionsMenu);

            profileOptionsMenu.show(profileButton, e.getX(), e.getY());
        }
//        if(SwingUtilities.isLeftMouseButton(e))
//        {
//
//        }
//        else if(SwingUtilities.isRightMouseButton(e))
//        {
//            JPopupMenu profileOptionsMenu = new JPopupMenu("Options");
//            JMenuItem logoutOption = new JMenuItem("Logout");
//            logoutOption.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    logoutOptionClicked();
//                }
//            });
//            profileOptionsMenu.add(logoutOption);
//            add(profileOptionsMenu);
//            profileOptionsMenu.show(profileButton, e.getX(), e.getY());
//        }
    }

    private void logoutOptionClicked()
    {
        //networkStatusUpdater.cancel(true);
        WindowManager.changeFromChatToSelection(false);
        //TODO: Call logout on network manager and use window manager to switch to user selection panel
    }

    private void chatHeaderIconLabelMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderIconLabelMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderIconLabelMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void newChatButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        SwingWorker<Void, Void> newChatWindowOpener = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                //TODO: Uncomment the below code checking the network status before letting the user to create a chat
                if(!NetworkManager.isConnected())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "The server is currently unavailable.\nPlease check your connection or try again later.",
                            "Server unavailable", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if(!NetworkManager.isLoggedIn())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "User is currently not logged in with the server.\nPlease try again a little later.",
                            "User not logged in", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if(NetworkManager.isBusy())
                {
                    JOptionPane.showMessageDialog(Main.getMainFrame(), "The server is currently busy.\nPlease try again later.",
                            "Server busy", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                NewChatCreateDialog chatCreateDialog = new NewChatCreateDialog(Main.getMainFrame(), true, getSelfReference());

                return null;
            }
        };
        newChatWindowOpener.execute();
    }

    private void chatHeaderNameLabelMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderNameLabelMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderNameLabelMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderIconLabelMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderIconLabelMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderNameLabelMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderNameLabelMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void ChatHeaderParticipantOrOnlineLabelMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void ChatHeaderParticipantOrOnlineLabelMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void ChatHeaderParticipantOrOnlineLabelMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void ChatHeaderParticipantOrOnlineLabelMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void ChatHeaderParticipantOrOnlineLabelMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderPanelMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderPanelMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderPanelMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderPanelMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatHeaderPanelMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void chatSendMessageOtherTypesToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void chatSendMessageFieldActionPerformed() {
        // TODO add your handling code here:
        if(chatSendMessageField.getText().isEmpty())
        {
            chatSendMessageButton.setEnabled(false);
        }
        else
        {
            chatSendMessageButton.setEnabled(true);
        }
    }

    synchronized private void loadOlderMessages()
    {
        //System.out.println("Load older messages called");
        String[][] fullMessageCountInActiveChat = DatabaseManager.makeQuery("select count(*) from chat" + activeChatComponentReference.getChatID() + ";");
        if(fullMessageCountInActiveChat != null && Long.parseLong(fullMessageCountInActiveChat[0][0]) > numberOfLoadedMessagesInActiveChat)
        {
//            JScrollBar chatMessagesListScrollBar = chatMessagesListScrollPanel.getVerticalScrollBar();
//            int currentScrollBarPosition = chatMessagesListScrollPanel.getVerticalScrollBar().getMaximum();

            long oldValue = Long.parseLong(ConfigManager.getInitialNumberOfMessagesToLoad());
            ConfigManager.setInitialNumberOfMessagesToLoad(String.valueOf(numberOfLoadedMessagesInActiveChat + oldValue), true);
            updateFromDB(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_LOADING_OLD_MESSAGES);
            ConfigManager.setInitialNumberOfMessagesToLoad(String.valueOf(oldValue), true);

//            System.out.println(chatMessagesListScrollPanel.getViewport().getChangeListeners());
//            chatMessagesListScrollPanel.getViewport().removeChangeListener(loadOlderMessagesEventListener);
//            chatMessagesListScrollPanel.getVerticalScrollBar().setValue(chatMessagesListScrollPanel.getVerticalScrollBar().getMaximum() - currentScrollBarPosition);
//            chatMessagesListScrollPanel.getViewport().addChangeListener(loadOlderMessagesEventListener);

        }
    }

    synchronized private void chatSendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String chatID = activeChatComponentReference.getChatID();
        Long messageID;
        Long messageTimestamp = System.currentTimeMillis();
        String messageType = "TEXT";
        String messageContent = chatSendMessageField.getText();
        chatSendMessageField.setText("");
        String[][] unsentMessages = DatabaseManager.makeQuery("select message_id from chat" + chatID + " where sent_to_server = 0;");
        if(unsentMessages != null && unsentMessages.length > 0)
        {
            messageID = Long.parseLong(DatabaseManager.makeQuery("select max(message_id) from chat" + chatID + ";")[0][0]) + 1;
        }
        else
        {
            messageID = Long.parseLong(DatabaseManager.makeQuery("select max(message_id) from chat" + chatID + ";")[0][0]) + 1000001;
        }

        DatabaseManager.makeUpdate("insert into chat" + chatID + " values(" + messageID + ", '" + ConfigManager.getAccountID() + "', 0, " + messageTimestamp + ", '" + messageType + "', '" + messageContent + "');");
        if((new BigInteger(DatabaseManager.makeQuery("select last_message_timestamp from chat where chat_id = " + chatID + ";")[0][0])).compareTo(new BigInteger(String.valueOf(messageTimestamp))) == -1)
        {
            DatabaseManager.makeUpdate("update chat set last_message_timestamp = " + messageTimestamp + " where chat_id = " + chatID + ";");
        }
        updateFromDB();
        messagesToBeSentList.add(new String[]{chatID, String.valueOf(messageID)});
        chatSendMessageField.grabFocus();
    }

    private void deleteChat(ChatListComponentPanel chatToBeDeleted)
    {
        //TODO:
        // simply delete all rows in its corresponding messages table in database, and then call updateFromDB,
        // as it will have no messages, its chat component will no be displayed anymore.
        // Show a success message after deleting and exit delete mode
        // Before starting to delete, ask for confirmation whether to delete or not, give 3 options, yes no cancel,
        // if yes, proceed with deleting and exit delete mode at end, if no, stop delete process and exit delete mode,
        // if cancel, stop deleting but do not exit delete mode, try to change name of cancel button

        //System.out.println("Delete chat called");

        String chatID = chatToBeDeleted.getChatID();
        String chatName = chatToBeDeleted.getChatNameLabel().getText();

        int response = JOptionPane.showOptionDialog(Main.getMainFrame(), "Are you sure you want to delete chat \"" + chatName + "\"?", "Delete selected chat", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Yes", "No", "Cancel and Stop"}, null);
        //System.out.println(response);
        if(response == JOptionPane.NO_OPTION)
        {
            return;
        }
        if(response == JOptionPane.CANCEL_OPTION)
        {
            deleteChatButton.setSelected(false);
            return;
        }
        if(response == JOptionPane.YES_OPTION) {
            DatabaseManager.makeUpdate("delete from chat" + chatID + ";");
            updateFromDB();
            JOptionPane.showMessageDialog(Main.getMainFrame(), "Chat \"" + chatName + "\" has been successfully deleted.", "Chat deleted", JOptionPane.INFORMATION_MESSAGE);
            deleteChatButton.setSelected(false);
        }
    }

    synchronized public void makeChatComponentActive(ChatListComponentPanel componentReference) {
        if (deleteChatButton.isSelected()) {
            deleteChat(componentReference);
            return;
        }

        chatMessagesListScrollPanel.getViewport().removeChangeListener(loadOlderMessagesEventListener);
        //System.out.println(chatMessagesListScrollPanel.getViewport().getChangeListeners().length);

        FlatAnimatedLafChange.showSnapshot();

        if (activeChatComponentReference != null) {
            if (!chatSendMessageField.getText().isEmpty()) {
                int response = JOptionPane.showConfirmDialog(Main.getMainFrame(), "Do you want to discard the written text in the message field and continue?", "Discard message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response != JOptionPane.YES_OPTION) {
                    FlatAnimatedLafChange.stop();
                    return;
                }
            }

            activeChatComponentReference.setActiveStatus(false);
            activeChatComponentReference.dehighlightPanel();
            activeChatComponentReference = null;
            numberOfLoadedMessagesInActiveChat = 0;

            chatMessagesListScrollPanel.getViewport().setView(null);
            chatHeaderPanel.setVisible(false);
            chatSendMessagePanel.setVisible(false);
            chatSendMessageOtherTypesToggleButton.setSelected(false);
            chatSendMessageField.setText("");
            chatHeaderNameLabel.setText("placeholderChatName");
            chatHeaderNameLabel.setToolTipText("placeholderChatName");
            chatHeaderParticipantOrOnlineLabel.setText("placeholderOnlineStatusOrListOfParticipants");
            chatHeaderParticipantOrOnlineLabel.setToolTipText("placeholderOnlineStatusOrListOfParticipantsFull");

        }

        activeChatComponentReference = componentReference;
        activeChatComponentReference.setActiveStatus(true);

        chatMessagesListScrollPanel.getViewport().setView(null);

        //TODO: Add code for initialising chat messages panel stuff from database
        String chatID = activeChatComponentReference.getChatID();
        chatHeaderNameLabel.setText(activeChatComponentReference.getChatNameLabel().getText());
        chatHeaderNameLabel.setToolTipText(chatHeaderNameLabel.getText());
        //TODO: Add functionality for checking online status and showing in the below header label
        chatHeaderParticipantOrOnlineLabel.setText("");
        chatHeaderParticipantOrOnlineLabel.setToolTipText("");
        chatHeaderParticipantOrOnlineLabel.setIcon(null);

        String chatType = "";
        String[][] chatInfo = DatabaseManager.makeQuery("select * from chat where chat_id = " + chatID + ";");
        if (chatInfo != null && chatInfo.length > 0) {
            chatType = chatInfo[0][3];
        }
        String otherParticipantID;
        if (chatType.equals("PERSONAL")) {
            otherParticipantID = chatInfo[0][5];

            Timer onlineStatusUpdateTimer = new Timer();
            onlineStatusUpdateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //System.out.println("Chat ID " + chatID + " checking");
                    if (activeChatComponentReference == null || !activeChatComponentReference.getChatID().equals(chatID)) {
                        cancel();
                        return;
                    }
                    Boolean online = NetworkManager.checkIfUserIsOnline(otherParticipantID);
                    if (activeChatComponentReference == null || !activeChatComponentReference.getChatID().equals(chatID)) {
                        cancel();
                        return;
                    }
                    if (online == null) {
                        chatHeaderParticipantOrOnlineLabel.setText("");
                        chatHeaderParticipantOrOnlineLabel.setIcon(null);
                    } else if (!online) {
                        chatHeaderParticipantOrOnlineLabel.setText("offline");
                        chatHeaderParticipantOrOnlineLabel.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("red circle.png"))));
                    } else if (online) {
                        chatHeaderParticipantOrOnlineLabel.setText("online");
                        chatHeaderParticipantOrOnlineLabel.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("green circle.png"))));
                    }
                }
            }, 1000, 2000);
        } else if (chatType.contains("GROUP")) {
            String[] chatParticipantsID = chatInfo[0][5].split(",");
            String participantList = "";
            for (int i = 0; i < chatParticipantsID.length; i++) {
                if (chatParticipantsID[i].isEmpty())
                    continue;
                String[][] participantName = DatabaseManager.makeQuery("select display_name from savedUsers where account_id = '" + chatParticipantsID[i] + "';");
                if (participantName == null || participantName.length == 0) {
                    DatabaseManager.makeUpdate("insert into unknownUsers values('" + chatParticipantsID[i] + "');");
                    continue;
                }
                participantList = participantList + participantName[0][0] + ", ";
            }
            int indexOfExtraCommaSpace = participantList.lastIndexOf(", ");
            if (indexOfExtraCommaSpace != -1) {
                participantList = participantList.substring(0, indexOfExtraCommaSpace);
            }
            chatHeaderParticipantOrOnlineLabel.setText(participantList);
            chatHeaderParticipantOrOnlineLabel.setToolTipText(participantList);
        }
        if (chatType.equals("PERSONAL")) {
            chatHeaderIconLabel.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatListComponentPanel.class.getClassLoader().getResource("user.png"))));
        } else {
            chatHeaderIconLabel.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatListComponentPanel.class.getClassLoader().getResource("group.png"))));
        }
        if (chatType.equals("PERSONAL")) {
            chatHeaderMoreOptionsButton.setVisible(false);
        } else {
            chatHeaderMoreOptionsButton.setVisible(true);
        }

        if (chatType.contains("GROUP")) {
            currentChatParticipantsColorsMapping.clear();
            String[][] allParticipantsWhoHaveSentAtLeastOneMessage = DatabaseManager.makeQuery("select distinct from_account_id from chat" + chatID + ";");
            String[][] allCurrentParticipants = DatabaseManager.makeQuery("select chat_participants from chat where chat_id = " + chatID + ";");
            ArrayList<String> allParticipantsPreviousAndNowMerged = new ArrayList<>();
            if (allParticipantsWhoHaveSentAtLeastOneMessage != null) {
                for (int i = 0; i < allParticipantsWhoHaveSentAtLeastOneMessage.length; i++) {
                    if (!allParticipantsWhoHaveSentAtLeastOneMessage[i][0].equals("0000000000000000") && !allParticipantsWhoHaveSentAtLeastOneMessage[i][0].equals(ConfigManager.getAccountID()))
                        allParticipantsPreviousAndNowMerged.add(allParticipantsWhoHaveSentAtLeastOneMessage[i][0]);
                }
            }
            if (allCurrentParticipants != null && allCurrentParticipants.length == 1) {
                String[] participants = allCurrentParticipants[0][0].split(",");
                for (int i = 0; i < participants.length; i++) {
                    if (!participants[i].equals("0000000000000000") && !participants[i].equals(ConfigManager.getAccountID()))
                        allParticipantsPreviousAndNowMerged.add(participants[i]);
                }
            }
            Set<String> duplicatePurgeSet = new HashSet<>(allParticipantsPreviousAndNowMerged);
            allParticipantsPreviousAndNowMerged.clear();
            allParticipantsPreviousAndNowMerged.addAll(duplicatePurgeSet);
            for (int i = allParticipantsPreviousAndNowMerged.size() - 1; i >= 0; i--) {
                if (allParticipantsPreviousAndNowMerged.get(i).isEmpty())
                    allParticipantsPreviousAndNowMerged.remove(i);
            }
            int numberOfOtherParticipants = allParticipantsPreviousAndNowMerged.size();
            Color[] generatedColors = RandomGenerators.generateRandomDifferentColorsForGroupMemberNamesUsingHSB(numberOfOtherParticipants + 1);
            for (int i = 0; i < numberOfOtherParticipants; i++) {
                currentChatParticipantsColorsMapping.put(allParticipantsPreviousAndNowMerged.get(i), generatedColors[i]);
            }
            currentChatParticipantsColorsMapping.put("default", generatedColors[generatedColors.length - 1]);
            //System.out.println(currentChatParticipantsColorsMapping);
        }


        JPanel insideChatMessagesContainer = new JPanel(new GridBagLayout());
        JPanel outsideChatMessagesContainer = new JPanel(new BorderLayout(1, 1));

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        layoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
        layoutConstraints.weightx = 1.0;
        //layoutConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        String[][] chatMessages = DatabaseManager.makeQuery("select * from chat" + chatID + " order by message_timestamp desc;");
        //System.out.println(chatMessages.length);
        if (chatMessages != null) {
            numberOfLoadedMessagesInActiveChat = 0;
            for (int i = (chatMessages.length > Integer.parseInt(ConfigManager.getInitialNumberOfMessagesToLoad()) ? (Integer.parseInt(ConfigManager.getInitialNumberOfMessagesToLoad()) - 1) : (chatMessages.length - 1)); i >= 0; i--) {
                if (chatMessages[i][4].equals("TEXT")) {
                    if (chatMessages[i][1].equals("0000000000000000")) {
                        ChatMessagesListInfoTextComponent newMessage = new ChatMessagesListInfoTextComponent(chatMessages[i][0]);
                        newMessage.getInfoTextLabel().setText(chatMessages[i][5]);
                        newMessage.getInfoTextLabel().setToolTipText(chatMessages[i][5]);
                        insideChatMessagesContainer.add(newMessage, layoutConstraints);
                        numberOfLoadedMessagesInActiveChat++;
                    } else if (chatMessages[i][1].equals(ConfigManager.getAccountID())) {
                        ChatMessagesListSentTextComponent newMessage = new ChatMessagesListSentTextComponent(chatMessages[i][0]);
                        newMessage.getMessageContentLabel().setText(chatMessages[i][5]);
                        newMessage.getMessageTimestampLabel().setText(chatMessages[i][3]);
                        //newMessage.getMessageTimestampLabel().setToolTipText(chatMessages[i][3]);
                        if (Integer.parseInt(chatMessages[i][2]) == 3) {
                            newMessage.getStatusIconLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("double-check.png"))));
                        } else if (Integer.parseInt(chatMessages[i][2]) == 2) {
                            newMessage.getStatusIconLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("sent.png"))));
                        } else if (Integer.parseInt(chatMessages[i][2]) == 1) {
                            newMessage.getStatusIconLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("check-symbol.png"))));
                        } else {
                            newMessage.getStatusIconLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("time-left.png"))));
                        }
                        insideChatMessagesContainer.add(newMessage, layoutConstraints);
                        numberOfLoadedMessagesInActiveChat++;
                    }
                    //else if ((DatabaseManager.makeQuery("select chat_participants from chat where chat_id = " + chatID + ";"))[0][0].contains(chatMessages[i][1])) {
                    else {
                        ChatMessagesListReceivedTextComponent newMessage = new ChatMessagesListReceivedTextComponent(chatMessages[i][0]);
                        if (chatType.equals("PERSONAL")) {
                            newMessage.getMessageContentLabel().setText(chatMessages[i][5]);
                        } else {
                            String senderName;
                            String[][] userName = DatabaseManager.makeQuery("select display_name from savedUsers where account_id = '" + chatMessages[i][1] + "';");
                            if (userName == null || userName.length == 0) {
                                senderName = chatMessages[i][1];
                                DatabaseManager.makeUpdate("insert into unknownUsers values('" + chatMessages[i][1] + "');");
                            } else {
                                senderName = userName[0][0];
                            }
                            try {
                                StyledDocument messageLabelDoc = newMessage.getMessageContentLabel().getStyledDocument();
                                Style senderNameStyle = messageLabelDoc.addStyle("", null);
                                Color nameColor = currentChatParticipantsColorsMapping.get(chatMessages[i][1]);
                                if (nameColor == null) {
                                    nameColor = currentChatParticipantsColorsMapping.get("default");
                                }
                                StyleConstants.setForeground(senderNameStyle, nameColor);
                                StyleConstants.setBold(senderNameStyle, true);
                                //StyleConstants.setUnderline(senderNameStyle, true);
                                messageLabelDoc.insertString(messageLabelDoc.getLength(), senderName, senderNameStyle);
                                messageLabelDoc.insertString(messageLabelDoc.getLength(), "\n" + chatMessages[i][5], null);
                            } catch (Exception e) {
                                //System.out.println(e);
                                newMessage.getMessageContentLabel().setText("<html><b><u>" + senderName + "</u></b><br>" + chatMessages[i][5] + "</html>");
                            }
                        }
                        newMessage.getMessageTimestampLabel().setText(chatMessages[i][3]);
                        //newMessage.getMessageTimestampLabel().setToolTipText(chatMessages[i][3]);
                        insideChatMessagesContainer.add(newMessage, layoutConstraints);
                        numberOfLoadedMessagesInActiveChat++;
                    }
                }
                //TODO: Add display for messages with different type than text


                if (!chatMessages[i][1].equals(ConfigManager.getAccountID()) && Integer.parseInt(chatMessages[i][2]) == 2) {
                    DatabaseManager.makeUpdate("update chat" + chatID + " set sent_to_server = 3 where message_id = " + chatMessages[i][0] + ";");
                    readReceiptsToBeSent.add(new String[]{chatID, chatMessages[i][0]});
                    DatabaseManager.makeUpdate("insert into unsentReadReceipts values(" + chatID + ", " + chatMessages[i][0] + ");");
                }
            }
        }
        outsideChatMessagesContainer.add(insideChatMessagesContainer, BorderLayout.SOUTH);

        if (chatType.equals("GROUP")) {
            chatHeaderMoreOptionsButton.setEnabled(true);
            chatSendMessageField.setEnabled(true);
        } else if (chatType.equals("GROUP_LF")) {
            chatHeaderMoreOptionsButton.setEnabled(false);
            chatSendMessageField.setEnabled(false);
        }
        chatHeaderPanel.setVisible(true);
        chatSendMessagePanel.setVisible(true);
        chatMessagesListScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatMessagesListScrollPanel.getVerticalScrollBar().setUnitIncrement(10);
        chatMessagesListScrollPanel.getViewport().setView(outsideChatMessagesContainer);
        revalidate();

        //System.out.println("Loaded messages: " + numberOfLoadedMessagesInActiveChat);

        String[][] updatedUnreadMessagesCount = DatabaseManager.makeQuery("select count(*) from chat" + activeChatComponentReference.getChatID() + " where sent_to_server = 2 and not from_account_id = '" + ConfigManager.getAccountID() + "';");
        if (updatedUnreadMessagesCount != null && Integer.parseInt(updatedUnreadMessagesCount[0][0]) > 0) {
            activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("reminder (2).png"))));
            activeChatComponentReference.getChatLastMessageLabel().setText(Integer.parseInt(updatedUnreadMessagesCount[0][0]) + " new message" + (Integer.parseInt(updatedUnreadMessagesCount[0][0]) > 1 ? "s" : ""));
            activeChatComponentReference.getChatLastMessageLabel().setToolTipText(activeChatComponentReference.getChatLastMessageLabel().getText());
        }

        if (updatedUnreadMessagesCount == null || Integer.parseInt(updatedUnreadMessagesCount[0][0]) == 0) {
            String[][] chatLastMessage = DatabaseManager.makeQuery("select message_content, from_account_id, sent_to_server from chat" + activeChatComponentReference.getChatID() + " where message_timestamp = (select max(message_timestamp) from chat" + activeChatComponentReference.getChatID() + ");");
            if (chatLastMessage != null && chatLastMessage.length == 1) {
                activeChatComponentReference.getChatLastMessageLabel().setIcon(null);
                //activeChatComponentReference.getChatLastMessageLabel().setText(chatLastMessage[0][0]);
                if (chatType.equals("PERSONAL")) {
                    activeChatComponentReference.getChatLastMessageLabel().setText(chatLastMessage[0][0]);
                } else {
                    String senderID = chatLastMessage[0][1];
                    if (senderID.equals("0000000000000000")) {
                        activeChatComponentReference.getChatLastMessageLabel().setText(chatLastMessage[0][0]);
                    } else if (senderID.equals(ConfigManager.getAccountID())) {
                        activeChatComponentReference.getChatLastMessageLabel().setText("You: " + chatLastMessage[0][0]);
                    } else {
                        String[][] senderName = DatabaseManager.makeQuery("select display_name from savedUsers where account_id = '" + senderID + "';");
                        if (senderName == null || senderName.length == 0) {
                            activeChatComponentReference.getChatLastMessageLabel().setText(senderID + ": " + chatLastMessage[0][0]);
                            DatabaseManager.makeUpdate("insert into unknownUsers values('" + senderID + "');");
                        } else {
                            activeChatComponentReference.getChatLastMessageLabel().setText(senderName[0][0] + ": " + chatLastMessage[0][0]);
                        }
                    }
                }
                if (chatLastMessage[0][1].equals(ConfigManager.getAccountID())) {
                    if (Integer.parseInt(chatLastMessage[0][2]) == 3) {
                        activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("double-check.png"))));
                    } else if (Integer.parseInt(chatLastMessage[0][2]) == 2) {
                        activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("sent.png"))));
                    } else if (Integer.parseInt(chatLastMessage[0][2]) == 1) {
                        activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("check-symbol.png"))));
                    } else {
                        activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("time-left.png"))));
                    }
                }
                activeChatComponentReference.getChatLastMessageLabel().setToolTipText(chatLastMessage[0][0]);
            }
        }



//        int difference = insideChatMessagesContainer.getSize().height - chatMessagesListScrollPanel.getViewport().getExtentSize().height;
//        if(difference > 0)
//        {
//            chatMessagesListScrollPanel.getViewport().setViewPosition(new Point(0, difference));
//        }
//
//        chatMessagesListScrollPanel.getViewport().addChangeListener(loadOlderMessagesEventListener);
//
//        chatSendMessageField.grabFocus();
//
//        FlatAnimatedLafChange.stop();




        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int difference = insideChatMessagesContainer.getSize().height - chatMessagesListScrollPanel.getViewport().getExtentSize().height;
                if(difference > 0)
                {
                    chatMessagesListScrollPanel.getViewport().setViewPosition(new Point(0, difference));
                }

                chatMessagesListScrollPanel.getViewport().addChangeListener(loadOlderMessagesEventListener);

                chatSendMessageField.grabFocus();

                FlatAnimatedLafChange.stop();

                //System.out.println(chatMessagesListScrollPanel.getViewport().getChangeListeners().length);
            }
        });
//        chatMessagesListScrollPanel.revalidate();
//
//        AdjustmentListener downScroller = new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                Adjustable adjustable = e.getAdjustable();
//                adjustable.setValue(adjustable.getMaximum());
//                chatMessagesListScrollPanel.getVerticalScrollBar().removeAdjustmentListener(this);
//            }
//        };
//        chatMessagesListScrollPanel.getVerticalScrollBar().addAdjustmentListener(downScroller);
//        chatMessagesListScrollPanel.validate();
//        Main.getMainFrame().revalidate();
//        System.out.println(chatMessagesListScrollPanel.getVerticalScrollBar().getValue());
//        chatMessagesListScrollPanel.getVerticalScrollBar().setValue(chatMessagesListScrollPanel.getVerticalScrollBar().getMaximum());
    }

    //TODO: Implement updating of chat panel from DB, update chat list and message list
    synchronized public void updateFromDB(int ... extraParameters)
    {
        chatMessagesListScrollPanel.getViewport().removeChangeListener(loadOlderMessagesEventListener);

        FlatAnimatedLafChange.showSnapshot();

        boolean keepChatListScrollPosition = false;
        int chatListPositionFromBottom = chatListScrollPanel.getVerticalScrollBar().getMaximum() - chatListScrollPanel.getVerticalScrollBar().getValue();
        if(chatListScrollPanel.getVerticalScrollBar().getValue() > 0)
        {
            keepChatListScrollPosition = true;
        }

//        if(keepChatListScrollPosition)
//        {
//            chatListPositionFromBottom = chatListScrollPanel.getVerticalScrollBar().getMaximum() - chatListScrollPanel.getVerticalScrollBar().getValue();
//        }

        String activeChatID = null;
        if(activeChatComponentReference != null)
            activeChatID = activeChatComponentReference.getChatID();

        chatListScrollPanel.getViewport().setView(null);
        for(int i = chatListComponents.size() - 1; i >= 0; i--)
        {
            chatListComponents.remove(i);
        }

        String[][] chatList = DatabaseManager.makeQuery("select chat_id, chat_type, chat_name, chat_participants from chat order by last_message_timestamp desc;");
        if(chatList != null && chatList.length > 0)
        {
            for(int i = 0; i < chatList.length; i++)
            {
                ChatListComponentPanel chatListComponent = new ChatListComponentPanel(chatList[i][0], getSelfReference());
                chatListComponent.getChatNameLabel().setText(chatList[i][2]);
                chatListComponent.getChatNameLabel().setToolTipText(chatList[i][2]);
                String[][] chatInfo = DatabaseManager.makeQuery("select message_timestamp, message_content, from_account_id, sent_to_server from chat" + chatList[i][0] + " where message_timestamp = (select max(message_timestamp) from chat" + chatList[i][0] + ");");
                if(chatInfo != null && chatInfo.length == 1)
                {
                    //TODO: verify later how timestamp is stored and displayed and add required code to display them properly instead of the entire thing, most likely use Date.getTime()
                    Calendar calendarInstance = Calendar.getInstance();
                    calendarInstance.setTimeInMillis(Long.parseLong(chatInfo[0][0]));
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm a");
                    if(chatList[i][1].equals("PERSONAL"))
                    {
                        chatListComponent.getChatIconLabel().setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatListComponentPanel.class.getClassLoader().getResource("user.png"))));
                    }
                    else
                    {
                        chatListComponent.getChatIconLabel().setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatListComponentPanel.class.getClassLoader().getResource("group.png"))));
                    }
                    chatListComponent.getChatLastMessageTimestamp().setText(chatInfo[0][0]);
                    //chatListComponent.getChatLastMessageTimestamp().setToolTipText((formatter.format(calendarInstance.getTime())).toUpperCase());
                    //TODO: add later functionality to verify type of message and show message content accordingly
                    if(chatList[i][1].equals("PERSONAL")) {
                        chatListComponent.getChatLastMessageLabel().setText(chatInfo[0][1]);
                    }
                    else
                    {
                        String senderID = chatInfo[0][2];
                        if(senderID.equals("0000000000000000"))
                        {
                            chatListComponent.getChatLastMessageLabel().setText(chatInfo[0][1]);
                        }
                        else if(senderID.equals(ConfigManager.getAccountID()))
                        {
                            chatListComponent.getChatLastMessageLabel().setText("You: " + chatInfo[0][1]);
                        }
                        else
                        {
                            String[][] senderName = DatabaseManager.makeQuery("select display_name from savedUsers where account_id = '" + senderID + "';");
                            if(senderName == null || senderName.length == 0)
                            {
                                chatListComponent.getChatLastMessageLabel().setText(senderID + ": " + chatInfo[0][1]);
                                DatabaseManager.makeUpdate("insert into unknownUsers values('" + senderID + "');");
                            }
                            else
                            {
                                chatListComponent.getChatLastMessageLabel().setText(senderName[0][0] + ": " + chatInfo[0][1]);
                            }
                        }
                    }
                    if(chatInfo[0][2].equals(ConfigManager.getAccountID()))
                    {
                        if(Integer.parseInt(chatInfo[0][3]) == 3)
                        {
                            chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("double-check.png"))));
                        }
                        else if(Integer.parseInt(chatInfo[0][3]) == 2)
                        {
                            chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("sent.png"))));
                        }
                        else if(Integer.parseInt(chatInfo[0][3]) == 1)
                        {
                            chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("check-symbol.png"))));
                        }
                        else
                        {
                            chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("time-left.png"))));
                        }
                    }
                    chatListComponent.getChatLastMessageLabel().setToolTipText(chatInfo[0][1]);

                    String chatParticipantsCompareQueryString = "";
                    String[] participants = chatList[i][3].split(",");
                    for(int j = 0; j < participants.length; j++)
                    {
                        chatParticipantsCompareQueryString = chatParticipantsCompareQueryString + " or from_account_id = '" + participants[j] + "'";
                    }
                    String[][] unreadMessagesCount = DatabaseManager.makeQuery("select count(*) from chat" + chatList[i][0] + " where sent_to_server = 2 and (from_account_id = '0000000000000000'" + chatParticipantsCompareQueryString + ");");
                    if(unreadMessagesCount != null && Integer.parseInt(unreadMessagesCount[0][0]) > 0)
                    {
                        chatListComponent.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("reminder (2).png"))));
                        chatListComponent.getChatLastMessageLabel().setText(Integer.parseInt(unreadMessagesCount[0][0]) + " new message" + (Integer.parseInt(unreadMessagesCount[0][0]) > 1 ? "s" : ""));
                        chatListComponent.getChatLastMessageLabel().setToolTipText(chatListComponent.getChatLastMessageLabel().getText());
                    }

                    chatListComponents.add(chatListComponent);
                }
            }

            if(!chatListComponents.isEmpty()) {
                JPanel outsideContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
                JPanel insideContainer = new JPanel(new GridBagLayout());

                GridBagConstraints layoutConstraints = new GridBagConstraints();
                layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
                layoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
                layoutConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                for (int i = 0; i < chatListComponents.size(); i++) {
                    insideContainer.add(chatListComponents.get(i), layoutConstraints);
                }

                outsideContainer.add(insideContainer);
                chatListScrollPanel.getViewport().setView(outsideContainer);
            }
            else
            {
                JLabel noChatMessageLabel = new JLabel("No chats found");
                noChatMessageLabel.setHorizontalAlignment(SwingUtilities.CENTER);
                noChatMessageLabel.setToolTipText("Create a new chat yourself or share your account ID with others");
                noChatMessageLabel.setEnabled(false);
                chatListScrollPanel.getViewport().setView(noChatMessageLabel);
            }
        }
        else
        {
            JLabel noChatMessageLabel = new JLabel("No chats found");
            noChatMessageLabel.setHorizontalAlignment(SwingUtilities.CENTER);
            noChatMessageLabel.setToolTipText("Create a new chat yourself or share your account ID with others");
            noChatMessageLabel.setEnabled(false);
            chatListScrollPanel.getViewport().setView(noChatMessageLabel);
        }

        if(keepChatListScrollPosition)
        {
            chatListScrollPanel.getVerticalScrollBar().setValue(chatListScrollPanel.getVerticalScrollBar().getMaximum() - chatListPositionFromBottom);

//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    chatListScrollPanel.getVerticalScrollBar().setValue(chatListScrollPanel.getVerticalScrollBar().getMaximum() - chatListPositionFromBottom);
//                }
//            });
        }

        FlatAnimatedLafChange.stop();

        if(activeChatID != null)
        {
            for(int i = 0; i < chatListComponents.size(); i++)
            {
                if(chatListComponents.get(i).getChatID().equals(activeChatID))
                {
                    activeChatComponentReference = chatListComponents.get(i);
                    break;
                }
            }

            if(extraParameters.length > 0 && Arrays.toString(extraParameters).contains(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_LOADING_OLD_MESSAGES + ""))
            {
                messageListCurrentScrollBarPositionFromBottom = chatMessagesListScrollPanel.getVerticalScrollBar().getMaximum() - chatMessagesListScrollPanel.getVerticalScrollBar().getValue();
            }
            else if(extraParameters.length > 0 && Arrays.toString(extraParameters).contains(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON + ""))
            {
                messageListCurrentScrollBarPositionFromTop = chatMessagesListScrollPanel.getVerticalScrollBar().getValue();
                if(chatMessagesListScrollPanel.getVerticalScrollBar().getMaximum() - (chatMessagesListScrollPanel.getVerticalScrollBar().getValue() + chatMessagesListScrollPanel.getVerticalScrollBar().getModel().getExtent()) < 50)
                    forceScrollDownMessageList = true;
                else
                    forceScrollDownMessageList = false;
            }

            boolean previousMessagesAlreadyLoadedInChat = false;
            long totalNumberOfMessagesToLoad = Integer.parseInt(ConfigManager.getInitialNumberOfMessagesToLoad());
            if(numberOfLoadedMessagesInActiveChat > Integer.parseInt(ConfigManager.getInitialNumberOfMessagesToLoad()))
            {
                previousMessagesAlreadyLoadedInChat = true;
                totalNumberOfMessagesToLoad = numberOfLoadedMessagesInActiveChat;
            }

            numberOfLoadedMessagesInActiveChat = 0;
            activeChatComponentReference.highlightPanel();
            activeChatComponentReference.setActiveStatus(true);

            FlatAnimatedLafChange.showSnapshot();

            chatHeaderNameLabel.setText(activeChatComponentReference.getChatNameLabel().getText());
            chatHeaderNameLabel.setToolTipText(chatHeaderNameLabel.getText());

            chatMessagesListScrollPanel.getViewport().setView(null);

            String chatType = "";
            String[][] chatInfo = DatabaseManager.makeQuery("select * from chat where chat_id = " + activeChatComponentReference.getChatID() + ";");
            if(chatInfo != null && chatInfo.length > 0)
            {
                chatType = chatInfo[0][3];
            }

            if(chatType.equals("PERSONAL"))
            {
                chatHeaderIconLabel.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatListComponentPanel.class.getClassLoader().getResource("user.png"))));
            }
            else
            {
                chatHeaderIconLabel.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(ChatListComponentPanel.class.getClassLoader().getResource("group.png"))));
            }

            JPanel insideChatMessagesContainer = new JPanel(new GridBagLayout());
            JPanel outsideChatMessagesContainer = new JPanel(new BorderLayout(1, 1));

            GridBagConstraints layoutConstraints = new GridBagConstraints();
            layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
            layoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
            layoutConstraints.weightx = 1.0;
            //layoutConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
            String[][] chatMessages = DatabaseManager.makeQuery("select * from chat" + activeChatComponentReference.getChatID() + " order by message_timestamp desc;");
            //System.out.println(chatMessages.length);
            if(chatMessages != null)
            {
                numberOfLoadedMessagesInActiveChat = 0;
                for (int i = (int)(chatMessages.length > Integer.parseInt(ConfigManager.getInitialNumberOfMessagesToLoad())? (previousMessagesAlreadyLoadedInChat? (totalNumberOfMessagesToLoad - 1) : (Integer.parseInt(ConfigManager.getInitialNumberOfMessagesToLoad()) - 1)) : (chatMessages.length - 1)); i >= 0; i--) {
                    if (chatMessages[i][4].equals("TEXT"))
                    {
                        if(chatMessages[i][1].equals("0000000000000000"))
                        {
                            ChatMessagesListInfoTextComponent newMessage = new ChatMessagesListInfoTextComponent(chatMessages[i][0]);
                            newMessage.getInfoTextLabel().setText(chatMessages[i][5]);
                            newMessage.getInfoTextLabel().setToolTipText(chatMessages[i][5]);
                            insideChatMessagesContainer.add(newMessage, layoutConstraints);
                            numberOfLoadedMessagesInActiveChat++;
                        }
                        else if(chatMessages[i][1].equals(ConfigManager.getAccountID()))
                        {
                            ChatMessagesListSentTextComponent newMessage = new ChatMessagesListSentTextComponent(chatMessages[i][0]);
                            newMessage.getMessageContentLabel().setText(chatMessages[i][5]);
                            newMessage.getMessageTimestampLabel().setText(chatMessages[i][3]);
                            //newMessage.getMessageTimestampLabel().setToolTipText(chatMessages[i][3]);
                            if(Integer.parseInt(chatMessages[i][2]) == 3)
                            {
                                newMessage.getStatusIconLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("double-check.png"))));
                            }
                            else if(Integer.parseInt(chatMessages[i][2]) == 2)
                            {
                                newMessage.getStatusIconLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("sent.png"))));
                            }
                            else if(Integer.parseInt(chatMessages[i][2]) == 1)
                            {
                                newMessage.getStatusIconLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("check-symbol.png"))));
                            }
                            else
                            {
                                newMessage.getStatusIconLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("time-left.png"))));
                            }
                            insideChatMessagesContainer.add(newMessage, layoutConstraints);
                            numberOfLoadedMessagesInActiveChat++;
                        }
                        //else if((DatabaseManager.makeQuery("select chat_participants from chat where chat_id = " + activeChatComponentReference.getChatID() + ";"))[0][0].contains(chatMessages[i][1]))
                        else {
                            ChatMessagesListReceivedTextComponent newMessage = new ChatMessagesListReceivedTextComponent(chatMessages[i][0]);
                            if(chatType.equals("PERSONAL")) {
                                newMessage.getMessageContentLabel().setText(chatMessages[i][5]);
                            }
                            else {
                                String senderName;
                                String[][] userName = DatabaseManager.makeQuery("select display_name from savedUsers where account_id = '" + chatMessages[i][1] + "';");
                                if(userName == null || userName.length == 0)
                                {
                                    senderName = chatMessages[i][1];
                                    DatabaseManager.makeUpdate("insert into unknownUsers values('" + chatMessages[i][1] + "');");
                                }
                                else
                                {
                                    senderName = userName[0][0];
                                }
                                try {
                                    StyledDocument messageLabelDoc = newMessage.getMessageContentLabel().getStyledDocument();
                                    Style senderNameStyle = messageLabelDoc.addStyle("", null);
                                    Color nameColor = currentChatParticipantsColorsMapping.get(chatMessages[i][1]);
                                    if(nameColor == null)
                                    {
                                        nameColor = currentChatParticipantsColorsMapping.get("default");
                                    }
                                    StyleConstants.setForeground(senderNameStyle, nameColor);
                                    StyleConstants.setBold(senderNameStyle, true);
                                    //StyleConstants.setUnderline(senderNameStyle, true);
                                    messageLabelDoc.insertString(messageLabelDoc.getLength(), senderName, senderNameStyle);
                                    messageLabelDoc.insertString(messageLabelDoc.getLength(), "\n" + chatMessages[i][5], null);
                                }
                                catch (Exception e) {
                                    //System.out.println(e);
                                    newMessage.getMessageContentLabel().setText("<html><b><u>" + senderName + "</u></b><br>" + chatMessages[i][5] + "</html>");
                                }
                            }
                            newMessage.getMessageTimestampLabel().setText(chatMessages[i][3]);
                            //newMessage.getMessageTimestampLabel().setToolTipText(chatMessages[i][3]);
                            insideChatMessagesContainer.add(newMessage, layoutConstraints);
                            numberOfLoadedMessagesInActiveChat++;
                        }
                    }
                    //TODO: Add display for messages with different type than text


                    if (!chatMessages[i][1].equals(ConfigManager.getAccountID()) && Integer.parseInt(chatMessages[i][2]) == 2)
                    {
                        DatabaseManager.makeUpdate("update chat" + activeChatComponentReference.getChatID() + " set sent_to_server = 3 where message_id = " + chatMessages[i][0] + ";");
                        readReceiptsToBeSent.add(new String[]{activeChatComponentReference.getChatID(), chatMessages[i][0]});
                        DatabaseManager.makeUpdate("insert into unsentReadReceipts values(" + activeChatComponentReference.getChatID() + ", " + chatMessages[i][0] + ");");
                    }
                }
            }
            outsideChatMessagesContainer.add(insideChatMessagesContainer, BorderLayout.SOUTH);

            if(chatType.equals("GROUP"))
            {
                chatHeaderMoreOptionsButton.setEnabled(true);
                chatSendMessageField.setEnabled(true);
            }
            else if(chatType.equals("GROUP_LF"))
            {
                chatHeaderMoreOptionsButton.setEnabled(false);
                chatSendMessageField.setEnabled(false);
            }

            if(chatType.contains("GROUP"))
            {
                String[] chatParticipantsID = chatInfo[0][5].split(",");
                String participantList = "";
                for(int i = 0; i < chatParticipantsID.length; i++)
                {
                    if(chatParticipantsID[i].isEmpty())
                        continue;
                    String[][] participantName = DatabaseManager.makeQuery("select display_name from savedUsers where account_id = '" + chatParticipantsID[i] + "';");
                    if(participantName == null || participantName.length == 0)
                    {
                        DatabaseManager.makeUpdate("insert into unknownUsers values('" + chatParticipantsID[i] + "');");
                        continue;
                    }
                    participantList = participantList + participantName[0][0] + ", ";
                }
                int indexOfExtraCommaSpace = participantList.lastIndexOf(", ");
                if(indexOfExtraCommaSpace != -1)
                {
                    participantList = participantList.substring(0, indexOfExtraCommaSpace);
                }
                chatHeaderParticipantOrOnlineLabel.setText(participantList);
                chatHeaderParticipantOrOnlineLabel.setToolTipText(participantList);
            }

            chatMessagesListScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            chatMessagesListScrollPanel.getVerticalScrollBar().setUnitIncrement(10);
            chatMessagesListScrollPanel.getViewport().setView(outsideChatMessagesContainer);
            revalidate();

            //System.out.println("Loaded messages: " + numberOfLoadedMessagesInActiveChat);
            String[][] updatedUnreadMessagesCount = DatabaseManager.makeQuery("select count(*) from chat" + activeChatComponentReference.getChatID() + " where sent_to_server = 2 and not from_account_id = '" + ConfigManager.getAccountID() + "';");
            if(updatedUnreadMessagesCount != null && Integer.parseInt(updatedUnreadMessagesCount[0][0]) > 0)
            {
                activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("reminder (2).png"))));
                activeChatComponentReference.getChatLastMessageLabel().setText(Integer.parseInt(updatedUnreadMessagesCount[0][0]) + " new message" + (Integer.parseInt(updatedUnreadMessagesCount[0][0]) > 1 ? "s" : ""));
                activeChatComponentReference.getChatLastMessageLabel().setToolTipText(activeChatComponentReference.getChatLastMessageLabel().getText());
            }

            if(updatedUnreadMessagesCount == null || Integer.parseInt(updatedUnreadMessagesCount[0][0]) == 0) {
                String[][] chatLastMessage = DatabaseManager.makeQuery("select message_content, from_account_id, sent_to_server from chat" + activeChatComponentReference.getChatID() + " where message_timestamp = (select max(message_timestamp) from chat" + activeChatComponentReference.getChatID() + ");");
                if (chatLastMessage != null && chatLastMessage.length == 1) {
                    activeChatComponentReference.getChatLastMessageLabel().setIcon(null);
                    //activeChatComponentReference.getChatLastMessageLabel().setText(chatLastMessage[0][0]);
                    if(chatType.equals("PERSONAL")) {
                        activeChatComponentReference.getChatLastMessageLabel().setText(chatLastMessage[0][0]);
                    }
                    else
                    {
                        String senderID = chatLastMessage[0][1];
                        if(senderID.equals("0000000000000000"))
                        {
                            activeChatComponentReference.getChatLastMessageLabel().setText(chatLastMessage[0][0]);
                        }
                        else if(senderID.equals(ConfigManager.getAccountID()))
                        {
                            activeChatComponentReference.getChatLastMessageLabel().setText("You: " + chatLastMessage[0][0]);
                        }
                        else
                        {
                            String[][] senderName = DatabaseManager.makeQuery("select display_name from savedUsers where account_id = '" + senderID + "';");
                            if(senderName == null || senderName.length == 0)
                            {
                                activeChatComponentReference.getChatLastMessageLabel().setText(senderID + ": " + chatLastMessage[0][0]);
                                DatabaseManager.makeUpdate("insert into unknownUsers values('" + senderID + "');");
                            }
                            else
                            {
                                activeChatComponentReference.getChatLastMessageLabel().setText(senderName[0][0] + ": " + chatLastMessage[0][0]);
                            }
                        }
                    }
                    if (chatLastMessage[0][1].equals(ConfigManager.getAccountID())) {
                        if (Integer.parseInt(chatLastMessage[0][2]) == 3) {
                            activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("double-check.png"))));
                        } else if (Integer.parseInt(chatLastMessage[0][2]) == 2) {
                            activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("sent.png"))));
                        } else if (Integer.parseInt(chatLastMessage[0][2]) == 1) {
                            activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("check-symbol.png"))));
                        } else {
                            activeChatComponentReference.getChatLastMessageLabel().setIcon(new ImageIcon(Objects.requireNonNull(ChatMainPanel.class.getClassLoader().getResource("time-left.png"))));
                        }
                    }
                    activeChatComponentReference.getChatLastMessageLabel().setToolTipText(chatLastMessage[0][0]);
                }
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int difference = insideChatMessagesContainer.getSize().height - chatMessagesListScrollPanel.getViewport().getExtentSize().height;
                    if(difference > 0)
                    {
                        chatMessagesListScrollPanel.getViewport().setViewPosition(new Point(0, difference));
                    }

                    if(extraParameters.length > 0 && Arrays.toString(extraParameters).contains(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_LOADING_OLD_MESSAGES + ""))
                    {
                        chatMessagesListScrollPanel.getVerticalScrollBar().setValue(chatMessagesListScrollPanel.getVerticalScrollBar().getMaximum() - messageListCurrentScrollBarPositionFromBottom);
                    }
                    else if(extraParameters.length > 0 && Arrays.toString(extraParameters).contains(KEEP_MESSAGE_LIST_AT_CURRENT_SCROLL_POSITION_WHEN_REFRESHING_FOR_ANY_REASON + ""))
                    {
                        if(!forceScrollDownMessageList)
                            chatMessagesListScrollPanel.getVerticalScrollBar().setValue(messageListCurrentScrollBarPositionFromTop);
                    }

                    chatMessagesListScrollPanel.getViewport().addChangeListener(loadOlderMessagesEventListener);

                    FlatAnimatedLafChange.stop();
                }
            });
        }
    }


    // Variables declaration - do not modify
    private JPopupMenu profileOptionsMenu;
    private JPopupMenu chatHeaderMoreOptionsMenu;
    private javax.swing.JLabel chatHeaderParticipantOrOnlineLabel;
    private javax.swing.JLabel appStatusColorLabel;
    private javax.swing.JPanel appStatusPanel;
    private javax.swing.JLabel appStatusTextLabel;
    private javax.swing.JLabel chatHeaderIconLabel;
    private javax.swing.JButton chatHeaderMoreOptionsButton;
    private javax.swing.JLabel chatHeaderNameLabel;
    private javax.swing.JPanel chatHeaderPanel;
    private javax.swing.JPanel chatListControlsPanel;
    private javax.swing.JPanel chatListPanel;
    private javax.swing.JScrollPane chatListScrollPanel;
    private javax.swing.JScrollPane chatMessagesListScrollPanel;
    private javax.swing.JPanel chatMessagesPanel;
    private javax.swing.JButton chatSendMessageButton;
    private javax.swing.JTextField chatSendMessageField;
    private javax.swing.JToggleButton chatSendMessageOtherTypesToggleButton;
    private javax.swing.JPanel chatSendMessagePanel;
    private javax.swing.JToggleButton deleteChatButton;
    private javax.swing.JButton newChatButton;
    private javax.swing.JButton profileButton;
    // End of variables declaration
}
