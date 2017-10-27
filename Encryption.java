import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import javax.crypto.*;

public class Encryption {
    static HashMap<Integer, String> idfilemap = new HashMap<Integer, String>();
    static HashMap<String, Integer> fileidmap = new HashMap<String, Integer>();
    public static void init(final File folder) throws Exception {
	
	int i = 1;
	for (final File fileEntry : folder.listFiles()) {
	    idfilemap.put(i, fileEntry.getName());
	    fileidmap.put(fileEntry.getName(),i);
	    i++;
	}
    }

    //1. encrypt files and store on server
    //2. Extract keywords from each file and store  map(keyword,LinkedList of files containing the keyword)
    //3. Create emap(encrypted keyword, content id of file)
    
    public static void encrypt(final File folder) throws Exception {
	KeyGen kg = KeyGen.read("/home/nikhil/Desktop/Project localmachine/ser/keys.ser");
	HashMap<String, LinkedList<String>> map = new HashMap<String, LinkedList<String>>();	
	for (final File fileEntry : folder.listFiles()) {
	    if (fileEntry.isDirectory()) continue;

	    // encrypting files and storing on the server

	    FileInputStream fis = new FileInputStream(fileEntry);
	    byte[] plainText = new byte[(int)fileEntry.length()];
	    fis.read(plainText);
	    Cipher aesCipher = Cipher.getInstance("AES");
	    aesCipher.init(Cipher.ENCRYPT_MODE, kg.Key2());
	    byte[] byteCipherText = aesCipher.doFinal(plainText);
	    File file = new File("/home/nikhil/Desktop/Project server/" + fileidmap.get(fileEntry.getName()));
	    FileOutputStream fos = new FileOutputStream(file);
	    fos.write(byteCipherText);
	    fis.close();
	    fos.close();

	    // extracting keywords from each file and storing in map(keyword,LinkedList of files containing the keyword)

	    Scanner scn = new Scanner(fileEntry);
	    for(int i = 0; i < 10 && scn.hasNext(); i++) {
		String s = scn.next();		
		if(map.containsKey(s)) {
		    map.get(s).add(fileEntry.getName());
		}
		else {
		    LinkedList<String> l = new LinkedList<String>();
		    l.add(fileEntry.getName());
		    map.put(s,l);
		}
	    }
	}

	// create map(encrypted keyword, content id of file in which keyword is present)

	HashMap<String, Integer> emap = new HashMap<String, Integer>();
	String buff = null;
	for(HashMap.Entry m : map.entrySet()) {
	    int j = 1;
	    LinkedList<String> ls = (LinkedList<String>) m.getValue();	    
	    for (String s: ls) {
		String w = (String)m.getKey() + j;
		j++;
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.ENCRYPT_MODE, kg.Key1());
		byte[] plainText = w.getBytes();
		byte[] byteCipherText = aesCipher.doFinal(plainText);
		emap.put(new String(byteCipherText), fileidmap.get(s));

		/*if( w.equals("I1")) {
		    System.out.println(new String(byteCipherText));
		    buff = new String(byteCipherText);*/
		}
	    }	    
	}
	FileOutputStream f = new FileOutputStream("/home/nikhil/Desktop/Project server/ser/index.ser");
	ObjectOutputStream o = new ObjectOutputStream(f);
	o.writeObject(emap);
	o.close();
	System.out.println(emap.get(buff));
    }

    public static void writeDocIDFileNameMapping() throws Exception {		    
	FileOutputStream fos = new FileOutputStream("/home/nikhil/Desktop/Project localmachine/ser/idfilemap.ser");
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	oos.writeObject(idfilemap);
	oos.close();
    }
    public static void main(String[] args) throws Exception{
	Encryption.init(new File("/home/nikhil/Desktop/Project files"));
	Encryption.encrypt(new File("/home/nikhil/Desktop/Project files"));
	Encryption.writeDocIDFileNameMapping();
    }	
}

			       
			       
	
			         
