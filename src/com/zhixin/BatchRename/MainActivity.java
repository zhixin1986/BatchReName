package com.zhixin.BatchRename;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView lvFiles;
	// ��¼��ǰ�ĸ��ļ���
	File currentParent;
	// ��¼��ǰ·���µ������ļ��е��ļ�����
	File[] currentFiles;
 
	@SuppressLint("SdCardPath")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvFiles = (ListView) this.findViewById(R.id.files);
		// ��ȡϵͳ��SDCard��Ŀ¼
		File root = new File("/mnt/sdcard/");
		// ���SD�����ڵĻ�
		if (root.exists()) {

			currentParent = root;
			currentFiles = root.listFiles();
			// ʹ�õ�ǰĿ¼�µ�ȫ���ļ����ļ��������ListView
			inflateListView(currentFiles);

		}

		lvFiles.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// ����û��������ļ���ֱ�ӷ��أ������κδ���
				if (currentFiles[position].isFile()) {
					// Ҳ���Զ�����չ������ļ���
					return;
				}
				// ��ȡ�û�������ļ��� �µ������ļ�
				File[] tem = currentFiles[position].listFiles();
				if (tem == null || tem.length == 0) {

					Toast.makeText(MainActivity.this, "��ǰ·�����ɷ��ʻ��߸�·����û���ļ�",
							Toast.LENGTH_LONG).show();
				} else {
					// ��ȡ�û��������б����Ӧ���ļ��У���Ϊ��ǰ�ĸ��ļ���
					currentParent = currentFiles[position];
					// ���浱ǰ�ĸ��ļ����ڵ�ȫ���ļ����ļ���
					currentFiles = tem;
					// �ٴθ���ListView
					inflateListView(currentFiles);
				}

			}
		});

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // ����Ƿ��ؼ�,ֱ�ӷ��ص�����
	        if(keyCode == KeyEvent.KEYCODE_BACK){
	        	try {
					if (!currentParent.getCanonicalPath().equals("/mnt/sdcard")) {
						// ��ȡ��һ��Ŀ¼
						currentParent = currentParent.getParentFile();
						// �г���ǰĿ¼�µ������ļ�
						currentFiles = currentParent.listFiles();
						// �ٴθ���ListView
						inflateListView(currentFiles);
						return false;
				}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       
	    }
        return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(1, 1, 1, "��������");
	    menu.add(1, 2, 2, "�����޸�");
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int index=item.getItemId();
		if(index==1)
		{
			Intent intent = new Intent();  
            intent.setClass(MainActivity.this, ReNameActivity.class);  
            intent.putExtra("path", currentParent.getAbsolutePath());
            startActivity(intent);  
		}
		else
			
		{
			
		}
		return true;
	}
	/**
	 * �����ļ������ListView
	 * 
	 * @param files
	 */
	private void inflateListView(File[] files) {

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < files.length; i++) {

			Map<String, Object> listItem = new HashMap<String, Object>();

			if (files[i].isDirectory()) {
				// ������ļ��о���ʾ��ͼƬΪ�ļ��е�ͼƬ
				listItem.put("icon", R.drawable.folder);
			} else {
				listItem.put("icon", R.drawable.file);
			}
			// ���һ���ļ�����
			listItem.put("filename", files[i].getName());

			File myFile = new File(files[i].getName());

			// ��ȡ�ļ�������޸�����
			long modTime = myFile.lastModified();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			// ���һ������޸�����
			listItem.put("modify", dateFormat.format(new Date(modTime)));

			listItems.add(listItem);

		}

		// ����һ��SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, listItems,
				R.layout.list_item,
				new String[] { "filename", "icon", "modify" }, new int[] {
						R.id.file_name, R.id.icon, R.id.file_modify });

		// ������ݼ�
		lvFiles.setAdapter(adapter);

		try {
			this.setTitle(currentParent.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
