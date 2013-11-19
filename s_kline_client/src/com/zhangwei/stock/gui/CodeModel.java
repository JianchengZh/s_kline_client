package com.zhangwei.stock.gui;


import java.util.ArrayList;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: Feb 6, 2008
 * Time: 10:25:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodeModel
{
    private ArrayList list = new ArrayList();

    public CodeModel()
    {
        try {
            FileInputStream fis = new FileInputStream( "D:\\stock\\thrid\\com\\javasrc\\ide\\CodePanel.java" );
            BufferedReader br = new BufferedReader( new InputStreamReader( fis ) );
            String line = br.readLine();
            while( line != null )
            {
                list.add( line );
                line = br.readLine();
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        /*
        list.add( "package com.javasrc.ide;" );
        list.add( "" );
        list.add( "public class CodeModel {" );
        list.add( "" );
        list.add( "   public CodeModel() {" );
        list.add( "      for( int i=0; i<10; i++ ) {" );
        list.add( "        // Do something cool" );
        list.add( "      }" );
        list.add( "      System.out.println(\"This is a test\");" );
        list.add( "   }" );
        list.add( "}" );
        */
    }

    public int getLineCount()
    {
        return list.size();
    }

    public String getLine( int i )
    {
        return ( String )list.get( i );
    }

}

