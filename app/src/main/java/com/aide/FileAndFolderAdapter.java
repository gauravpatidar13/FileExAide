package com.aide;
import android.support.v7.widget.PopupMenu;
import android.view.*;
import java.io.*;
import android.widget.*;
import android.view.View.*;
import android.content.*;
import android.net.*;
import android.graphics.*;
import android.media.*;
import android.provider.*;
import java.util.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.Filter.*;

public class FileAndFolderAdapter extends RecyclerView.Adapter<FileAndFolderAdapter.FileAndFolderViewHolder> implements Filterable
{

	

	List<File> list;
	List<File> listAll;
	public FileAndFolderAdapter(List<File> list)
	{
		this.list = list;
		listAll = new ArrayList<>(list);
	}
	@Override
	public Filter getFilter()
	{
		return filter;
	}

	@Override
	public FileAndFolderAdapter.FileAndFolderViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		View v=LayoutInflater.from(p1.getContext()).inflate(R.layout.layout_file_and_folder_item, p1, false);
		return new FileAndFolderViewHolder(v);
	}

	@Override
	public void onBindViewHolder(FileAndFolderAdapter.FileAndFolderViewHolder p1, int p2)
	{
		File f=list.get(p2);
		String name;
		Bitmap thumb=null;
		name = f.getName();
		p1.txt.setText(name);
		if (f.isFile())
		{		
			//set default file icon
			p1.img.setImageResource(R.drawable.ic_file);
			String ext=getExtensionOfFile(f);
			if(ext!=null&&ext.length()>0){
			if (ext.equalsIgnoreCase("mp4"))
			{
				thumb = getThumbOfVideoFile(f);
				if (thumb != null)
				{
					p1.img.setImageBitmap(thumb);
				}	
			}
			else if (ext.equalsIgnoreCase("jpg"))
			{
				thumb = getThumbOfImageFile(f);
				if (thumb != null)
				{
					p1.img.setImageBitmap(thumb);
				}	
			}
}
		}
		else
			p1.img.setImageResource(R.drawable.folder);

	}

	@Override
	public int getItemCount()
	{
		return list.size();
	}
	Filter filter=new Filter(){

		@Override
		protected Filter.FilterResults performFiltering(CharSequence p1)
		{
			List<File> filteredList=new ArrayList<>();
			if(p1.toString().isEmpty()){
				filteredList.addAll(listAll);
			}else{
				for(File f:listAll){
					if(f.getName().toLowerCase().toString().contains(p1.toString().toLowerCase())){
						filteredList.add(f);
					}
				}
			}
			FilterResults fr=new FilterResults();
			fr.values=filteredList;
			return fr;
		}

		@Override
		protected void publishResults(CharSequence p1, Filter.FilterResults p2)
		{
			list.clear();
			list.addAll((Collection<? extends File>)p2.values);
			notifyDataSetChanged();
		}
		
	
};
	class FileAndFolderViewHolder extends RecyclerView.ViewHolder
	{
		ImageView img;TextView txt;
		public FileAndFolderViewHolder(View v)
		{
			super(v);
			img = v.findViewById(R.id.img_file_and_folder);
			txt = v.findViewById(R.id.txt_file_and_folder);
			v.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						int p=getAdapterPosition();
						File f=list.get(p);
						if (f.isDirectory())
						{
							Intent i=new Intent(p1.getContext(), ShowFileAndFolderActivity.class);
							i.putExtra("path", f.getAbsolutePath());
							p1.getContext().startActivity(i);
						}
						else
						{

							String ext=getExtensionOfFile(f);			
							if(ext!=null&&ext.length()>0){
							if (ext.equalsIgnoreCase("mp4"))
							{
								playMP4VideoFile(p1.getContext(), f);
							}
							else if (ext.equalsIgnoreCase("jpg"))
							{
								openImageFile(p1.getContext(), f);
							}
}
						}


					}


				});
			v.setOnLongClickListener(new OnLongClickListener(){

					@Override
					public boolean onLongClick(final View p1)
					{
						final int p=getAdapterPosition();
						final File f=list.get(p);
						if (f.isFile())
						{
							PopupMenu pm=new PopupMenu(p1.getContext(), p1);
							pm.getMenu().add("Delete");
							pm.getMenu().add("Rename");
							pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

									@Override
									public boolean onMenuItemClick(MenuItem i1)
									{
										Toast.makeText(p1.getContext(), "" + i1.getTitle().equals("Delete"), Toast.LENGTH_SHORT).show();
										if (i1.getTitle().equals("Delete"))
										{
											try
											{
												boolean deleted=f.delete();
												if (deleted)
												{
													Toast.makeText(p1.getContext(), "file deleted", Toast.LENGTH_SHORT).show();
													list.remove(getAdapterPosition());
													notifyItemRemoved(getAdapterPosition());
													notifyDataSetChanged();
												}
											}
											catch (Exception e)
											{
												StringBuilder sb=new StringBuilder();
												for (StackTraceElement st: e.getStackTrace())
												{

													sb.append(st.toString());
												}

												//Toast.makeText(p1.getContext(),"Exception->"+sb,Toast.LENGTH_SHORT).show();
												writeLogException(p1.getContext(), sb);
											}}
										return true;
									}


								});
							pm.show();
						}




						return true;
					}


				});
		}


	}

	public Bitmap getThumbOfVideoFile(File f)
	{
		Bitmap thumb = ThumbnailUtils.createVideoThumbnail
		(f.getAbsolutePath(),
		 MediaStore.Images.Thumbnails.MINI_KIND);
		return thumb;	
	}

	public Bitmap getThumbOfImageFile(File f)
	{
		Bitmap thumb=ThumbnailUtils.createImageThumbnail(f.getAbsolutePath(),
														 MediaStore.Images.Thumbnails.MINI_KIND);
		if (thumb != null)
		{
			return thumb;
		}
		return null;
	}
	public String getExtensionOfFile(File f)
	{
		String fileName = f.toString();
		int index = fileName.lastIndexOf('.');
		if (index > 0)
		{
			String extension = fileName.substring(index + 1);
			return extension;
		}
		return null;
	}
	public void playMP4VideoFile(Context p1, File f)
	{
		Intent tostart = new Intent(Intent.ACTION_VIEW);
		tostart.setDataAndType(Uri.parse(f.getAbsolutePath()), "video/*");
		p1.startActivity(tostart);
	}
	public void openImageFile(Context p1, File f)
	{
		Intent tostart = new Intent(Intent.ACTION_VIEW);
		tostart.setDataAndType(Uri.parse(f.getAbsolutePath()), "image/*");
		p1.startActivity(tostart);
	}
	public void writeLogException(Context c, StringBuilder sb)
	{
		try
		{
			File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "log.txt");
			BufferedWriter bw=new BufferedWriter(new FileWriter(file));
			bw.write(sb.toString());
			bw.flush();
			bw.close();
			Toast.makeText(c, "check log file for exception detsils", Toast.LENGTH_SHORT).show();
		}
		catch (IOException ee)
		{
			Toast.makeText(c, ee.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}