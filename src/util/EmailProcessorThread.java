/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author torsello
 */
public class EmailProcessorThread extends Thread {   
    
    private EmailSender sender=new EmailSender();
    
    public EmailProcessorThread()
    {
        
        
        //fake infos please look to EmailSender.java send() function
        sender.setServer("smtp.gmail.com");
        sender.setUser("unisalentopps.2018@gmail.com");
        sender.setPassword("havana2017");
        sender.setFrom("unisalentopps.2018@gmail.com");
    }
    
    
    @Override
    public void start()
    {
        new Thread(new Runnable() {
            public void run(){
                sender.send();
            }
        }).start();
        
    }

    /**
     * @return the sender
     */
    public EmailSender getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(EmailSender sender) {
        this.sender = sender;
    }
}
