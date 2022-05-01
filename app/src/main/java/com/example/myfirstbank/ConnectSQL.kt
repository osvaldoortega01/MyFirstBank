package com.example.myfirstbank

import android.os.StrictMode
import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectSQL {
    private val ip="myfirstbank.database.windows.net"
    private val db="myfirstbankdb"
    private val username="dispositivos"
    private val password="zHpJ7y9BZ4S66Qt"

    fun dbConn(): Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection? = null
        val connString : String
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance()
            connString = "jdbc:jtds:sqlserver://myfirstbank.database.windows.net:1433;DatabaseName=myfirstbankdb;user=dispositivos@myfirstbank;password=zHpJ7y9BZ4S66Qt;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=request"
            conn = DriverManager.getConnection(connString)
        } catch (ex: SQLException){
            Log.e("Error: ", ex.message!!)
        } catch (ex1: ClassNotFoundException)
        {
            Log.e("Error: ", ex1.message!!)
        } catch (ex2: Exception){
            Log.e("Error: ", ex2.message!!)
        }
        return conn
    }
}