package com.search.api;

import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;


public class MainActivity extends  Activity { 
	
	
	private ArrayList<HashMap<String, Object>> Array_Map = new ArrayList<>();
	

	 EditText edittext1;
	 ListView listview1;
	
	 RequestNetwork search_api;
	 RequestNetwork.RequestListener _search_api_request_listener;


	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);

	}
	
	private void initialize(Bundle _savedInstanceState) {
		

		edittext1 = findViewById(R.id.edittext1);
		listview1 = findViewById(R.id.listview1);

		//INTI
		search_api = new RequestNetwork(this);





		edittext1.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();

				_search_on_database(
						"https://hard-fought-custodi.000webhostapp.com/Hotelproject/searchapi.php?c_name="+ _charSeq);

			}

			@Override
			public void afterTextChanged(Editable editable) {

			}

		});





		_search_api_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				if (_response.contains("id")) {
					Array_Map = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					listview1.setAdapter(new Listview1Adapter(Array_Map));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	

	


	// SEARCH API FUNCTION
	public void _search_on_database (final String _search_text) {

		search_api.startRequestNetwork(
				RequestNetworkController.GET,
				_search_text.trim(),
				"",
				_search_api_request_listener);
	}
	



	//LISTVIEW ADAPTER


	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.item_custom, null);
			}
			

			final TextView name =  _view.findViewById(R.id.name);
			final TextView price =  _view.findViewById(R.id.price);
			
			name.setText(Array_Map.get((int)_position).get("c_name").toString());
			price.setText("Price : ₹".concat(Array_Map.get((int)_position).get("c_price").toString().replace(".0","")));
			
			return _view;
		}
	}
	

}
