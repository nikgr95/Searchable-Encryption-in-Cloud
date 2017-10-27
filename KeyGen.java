import javax.crypto.*;
import java.util.Base64;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class KeyGen implements Serializable{

    private static final long serialVersionUID = 1L;
    private SecretKey key1 = null;
    private SecretKey key2 = null;

    public SecretKey Key1() {
	return key1;
    }

    public SecretKey Key2() {
	return key2;
    }
    
    public void generate() throws Exception {
	KeyGenerator generator = KeyGenerator.getInstance("AES");
	generator.init(128);
	key1 = generator.generateKey();
	key2 = generator.generateKey();
    }

    public void store(String address) throws Exception {
	FileOutputStream fos = new FileOutputStream(address);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	oos.writeObject(this);
	oos.close();
    }

    public static KeyGen read(String address) throws Exception {	
	FileInputStream fis = new FileInputStream(address);
	ObjectInputStream ois = new ObjectInputStream(fis);
	return (KeyGen) ois.readObject();
    }
    public static void main(String[] args) throws Exception{
	/*KeyGen gen = new KeyGen();
	gen.generate();
	String encodedKey1 = Base64.getEncoder().encodeToString(gen.key1.getEncoded());
	String encodedKey2 = Base64.getEncoder().encodeToString(gen.key2.getEncoded());
	System.out.println(encodedKey1);
	System.out.println(encodedKey2);

	// write object to file
	gen.store("/home/nikhil/Desktop/Project localmachine/ser/keys.ser");
	*/
	// read object from file

	KeyGen gen = new KeyGen();
	gen.generate();
	gen.store("/home/nikhil/Desktop/Project localmachine/ser/keys.ser");
	KeyGen result = KeyGen.read("/home/nikhil/Desktop/Project localmachine/ser/keys.ser");
	String encodedKey1 = Base64.getEncoder().encodeToString(result.key1.getEncoded());
        String encodedKey2 = Base64.getEncoder().encodeToString(result.key2.getEncoded());
	System.out.println(encodedKey1);
	System.out.println(encodedKey2);
	
    }
	
    
}
