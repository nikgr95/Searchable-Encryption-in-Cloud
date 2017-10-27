import java.io.*;
import javax.crypto.*;
import java.util.*;

public class Decryption {

    public static void decrypt() throws Exception{
	KeyGen kg = KeyGen.read("/home/nikhil/Desktop/Project localmachine/ser/keys.ser");
	File folder = new File("/home/nikhil/Desktop/Project localmachine/encrypted files");
	FileInputStream fis = new FileInputStream("/home/nikhil/Desktop/Project localmachine/ser/idfilemap.ser");
	ObjectInputStream ois = new ObjectInputStream(fis);	
	HashMap<Integer, String> idfilemap = new HashMap<Integer, String>();
	idfilemap = (HashMap<Integer, String>) ois.readObject();	

	// Decrypting the retrieved files in the localmachine
	for (final File fileEntry : folder.listFiles()) {
	    if (fileEntry.isDirectory()) continue;
	    Cipher aesCipher2 = Cipher.getInstance("AES");
	    aesCipher2.init(Cipher.DECRYPT_MODE, kg.Key2());
	    FileInputStream fin = new FileInputStream(fileEntry);
	    FileOutputStream fout = new FileOutputStream("/home/nikhil/Desktop/Project localmachine/" + idfilemap.get(Integer.parseInt(fileEntry.getName())));
	    byte[] byteCipherText = new byte[(int)fileEntry.length()];
	    fin.read(byteCipherText);
	    byte[] bytePlainText = aesCipher2.doFinal(byteCipherText);
	    fout.write(bytePlainText);
	}
	
    }

    public static void main(String[] args) throws Exception{
	Decryption.decrypt();
    }

}
