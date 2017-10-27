import javax.crypto.*;
import java.io.*;
import java.util.*;

public class Search {

    public static void searchToken(String keyword) throws Exception {
	KeyGen kg = KeyGen.read("/home/nikhil/Desktop/Project localmachine/ser/keys.ser");
	FileInputStream fis = new FileInputStream("/home/nikhil/Desktop/Project localmachine/ser/idfilemap.ser");
	ObjectInputStream ois = new ObjectInputStream(fis);
	HashMap<Integer, String> m = (HashMap<Integer, String>) ois.readObject();
	LinkedList<String> l = new LinkedList<String>();
	ois.close();
	
	fis = new FileInputStream("/home/nikhil/Desktop/Project server/ser/index.ser");
	ois = new ObjectInputStream(fis);
	HashMap<String,Integer> emap = (HashMap<String,Integer>) ois.readObject();
	ois.close();

	// Creating linked list of encrypted keywords to send to the server
	for(int i = 1; i <= m.size(); i++) {
	    String s = keyword + i;
	    Cipher aesCipher = Cipher.getInstance("AES");
	    aesCipher.init(Cipher.ENCRYPT_MODE, kg.Key1());
	    byte[] byteCipherText = aesCipher.doFinal(s.getBytes());
	    l.add(new String(byteCipherText));
	}
	
	FileOutputStream fos = new FileOutputStream("/home/nikhil/Desktop/Project server/ser/searchToken.ser");
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	oos.writeObject(l);

	/*	System.out.println("emap:\n");
	for(HashMap.Entry mm : emap.entrySet()) {
	    Cipher aesCipher2 = Cipher.getInstance("AES");
	    aesCipher2.init(Cipher.DECRYPT_MODE, kg.Key1());
	    byte[] bytePlainText = aesCipher2.doFinal((byte[])mm.getKey());
	    if (new String(bytePlainText).equals("I1")) System.out.println(new String((byte[])mm.getKey()));
	    //	    System.out.println(new String(bytePlainText));	   
	    }*/
    
    	//	for( byte[] b : l) {
	    
    }

    public static void search() throws Exception {	
	FileInputStream fis = new FileInputStream("/home/nikhil/Desktop/Project server/ser/searchToken.ser");
	ObjectInputStream ois = new ObjectInputStream(fis);
	LinkedList<String> tokens = (LinkedList<String>) ois.readObject();
	ois.close();
	fis = new FileInputStream("/home/nikhil/Desktop/Project server/ser/index.ser");
	ois = new ObjectInputStream(fis);
	HashMap<String,Integer> emap = (HashMap<String,Integer>) ois.readObject();
	ois.close();

	//send files which are found using searchTken to the localmachine
	for( String b : tokens) {
	    if (!emap.containsKey(b)) break;
	    int i = emap.get(b);
	    //System.out.println(emap.get(b));
	    
	    File file = new File("/home/nikhil/Desktop/Project server/" + i);
	    fis = new FileInputStream(file);
	    File file2 = new File("/home/nikhil/Desktop/Project localmachine/encrypted files/" + i);
	    FileOutputStream fos = new FileOutputStream(file2);
	    byte[] buffer = new byte[(int)file.length()];
	    fis.read(buffer);
	    fos.write(buffer);
	}
    }				     
    public static void main(String[] args) throws Exception {
	Search.searchToken(args[0]);
	Search.search();
    }
}
