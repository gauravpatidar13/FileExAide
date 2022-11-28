package com.aide;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.pm.*;
import android.content.*;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.*;

public class MainActivity extends AppCompatActivity
{
	Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
    }
	public void clickMe(View v)
	{
	
		if (checkPermit())
		{
			String path=Environment.getExternalStorageDirectory().getAbsolutePath();
			//Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
			Intent i=new Intent(this,ShowFileAndFolderActivity.class);
			i.putExtra("path",path);
			startActivity(i);
			finish();
		}
		else
		{
			if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE))
			{
				requestPermit();
			}
			else
			{
				Toast.makeText(this, R.string.permit, Toast.LENGTH_SHORT).show();

			}
		}
	}
	public void requestPermit()
	{
		requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
	}
	public boolean checkPermit()
	{
		if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
		{
			return false;
		}
		else return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main,menu);
		return true;
	}
	
}