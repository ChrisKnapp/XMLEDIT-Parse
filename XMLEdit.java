import java.io.*;
import java.awt.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;

// Import-Klassen f�r das Fenster

import java.awt.event.*;
import javax.swing.*;

public class XMLEdit extends DefaultHandler

{

XMLEditFenster fenster;
JErrorFenster ErrorFenster;

    public static void main(String param[])
    {

        DefaultHandler handler= new XMLEdit(param);

   }

public XMLEdit(String param[])
{
	DefaultHandler handler=(DefaultHandler)this;
    ErrorFenster=new JErrorFenster();
    fenster=new XMLEditFenster(this,handler,ErrorFenster);
 
    if (param.length!=0)
      {
        loading(new File(param[0]));
        parsing(handler, new File(param[0]));
      }

}

public void parsing(DefaultHandler handler, File datei)
{

         boolean laden=datei.canRead();

        if (laden)
         {
          try
           {
             SAXParserFactory factory = SAXParserFactory.newInstance();
             SAXParser saxParser = factory.newSAXParser();
             saxParser.parse(datei,handler);
           }

          catch (SAXParseException error) 
           {
            ErrorFenster.ausgabe("\n+++Parse Error+++"+ "\nZeile: " + error.getLineNumber() + "\nDatei: " + error.getSystemId());
            ErrorFenster.ausgabe("\n" + error.getMessage() );


        }

          catch (Throwable t)
           {
            t.printStackTrace();
           }


        }
       else
       {
         ErrorFenster.ausgabe("File does not exist!");

       }

}

public void loading(File datei)
{
  	fenster.laden(datei);
}

public void startDocument()
{
  ErrorFenster.ausgabe("Parse process starts; begin of document"+"\n");
 
}
public void endDocument() 
{
          ErrorFenster.ausgabe("Parse ends; end of document"+"\n");
          ErrorFenster.ausgabe("No error"+"\n");
 
}

public void error(SAXParseException e) throws SAXParseException
{
    throw e;
}



}


// Windows

class XMLEditFenster extends JFrame implements WindowListener, ActionListener
{

 JTextArea textbereich;
 DefaultHandler handler;
 XMLEdit ed;
 JErrorFenster errorFenster;
 File xmlDatei=new File ("temp.xml");

 public XMLEditFenster(XMLEdit e, DefaultHandler h, JErrorFenster ErrorFenster)
  {
  	
  	   try
       {
         String LookAndFeel=UIManager.getSystemLookAndFeelClassName();      
         UIManager.setLookAndFeel(LookAndFeel);
              }
       catch(Exception ex)
           {
           	   System.out.println("Error " + ex); 
           }
           
  	ed=e;
  	handler=h;
  	errorFenster=ErrorFenster;
  	    setSize (800,600);
        setLocation (0,0);
        setTitle ("XMLEditor with parse");
   //menu items
        
 
 

  JMenuBar Menueleiste=new JMenuBar(); 

  JMenu MenuePunkt1=new JMenu ("File"); 

  JMenuItem neu=new JMenuItem ("New XML File");

  MenuePunkt1.add(neu); 

  JMenuItem laden=new JMenuItem ("Load XML File"); 

  MenuePunkt1.add(laden);  

  JMenuItem speichern=new JMenuItem ("Save XML File"); 

  MenuePunkt1.add(speichern);  

  MenuePunkt1.addSeparator(); 

  JMenuItem beenden=new JMenuItem ("Exit Program");

  MenuePunkt1.add(beenden);

   JMenu MenuePunkt2=new JMenu ("Parse XML File");

   JMenuItem parsen=new JMenuItem ("Parse XML File"); 

  MenuePunkt2.add(parsen);  
  
  JMenu MenuePunkt3=new JMenu ("Info"); 

  JMenuItem infos=new JMenuItem ("© 2025 C.Knapp");

  MenuePunkt3.add(infos); 
  
                                  Menueleiste.add(MenuePunkt1); 
     

                                  Menueleiste.add(MenuePunkt2); 
                                  
                                  Menueleiste.add(MenuePunkt3);
    
     

                                  setJMenuBar(Menueleiste); 

                                  

        textbereich=new JTextArea();
        Container content=getContentPane();
        content.add (textbereich);
        
      
     beenden.addActionListener(this);
     neu.addActionListener(this);
     speichern.addActionListener(this);
         laden.addActionListener(this);
    parsen.addActionListener(this);
        
        
        addWindowListener(this);
       
   setVisible(true); 
 }

public void ausgabe(String s)
{

  textbereich.append(s);

}


  // Nun die Ereignisse des WindowListener
  public void windowClosing(WindowEvent evt) 
   {
   	    dispose();
   	    System.exit(0);
    }
   public void windowOpened(WindowEvent evt)     {    }
   public void windowIconified(WindowEvent evt)     {    }
   public void windowDeiconified(WindowEvent evt)     {    }
   public void windowClosed(WindowEvent evt)     {    }
   public void windowActivated(WindowEvent evt)    {    }
   public void windowDeactivated(WindowEvent evt)     {    }


//Nun der ActionListener

public void actionPerformed(ActionEvent e)
{

 if (e.getSource() instanceof JMenuItem)
  {
  	String menue=e.getActionCommand();
  	if (menue.equals("Exit"))
  	  {
  	    dispose();
   	    System.exit(0);
      } 
     if (menue.equals("Parse XML File"))
  	  {
   	  	
   	  	speichern(xmlDatei);
   	  	errorFenster.neu();
  	  	ed.parsing(handler, xmlDatei);
      } 
    if (menue.equals("New XML File"))
  	  {
   	  	textbereich.setText("");
  	  	errorFenster.neu();
       } 
      if (menue.equals("Load XML File"))
  	  {
     
       JFileChooser auswahl = new JFileChooser();
       int returnVal = auswahl.showOpenDialog(this);
       if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
         File datei=auswahl.getSelectedFile();
         laden(datei);
         }
       
      } 
      if (menue.equals("Save XML File"))
  	  {
     
       JFileChooser auswahl = new JFileChooser();
       int returnVal = auswahl.showSaveDialog(this);
       if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
         File datei=auswahl.getSelectedFile();
         speichern(datei);
        }
       
      } 
	
  }	
}

public void laden (File Datei)
{
	xmlDatei=Datei;
	
	String Zeile="";
       try 
        {    
          FileReader Eingabestrom= new FileReader(Datei);
                   
          BufferedReader input = new BufferedReader(Eingabestrom);
          textbereich.setText("");
          while((Zeile= input.readLine()) != null)
           {
             textbereich.append(Zeile+"\n");
            
           }
          input.close();
 
        }
        catch (IOException e) 
       { 
  		 System.out.println("Error: "+e);
        } 

}
public void speichern (File Datei)
{
       try 
        {    
          FileWriter Ausgabestrom= new FileWriter(Datei);
                   
          BufferedWriter output = new BufferedWriter(Ausgabestrom);
          String inhalt=textbereich.getText();
          output.write(inhalt);

          output.close();
 

        }
        catch (IOException e) 
       { 
 		 System.out.println("Error: "+e);
       } 
}        
}

// Jetzt kommt die Klasse f�r das Fenster

class JErrorFenster extends JFrame implements WindowListener
{

JTextArea textbereich_fehler;

 public JErrorFenster()
  {
  	
  	   try
       {
         String LookAndFeel=UIManager.getSystemLookAndFeelClassName();      
         UIManager.setLookAndFeel(LookAndFeel);
              }
       catch(Exception e)
           {
           	   System.out.println("Error " + e); 
           }

        setSize (800,200);
        setLocation (100,600);
        setTitle ("XMLEdit Error Messages");
        textbereich_fehler=new JTextArea();
        Container content=getContentPane();
        content.add (textbereich_fehler);
          
        
        addWindowListener(this);
    setVisible(true); 
 }

public void ausgabe(String s)
{

  textbereich_fehler.append(s);

}
public void neu ()
{

  textbereich_fehler.setText("");

}


  // Nun die Ereignisse des WindowListener
  public void windowClosing(WindowEvent evt) 
   {
   setVisible(false);
      }
   public void windowOpened(WindowEvent evt)     {    }
   public void windowIconified(WindowEvent evt)     {    }
   public void windowDeiconified(WindowEvent evt)     {    }
   public void windowClosed(WindowEvent evt)     {    }
   public void windowActivated(WindowEvent evt)    {    }
   public void windowDeactivated(WindowEvent evt)     {    }

 

}