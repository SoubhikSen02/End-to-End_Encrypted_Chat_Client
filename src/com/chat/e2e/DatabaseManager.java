package com.chat.e2e;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager
{
    private static String appFolderPath;
    private static String dbFolderPath;
    private static String dbFilePath;

    private static Connection dbConnection = null;

    static
    {
        initializeDB();
    }

    synchronized public static void initializeDB()
    {
        appFolderPath = ConfigManager.getAppFolderPath();
        dbFolderPath = appFolderPath + "\\database";
        dbFilePath = dbFolderPath + "\\" + ConfigManager.getAccountID() + "_main.db";

        File appFolder = new File(appFolderPath);
        if(!appFolder.exists())
        {
            boolean success = appFolder.mkdir();
            if(!success)
                return;
        }

        File dbFolder = new File(dbFolderPath);
        if(!dbFolder.exists())
        {
            boolean success = dbFolder.mkdir();
            if(!success)
                return;
        }

        try
        {
            if(dbConnection != null)
                dbConnection.close();
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        }
        catch(Exception e)
        {
            return;
        }

        //TODO: Initialize database by checking whether all tables exist and properly filled with initial info as required
        try{
            if(!dbConnection.getMetaData().getTables(null, null, "chat", null).next())
            {
                makeUpdate("create table chat(chat_id number(32) primary key not null, encryption_key char(44) not null, encryption_IV char(16) not null, chat_type varchar(8) not null, chat_name varchar(64) not null, chat_participants varchar(16384) not null, last_message_timestamp number(64) not null);");
            }
            else
            {
                String[][] chatList = makeQuery("select chat_id from chat;");
                if(chatList != null)
                {
                    DatabaseMetaData tableList = dbConnection.getMetaData();
                    for(int i = 0; i < chatList.length; i++)
                    {
                        if(!tableList.getTables(null, null, "chat" + chatList[i][0], null).next())
                        {
                            makeUpdate("create table chat" + chatList[i][0] + "(message_id number(64) primary key not null, from_account_id char(16) not null, sent_to_server integer(2) not null, message_timestamp number(64) not null, message_type varchar(16) not null, message_content varchar(1048576) not null);");
                        }
                    }
                }
            }

            if(!dbConnection.getMetaData().getTables(null, null, "savedUsers", null).next())
            {
                makeUpdate("create table savedUsers(account_id char(16) primary key not null, display_name varchar(64) not null);");
            }

            if(!dbConnection.getMetaData().getTables(null, null, "unsentReadReceipts", null).next())
            {
                makeUpdate("create table unsentReadReceipts(chat_id number(32) not null, message_id number(64) not null, primary key(chat_id, message_id));");
            }
        }
        catch(Exception e)
        {
            return;
        }
    }

    synchronized public static void closeDB()
    {
        try
        {
            if(dbConnection != null)
                dbConnection.close();
        }
        catch(Exception e)
        {

        }
    }

    synchronized public static boolean makeUpdate(String statement)
    {
        //System.out.println(statement);
        if(dbConnection == null)
            return false;
        try {
            Statement updateStatement = dbConnection.createStatement();
            updateStatement.setQueryTimeout(30);

            updateStatement.executeUpdate(statement);

            updateStatement.close();
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    synchronized public static String[][] makeQuery(String statement)
    {
        ResultSet queryResultSet;
        List<String[]> queryResultStringList = new ArrayList<>();
        String[][] queryResultString2dArray;
        if(dbConnection == null)
            return null;
        try
        {
            Statement updateStatement = dbConnection.createStatement();
            updateStatement.setQueryTimeout(30);

            queryResultSet = updateStatement.executeQuery(statement);

            //queryResultSet.last();
            //int numberOfRows = queryResultSet.getRow();
            //queryResultSet.beforeFirst();
            int numberOfColumns = queryResultSet.getMetaData().getColumnCount();

            for(int i = 0; queryResultSet.next(); i++)
            {
                queryResultStringList.add(new String[numberOfColumns]);
                for(int j = 0; j < numberOfColumns; j++)
                {
                    queryResultStringList.get(i)[j] = queryResultSet.getString(j + 1);
                    //queryResultString2dArray[i][j] = queryResultSet.getString(j + 1);
                }
            }

            int numberOfRows = queryResultStringList.size();

            queryResultString2dArray = new String[numberOfRows][numberOfColumns];

            for(int i = 0; i < numberOfRows; i++)
            {
                for(int j = 0; j < numberOfColumns; j++)
                {
                    queryResultString2dArray[i][j] = queryResultStringList.get(i)[j];
                }
            }

            updateStatement.close();
        }
        catch (Exception e)
        {
            //System.out.println(e);
            return null;
        }
        return queryResultString2dArray;
    }
}
