import java.io.File;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;

import java.io.FileNotFoundException;

public class WGet {
	public static void main(String args[])
	{
		if(args.length < 2)
		{
			System.out.println("Usage: WGet [url] [filename]");
			System.exit(-1);
		}
		String addr = args[0];
		String filename = args[1];
		File f = new File(filename);
		if(f.exists())
		{
			System.out.println("The file "+filename+" already exists, please change the file name to wirte.");
			System.exit(-1);
		}
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(f);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}
		
		try {
			URL url = new URL(addr);
			HttpURLConnection urlcon = (HttpURLConnection)url.openConnection();
	        urlcon.connect(); 
	        InputStream IN = urlcon.getInputStream();
	        int tmp;
	        while((tmp = IN.read()) != -1)
	        {
	        	fout.write(tmp);
	        }
	        
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
