package com.aide;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

public class ShowFileAndFolderActivity extends AppCompatActivity implements View.OnClickListener
{
	RecyclerView rv;
	Intent i;
	String path;
	Toolbar toolbar;
	BottomSheetDialog bsd;
	FileAndFolderAdapter adapter;
	FloatingActionButton fab;
	List<File> list1=new ArrayList<>();
	List<File> archived=new ArrayList<>();
	@Override
	public void onClick(View p1)
	{
		Toast toast=new Toast(this);
		View vi=LayoutInflater.from(this).inflate(R.layout.layout_custom_toast,null);
		TextView txt=vi.findViewById(R.id.txt_toast);
		
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM,12,12);
		
		switch(p1.getId()){
			case R.id.layout_cut:
				txt.setText("Hello, Cut");
				toast.setView(vi);
				toast.show();
				bsd.hide();
				break;
			case R.id.layout_copy:
				txt.setText("Hello, Copy");
				toast.setView(vi);
				toast.show();
				bsd.hide();
				break;
			case R.id.layout_paste:
				txt.setText("Hello, Paste");
				toast.setView(vi);
				toast.show();
				bsd.hide();
				break;
		}
	}
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_file_and_folder);
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		fab=(FloatingActionButton)findViewById(R.id.fab);
		setSupportActionBar(toolbar);
		i=getIntent();
		if(i!=null){
		path=i.getStringExtra("path");
		rv=(RecyclerView)findViewById(R.id.rv);
		initRecView();
			fab.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						showBottomSheet();
					}
		});
		}
	
    }
	ItemTouchHelper.SimpleCallback sc=new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

		@Override
		public boolean onMove(RecyclerView p1, RecyclerView.ViewHolder p2, RecyclerView.ViewHolder p3)
		{
			
			return false;
		}

		@Override
		public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
		{
			/*new RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
			.addSwipeLeftBackgroundColor(ContextCompat.getColor(ShowFileAndFolderActivity.this,R.color.colorSwipeLeft))
			.addSwipeRightBackgroundColor(ContextCompat.getColor(ShowFileAndFolderActivity.this,R.color.colorSwipeRight))
				.addSwipeLeftActionIcon(R.drawable.trash)
				.addSwipeRightActionIcon(R.drawable.archive)
				.create()
				.decorate();*/
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
		}
		

		@Override
		public void onSwiped(RecyclerView.ViewHolder p1, int p2)
		{ 
		final int pos=p1.getAdapterPosition();
		final File deleted;
		final File arc;
			switch(p2){
				case ItemTouchHelper.LEFT:
					deleted=list1.get(pos);
					list1.remove(pos);
					adapter.notifyItemRemoved(pos);
					Snackbar.make(rv,deleted.getName()+", deleted",Snackbar.LENGTH_LONG)
						.setAction("Undo", new View.OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								list1.add(pos,deleted);
								adapter.notifyItemInserted(pos);
							}
							
						
					}).show();
					break;
				case ItemTouchHelper.RIGHT:
					arc=list1.get(pos);
					archived.add(arc);
					list1.remove(pos);
					adapter.notifyItemRemoved(pos);
					Snackbar.make(rv,arc.getName()+", archived",Snackbar.LENGTH_LONG)
						.setAction("Undo", new View.OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								archived.remove(archived.lastIndexOf(arc));
								list1.add(pos,arc);
								adapter.notifyItemInserted(pos);
							}


						}).show();
					break;
			}
		}

	
};
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_search,menu);
		MenuItem mi= menu.findItem(R.id.action_search);
		SearchView sv=(SearchView)mi.getActionView();
		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

				@Override
				public boolean onQueryTextSubmit(String p1)
				{

					return false;
				}

				@Override
				public boolean onQueryTextChange(String p1)
				{
					adapter.getFilter().filter(p1);
					return true;
				}


			});
		return true;
	}
	
	public void initRecView(){
		File file=new File(path);
		File[] list;
		list=file.listFiles();
		
		for(File f:list){
			list1.add(f);
		}
		
		adapter=new FileAndFolderAdapter(list1);
		rv.setHasFixedSize(true);
		rv.setLayoutManager(new LinearLayoutManager(this));
		rv.setAdapter(adapter);
		ItemTouchHelper h=new ItemTouchHelper(sc);
		h.attachToRecyclerView(rv);
	}
	public void showBottomSheet(){
		LinearLayout lay_cut,lay_copy,lay_paste;
		if(bsd==null){
			bsd=new BottomSheetDialog(this);
			View vi=LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet,null);
			lay_cut=vi.findViewById(R.id.layout_cut);
			lay_copy=vi.findViewById(R.id.layout_copy);
			lay_paste=vi.findViewById(R.id.layout_paste);
			lay_cut.setOnClickListener(this);
			lay_copy.setOnClickListener(this);
			lay_paste.setOnClickListener(this);
			bsd.setContentView(vi);
			bsd.create();
			bsd.show();
		}else{
			bsd.create();
			bsd.show();
		}
	}
}