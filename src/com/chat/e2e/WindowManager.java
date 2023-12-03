package com.chat.e2e;

import javax.swing.*;

public class WindowManager
{
    private static String currentPanel = "";
    private static AppWindowFrame mainFrame;

    //TODO: While changing from lets say panel 1 to panel 2, clear the reference of panel 1 to null from its respective
    // variable, and add reference of panel 2 to its respective variable. Not clearing previous panel reference to null
    // means it won't be garbage collected, and would needlessly consume memory. Especially in case of switching from
    // chat panel to other panels, if the previous chat panel reference is not set to null after changing to the other
    // panel, then the chat panel along with all its loaded data will remain in memory.
    private static UserLoginPanel loginPanelReference;
    private static UserRegisterPanel registerPanelReference;
    private static UserSelectionPanel selectionPanelReference;
    private static ChatMainPanel chatPanelReference;

    public static String getCurrentPanel()
    {
        return currentPanel;
    }

    public static ChatMainPanel getChatPanelReference()
    {
        return chatPanelReference;
    }


    synchronized public static void start(AppWindowFrame parentFrame)
    {
        mainFrame = parentFrame;
        if(ConfigManager.getAccountSessionID().equals("null"))
        {
            currentPanel = "UserSelectionPanel";
            selectionPanelReference = new UserSelectionPanel();
            mainFrame.addToMainPanel(selectionPanelReference, true);
            NetworkManager.connectToServer();
        }
        else
        {
            //TODO: Add functionality to connect and login to server, then initialise main chat panel and show it
            // First create and initialise chat panel from database and show it
            // Then connect to server and try to login to old session using the session id
            // if login is successful, update database from server and update chat panel as required
            // if login fails, check if it due to network issues or server declining the session id
            // if due to network issue, stay in offline mode and periodically try to go online
            // the functionality for periodically checking to go online needs to be added
            // if login fails due to server declining session id, force user to logout and go to user selection

            currentPanel = "UserChatPanel";
            chatPanelReference = new ChatMainPanel();
            mainFrame.addToMainPanel(chatPanelReference, true);

            NetworkManager.connectToServer();
            SwingWorker<Void, Void> initialLoginWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Boolean success = NetworkManager.loginToOldSession(ConfigManager.getAccountID(), ConfigManager.getAccountSessionID());
                    if(success == null)
                        return null;
                    if(!success)
                    {
                        JOptionPane.showMessageDialog(Main.getMainFrame(), "You have been force logged out due to invalid session ID.\nYou will be moved back to the main menu.\nPlease try logging in again.",
                                "User logged out", JOptionPane.ERROR_MESSAGE);
                        changeFromChatToSelection(true);
                    }
                    return null;
                }
            };
            initialLoginWorker.execute();
        }
    }

    synchronized public static void changeFromSelectionToRegister()
    {
        mainFrame.getAppWindowPanel().removeAll();
        selectionPanelReference = null;

        currentPanel = "UserRegisterPanel";
        registerPanelReference = new UserRegisterPanel();
        mainFrame.addToMainPanel(registerPanelReference, false);
    }

    synchronized public static void changeFromSelectionToLogin()
    {
        mainFrame.getAppWindowPanel().removeAll();
        selectionPanelReference = null;

        currentPanel = "UserLoginPanel";
        loginPanelReference = new UserLoginPanel();
        mainFrame.addToMainPanel(loginPanelReference, false);
    }

    synchronized public static void changeFromRegisterToSelection()
    {
        mainFrame.getAppWindowPanel().removeAll();
        registerPanelReference = null;

        currentPanel = "UserSelectionPanel";
        selectionPanelReference = new UserSelectionPanel();
        mainFrame.addToMainPanel(selectionPanelReference, true);
    }

    synchronized public static void changeFromLoginToSelection()
    {
        mainFrame.getAppWindowPanel().removeAll();
        loginPanelReference = null;

        currentPanel = "UserSelectionPanel";
        selectionPanelReference = new UserSelectionPanel();
        mainFrame.addToMainPanel(selectionPanelReference, true);
    }

    synchronized public static void changeFromLoginToChat()
    {
        mainFrame.getAppWindowPanel().removeAll();
        loginPanelReference = null;

        currentPanel = "UserChatPanel";
        chatPanelReference = new ChatMainPanel();
        mainFrame.addToMainPanel(chatPanelReference, true);
    }

    synchronized public static void changeFromChatToSelection(boolean forceLogout)
    {
        if(!forceLogout) {
            Boolean success = NetworkManager.logoutUser();
        }
        NetworkManager.setLoggedIn(false);
        ConfigManager.setAccountSessionID("null");
        ConfigManager.readConfig();
        DatabaseManager.initializeDB();

        mainFrame.getAppWindowPanel().removeAll();
        chatPanelReference.getOtherThreadsCrashHandler().cancel();
        chatPanelReference.getNetworkStatusUpdaterThread().cancel(true);
        if(!chatPanelReference.getNetworkStatusUpdaterThread().isDone())
            chatPanelReference.getNetworkStatusUpdaterThread().cancel(true);
        chatPanelReference.getReloginThread().cancel(true);
        if(!chatPanelReference.getReloginThread().isDone())
            chatPanelReference.getReloginThread().cancel(true);
        chatPanelReference.getNewChatAndMessageReceiverThread().cancel(true);
        if(!chatPanelReference.getNewChatAndMessageReceiverThread().isDone())
            chatPanelReference.getNewChatAndMessageReceiverThread().cancel(true);
        chatPanelReference.getPeriodicPanelUpdatorTimer().cancel();
        chatPanelReference.getNewMessageSenderThread().cancel(true);
        if(!chatPanelReference.getNewMessageSenderThread().isDone())
            chatPanelReference.getNewMessageSenderThread().cancel(true);
        chatPanelReference.getMessageReadReceiptsUpdatorThread().cancel(true);
        if(!chatPanelReference.getMessageReadReceiptsUpdatorThread().isDone())
            chatPanelReference.getMessageReadReceiptsUpdatorThread().cancel(true);
        chatPanelReference = null;

        currentPanel = "UserSelectionPanel";
        selectionPanelReference = new UserSelectionPanel();
        mainFrame.addToMainPanel(selectionPanelReference, true);
    }

    synchronized public static void closeAppWindow()
    {
        //System.out.println("Close called");

        if(currentPanel.equals("UserSelectionPanel"))
        {
            Main.getMainFrame().setVisible(false);
            NetworkManager.disconnectFromServer(true);
            DatabaseManager.closeDB();
            Main.getMainFrame().dispose();
        }
        else if(currentPanel.equals("UserRegisterPanel"))
        {
            Main.getMainFrame().setVisible(false);
            NetworkManager.disconnectFromServer(true);
            DatabaseManager.closeDB();
            Main.getMainFrame().dispose();
        }
        else if(currentPanel.equals("UserLoginPanel"))
        {
            Main.getMainFrame().setVisible(false);
            NetworkManager.disconnectFromServer(true);
            DatabaseManager.closeDB();
            Main.getMainFrame().dispose();
        }
        else if(currentPanel.equals("UserChatPanel"))
        {
            chatPanelReference.hideMessagesPanel();

            Main.getMainFrame().setVisible(false);
            chatPanelReference.getOtherThreadsCrashHandler().cancel();
            chatPanelReference.getNetworkStatusUpdaterThread().cancel(true);
            chatPanelReference.getReloginThread().cancel(true);
            chatPanelReference.getNewChatAndMessageReceiverThread().cancel(true);
            chatPanelReference.getNewMessageSenderThread().cancel(true);
            chatPanelReference.getMessageReadReceiptsUpdatorThread().cancel(true);
            chatPanelReference.getPeriodicPanelUpdatorTimer().cancel();
            NetworkManager.disconnectFromServer(true);
            DatabaseManager.closeDB();
            Main.getMainFrame().dispose();
        }

        //Force exit if safe exit doesn't work above
        try
        {
            Thread.sleep(5000);
        }
        catch(Exception e)
        {

        }
        System.exit(0);
    }
}
