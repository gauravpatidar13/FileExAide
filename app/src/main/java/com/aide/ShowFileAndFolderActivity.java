package com.aide;
import android.app.*;
import android.os.*;
import android.support.v7.widget.RecyclerView;
import android.content.*;
import java.io.*;
import android.support.v7.widget.*;
import java.util.*;

public class ShowFileAndFolderActivity extends Activity 
{
	RecyclerView rv;
	Intent i;
	String path;
	FileAndFolderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_file_and_folder);
		i=getIntent();
		if(i!=null){
		path=i.getStringExtra("path");
		rv=findViewById(R.id.rv);
		initRecView();
		}
    }
	public void initRecView(){
		File file=new File(path);
		File[] list;
		list=file.listFiles();
		List<File> list1=new ArrayList<>();
		for(File f:list){
			list1.add(f);
		}
		adapter=new FileAndFolderAdapter(list1);
		rv.setHasFixedSize(true);
		rv.setLayoutManager(new LinearLayoutManager(this));
		rv.setAdapter(adapter);
	}
}