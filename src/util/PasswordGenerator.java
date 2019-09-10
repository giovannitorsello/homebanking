/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author torsello
 */
public class PasswordGenerator {
    
    /**
     *
     * @param lenght
     * @return
     */
    static public String GeneraPassCasuale(int lenght)
    {
        String pass="";
        //String charArray[]={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","y","w","z","1","2","3","4","5","6","7","8","9"};
        String charArray[]={"1","2","3","4","5","6","7","8","9"};
        int i=0;
        while(i<lenght)
        {
            int index=(int) (((float)charArray.length) * (float)java.lang.Math.random());
            pass+=charArray[index];
            i++;
        }
        pass=pass.toUpperCase();
        return pass;
    }
}
