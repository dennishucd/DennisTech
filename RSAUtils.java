import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtils {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String path = "c/";
		generatePairKey(path);
		
		Key pubKey = readKey(path+"public.key");
		Key priKey = readKey(path+"public.key");
		
		byte[] en = null;
		try {
			en = encrypt("中华人民共和国".getBytes("UTF-8"), pubKey);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		if (en != null) {
			printBytes(en);
			
			byte[] de = decrypt(en, priKey);
			
			printBytes(de);
		}
	}
	
	public static void printBytes(byte[] en) {
		try {
			System.out.println(new String(en, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * generate the public and private pair key and save to given path
	 * @param path eg: "c:/"
	 */
	public static void generatePairKey(String path) {
		// 产生钥匙对
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			
			saveKey(privateKey, path+"private.key");
			saveKey(publicKey, path+"public.key");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * encrypt the content with given public key
	 * @param content
	 * @param pubKey
	 * @return
	 */
	private static byte[] encrypt(byte content[], Key pubKey) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			
			return cipher.doFinal(content);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * decrypt the content with the private key
	 * @param content
	 * @param priKey
	 * @return
	 */
	private static byte[] decrypt(byte content[], Key priKey) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			
			return cipher.doFinal(content);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static void saveKey(Key key, String keyName){
		FileOutputStream foskey;
		try {
			foskey = new FileOutputStream(keyName);
			ObjectOutputStream oos = new ObjectOutputStream(foskey);
			oos.writeObject(key);
			oos.close();
			foskey.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Key readKey(String fullPath){
		FileInputStream fiskey;
		try {
			fiskey = new FileInputStream(fullPath);
			ObjectInputStream oiskey = new ObjectInputStream(fiskey);
			Key key = (Key) oiskey.readObject();
			oiskey.close();
			fiskey.close();
			
			return key;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
