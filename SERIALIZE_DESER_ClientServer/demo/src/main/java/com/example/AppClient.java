package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class AppClient {

    String nomeServer = "localHost";
    int portaServer = 6789;
    Socket mioSocket;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;
    String stringaRicevutaDalServer;
    XmlMapper xmlMapper = new XmlMapper();
    BufferedReader tastiera;
    String scelta;
    boolean nonCorretto;
    Persona p;
    ObjectMapper jsonMapper = new ObjectMapper();

    public Socket connetti(){
        System.out.println("CLIENT partito in esecuzione...");

        try {

            tastiera = new BufferedReader(new InputStreamReader(System.in));
            mioSocket = new Socket(nomeServer, portaServer);

            outVersoServer = new DataOutputStream(mioSocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(mioSocket.getInputStream()));
        
        }
        catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println("Errore durante la connessione! \n");
                System.exit(1);
        }
        return mioSocket;
    }

    public void comunica(){
        try {
            
           //INVIO SCELTA SERIALIZZAZIONE
           do {
            menu();

            scelta = tastiera.readLine();

            switch (scelta) {

                case "1": 
                        nonCorretto = false;
                        outVersoServer.writeBytes("XML" + "\n");
                        System.out.println("\nScelta confermata!");
                        break;
                case "2": 
                        nonCorretto = false;
                        outVersoServer.writeBytes("JSON" + "\n");
                        System.out.println("\nScelta confermata!");
                        break;
                default: System.out.println("ERRORE. Selezionare scelta valida!\n");
                        break;
            }   
           } while (nonCorretto);
            
            //ASPETTO STRINGA DA DESERIALIZZARE
            stringaRicevutaDalServer = inDalServer.readLine();
            System.out.println("Risposta dal server: " + stringaRicevutaDalServer + "\n");

            switch (scelta) {
                case "1":
                    p = xmlMapper.readValue(stringaRicevutaDalServer, Persona.class);
                    break;
            
                case "2":
                    p = jsonMapper.readValue(stringaRicevutaDalServer, Persona.class);
                    break;
            }
            

            System.out.println(p);
            
            System.out.println("CLIENT: termina elaborazione e chiude connessione");
            mioSocket.close();
        
        } catch (Exception e) {

            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione col server!");
            System.exit(1);
        }
    }

    public static void main( String[] args ){
        AppClient cliente = new AppClient();
        cliente.connetti();
        cliente.comunica();
    }

    public void menu (){
        System.out.println("\n--MENU--\n\n");
        System.out.println("1 - Serializzazione in xml\n");
        System.out.println("2 - Serializzazione in JSON\n");
    }
}

