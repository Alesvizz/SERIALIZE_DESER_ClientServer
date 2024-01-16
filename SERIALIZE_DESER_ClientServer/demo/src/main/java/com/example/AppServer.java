package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class AppServer {
    ServerSocket server = null;
    Socket client = null;
    String stringaInvio;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    XmlMapper xmlMapper = new XmlMapper();
    ObjectMapper jsonMapper = new ObjectMapper();
    String scelta;

    public Socket attenti(){
        try{
            System.out.println("SERVER partito in esecuzione ...");

            server = new ServerSocket(6789);

            client = server.accept();

            server.close();

            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));

            outVersoClient = new DataOutputStream(client.getOutputStream());
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server!");
            System.exit(1);
        }
        return client;
    }

    public void comunica(){
        try{

            Persona p = new Persona("Mario", "Franchi", 221);

            //RICEVE SCELTA SERIALIZZAZIONE
            System.out.println("Riceve scelta serializzazione da client");
            scelta = inDalClient.readLine();
            System.out.println(scelta);
            
            switch (scelta) {
                case "XML":
                    stringaInvio = xmlMapper.writeValueAsString(p);
                    System.out.println(stringaInvio);
                    break;
            
                case "JSON": 
                    stringaInvio = jsonMapper.writeValueAsString(p);
                    break;
            }
            
            

            //INVIO STRINGA A CLIENT
            System.out.println("Invio risultato al client...");
            outVersoClient.writeBytes(String.valueOf(stringaInvio) + '\n');
                    
            
            System.out.println("SERVER: fine elaborazione!");
            client.close();
        }catch(Exception e){

        }
    }

    public static void main(String args[]){
        AppServer servente = new AppServer();
        servente.attenti();
        servente.comunica();
    }
}
