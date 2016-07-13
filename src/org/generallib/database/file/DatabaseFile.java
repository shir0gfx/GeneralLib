package org.generallib.database.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.CharSet;
import org.generallib.database.Database;

public class DatabaseFile<T> extends Database<T> {
	private final Type type;
	private File folder;
	
	public DatabaseFile(File folder, Type type){
		this.folder = folder;
		this.type = type;
		
		folder.mkdirs();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T load(String key, T def) {
		File file = new File(folder, key);
		if(!file.exists())
			return def;
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		T result = def;
		String ser = "";
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, Charset.forName("UTF-8").newDecoder());
			br = new BufferedReader(isr);
			
			String buff;
			while((buff = br.readLine()) != null)
				ser += buff;
			result = (T) deserialize(ser, type);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}

	@Override
	public synchronized void save(String key, T value) {
		File dest = new File(folder, key);
		if(value == null){
			dest.delete();
			return;
		}
		
		File file = new File(folder, key+"_tmp");
		
		FileChannel fc = null;
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		FileLock lock = null;
		
		try {
			fos = new FileOutputStream(file);
			fc = fos.getChannel();
			bw = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-8").newEncoder()));
			
			lock = fc.lock();
			
			String ser = serialize(value, type);
			bw.write(ser);
			
			lock.release();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(lock != null)
					lock.release();
				bw.close();
				fc.close();
				fos.close();
				
				Files.move(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
				file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Set<String> getKeys() {
		Set<String> keys = new HashSet<String>();
		
		for(File file : folder.listFiles()){
			keys.add(file.getName());
		}
		
		return keys;
	}

	@Override
	public boolean has(String key) {
		for(String fileName : folder.list()){
			if(fileName.equalsIgnoreCase(key))
				return true;
		}
		
		return false;
	}
}
