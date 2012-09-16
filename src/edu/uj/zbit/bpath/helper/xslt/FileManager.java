/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.helper.xslt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManager {

	 public static void copyfile(String srFile, String dtFile){
		    try{
		      File f1 = new File(srFile);
		      File f2 = new File(dtFile);
		      InputStream in = new FileInputStream(f1);
		      
		      //For Append the file.
//		      OutputStream out = new FileOutputStream(f2,true);

		      //For Overwrite the file.
		      OutputStream out = new FileOutputStream(f2);

		      byte[] buf = new byte[1024];
		      int len;
		      while ((len = in.read(buf)) > 0){
		        out.write(buf, 0, len);
		      }
		      in.close();
		      out.close();

		    }
		    catch(FileNotFoundException ex){
		      System.out.println(ex.getMessage() + " in the specified directory.");
		    }
		    catch(IOException e){
		      System.out.println(e.getMessage());      
		    }
		  }
	
}
 